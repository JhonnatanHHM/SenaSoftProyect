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

// Helper to read token from sessionStorage (centralizado)
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
      console.warn(
        "No se pudieron cargar los vuelos desde localStorage:",
        parseError
      );
    }

    // If we have flight data with seats, use it
    if (flightData && flightData.avion && flightData.avion.asientos) {
      seatsData = flightData.avion.asientos.map((seat) => ({
        idAsiento: seat.idAsiento,
        nombre: seat.nombre,
        estado: seat.estado || "DISPONIBLE", // Preserve original estado or default to DISPONIBLE
        precio: seat.precio || 0, // Ensure we have a price
      }));

      console.log("Using cached seats data:", seatsData);

      // Hide loading indicator
      loadingIndicator.classList.add("hidden");

      // Render seats
      renderSeats(seatsData);
      seatsContent.classList.remove("hidden");

      // Update flight information in the header
      updateFlightInfo(flightData);
      return;
    }

    // Otherwise, fetch from API
    const response = await fetch(
      `http://localhost:8080/api/vuelos/${flightId}/asientos`
    );

    if (!response.ok) {
      if (response.status === 404) {
        throw new Error("No se encontraron asientos para este vuelo.");
      } else if (response.status === 500) {
        throw new Error(
          "Error en el servidor. Por favor, inténtalo más tarde."
        );
      } else {
        throw new Error(
          `Error HTTP: ${response.status} - ${response.statusText}`
        );
      }
    }

    const rawSeatsData = await response.json();
    console.log("Fetched seats data from API:", rawSeatsData);

    // Ensure all seats have proper estado and price data
    seatsData = rawSeatsData.map((seat) => ({
      idAsiento: seat.idAsiento,
      nombre: seat.nombre,
      estado: seat.estado || "DISPONIBLE", // Preserve original estado or default to DISPONIBLE
      precio: seat.precio || 0,
    }));

    // Hide loading indicator
    loadingIndicator.classList.add("hidden");

    // Render seats
    renderSeats(seatsData);
    seatsContent.classList.remove("hidden");

    // Update flight information in the header
    updateFlightInfoFromId(flightId);
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
      flightData.ciudadSalida?.nombre || flightData.lugarSalida || "Origen";
    const destination =
      flightData.ciudadLlegada?.nombre || flightData.lugarLlegada || "Destino";
    flightRouteElement.textContent = `${origin} a ${destination}`;
  }

  // Format and display departure time
  if (departureTimeElement) {
    if (flightData.fechaSalida && flightData.horaSalida) {
      // Combine date and time
      const departureDateTime = new Date(
        `${flightData.fechaSalida}T${flightData.horaSalida}`
      );
      if (!isNaN(departureDateTime.getTime())) {
        // Format time as HH:MM
        const hours = departureDateTime.getHours().toString().padStart(2, "0");
        const minutes = departureDateTime
          .getMinutes()
          .toString()
          .padStart(2, "0");
        departureTimeElement.textContent = `${hours}:${minutes}`;
      } else {
        departureTimeElement.textContent = "--:--";
      }
    } else {
      departureTimeElement.textContent = "--:--";
    }
  }

  // Format and display arrival time
  if (arrivalTimeElement) {
    if (flightData.fechaLlegada && flightData.horaLlegada) {
      // Combine date and time
      const arrivalDateTime = new Date(
        `${flightData.fechaLlegada}T${flightData.horaLlegada}`
      );
      if (!isNaN(arrivalDateTime.getTime())) {
        // Format time as HH:MM
        const hours = arrivalDateTime.getHours().toString().padStart(2, "0");
        const minutes = arrivalDateTime
          .getMinutes()
          .toString()
          .padStart(2, "0");
        arrivalTimeElement.textContent = `${hours}:${minutes}`;
      } else {
        arrivalTimeElement.textContent = "--:--";
      }
    } else {
      arrivalTimeElement.textContent = "--:--";
    }
  }

  // Calculate and display flight duration
  if (flightDurationElement) {
    if (
      flightData.fechaSalida &&
      flightData.horaSalida &&
      flightData.fechaLlegada &&
      flightData.horaLlegada
    ) {
      const departureDateTime = new Date(
        `${flightData.fechaSalida}T${flightData.horaSalida}`
      );
      const arrivalDateTime = new Date(
        `${flightData.fechaLlegada}T${flightData.horaLlegada}`
      );

      if (
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
    } else {
      flightDurationElement.textContent = "--h --m";
    }
  }
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

  // Set default values for time elements
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
      // Generate more realistic pricing based on seat position
      let precio = 50; // Base price
      if (row <= 3) precio += 30; // Premium rows
      if (row >= 8) precio += 10; // Back rows
      if (letters[i] === "A" || letters[i] === "D") precio += 15; // Window seats
      if (letters[i] === "B" || letters[i] === "C") precio += 10; // Middle seats

      seats.push({
        idAsiento: (row - 1) * 4 + i + 1,
        nombre: `${row}${letters[i]}`,
        estado: estado,
        precio: precio,
      });
    }
  }

  // Mark some seats as emergency exits
  if (seats.length > 0) {
    if (seats[8]) seats[8].estado = "EMERGENCIA"; // Row 3, seat A
    if (seats[11]) seats[11].estado = "EMERGENCIA"; // Row 3, seat D
    if (seats[28]) seats[28].estado = "EMERGENCIA"; // Row 8, seat A
    if (seats[31]) seats[31].estado = "EMERGENCIA"; // Row 8, seat D
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

  // Clear existing seats
  seatRows.innerHTML = "";

  // Group seats by row (4 seats per row)
  const rows = [];
  for (let i = 0; i < seats.length; i += 4) {
    rows.push(seats.slice(i, i + 4));
  }

  // Render each row
  rows.forEach((rowSeats, rowIndex) => {
    // Create row container
    const rowContainer = document.createElement("div");
    rowContainer.className = "contents";

    // Left side (first 2 seats)
    const leftSide = document.createElement("div");
    leftSide.className = "flex justify-around";

    // Right side (last 2 seats)
    const rightSide = document.createElement("div");
    rightSide.className = "flex justify-around";

    // Row number
    const rowLabel = document.createElement("div");
    rowLabel.className =
      "text-gray-500 dark:text-gray-400 font-bold self-center text-lg";
    rowLabel.textContent = rowIndex + 1;

    // Process seats for this row
    rowSeats.forEach((seat, seatIndex) => {
      const seatElement = document.createElement("div");
      // Check if seat is already selected
      const isSelected = selectedSeats.some(
        (s) => s.idAsiento === seat.idAsiento
      );

      // Determine seat class based on state
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

      // Add click event for available seats only
      if (seat.estado === "DISPONIBLE" || seat.estado === "EMERGENCIA") {
        seatElement.addEventListener("click", () => {
          // Directly toggle the seat selection without creating a new object
          toggleSeatSelection(seat);
        });
      }

      // Add seat to appropriate side
      if (seatIndex < 2) {
        leftSide.appendChild(seatElement);
      } else {
        rightSide.appendChild(seatElement);
      }
    });

    // Add elements to row container
    rowContainer.appendChild(leftSide);
    rowContainer.appendChild(rowLabel);
    rowContainer.appendChild(rightSide);

    // Add row to seat rows container
    seatRows.appendChild(rowContainer);
  });
}

