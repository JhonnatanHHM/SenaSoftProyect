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

    // registro.js

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('registroForm');
  const passwordInput = document.getElementById('passwordInput');
  const confirmPasswordInput = document.getElementById('confirmPasswordInput');
  const passwordMismatchError = document.getElementById('passwordMismatchError');
  const togglePassword = document.getElementById('togglePasswordVisibility');
  const toggleConfirmPassword = document.getElementById('toggleConfirmPasswordVisibility');

  // Alternar visibilidad de contraseña principal
  togglePassword.addEventListener('click', () => {
    const type = passwordInput.type === 'password' ? 'text' : 'password';
    passwordInput.type = type;
    togglePassword.innerHTML = `<span class="material-symbols-outlined">${
      type === 'password' ? 'visibility_off' : 'visibility'
    }</span>`;
  });

  // Alternar visibilidad de confirmación de contraseña
  toggleConfirmPassword.addEventListener('click', () => {
    const type = confirmPasswordInput.type === 'password' ? 'text' : 'password';
    confirmPasswordInput.type = type;
    toggleConfirmPassword.innerHTML = `<span class="material-symbols-outlined">${
      type === 'password' ? 'visibility_off' : 'visibility'
    }</span>`;
  });

  // Envío del formulario
  form.addEventListener('submit', async (event) => {
    event.preventDefault();

    // Limpiar error previo
    passwordMismatchError.classList.add('hidden');

    // Obtener valores del formulario
    const nombres = document.getElementById('nombresInput').value.trim();
    const apellido1 = document.getElementById('apellido1').value.trim();
    const apellido2 = document.getElementById('apellido2').value.trim();
    const email = document.getElementById('emailInput').value.trim();
    const celular = document.getElementById('celular').value.trim();
    const password = passwordInput.value;
    const confirmPassword = confirmPasswordInput.value;
    const termsChecked = document.getElementById('terms-checkbox').checked;

    // Validaciones
    if (password !== confirmPassword) {
      passwordMismatchError.classList.remove('hidden');
      return;
    }

    if (!termsChecked) {
      alert('Debes aceptar los términos para continuar.');
      return;
    }

    // Si tu API requiere autenticación, puedes usar un token aquí
    const token = localStorage.getItem('accessToken');

    // Crear objeto con datos
    const payload = {
      estado: true,
      nombres,
      primerApellido: apellido1,
      segundoApellido: apellido2 || null,
      celular,
      email,
      password
    };

    try {
      const response = await fetch('http://localhost:8080/api/usuarios/register', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
          ...(token && { 'Authorization': `Bearer ${token}` }) // Se agrega solo si existe token
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Error al registrar usuario: ${response.status} ${errorText}`);
      }

      const data = await response.json();
      console.log('✅ Registro exitoso:', data);

      alert(`Usuario registrado correctamente.`);
      window.location.href = 'login.html'; // Redirige al login

    } catch (error) {
      console.error('❌ Error al registrar usuario:', error);
      alert('No se pudo completar el registro. Verifica los datos e inténtalo de nuevo.');
    }
  });
});
