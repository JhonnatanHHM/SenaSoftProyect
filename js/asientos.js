// Add custom styles for seat selection
(function () {
  const style = document.createElement("style");
  style.innerHTML = `
    .seat {
      width: 3.5rem;
      height: 3.5rem;
      border-radius: 0.75rem;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 700;
      font-size: 0.875rem;
      cursor: pointer;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      position: relative;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      border: 2px solid transparent;
    }
    
    .seat.available {
      background-color: #f1f5f9;
      color: #334155;
      border-color: #e2e8f0;
    }
    
    .seat.available:hover {
      background-color: #e2e8f0;
      transform: translateY(-3px);
      box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
    }
    
    .seat.occupied {
      background-color: #64748b;
      color: #cbd5e1;
      cursor: not-allowed;
      opacity: 0.7;
    }
    
    .seat.selected {
      background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
      color: white;
      border-color: #1d4ed8;
      transform: translateY(-3px);
      box-shadow: 0 6px 12px rgba(37, 99, 235, 0.3);
    }
    
    .seat.emergency {
      background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
      color: white;
      border-color: #c2410c;
    }
    
    .seat.emergency::after {
      content: "!";
      position: absolute;
      top: -5px;
      right: -5px;
      background: #ef4444;
      color: white;
      border-radius: 50%;
      width: 20px;
      height: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 0.75rem;
      font-weight: bold;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    }
    
    #seatRows {
      grid-template-columns: 1fr auto 1fr !important;
    }
  `;
  document.head.appendChild(style);
})();

// Global variables
let selectedSeats = [];
const maxSeats = 5;
let seatsData = [];
let currentFlight = null;

// Function to load selected seats from localStorage when page loads
function loadSelectedSeatsFromLocalStorage() {
  try {
    // Get selected flight ID from sessionStorage
    const flightId = sessionStorage.getItem("selectedFlightId");
    
    // Clear any generic selectedSeats to avoid loading seats from other flights
    localStorage.removeItem("selectedSeats");

    // Only load seats for the specific flight
    if (flightId) {
      const savedSeats = localStorage.getItem(`selectedSeats_flight_${flightId}`);
      if (savedSeats) {
        selectedSeats = JSON.parse(savedSeats);
        // Validate that seats belong to the current flight's aircraft
        const storedVuelos = localStorage.getItem("vuelosCache");
        if (storedVuelos) {
          const vuelosCache = JSON.parse(storedVuelos);
          const flightData = vuelosCache.find((vuelo) => vuelo.idVuelo == flightId);
          if (flightData && flightData.avion && flightData.avion.asientos) {
            const validSeatIds = flightData.avion.asientos.map(seat => seat.idAsiento);
            selectedSeats = selectedSeats.filter(seat => validSeatIds.includes(seat.idAsiento));
          }
        }
        console.log("Loaded selected seats for flight ID", flightId, ":", selectedSeats);
      } else {
        selectedSeats = []; // Ensure no seats from other flights are used
        console.log("No seats found for flight ID", flightId);
      }
    } else {
      selectedSeats = [];
      console.log("No flight ID found, initializing empty selectedSeats");
    }
  } catch (e) {
    console.error("Error loading selected seats from localStorage:", e);
    selectedSeats = [];
  }
}

// Helper to read token from sessionStorage
function getToken() {
  try {
    return sessionStorage.getItem("token") || null;
  } catch (e) {
    console.warn("getToken: no se pudo leer sessionStorage:", e);
    return null;
  }
}

