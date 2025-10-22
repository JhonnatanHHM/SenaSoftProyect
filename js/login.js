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
}// --- Función para decodificar un JWT ---
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

  try {
   
     const response = await fetch("https://localhost:8080/api/login", {
       method: "POST",
       headers: { "Content-Type": "application/json" },
       body: JSON.stringify({ email, password }),
     });
     const data = await response.json();
    const token = data.token;

    const decoded = parseJwt(token);
    if (decoded) {
      sessionStorage.setItem("token", token);
      sessionStorage.setItem("id", decoded.id);
      sessionStorage.setItem("nombre", decoded.nombres);
      sessionStorage.setItem("correo", decoded.email);
      sessionStorage.setItem("expira", decoded.exp);

      console.log("✅ Usuario logueado:", decoded);

      // Redirigir al index
      window.location.href = "/index.html";
    } else {
      alert("Token inválido o error al iniciar sesión.");
    }
  } catch (error) {
    console.error("❌ Error en el login:", error);
    alert("Error al iniciar sesión. Intenta de nuevo.");
  }
});
