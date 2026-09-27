import React from 'react'
import { traducirDescripcion } from './Traduccion';

const DisplayClima = ({ clima }) => {
    
    try {

        if(!clima) {
            return (
                <div className="text-center text-muted py-4">
                <p className="mb-0">Ingresá una ciudad para ver el clima</p>
                </div>
            )
        } 

        const climaData = JSON.parse(clima);

        const temperaturaCelsius = climaData.main.temp - 273.15;

        return (
            <>
                <div className='clima'>

                    <table className='table table-striped table-bordered'>

                        <thead className='thead-dark'>
                            <tr>
                                <th>Ciudad</th>
                                <th>Tempreatura (°C)</th>
                                <th>Descripción</th>
                            </tr>
                        </thead>

                        <tbody>
                            <tr>
                                <td>{climaData.name}</td>
                                <td>{temperaturaCelsius.toFixed(2)}</td>
                                <td>{traducirDescripcion(climaData.weather[0].description)}</td>
                            </tr>
                        </tbody>

                    </table>

                </div>
            </>
        )

    } catch (error) {
        console.error("Error en la carga de datos: ", error);
        return <div>Error al cargar los datos del clima</div>
    }

}

export default DisplayClima
