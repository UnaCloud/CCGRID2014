import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import loadgenerator.*;
import utils.FileGenerator;
public class LoadGenerator {
    static TaskProcess hilos[];
    static int threads=1;
    public static void main(String[] args)throws Exception{
        FileGenerator.generateReadFile(args[0]);
        Socket s=new Socket("157.253.204.12",85);
        try(PrintWriter pw=new PrintWriter(s.getOutputStream(),true);BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()))){
            pw.println(args[0]);
            threads=Integer.parseInt(args[1]);
            pw.println(threads);
            pw.println(args[2]);
            for(String h;(h=br.readLine())!=null;){
                attendQuery(h.split(" +"),pw);
            }
        }
    }
    public static void attendQuery(final String[] args,final PrintWriter pw){
        new Thread(){
            @Override
            public void run(){
                processQuery(args, pw);
                System.out.println("End");
            }
        }.start();
    }
    public static void processQuery(String[] args,PrintWriter pw){
        System.out.println("processQuery "+Arrays.toString(args));
        if(args.length<1)return;
        if(args[0].equalsIgnoreCase("exit")){
            System.exit(0);
        }
        if(args[0].equalsIgnoreCase("stop")){
            System.out.println("Interrumpiendo "+hilos.length);
            if(hilos!=null){
                for(int e=0;e<hilos.length;e++){
                    hilos[e].interrupt();
                    hilos[e].terminado=true;
                }
                System.out.println("Interrumpidos");
            }
        }
        if(hilos!=null)return;
        long size=Long.MAX_VALUE;
        int times=1;
        String process=args[0];
        for(int e=0;e<args.length;e++)if(args[e]!=null){
            if(args[e].equals("-th"))threads=Integer.parseInt(args[e+1]);
            if(args[e].equals("-t"))times=Integer.parseInt(args[e+1]);
            if(args[e].equals("-s"))size=Long.parseLong(args[e+1]);
            if(args[e].equals("-S"))size=Long.MAX_VALUE;
        }
        hilos=new TaskProcess[threads];
        switch(process){
            case "process":
                for(int e=0;e<hilos.length;e++)hilos[e]=new TaskProcessingProcess(times,size);
                break;
            case "write":
                for(int e=0;e<hilos.length;e++)hilos[e]=new WriteDiskProcess(times,size);
                break;
            case "read":
                for(int e=0;e<hilos.length;e++)hilos[e]=new ReadDiskProcess(times,size);
                break;
            case "writeread":
                for(int e=0;e<hilos.length;e++)hilos[e]=new WriteReadDiskProcess(times,size);
                break;
        }
        for(int e=0;e<hilos.length;e++)hilos[e].start();
        for(int e=0;e<hilos.length;e++)try {
            hilos[e].join();
        } catch (InterruptedException ex) {
        }
        for(int e=0;e<hilos.length;e++)pw.println("hilo "+e+" "+hilos[e].getOutput());
        hilos=null;
    }
}
