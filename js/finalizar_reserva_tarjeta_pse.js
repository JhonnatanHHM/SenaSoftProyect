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

// Handle PSE payment confirmation
document.addEventListener('DOMContentLoaded', function() {
    const confirmPsePaymentButton = document.getElementById('confirmPsePaymentButton');
    if (confirmPsePaymentButton) {
        confirmPsePaymentButton.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Get payment data from form
            const documentType = document.querySelector('select')?.value;
            const documentNumber = document.querySelector('input[placeholder="12345678"]')?.value;
            const phoneLastDigits = document.querySelector('input[placeholder="3054"]')?.value;
            const payerName = document.querySelector('input[placeholder="Ingrese el nombre completo"]')?.value;
            const payerEmail = document.querySelector('input[type="email"]')?.value;
            
            // Validate required fields
            if (!documentType || !documentNumber || !phoneLastDigits || !payerName || !payerEmail) {
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