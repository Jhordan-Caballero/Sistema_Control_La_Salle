const form = document.getElementById("formEstudiante");
const mensaje = document.getElementById("mensaje");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const datos = Object.fromEntries(new FormData(form).entries());

    try {
        const respuesta = await fetch("/api/estudiantes", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        });

        const resultado = await respuesta.json();

        if (respuesta.ok) {
            mensaje.textContent = `Alumno guardado con ID ${resultado.id}`;
            mensaje.className = "ok";
            form.reset();
        } else {
            mensaje.textContent = resultado.error;
            mensaje.className = "error";
        }
    } catch (error) {
        mensaje.textContent = "No se pudo conectar con el servidor";
        mensaje.className = "error";
    }
});