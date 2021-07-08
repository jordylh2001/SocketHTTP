
import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;

public class HttpServer implements Runnable{

    protected boolean isStoped=false;
    protected static ServerSocket server=null;

    /**
     * WebServer constructor.
     */
    protected void start() {
        ServerSocket s;
        int port=8084;
        System.out.println("Webserver starting up on port: "+port);
        System.out.println("(press ctrl-c to exit)");
        try {
            s = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return;
        }

        System.out.println("Waiting for connection");
        for (;;) {
            try {
                // wait for a connection
                Socket remote = s.accept();
                // remote is now the connected socket
                System.out.println("Connection, sending data.");
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        remote.getInputStream()));
                PrintWriter out = new PrintWriter(remote.getOutputStream());

                // read the data sent.
                // stop reading once a blank line is hit. This
                // blank line signals the end of the client HTTP
                // headers.
                String str = ".";
                String[] split_str;
                String file;
                int cont;
                URL url;
                while (!str.equals("")) {
                    str = in.readLine();
                    if (str.startsWith("GET")) {
                        split_str = str.split(" ");
                        cont = split_str.length;
                        System.err.println(split_str[1]);
                        if (cont == 3) {
                            //Index
                            if (split_str[1].equals("/")) {
                                // Send the response
                                // Send the headers
                                out.println("HTTP/1.0 200 OK");
                                out.println("Content-Type: text/html");
                                out.println("Server: Bot");
                                // this blank line signals the end of the headers
                                out.println("");
                                // Send the HTML page
                                out.println("<H1>Welcome to the Ultra Mini-WebServer</H2>");
                            } else {
                                String parte2="/home/jmonreal/IdeaProjects/SocketHTTP"+split_str[1];
                                parte2=parte2.replaceAll(" ","");
                                url = new URL("file:/"+parte2);
                                if (url.getFile().endsWith("html")) {
                                    System.err.println("es una pagina");
                                    File file2=new File(parte2);
                                    System.err.println(parte2);
                                    System.err.println(file2.exists());
                                    if(file2.exists()){
                                        System.err.println("El archivo existe");
                                        String contenido=obtener(parte2);
                                        // Send the response
                                        // Send the headers
                                        out.println("HTTP/1.0 200 OK");
                                        out.println("Content-Type: text/html");
                                        out.println("Server: Bot");
                                        // this blank line signals the end of the headers
                                        out.println("");
                                        // Send the HTML page
                                        out.println(contenido);
                                    }else{
                                        // Send the response
                                        // Send the headers
                                        out.println("HTTP/1.0 404 Not found");
                                        out.println("Content-Type: text/html");
                                        out.println("Server: Bot");
                                        // this blank line signals the end of the headers
                                        out.println("<H1>Page not found</H1>");
                                        // Send the HTML page
                                    }
                                } else {
                                    System.err.println("ES un directorio");
                                    File file2=new File(parte2);
                                    System.err.println(parte2);
                                    System.err.println(file2.exists());
                                    if(file2.exists()){
                                        String[] lista=file2.list();
                                        Arrays.sort(lista);
                                        String enlace;
                                        // Send the response
                                        // Send the headers
                                        out.println("HTTP/1.0 200 OK");
                                        out.println("Content-Type: text/html");
                                        out.println("Server: Bot");
                                        out.println("");
                                        for(int i=0;i<lista.length;i++){
                                            if(lista[i].contains(".")){
                                                out.println("<h3><a href=\""+lista[1]+"\">"+lista[i]+"</a></h3>");
                                            }else{
                                                out.println("<h3><a href=\""+split_str[1]+"/"+lista[i]+"/"+"\">"+lista[i]+"</a></h3>");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                out.flush();
                remote.close();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

    private String obtener(String parte2) throws IOException {
        String contenido="";
        String linea;
        FileReader f=new FileReader(parte2);
        BufferedReader b=new BufferedReader(f);
        while((linea=b.readLine())!=null){
            contenido=contenido+linea+"\n";
        }
        return contenido;
    }

    /**
     * Start the application.
     *
     * @param args
     *            Command line parameters are not used.
     */
    public static void main(String args[]) throws MalformedURLException {
        int port=8082;
        try{
            server=new ServerSocket(port);
            System.err.println("Server is running on port: "+port);
            Thread t1=new Thread(new HttpServer());
            t1.start();
        }catch(IOException e){
            System.out.println("No se pudo abrir en el puerto: "+port);
        }
    }

    @Override
    public void run() {
        while(!isStoped){
            try {
                Socket client=server.accept();
                System.out.println(client.getRemoteSocketAddress()+"has connected");
                new Thread(new Worker(client)).start();
            } catch (IOException e) {
                System.out.println(e);
            }

        }
    }
}
