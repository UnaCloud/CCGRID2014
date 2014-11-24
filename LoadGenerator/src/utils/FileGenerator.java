package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
public class FileGenerator {
    public static void generateReadFile(String type){
        Random r=new Random();
        try(FileOutputStream pw=new FileOutputStream("./10G.in")){
            byte[] buffer=new byte[1024*1024*10];
            long size=type.equals("virtual")?2:10;
            size*=1024l*1024l*1024;
            for(long e=0;e<size;e+=buffer.length){
                r.nextBytes(buffer);
                pw.write(buffer);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}