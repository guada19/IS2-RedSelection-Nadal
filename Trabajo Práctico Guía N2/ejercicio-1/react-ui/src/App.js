import ButtonAppBar from './components/Appbar';
import './App.css';
import Productos from './components/Productos';

function App () {
  return (
    <div className='App'>
      <ButtonAppBar />
      <Productos />
    </div>
  )
}

export default App