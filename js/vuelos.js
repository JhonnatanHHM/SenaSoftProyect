tailwind.config = {
      darkMode: "class",
      theme: {
        extend: {
          colors: {
            "primary": "#00529B",
            "background-light": "#F8F9FA",
            "background-dark": "#101c22",
            "text-primary": "#212529",
            "text-secondary": "#6c757d",
            "accent-green": "#28a745"
          },
          fontFamily: {
            "display": ["Plus Jakarta Sans", "sans-serif"]
          },
          borderRadius: { "DEFAULT": "0.25rem", "lg": "0.5rem", "xl": "0.75rem", "full": "9999px" },
        },
      },
    }

    async function validarToken() {
      const token = localStorage.getItem('token');
      if (!token) {
        window.location.href = 'login.html';
        return;
      }

      const res = await fetch('http://localhost:8080/api/auth/validate', {
        method: 'GET',
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (!res.ok) {
        localStorage.removeItem('token');
        window.location.href = 'login.html';
      }
    }

    async function cargarVuelos() {
      const token = localStorage.getItem('token');
      const container = document.getElementById('vuelosContainer');
      const sinVuelos = document.getElementById('sinVuelos');

      try {
        const res = await fetch('http://localhost:8080/api/tickets/mis-vuelos', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (!res.ok) throw new Error('Error al obtener los vuelos');

        const tickets = await res.json();

        if (!tickets || tickets.length === 0) {
          sinVuelos.classList.remove('hidden');
          return;
        }

        tickets.forEach(ticket => {
          const vuelo = ticket.reservas.vuelos;
          const aerolinea = vuelo.aerolinea;
          const ciudadSalida = vuelo.ciudad_salida.ciudad;
          const ciudadLlegada = vuelo.ciudad_llegada.ciudad;

          const vueloHTML = `
            <div class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 rounded-lg bg-white dark:bg-background-dark/50 p-4 shadow-sm border border-gray-200 dark:border-gray-700">
              <div class="flex items-center gap-4">
                <div class="w-16 h-16 bg-center bg-no-repeat aspect-square bg-cover rounded-lg flex-1"
                  style='background-image: url("data:image/png;base64,${aerolinea.imagen?.base64 || ""}")'>
                </div>
                <div class="flex flex-col">
                  <p class="text-lg font-bold text-text-primary dark:text-white">${ciudadSalida} (${vuelo.ciudad_salida.id_ciudad}) a ${ciudadLlegada} (${vuelo.ciudad_llegada.id_ciudad})</p>
                  <p class="text-sm text-text-secondary dark:text-gray-400">${vuelo.hora_salida} - ${vuelo.hora_llegada}</p>
                  <p class="text-sm text-text-secondary dark:text-gray-400">${aerolinea.nombre}</p>
                </div>
              </div>
              <button class="flex items-center justify-center gap-2 min-w-[84px] cursor-pointer rounded-lg h-10 px-4 bg-primary/10 text-primary hover:bg-primary/20 dark:bg-primary/20 dark:hover:bg-primary/30 transition-colors text-sm font-medium w-full sm:w-auto">
                <span class="material-symbols-outlined !text-lg">picture_as_pdf</span>
                <span class="truncate">Descargar PDF</span>
              </button>
            </div>
          `;
          container.insertAdjacentHTML('beforeend', vueloHTML);
        });

      } catch (err) {
        console.error(err);
        sinVuelos.classList.remove('hidden');
      }
    }

    // Ejecutar al cargar la página
    validarToken().then(() => cargarVuelos());