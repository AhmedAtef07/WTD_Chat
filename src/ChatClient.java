import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ahmedatef on 16/03/17.
 */
public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8000);

        System.out.println("Client connected to the server successfully...");
        System.out.println("Start typing and press enter to send...");

        DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        Listener listener = new Listener(socket);
        listener.start();

        while(true) {
            String msg = new Scanner(System.in).nextLine();
            byte[] bytes = msg.getBytes();
            os.writeInt(bytes.length);
            os.write(bytes);
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
                while (true) {
                    DataInputStream is = new DataInputStream(socket.getInputStream());
                    int msgLength = is.readInt();
                    byte[] msgBytes = new byte[msgLength];
                    is.read(msgBytes);
                    System.out.println("Server Reply: " + new String(msgBytes));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
