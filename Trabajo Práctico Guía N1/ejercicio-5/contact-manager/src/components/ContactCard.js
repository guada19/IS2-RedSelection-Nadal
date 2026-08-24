import React from "react";

const ContactCard = (props) => {
    
    const {id, name, email} = props.contact
    const avatarUrl = `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=0D6EFD&color=fff&bold=true`;

    return (
        <li className="list-group-item d-flex justify-content-between align-items-center py-3 px-3">

        <div className="d-flex align-items-center">
            <img 
                src={avatarUrl} 
                alt={name} 
                className="rounded-circle me-3 shadow-sm border" 
                width="48" 
                height="48" 
            />

            <div>
                <h5 className="mb-1 fw-bold text-dark">{name}</h5>
                <span className="text-muted small">{email}</span>
            </div>
        </div>

        <button 
            type="button" 
            className="btn btn-outline-danger btn-sm border-0" 
            title="Eliminar contacto"
            onClick={() => props.clickHandler(id)}
        >
         <i className="bi bi-trash fs-5"></i>   
        </button>
        </li>
    );
};

export default ContactCard