// Configuración de la API
const API_URL = "https://senasoftproyect.onrender.com/api/vuelos/all";

// Tamaño de página (máximo de vuelos por página)
const PAGE_SIZE = 5;

// Función para formatear fecha y hora
function formatearFechaHora(fechaISO) {
  const fecha = new Date(fechaISO);
  const opciones = {
    year: "numeric",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  };
  return fecha.toLocaleDateString("es-ES", opciones);
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
function contarAsientos(asientos = []) {
  const disponibles = asientos.filter((a) => a.estado === "DISPONIBLE").length;
  const ocupados = asientos.filter((a) => a.estado === "OCUPADO").length;
  const seleccionados = asientos.filter(
    (a) => a.estado === "SELECCIONADO"
  ).length;

  return { disponibles, ocupados, seleccionados };
}

// Inyectar estilos para forzar ancho completo de las tarjetas y evitar overflow lateral
(function () {
  const style = document.createElement("style");
  style.innerHTML = `
    /* Asegura que el contenedor y las tarjetas usen 100% del ancho disponible */
    #contenedor-vuelos, #vuelos-list { width: 100% !important; max-width: 100% !important; box-sizing: border-box; }
    .flight-card { width: 100% !important; max-width: 100% !important; box-sizing: border-box; margin: 0 0 1rem 0; }
    .flight-card > .card-inner { width: 100% !important; box-sizing: border-box; }
    /* Evita que imágenes u otros elementos provoquen overflow */
    .flight-card img { max-width: 48px; height: auto; display: block; }
    /* En mobiles asegurar padding interno y que no se desborde */
    @media (max-width: 640px) {
      .flight-card { padding-left: 0; padding-right: 0; }
    }
    /* Asegurar que las tarjetas ocupen el ancho completo en todos los dispositivos */
    .w-full { width: 100% !important; }
    .max-w-full { max-width: 100% !important; }
    .box-border { box-sizing: border-box; }
    /* Forzar ancho completo en elementos anidados */
    .flight-card * { max-width: 100% !important; }
    
    /* Estilos uniformes para todos los inputs */
    input[type="text"],
    input[type="date"],
    #origen,
    #destino,
    #departure-date,
    #return-date {
      border: 2px solid #e5e7eb;
      border-radius: 0.5rem;
      padding: 0.75rem 1rem 0.75rem 2.5rem;
      font-size: 1rem;
      transition: all 0.2s ease;
      background-color: #ffffff;
      width: 100%;
      box-sizing: border-box;
    }
    
    input[type="text"]:focus,
    input[type="date"]:focus,
    #origen:focus,
    #destino:focus,
    #departure-date:focus,
    #return-date:focus {
      outline: none;
      border-color: #3b82f6;
      box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
    }
    
    input[type="text"]::placeholder,
    input[type="date"]::placeholder {
      color: #9ca3af;
    }
    
    /* Estilos para los iconos de los inputs */
    .input-wrapper {
      position: relative;
    }
    
    .input-icon {
      position: absolute;
      left: 1rem;
      top: 50%;
      transform: translateY(-50%);
      color: #9ca3af;
      z-index: 1;
    }
    
    /* Botón de búsqueda uniforme */
    button[type="submit"] {
      background-color: #3b82f6;
      color: white;
      border: none;
      border-radius: 0.5rem;
      padding: 0.75rem 1.5rem;
      font-size: 1rem;
      font-weight: 600;
      transition: background-color 0.2s ease;
      width: 100%;
      cursor: pointer;
    }
    
    button[type="submit"]:hover {
      background-color: #2563eb;
    }
    
    /* Estilo para las sugerencias de autocompletado */
    .suggestions {
      position: absolute;
      background: white;
      border: 1px solid #e5e7eb;
      border-radius: 0.5rem;
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
      z-index: 100;
      width: 100%;
      max-height: 200px;
      overflow-y: auto;
      margin-top: 0.25rem;
    }
    
    .suggestions div {
      padding: 0.75rem 1rem;
      cursor: pointer;
      transition: background-color 0.1s ease;
    }
    
    .suggestions div:hover {
      background-color: #f9fafb;
    }
    
    .no-results {
      padding: 0.75rem 1rem;
      color: #9ca3af;
    }
  `;
  document.head.appendChild(style);
})();

// Función para crear la tarjeta de vuelo (asegura ancho completo)
function crearTarjetaVuelo(vuelo) {
  const { disponibles, ocupados, seleccionados } = contarAsientos(
    vuelo.avion?.asientos || []
  );
  const duracion = calcularDuracion(vuelo.horaSalida, vuelo.horaLlegada);
  const precio = vuelo.avion?.asientos?.[0]?.precio || 150;
  const imagen = vuelo.aerolinea?.imagen?.keyR2 || "";

  return `
    <div class="flight-card w-full max-w-full box-border">
      <div class="card-inner w-full bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
        <!-- Header con aerolínea -->
        <div class="flex flex-wrap items-center justify-between mb-4">
          <div class="flex items-center gap-4">
            <img src="${imagen}"
                 alt="${vuelo.aerolinea?.nombre || "Aerolínea"}"
                 class="w-20 h-20 object-contain rounded max-w-full" 
                 onerror="this.src='https://via.placeholder.com/80x80?text=No+Image'" />
            <div>
              <h3 class="font-bold text-lg">${
                vuelo.aerolinea?.nombre || "Aerolínea"
              }</h3>
              <p class="text-sm text-gray-600">${vuelo.avion?.modelo || ""}</p>
            </div>
          </div>
          <div class="text-right mt-2 md:mt-0">
            <p class="text-xs text-gray-500">Vuelo #${vuelo.idVuelo}</p>
            <p class="text-xs text-gray-500">Capacidad: ${
              vuelo.avion?.capacidad || "-"
            }</p>
          </div>
        </div>

        <!-- Ruta de vuelo -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-center mb-4">
          <!-- Origen -->
          <div class="text-center md:text-left">
            <p class="text-2xl font-bold">${
              vuelo.ciudadSalida?.nombre || ""
            }</p>
            <p class="text-sm text-gray-600">${vuelo.lugarSalida || ""}</p>
            <p class="text-sm font-medium mt-1">${formatearFechaHora(
              vuelo.horaSalida
            )}</p>
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
          <div class="text-center md:text-right">
            <p class="text-2xl font-bold">${
              vuelo.ciudadLlegada?.nombre || ""
            }</p>
            <p class="text-sm text-gray-600">${vuelo.lugarLlegada || ""}</p>
            <p class="text-sm font-medium mt-1">${formatearFechaHora(
              vuelo.horaLlegada
            )}</p>
          </div>
        </div>

        <!-- Información de asientos -->
        <div class="border-t pt-4 mt-4">
          <div class="flex flex-col md:flex-row md:justify-between items-center gap-4">
            <div class="flex flex-wrap gap-4 text-sm justify-center md:justify-start">
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
              <p class="text-2xl font-bold text-blue-600">$${precio}</p>
              <button onclick="seleccionarVuelo(${vuelo.idVuelo})"
                      class="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors whitespace-nowrap">
                Seleccionar
              </button>
            </div>
          </div>
        </div>

        <!-- Contacto aerolínea -->
        <div class="mt-3 text-xs text-gray-500 flex flex-wrap gap-4 justify-center md:justify-start">
          <span>📧 ${vuelo.aerolinea?.email || "-"}</span>
          <span>📱 ${vuelo.aerolinea?.celular || "-"}</span>
        </div>
      </div>
    </div>
  `;
}

// Función para cargar los vuelos
async function cargarVuelos() {
  const contenedor = document.getElementById("contenedor-vuelos");

  contenedor.innerHTML = `
    <div class="text-center py-8">
      <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      <p class="mt-4 text-gray-600">Cargando vuelos disponibles...</p>
    </div>
  `;

  try {
    const response = await fetch(API_URL, {
      method: "GET",
      headers: {
        accept: "*/*",
      },
    });

    if (!response.ok) {
      throw new Error(`Error HTTP: ${response.status}`);
    }

    const vuelos = await response.json();
    vuelosCache = Array.isArray(vuelos) ? vuelos : [vuelos]; // Guardamos globalmente

    // Guardar vuelos en localStorage
    try {
      localStorage.setItem("vuelosCache", JSON.stringify(vuelosCache));
    } catch (storageError) {
      console.warn(
        "No se pudo guardar los vuelos en localStorage:",
        storageError
      );
    }

    if (vuelosCache.length === 0) {
      contenedor.innerHTML = `
        <div class="text-center py-8">
          <p class="text-gray-600">No hay vuelos disponibles en este momento.</p>
        </div>
      `;
      return;
    }

    renderVuelosPaginados(contenedor, vuelosCache, PAGE_SIZE);
  } catch (error) {
    console.error("Error al cargar vuelos:", error);

    // Intentar cargar vuelos desde localStorage como fallback
    try {
      const storedVuelos = localStorage.getItem("vuelosCache");
      if (storedVuelos) {
        vuelosCache = JSON.parse(storedVuelos);
        if (vuelosCache.length > 0) {
          contenedor.innerHTML = `
            <div class="text-center py-8 bg-yellow-50 rounded-lg">
              <p class="text-yellow-700 font-medium">Mostrando datos guardados. Conéctate a internet para obtener datos actualizados.</p>
            </div>
          `;
          renderVuelosPaginados(contenedor, vuelosCache, PAGE_SIZE);
          return;
        }
      }
    } catch (parseError) {
      console.warn(
        "No se pudieron cargar los vuelos desde localStorage:",
        parseError
      );
    }

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

// Renderiza vuelos paginados (cada tarjeta ocupa ancho completo)
function renderVuelosPaginados(rootEl, vuelos, pageSize = PAGE_SIZE) {
  const totalItems = vuelos.length;
  const totalPages = Math.max(1, Math.ceil(totalItems / pageSize));
  let currentPage = 1;

  // contenedor base
  rootEl.innerHTML = `
    <div id="vuelos-list" class="flex flex-col gap-6 w-full"></div>
    <div id="vuelos-pagination" class="mt-6 flex flex-wrap items-center justify-center gap-2 w-full"></div>
  `;

  const listEl = document.getElementById("vuelos-list");
  const pagEl = document.getElementById("vuelos-pagination");

  function renderPage(page) {
    currentPage = Math.min(Math.max(1, page), totalPages);
    const start = (currentPage - 1) * pageSize;
    const pageItems = vuelos.slice(start, start + pageSize);

    listEl.innerHTML = pageItems.map((v) => crearTarjetaVuelo(v)).join("");

    renderPagination();
    // Scroll to top of flight results container
    document
      .getElementById("contenedor-vuelos")
      .scrollIntoView({ behavior: "smooth" });
  }

  function renderPagination() {
    pagEl.innerHTML = "";

    // Prev
    const prev = document.createElement("button");
    prev.type = "button";
    prev.className = `px-3 py-2 rounded ${
      currentPage === 1
        ? "opacity-50 cursor-not-allowed"
        : "bg-gray-100 hover:bg-gray-200"
    }`;
    prev.innerText = "Anterior";
    prev.disabled = currentPage === 1;
    prev.addEventListener("click", () => {
      renderPage(currentPage - 1);
    });
    pagEl.appendChild(prev);

    // Páginas (mostrar máximo 7 botones con truncamiento si es necesario)
    const maxButtons = 7;
    let startPage = Math.max(1, currentPage - Math.floor(maxButtons / 2));
    let endPage = Math.min(totalPages, startPage + maxButtons - 1);
    if (endPage - startPage < maxButtons - 1) {
      startPage = Math.max(1, endPage - maxButtons + 1);
    }

    for (let p = startPage; p <= endPage; p++) {
      const btn = document.createElement("button");
      btn.type = "button";
      btn.className = `px-3 py-2 rounded ${
        p === currentPage
          ? "bg-blue-600 text-white"
          : "bg-gray-100 text-gray-700 hover:bg-gray-200"
      }`;
      btn.innerText = p;
      btn.addEventListener("click", () => {
        renderPage(p);
      });
      pagEl.appendChild(btn);
    }

    // Next
    const next = document.createElement("button");
    next.type = "button";
    next.className = `px-3 py-2 rounded ${
      currentPage === totalPages
        ? "opacity-50 cursor-not-allowed"
        : "bg-gray-100 hover:bg-gray-200"
    }`;
    next.innerText = "Siguiente";
    next.disabled = currentPage === totalPages;
    next.addEventListener("click", () => {
      renderPage(currentPage + 1);
    });
    pagEl.appendChild(next);

    // info resumen
    const info = document.createElement("div");
    info.className = "ml-4 text-sm text-gray-600";
    info.innerText = `Mostrando ${Math.min(
      pageSize,
      totalItems - (currentPage - 1) * pageSize
    )} de ${totalItems} vuelos`;
    pagEl.appendChild(info);
  }

  // render inicial
  renderPage(1);
}

// Función para manejar la selección de vuelo
function seleccionarVuelo(idVuelo) {
  console.log("Vuelo seleccionado:", idVuelo);

  // Verificar si el usuario está logueado
  const token = sessionStorage.getItem("token");

  if (!token) {
    // Si no está logueado, mostrar mensaje y redirigir al login
    Swal.fire({
      title: "Acceso requerido",
      text: "Debes iniciar sesión para seleccionar un vuelo.",
      icon: "warning",
      confirmButtonText: "Iniciar sesión",
      showCancelButton: true,
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.href = "login.html";
      }
    });
    return;
  }

  // Si está logueado, proceder con la selección del vuelo
  // Guardar el ID del vuelo en sessionStorage
  sessionStorage.setItem("selectedFlightId", idVuelo);

  // Redirigir a la página de asientos
  window.location.href = "asientos.html";
}

// Variable global para guardar vuelos
let vuelosCache = [];

// Cargar vuelos cuando el DOM esté listo
document.addEventListener("DOMContentLoaded", () => {
  // Intentar cargar vuelos desde localStorage primero para una carga más rápida
  try {
    const storedVuelos = localStorage.getItem("vuelosCache");
    if (storedVuelos) {
      vuelosCache = JSON.parse(storedVuelos);
      if (vuelosCache.length > 0) {
        const contenedor = document.getElementById("contenedor-vuelos");
        renderVuelosPaginados(contenedor, vuelosCache, PAGE_SIZE);
        // Aún así, cargar vuelos actualizados desde la API
        setTimeout(cargarVuelos, 1000);
        return;
      }
    }
  } catch (error) {
    console.warn(
      "No se pudieron cargar los vuelos desde localStorage al inicio:",
      error
    );
  }

  // Si no hay vuelos en localStorage, cargar desde la API
  cargarVuelos();
});

// Opcional: Recargar vuelos cada 30 segundos
// setInterval(cargarVuelos, 30000);
tailwind.config = {
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        primary: "#00529B",
        secondary: "#F0F2F5",
        accent: {
          orange: "#FF6B00",
          green: "#00BFA5",
        },
        "text-dark": "#333333",
        "background-light": "#f6f7f8",
        "background-dark": "#101c22",
      },
      fontFamily: {
        display: ["Plus Jakarta Sans", "sans-serif"],
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

// 🧠 Función para decodificar el JWT
function parseJwt(token) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
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
      if (loginButton) {
        loginButton.innerHTML = `
          <span class="truncate flex items-center gap-2">
            <span class="material-symbols-outlined text-lg">person</span>
            ${nombreCompleto}
          </span>
        `;

        // (Opcional) añadir acción para cerrar sesión
        loginButton.addEventListener("click", () => {
          Swal.fire({
            title: "¿Cerrar sesión?",
            text: "¿Estás seguro de que deseas cerrar sesión?",
            icon: "question",
            showCancelButton: true,
            confirmButtonText: "Sí, cerrar sesión",
            cancelButtonText: "Cancelar",
            reverseButtons: true,
          }).then((result) => {
            if (result.isConfirmed) {
              sessionStorage.clear();
              Swal.fire({
                title: "Sesión cerrada",
                text: "Tu sesión se ha cerrado correctamente.",
                icon: "success",
                confirmButtonText: "Aceptar",
              }).then(() => {
                window.location.reload();
              });
            }
          });
        });
      }
    }
  }
}

