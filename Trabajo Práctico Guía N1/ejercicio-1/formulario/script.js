document.getElementById("myform").addEventListener("submit", function(event) {
    event.preventDefault(); // Evita que el formulario se envíe automáticamente

    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;

    if(username.length < 8) {
        console.log("El usuario debe tener como mínimo 8 caracteres")
        return
    }

    if (username && email) {
        console.log(`Usuario: ${username} y su email es: ${email}`);
        
    } else {
        console.log("Por favor, complete todos los campos.");
    }


})