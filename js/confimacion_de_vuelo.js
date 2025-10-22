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

    // Function to fetch reservation data from the API
    async function fetchReservationData() {
      const loadingIndicator = document.getElementById("loadingIndicator");
      const errorMessage = document.getElementById("errorMessage");
      const contentArea = document.getElementById("contentArea");

      // Show loading indicator
      loadingIndicator.classList.remove("hidden");
      errorMessage.classList.add("hidden");
      contentArea.classList.add("hidden");

      try {
        // Get reservation ID from URL parameters or localStorage
        // For now, we'll use a placeholder ID
        const reservationId = localStorage.getItem("currentReservationId") || 1;

        // Replace with your actual Spring Boot API endpoint
        const response = await fetch(
          `http://localhost:8080/api/reservas/${reservationId}`
        );

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const reserva = await response.json();

        // Hide loading indicator
        loadingIndicator.classList.add("hidden");

        // Populate the page with reservation data
        populateReservationData(reserva);

        // Show content area
        contentArea.classList.remove("hidden");
      } catch (error) {
        console.error("Error fetching reservation data:", error);
        loadingIndicator.classList.add("hidden");
        errorMessage.classList.remove("hidden");
      }
    }

    // Function to populate the page with reservation data
    function populateReservationData(reserva) {
      // Populate flight details
      if (reserva.vuelos && reserva.vuelos.length > 0) {
        const vuelo = reserva.vuelos[0]; // Assuming one flight per reservation

        // Flight route
        document.getElementById(
          "flightRoute"
        ).textContent = `Vuelo de ${vuelo.ciudad_salida} a ${vuelo.ciudad_llegada}`;

        // Airport codes
        document.getElementById(
          "flightAirports"
        ).innerHTML = `<strong>${vuelo.lugar_salida} - ${vuelo.lugar_llegada}</strong>`;

        // Departure info
        const departureDate = new Date(vuelo.hora_salida);
        document.getElementById(
          "departureInfo"
        ).innerHTML = `<strong>Salida:</strong> ${departureDate.toLocaleDateString(
          "es-ES"
        )} a las ${departureDate.toLocaleTimeString("es-ES", {
          hour: "2-digit",
          minute: "2-digit",
        })}`;

        // Arrival info
        const arrivalDate = new Date(vuelo.hora_llegada);
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
          vuelo.hora_salida,
          vuelo.hora_llegada
        );
        document.getElementById(
          "durationInfo"
        ).innerHTML = `<strong>Duración:</strong> ${duration}, Vuelo Directo`;

        // Airline info
        document.getElementById(
          "airlineInfo"
        ).innerHTML = `<strong>Aerolínea:</strong> ${
          vuelo.aerolinea?.nombre || "N/A"
        }`;

        // Flight image (using a placeholder based on airline)
        const flightImage = document.getElementById("flightImage");
        flightImage.style.backgroundImage = `url('https://placehold.co/600x400?text=${encodeURIComponent(
          vuelo.aerolinea?.nombre || "Aerolínea"
        )}')`;
      }

      // Populate passenger seats
      const passengersContainer = document.getElementById(
        "passengersContainer"
      );
      passengersContainer.innerHTML = "";

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
                ${pasajero.nombres} ${pasajero.primer_apellido} ${
            pasajero.segundo_apellido || ""
          }
              </p>
              <p class="text-[#4c809a] dark:text-slate-400 text-sm font-normal leading-normal">
                ${seatName} | ${pasajero.tipo_documento || ""}: ${
            pasajero.numero_documento || ""
          }
              </p>
            </div>
            <button class="px-6 py-2.5 rounded-lg text-base font-semibold bg-primary/10 dark:bg-primary/20 text-primary hover:bg-primary/20 dark:hover:bg-primary/30 transition-colors">
              <a href="añadir_pasajeros.html?id=${
                pasajero.id_pasajero
              }">Editar</a>
            </button>
          `;

          passengersContainer.appendChild(passengerElement);
        });
      } else {
        // Show empty state if no passengers
        passengersContainer.innerHTML = `
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

    // Fetch data when page loads
    document.addEventListener("DOMContentLoaded", function () {
      fetchReservationData();
    });