import java.io.PrintWriter;
import java.net.Socket;

public class Worker2 implements Runnable {
    protected Socket client;
    public Worker2(Socket client) {
        this.client=client;
    }

    @Override
    public void run() {
        try{
            PrintWriter out=new PrintWriter(client.getOutputStream());
            out.println("HTTP/1.2 200 OK");
            out.println("content-type: text/html");
            out.println("\r\n");
            out.println("<br>Hola es una prueba</br>");
            out.println("<a href=http://localhost:8082>Enlace a la pagina 1</a>");
            out.flush();
            out.close();
            client.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
