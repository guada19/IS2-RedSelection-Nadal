import React from 'react'

const InputCiudad = ({ ciudad, setCiudad }) => {
    return (
        <>
            <input 
                className='form-control form-control-lg' 
                type="text" 
                placeholder="Ej: Mendoza, Buenos Aires..." 
                value={ciudad} 
                onChange={e => setCiudad(e.target.value)} 
            />
        </>
    )
}

export default InputCiudad

//rafc