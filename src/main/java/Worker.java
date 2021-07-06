import java.io.PrintWriter;
import java.net.Socket;

public class Worker implements Runnable {
    protected Socket client;
    public Worker(Socket client) {
        this.client=client;
    }

    @Override
    public void run() {
        try{
            PrintWriter out=new PrintWriter(client.getOutputStream());
            out.println("HTTP/1.1 200 OK");
            out.println("content-type: text/html");
            out.println("\r\n");
            out.println("<br>Hola es una prueba</br>");
            out.println("<a href=http://localhost:8083>Enlace a otra pagina2</a>");
            out.flush();
            out.close();
            client.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
