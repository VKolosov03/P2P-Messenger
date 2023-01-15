import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread{
    private ServerSocket serverSocket;
    private Set<ServerThreads> serverThreads = new HashSet<ServerThreads>();

    ServerThread(String port) throws IOException {
        serverSocket = new ServerSocket(Integer.parseInt(port));
    }

    @Override
    public void run() {
        try {
            while(true){
                ServerThreads serverThreads2 = new ServerThreads(serverSocket.accept(), this);
                serverThreads.add(serverThreads2);
                serverThreads2.start();
            }
        } catch (IOException e) {e.printStackTrace();}
    }

    public void sendMessage(String message) {
        try{
            serverThreads.forEach(t->t.getPrintWriter().println(message));
        } catch (Exception e) {e.printStackTrace();}
    }

    public Set<ServerThreads> getServerThreads() {return serverThreads;}
}
