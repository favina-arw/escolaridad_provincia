package org.me.estadistica.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Setter
@NoArgsConstructor
public class AlumnosGrabber {

    private List<AlumnoData> alumnos = new ArrayList<>();

    public List<AlumnoData> getAlumnos(){
        cargarAlumnos();
        return this.alumnos;
    }

    public void cargarAlumnos(){
        try{
            FileGrabber fileGrabber = new FileGrabber("C:/Users/Admin/reportes_para_apropiacion_escolaridad/DATOS");
            List<String> archivos = fileGrabber.seleccionarArchivos();
            for(String archivo : archivos){
                POIFSFileSystem fs = new POIFSFileSystem(new File(archivo));
                HSSFWorkbook libro = new HSSFWorkbook(fs.getRoot(),true);
                Sheet hoja = libro.getSheetAt(0);

                AlumnoData alumnoData = new AlumnoData();
                for (int i = 0; i < hoja.getLastRowNum(); i++) {
                    alumnoData.setCueAnexo(Integer.parseInt(hoja.getRow(1).getCell(0).getStringCellValue().split("-")[0].trim()));
                    alumnoData.setNivelEducativo(hoja.getRow(1).getCell(0).getStringCellValue().split("-")[2].trim());
                    Row fila = hoja.getRow(i);
                    if(fila.getCell(1).getStringCellValue().split(" ")[0].equalsIgnoreCase("DNI")){
                        alumnoData.setNombreApellido(fila.getCell(0).getStringCellValue().replace(",", " "));
                        alumnoData.setDni(Integer.parseInt(fila.getCell(1).getStringCellValue().split(" ")[1]));
                        alumnoData.setGradoAño(fila.getCell(4).getStringCellValue().isEmpty() ? ' ':
                                fila.getCell(4).getStringCellValue().charAt(0));
                        alumnoData.setCicloLectivo("2024");
                        alumnoData.setEsAlumnoRegular(fila.getCell(8).getStringCellValue().equalsIgnoreCase("Regular") ? 'S' : 'N');

                        this.alumnos.add(alumnoData);
                        System.out.println("Agregado: " + alumnoData);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}
