tailwind.config = {
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        primary: "#003366",
        secondary: "#00AEEF",
        "background-light": "#F5F5F5",
        "background-dark": "#101c22",
      },
      fontFamily: {
        display: ["Plus Jakarta Sans", "Noto Sans", "sans-serif"],
      },
      borderRadius: {
        DEFAULT: "0.5rem",
        lg: "0.75rem",
        xl: "1rem",
        full: "9999px",
      },
    },
  },
};

// --- Función para decodificar un JWT ---
function parseJwt(token) {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = atob(base64);
    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error("❌ Error al decodificar el token:", error);
    return null;
  }
}

// --- Manejar el envío del formulario de login ---
document.getElementById("loginForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const email = document.getElementById("emailInput").value;
  const password = document.getElementById("passwordInput").value;
  const loginButton = document.getElementById("loginButton");

  // Disable button and show loading state
  loginButton.disabled = true;
  loginButton.textContent = "Iniciando sesión...";

  try {
    const response = await fetch("https://senasoftproyect.onrender.com/api/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    const data = await response.json();

    if (response.ok && data.accessToken) {
      const token = data.accessToken;
      const decoded = parseJwt(token);

      if (decoded) {
        sessionStorage.setItem("token", token);
        sessionStorage.setItem("id", decoded.id);
        sessionStorage.setItem("nombre", decoded.nombres);
        sessionStorage.setItem("correo", decoded.email);
        sessionStorage.setItem("expira", decoded.exp);

        console.log("✅ Usuario logueado:", decoded);

        // Show success SweetAlert
        Swal.fire({
          title: "¡Éxito!",
          text: "Inicio de sesión exitoso. Bienvenido " + decoded.nombres,
          icon: "success",
          confirmButtonText: "Continuar",
        }).then((result) => {
          if (result.isConfirmed) {
            // Redirigir al index
            window.location.href = "/index.html";
          }
        });
      } else {
        // Show error SweetAlert for invalid token
        Swal.fire({
          title: "Error",
          text: "Token inválido o error al iniciar sesión.",
          icon: "error",
          confirmButtonText: "Aceptar",
        });

        // Re-enable button
        loginButton.disabled = false;
        loginButton.textContent = "Entrar";
      }
    } else {
      // Show error SweetAlert for incorrect credentials
      Swal.fire({
        title: "Credenciales incorrectas",
        text:
          data.message ||
          "Nombre de usuario o contraseña incorrectos. Por favor, inténtalo de nuevo.",
        icon: "error",
        confirmButtonText: "Aceptar",
      });

      // Re-enable button
      loginButton.disabled = false;
      loginButton.textContent = "Entrar";
    }
  } catch (error) {
    console.error("❌ Error en el login:", error);

    // Show error SweetAlert for network/other errors
    Swal.fire({
      title: "Error",
      text: "Error al iniciar sesión. Por favor, verifica tu conexión e inténtalo de nuevo.",
      icon: "error",
      confirmButtonText: "Aceptar",
    });

    // Re-enable button
    loginButton.disabled = false;
    loginButton.textContent = "Entrar";
  }
});
