package me.ictmo1;

import com.sun.tools.javac.Main;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class MonitorGUI {

    static HashMap<Socket, Long> serverTime = new HashMap<>();

    static JProgressBar procesbelastingProgressBar = new JProgressBar();
    static JProgressBar beschikbaarheidProgressBar = new JProgressBar();
    static JProgressBar diskUsageProgressBar = new JProgressBar();

    static ServerSocket serverSocket = null;


    public static void main(String[] args) {

        JFrame frame = new JFrame("Graphic Tracer Monitoring");
        frame.setSize(720, 450);

        frame.setLayout(new MigLayout("", "[grow,fill]"));

        frame.add(new JLabel("Beschikbaar"));
        frame.add(new JCheckBox());

        procesbelastingProgressBar.setStringPainted(true);
        procesbelastingProgressBar.setMaximum(100);

        frame.add(procesbelastingProgressBar, "skip 5, wrap");

        frame.add(new JLabel("Diskruimte"));

        beschikbaarheidProgressBar.setStringPainted(true);
        beschikbaarheidProgressBar.setMaximum(100);

        frame.add(beschikbaarheidProgressBar, "skip 1");

        frame.add(new JLabel("Uptime"), "skip 3");
        frame.add(new JTextField(3), "wrap");


        diskUsageProgressBar.setStringPainted(true);
        diskUsageProgressBar.setMaximum(100);

//        new Thread(() -> {
//            while (true) {
//                diskUsageProgressBar.setValue(new Random().nextInt(0, 100));
//                beschikbaarheidProgressBar.setValue(new Random().nextInt(0, 100));
//                procesbelastingProgressBar.setValue(new Random().nextInt(0, 100));
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();


        frame.add(diskUsageProgressBar, "skip 2");


        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(6969);
                while (true) {
                    handleClient(serverSocket.accept());
                }

            } catch (Exception t) {
                t.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Er is een fout opgetreden!");
            }
        }).start();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev){
                if(serverSocket != null){
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });

        new Thread(() -> {
            int totalTicks = 0;
            int ticksAlive = 0;

            while(true){
                if(!serverTime.isEmpty()){
                    long time = serverTime.values().iterator().next();
                    long delta = System.currentTimeMillis() - time;

                    if(delta < 3000){
                        ticksAlive++;
                    }
                }

                totalTicks++;
                double beschikbaarheid = (ticksAlive * 100d) / totalTicks;
                beschikbaarheidProgressBar.setValue((int) beschikbaarheid);

                beschikbaarheidProgressBar.setString("Beschikbaarheid " + String.format("%.2f", beschikbaarheid) + " %");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        frame.setVisible(true);
    }



    public static void handleClient(Socket socket) {
        serverTime.put(socket, System.currentTimeMillis());

        try {
            var input = new Scanner(new BufferedInputStream(socket.getInputStream()));
            while (true) {
                while (!input.hasNextLine()) {
                    Thread.yield();
                }
//                System.out.println("Binnen: " + input.nextLine());
                String lijn = input.nextLine();
                String[] parts = lijn.split(" ");

                if (parts.length != 2){
                    JOptionPane.showMessageDialog(null, "Een verouderde monitorserver probeert contact te maken");
                    return;
                }
                double processorbelasting = (Double.parseDouble(parts[0])*100);
                double diskUsage = (Double.parseDouble(parts[1])*100);

                procesbelastingProgressBar.setValue((int) processorbelasting);
                procesbelastingProgressBar.setString("Procesbelasting " + String.format("%.2f", processorbelasting) + " %");

                diskUsageProgressBar.setValue((int) diskUsage);
                diskUsageProgressBar.setString("Gebruikt " + String.format("%.2f", diskUsage) + " %");

                serverTime.put(socket, System.currentTimeMillis());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
