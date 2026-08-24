let listaEmpleados = [];

const objetoEmpleado = {
    id: "",
    name: "",
    job: ""
};

let editando = false; //detectar agregar y actualizar

const form = document.querySelector("#myform");
const nameInput = document.querySelector("#name");
const jobInput = document.querySelector("#job");
const btnAdd = document.querySelector("#btnAdd");

form.addEventListener("submit", validateForm);

function validateForm(e) {
    e.preventDefault();

    if(nameInput.value === "" || jobInput.value === "") {
        alert("Todos los campos son obligatorios");
        return;
    }

    if (editando) {
        editarEmpleado();
        editando = false;
    } else {
        objetoEmpleado.id = Date.now();
        objetoEmpleado.name = nameInput.value;
        objetoEmpleado.job = jobInput.value

        agregarEmpleado();

    }

}

function agregarEmpleado() {
    listaEmpleados.push({...objetoEmpleado})
    mostrarEmpleados();    
    form.reset();

    limpiarObjeto();

}

function limpiarObjeto() {
    objetoEmpleado.id = "";
    objetoEmpleado.name = "";
    objetoEmpleado.job = "";
}

function mostrarEmpleados() {

    limpiarHTML();

    const divEmpleados = document.querySelector(".div-empleados");

    listaEmpleados.forEach( empleado => {
        const {id, name, job} = empleado;
        const parrafo = document.createElement("p");
        parrafo.textContent = `${name} - ${job} `;
        parrafo.dataset.id = id;

        const btnEdit = document.createElement("button");
        btnEdit.onclick = () => cargarEmpleado(empleado);
        btnEdit.textContent = "Editar";
        btnEdit.classList.add("btn", "btn-editar");
        parrafo.append(btnEdit);


        const btnDelete = document.createElement("button");
        btnDelete.onclick = () => eliminarEmpleado(id);
        btnDelete.textContent = "Eliminar";
        btnDelete.classList.add("btn", "btn-eliminar");
        parrafo.append(btnDelete);

        const hr = document.createElement("hr");
        divEmpleados.appendChild(parrafo);
        divEmpleados.appendChild(hr);

    })

}

function limpiarHTML() {
    const divEmpleados = document.querySelector(".div-empleados");
    while (divEmpleados.firstChild) {
        divEmpleados.removeChild(divEmpleados.firstChild);
    }
}

function cargarEmpleado(empleado) {
    const {id, name, job} = empleado;

    nameInput.value = name;
    jobInput.value = job;

    objetoEmpleado.id = id;

    form.querySelector('button[type="submit"]').textContent = "Actualizar";
    editando = true;

}

function editarEmpleado() {
    objetoEmpleado.name = nameInput.value;
    objetoEmpleado.job = jobInput.value;

    listaEmpleados.map( empleado => {
        if(empleado.id == objetoEmpleado.id) {
            empleado.id = objetoEmpleado.id;
            empleado.name = objetoEmpleado.name;
            empleado.job = objetoEmpleado.job;
        }
    })

    limpiarHTML();
    mostrarEmpleados();

    form.reset();
    form.querySelector('button[type="submit"]').textContent = "Agregar"

    editando = false;

}

function eliminarEmpleado(id) {

    listaEmpleados = listaEmpleados.filter( empleado => empleado.id != id);

    limpiarHTML();
    mostrarEmpleados();
}