// Función para filtrar vuelos en memoria según filtros
function filtrarVuelosLocal(
  origen,
  destino,
  fechaSalida,
  fechaRegreso,
  enableReturn
) {
  return vuelosCache.filter((vuelo) => {
    // Ajusta aquí según tu estructura real de vuelo y campos de fecha
    const matchOrigen = origen
      ? vuelo.ciudadSalida?.nombre
          ?.toLowerCase()
          .includes(origen.toLowerCase()) ||
        vuelo.lugarSalida?.toLowerCase().includes(origen.toLowerCase())
      : true;
    const matchDestino = destino
      ? vuelo.ciudadLlegada?.nombre
          ?.toLowerCase()
          .includes(destino.toLowerCase()) ||
        vuelo.lugarLlegada?.toLowerCase().includes(destino.toLowerCase())
      : true;

    // Para las fechas, comparamos solo la parte de la fecha (sin hora)
    let matchFechaSalida = true;
    if (fechaSalida) {
      const vueloFechaSalida = new Date(vuelo.horaSalida);
      const filtroFechaSalida = new Date(fechaSalida);
      vueloFechaSalida.setHours(0, 0, 0, 0);
      filtroFechaSalida.setHours(0, 0, 0, 0);
      matchFechaSalida =
        vueloFechaSalida.getTime() === filtroFechaSalida.getTime();
    }

    let matchFechaRegreso = true;
    if (enableReturn && fechaRegreso) {
      const vueloFechaRegreso = new Date(vuelo.horaLlegada);
      const filtroFechaRegreso = new Date(fechaRegreso);
      vueloFechaRegreso.setHours(0, 0, 0, 0);
      filtroFechaRegreso.setHours(0, 0, 0, 0);
      matchFechaRegreso =
        vueloFechaRegreso.getTime() === filtroFechaRegreso.getTime();
    }

    return matchOrigen && matchDestino && matchFechaSalida && matchFechaRegreso;
  });
}

