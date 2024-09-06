package org.me.estadistica.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlumnoData {
    private String nombreApellido;
    private int dni;
    private int cueAnexo;
    private int cuilt;
    private String cicloLectivo;
    private int nivelEducativo;
    private char gradoAño;
    private char esAlumnoRegular;

    public void setNivelEducativo(String nivelEducativo){
        switch (nivelEducativo){
            case "Primario":
                this.nivelEducativo = 1;
                break;
            case "Inicial":
                this.nivelEducativo = 4;
                break;
            case "Secundario":
                this.nivelEducativo = 2;
                break;
        }
    }

    public boolean isAlumnoRegular(){
        return this.getEsAlumnoRegular() == 'S' ? true : false;
    }

    @Override
    public String toString() {
        return this.cueAnexo + " " + this.nombreApellido + " " +
                this.dni  + " " + this.cicloLectivo + " " +
                this.nivelEducativo + " " + this.gradoAño + " " +
                this.esAlumnoRegular;
    }
}
