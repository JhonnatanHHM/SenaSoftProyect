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
  const form = document.getElementById('registerForm');
  const passwordInput = document.getElementById('passwordInput');
  const confirmPasswordInput = document.getElementById('confirmPasswordInput');
  const passwordMismatchError = document.getElementById('passwordMismatchError');
  const togglePassword = document.getElementById('togglePasswordVisibility');
  const toggleConfirmPassword = document.getElementById('toggleConfirmPasswordVisibility');

  // Toggle visibility for password
  togglePassword.addEventListener('click', () => {
    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
    passwordInput.setAttribute('type', type);
    togglePassword.innerHTML = `<span class="material-symbols-outlined">${type === 'password' ? 'visibility' : 'visibility_off'}</span>`;
  });

  toggleConfirmPassword.addEventListener('click', () => {
    const type = confirmPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
    confirmPasswordInput.setAttribute('type', type);
    toggleConfirmPassword.innerHTML = `<span class="material-symbols-outlined">${type === 'password' ? 'visibility_off' : 'visibility'}</span>`;
  });

  form.addEventListener('submit', async (event) => {
    event.preventDefault();

    // Clear previous error
    passwordMismatchError.classList.add('hidden');

    const nombres = document.getElementById('nombresInput').value.trim();
    const primerApellido = document.getElementById('primerApellidoInput').value.trim();
    const email = document.getElementById('emailInput').value.trim();
    const password = passwordInput.value;
    const confirmPassword = confirmPasswordInput.value;
    const termsChecked = document.getElementById('terms-checkbox').checked;

    if (password !== confirmPassword) {
      passwordMismatchError.classList.remove('hidden');
      return;
    }

    if (!termsChecked) {
      alert('Debes aceptar los términos para continuar.');
      return;
    }

    // Token de autenticación (si necesario)
    const token = localStorage.getItem('accessToken');
    if (!token) {
      alert('No estás autenticado. Por favor inicia sesión para poder registrar un nuevo usuario.');
      return;
    }

    const payload = {
      estado: true,
      nombres: nombres,
      primerApellido: primerApellido,
      // segundoApellido: '' (añade si lo tienes)
      celular: '', // si tienes campo celular, cambia aquí
      email: email,
      password: password
    };

    try {
      const response = await fetch('http://localhost:8080/api/usuarios/register', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Error al registrar usuario: ${response.status} ${errorText}`);
      }

      const data = await response.json();
      console.log('Registro exitoso:', data);
      alert(`Usuario registrado con ID: ${data.idUsuario}`);
      // Por ejemplo, redirigir:
      window.location.href = 'login.html';

    } catch (error) {
      console.error('Error al registrar usuario:', error);
      alert('No se pudo completar el registro. Verifica los datos e inténtalo de nuevo.');
    }
  });
});
