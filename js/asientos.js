tailwind.config = {
      darkMode: "class",
      theme: {
        extend: {
          colors: {
            "primary": "#13a4ec",
            "background-light": "#f6f7f8",
            "background-dark": "#101c22",
          },
          fontFamily: {
            "display": ["Plus Jakarta Sans", "Noto Sans", "sans-serif"]
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
    
  // Global variables
  let selectedSeats = [];
  const maxSeats = 5;
  let seatsData = [];
  
  // Function to fetch seats from the API
  async function fetchSeats() {
    const loadingIndicator = document.getElementById('loadingIndicator');
    const errorMessage = document.getElementById('errorMessage');
    const seatsContent = document.getElementById('seatsContent');
    
    // Show loading indicator
    loadingIndicator.classList.remove('hidden');
    errorMessage.classList.add('hidden');
    seatsContent.classList.add('hidden');
    
    try {
      // Replace with your actual Spring Boot API endpoint
      // Example: http://localhost:8080/api/vuelos/{id_vuelo}/asientos
      const response = await fetch('http://localhost:8080/api/vuelos/1/asientos'); // Assuming flight ID 1 for now
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      seatsData = await response.json();
      
      // Hide loading indicator
      loadingIndicator.classList.add('hidden');
      
      // Render seats
      renderSeats(seatsData);
      seatsContent.classList.remove('hidden');
    } catch (error) {
      console.error('Error fetching seats:', error);
      loadingIndicator.classList.add('hidden');
      errorMessage.classList.remove('hidden');
    }
  }
  
  // Function to render seats
  function renderSeats(seats) {
    const seatRows = document.getElementById('seatRows');
    
    // Clear existing seats
    seatRows.innerHTML = '';
    
    // Group seats by row
    const rows = {};
    seats.forEach(seat => {
      const rowNumber = seat.nombre.match(/\d+/)[0];
      if (!rows[rowNumber]) {
        rows[rowNumber] = [];
      }
      rows[rowNumber].push(seat);
    });
    
    // Sort rows
    const sortedRows = Object.keys(rows).sort((a, b) => parseInt(a) - parseInt(b));
    
    // Render each row
    sortedRows.forEach(rowNumber => {
      const rowSeats = rows[rowNumber];
      
      // Create row container
      const rowContainer = document.createElement('div');
      rowContainer.className = 'contents';
      
      // Left side (A, B, C)
      const leftSide = document.createElement('div');
      leftSide.className = 'flex justify-around';
      
      // Right side (D, E, F)
      const rightSide = document.createElement('div');
      rightSide.className = 'flex justify-around';
      
      // Row number
      const rowLabel = document.createElement('div');
      rowLabel.className = 'text-gray-500 dark:text-gray-400 font-bold self-center';
      rowLabel.textContent = rowNumber;
      
      // Process seats for this row
      rowSeats.forEach(seat => {
        const seatElement = document.createElement('div');
        seatElement.className = `seat ${seat.estado.toLowerCase()}`;
        seatElement.textContent = seat.nombre;
        seatElement.dataset.id = seat.id_asiento;
        seatElement.dataset.price = seat.precio || 0;
        
        // Add click event for available seats
        if (seat.estado === 'Disponible') {
          seatElement.addEventListener('click', () => toggleSeatSelection(seat));
        }
        
        // Add seat to appropriate side
        if (seat.nombre.endsWith('A') || seat.nombre.endsWith('B') || seat.nombre.endsWith('C')) {
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
    const seatElement = document.querySelector(`.seat[data-id="${seat.id_asiento}"]`);
    
    // Check if seat is already selected
    const selectedIndex = selectedSeats.findIndex(s => s.id_asiento === seat.id_asiento);
    
    if (selectedIndex !== -1) {
      // Deselect seat
      selectedSeats.splice(selectedIndex, 1);
      seatElement.classList.remove('selected');
      seatElement.classList.add('available');
    } else {
      // Check if we've reached the maximum number of seats
      if (selectedSeats.length >= maxSeats) {
        alert(`Solo puedes seleccionar un máximo de ${maxSeats} asientos.`);
        return;
      }
      
      // Select seat
      selectedSeats.push(seat);
      seatElement.classList.remove('available');
      seatElement.classList.add('selected');
    }
    
    // Update summary
    updateSummary();
  }
  
  // Function to update selection summary
  function updateSummary() {
    const selectedSeatsElement = document.getElementById('selectedSeats');
    const seatPriceElement = document.getElementById('seatPrice');
    const totalCostElement = document.getElementById('totalCost');
    const confirmButton = document.getElementById('confirmButton');
    
    if (selectedSeats.length === 0) {
      selectedSeatsElement.textContent = 'Ninguno';
      seatPriceElement.textContent = '$0.00';
      totalCostElement.textContent = '$0.00';
      confirmButton.disabled = true;
    } else {
      // Show selected seats
      const seatNames = selectedSeats.map(seat => seat.nombre).join(', ');
      selectedSeatsElement.textContent = seatNames;
      
      // Calculate total cost
      const totalPrice = selectedSeats.reduce((sum, seat) => sum + (parseFloat(seat.precio) || 0), 0);
      
      // Show price information (assuming all seats have the same price for simplicity)
      const pricePerSeat = selectedSeats.length > 0 ? parseFloat(selectedSeats[0].precio) || 0 : 0;
      seatPriceElement.textContent = `$${pricePerSeat.toFixed(2)}`;
      totalCostElement.textContent = `$${totalPrice.toFixed(2)}`;
      
      // Enable confirm button
      confirmButton.disabled = false;
    }
  }
  
  // Add event listener to confirm button
  document.addEventListener('DOMContentLoaded', function() {
    const confirmButton = document.getElementById('confirmButton');
    confirmButton.addEventListener('click', function() {
      if (selectedSeats.length > 0) {
        // Here you would typically send the selected seats to your backend
        // For now, we'll just redirect to the confirmation page
        alert('Asientos seleccionados: ' + selectedSeats.map(s => s.nombre).join(', '));
        window.location.href = 'confimacion_de_vuelo.html';
      }
    });
    
    // Fetch seats when page loads
    fetchSeats();
  });
