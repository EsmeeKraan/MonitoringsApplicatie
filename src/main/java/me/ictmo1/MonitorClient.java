package me.ictmo1;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MonitorClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 6969);
        var scanner = new Scanner(System.in);
        var output = socket.getOutputStream();
        while (true) {
            output.write(scanner.nextLine().getBytes(StandardCharsets.UTF_8));
            output.flush();
        }
    }

}