// Funciones para obtener orígenes y destinos únicos para autocompletar
function obtenerOrigenes(query = "") {
  const lowerQuery = query.toLowerCase();
  const origenes = [
    ...new Set(
      vuelosCache
        .map((v) => v.ciudadSalida?.nombre || v.lugarSalida)
        .filter(Boolean)
    ),
  ];
  return origenes
    .filter((o) => o.toLowerCase().includes(lowerQuery))
    .map((nombre) => ({ codigo: nombre, ciudad: nombre }));
}

function obtenerDestinos(query = "") {
  const lowerQuery = query.toLowerCase();
  const destinos = [
    ...new Set(
      vuelosCache
        .map((v) => v.ciudadLlegada?.nombre || v.lugarLlegada)
        .filter(Boolean)
    ),
  ];
  return destinos
    .filter((d) => d.toLowerCase().includes(lowerQuery))
    .map((nombre) => ({ codigo: nombre, ciudad: nombre }));
}

// Escuchar el formulario y manejar el filtro local
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formBusqueda");
  const enableReturn = document.getElementById("enable-return");
  const returnDateInput = document.getElementById("return-date");
  const contenedor = document.getElementById("contenedor-vuelos");
  const origenInput = document.getElementById("origen");
  const destinoInput = document.getElementById("destino");
  const departureDateInput = document.getElementById("departure-date");
  const returnDate = document.getElementById("return-date");

  // Deshabilitar fecha de regreso si el checkbox está desmarcado
  enableReturn.addEventListener("change", () => {
    returnDateInput.disabled = !enableReturn.checked;
    if (!enableReturn.checked) {
      returnDateInput.value = "";
    }
  });

  // Al enviar formulario, filtrar vuelos en memoria
  form.addEventListener("submit", (e) => {
    e.preventDefault();
    filtrarVuelos();
  });

  // Mostrar todos los vuelos cuando se limpian los campos de búsqueda
  origenInput.addEventListener("input", () => {
    if (
      !origenInput.value.trim() &&
      !destinoInput.value.trim() &&
      !departureDateInput.value
    ) {
      renderVuelosPaginados(contenedor, vuelosCache, PAGE_SIZE);
    }
  });

  destinoInput.addEventListener("input", () => {
    if (
      !origenInput.value.trim() &&
      !destinoInput.value.trim() &&
      !departureDateInput.value
    ) {
      renderVuelosPaginados(contenedor, vuelosCache, PAGE_SIZE);
    }
  });

  // Función para filtrar vuelos
  function filtrarVuelos() {
    const origen = origenInput.value.trim();
    const destino = destinoInput.value.trim();
    const fechaSalida = departureDateInput.value;
    const fechaRegreso = returnDate.value;

    contenedor.innerHTML = `
      <div class="text-center py-8">
        <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        <p class="mt-4 text-gray-600">Buscando vuelos...</p>
      </div>
    `;

    // Filtrar vuelos desde vuelosCache
    setTimeout(() => {
      // simular async para mostrar loading
      // Si todos los campos están vacíos, mostrar todos los vuelos
      if (!origen && !destino && !fechaSalida && !fechaRegreso) {
        renderVuelosPaginados(contenedor, vuelosCache, PAGE_SIZE);
        return;
      }

      const vuelosFiltrados = filtrarVuelosLocal(
        origen,
        destino,
        fechaSalida,
        fechaRegreso,
        enableReturn.checked
      );

      if (vuelosFiltrados.length === 0) {
        contenedor.innerHTML = `
          <div class="text-center py-8">
            <p class="text-gray-600 font-medium">No se encontraron vuelos con esos filtros.</p>
          </div>
        `;
        return;
      }

      renderVuelosPaginados(contenedor, vuelosFiltrados, PAGE_SIZE);
    }, 500);
  }
});

