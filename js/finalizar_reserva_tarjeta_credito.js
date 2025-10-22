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
       
    async function validarToken() {
  const token = localStorage.getItem('token');

  if (!token) {
    window.location.href = 'login.html';
    return;
  }

  // Validar contra el backend
  const res = await fetch('http://localhost:8080/api/auth/validate', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });

  if (!res.ok) {
    // Token inválido o expirado
    localStorage.removeItem('token');
    window.location.href = 'login.html';
  }
}

// Ejecutar al cargar la página
validarToken();

// Handle payment confirmation
document.addEventListener('DOMContentLoaded', function() {
    const confirmPaymentButton = document.getElementById('confirmPaymentButton');
    if (confirmPaymentButton) {
        confirmPaymentButton.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Get payment data from form
            const cardNumber = document.querySelector('input[placeholder="0000 0000 0000 0000"]')?.value;
            const cardName = document.querySelector('input[placeholder="John Doe"]')?.value;
            const payerName = document.querySelector('input[placeholder="Ingrese el nombre completo"]')?.value;
            const payerEmail = document.querySelector('input[type="email"]')?.value;
            
            // Validate required fields
            if (!cardNumber || !cardName || !payerName || !payerEmail) {
                alert('Por favor complete todos los campos requeridos.');
                return;
            }
            
            // Show confirmation alert
            alert('¡Pago procesado exitosamente! Su reserva ha sido confirmada.');
            
            // Redirect to confirmation page
            window.location.href = 'reserva_confirmada.html';
        });
    }
});