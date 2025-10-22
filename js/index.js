// Esperar a que el DOM esté completamente cargado
    document.addEventListener("DOMContentLoaded", function () {
      // Obtener referencias a los elementos del formulario
      const departureDateInput = document.getElementById("departure-date");
      const returnDateInput = document.getElementById("return-date");
      const enableReturnCheckbox = document.getElementById("enable-return");

      // Establecer la fecha mínima a hoy para ambos campos
      const today = new Date().toISOString().split("T")[0];
      departureDateInput.min = today;
      returnDateInput.min = today;

      // Establecer el estado inicial
      returnDateInput.disabled = !enableReturnCheckbox.checked;
      if (!enableReturnCheckbox.checked) {
        returnDateInput.placeholder = "Deshabilitado";
      }

      // Función para habilitar/deshabilitar la fecha de regreso
      enableReturnCheckbox.addEventListener("change", function () {
        returnDateInput.disabled = !this.checked;
        if (this.checked) {
          returnDateInput.placeholder = "Seleccionar fecha";
          returnDateInput.min = departureDateInput.value || today;
        } else {
          returnDateInput.placeholder = "Deshabilitado";
          returnDateInput.value = "";
        }
      });

      // Actualizar las restricciones de la fecha de regreso cuando cambia la fecha de salida
      departureDateInput.addEventListener("change", function () {
        if (this.value) {
          // Establecer la fecha mínima de regreso igual a la fecha de salida
          returnDateInput.min = this.value;

          // Establecer la fecha máxima de regreso a 2 meses después de la salida
          const maxDate = new Date(this.value);
          maxDate.setMonth(maxDate.getMonth() + 2);
          returnDateInput.max = maxDate.toISOString().split("T")[0];

          // Si la fecha de regreso es anterior a la de salida, limpiarla
          if (returnDateInput.value && returnDateInput.value < this.value) {
            returnDateInput.value = "";
          }

          // Si la fecha de regreso es posterior a la fecha máxima, limpiarla
          if (
            returnDateInput.value &&
            returnDateInput.value > returnDateInput.max
          ) {
            returnDateInput.value = "";
          }
        }
      });
    });
      document.addEventListener("DOMContentLoaded", () => {
        const form = document.getElementById("formBusqueda");
        const contenedorVuelos = document.getElementById("contenedor-vuelos");

        form.addEventListener("submit", async (e) => {
          e.preventDefault();
          contenedorVuelos.innerHTML = `
            <div class="text-center text-gray-500">
              <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-2"></div>
              Buscando vuelos...
            </div>
          `;

          const origen = document.getElementById("origen").value.trim();
          const destino = document.getElementById("destino").value.trim();

          try {
            const response = await fetch(`http://localhost:8080/api/vuelos`);
            if (!response.ok) throw new Error("Error al obtener los vuelos");
            const vuelos = await response.json();

            // Filtrar por origen/destino (opcional)
            const vuelosFiltrados = vuelos.filter(v =>
              (!origen || v.ciudad_salida?.ciudad.toLowerCase().includes(origen.toLowerCase())) &&
              (!destino || v.ciudad_llegada?.ciudad.toLowerCase().includes(destino.toLowerCase()))
            );

            if (vuelosFiltrados.length === 0) {
              contenedorVuelos.innerHTML = `
                <div class="bg-white p-6 rounded-xl shadow-md border text-center text-gray-500">
                  <span class="material-symbols-outlined text-4xl mb-2">error</span>
                  <p>No se encontraron vuelos para la búsqueda especificada.</p>
                </div>`;
              return;
            }

            contenedorVuelos.innerHTML = vuelosFiltrados.map(vuelo => `
              <div class="bg-white p-4 rounded-xl shadow-md border flex flex-col md:flex-row items-center gap-4 hover:shadow-lg transition-all">
                <img class="w-16 h-16 object-contain rounded-md" src="${vuelo.aerolinea?.imagen?.keyS3 || 'https://via.placeholder.com/64'}" alt="Logo Aerolínea">
                <div class="flex-1 grid grid-cols-1 md:grid-cols-3 gap-4 text-center md:text-left">
                  <div>
                    <p class="font-bold text-lg">${vuelo.hora_salida} - ${vuelo.hora_llegada}</p>
                    <p class="text-sm text-gray-500">${vuelo.ciudad_salida?.ciudad} - ${vuelo.ciudad_llegada?.ciudad}</p>
                  </div>
                  <div>
                    <p class="font-semibold">${vuelo.aerolinea?.nombre || "Aerolínea desconocida"}</p>
                    <p class="text-sm text-gray-500">${vuelo.duracion || "Duración no disponible"}</p>
                  </div>
                  <div>
                    <p class="font-bold text-lg text-green-600">$${vuelo.asientos?.[0]?.precio || "0"}</p>
                    <p class="text-sm text-gray-500">Por persona</p>
                  </div>
                </div>
                <a href="asientos.html" class="bg-orange-500 text-white px-6 py-2 rounded-lg font-bold hover:scale-105 transition-transform">Asientos</a>
              </div>
            `).join("");

          } catch (error) {
            console.error(error);
            contenedorVuelos.innerHTML = `
              <div class="bg-white p-6 rounded-xl shadow-md border text-center text-red-500">
                Error al cargar los vuelos. Intenta nuevamente.
              </div>`;
          }
        });
      });
      tailwind.config = {
            darkMode: "class",
            theme: {
                extend: {
                    colors: {
                        "primary": "#00529B",
                        "secondary": "#F0F2F5",
                        "accent": {
                            "orange": "#FF6B00",
                            "green": "#00BFA5"
                        },
                        "text-dark": "#333333",
                        "background-light": "#f6f7f8",
                        "background-dark": "#101c22",
                    },
                    fontFamily: {
                        "display": ["Plus Jakarta Sans", "sans-serif"]
                    },
                    borderRadius: {
                        "DEFAULT": "0.25rem",
                        "lg": "0.5rem",
                        "xl": "0.75rem",
                        "full": "9999px"
                    },
                },
            },
        }
