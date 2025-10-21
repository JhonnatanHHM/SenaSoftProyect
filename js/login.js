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

async function iniciarSesion() {
    let datos = {
        email: document.getElementById('nombre').value,
        password: document.getElementById('contraseña').value
    };

    try {
        const request = await fetch('https://ticket-backend-bkkf.onrender.com/auth/login', {
            method: 'POST',
            headers: {
                'accept': '*/*',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        });

        const respuesta = await request.json();

        if (request.ok && respuesta.token) {
            const tokenData = jwt_decode(respuesta.token);

            localStorage.setItem('token', respuesta.token);
            localStorage.setItem('email', tokenData.sub);
            localStorage.setItem('nombre', tokenData.name);
            localStorage.setItem('userId', tokenData.id);

            if (tokenData === 'usuarios') {
                window.location.href = 'index.html';
            }
        } else {
            if (respuesta.error && respuesta.error.toLowerCase().includes("no encontrado")) {
                if (confirm("El usuario no está registrado. ¿Desea registrarse ahora?")) {
                    window.location.href = "registro.html";
                }
            } else {
                alert(respuesta.error || "Error de autenticación.");
            }
        }

    } catch (error) {
        console.error("Error en la solicitud:", error);
        alert("No se pudo conectar con el servidor.");
    }
}