// Function to fetch seats from the API or cache
async function fetchSeats() {
  const loadingIndicator = document.getElementById("loadingIndicator");
  const errorMessage = document.getElementById("errorMessage");
  const errorMessageText = document.getElementById("errorMessageText");
  const seatsContent = document.getElementById("seatsContent");

  // Get flight ID from sessionStorage
  const flightId = sessionStorage.getItem("selectedFlightId");
  console.log("Fetching seats for flight ID:", flightId);

  // Show loading indicator
  loadingIndicator.classList.remove("hidden");
  errorMessage.classList.add("hidden");
  seatsContent.classList.add("hidden");

  try {
    // Check if flight ID exists
    if (!flightId) {
      throw new Error("No se ha seleccionado un vuelo válido.");
    }

    // Load previously selected seats for this flight only
    loadSelectedSeatsFromLocalStorage();

    // Try to get flight data from cache first
    let flightData = null;
    try {
      const storedVuelos = localStorage.getItem("vuelosCache");
      if (storedVuelos) {
        const vuelosCache = JSON.parse(storedVuelos);
        flightData = vuelosCache.find((vuelo) => vuelo.idVuelo == flightId);
        currentFlight = flightData;
      }
    } catch (parseError) {
      console.warn("No se pudieron cargar los vuelos desde localStorage:", parseError);
    }

    // If we have flight data with seats, use it
    if (flightData && flightData.avion && flightData.avion.asientos) {
      seatsData = flightData.avion.asientos.map((seat) => ({
        idAsiento: seat.idAsiento,
        nombre: seat.nombre,
        estado: seat.estado || "DISPONIBLE",
        precio: seat.precio || 0,
      }));

      console.log("Using cached seats data for flight ID", flightId, ":", seatsData);

      // Hide loading indicator
      loadingIndicator.classList.add("hidden");

      // Render seats
      renderSeats(seatsData);
      seatsContent.classList.remove("hidden");

      // Update flight information in the header
      updateFlightInfo(flightData);

      // Restore selected seats UI
      restoreSelectedSeatsUI();
      return;
    }

    // Otherwise, fetch from API
    const response = await fetch(
      `https://senasoftproyect.onrender.com/api/vuelos/${flightId}/asientos`
    );

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error("No se encontraron asientos para este vuelo.");
      } else if (response.status === 500) {
        throw new Error("Error en el servidor. Por favor, inténtalo más tarde.");
      } else {
        throw new Error(`Error HTTP: ${response.status} - ${response.statusText}`);
      }
    }

    const rawSeatsData = await response.json();
    console.log("Fetched seats data from API for flight ID", flightId, ":", rawSeatsData);

    // Ensure all seats have proper estado and price data
    seatsData = rawSeatsData.map((seat) => ({
      idAsiento: seat.idAsiento,
      nombre: seat.nombre,
      estado: seat.estado || "DISPONIBLE",
      precio: seat.precio || 0,
    }));

    // Hide loading indicator
    loadingIndicator.classList.add("hidden");

    // Render seats
    renderSeats(seatsData);
    seatsContent.classList.remove("hidden");

    // Update flight information in the header
    updateFlightInfoFromId(flightId);

    // Restore selected seats UI
    restoreSelectedSeatsUI();
  } catch (error) {
    console.error("Error fetching seats:", error);
    loadingIndicator.classList.add("hidden");
    errorMessage.classList.remove("hidden");

    // Provide more specific error messages
    if (error.message.includes("Failed to fetch")) {
      errorMessageText.innerHTML = `
        No se pudo conectar con el servidor. Por favor, verifica que:
        <ul class="list-disc list-inside mt-2">
          <li>El servidor backend esté en ejecución</li>
          <li>La URL de la API sea correcta</li>
          <li>Tienes conexión a internet</li>
        </ul>
        <p class="mt-2">Mostrando datos de ejemplo para demostración:</p>
      `;

      // Show sample seats data for demonstration
      const sampleSeats = generateSampleSeats();
      console.log("Showing sample seats:", sampleSeats);
      renderSeats(sampleSeats);
      seatsContent.classList.remove("hidden");

      // Restore selected seats UI
      restoreSelectedSeatsUI();
    } else {
      errorMessageText.innerHTML = error.message;
    }
  }
}

