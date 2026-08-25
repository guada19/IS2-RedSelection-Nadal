package com.prueba.Thymeleaf.model;


import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
@NoArgsConstructor
public class Persona {
    private String nombre;
    private String apellido;
    private int edad;
}
