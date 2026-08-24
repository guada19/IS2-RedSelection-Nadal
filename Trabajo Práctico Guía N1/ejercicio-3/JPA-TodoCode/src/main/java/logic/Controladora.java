package logic;

import persistence.ControladoraPersistencia;

import java.util.ArrayList;

public class Controladora {

    ControladoraPersistencia controlPersis = new ControladoraPersistencia();

    public void crearAlumno(Alumno alu) {
        controlPersis.crearAlumno(alu);
    }

    public void eliminarAlumno(int idAlumno){
        controlPersis.eliminarAlumno(idAlumno);
    }

    public void actualizarAlumno(Alumno alu) {
        controlPersis.editarAlumno(alu);
    }

    public Alumno traerAlumno(int id) {
        return controlPersis.traerAlumno(id);
    }

    public ArrayList<Alumno> traerListaAlumnos() {
        return controlPersis.traerListaAlumnos();
    }

    public void crearMateria(Materia materia) {
        controlPersis.crearMateria(materia);
    }

    public void eliminarMateria(int id){
        controlPersis.eliminarMateria(id);
    }

    public void actualizarMateria(Materia materia) {
        controlPersis.editarMateria(materia);
    }

    public Materia traerMateria(int id) {
        return controlPersis.traerMateria(id);
    }

    public ArrayList<Materia> traerListaMateria() {
        return controlPersis.traerListaMateria();
    }

    public void crearCarrera(Carrera carrera) {
        controlPersis.crearCarrera(carrera);
    }

    public void eliminarCarrera(int id){
        controlPersis.eliminarCarrera(id);
    }

    public void actualizarCarrera(Carrera carrera) {
        controlPersis.editarCarrera(carrera);
    }

    public Carrera traerCarrera(int id) {
        return controlPersis.traerCarrera(id);
    }

    public ArrayList<Carrera> traerListaCarrera() {
        return controlPersis.traerListaCarrera();
    }
}