// Function to update flight information in the header
function updateFlightInfo(flightData) {
  if (!flightData) return;

  const flightNumberElement = document.getElementById("flightNumber");
  const flightRouteElement = document.getElementById("flightRoute");
  const departureTimeElement = document.getElementById("departureTime");
  const arrivalTimeElement = document.getElementById("arrivalTime");
  const flightDurationElement = document.getElementById("flightDuration");

  if (flightNumberElement) {
    flightNumberElement.textContent = `Vuelo #${flightData.idVuelo}`;
  }

  if (flightRouteElement) {
    const origin =
      flightData.ciudadSalida?.nombre ||
      flightData.lugarSalida ||
      flightData.ciudad_salida ||
      "Origen";
    const destination =
      flightData.ciudadLlegada?.nombre ||
      flightData.lugarLlegada ||
      flightData.ciudad_llegada ||
      "Destino";
    flightRouteElement.textContent = `${origin} a ${destination}`;
  }

  // Format and display departure time
  if (departureTimeElement) {
    let departureDateTime = null;
    if (flightData.fechaHoraSalida) {
      departureDateTime = new Date(flightData.fechaHoraSalida);
    } else if (flightData.fechaSalida && flightData.horaSalida) {
      departureDateTime = new Date(
        `${flightData.fechaSalida}T${flightData.horaSalida}`
      );
    }

    if (departureDateTime && !isNaN(departureDateTime.getTime())) {
      const hours = departureDateTime.getHours().toString().padStart(2, "0");
      const minutes = departureDateTime.getMinutes().toString().padStart(2, "0");
      departureTimeElement.textContent = `${hours}:${minutes}`;
    } else {
      departureTimeElement.textContent = "--:--";
    }
  }

  // Format and display arrival time
  if (arrivalTimeElement) {
    let arrivalDateTime = null;
    if (flightData.fechaHoraLlegada) {
      arrivalDateTime = new Date(flightData.fechaHoraLlegada);
    } else if (flightData.fechaLlegada && flightData.horaLlegada) {
      arrivalDateTime = new Date(
        `${flightData.fechaLlegada}T${flightData.horaLlegada}`
      );
    }

    if (arrivalDateTime && !isNaN(arrivalDateTime.getTime())) {
      const hours = arrivalDateTime.getHours().toString().padStart(2, "0");
      const minutes = arrivalDateTime.getMinutes().toString().padStart(2, "0");
      arrivalTimeElement.textContent = `${hours}:${minutes}`;
    } else {
      arrivalTimeElement.textContent = "--:--";
    }
  }

  // Calculate and display flight duration
  if (flightDurationElement) {
    let departureDateTime = null;
    let arrivalDateTime = null;

    if (flightData.fechaHoraSalida) {
      departureDateTime = new Date(flightData.fechaHoraSalida);
    } else if (flightData.fechaSalida && flightData.horaSalida) {
      departureDateTime = new Date(
        `${flightData.fechaSalida}T${flightData.horaSalida}`
      );
    }

    if (flightData.fechaHoraLlegada) {
      arrivalDateTime = new Date(flightData.fechaHoraLlegada);
    } else if (flightData.fechaLlegada && flightData.horaLlegada) {
      arrivalDateTime = new Date(
        `${flightData.fechaLlegada}T${flightData.horaLlegada}`
      );
    }

    if (
      departureDateTime &&
      arrivalDateTime &&
      !isNaN(departureDateTime.getTime()) &&
      !isNaN(arrivalDateTime.getTime())
    ) {
      const durationMs = arrivalDateTime - departureDateTime;
      const durationHours = Math.floor(durationMs / (1000 * 60 * 60));
      const durationMinutes = Math.floor(
        (durationMs % (1000 * 60 * 60)) / (1000 * 60)
      );
      flightDurationElement.textContent = `${durationHours}h ${durationMinutes}m`;
    } else {
      flightDurationElement.textContent = "--h --m";
    }
  }

  // Save flight data to localStorage so confirmation page can access it
  localStorage.setItem("currentFlight", JSON.stringify(flightData));
}

// Function to update flight information when we only have the ID
function updateFlightInfoFromId(flightId) {
  const flightNumberElement = document.getElementById("flightNumber");
  const flightRouteElement = document.getElementById("flightRoute");
  const departureTimeElement = document.getElementById("departureTime");
  const arrivalTimeElement = document.getElementById("arrivalTime");
  const flightDurationElement = document.getElementById("flightDuration");

  if (flightNumberElement) {
    flightNumberElement.textContent = `Vuelo #${flightId}`;
  }

  if (flightRouteElement) {
    flightRouteElement.textContent = "Ciudad de México a Cancún";
  }

  if (departureTimeElement) departureTimeElement.textContent = "--:--";
  if (arrivalTimeElement) arrivalTimeElement.textContent = "--:--";
  if (flightDurationElement) flightDurationElement.textContent = "--h --m";
}

