package logic;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Alumno {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Basic
    private String name;
    private String apellido;

    @Temporal(TemporalType.DATE)
    private Date fechaNac;

    @OneToOne
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;


    public Alumno() {
    }

    public Alumno(String name, String apellido, Date fechaNac, Carrera carrea) {
        this.name = name;
        this.apellido = apellido;
        this.fechaNac = fechaNac;
        this.carrera = carrea;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrea) {
        this.carrera = carrea;
    }
}
