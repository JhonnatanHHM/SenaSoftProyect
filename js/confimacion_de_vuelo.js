tailwind.config = {
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        primary: "#13a4ec",
        "background-light": "#f6f7f8",
        "background-dark": "#101c22",
      },
      fontFamily: {
        display: ["Plus Jakarta Sans", "Noto Sans", "sans-serif"],
      },
      borderRadius: {
        DEFAULT: "0.25rem",
        lg: "0.5rem",
        xl: "0.75rem",
        full: "9999px",
      },
    },
  },
};

// Function to load reservation data from localStorage (no network)
async function fetchReservationData() {
  const loadingIndicator = document.getElementById("loadingIndicator");
  const errorMessage = document.getElementById("errorMessage");
  const contentArea = document.getElementById("contentArea");

  // Show loading indicator
  if (loadingIndicator) loadingIndicator.classList.remove("hidden");
  if (errorMessage) errorMessage.classList.add("hidden");
  if (contentArea) contentArea.classList.add("hidden");

  try {
    // Try to obtain reservationId from localStorage (if present)
    const reservationId =
      localStorage.getItem("currentReservationId") ||
      localStorage.getItem("reservationId") ||
      null;

    // Get selected flight ID from sessionStorage
    const selectedFlightId = sessionStorage.getItem("selectedFlightId");

    // Candidate keys to look for in localStorage (ordered)
    const candidateKeys = [];
    if (reservationId) {
      candidateKeys.push(`reserva_${reservationId}`);
      candidateKeys.push(`reservation_${reservationId}`);
      candidateKeys.push(`reservation-${reservationId}`);
    }
    // generic fallbacks
    candidateKeys.push("currentReservation");
    candidateKeys.push("currentReservationData");
    candidateKeys.push("reservation");
    candidateKeys.push("reserva");
    candidateKeys.push("selectedSeats");
    candidateKeys.push("seatsFallback");

    let reserva = null;
    for (const key of candidateKeys) {
      const raw = localStorage.getItem(key);
      if (!raw) continue;
      try {
        const parsed = JSON.parse(raw);
        // Heurística simple: debe tener al menos alguno de estos campos
        if (
          parsed &&
          (parsed.vuelos || parsed.pasajeros || Array.isArray(parsed))
        ) {
          reserva = parsed;
          console.log(`Loaded reservation from localStorage key: ${key}`);
          break;
        }
        // Special case: if it's just an array of seats
        if (Array.isArray(parsed)) {
          reserva = { selectedSeats: parsed };
          console.log(`Loaded seats array from localStorage key: ${key}`);
          break;
        }
      } catch (e) {
        // If parsing fails, but raw contains seat array, try to wrap
        console.warn(`Could not parse localStorage key ${key}:`, e);
      }
    }

    // If no reservation found, try to create one from available data
    if (!reserva) {
      // Try to get flight data based on flight ID
      let vuelo = null;

      if (selectedFlightId) {
        // Try to get flight data from vuelosCache
        const vuelosCache = localStorage.getItem("vuelosCache");
        if (vuelosCache) {
          try {
            const vuelos = JSON.parse(vuelosCache);
            vuelo = vuelos.find((v) => v.idVuelo == selectedFlightId);
          } catch (e) {
            console.warn("Error parsing vuelosCache:", e);
          }
        }
      }

      // If still no flight data, try to get from a flight-specific key
      if (!vuelo && selectedFlightId) {
        const flightData = localStorage.getItem(`flight_${selectedFlightId}`);
        if (flightData) {
          try {
            vuelo = JSON.parse(flightData);
          } catch (e) {
            console.warn(`Error parsing flight_${selectedFlightId}:`, e);
          }
        }
      }

      // Try to get passenger data
      const passengersData = localStorage.getItem("passengersData");

      // Try to get seat selection data
      const selectedSeatsData = getSavedSeatsForFlight(selectedFlightId, null);

      // Even if we don't have a full reservation, we can still display selected seats
      reserva = {
        vuelos: vuelo ? [vuelo] : [],
        pasajeros: passengersData ? JSON.parse(passengersData) : [],
        selectedSeats: selectedSeatsData || [],
      };

      console.log("Created reservation from available data:", reserva);
    }

    // Hide loading indicator
    if (loadingIndicator) loadingIndicator.classList.add("hidden");

    // Populate and render
    populateReservationData(reserva);

    if (contentArea) contentArea.classList.remove("hidden");
  } catch (error) {
    console.error("Error loading reservation data from localStorage:", error);
    if (loadingIndicator) loadingIndicator.classList.add("hidden");

    // Even if we have an error, try to show selected seats if they exist
    try {
      const selectedFlightId = sessionStorage.getItem("selectedFlightId");
      const selectedSeatsData = getSavedSeatsForFlight(selectedFlightId, null);

      if (selectedSeatsData && selectedSeatsData.length > 0) {
        // Create a minimal reservation object with just the seats
        const reserva = {
          vuelos: [],
          pasajeros: [],
          selectedSeats: selectedSeatsData,
        };

        // Hide error message and show content
        if (errorMessage) errorMessage.classList.add("hidden");
        if (contentArea) contentArea.classList.remove("hidden");

        // Populate with the seat data
        populateReservationData(reserva);
      } else {
        // Show error only if there's no seat data either
        if (errorMessage) errorMessage.classList.remove("hidden");
      }
    } catch (innerError) {
      console.error("Error displaying seat data:", innerError);
      if (errorMessage) errorMessage.classList.remove("hidden");
    }
  }
}

