import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer2 implements Runnable{

    protected boolean isStopped=false;
    protected static ServerSocket server=null;
    protected static ServerSocket server2=null;

    public static void main(String[] args){
        int port=8083;
        try{
            server = new ServerSocket(port);
            System.err.println("Server is running on port: "+port);
            Thread t1=new Thread(new HttpServer2());
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
                new Thread(new Worker2(client)).start();
                Thread t2=new Thread(new HttpServer2());
                t2.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
