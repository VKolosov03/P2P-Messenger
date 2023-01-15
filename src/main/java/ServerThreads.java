import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreads extends Thread{
    private ServerThread serverThread;
    private Socket socket;
    private PrintWriter printWriter;

    ServerThreads(Socket socket, ServerThread serverThread){
        this.socket=socket;
        this.serverThread=serverThread;
    }

    @Override
    public void run() {
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.printWriter =new PrintWriter(socket.getOutputStream(), true);
            while (true) serverThread.sendMessage(bufferedReader.readLine());
        } catch (IOException e) {
            serverThread.getServerThreads().remove(this);
        }
    }

    public PrintWriter getPrintWriter() {return printWriter;}
}