// Function to toggle seat selection
function toggleSeatSelection(seat) {
  console.log("Toggle seat selection called for seat:", seat);

  // Validate seat data before proceeding
  if (!seat || !seat.idAsiento || isNaN(seat.idAsiento)) {
    console.error("Invalid seat data:", seat);
    return;
  }

  // Find the seat element by data-id attribute
  const seatElement = document.querySelector(`.seat[data-id="${seat.idAsiento}"]`);
  
  if (!seatElement) {
    console.log("Seat element not found for seat ID:", seat.idAsiento);
    return;
  }

  // Check if seat is already selected
  const selectedIndex = selectedSeats.findIndex(
    (s) => s.idAsiento === seat.idAsiento
  );

  console.log("Selected index:", selectedIndex);
  console.log("Current selected seats:", selectedSeats);

  if (selectedIndex !== -1) {
    // Deselect seat
    console.log("Deselecting seat:", seat.idAsiento);
    selectedSeats.splice(selectedIndex, 1);
    seatElement.classList.remove("selected");
    seatElement.classList.add("available");

    // Update seat status on server to "DISPONIBLE"
    updateSeatStatusOnServer(seat.idAsiento, "DISPONIBLE");
  } else {
    // Check if we've reached the maximum number of seats
    if (selectedSeats.length >= maxSeats) {
      console.log("Maximum seats reached");
      alert(`Solo puedes seleccionar un máximo de ${maxSeats} asientos.`);
      return;
    }

    // Select seat - ensure we store all necessary data
    console.log("Selecting seat:", seat.idAsiento);
    selectedSeats.push({
      idAsiento: seat.idAsiento,
      nombre: seat.nombre,
      precio: seat.precio,
      estado: seat.estado,
    });

    // Remove all state classes first
    seatElement.classList.remove("available", "occupied", "emergency");
    // Add selected class
    seatElement.classList.add("selected");

    // Update seat status on server to "SELECCIONADO"
    updateSeatStatusOnServer(seat.idAsiento, "SELECCIONADO");
  }

  // Update summary
  updateSummary();
  console.log("Updated selected seats:", selectedSeats);
}
// Function to update seat status on server (single robust implementation)
async function updateSeatStatusOnServer(seatId, status) {
  // Get token and userId from storage (with safe defaults)
  const token = getToken();
  const userId = parseInt(sessionStorage.getItem("id")) || 1;

  // Helper to find the seat element in the DOM
  function findSeatElement(id) {
    const seatElements = document.querySelectorAll(".seat");
    for (let element of seatElements) {
      if (parseInt(element.dataset.id) === id) return element;
    }
    return null;
  }

  // Validate seatId
  if (!seatId || isNaN(seatId)) {
    console.error("Invalid seat ID:", seatId);
    return;
  }

  const seatElement = findSeatElement(seatId);
  if (seatElement) seatElement.classList.add("updating");

  const idAsiento = seatId; // Just to illustrate usage
  const idUsuario = userId;

  try {
    // Prepare fetch options
    const url = `http://localhost:8080/api/asientos/estado/${seatId}/${userId}`;
    const body = JSON.stringify({ estado: status, usuarioReservado: idUsuario });

    const options = {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body,
      // keep credentials omitted by default; if backend uses cookies consider adding credentials: 'include'
    };

    if (token) options.headers["Authorization"] = `Bearer ${token}`;

    console.log("PATCH options:", options, "url:", url);

    // Perform fetch and capture network errors separately
    let response;
    try {
      response = await fetch(url, options);
    } catch (networkErr) {
      // Network errors (including CORS preflight failures) will be caught here
      console.error("Network error during fetch (possible CORS/preflight issue):", networkErr);
      throw networkErr; // rethrow to be handled by outer catch
    }

    if (!response.ok) {
      // If CORS blocks preflight, response may not be available; throw to go to catch
      throw new Error(`HTTP ${response.status} ${response.statusText}`);
    }

    console.log(`Seat ${seatId} status updated to ${status} on server`);
  } catch (err) {
    console.error("Error updating seat status:", err);

    // Fallback behavior when fetch fails (e.g., CORS or server down):
    // Update local seatsData and UI using the provided JSON structure
    console.warn(
      "Aplicando cambio de estado en modo local (fallback). Esto ocurre cuando la petición al backend falla o es bloqueada por CORS."
    );

    // Find seat in seatsData and update it
    const index = seatsData.findIndex((s) => s.idAsiento === seatId);
    const fallbackPayload = {
      idAsiento: seatId,
      nombre: index !== -1 ? seatsData[index].nombre : `S${seatId}`,
      precio: index !== -1 ? seatsData[index].precio || 0 : 0,
      estado: status,
      idAvion: currentFlight && currentFlight.avion ? currentFlight.avion.idAvion : null,
      usuarioReservado: userId,
    };

    if (index !== -1) {
      seatsData[index] = Object.assign({}, seatsData[index], {
        estado: status,
      });
    } else {
      // If seat not present, push a new one (for demo purposes)
      seatsData.push({
        idAsiento: fallbackPayload.idAsiento,
        nombre: fallbackPayload.nombre,
        precio: fallbackPayload.precio,
        estado: fallbackPayload.estado,
      });
    }

    // Update the seat element classes in the UI
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

    // Persist fallback changes to localStorage so page reload shows changes
    try {
      localStorage.setItem("seatsFallback", JSON.stringify(seatsData));
    } catch (e) {
      console.warn("No se pudo guardar seatsFallback en localStorage:", e);
    }

    // Show the user a clear message explaining the fallback
    alert(
      "No se pudo actualizar el asiento en el servidor (problema de CORS o conexión). El cambio se aplicó localmente para la demostración. Por favor, inténtalo de nuevo más tarde."
    );
  } finally {
    // Remove updating indicator
    if (seatElement) seatElement.classList.remove("updating");
  }
}