// Autocompletado usando datos locales
document.addEventListener("DOMContentLoaded", () => {
  const origenInput = document.getElementById("origen");
  const destinoInput = document.getElementById("destino");

  const origenSuggestions = document.getElementById("origen-suggestions");
  const destinoSuggestions = document.getElementById("destino-suggestions");

  let selectedOrigen = "";

  // Autocomplete Origen
  origenInput.addEventListener("input", () => {
    const query = origenInput.value.trim();
    selectedOrigen = "";
    destinoInput.value = "";
    destinoInput.disabled = true;
    destinoSuggestions.innerHTML = "";

    if (!query) {
      origenSuggestions.innerHTML = "";
      return;
    }

    const opciones = obtenerOrigenes(query);

    if (opciones.length === 0) {
      origenSuggestions.innerHTML = `<div class="no-results p-2 text-gray-500">No se encontraron opciones</div>`;
      return;
    }

    origenSuggestions.innerHTML = opciones
      .map(
        (item) =>
          `<div class="p-2 hover:bg-gray-100 cursor-pointer" data-value="${item.codigo}">${item.ciudad}</div>`
      )
      .join("");

    origenSuggestions.querySelectorAll("div").forEach((div) => {
      div.addEventListener("click", () => {
        origenInput.value = div.dataset.value;
        selectedOrigen = div.dataset.value;
        origenSuggestions.innerHTML = "";
        destinoInput.disabled = false;
        destinoInput.focus();
      });
    });
  });

  // Autocomplete Destino
  destinoInput.addEventListener("input", () => {
    const query = destinoInput.value.trim();
    if (!query || !selectedOrigen) {
      destinoSuggestions.innerHTML = "";
      return;
    }

    const opciones = obtenerDestinosPorOrigen(selectedOrigen, query);

    if (opciones.length === 0) {
      destinoSuggestions.innerHTML = `<div class="no-results p-2 text-gray-500">No se encontraron opciones</div>`;
      return;
    }

    destinoSuggestions.innerHTML = opciones
      .map(
        (item) =>
          `<div class="p-2 hover:bg-gray-100 cursor-pointer" data-value="${item.codigo}">${item.ciudad}</div>`
      )
      .join("");

    destinoSuggestions.querySelectorAll("div").forEach((div) => {
      div.addEventListener("click", () => {
        destinoInput.value = div.dataset.value;
        destinoSuggestions.innerHTML = "";
      });
    });
  });

  // Cerrar sugerencias al hacer clic fuera
  document.addEventListener("click", (e) => {
    if (
      !origenInput.contains(e.target) &&
      !origenSuggestions.contains(e.target)
    ) {
      origenSuggestions.innerHTML = "";
    }
    if (
      !destinoInput.contains(e.target) &&
      !destinoSuggestions.contains(e.target)
    ) {
      destinoSuggestions.innerHTML = "";
    }
  });
});

