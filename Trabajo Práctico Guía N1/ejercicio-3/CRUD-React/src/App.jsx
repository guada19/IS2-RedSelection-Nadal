import { Routes, Route } from 'react-router-dom'
import Show from './components/show'
import Create from './components/create'
import Edit from './components/edit'

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Show />} />
        <Route path="/create" element={<Create />} />
        <Route path="/edit/:id" element={<Edit />} /> 
      </Routes>
    </div>
  )
}

export default App