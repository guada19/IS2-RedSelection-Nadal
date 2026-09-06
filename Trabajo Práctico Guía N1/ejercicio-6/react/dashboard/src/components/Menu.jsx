import logo from '../assets/img/logo.png';
import { Routes, Route, Link } from 'react-router-dom';
import Contenido from './Contenido';
import Preventa from './Preventa';
import Aspirantes from './Aspirantes';
import Profesiones from './Profesiones';

function Menu() {

    const profesiones = [
        {
            id: 1,
            nombre: 'Administración',
        },
        {
            id: 2,
            nombre: 'Ingeniería',
        },
        {
            id: 3,
            nombre: 'Salud',    
        },
        {
            id: 4,
            nombre: 'Educación',
        },
        {
            id: 5,
            nombre: 'Desarrollo de Software',
        }
    ]

   return (
        <>
            <header className="menu-wrap">
                <figure className="user">
                    <div className="user-avatar">
                        <Link to="/" ><img className="w-100" src={logo} alt="Cedavilu Web Academy"/></Link> 
                    </div>
                    <figcaption>
                        Cedavilu Web Academy
                    </figcaption>
                </figure>
                <nav>
                    <section className="menu">
                        <h3>Opciones</h3>
                        <ul>
                            <li>
                                <Link to="/empresa">
                                    <i className="bi bi-building" style={{fontSize: "1.2rem", color: "cornflowerblue"}}></i>
                                    - Empresas
                                </Link>
                            </li>
                            <li>
                                <Link to="/aspirantes">
                                    <i className="bi bi-person" style={{fontSize: "1.2rem", color: "cornflowerblue"}}></i>
                                    - Aspirantes
                                </Link>
                            </li>
                            <li>
                                <Link to="/profesiones">
                                    <i className="bi bi-list-check"></i>
                                    - Profesiones
                                </Link>
                            </li>
                            <li>
                                <Link to="#">
                                    <i className="bi bi-person-vcard" style={{fontSize: "1.2rem", color: "cornflowerblue"}}></i>
                                    - Postulate aquí
                                </Link>
                            </li>
                            <li>
                                <Link to="#">
                                    <i className="bi bi-chat-left-text"></i>
                                    - Contacto
                                </Link>
                            </li>
                        </ul>
                    </section>
                </nav>
            </header>
            
            <Routes>
                <Route path='/' element={<Contenido/>} />  
                <Route path='/empresa' element={<Preventa/>}/>
                <Route path='/aspirantes' element={<Aspirantes/>} />
                <Route path='/profesiones' element={<Profesiones  listado={ profesiones } />} />
            </Routes>
        </>
    )
}
export default Menu;