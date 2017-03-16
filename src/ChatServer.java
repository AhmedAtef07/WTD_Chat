import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    static ArrayList<Socket> sockets = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);

        System.out.println("Server is up and running...");

        while(true) {
            Socket socket = serverSocket.accept();
            sockets.add(socket);
            new Listener(socket).start();
        }
    }

    static class Listener extends Thread {
        private Socket socket;

        public Listener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                while(true) {
                    // read from this stream
                    DataInputStream is = new DataInputStream(socket.getInputStream());
                    int msgLength = is.readInt();
                    byte[] msgBytes = new byte[msgLength];
                    is.read(msgBytes);

                    String receivedMsg = new String(msgBytes);

                    // send the message to everyone
                    for(Socket s : sockets) {
                        // should handle when one of the clients disconnects
                        // it will raise an exception then remove it from the list
                        DataOutputStream os = new DataOutputStream(s.getOutputStream());
                        byte[] bytes = receivedMsg.getBytes();
                        os.writeInt(bytes.length);
                        os.write(bytes);
                    }
                    System.out.println("Server Reply: " + new String(msgBytes));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
