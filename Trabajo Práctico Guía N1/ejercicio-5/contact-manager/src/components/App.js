import React, { useState, useEffect }  from "react";
import { v4 as uuidv4 } from 'uuid';
import './App.css';
import Header from "./Header";
import AddContact from "./AddContact";
import ContactList from "./ContactList";

function App() {

  //Api del navegador para almacenar pares clave-valor en texto plano
  const LOCAL_STORAGE_KEY = "contacts"
  const [contacts, setContacts] = useState(
    JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY)) ?? []
  )
  
  // Funciones controladoras encargadas de gestionar la lógica ante eventos del usuario
  // pasar información que capturó el hijo al padre
  const addContactHandler = (contact) => {
    setContacts([...contacts, {id: uuidv4(), ...contact}])
  }

  const removeContactHandler = (id) => {
    const newContactList = contacts.filter((contact) => {
      return contact.id !== id;
    })

    setContacts(newContactList);
  }

  // Hook que sincroniza el estado local con la API externa de localStorage.
  // Se ejecuta cada vez que 'contacts' cambia, guardando el arreglo convertido en texto JSON.
  useEffect(() => {
    const retriveContacts= JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY));
    if (retriveContacts) setContacts(retriveContacts);
  }, []);  

  useEffect(() => {
    localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(contacts));
  }, [contacts]);


  return (
      <>
        <Header />
        <main className="container my-4">
          <AddContact addContactHandler ={addContactHandler}/>
          <hr className="my-4 text-muted" />
          <ContactList contacts={contacts} getContactId={removeContactHandler}/>
        </main>
      </>
    );
}

export default App;
