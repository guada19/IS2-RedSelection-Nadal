document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector(".formulario");
    const paragraph = document.getElementById("parrafo");

    paragraph.style.display = "none";

    form.addEventListener("submit", function(event) {
        event.preventDefault(); // Evita que el formulario se envíe automáticamente

        const name = document.querySelector("#name").value;
        const password = document.querySelector("#password").value;

        if (name === "" || password === "") {
            paragraph.textContent = "Por favor, complete todos los campos";
            paragraph.style.display = "block";
            paragraph.style.color = "red";
        } else {
            paragraph.textContent = "Formulario enviado correctamente";
            paragraph.style.color = "green";
            paragraph.style.display = "block";
        }
    });
}); 