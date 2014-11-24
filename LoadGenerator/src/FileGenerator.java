
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Asistente
 */
public class FileGenerator {
    public static void generateReadFile(){
        Random r=new Random();
        try(FileOutputStream pw=new FileOutputStream("./10G.in")){
            byte[] buffer=new byte[1024];
            for(long e=0;e<10*1024;e++){
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