// --- Helpers to load saved seat selections from localStorage ---
function _getSavedByKey(key) {
  try {
    const raw = localStorage.getItem(key);
    if (!raw) return null;
    return JSON.parse(raw);
  } catch (e) {
    console.warn(`_getSavedByKey(${key}) parse error:`, e);
    return null;
  }
}

function getSavedSeatsForFlight(flightId, idAvion) {
  if (!flightId && !idAvion) return null;

  // Try flight-specific key
  if (flightId) {
    const k = `selectedSeats_flight_${flightId}`;
    const v = _getSavedByKey(k);
    if (v && Array.isArray(v) && v.length) return v;
  }

  // Try avion-specific key
  if (idAvion) {
    const k2 = `selectedSeats_avion_${idAvion}`;
    const v2 = _getSavedByKey(k2);
    if (v2 && Array.isArray(v2) && v2.length) return v2;
  }

  // Generic fallbacks
  const generic =
    _getSavedByKey("selectedSeats") || _getSavedByKey("seatsFallback");
  if (generic && Array.isArray(generic) && generic.length) return generic;
  return null;
}

function renderSavedSelections(reserva) {
  // Determine identifiers
  const vuelo =
    reserva && reserva.vuelos && reserva.vuelos.length
      ? reserva.vuelos[0]
      : null;
  const flightId = vuelo ? vuelo.idVuelo || vuelo.id || vuelo.id_vuelo : null;
  const idAvion =
    vuelo && vuelo.avion
      ? vuelo.avion.idAvion || vuelo.avion.id || vuelo.avion.id_avion
      : null;

  const savedSeats = getSavedSeatsForFlight(flightId, idAvion);
  if (!savedSeats) {
    console.log("No saved seats found in localStorage for this reservation");
    return;
  }

  const passengersContainer = document.getElementById("passengersContainer");
  if (!passengersContainer) return;

  // Create a section for selected seats
  const seatsSection = document.createElement("div");
  seatsSection.className = "mb-6";
  seatsSection.innerHTML = `
    <h3 class="text-lg font-bold text-[#0d171b] dark:text-white mb-3">Asientos Seleccionados</h3>
    <div id="selectedSeatsList" class="space-y-2"></div>
  `;

  // Insert the seats section at the top of passengersContainer
  passengersContainer.insertBefore(
    seatsSection,
    passengersContainer.firstChild
  );

  // Populate the selected seats list
  const seatsList = document.getElementById("selectedSeatsList");
  savedSeats.forEach((seat) => {
    const seatElement = document.createElement("div");
    seatElement.className =
      "flex items-center gap-4 bg-white dark:bg-slate-800 p-4 rounded-lg shadow-[0_0_4px_rgba(0,0,0,0.1)]";
    seatElement.innerHTML = `
      <div class="shrink-0">
        <span class="material-symbols-outlined text-primary text-3xl">event_seat</span>
      </div>
      <div class="flex-1">
        <p class="text-[#0d171b] dark:text-white text-base font-semibold leading-normal">
          Asiento ${seat.nombre || seat.idAsiento || "N/A"}
        </p>
        <p class="text-[#4c809a] dark:text-slate-400 text-sm font-normal leading-normal">
          Precio: €${(seat.precio || 0).toFixed(2)}
        </p>
      </div>
      <button class="px-6 py-2.5 rounded-lg text-base font-semibold bg-primary/10 dark:bg-primary/20 text-primary hover:bg-primary/20 dark:hover:bg-primary/30 transition-colors">
        <a href="asientos.html">Editar</a>
      </button>
    `;
    seatsList.appendChild(seatElement);
  });

  console.log("Rendered saved seats in confirmation view:", savedSeats.length);
}

