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

    let reserva = null;

    // Try to get the most recent reservation data
    const currentReservation = localStorage.getItem("currentReservation");
    if (currentReservation) {
      try {
        reserva = JSON.parse(currentReservation);
        console.log("Loaded current reservation from localStorage");
      } catch (e) {
        console.warn("Error parsing currentReservation:", e);
      }
    }

    // If no reservation found, try other keys
    if (!reserva) {
      // Candidate keys to look for in localStorage (ordered)
      const candidateKeys = [];
      if (reservationId) {
        candidateKeys.push(`reserva_${reservationId}`);
        candidateKeys.push(`reservation_${reservationId}`);
        candidateKeys.push(`reservation-${reservationId}`);
      }
      // generic fallbacks
      candidateKeys.push("currentReservationData");
      candidateKeys.push("reservation");
      candidateKeys.push("reserva");

      for (const key of candidateKeys) {
        const raw = localStorage.getItem(key);
        if (!raw) continue;
        try {
          const parsed = JSON.parse(raw);
          // Heurística simple: debe tener al menos alguno de estos campos
          if (
            parsed &&
            (parsed.vuelos ||
              parsed.pasajeros ||
              Array.isArray(parsed) ||
              parsed.selectedSeats)
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

      // Try to get from currentFlight key (saved by asientos.js)
      if (!vuelo) {
        const currentFlightData = localStorage.getItem("currentFlight");
        if (currentFlightData) {
          try {
            vuelo = JSON.parse(currentFlightData);
          } catch (e) {
            console.warn("Error parsing currentFlight:", e);
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

    // Ensure selectedSeats is properly populated
    if (
      (!reserva.selectedSeats || reserva.selectedSeats.length === 0) &&
      selectedFlightId
    ) {
      const selectedSeatsData = getSavedSeatsForFlight(selectedFlightId, null);
      if (selectedSeatsData && selectedSeatsData.length > 0) {
        reserva.selectedSeats = selectedSeatsData;
      }
    }

    // Ensure vuelos is properly populated
    if ((!reserva.vuelos || reserva.vuelos.length === 0) && reserva.flightId) {
      const vuelosCache = localStorage.getItem("vuelosCache");
      if (vuelosCache) {
        try {
          const vuelos = JSON.parse(vuelosCache);
          const vuelo = vuelos.find((v) => v.idVuelo == reserva.flightId);
          if (vuelo) {
            reserva.vuelos = [vuelo];
          }
        } catch (e) {
          console.warn("Error parsing vuelosCache:", e);
        }
      }
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
        <a href="asientos.html">Cambiar o Añadir</a>
      </button>
    `;
  });

  console.log("Rendered saved seats in confirmation view:", savedSeats.length);
}

// Function to populate the page with reservation data
function populateReservationData(reserva) {
  console.log("Populating reservation data:", reserva);

  // Populate flight details
  if (reserva.vuelos && reserva.vuelos.length > 0) {
    const vuelo = reserva.vuelos[0]; // Assuming one flight per reservation
    populateFlightDetails(vuelo);
  } else if (reserva.flightId) {
    // Try to get flight data from localStorage using flightId
    const vuelosCache = localStorage.getItem("vuelosCache");
    if (vuelosCache) {
      try {
        const vuelos = JSON.parse(vuelosCache);
        const vuelo = vuelos.find((v) => v.idVuelo == reserva.flightId);
        if (vuelo) {
          populateFlightDetails(vuelo);
        }
      } catch (e) {
        console.warn("Error parsing vuelosCache:", e);
      }
    }
  } else {
    // Try to get flight data from currentFlight key
    const currentFlightData = localStorage.getItem("currentFlight");
    if (currentFlightData) {
      try {
        const vuelo = JSON.parse(currentFlightData);
        populateFlightDetails(vuelo);
      } catch (e) {
        console.warn("Error parsing currentFlight:", e);
      }
    }
  }

  // Populate passenger seats
  const passengersContainer = document.getElementById("passengersContainer");
  if (passengersContainer) {
    // First, display selected seats
    const vuelo =
      reserva.vuelos && reserva.vuelos.length > 0 ? reserva.vuelos[0] : null;
    const flightId = vuelo ? vuelo.idVuelo || vuelo.id || vuelo.id_vuelo : null;
    const idAvion =
      vuelo && vuelo.avion
        ? vuelo.avion.idAvion || vuelo.avion.id || vuelo.avion.id_avion
        : null;

    let savedSeats = [];
    if (reserva.selectedSeats && reserva.selectedSeats.length > 0) {
      savedSeats = reserva.selectedSeats;
    } else {
      savedSeats = getSavedSeatsForFlight(flightId, idAvion) || [];
    }

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
            <a href="asientos.html">Editar o Añadir</a>
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
            }">Editar o Añadir</a>
          </button>
        `;

        passengersContainer.appendChild(passengerElement);
      });
    } else {
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

// New function to populate flight details
function populateFlightDetails(vuelo) {
  if (!vuelo) return;

  // Flight route
  const origin =
    vuelo.ciudad_salida ||
    vuelo.origen ||
    vuelo.ciudadSalida?.nombre ||
    vuelo.lugarSalida ||
    "Origen";
  const destination =
    vuelo.ciudad_llegada ||
    vuelo.destino ||
    vuelo.ciudadLlegada?.nombre ||
    vuelo.lugarLlegada ||
    "Destino";
  document.getElementById(
    "flightRoute"
  ).textContent = `Vuelo de ${origin} a ${destination}`;

  // Airport codes
  const originCode =
    vuelo.codigo_origen || vuelo.lugar_salida || vuelo.lugarSalida || "COD";
  const destinationCode =
    vuelo.codigo_destino || vuelo.lugar_llegada || vuelo.lugarLlegada || "COD";
  document.getElementById(
    "flightAirports"
  ).innerHTML = `<strong>${originCode} - ${destinationCode}</strong>`;

  // Departure info
  let departureDate = null;
  if (vuelo.fechaHoraSalida) {
    departureDate = new Date(vuelo.fechaHoraSalida);
  } else if (vuelo.fechaSalida && vuelo.horaSalida) {
    departureDate = new Date(`${vuelo.fechaSalida}T${vuelo.horaSalida}`);
  } else if (vuelo.fecha_salida) {
    departureDate = new Date(vuelo.fecha_salida);
  } else {
    departureDate = new Date();
  }

  document.getElementById(
    "departureInfo"
  ).innerHTML = `<strong>Salida:</strong> ${departureDate.toLocaleDateString(
    "es-ES"
  )} a las ${departureDate.toLocaleTimeString("es-ES", {
    hour: "2-digit",
    minute: "2-digit",
  })}`;

  // Arrival info
  let arrivalDate = null;
  if (vuelo.fechaHoraLlegada) {
    arrivalDate = new Date(vuelo.fechaHoraLlegada);
  } else if (vuelo.fechaLlegada && vuelo.horaLlegada) {
    arrivalDate = new Date(`${vuelo.fechaLlegada}T${vuelo.horaLlegada}`);
  } else if (vuelo.fecha_llegada) {
    arrivalDate = new Date(vuelo.fecha_llegada);
  } else {
    arrivalDate = new Date();
  }

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
    vuelo.fechaHoraSalida || (vuelo.fechaSalida && vuelo.horaSalida)
      ? `${vuelo.fechaSalida}T${vuelo.horaSalida}`
      : vuelo.fecha_salida,
    vuelo.fechaHoraLlegada || (vuelo.fechaLlegada && vuelo.horaLlegada)
      ? `${vuelo.fechaLlegada}T${vuelo.horaLlegada}`
      : vuelo.fecha_llegada
  );
  document.getElementById(
    "durationInfo"
  ).innerHTML = `<strong>Duración:</strong> ${duration}, Vuelo Directo`;

  // Airline info
  const airlineName =
    vuelo.aerolinea?.nombre ||
    vuelo.aerolinea ||
    vuelo.nombreAerolinea ||
    "Aerolínea";
  document.getElementById(
    "airlineInfo"
  ).innerHTML = `<strong>Aerolínea:</strong> ${airlineName}`;

  // Flight image
  const flightImage = document.getElementById("flightImage");

  // Check if the flight data contains an image URL
  if (vuelo.imagen || vuelo.imageUrl || vuelo.imagenUrl) {
    // Use the actual image if available
    const imageUrl = vuelo.imagen || vuelo.imageUrl || vuelo.imagenUrl;
    flightImage.style.backgroundImage = `url('${imageUrl}')`;
  } else if (vuelo.aerolinea?.imagen || vuelo.aerolinea?.imageUrl) {
    // Use airline image if available
    const airlineImage = vuelo.aerolinea.imagen || vuelo.aerolinea.imageUrl;
    flightImage.style.backgroundImage = `url('${airlineImage}')`;
  } else {
    // Fallback to placeholder with airline name
    flightImage.style.backgroundImage = `url('https://placehold.co/600x400?text=${encodeURIComponent(
      airlineName
    )}')`;
  }

  // Ensure background properties are set correctly
  flightImage.style.backgroundSize = "cover";
  flightImage.style.backgroundPosition = "center";
  flightImage.style.backgroundRepeat = "no-repeat";
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
