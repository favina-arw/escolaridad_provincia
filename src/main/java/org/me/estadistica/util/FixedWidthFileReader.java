package org.me.estadistica.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FixedWidthFileReader {

    public static List<String> readFixedWidthFile(String filePath, int lineWidth){
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), Charset.forName("Cp1252")))) {

            char[] buffer = new char[lineWidth];
            int charsRead;

            while ((charsRead = reader.read(buffer)) != -1) {
                // Si la longitud leída es menor que el ancho esperado, solo añade los caracteres leídos
                if (charsRead < lineWidth) {
                    lines.add(new String(buffer, 0, charsRead));
                } else {
                    lines.add(new String(buffer));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }
}
