// Configuración de la API
const API_URL = 'http://localhost:8080/api/vuelos';

// Función para formatear fecha y hora
function formatearFechaHora(fechaISO) {
  const fecha = new Date(fechaISO);
  const opciones = { 
    year: 'numeric', 
    month: 'short', 
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  };
  return fecha.toLocaleDateString('es-ES', opciones);
}

// Función para calcular duración del vuelo
function calcularDuracion(salida, llegada) {
  const inicio = new Date(salida);
  const fin = new Date(llegada);
  const diff = fin - inicio;
  const horas = Math.floor(diff / (1000 * 60 * 60));
  const minutos = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
  return `${horas}h ${minutos}m`;
}

// Función para contar asientos por estado
function contarAsientos(asientos) {
  const disponibles = asientos.filter(a => a.estado === 'DISPONIBLE').length;
  const ocupados = asientos.filter(a => a.estado === 'OCUPADO').length;
  const seleccionados = asientos.filter(a => a.estado === 'SELECCIONADO').length;
  
  return { disponibles, ocupados, seleccionados };
}

// Función para crear la tarjeta de vuelo
function crearTarjetaVuelo(vuelo) {
  const { disponibles, ocupados, seleccionados } = contarAsientos(vuelo.avion.asientos);
  const duracion = calcularDuracion(vuelo.horaSalida, vuelo.horaLlegada);
  
  return `
    <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
      <!-- Header con aerolínea -->
      <div class="flex items-center justify-between mb-4">
        <div class="flex items-center gap-3">
          <img src="${vuelo.aerolinea.imagen.keyR2}" 
               alt="${vuelo.aerolinea.nombre}" 
               class="w-12 h-12 object-contain rounded">
          <div>
            <h3 class="font-bold text-lg">${vuelo.aerolinea.nombre}</h3>
            <p class="text-sm text-gray-600">${vuelo.avion.modelo}</p>
          </div>
        </div>
        <div class="text-right">
          <p class="text-xs text-gray-500">Vuelo #${vuelo.idVuelo}</p>
          <p class="text-xs text-gray-500">Capacidad: ${vuelo.avion.capacidad}</p>
        </div>
      </div>

      <!-- Ruta de vuelo -->
      <div class="grid grid-cols-3 gap-4 items-center mb-4">
        <!-- Origen -->
        <div>
          <p class="text-2xl font-bold">${vuelo.ciudadSalida.nombre}</p>
          <p class="text-sm text-gray-600">${vuelo.lugarSalida}</p>
          <p class="text-sm font-medium mt-1">${formatearFechaHora(vuelo.horaSalida)}</p>
        </div>

        <!-- Duración y línea -->
        <div class="text-center">
          <p class="text-xs text-gray-500 mb-1">${duracion}</p>
          <div class="flex items-center justify-center gap-2">
            <div class="h-px bg-gray-300 flex-1"></div>
            <svg class="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 5l7 7m0 0l-7 7m7-7H3"></path>
            </svg>
            <div class="h-px bg-gray-300 flex-1"></div>
          </div>
          <p class="text-xs text-gray-500 mt-1">Vuelo directo</p>
        </div>

        <!-- Destino -->
        <div class="text-right">
          <p class="text-2xl font-bold">${vuelo.ciudadLlegada.nombre}</p>
          <p class="text-sm text-gray-600">${vuelo.lugarLlegada}</p>
          <p class="text-sm font-medium mt-1">${formatearFechaHora(vuelo.horaLlegada)}</p>
        </div>
      </div>

      <!-- Información de asientos -->
      <div class="border-t pt-4 mt-4">
        <div class="flex justify-between items-center">
          <div class="flex gap-4 text-sm">
            <div class="flex items-center gap-2">
              <span class="w-3 h-3 bg-green-500 rounded-full"></span>
              <span>${disponibles} Disponibles</span>
            </div>
            <div class="flex items-center gap-2">
              <span class="w-3 h-3 bg-red-500 rounded-full"></span>
              <span>${ocupados} Ocupados</span>
            </div>
            <div class="flex items-center gap-2">
              <span class="w-3 h-3 bg-yellow-500 rounded-full"></span>
              <span>${seleccionados} Seleccionados</span>
            </div>
          </div>
          
          <div class="flex items-center gap-3">
            <p class="text-2xl font-bold text-blue-600">$${vuelo.avion.asientos[0]?.precio || 150}</p>
            <button onclick="seleccionarVuelo(${vuelo.idVuelo})" 
                    class="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors">
              Seleccionar
            </button>
          </div>
        </div>
      </div>

      <!-- Contacto aerolínea -->
      <div class="mt-3 text-xs text-gray-500 flex gap-4">
        <span>📧 ${vuelo.aerolinea.email}</span>
        <span>📱 ${vuelo.aerolinea.celular}</span>
      </div>
    </div>
  `;
}