// Function to generate sample seat data for demonstration
function generateSampleSeats() {
  const seats = [];
  const rows = 10;
  const letters = ["A", "B", "C", "D"];

  for (let row = 1; row <= rows; row++) {
    for (let i = 0; i < letters.length; i++) {
      const estado = Math.random() > 0.7 ? "OCUPADO" : "DISPONIBLE";
      let precio = 50;
      if (row <= 3) precio += 30;
      if (row >= 8) precio += 10;
      if (letters[i] === "A" || letters[i] === "D") precio += 15;
      if (letters[i] === "B" || letters[i] === "C") precio += 10;

      seats.push({
        idAsiento: (row - 1) * 4 + i + 1,
        nombre: `${row}${letters[i]}`,
        estado: estado,
        precio: precio,
      });
    }
  }

  if (seats.length > 0) {
    if (seats[8]) seats[8].estado = "EMERGENCIA";
    if (seats[11]) seats[11].estado = "EMERGENCIA";
    if (seats[28]) seats[28].estado = "EMERGENCIA";
    if (seats[31]) seats[31].estado = "EMERGENCIA";
  }

  return seats;
}

// Function to render seats
function renderSeats(seats) {
  console.log("Rendering seats:", seats);
  const seatRows = document.getElementById("seatRows");

  if (!seatRows) {
    console.warn("Element #seatRows not found in DOM");
    return;
  }

  seatRows.innerHTML = "";

  const rows = [];
  for (let i = 0; i < seats.length; i += 4) {
    rows.push(seats.slice(i, i + 4));
  }

  rows.forEach((rowSeats, rowIndex) => {
    const rowContainer = document.createElement("div");
    rowContainer.className = "contents";

    const leftSide = document.createElement("div");
    leftSide.className = "flex justify-around";

    const rightSide = document.createElement("div");
    rightSide.className = "flex justify-around";

    const rowLabel = document.createElement("div");
    rowLabel.className =
      "text-gray-500 dark:text-gray-400 font-bold self-center text-lg";
    rowLabel.textContent = rowIndex + 1;

    rowSeats.forEach((seat, seatIndex) => {
      const seatElement = document.createElement("div");
      const isSelected = selectedSeats.some(
        (s) => s.idAsiento === seat.idAsiento
      );

      let seatClass = "seat ";
      if (seat.estado === "OCUPADO") {
        seatClass += "occupied";
      } else if (seat.estado === "EMERGENCIA") {
        seatClass += "emergency";
      } else if (isSelected) {
        seatClass += "selected";
      } else {
        seatClass += "available";
      }

      seatElement.className = seatClass;
      seatElement.textContent = seat.nombre;
      seatElement.dataset.id = seat.idAsiento;
      seatElement.dataset.price = seat.precio || 0;
      seatElement.dataset.estado = seat.estado;

      if (seat.estado === "DISPONIBLE" || seat.estado === "EMERGENCIA") {
        seatElement.addEventListener("click", () => {
          toggleSeatSelection(seat);
        });
      }

      if (seatIndex < 2) {
        leftSide.appendChild(seatElement);
      } else {
        rightSide.appendChild(seatElement);
      }
    });

    rowContainer.appendChild(leftSide);
    rowContainer.appendChild(rowLabel);
    rowContainer.appendChild(rightSide);

    seatRows.appendChild(rowContainer);
  });
}

// Function to restore selected seats UI after rendering
function restoreSelectedSeatsUI() {
  console.log("Restoring selected seats UI:", selectedSeats);

  selectedSeats.forEach((seat) => {
    const seatElement = document.querySelector(
      `.seat[data-id="${seat.idAsiento}"]`
    );
    if (seatElement) {
      seatElement.classList.remove("available", "occupied", "emergency");
      seatElement.classList.add("selected");
    }
  });

  updateSummary();
}

