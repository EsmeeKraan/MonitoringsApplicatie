package me.ictmo1;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.io.BufferedInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Timer;

public class MonitorGUI {

    // JavaFX: grafiek te tekenen

    public static void main(String[] args) {
        JFrame frame = new JFrame("Graphic Tracer Monitoring");
        frame.setSize(720, 450);

        frame.setLayout(new MigLayout("", "[grow,fill]"));

        frame.add(new JLabel("Beschikbaar"));
        frame.add(new JCheckBox());

        JProgressBar procesbelastingProgressBar = new JProgressBar();
        procesbelastingProgressBar.setString("Procesbelasting %");
        procesbelastingProgressBar.setStringPainted(true);
        procesbelastingProgressBar.setMaximum(100);

        frame.add(procesbelastingProgressBar, "skip 5, wrap");

        frame.add(new JLabel("Diskruimte"));

        JProgressBar beschikbaarheidProgressBar = new JProgressBar();
        beschikbaarheidProgressBar.setString("Beschikbaarheid %");
        beschikbaarheidProgressBar.setStringPainted(true);
        beschikbaarheidProgressBar.setMaximum(100);

        frame.add(beschikbaarheidProgressBar, "skip 1");

        frame.add(new JLabel("Uptime"), "skip 3, wrap");

        /* bende met tijd converten */

//        java.util.Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask()
//        {
//            public void run()
//            {
//                int second, minute, hour;
//                Calendar date = Calendar.getInstance();
//                second = date.get(Calendar.SECOND);
//                minute = date.get(Calendar.MINUTE);
//                hour = date.get(Calendar.HOUR);
//                frame.add(new JLabel("Current time is  " + hour + " : " +
//                        minute +" : " + second), "wrap, \r");
//            }
//        }, 1 * 5000, 1 * 5000);

        JProgressBar diskUsageProgressBar = new JProgressBar();
        diskUsageProgressBar.setString("Gebruikt %");
        diskUsageProgressBar.setStringPainted(true);
        diskUsageProgressBar.setMaximum(100);


        new Thread(() -> {
            while (true) {
                diskUsageProgressBar.setValue(new Random().nextInt(0, 100));
                beschikbaarheidProgressBar.setValue(new Random().nextInt(0, 100));
                procesbelastingProgressBar.setValue(new Random().nextInt(0, 100));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        frame.add(diskUsageProgressBar, "skip 2");


        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(6969);
                while (true) {
                    handleClient(serverSocket.accept());
                }

            } catch (Exception t) {
                t.printStackTrace();
                JOptionPane.showMessageDialog(frame, "kankerzooi");
            }
        });//.run();

        frame.setVisible(true);
    }

    public static void handleClient(Socket socket) {
        try {
            var input = new Scanner(new BufferedInputStream(socket.getInputStream()));
            while (true) {
                System.out.println("Binnen: " + input.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
