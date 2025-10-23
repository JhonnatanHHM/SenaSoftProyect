// Temporary script to populate localStorage with sample data for testing
// Run this in the browser console or include it in a page to test the confirmation page

function populateSampleData() {
  // Sample flight data
  const sampleVuelos = [
    {
      idVuelo: 101,
      ciudad_salida: "Bogotá",
      ciudad_llegada: "Medellín",
      lugar_salida: "BOG",
      lugar_llegada: "MDE",
      hora_salida: "2025-10-25T08:30:00",
      hora_llegada: "2025-10-25T09:45:00",
      aerolinea: {
        nombre: "Avianca",
      },
    },
    {
      idVuelo: 102,
      ciudad_salida: "Cali",
      ciudad_llegada: "Cartagena",
      lugar_salida: "CLO",
      lugar_llegada: "CTG",
      hora_salida: "2025-10-26T14:15:00",
      hora_llegada: "2025-10-26T15:30:00",
      aerolinea: {
        nombre: "LATAM",
      },
    },
  ];

  // Sample seat selection data
  const sampleSelectedSeats = [
    {
      idAsiento: 15,
      nombre: "5B",
      precio: 75.5,
      estado: "SELECCIONADO",
    },
    {
      idAsiento: 16,
      nombre: "5C",
      precio: 75.5,
      estado: "SELECCIONADO",
    },
  ];

  // Sample passenger data
  const samplePasajeros = [
    {
      id_pasajero: 1,
      nombres: "Juan",
      primer_apellido: "Pérez",
      segundo_apellido: "García",
      tipo_documento: "Cédula",
      numero_documento: "123456789",
      asiento: {
        nombre: "5B",
      },
    },
    {
      id_pasajero: 2,
      nombres: "María",
      primer_apellido: "López",
      segundo_apellido: "Martínez",
      tipo_documento: "Pasaporte",
      numero_documento: "P987654321",
      asiento: {
        nombre: "5C",
      },
    },
  ];

  // Sample payment data
  const samplePago = {
    total: 151.0,
  };

  // Sample reservation data
  const sampleReserva = {
    vuelos: [sampleVuelos[0]], // Using the first flight
    pasajeros: samplePasajeros,
    selectedSeats: sampleSelectedSeats,
    pago: samplePago,
  };

  // Save to localStorage
  localStorage.setItem("vuelosCache", JSON.stringify(sampleVuelos));
  localStorage.setItem("selectedSeats", JSON.stringify(sampleSelectedSeats));
  localStorage.setItem("currentReservation", JSON.stringify(sampleReserva));
  localStorage.setItem("reservation_101", JSON.stringify(sampleReserva));

  // Save flight-specific data
  localStorage.setItem(`flight_101`, JSON.stringify(sampleVuelos[0]));
  localStorage.setItem(`flight_102`, JSON.stringify(sampleVuelos[1]));

  // Set the selected flight ID in sessionStorage
  sessionStorage.setItem("selectedFlightId", "101");

  console.log("Sample data populated in localStorage");
  console.log("Flight ID 101 data is now set as selected");
  console.log("You can now view the confirmation page to see the data");
}

// Function to test with different flight IDs
function testWithFlightId(flightId) {
  // Set the selected flight ID in sessionStorage
  sessionStorage.setItem("selectedFlightId", flightId);

  // Get the flight data
  const vuelosCache = JSON.parse(localStorage.getItem("vuelosCache") || "[]");
  const selectedFlight = vuelosCache.find((vuelo) => vuelo.idVuelo == flightId);

  if (selectedFlight) {
    // Save flight-specific data
    localStorage.setItem(`flight_${flightId}`, JSON.stringify(selectedFlight));
    console.log(`Flight data for ID ${flightId} is now set as selected`);
  } else {
    console.log(`Flight with ID ${flightId} not found in cache`);
  }
}

// Run the function to populate sample data
populateSampleData();

// To test with a different flight ID, uncomment the line below and change the ID
// testWithFlightId(102);