// Function to populate the page with reservation data
function populateReservationData(reserva) {
  // Populate flight details
  if (reserva.vuelos && reserva.vuelos.length > 0) {
    const vuelo = reserva.vuelos[0]; // Assuming one flight per reservation

    // Flight route
    document.getElementById("flightRoute").textContent = `Vuelo de ${
      vuelo.ciudad_salida || vuelo.origen || "Ciudad"
    } a ${vuelo.ciudad_llegada || vuelo.destino || "Ciudad"}`;

    // Airport codes
    document.getElementById("flightAirports").innerHTML = `<strong>${
      vuelo.lugar_salida || vuelo.codigo_origen || "COD"
    } - ${vuelo.lugar_llegada || vuelo.codigo_destino || "COD"}</strong>`;

    // Departure info
    const departureDate = new Date(
      vuelo.hora_salida || vuelo.fecha_salida || vuelo.fechaSalida || new Date()
    );
    document.getElementById(
      "departureInfo"
    ).innerHTML = `<strong>Salida:</strong> ${departureDate.toLocaleDateString(
      "es-ES"
    )} a las ${departureDate.toLocaleTimeString("es-ES", {
      hour: "2-digit",
      minute: "2-digit",
    })}`;

    // Arrival info
    const arrivalDate = new Date(
      vuelo.hora_llegada ||
        vuelo.fecha_llegada ||
        vuelo.fechaLlegada ||
        new Date()
    );
    document.getElementById(
      "arrivalInfo"
    ).innerHTML = `<strong>Llegada:</strong> ${arrivalDate.toLocaleDateString(
      "es-ES"
    )} a las ${arrivalDate.toLocaleTimeString("es-ES", {
      hour: "2-digit",
      minute: "2-digit",
    })}`;

    // Duration info
    const duration = calculateDuration(
      vuelo.hora_salida || vuelo.fecha_salida || vuelo.fechaSalida,
      vuelo.hora_llegada || vuelo.fecha_llegada || vuelo.fechaLlegada
    );
    document.getElementById(
      "durationInfo"
    ).innerHTML = `<strong>Duración:</strong> ${duration}, Vuelo Directo`;

    // Airline info
    document.getElementById(
      "airlineInfo"
    ).innerHTML = `<strong>Aerolínea:</strong> ${
      vuelo.aerolinea?.nombre || vuelo.aerolinea || "N/A"
    }`;

    // Flight image (using a placeholder based on airline)
    const flightImage = document.getElementById("flightImage");
    const airlineName =
      vuelo.aerolinea?.nombre || vuelo.aerolinea || "Aerolínea";
    flightImage.style.backgroundImage = `url('https://placehold.co/600x400?text=${encodeURIComponent(
      airlineName
    )}')`;
  }

  // Populate passenger seats
  const passengersContainer = document.getElementById("passengersContainer");
  if (passengersContainer) {
    // First, display selected seats
    const savedSeats = getSavedSeatsForFlight(
      vuelo ? vuelo.idVuelo || vuelo.id || vuelo.id_vuelo : null,
      vuelo && vuelo.avion
        ? vuelo.avion.idAvion || vuelo.avion.id || vuelo.avion.id_avion
        : null
    );

    // Clear the container
    passengersContainer.innerHTML = "";

    if (savedSeats && savedSeats.length > 0) {
      const seatsSection = document.createElement("div");
      seatsSection.className = "mb-6";
      seatsSection.innerHTML = `
        <h3 class="text-lg font-bold text-[#0d171b] dark:text-white mb-3">Asientos Seleccionados</h3>
        <div id="selectedSeatsList" class="space-y-2"></div>
      `;
      passengersContainer.appendChild(seatsSection);

      const seatsList = document.getElementById("selectedSeatsList");
      savedSeats.forEach((seat) => {
        const seatElement = document.createElement("div");
        seatElement.className =
          "flex items-center gap-4 bg-white dark:bg-slate-800 p-4 rounded-lg shadow-[0_0_4px_rgba(0,0,0,0.1)]";
        seatElement.innerHTML = `
          <div class="shrink-0">
            <span class="material-symbols-outlined text-primary text-3xl">event_seat</span>
          </div>
          <div class="flex-1">
            <p class="text-[#0d171b] dark:text-white text-base font-semibold leading-normal">
              Asiento ${seat.nombre || seat.idAsiento || "N/A"}
            </p>
            <p class="text-[#4c809a] dark:text-slate-400 text-sm font-normal leading-normal">
              Precio: €${(seat.precio || 0).toFixed(2)}
            </p>
          </div>
          <button class="px-6 py-2.5 rounded-lg text-base font-semibold bg-primary/10 dark:bg-primary/20 text-primary hover:bg-primary/20 dark:hover:bg-primary/30 transition-colors">
            <a href="asientos.html">Editar</a>
          </button>
        `;
        seatsList.appendChild(seatElement);
      });
    }

    if (reserva.pasajeros && reserva.pasajeros.length > 0) {
      reserva.pasajeros.forEach((pasajero) => {
        const seatName = pasajero.asiento?.nombre || "Asiento no asignado";

        const passengerElement = document.createElement("div");
        passengerElement.className =
          "flex items-center gap-4 bg-white dark:bg-slate-800 p-4 rounded-lg shadow-[0_0_4px_rgba(0,0,0,0.1)]";
        passengerElement.innerHTML = `
          <div class="shrink-0">
            <span class="material-symbols-outlined text-primary text-3xl">account_circle</span>
          </div>
          <div class="flex-1">
            <p class="text-[#0d171b] dark:text-white text-base font-semibold leading-normal">
              ${pasajero.nombres || pasajero.nombre || ""} ${
          pasajero.primer_apellido || pasajero.apellido || ""
        } ${pasajero.segundo_apellido || ""}
            </p>
            <p class="text-[#4c809a] dark:text-slate-400 text-sm font-normal leading-normal">
              ${seatName} | ${
          pasajero.tipo_documento || pasajero.tipoDocumento || ""
        }: ${pasajero.numero_documento || pasajero.numeroDocumento || ""}
            </p>
          </div>
          <button class="px-6 py-2.5 rounded-lg text-base font-semibold bg-primary/10 dark:bg-primary/20 text-primary hover:bg-primary/20 dark:hover:bg-primary/30 transition-colors">
            <a href="añadir_pasajeros.html?id=${
              pasajero.id_pasajero || pasajero.id || ""
            }">Editar</a>
          </button>
        `;

        passengersContainer.appendChild(passengerElement);
      });
    } else {
      // Show empty state if no passengers
      passengersContainer.innerHTML += `
        <div class="flex items-center gap-4 bg-white dark:bg-slate-800 p-4 rounded-lg shadow-[0_0_4px_rgba(0,0,0,0.1)]">
          <div class="shrink-0">
            <span class="material-symbols-outlined text-primary text-3xl">warning</span>
          </div>
          <p class="text-[#0d171b] dark:text-white text-base font-normal leading-normal flex-1">
            No hay pasajeros registrados
          </p>
          <button class="px-6 py-2.5 rounded-lg text-base font-semibold bg-primary text-white hover:bg-opacity-90 transition-colors">
            <a href="añadir_pasajeros.html">Agregar Pasajero</a>
          </button>
        </div>
      `;
    }
  }

  // Populate pricing information
  if (reserva.pago) {
    document.getElementById("baseFare").textContent = `€${(
      reserva.pago.total * 0.85
    ).toFixed(2)}`;
    document.getElementById("taxes").textContent = `€${(
      reserva.pago.total * 0.15
    ).toFixed(2)}`;
    document.getElementById(
      "totalAmount"
    ).textContent = `€${reserva.pago.total.toFixed(2)}`;
  } else {
    // Calculate total from selected seats if no payment data
    let selectedSeats = [];
    if (reserva.selectedSeats && Array.isArray(reserva.selectedSeats)) {
      selectedSeats = reserva.selectedSeats;
    } else {
      const savedSeats = localStorage.getItem("selectedSeats");
      if (savedSeats) {
        try {
          selectedSeats = JSON.parse(savedSeats);
        } catch (e) {
          console.warn("Error parsing selected seats from localStorage:", e);
        }
      }
    }

    const total = selectedSeats.reduce(
      (sum, seat) => sum + (seat.precio || 0),
      0
    );
    document.getElementById("baseFare").textContent = `€${(
      total * 0.85
    ).toFixed(2)}`;
    document.getElementById("taxes").textContent = `€${(total * 0.15).toFixed(
      2
    )}`;
    document.getElementById("totalAmount").textContent = `€${total.toFixed(2)}`;
  }
}

