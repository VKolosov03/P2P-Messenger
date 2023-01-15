import javax.json.Json;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class Peer {
    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter username & port");
        String setUpValues = bufferedReader.readLine();
        Random random = new Random();
        while (true) {
            try {
                Integer i = random.nextInt(99998) + 1;
                ServerThread serverThread = new ServerThread(i.toString());
                System.out.println("ypur port: "+i);
                serverThread.start();
                (new Peer()).updateListenToPeers(bufferedReader, setUpValues, serverThread);
                break;
            } catch (Exception e) {
                System.out.println("mistake");
            }
        }
    }
    public void updateListenToPeers(BufferedReader bufferedReader, String username, ServerThread serverThread) throws Exception {
        System.out.println("enter ports");
        System.out.println("Peers to receive message from");
        String input = bufferedReader.readLine();
        String[] inputValues = input.split(" ");
        if(!input.equals("s")) {
            for (String inputValue : inputValues) {
                Socket socket = null;
                try {
                    socket = new Socket(InetAddress.getLocalHost(), Integer.parseInt(inputValue));
                    (new PeerThread(socket)).start();
                } catch (Exception e) {
                    if (socket != null) socket.close();
                    else System.out.println("invalid input");
                }
            }
        }
        communicate(bufferedReader,username,serverThread);
    }
    public void communicate(BufferedReader bufferedReader, String username, ServerThread serverThread) throws Exception {
        try {
            System.out.println("Communicate");
            while (true) {
                String message = bufferedReader.readLine();
                if (message.equals("e")) {
                    break;
                } else if (message.equals("c")) updateListenToPeers(bufferedReader, username, serverThread);
                else {
                    StringWriter stringWriter = new StringWriter();
                    Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
                            .add("username", username)
                            .add("message", message)
                            .build());
                    serverThread.sendMessage(stringWriter.toString());
                }
            }
            System.exit(0);
        }catch (Exception ignored){}
    }
}
