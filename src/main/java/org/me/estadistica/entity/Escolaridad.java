package org.me.estadistica.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter(AccessLevel.PUBLIC)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Escolaridad {

    private static final String FILLER_ZERO = "0";
    private static final String FILLER_SPACE = " ";
    private static final String CODIGO_DEPENDENCIA = "90022804";
    private static final String FIN_TUPLA = "\r\n";


    String cueAnexo = "";
    String regice = "";
    String cuilEstudiante = "";
    String cicloLectivo = "";
    String nivel = "";
    String gradoAnio ="";
    String nombreCursoCarrera = "";
    String fechaCertificacion = "";
    String fechaInicioCicloLectivo = "";
    char esEducacionOficial;
    char esAlumnoRegular;

    public void setGradoAnio(String gradoAnio){
        this.gradoAnio = gradoAnio;
    }

    public void setCueAnexo(@NonNull String cueAnexo) {
        this.cueAnexo = this.agregarCerosAdelante(cueAnexo,9);
    }

    public void setRegice(@NonNull String regice) {
        this.regice = this.agregarCerosAdelante(regice, 10);
    }

    public void setCuilEstudiante(@NonNull String cuilEstudiante) {
        if(cuilEstudiante.length() > 11){
            cuilEstudiante.replace("-","").replace(".","");
        }
        this.cuilEstudiante = this.agregarCerosAdelante(cuilEstudiante, 11);
    }

    public void setCicloLectivo(@NonNull String cicloLectivo) {
        this.cicloLectivo = this.agregarCerosAdelante(cicloLectivo, 4);
    }

    public void setNivel(@NonNull String nivel) {
        this.nivel = agregarCerosAdelante(nivel,2);
    }

    public void setFechaInicioCicloLectivo(String fechaInicioCicloLectivo) {
        if(fechaInicioCicloLectivo.isEmpty() || fechaInicioCicloLectivo.isBlank())
            this.fechaInicioCicloLectivo = "        ";
        else if(Integer.parseInt(this.nivel) > 5)
            this.fechaInicioCicloLectivo = fechaInicioCicloLectivo;

    }

    public void setNombreCursoCarrera(String nombreCursoCarrera) {
        if(nombreCursoCarrera.isEmpty() || nombreCursoCarrera.isBlank())
            this.nombreCursoCarrera = "";
        else if(Integer.parseInt(this.nivel) > 5)
            this.nombreCursoCarrera = nombreCursoCarrera;
    }

    public void setFechaCertificacion(String fechaCertificacion) {
        //La fecha de certificación, se refiere a la fecha en la que el estableimiento educativo
        //acredita la escolaridad del menor.
        //Debe ser mayor o igual a la fecha de inicio de ciclo lectivo.
        this.fechaCertificacion = formatearFechaArgentina(fechaCertificacion);
    }

    public void setEsEducacionOficial(@NonNull String esEducacionOficial) {
        this.esEducacionOficial = esEducacionOficial.toUpperCase().charAt(0);
    }

    public void setEsAlumnoRegular(@NonNull String esAlumnoRegular) {
        this.esAlumnoRegular = esAlumnoRegular.toUpperCase().charAt(0);
    }

    public String agregarCerosAdelante(String cadena, int longitudMaxima){
        for (int i = cadena.length(); i < longitudMaxima; i++) {
            cadena = FILLER_ZERO + cadena;
        }
        return cadena;
    }

    public String agregarEspaciosAlFinal(String cadena, int longitudMaxima){
        for (int i = cadena.length(); i < longitudMaxima; i++) {
            cadena = cadena.concat(FILLER_SPACE);
        }
        return cadena;
    }

    public String formatearFechaArgentina(String fechaInput){
        String fecha = fechaInput.replace("/", "-");
        String pattern = "dd-MM-yyyy";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate date = LocalDate.parse(fecha, formatter);

        return  date.toString().replace("-","");

    }

    public String formatearFechaExtrangera(String fechaInput){
        String fecha = fechaInput.replace("/", "-");
        String pattern = "yyyy-MM-dd";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate date = LocalDate.parse(fecha, formatter);

        return  date.toString().replace("-","");

    }

    @Override
    public String toString() {
        return agregarCerosAdelante(this.cueAnexo, 9) + ";" +
                agregarCerosAdelante(this.regice, 10) + ";" +
                agregarCerosAdelante(this.cuilEstudiante, 11) + ";" +
                agregarCerosAdelante(this.cicloLectivo, 4) + ";" +
                agregarCerosAdelante(this.nivel, 2) + ";" +
                agregarCerosAdelante(this.gradoAnio, 1) + ";" +
                this.esEducacionOficial + ";" +
                this.esAlumnoRegular + ";" +
                agregarEspaciosAlFinal(this.fechaInicioCicloLectivo, 8) + ";" +
                agregarEspaciosAlFinal(this.nombreCursoCarrera, 80) + ";" +
                agregarCerosAdelante(this.fechaCertificacion, 8) + ";" +
                CODIGO_DEPENDENCIA +
                FIN_TUPLA;
    }
}
