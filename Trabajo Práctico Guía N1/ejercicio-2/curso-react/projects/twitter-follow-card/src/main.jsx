import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import {App} from './App.jsx'
import './index.css'


const root = createRoot(document.getElementById('root'));

//Componente --> función que retorna un elemento
//Componentes en PacalCase para que react diferencie entre elementos html o componentes 


root.render(
  <App />
);