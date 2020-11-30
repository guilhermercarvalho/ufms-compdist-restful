import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The ClientHandler represents de socket connection with the client
 * 
 * @author Roger Daniel; Guilherme Carvalho
 * @version 1.0
 * @since 2020-10-25
 */
public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private OutputStream out;

    /**
     * ClientHandler constructor that instanciate socket, input and output of client
     * 
     * @param c - client of request, Socket class
     * @throws IOException
     */
    ClientHandler(Socket c) throws IOException {
        client = c;
        InputStreamReader inst = new InputStreamReader(client.getInputStream());
        in = new BufferedReader(inst);
        out = client.getOutputStream();
        client.setSoTimeout((60 * 1000));
    }

    /**
     * Method that handle with client requests. write content in the standard output
     * client and execute programs
     */
    public void run() {
        System.out.println("Client " + client.toString());

        try {
            while (true) {
                StringBuilder reqBuilder = new StringBuilder();
                String line;

                // read input client
                while (!(line = in.readLine()).isBlank()) {
                    reqBuilder.append(line + "\r\n");
                }

                String req = reqBuilder.toString();

                // serialize input
                String[] reqsLines = req.split("\r\n");
                String[] reqLine = reqsLines[0].split(" ");

                String method = reqLine[0];
                String path = reqLine[1];
                String version = reqLine[2];
                String host = reqsLines[1].split(" ")[1];

                List<String> headers = new ArrayList<>();

                for (int h = 2; h < reqsLines.length; h++) {
                    String header = reqsLines[h].toLowerCase();
                    headers.add(header);
                }

                // print client information
                String accessLog = String.format("method %s, path %s, version %s, host %s, headers %s", method, path,
                        version, host, headers.toString());
                System.out.println(accessLog);

                // convert path into avaliable file in the sistem
                Path filePath = getFilePath(path);
                File fileName = new File(filePath.toString());

                if (fileName.getParent().substring(6).equals("/cgi-bin")) { // file in cgi-bin
                    ByteArrayOutputStream content = new ByteArrayOutputStream();

                    // get file to exec and query
                    ProcessBuilder pb;
                    if(fileName.getPath().contains("?"))  {
                        String fileExec = fileName.getPath().split("\\?")[0];
                        String queryString = fileName.getPath().split("\\?")[1];
                        
                         pb = new ProcessBuilder(fileExec);
                        
                        // add query to environment
                        Map<String, String> env = pb.environment();
                        env.put("QUERY_STRING", queryString);
                    } else {
                        pb = new ProcessBuilder(fileName.getPath());
                    }

                    Process proc = pb.start();

                    InputStream is = proc.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    // read standard output of program
                    String lineq;
                    while ((lineq = br.readLine()) != null) {
                        content.write(lineq.getBytes());
                    }

                    sendResponse("200 Document Follows", "text/html", content.toByteArray());

                    // close all streams
                    br.close();
                    isr.close();
                    is.close();
                    content.close();

                } else if (Files.isDirectory(filePath)) { // file is a directory
                    String[] names = fileName.list();
                    ByteArrayOutputStream content = new ByteArrayOutputStream();
                    content.write(("<html xmlns=\"http://www.w3.org/1999/xhtml\"xml:lang=\"en\" lang=\"en\">\r\n"
                            + "<head>\r\n" + "<title>In a Folder</title>\r\n" + "</head>\r\n" + "<body>\r\n"
                            + "<h1>Avaliable folders</h1>\r\n").getBytes());

                    content.write(("<spam><a href=\"" + fileName.getParent().substring(6)
                            + "/\">go back</a></spam>\r\n</br>\r\n").getBytes());

                    content.write("<ul>\r\n".getBytes());

                    for (int i = 0; i < names.length; i++) {
                        String newLine = String.format("<li><a href=\"%s\">%s</a></li>\n",
                                filePath.toString().substring(6) + "/" + names[i], names[i]);
                        content.write(newLine.getBytes());
                    }
                    content.write("</ul>\r\n<body>\r\n".getBytes());

                    sendResponse("200 Document Follows", "text/html", content.toByteArray());

                } else if (Files.exists(filePath)) { // is a file
                    String contentType = Files.probeContentType(filePath);
                    sendResponse("200 Document Follows", contentType, Files.readAllBytes(filePath));
                } else { // not found
                    byte[] notFoundContet = "Could not find the specified URL".getBytes();
                    sendResponse("404 File Not Found", "text/plain", notFoundContet);
                }
            }
        } catch (SocketTimeoutException err) {
            System.err.println("Client timeout exceeded");
            System.err.println("Closing connection...");
            try {
                in.close();
                out.close();
                client.close();
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        } finally {
            System.err.println("Closing connection...");
            try {
                in.close();
                out.close();
                client.close();
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

    private void sendResponse(String status, String contentType, byte[] content) throws IOException {
        out.write(("HTTP/1.1 " + status + "\r\n").getBytes());
        out.write("Server: FACOM-CD-2020/1.0\r\n".getBytes());
        out.write(("Content-type: " + contentType + "\r\n").getBytes());
        out.write(("Content-length: " + content.length + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(content);
        out.write("\r\n\r\n".getBytes());
        out.flush();
    }

    private Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "/index.html";
        }
        return Paths.get("../www", path);
    }
}
