package persistence;

import logic.Alumno;
import logic.Carrera;
import logic.Materia;

import java.util.ArrayList;
import java.util.List;


public class ControladoraPersistencia {

    AlumnoJpaController aluJpa = new AlumnoJpaController();
    CarreraJpaController cJpa = new CarreraJpaController();
    MateriaJpaController mateJpa = new MateriaJpaController();

    public void crearAlumno(Alumno alu) {
        aluJpa.create(alu);
    }

    public void eliminarAlumno(int idAlumno) {
        aluJpa.destroy(idAlumno);
    }

    public void editarAlumno(Alumno alu) {
        aluJpa.edit(alu);
    }

    public Alumno traerAlumno(int id) {
        return aluJpa.findAlumno(id);
    }

    public ArrayList<Alumno> traerListaAlumnos() {
        List<Alumno> listaAux = aluJpa.findAlumnoEntities();
        return new ArrayList<Alumno>(listaAux);
    }

    public void crearCarrera(Carrera carrera) {
        cJpa.create(carrera);
    }

    public void eliminarCarrera(int id) {
        cJpa.destroy(id);
    }

    public void editarCarrera(Carrera carrera) {
        cJpa.edit(carrera);
    }

    public Carrera traerCarrera(int id) {
        return cJpa.findCarrera(id);
    }

    public ArrayList<Carrera> traerListaCarrera() {
        List<Carrera> listaAux = cJpa.findCarreraEntities();
        return new ArrayList<Carrera>(listaAux);
    }

    public void crearMateria(Materia materia) {
        mateJpa.create(materia);
    }

    public void eliminarMateria(int id) {
        mateJpa.destroy(id);
    }

    public void editarMateria(Materia materia) {
        mateJpa.edit(materia);
    }

    public Materia traerMateria(int id) {
        return mateJpa.findMateria(id);
    }

    public ArrayList<Materia> traerListaMateria() {
        List<Materia> listaAux = mateJpa.findMateriaEntities();
        return new ArrayList<Materia>(listaAux);
    }
}
