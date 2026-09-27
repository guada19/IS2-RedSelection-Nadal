package com.ejercicio_1.spring_api;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String nombre;
    private String descripcion;
    private String marca;
    LocalDate fechaAdquisicion;
    private double precio;
}
