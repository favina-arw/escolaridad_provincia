package org.me.estadistica;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.me.estadistica.entity.Escolaridad;
import org.me.estadistica.util.FileGrabber;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {
            FileGrabber fg = new FileGrabber();
            String rutaDestino = "";

            if(args.length == 0){
                Scanner input = new Scanner(System.in);
                System.out.println("Ingrese la ruta absoluta donde se encuentran los archivos a leer");
                fg.setPathToFiles(input.nextLine());
                System.out.println("Ingrese la ruta absoluta donde se deba depositar el archivo resulante");
                rutaDestino = input.nextLine();
                System.out.println(rutaDestino);
                if (rutaDestino.charAt(rutaDestino.length()-1) != '/'){
                    rutaDestino.concat("/");
                }
                System.out.println(rutaDestino);
            }else{
                fg.setPathToFiles(args[0]);
                rutaDestino = args[1];
            }
            List<String> archivos = fg.seleccionarArchivos();

            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyMMdd");
            String hoy = fechaActual.format(formato);

            //rutaDestino+"/APRO.CE.XX."+hoy+".txt"

            BufferedWriter escritor = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(rutaDestino+"/CERTESC_90022804_"+hoy+".txt"),"Windows-1252"));

            /*PARA PENSAR*/
            /*¿CÓMO VAMOS A ENCONTRAR A LOS ALUMNOS?*/

            for (String archivo : archivos){
                POIFSFileSystem fs = new POIFSFileSystem(new File(archivo));
                HSSFWorkbook libro = new HSSFWorkbook(fs.getRoot(),true);
                Sheet hoja = libro.getSheetAt(0);

                Escolaridad escolaridad = new Escolaridad();

                //Cambiar el 0 por la fila en la que comienzan los datos
                for (int i = 0; i <= hoja.getLastRowNum() ; i++){
                    Row fila = hoja.getRow(i);
                    /* INICIO LOGICA DE ARMADO DEL OBJETO ESCOLARIDAD */



                    /* FIN LOGICA DE ARMADO DEL OBJETO ESCOLARIDAD */
                    escritor.write(escolaridad.toString());
                }
                escritor.write(escolaridad.toString());
                /*---- CERRDADO DE ARCHIVO DE LECTURA----*/
                fs.close();
            }
            /*---- CERRDADO DE ARCHIVO DE ESCRITURA----*/
            escritor.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        /*Pruebas de print*/

        /*Escolaridad escolaridadUno = new Escolaridad("260065000","","20380809290",
                "2024","2","5","","17/06/2024",
                "",'S','S');

        Escolaridad escolaridadDos = new Escolaridad("260065000","","20380809290",
                "2024","6","","Tecnicatura Universitaria en Desarrollo de Software","17/06/2024",
                "03/04/2024","Si","Si");

        System.out.println(escolaridadUno);
        System.out.println(escolaridadDos);*/
    }
}