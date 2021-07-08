import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;

public class Worker implements Runnable {
    protected Socket client;
    public Worker(Socket client) {
        this.client=client;
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

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());
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
                            String parte2 = "/home/jmonreal/IdeaProjects/SocketHTTP" + split_str[1];
                            parte2 = parte2.replaceAll(" ", "");
                            url = new URL("file:/" + parte2);
                            if (url.getFile().endsWith("html")) {
                                System.err.println("es una pagina");
                                File file2 = new File(parte2);
                                System.err.println(parte2);
                                System.err.println(file2.exists());
                                if (file2.exists()) {
                                    System.err.println("El archivo existe");
                                    String contenido = obtener(parte2);
                                    // Send the response
                                    // Send the headers
                                    out.println("HTTP/1.0 200 OK");
                                    out.println("Content-Type: text/html");
                                    out.println("Server: Bot");
                                    // this blank line signals the end of the headers
                                    out.println("");
                                    // Send the HTML page
                                    out.println(contenido);
                                } else {
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
                                File file2 = new File(parte2);
                                System.err.println(parte2);
                                System.err.println(file2.exists());
                                if (file2.exists()) {
                                    String[] lista = file2.list();
                                    Arrays.sort(lista);
                                    String enlace;
                                    // Send the response
                                    // Send the headers
                                    out.println("HTTP/1.0 200 OK");
                                    out.println("Content-Type: text/html");
                                    out.println("Server: Bot");
                                    out.println("");
                                    for (int i = 0; i < lista.length; i++) {
                                        if (lista[i].contains(".")) {
                                            out.println("<h3><a href=\"" + lista[1] + "\">" + lista[i] + "</a></h3>");
                                        } else {
                                            out.println("<h3><a href=\"" + split_str[1] + "/" + lista[i] + "/" + "\">" + lista[i] + "</a></h3>");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            out.flush();
            client.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
