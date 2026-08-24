import React from "react";

// Componente basado en clases (antes de los Hooks) que usa this.state
// Gestiona el estado local del formulario de forma aislada antes de enviarlo al componente principal.
class AddContact extends React.Component {
    state = {
        name:"",
        email:""
    };

    add = (e) =>{
        e.preventDefault();
        if(this.state.name === "" || this.state.email === "") {
            alert("Todos los campos son requeridos")
            return
        }
        this.props.addContactHandler(this.state);
        this.setState({name: "", email: ""})
    }

    render() {
        return (
            <div className="container pt-5 mt-4">
                <h2 className="mb-3">Añadir Contacto</h2>
                
                <form onSubmit={this.add}>
                    
                    <div className="mb-3">
                        <label className="form-label">Name</label>
                        <input 
                            type="text" 
                            className="form-control" 
                            name ="name" 
                            placeholder="Nombre"
                            value={this.state.name} 
                            onChange={(e)=> this.setState({name: e.target.value}) }
                        />
                    
                    </div>
                    
                    <div className="mb-3">
                        <label className="form-label">Email</label>
                        <input 
                            type="email" 
                            className="form-control" 
                            name ="mail" 
                            placeholder="mailejemplo@gmail.com"
                            value={this.state.email} 
                            onChange={(e)=> this.setState({email: e.target.value}) }
                        />
                    
                    </div>

                    <button className="btn btn-primary">Añadir</button>
                        
                </form>
            </div>
        );
    }
}

export default AddContact