// Function to toggle seat selection
function toggleSeatSelection(seat) {
  console.log("Toggle seat selection called for seat:", seat);

  if (!seat || !seat.idAsiento || isNaN(seat.idAsiento)) {
    console.error("Invalid seat data:", seat);
    return;
  }

  const seatElement = document.querySelector(
    `.seat[data-id="${seat.idAsiento}"]`
  );

  if (!seatElement) {
    console.log("Seat element not found for seat ID:", seat.idAsiento);
    return;
  }

  const selectedIndex = selectedSeats.findIndex(
    (s) => s.idAsiento === seat.idAsiento
  );

  console.log("Selected index:", selectedIndex);
  console.log("Current selected seats:", selectedSeats);

  if (selectedIndex !== -1) {
    console.log("Deselecting seat:", seat.idAsiento);
    selectedSeats.splice(selectedIndex, 1);
    seatElement.classList.remove("selected");
    seatElement.classList.add("available");

    updateSeatStatusOnServer(seat.idAsiento, "DISPONIBLE");
  } else {
    if (selectedSeats.length >= maxSeats) {
      console.log("Maximum seats reached");
      alert(`Solo puedes seleccionar un máximo de ${maxSeats} asientos.`);
      return;
    }

    console.log("Selecting seat:", seat.idAsiento);
    selectedSeats.push({
      idAsiento: seat.idAsiento,
      nombre: seat.nombre,
      precio: seat.precio,
      estado: seat.estado,
    });

    seatElement.classList.remove("available", "occupied", "emergency");
    seatElement.classList.add("selected");

    updateSeatStatusOnServer(seat.idAsiento, "SELECCIONADO");
  }

  saveSelectedSeatsToLocalStorage();
  updateSummary();
  console.log("Updated selected seats:", selectedSeats);
}

// Function to save selected seats to localStorage
function saveSelectedSeatsToLocalStorage() {
  try {
    const flightId = sessionStorage.getItem("selectedFlightId");
    if (flightId) {
      localStorage.setItem(
        `selectedSeats_flight_${flightId}`,
        JSON.stringify(selectedSeats)
      );
      console.log(
        `Selected seats saved to localStorage with flight key: selectedSeats_flight_${flightId}`
      );

      const reservationData = {
        selectedSeats: selectedSeats,
        flightId: flightId,
        timestamp: new Date().toISOString(),
      };
      localStorage.setItem("currentReservation", JSON.stringify(reservationData));
    }
  } catch (e) {
    console.error("Error saving selected seats to localStorage:", e);
  }
}

