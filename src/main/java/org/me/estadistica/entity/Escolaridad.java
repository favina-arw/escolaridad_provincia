package org.me.estadistica.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter(AccessLevel.PUBLIC)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Escolaridad {

    private static final String FILLER_ZERO = "0";
    private static final String FILLER_SPACE = " ";
    private static final String CODIGO_DEPENDENCIA = "90022804";
    private static final String FIN_TUPLA = "\r\n";

    public Escolaridad(@NonNull String cueAnexo, @NonNull String regice, @NonNull String cuilEstudiante, @NonNull String cicloLectivo, @NonNull String nivel, String gradoAnio, String nombreCursoCarrera, String fechaCertificacion, String fechaInicioCicloLectivo, @NonNull char esEducacionOficial, @NonNull char esAlumnoRegular) {
        this.cueAnexo = agregarCerosAdelante(cueAnexo, 9);
        this.regice = agregarCerosAdelante(regice, 10);
        if(cuilEstudiante.length() > 11){
            cuilEstudiante.replace("-","").replace(".","");
        }
        this.cuilEstudiante = agregarCerosAdelante(cuilEstudiante, 11);
        this.cicloLectivo = agregarCerosAdelante(cicloLectivo, 4);
        this.nivel = agregarCerosAdelante(nivel,2);
        this.gradoAnio = agregarCerosAdelante(gradoAnio,1);
        this.nombreCursoCarrera = agregarEspaciosAlFinal(nombreCursoCarrera,80);
        this.fechaCertificacion = agregarCerosAdelante(formatearFechaArgentina(fechaCertificacion),8);
        this.fechaInicioCicloLectivo = agregarCerosAdelante(formatearFechaArgentina(fechaInicioCicloLectivo),8);
        this.esEducacionOficial = esEducacionOficial;
        this.esAlumnoRegular = esAlumnoRegular;
    }

    public Escolaridad(@NonNull String cueAnexo, @NonNull String regice, @NonNull String cuilEstudiante, @NonNull String cicloLectivo, @NonNull String nivel, String gradoAnio, String nombreCursoCarrera, String fechaCertificacion, String fechaInicioCicloLectivo, @NonNull String esEducacionOficial, @NonNull String esAlumnoRegular) {
        this.cueAnexo = agregarCerosAdelante(cueAnexo, 9);
        this.regice = agregarCerosAdelante(regice, 10);
        if(cuilEstudiante.length() > 11){
            cuilEstudiante.replace("-","").replace(".","");
        }
        this.cuilEstudiante = agregarCerosAdelante(cuilEstudiante, 11);
        this.cicloLectivo = agregarCerosAdelante(cicloLectivo, 4);
        this.nivel = agregarCerosAdelante(nivel,2);
        if (gradoAnio.length() == 0 || gradoAnio.equals(""))
            this.gradoAnio = "0";
        else this.gradoAnio = gradoAnio;
        this.nombreCursoCarrera = agregarEspaciosAlFinal(nombreCursoCarrera,80);
        this.fechaCertificacion = agregarCerosAdelante(formatearFechaArgentina(fechaCertificacion),8);
        this.fechaInicioCicloLectivo = agregarCerosAdelante(formatearFechaArgentina(fechaInicioCicloLectivo),8);
        this.esEducacionOficial = esEducacionOficial.toUpperCase().charAt(0);
        this.esAlumnoRegular = esAlumnoRegular.toUpperCase().charAt(0);
    }

    @NonNull
    String cueAnexo, regice, cuilEstudiante, cicloLectivo, nivel;

    String gradoAnio ="";
    String nombreCursoCarrera="";
    String fechaCertificacion="";
    String fechaInicioCicloLectivo="";

    @NonNull
    char esEducacionOficial, esAlumnoRegular;

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
        this.nivel = this.agregarCerosAdelante(nivel, 2);
    }

    public void setGradoAnio(String gradoAnio) {
        if(Integer.parseInt(gradoAnio) == 1 || Integer.parseInt(gradoAnio) == 2){
            this.gradoAnio = agregarCerosAdelante(gradoAnio,2);
        }
    }

    public void setFechaInicioCicloLectivo(String fechaInicioCicloLectivo) {
        if(Integer.parseInt(this.nivel) > 5)
            this.fechaInicioCicloLectivo = formatearFechaExtrangera(fechaInicioCicloLectivo);;
    }

    public void setNombreCursoCarrera(String nombreCursoCarrera) {
        if(Integer.parseInt(this.nivel) > 5)
            this.nombreCursoCarrera = nombreCursoCarrera;
    }

    public void setFechaCertificacion(String fechaCertificacion) {
        //La fecha de certificación, se refiere a la fecha en la que el estableimiento educativo
        //acredita la escolaridad del menor.
        //Debe ser mayor o igual a la fecha de inicio de ciclo lectivo.
        this.fechaCertificacion = this.formatearFechaArgentina(fechaCertificacion);
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
        if(fechaInput.length() == 0){
            return "";
        }
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
        return this.cueAnexo + ";" +
                this.regice + ";" +
                this.cuilEstudiante + ";" +
                this.cicloLectivo + ";" +
                this.nivel + ";" +
                this.gradoAnio + ";" +
                this.esEducacionOficial + ";" +
                this.esAlumnoRegular + ";" +
                this.fechaInicioCicloLectivo + ";" +
                this.nombreCursoCarrera + ";" +
                this.fechaCertificacion + ";" +
                CODIGO_DEPENDENCIA + ";" +
                FIN_TUPLA;
    }
}