// Function to remove a seat from selection
function removeSeat(seatId) {
  // Find the seat in selectedSeats array
  const selectedIndex = selectedSeats.findIndex((s) => s.idAsiento === seatId);

  if (selectedIndex !== -1) {
    // Remove from selectedSeats array
    const removedSeat = selectedSeats.splice(selectedIndex, 1)[0];

    // Update the seat element in the seat map
    const seatElements = document.querySelectorAll(".seat");
    for (let element of seatElements) {
      if (parseInt(element.dataset.id) === seatId) {
        element.classList.remove("selected");
        element.classList.add("available");
        break;
      }
    }

    // Update summary
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

  // Clear the container
  if (selectedSeatsContainer) {
    selectedSeatsContainer.innerHTML = "";
  } else {
    console.warn("updateSummary: #selectedSeatsContainer no encontrado en el DOM");
  }

  if (selectedSeats.length === 0) {
    // Show "no seats selected" message
    if (noSeatsSelected) noSeatsSelected.style.display = "block";
    if (seatPriceElement) seatPriceElement.textContent = "$0.00";
    if (seatCountElement) seatCountElement.textContent = "0";
    if (totalCostElement) totalCostElement.textContent = "$0.00";
    if (confirmButton) confirmButton.disabled = true;
  } else {
    // Hide "no seats selected" message
    if (noSeatsSelected) {
      noSeatsSelected.style.display = "none";
    }

    // Add each selected seat to the summary
    selectedSeats.forEach((seat) => {
      const seatItem = document.createElement("div");
      seatItem.className = "selected-seat-item";

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

    // Add event listeners to remove buttons
    document.querySelectorAll(".remove-seat-btn").forEach((button) => {
      button.addEventListener("click", function () {
        const seatId = parseInt(this.dataset.seatId);
        removeSeat(seatId);
      });
    });

    // Calculate total cost
    const totalPrice = selectedSeats.reduce(
      (sum, seat) => sum + (parseFloat(seat.precio) || 0),
      0
    );

    // Show average price per seat
    const averagePrice =
      selectedSeats.length > 0 ? totalPrice / selectedSeats.length : 0;
    seatPriceElement.textContent = `$${averagePrice.toFixed(2)}`;
    seatCountElement.textContent = selectedSeats.length;
    totalCostElement.textContent = `$${totalPrice.toFixed(2)}`;

    // Enable confirm button
    if (confirmButton) confirmButton.disabled = false;
  }
}

// Add event listener to confirm button
document.addEventListener("DOMContentLoaded", function () {
  const confirmButton = document.getElementById("confirmButton");
  if (confirmButton) {
    confirmButton.addEventListener("click", function () {
      if (selectedSeats.length > 0) {
        // Here you would typically send the selected seats to your backend
        // For now, we'll just redirect to the confirmation page
        alert(
          "Asientos seleccionados: " +
            selectedSeats.map((s) => s.nombre).join(", ")
        );
        window.location.href = "confimacion_de_vuelo.html";
      }
    });
  } else {
    console.warn("DOMContentLoaded: #confirmButton no encontrado en el DOM");
  }

  // Add event listener to back button in error message
  const backButton = document.getElementById("backButton");
  if (backButton) {
    backButton.addEventListener("click", function () {
      window.location.href = "index.html";
    });
  } else {
    console.warn("DOMContentLoaded: #backButton no encontrado en el DOM");
  }

  // Fetch seats when page loads
  // Diagnostic: print token and userId so developer can verify Authorization is sent
  const diagnosticToken = getToken();
  const diagnosticUserId = sessionStorage.getItem("id") || sessionStorage.getItem("userId") || null;
  console.log("Diagnostic - token (sessionStorage):", diagnosticToken);
  console.log("Diagnostic - userId (sessionStorage):", diagnosticUserId);

  fetchSeats();
});
