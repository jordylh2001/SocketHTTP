import java.net.*;
import java.io.*;

public class HttpServer implements Runnable{

    protected boolean isStopped=false;
    protected static ServerSocket server=null;
    protected static ServerSocket server2=null;

    public static void main(String[] args){
        int port=8082;
        try{
            server = new ServerSocket(port);
            System.err.println("Server is running on port: "+port);
            Thread t1=new Thread(new HttpServer());
            t1.start();
        }catch(IOException e){
            System.out.println("No se pudo abrir el puerto: "+port+"\n"+e);
        }
    }

    @Override
    public void run() {
        while(!isStopped){
            try {
                Socket client=server.accept();
                System.out.println(client.getRemoteSocketAddress()+"has connected");
                new Thread(new Worker(client)).start();
                Thread t2=new Thread(new HttpServer());
                t2.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
