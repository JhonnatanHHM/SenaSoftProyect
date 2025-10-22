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

    document.addEventListener('DOMContentLoaded', () => {
  // Verificar si existe el token en sessionStorage
  const token = sessionStorage.getItem('token');

  if (!token) {
    // Si no hay token, redirigir al login
    alert('Debes iniciar sesión para acceder a esta página.');
    window.location.href = 'login.html';
    return;
  }

  // Si el token existe, puedes continuar con tus consultas
  console.log('✅ Token encontrado, acceso permitido.');

  // Aquí puedes agregar tu lógica para cargar los vuelos
  cargarVuelos(token);
});

// Ejemplo de función para cargar vuelos (puedes adaptarla a tu API)
async function cargarVuelos(token) {
  try {
    const response = await fetch('http://localhost:8080/api/vuelos/mis-vuelos', {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Accept': 'application/json'
      }
    });

    if (!response.ok) throw new Error('Error al obtener vuelos.');

    const vuelos = await response.json();

    const container = document.getElementById('vuelosContainer');
    const sinVuelos = document.getElementById('sinVuelos');

    if (!vuelos || vuelos.length === 0) {
      sinVuelos.classList.remove('hidden');
      return;
    }

    // Mostrar los vuelos dinámicamente
    vuelos.forEach((vuelo) => {
      const div = document.createElement('div');
      div.className =
        'p-4 bg-white dark:bg-gray-800 rounded-lg shadow border border-gray-200 dark:border-gray-700';
      div.innerHTML = `
        <p class="text-lg font-semibold text-text-primary dark:text-white">
          ✈️ ${vuelo.origen} → ${vuelo.destino}
        </p>
        <p class="text-sm text-gray-500 dark:text-gray-400">
          Fecha: ${vuelo.fecha}
        </p>
        <p class="text-sm text-gray-500 dark:text-gray-400">
          Estado: ${vuelo.estado}
        </p>
      `;
      container.appendChild(div);
    });
  } catch (error) {
    console.error('❌ Error al cargar vuelos:', error);
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