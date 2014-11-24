
package loadgenerator;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import utils.MyUtilies;

/**
 *
 * Ing. Mario Jose Villamizar Cano
 * Universidad de los Andes
 * Bogota - Colombia
 * Maestria en Ingenieria - Sistemas y Computacion
 * mjvc007@hotmail.com - mj.villamizar24@uniandes.edu.co
 * 2009
 * 
 */

public class DiskProcess{
    private static volatile int file=0;
    public synchronized static File getTempWriteFile(){
        File f=new File("./testWriteFile"+file+++".in");
        f.deleteOnExit();
        return f;
    }
    /*
     * Read a file f to the disk
     * return the line number read
     */
    public static long readFile(File f,long size){
        long readLineNumber = 0,readedSize=0;
        try(BufferedReader input =  new BufferedReader(new FileReader(f))) {
             for(String h;(h=input.readLine())!=null&&readedSize<size;){
                 readedSize+=h.length();
                 readLineNumber++;
             }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DiskProcess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DiskProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return readLineNumber;
    }

    public static void writeSimpleFile(File f, int sizeBytes){
        final String lineWritten = "abcdefghijklmnopqrstuvwxyz12345\n";
        try(Writer output = new BufferedWriter(new FileWriter(f))){
            for(long i=0,l=lineWritten.length();i<sizeBytes;i+=l)output.write(lineWritten);
        }
        catch(IOException e){
            System.out.println("Error opening the file: "+e.toString());
        }
    }

    public static void writeComplexFile(File f, int sizeKBytes){
        long fileSize = 0;
        long sizeBytes = sizeKBytes * 1024;
        Random r=new Random();
        try(Writer output = new BufferedWriter(new FileWriter(f));){
            while(fileSize<sizeBytes){
                String lineWritten = ""+r.nextDouble();
                output.write(lineWritten);
                fileSize += lineWritten.length();
            }
        }
        catch(IOException e){
            System.out.println("Error opening the file: "+e.toString());
        }
    }

    /*
     * Compress a source file (sourceFile) to a destination file (targetFile)
     */
    public static void compressFile(File sourceFile, File targetFile){
        try {
            String pathSourceFile = sourceFile.getAbsolutePath();
            String pathTargetFile = targetFile.getAbsolutePath();
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pathTargetFile));
            FileInputStream fis = new FileInputStream(pathSourceFile);

            // put a new ZipEntry in the ZipOutputStream
            zos.putNextEntry(new ZipEntry(pathSourceFile));

            int size = 0;
            byte[] buffer = new byte[1024];

                // read data to the end of the source file and write it to the zip
                // output stream.
            while ((size = fis.read(buffer, 0, buffer.length)) > 0) {
                zos.write(buffer, 0, size);
            }

            zos.closeEntry();
            fis.close();

            // Finish zip process
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