// Función para obtener destinos disponibles para un origen específico
function obtenerDestinosPorOrigen(origen, query = "") {
  const lowerQuery = query.toLowerCase();

  // Filtrar vuelos por el origen seleccionado
  const vuelosDesdeOrigen = vuelosCache.filter(
    (vuelo) =>
      vuelo.ciudadSalida?.nombre === origen || vuelo.lugarSalida === origen
  );

  // Obtener destinos únicos de esos vuelos
  const destinos = [
    ...new Set(
      vuelosDesdeOrigen
        .map((v) => v.ciudadLlegada?.nombre || v.lugarLlegada)
        .filter(Boolean)
    ),
  ];

  // Filtrar por la consulta si existe
  return destinos
    .filter((d) => d.toLowerCase().includes(lowerQuery))
    .map((nombre) => ({ codigo: nombre, ciudad: nombre }));
}

// Enforce: departure >= today, return >= departure and return <= departure + 2 months

(function () {
  const dep = document.getElementById('departure-date');
  const ret = document.getElementById('return-date');
  const enableReturn = document.getElementById('enable-return');

  if (!dep || !ret) {
    console.warn('date_constraints: elementos #departure-date o #return-date no encontrados.');
    return;
  }

  const toISO = (d) => {
    const tzOffset = d.getTimezoneOffset() * 60000;
    return new Date(d - tzOffset).toISOString().slice(0, 10);
  };

  const addMonths = (date, months) => {
    const d = new Date(date);
    const day = d.getDate();
    d.setMonth(d.getMonth() + months);

    // Ajuste si el mes nuevo no tiene el mismo día (ej. 31 -> 30/Feb)
    if (d.getDate() < day) {
      d.setDate(0); // último día del mes anterior
    }
    return d;
  };

  const today = (() => {
    const n = new Date();
    n.setHours(0, 0, 0, 0);
    return n;
  })();

  // Inicializar min para departure
  dep.min = toISO(today);

  function updateReturnConstraints() {
    if (!dep.value) {
      // Si no hay departure elegido, return mínimo = today
      ret.min = dep.min;
      ret.max = toISO(addMonths(new Date(ret.min), 2));
      return;
    }
    const depDate = new Date(dep.value + 'T00:00:00');
    const minRet = depDate;
    const maxRet = addMonths(depDate, 2);

    ret.min = toISO(minRet);
    ret.max = toISO(maxRet);

    // Si return está fuera de rango, ajustar
    if (ret.value) {
      const cur = new Date(ret.value + 'T00:00:00');
      if (cur < minRet) ret.value = ret.min;
      else if (cur > maxRet) ret.value = ret.max;
    }
  }

  // Toggle return enabled/disabled with checkbox (if present)
  function updateReturnEnabled() {
    if (enableReturn) {
      if (!enableReturn.checked) {
        ret.disabled = true;
        // opcional: borrar valor
        // ret.value = '';
      } else {
        ret.disabled = false;
      }
    }
  }

  // Listeners
  dep.addEventListener('change', () => {
    // evitar fechas anteriores por si el navegador no aplica min
    if (dep.value) {
      const chosen = new Date(dep.value + 'T00:00:00');
      if (chosen < today) dep.value = toISO(today);
    }
    updateReturnConstraints();
  });

  ret.addEventListener('change', () => {
    // validar que no se pueda poner antes de dep o después de dep+2meses (por si navegador no aplica)
    updateReturnConstraints();
  });

  if (enableReturn) {
    enableReturn.addEventListener('change', () => {
      updateReturnEnabled();
      // si se activa, actualizar constraints inmediatamente
      if (enableReturn.checked) updateReturnConstraints();
    });
  }

  // Inicializar estado al cargar la página
  document.addEventListener('DOMContentLoaded', () => {
    // si hay un valor inicial en departure, garantizar que cumpla
    if (dep.value) {
      const chosen = new Date(dep.value + 'T00:00:00');
      if (chosen < today) dep.value = toISO(today);
    } else {
      // opcional: setear departure a today por defecto
      // dep.value = toISO(today);
    }

    updateReturnEnabled();
    updateReturnConstraints();
  });

  // Ejecutar ahora por si el script se carga después de DOMReady
  if (document.readyState !== 'loading') {
    updateReturnEnabled();
    updateReturnConstraints();
  }
})();

