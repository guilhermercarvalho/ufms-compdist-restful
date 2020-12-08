package server;

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

import org.json.JSONException;

import api.Api;

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
                StringBuilder payload = new StringBuilder();
                String line = null;

                while (!(line = in.readLine()).isEmpty()) {
                    reqBuilder.append(line + "\r\n");
                }

                while (in.ready()) {
                    payload.append((char) in.read());
                }

                String req = reqBuilder.toString();
                String body = payload.toString();

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

                // API Functionalities
                if (path.contains("/actors") || path.contains("/movies")) {
                    Api api = new Api();
                    String data;
                    String application;
                    int id_search;
                    switch (method) {
                        case "GET":
                            // Return Actors
                            if ((path.equals("/actors.json") || path.equals("/actors.xml")) && body.length() == 0) {
                                if (path.contains("json")) {
                                    data = api.getActors("json");
                                    application = "application/json";
                                } else {
                                    data = api.getActors("xml");
                                    application = "application/xml";
                                }

                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());

                                // Return Actors Search
                            } else if ((path.equals("/actors.json") || path.equals("/actors.xml"))
                                    && body.length() > 0) {
                                if (path.contains("json")) {
                                    data = api.searchActor(body, "json");
                                    application = "application/json";
                                } else {
                                    data = api.searchActor(body, "xml");
                                    application = "application/xml";
                                }

                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());

                                // Return Actor with ID
                            } else if (path.matches("/actors/\\d*\\.json") || path.matches("/actors/\\d*\\.xml")) {
                                id_search = Integer.parseInt(path.split("/actors/")[1].split("\\.")[0]);

                                if (path.contains("json")) {
                                    data = api.getActors(id_search, "json");
                                    application = "application/json";
                                } else {
                                    data = api.getActors(id_search, "xml");
                                    application = "application/xml";
                                }

                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());

                                // Return Movies
                            } else if ((path.equals("/movies.json") || path.equals("/movies.xml"))
                                    && body.length() == 0) {
                                if (path.contains("json")) {
                                    data = api.getMovies("json");
                                    application = "application/json";
                                } else {
                                    data = api.getMovies("xml");
                                    application = "application/xml";
                                }

                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());

                                // Return Movies Search
                            } else if ((path.equals("/movies.json") || path.equals("/movies.xml"))
                                    && body.length() > 0) {
                                if (path.contains("json")) {
                                    data = api.searchMovie(body, "json");
                                    application = "application/json";
                                } else {
                                    data = api.searchMovie(body, "xml");
                                    application = "application/xml";
                                }
                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());

                                // Return Movie with ID
                            } else if (path.matches("/movies/\\d*\\.json") || path.matches("/movies/\\d*\\.xml")) {
                                id_search = Integer.parseInt(path.split("/movies/")[1].split("\\.")[0]);
                                
                                if (path.contains("json")) {
                                    data = api.getMovies(id_search, "json");
                                    application = "application/json";
                                } else {
                                    data = api.getMovies(id_search, "xml");
                                    application = "application/xml";
                                }
                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());

                                // Return Actors from a Movie
                            } else if (path.matches("/movies/\\d*/actors.json") || path.matches("/movies/\\d*/actors.xml")) {
                                id_search = Integer.parseInt(path.split("/movies/")[1].split("/actors")[0].split("\\.")[0]);
                                
                                if (path.contains("json")) {
                                    data = api.getMovieActors(id_search, "json");
                                    application = "application/json";
                                } else {
                                    data = api.getMovieActors(id_search, "xml");
                                    application = "application/xml";
                                }

                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());
                            }
                            break;
                        case "POST":
                            // Insert Actor
                            if (path.equals("/actors.json") || path.equals("/actors.xml")) {
                                if (path.contains("json")) {
                                    data = api.insertActor(body, "json");
                                    application = "application/json";
                                } else {
                                    data = api.insertActor(body, "xml");
                                    application = "application/xml";
                                }
                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());

                                // Insert Movie
                            } else if (path.equals("/movies.json") || path.equals("/movies.xml")) {
                                if (path.contains("json")) {
                                    data = api.insertMovie(body, "json");
                                    application = "application/json";
                                } else {
                                    data = api.insertMovie(body, "xml");
                                    application = "application/xml";
                                }
                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());
                            }
                            break;
                        case "PUT":
                            // Update Actor
                            if (path.matches("/actors/\\d*\\.json") || path.matches("/actors/\\d*\\.xml")) {
                                id_search = Integer.parseInt(path.split("/actors/")[1].split("\\.")[0]);

                                if (path.contains("json")) {
                                    data = api.updateActor(id_search, body, "json");
                                    application = "application/json";
                                } else {
                                    data = api.updateActor(id_search, body, "xml");
                                    application = "application/xml";
                                }

                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());

                                // Update Movie
                            } else if (path.matches("/movies/\\d*\\.json") || path.matches("/movies/\\d*\\.xml")) {
                                id_search = Integer.parseInt(path.split("/movies/")[1].split("\\.")[0]);

                                if (path.contains("json")) {
                                    data = api.updateMovie(id_search, body, "json");
                                    application = "application/json";
                                } else {
                                    data = api.updateMovie(id_search, body, "xml");
                                    application = "application/xml";
                                }

                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());

                            }
                            break;
                        case "DELETE":
                            // Remove Actor
                            if (path.matches("/actors/\\d*\\.json") || path.matches("/actors/\\d*\\.xml")) {
                                id_search = Integer.parseInt(path.split("/actors/")[1].split("\\.")[0]);
                                System.out.println(id_search);
                                if (path.contains("json")) {
                                    data = api.deleteActor(id_search, "json");
                                    application = "application/json";
                                } else {
                                    data = api.deleteActor(id_search, "xml");
                                    application = "application/xml";
                                }
                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());

                                // Remove Movie
                            } else if (path.matches("/movies/\\d*\\.json") || path.matches("/movies/\\d*\\.xml")) {
                                id_search = Integer.parseInt(path.split("/movies/")[1].split("\\.")[0]);

                                if (path.contains("json")) {
                                    data = api.deleteMovie(id_search, "json");
                                    application = "application/json";
                                } else {
                                    data = api.deleteMovie(id_search, "xml");
                                    application = "application/xml";
                                }
                                sendResponse("200 Document Follows", application + "; charset=utf-8", data.getBytes());
                            }
                            break;
                        default:
                            break;
                    }
                }

                // convert path into avaliable file in the sistem
                Path filePath = getFilePath(path);
                File fileName = new File(filePath.toString());

                if (fileName.getParent().substring(6).equals("/cgi-bin")) { // file in cgi-bin
                    ByteArrayOutputStream content = new ByteArrayOutputStream();

                    // get file to exec and query
                    ProcessBuilder pb;
                    if (fileName.getPath().contains("?")) {
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
        } catch (JSONException e) {
            System.err.println(e.toString());
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