// Helper function to calculate duration between two dates
function calculateDuration(start, end) {
  if (!start || !end) return "N/A";

  const inicio = new Date(start);
  const fin = new Date(end);
  const diffMs = fin - inicio;
  const diffHrs = Math.floor(diffMs / 3600000);
  const diffMins = Math.floor((diffMs % 3600000) / 60000);

  return `${diffHrs}h ${diffMins}m`;
}

// Enhanced function to save reservation data to localStorage
function saveReservationDataToLocalStorage(reserva) {
  try {
    // Save the main reservation data
    localStorage.setItem("currentReservation", JSON.stringify(reserva));

    // If we have flight data, save it with a flight-specific key
    if (reserva.vuelos && reserva.vuelos.length > 0) {
      const vuelo = reserva.vuelos[0];
      if (vuelo.idVuelo) {
        localStorage.setItem(
          `reservation_${vuelo.idVuelo}`,
          JSON.stringify(reserva)
        );
      }
    }

    // If we have selected seats, save them separately
    if (reserva.selectedSeats) {
      localStorage.setItem(
        "selectedSeats",
        JSON.stringify(reserva.selectedSeats)
      );
    }

    console.log("Reservation data saved to localStorage");
  } catch (e) {
    console.error("Error saving reservation data to localStorage:", e);
  }
}

