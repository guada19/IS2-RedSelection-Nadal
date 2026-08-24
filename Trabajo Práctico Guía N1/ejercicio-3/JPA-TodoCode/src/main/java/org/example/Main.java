package org.example;

import logic.Alumno;
import logic.Carrera;
import logic.Controladora;
import logic.Materia;
import persistence.ControladoraPersistencia;

import java.util.Date;
import java.util.LinkedList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //ControladoraPersistencia controlPersis = new ControladoraPersistencia();
        Controladora control = new Controladora();
        //Carrera carrera = new Carrera("LCC");
        //Alumno alu = new Alumno("Evelyn", "Bottom", new Date(), carrera);
        //control.crearAlumno(alu);



        LinkedList<Materia> listaMaterias = new LinkedList<Materia>();
        Carrera carre = new Carrera("Tecnicatura en Programación", listaMaterias);

        control.crearCarrera(carre);

        Materia mate1 = new Materia("Programación I", "Cuatrimestral", carre);
        Materia mate2 = new Materia("Programación II", "Cuatrimestral", carre);
        Materia mate3 = new Materia("Programación Avanzada", "Anual", carre);

        control.crearMateria(mate1);
        control.crearMateria(mate2);
        control.crearMateria(mate3);

        listaMaterias.add(mate1);
        listaMaterias.add(mate2);
        listaMaterias.add(mate3);

        carre.setMaterias(listaMaterias);
        control.actualizarCarrera(carre);
    }
}