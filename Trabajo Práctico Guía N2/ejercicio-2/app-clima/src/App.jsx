import { useState } from 'react'
import InputCiudad from './components/InputCiudad'
import ObtenerClima from './api/ObtenerClima'
import { BotonConsulta } from './components/BotonConsulta'
import DisplayClima from './components/DisplayClima'

function App() {

  const [ciudad, setCiudad] = useState("")
  const [clima, setClima] = useState("")

  const handlerObtenerClima = (e) => {
    e.preventDefault
    ObtenerClima(ciudad, setClima)
  }


  return (
    <div className="min-vh-100 bg-light d-flex flex-column align-items-center py-5">
      <div className="container" style={{ maxWidth: '560px' }}>
        <h1 className="text-center fw-bold mb-4 text-primary">Buscar el clima Actual</h1>

        <div className="d-flex gap-2 shadow-sm p-2 bg-white rounded-3 mb-4">
          <InputCiudad ciudad={ciudad} setCiudad={setCiudad} />
          <BotonConsulta obtenerClima={() => ObtenerClima(ciudad, setClima)} />
        </div>
        
        <DisplayClima clima={clima} />
      </div>
    </div>
  );
}

export default App
