tailwind.config = {
    darkMode: "class",
    theme: {
        extend: {
            colors: {
                "primary": "#003366",
                "secondary": "#00AEEF",
                "background-light": "#F5F5F5",
                "background-dark": "#101c22",
            },
            fontFamily: {
                "display": ["Plus Jakarta Sans", "Noto Sans", "sans-serif"]
            },
            borderRadius: {
                "DEFAULT": "0.5rem",
                "lg": "0.75rem",
                "xl": "1rem",
                "full": "9999px"
            },
        },
    },
}

document.getElementById('loginForm').addEventListener('submit', async function(event) {
  event.preventDefault(); // evitar que el formulario se envíe de forma tradicional

  const email = document.getElementById('emailInput').value.trim();
  const password = document.getElementById('passwordInput').value;

  try {
    const response = await fetch('http://localhost:8080/api/login', {
      method: 'POST',
      headers: {
        'Accept': '*/*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: email,
        password: password
      })
    });

    if (!response.ok) {
      // manejar errores HTTP
      const errorText = await response.text();
      throw new Error(`Error al iniciar sesión: ${response.status} ${errorText}`);
    }

    const data = await response.json();
    console.log('Login exitoso, token:', data.accessToken);

    // Aquí puedes guardar el token, por ejemplo en localStorage:
    localStorage.setItem('accessToken', data.accessToken);

    // Redireccionar al usuario a la página protegida:
    window.location.href = '/index.html';  // ajusta la ruta

  } catch (error) {
    console.error('Error al iniciar sesión:', error);
    // Mostrar mensaje de error al usuario:
    alert('Correo o contraseña incorrectos o no se pudo conectar con el servidor.');
  }
});
