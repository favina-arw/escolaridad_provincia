package org.me.estadistica;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.me.estadistica.entity.Escolaridad;
import org.me.estadistica.util.*;

import java.io.*;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileGrabber fl = new FileGrabber("C:/Users/Usuario/Desktop/ANSES/reportes para apropiacion_escolaridad");
        List<String> rutas = fl.seleccionarArchivos();
        List<String> apropiados = TextFileReader.readTextFile("C:/Users/Usuario/Desktop/ANSES/SIE58451/APROPIA.F240826.txt");
        List<AlumnoData> datosAlumnos = new ArrayList<>();

        try{
            for(String ruta : rutas){
                POIFSFileSystem fs = new POIFSFileSystem(new File(ruta));
                HSSFWorkbook libro = new HSSFWorkbook(fs.getRoot(),true);
                Sheet hoja = libro.getSheetAt(0);

                int cueAnexo = Integer.parseInt(hoja.getRow(1).getCell(0).getStringCellValue().split(" - ")[0].trim());
                //El nivel se buscar ahora 2 posiciones ANTES del final de los datos.
                String nivel = hoja.getRow(1).getCell(0).getStringCellValue().split(" - ")[hoja.getRow(1).getCell(0).getStringCellValue().split(" - ").length - 2].trim();
                String modalidad = hoja.getRow(1).getCell(0).getStringCellValue().split(" - ")[3].trim();


                for (int i = 3; i <= hoja.getLastRowNum(); i++) {
                    Row fila = hoja.getRow(i);
                    AlumnoData alumnoAux = new AlumnoData();
                    alumnoAux.setCueAnexo(cueAnexo);
                    alumnoAux.setCuilt(0);
                    if(fila.getCell(1).getStringCellValue().split(" ")[0].equalsIgnoreCase("DNI")){
                        if(fila.getCell(1).getStringCellValue().split(" ")[1].length() == 11 ){
                            alumnoAux.setDni(Integer.parseInt(fila.getCell(1).getStringCellValue().split(" ")[1].substring(2,10)));
                        }else{
                            alumnoAux.setDni(Integer.parseInt(fila.getCell(1).getStringCellValue().split(" ")[1]));
                        }
                    }else{
                        alumnoAux.setDni(0);
                    }
                    alumnoAux.setNombreApellido(fila.getCell(0).getStringCellValue().replace(",", "").trim());
                    alumnoAux.setCicloLectivo(Year.now().toString());

                    alumnoAux.setNivelEducativo(nivel);
                    if(cueAnexo == 260061600 || String.valueOf(cueAnexo).equalsIgnoreCase("260061600"))
                        alumnoAux.setNivelEducativo("02");

                    if(modalidad.equalsIgnoreCase("Adultos")){
                        alumnoAux.setGradoAño(fila.getCell(4).getStringCellValue().isEmpty() ? ' ' : fila.getCell(4).getStringCellValue().split(" ")[1].charAt(0));
                    } else if(nivel.equalsIgnoreCase("Inicial")) {
                        if (fila.getCell(4).getStringCellValue().equalsIgnoreCase("Lactantes") ||
                                fila.getCell(4).getStringCellValue().equalsIgnoreCase("Deambuladores")||
                                fila.getCell(4).getStringCellValue().equalsIgnoreCase("Educación Temprana")){
                            alumnoAux.setGradoAño('0');
                        }else{
                            try{
                                alumnoAux.setGradoAño(fila.getCell(4).getStringCellValue().isEmpty() ? ' ' : fila.getCell(4).getStringCellValue().split(" ")[2].charAt(0));
                            }catch (ArrayIndexOutOfBoundsException aioobe){
                                System.out.println(aioobe.getMessage());
                                System.out.println(fila.getCell(4).getStringCellValue());
                            }


                        }
                    }else{
                        alumnoAux.setGradoAño(fila.getCell(4).getStringCellValue().isEmpty() ? ' ' : fila.getCell(4).getStringCellValue().charAt(0));
                    }

                    alumnoAux.setEsAlumnoRegular(fila.getCell(8).getStringCellValue().equalsIgnoreCase("Regular")? 'S' : 'N');

                    //System.out.println("Alumno generado: " + alumnoAux);
                    datosAlumnos.add(alumnoAux);

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        List<String> alumnosNoEncontrados = new ArrayList<>();
        List<AlumnoData> alumnosNoRegulares = new ArrayList<>();
        List<Escolaridad> escolaridadesParaImprimir = new ArrayList<>();
        for(String apropiado : apropiados){
            AlumnoData alumnoApropiado = new AlumnoData();
            Escolaridad escolaridad = new Escolaridad();
            int dni = Integer.parseInt(apropiado.substring(2,10));
            String apellidoNombre = apropiado.substring(10,50).trim();
            Long cuilt = Long.parseLong(apropiado.substring(149,160));

            for (AlumnoData a : datosAlumnos){
                if(a.getDni() == dni && a.getNombreApellido().equalsIgnoreCase(apellidoNombre)){
                    alumnoApropiado = a;
                    break;
                }
            }

            if(alumnoApropiado == null){
                alumnosNoEncontrados.add(apropiado);
            }else if(!alumnoApropiado.isAlumnoRegular()){
                alumnosNoRegulares.add(alumnoApropiado);
                /*
                System.out.println("--------------ALUMNO NO REGULAR----------------");
                System.out.println("DNI: " + dni + ".-");
                System.out.println("APELLIDO Y NOMBRE: " + apellidoNombre + ".-");
                System.out.println("CUIL/T: " + cuilt + ".-");
                System.out.println("------------------------------------------------");

                 */
            }else{
                escolaridad.setCueAnexo(String.valueOf(alumnoApropiado.getCueAnexo()));
                escolaridad.setRegice("          "); //10 espacios en blanco
                escolaridad.setCuilEstudiante(String.valueOf(cuilt));
                escolaridad.setCicloLectivo(alumnoApropiado.getCicloLectivo());
                //System.out.println(alumnoApropiado.getNivelEducativo());
                escolaridad.setNivel(String.valueOf(alumnoApropiado.getNivelEducativo()));
                //System.out.println(alumnoApropiado.getGradoAño());
                escolaridad.setGradoAnio(alumnoApropiado.getGradoAño()+"");
                escolaridad.setEsEducacionOficial("S");
                escolaridad.setEsAlumnoRegular(alumnoApropiado.getEsAlumnoRegular()+"");
                escolaridad.setFechaInicioCicloLectivo(""); //8 espacios en blanco
                escolaridad.setNombreCursoCarrera("");
                escolaridad.setFechaCertificacion("23/08/2024");

                escolaridadesParaImprimir.add(escolaridad);


            }
        }
        System.out.println("Alumnos apropiados no encontrados: " + alumnosNoEncontrados.size());
        System.out.println("Alumnos apropiados no regulares: " + alumnosNoRegulares.size());
        System.out.println("Alumnos escolarizados listos para enviar: " + escolaridadesParaImprimir.size());

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyMMdd");
        String hoy = fechaActual.format(formato);
        String archivo = "C:/Users/Usuario/Desktop/ANSES/CERTESC_90022804_"+hoy+".txt";

        try(BufferedWriter escritor = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(
                                archivo),
                        "Cp1252"))){
            for(Escolaridad e : escolaridadesParaImprimir){
                escritor.write(e.toString());
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        /*
        escolaridad.setCueAnexo(String.valueOf(alumno.getCueAnexo()));
        escolaridad.setRegice("          "); //10 espacios en blanco
        escolaridad.setCuilEstudiante(String.valueOf(20492162443L));
        escolaridad.setCicloLectivo(alumno.getCicloLectivo());
        escolaridad.setNivel(String.valueOf(alumno.getNivelEducativo()));
        escolaridad.setGradoAnio(alumno.getGradoAño()+"");
        escolaridad.setEsEducacionOficial("S");
        escolaridad.setEsAlumnoRegular(alumno.getEsAlumnoRegular()+"");
        escolaridad.setFechaInicioCicloLectivo("        "); //8 espacios en blanco
        escolaridad.setNombreCursoCarrera("");
        escolaridad.setFechaCertificacion("23/08/2024");

        System.out.println(escolaridad);

        for (int i = 0; i <= 5; i++) {
            Escolaridad escolaridad = null;
            AlumnoData alumno;
            String tupla = apropiados.get(i);
            int dni = Integer.parseInt(tupla.substring(2,10));
            String apellidoNombre = tupla.substring(10,50).trim();
            Long cuilt = Long.parseLong(tupla.substring(149,160));

            alumno = alumnosGrabber.buscar(49216244,"ACEVEDO, MISAEL ISAIAS");

            if(alumno == null){
                System.out.println("--------------ALUMNO NO SE ENCUENTRA EN LA LISTA----------------");
                System.out.println("DNI: " + dni + ".-");
                System.out.println("APELLIDO Y NOMBRE: " + apellidoNombre + ".-");
                System.out.println("CUIL/T: " + cuilt + ".-");
                System.out.println("----------------------------------------------------------------");

            }else if(!alumno.isAlumnoRegular()){
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
        */
    }
}