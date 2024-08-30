package org.me.estadistica.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TextFileReader {
    public static List<String> readTextFile(String filePath){
        List<String> lines = new ArrayList<>();
        try(BufferedReader lector = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("Cp1252")))){
            String line = "";
            while ((line = lector.readLine()) != null){
                lines.add(line);
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return lines;
    }
}
