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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter

public class AlumnosGrabber {
    private static List<AlumnoData> alumnos = new LinkedList<>();

    public AlumnosGrabber(){
        this.cargarAlumnos();
    }

    public List<AlumnoData> getAlumnos (){
        if (alumnos.isEmpty())
            throw new RuntimeException("Lista vacía");
        else
            return alumnos;
    }

    public void cargarAlumnos(){
        FileGrabber fileGrabber = new FileGrabber("C:/Users/Admin/reportes_para_apropiacion_escolaridad/DATOS");
        List<String> archivos = fileGrabber.seleccionarArchivos();
        archivos.forEach(archivo -> {
            try {
                AlumnoData alumnoData = new AlumnoData();
                POIFSFileSystem fs = new POIFSFileSystem(new File(archivo));
                HSSFWorkbook libro = new HSSFWorkbook(fs.getRoot(), true);
                Sheet hoja = libro.getSheetAt(0);

                alumnoData.setCueAnexo(Integer.parseInt(hoja.getRow(1).getCell(0).getStringCellValue().split("-")[0].replace(" ", "")));
                alumnoData.setNivelEducativo(hoja.getRow(1).getCell(0).getStringCellValue().split("-")[2].replace(" ", ""));

                for (int i = 3; i < hoja.getLastRowNum(); i++) {
                    Row fila = hoja.getRow(i);

                    if (fila.getCell(1).getStringCellValue().split(" ")[0].equals("DNI")){

                        alumnoData.setNombreApellido(fila.getCell(0).getStringCellValue());
                        if (fila.getCell(1).getStringCellValue().split(" ")[1].length() < 11)
                            alumnoData.setDni(Integer.parseInt(fila.getCell(1).getStringCellValue().split(" ")[1]));
                        else
                            alumnoData.setDni(Integer.parseInt(fila.getCell(1).getStringCellValue().split(" ")[1].substring(2,10)));

                        alumnoData.setGradoAño(fila.getCell(4).getStringCellValue().isEmpty() ? ' ' : fila.getCell(4).getStringCellValue().charAt(0));
                        alumnoData.setCicloLectivo(Year.now().toString());
                        alumnoData.setEsAlumnoRegular(
                                fila.getCell(8).getStringCellValue().equals("Regular") ? 'S' : 'N'
                        );

                        alumnos.add(alumnoData);
                    }
                }

                /* CERRADO DE ARCHIVO */
                fs.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }

    public AlumnoData buscar(int dni, String apellidoNombre){
        for (AlumnoData alumno : alumnos){
            if (alumno.getDni() == dni && alumno.getNombreApellido().equalsIgnoreCase(apellidoNombre))
                return alumno;
        }
        return null;
    }
}