// Function to update seat status on server
async function updateSeatStatusOnServer(seatId, status) {
  const token = getToken();
  const userId = parseInt(sessionStorage.getItem("id")) || 1;

  function findSeatElement(id) {
    const seatElements = document.querySelectorAll(".seat");
    for (let element of seatElements) {
      if (parseInt(element.dataset.id) === id) return element;
    }
    return null;
  }

  if (!seatId || isNaN(seatId)) {
    console.error("Invalid seat ID:", seatId);
    return;
  }

  const seatElement = findSeatElement(seatId);
  if (seatElement) seatElement.classList.add("updating");

  const idAsiento = seatId;
  const idUsuario = userId;

  try {
    const url = `https://senasoftproyect.onrender.com/api/asientos/estado/${seatId}/${userId}`;
    const body = JSON.stringify({
      estado: status,
      usuarioReservado: idUsuario,
    });

    const options = {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body,
    };

    if (token) options.headers["Authorization"] = `Bearer ${token}`;

    console.log("PATCH options:", options, "url:", url);

    let response;
    try {
      response = await fetch(url, options);
    } catch (networkErr) {
      console.error("Network error during fetch:", networkErr);
      throw networkErr;
    }

    if (!response.ok) {
      throw new Error(`HTTP ${response.status} ${response.statusText}`);
    }

    console.log(`Seat ${seatId} status updated to ${status} on server`);
  } catch (err) {
    console.error("Error updating seat status:", err);

    console.warn("Aplicando cambio de estado en modo local (fallback).");
    const index = seatsData.findIndex((s) => s.idAsiento === seatId);
    const fallbackPayload = {
      idAsiento: seatId,
      nombre: index !== -1 ? seatsData[index].nombre : `S${seatId}`,
      precio: index !== -1 ? seatsData[index].precio || 0 : 0,
      estado: status,
      idAvion:
        currentFlight && currentFlight.avion
          ? currentFlight.avion.idAvion
          : null,
      usuarioReservado: userId,
    };

    if (index !== -1) {
      seatsData[index] = Object.assign({}, seatsData[index], {
        estado: status,
      });
    } else {
      seatsData.push({
        idAsiento: fallbackPayload.idAsiento,
        nombre: fallbackPayload.nombre,
        precio: fallbackPayload.precio,
        estado: fallbackPayload.estado,
      });
    }

    if (seatElement) {
      seatElement.classList.remove("available", "occupied", "emergency");
      if (status === "SELECCIONADO") {
        seatElement.classList.add("selected");
      } else if (status === "DISPONIBLE") {
        seatElement.classList.add("available");
      } else if (status === "OCUPADO") {
        seatElement.classList.add("occupied");
      }
      seatElement.dataset.estado = status;
    }

    try {
      localStorage.setItem("seatsFallback", JSON.stringify(seatsData));
    } catch (e) {
      console.warn("No se pudo guardar seatsFallback en localStorage:", e);
    }

    alert(
      "No se pudo actualizar el asiento en el servidor. El cambio se aplicó localmente para la demostración."
    );
  } finally {
    if (seatElement) seatElement.classList.remove("updating");
  }
}

// Function to remove a seat from selection
function removeSeat(seatId) {
  const selectedIndex = selectedSeats.findIndex((s) => s.idAsiento === seatId);

  if (selectedIndex !== -1) {
    const removedSeat = selectedSeats.splice(selectedIndex, 1)[0];
    const seatElements = document.querySelectorAll(".seat");
    for (let element of seatElements) {
      if (parseInt(element.dataset.id) === seatId) {
        element.classList.remove("selected");
        element.classList.add("available");
        break;
      }
    }

    updateSummary();
  }
}

// Function to update selection summary
function updateSummary() {
  const selectedSeatsContainer = document.getElementById(
    "selectedSeatsContainer"
  );
  const noSeatsSelected = document.getElementById("noSeatsSelected");
  const seatPriceElement = document.getElementById("seatPrice");
  const seatCountElement = document.getElementById("seatCount");
  const totalCostElement = document.getElementById("totalCost");
  const confirmButton = document.getElementById("confirmButton");

  if (selectedSeatsContainer) {
    selectedSeatsContainer.innerHTML = "";
  } else {
    console.warn(
      "updateSummary: #selectedSeatsContainer no encontrado en el DOM"
    );
  }

  if (selectedSeats.length === 0) {
    if (noSeatsSelected) noSeatsSelected.style.display = "block";
    if (seatPriceElement) seatPriceElement.textContent = "$0.00";
    if (seatCountElement) seatCountElement.textContent = "0";
    if (totalCostElement) totalCostElement.textContent = "$0.00";
    if (confirmButton) confirmButton.disabled = true;
  } else {
    if (noSeatsSelected) noSeatsSelected.style.display = "none";

    selectedSeats.forEach((seat) => {
      const seatItem = document.createElement("div");
      seatItem.className = "selected-seat-item";
      seatItem.style.animation = "slideIn 0.3s ease forwards";

      const price = parseFloat(seat.precio) || 0;

      seatItem.innerHTML = `
        <div>
          <span class="font-semibold text-gray-900">Asiento ${
            seat.nombre
          }</span>
          <span class="text-gray-500 ml-2">$${price.toFixed(2)}</span>
        </div>
        <button class="remove-seat-btn" data-seat-id="${seat.idAsiento}">
          <svg class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      `;

      selectedSeatsContainer.appendChild(seatItem);
    });

    document.querySelectorAll(".remove-seat-btn").forEach((button) => {
      button.addEventListener("click", function () {
        const seatId = parseInt(this.dataset.seatId);
        removeSeat(seatId);
      });
    });

    const totalPrice = selectedSeats.reduce(
      (sum, seat) => sum + (parseFloat(seat.precio) || 0),
      0
    );

    const averagePrice =
      selectedSeats.length > 0 ? totalPrice / selectedSeats.length : 0;
    seatPriceElement.textContent = `$${averagePrice.toFixed(2)}`;
    seatCountElement.textContent = selectedSeats.length;
    totalCostElement.textContent = `$${totalPrice.toFixed(2)}`;

    if (confirmButton) confirmButton.disabled = false;
  }
}

