package org.me.estadistica;

import org.me.estadistica.entity.Escolaridad;
import org.me.estadistica.util.AlumnoData;
import org.me.estadistica.util.AlumnosGrabber;
import org.me.estadistica.util.FixedWidthFileReader;
import org.me.estadistica.util.TextFileReader;

import java.io.File;
import java.time.Year;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        AlumnosGrabber alumnosGrabber = new AlumnosGrabber();
        String archivoDeAlumnosApropiados = "C:/Users/Admin/reportes_para_apropiacion_escolaridad/REPORTES_SITASI/SIE58451/APROPIA.F240826.txt";
        List<String> apropiados = TextFileReader.readTextFile(archivoDeAlumnosApropiados);

        for (int i = 0; i <= 5; i++) {
            Escolaridad escolaridad = null;
            AlumnoData alumno;
            String tupla = apropiados.get(i);
            int dni = Integer.parseInt(tupla.substring(2,10));
            String apellidoNombre = tupla.substring(10,50).trim();
            Long cuilt = Long.parseLong(tupla.substring(149,160));

            alumno = alumnosGrabber.buscar(dni,apellidoNombre);

            if(alumno == null){
                System.out.println("--------------ALUMNO NO SE ENCUENTRA EN LA LISTA----------------");
                System.out.println("DNI: " + dni + ".-");
                System.out.println("APELLIDO Y NOMBRE: " + apellidoNombre + ".-");
                System.out.println("CUIL/T: " + cuilt + ".-");
                System.out.println("----------------------------------------------------------------");

            }if(!alumno.isAlumnoRegular()){
                System.out.println("--------------ALUMNO NO REGULAR----------------");
                System.out.println("DNI: " + dni + ".-");
                System.out.println("APELLIDO Y NOMBRE: " + apellidoNombre + ".-");
                System.out.println("CUIL/T: " + cuilt + ".-");
                System.out.println("------------------------------------------------");
            }else{
                escolaridad.setCueAnexo(String.valueOf(alumno.getCueAnexo()));
                escolaridad.setRegice("          "); //10 espacios en blanco
                escolaridad.setCuilEstudiante(String.valueOf(cuilt));
                escolaridad.setCicloLectivo(alumno.getCicloLectivo());
                escolaridad.setNivel(String.valueOf(alumno.getNivelEducativo()));
                escolaridad.setGradoAnio(alumno.getGradoAño()+"");
                escolaridad.setEsEducacionOficial("S");
                escolaridad.setEsAlumnoRegular(alumno.getEsAlumnoRegular()+"");
                escolaridad.setFechaInicioCicloLectivo("        "); //8 espacios en blanco
                escolaridad.setNombreCursoCarrera("");
                escolaridad.setFechaCertificacion("23/08/2024");

                System.out.println(escolaridad);

            }
        }

    }
}