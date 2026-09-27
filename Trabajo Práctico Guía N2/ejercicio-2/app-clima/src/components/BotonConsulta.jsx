import React from 'react'

export const BotonConsulta = ({ obtenerClima }) => {
    return (
        <button type="submit" className='btn btn-primary btn-lg px-4' onClick={obtenerClima}>
            Consultar
        </button>
    )
}