// Add event listener to confirm button
document.addEventListener("DOMContentLoaded", function () {
  const confirmButton = document.getElementById("confirmButton");
  if (confirmButton) {
    confirmButton.addEventListener("click", function () {
      if (selectedSeats.length > 0) {
        saveSelectedSeatsToLocalStorage();
        if (currentFlight) {
          localStorage.setItem("currentFlight", JSON.stringify(currentFlight));
        }
        console.log("Selected seats:", selectedSeats);
        window.location.href = "confimacion_de_vuelo.html";
      } else {
        alert("Por favor, selecciona al menos un asiento antes de continuar.");
      }
    });
  } else {
    console.warn("DOMContentLoaded: #confirmButton no encontrado en el DOM");
  }

  const backButton = document.getElementById("backButton");
  if (backButton) {
    backButton.addEventListener("click", function () {
      deselectSeatsVisually();
      window.location.href = "index.html";
    });
  } else {
    console.warn("DOMContentLoaded: #backButton no encontrado en el DOM");
  }

  const homeLink = document.getElementById("homeLink");
  if (homeLink) {
    homeLink.addEventListener("click", function (e) {
      e.preventDefault();
      deselectSeatsVisually();
      window.location.href = "index.html";
    });
  }

  const diagnosticToken = getToken();
  const diagnosticUserId =
    sessionStorage.getItem("id") || sessionStorage.getItem("userId") || null;
  console.log("Diagnostic - token (sessionStorage):", diagnosticToken);
  console.log("Diagnostic - userId (sessionStorage):", diagnosticUserId);

  // Clear any irrelevant seat data from previous flights
  const flightId = sessionStorage.getItem("selectedFlightId");
  if (flightId) {
    Object.keys(localStorage).forEach((key) => {
      if (key.startsWith("selectedSeats_flight_") && !key.includes(flightId)) {
        localStorage.removeItem(key);
        console.log(`Cleared irrelevant localStorage key: ${key}`);
      }
    });
  }

  fetchSeats();
});

// Function to deselect seats visually when returning to index
function deselectSeatsVisually() {
  const flightId = sessionStorage.getItem("selectedFlightId");
  let selectedSeatsData = [];

  if (flightId) {
    const savedSeats = localStorage.getItem(`selectedSeats_flight_${flightId}`);
    if (savedSeats) {
      selectedSeatsData = JSON.parse(savedSeats);
    }
  }

  if (selectedSeatsData.length > 0) {
    selectedSeatsData.forEach((seat) => {
      const seatElement = document.querySelector(
        `.seat[data-id="${seat.idAsiento}"]`
      );
      if (seatElement) {
        seatElement.classList.remove("selected");
        seatElement.classList.add("available");
      }
    });
  } else if (selectedSeats.length > 0) {
    selectedSeats.forEach((seat) => {
      const seatElement = document.querySelector(
        `.seat[data-id="${seat.idAsiento}"]`
      );
      if (seatElement) {
        seatElement.classList.remove("selected");
        seatElement.classList.add("available");
      }
    });
  }

  selectedSeats = [];
  localStorage.removeItem("selectedSeats");
  if (flightId) {
    localStorage.removeItem(`selectedSeats_flight_${flightId}`);
  }
  localStorage.removeItem("currentReservation");
  localStorage.removeItem("currentFlight");
  updateSummary();
  console.log("Seats visually deselected and data cleared from memory and localStorage");
}

// Add event listener for when the user leaves the page
window.addEventListener("beforeunload", function (e) {
  console.log("User is leaving the seat selection page");
});