// Function to collect and save current reservation data
function collectAndSaveReservationData() {
  try {
    // Collect flight data from sessionStorage
    const selectedFlightId = sessionStorage.getItem("selectedFlightId");
    let vuelosData = [];

    if (selectedFlightId) {
      const vuelosCache = localStorage.getItem("vuelosCache");
      if (vuelosCache) {
        const vuelos = JSON.parse(vuelosCache);
        const vuelo = vuelos.find((v) => v.idVuelo == selectedFlightId);
        if (vuelo) {
          vuelosData = [vuelo];
        }
      }
    }

    // Collect passenger data (this would need to be implemented based on your form)
    // For now, we'll use a placeholder
    const passengersData = [];

    // Collect seat selection data
    const selectedSeatsData = JSON.parse(
      localStorage.getItem("selectedSeats") || "[]"
    );

    // Create reservation object
    const reserva = {
      vuelos: vuelosData,
      pasajeros: passengersData,
      selectedSeats: selectedSeatsData,
    };

    // Save to localStorage
    saveReservationDataToLocalStorage(reserva);

    return reserva;
  } catch (e) {
    console.error("Error collecting reservation data:", e);
    return null;
  }
}

// Fetch data when page loads
document.addEventListener("DOMContentLoaded", function () {
  fetchReservationData();
});
