import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Server that recives clients connections
 * 
 * @author Roger Daniel; Guilherme Carvalho
 * @version 1.0
 * @since 2020-10-25
 */
public class Server {
    private static final int MAX_THREADS = 10; // number of threads
    private static ArrayList<ClientHandler> clients = new ArrayList<>(); // list of clients
    private static ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS); // pool of threads

    public static void main(String[] args) throws Exception {
        // validates arguments
        validateArgs(args);

        final int PORT = args.length > 1 ? Integer.parseInt(args[1]) : 8080;
        ServerSocket socket = new ServerSocket(PORT);

        try {
            while (true) {
                System.out.println("Server IP " + socket.getInetAddress().getHostAddress() + " on port " + PORT);
                Socket client = socket.accept();

                ClientHandler clientThread = new ClientHandler(client);
                clients.add(clientThread);
                pool.execute(clientThread);
            }
        } catch (Exception ioe) {
            System.err.println(ioe.getMessage());
            System.exit(-1);
        } finally {
            socket.close();
            System.out.println("Server Stopped");
        }
    }

    static void validateArgs(String[] args) {
        String avaliable = "-t";

        try {
            if (args.length > 0 && args.length < 3) {

                if (!args[0].equals(avaliable))
                    throw new IllegalArgumentException();

                if (args.length > 1)
                    Integer.parseInt(args[1]);

            } else {
                throw new IllegalArgumentException();
            }
        } catch (NumberFormatException e) {
            System.err.println("Argument " + args[1] + " is invalid. Must be an integer.");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid Arguments" + "Avaliables are: " + avaliable);
            System.exit(1);
        }
    }
}