/* =========================
   Mostrar vuelos más cercanos a la fecha seleccionada
   ========================= */

function parseFlightDate(v) {
  if (!v) return null;
  const candidates = [
    v.fechaSalida, v.horaSalida, v.salida,
    v.departureDate, v.departureTime, v.departure,
    v.fecha, v.date, v.datetime
  ];
  for (const c of candidates) {
    if (!c) continue;
    const d = new Date(c);
    if (!isNaN(d)) return d;
    // intentar si es YYYY-MM-DD sin hora
    if (/^\d{4}-\d{2}-\d{2}$/.test(String(c))) return new Date(c + 'T00:00:00');
  }
  return null;
}

function toISODateOnly(date) {
  if (!date) return '';
  const d = new Date(date);
  d.setHours(0,0,0,0);
  const tz = d.getTimezoneOffset()*60000;
  return new Date(d - tz).toISOString().slice(0,10);
}

function findNearestFlights(targetISO, maxResults = 5) {
  if (!targetISO || !Array.isArray(vuelosCache)) return [];
  const target = new Date(targetISO + 'T00:00:00');
  const list = vuelosCache
    .map(v => {
      const fd = parseFlightDate(v);
      return { vuelo: v, date: fd, diff: fd ? Math.abs(fd - target) : Infinity };
    })
    .filter(x => x.date && isFinite(x.diff))
    .sort((a,b) => a.diff - b.diff)
    .slice(0, maxResults)
    .map(x => x.vuelo);
  return list;
}

