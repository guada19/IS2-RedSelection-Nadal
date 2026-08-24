import React from "react";
import ContactCard from "./ContactCard";

const ContactList = (props) => {
    
    const deleteContactHandler = (id) => {
        props.getContactId(id);
    }
    
    
    const renderContactList = props.contacts.map((contact) => {
        return (
            <ContactCard 
                contact={contact} 
                clickHandler ={deleteContactHandler}
                key={ contact.id }
            />
        );
    });

    return (
        <div className="container mt-4">
        <h2 className="mb-3">Lista de contactos</h2>
        <ul className="list-group shadow-sm">
            {renderContactList}
        </ul>
        </div>
    );
}

export default ContactList