// Función para cargar los vuelos
async function cargarVuelos() {
  const contenedor = document.getElementById('contenedor-vuelos');
  
  // Mostrar loading
  contenedor.innerHTML = `
    <div class="text-center py-8">
      <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      <p class="mt-4 text-gray-600">Cargando vuelos disponibles...</p>
    </div>
  `;

  try {
    const response = await fetch(API_URL, {
      method: 'GET',
      headers: {
        'accept': '*/*'
      }
    });

    if (!response.ok) {
      throw new Error(`Error HTTP: ${response.status}`);
    }

    const vuelos = await response.json();
    
    // Si es un solo vuelo, convertirlo en array
    const vuelosArray = Array.isArray(vuelos) ? vuelos : [vuelos];

    if (vuelosArray.length === 0) {
      contenedor.innerHTML = `
        <div class="text-center py-8">
          <p class="text-gray-600">No hay vuelos disponibles en este momento.</p>
        </div>
      `;
      return;
    }

    // Renderizar los vuelos
    contenedor.innerHTML = vuelosArray.map(vuelo => crearTarjetaVuelo(vuelo)).join('');

  } catch (error) {
    console.error('Error al cargar vuelos:', error);
    contenedor.innerHTML = `
      <div class="text-center py-8 bg-red-50 rounded-lg">
        <p class="text-red-600 font-medium">Error al cargar los vuelos</p>
        <p class="text-sm text-gray-600 mt-2">${error.message}</p>
        <button onclick="cargarVuelos()" 
                class="mt-4 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
          Reintentar
        </button>
      </div>
    `;
  }
}

// Función para manejar la selección de vuelo
function seleccionarVuelo(idVuelo) {
  console.log('Vuelo seleccionado:', idVuelo);
  // Aquí puedes agregar la lógica para redirigir o guardar el vuelo seleccionado
  // Por ejemplo: window.location.href = `/seleccionar-asiento?vuelo=${idVuelo}`;
  alert(`Vuelo #${idVuelo} seleccionado`);
}

// Cargar vuelos cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', cargarVuelos);

// Opcional: Recargar vuelos cada 30 segundos
// setInterval(cargarVuelos, 30000);
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

  // 🧠 Función para decodificar el JWT
  function parseJwt(token) {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (e) {
      console.error("❌ Error al decodificar el token:", e);
      return null;
    }
  }

  // 🧩 Verificar si hay un token en sessionStorage
  const token = sessionStorage.getItem("token");

  if (token) {
    const data = parseJwt(token);
    if (data && data.nombres) {
      // Tomar el nombre del usuario directamente
      const nombreCompleto = data.nombres;

      // Buscar el enlace al login
      const loginLink = document.querySelector('a[href="login.html"]');
      if (loginLink) {
        const loginButton = loginLink.closest("button");

        // Reemplazar contenido del botón por el nombre del usuario
        loginButton.innerHTML = `
          <span class="truncate flex items-center gap-2">
            <span class="material-symbols-outlined text-lg">person</span>
            ${nombreCompleto}
          </span>
        `;

        // (Opcional) añadir acción para cerrar sesión
        loginButton.addEventListener("click", () => {
          if (confirm("¿Deseas cerrar sesión?")) {
            sessionStorage.clear();
            window.location.reload();
          }
        });
      }
    }
  }