function renderNearestFlightsForDate(targetISO) {
  const container = document.getElementById('contenedor-vuelos');
  if (!container) return;
  container.innerHTML = ''; // limpiar resultados anteriores

  if (!targetISO) {
    container.innerHTML = '<p class="text-gray-600">Selecciona una fecha de salida para ver vuelos cercanos.</p>';
    return;
  }

  const nearest = findNearestFlights(targetISO, 10);
  if (!nearest.length) {
    container.innerHTML = '<p class="text-red-600">No se encontraron vuelos cerca de esa fecha.</p>';
    return;
  }

  // si existe la función crearTarjetaVuelo en este archivo, usarla para cada vuelo
  if (typeof crearTarjetaVuelo === 'function') {
    nearest.forEach(v => {
      const card = crearTarjetaVuelo(v);
      if (card instanceof HTMLElement) container.appendChild(card);
      else container.insertAdjacentHTML('beforeend', card);
    });
    return;
  }

  // fallback simple: mostrar información básica
  nearest.forEach(v => {
    const origen = v.origen || v.origin || v.ciudadOrigen || v.departure || 'Origen';
    const destino = v.destino || v.destination || v.ciudadDestino || v.arrival || 'Destino';
    const fecha = toISODateOnly(parseFlightDate(v)) || 'Fecha desconocida';
    const precio = v.precio || v.price || v.tarifa || '—';

    const html = `
      <article class="p-4 bg-white rounded-lg shadow mb-3">
        <div class="flex justify-between items-center">
          <div>
            <div class="font-bold text-lg">${origen} ➝ ${destino}</div>
            <div class="text-sm text-gray-600">Fecha: ${fecha}</div>
          </div>
          <div class="text-right">
            <div class="font-extrabold text-sky-600">${precio ? '$' + precio : '—'}</div>
            <button class="mt-2 px-3 py-1 rounded bg-sky-500 text-white" onclick="seleccionarVuelo('${v.id || v.idVuelo || v.codigo || ''}')">Seleccionar</button>
          </div>
        </div>
      </article>
    `;
    container.insertAdjacentHTML('beforeend', html);
  });
}

/* Hook: cuando el usuario cambia la fecha de salida mostramos los vuelos más cercanos */
document.addEventListener('DOMContentLoaded', () => {
  const dep = document.getElementById('departure-date');
  if (!dep) return;

  dep.addEventListener('change', () => {
    // actualizar disponibilidad (si ya tienes esa función)
    if (typeof updateDateAvailabilityUI === 'function') updateDateAvailabilityUI();
    // renderizar vuelos cercanos
    renderNearestFlightsForDate(dep.value);
  });

  // si ya hay un valor al cargar la página
  if (dep.value) renderNearestFlightsForDate(dep.value);
});


