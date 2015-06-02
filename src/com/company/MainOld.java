package com.company;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.InetAddress;

public class MainOld {

    public static void main(String[] args) {
        BufferedImage image;
        JFrame frame = new JFrame("Current IP");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        String IP = "";
        try {
            IP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            IP = "Error finding IP";
        }
        //Создается панель,
        //которая будет содржать информацию о IP адресе
        JPanel panel = new JPanel();
        //добавление границы к панели
        panel.setBorder(BorderFactory.createTitledBorder(" Current IP Address"));
        panel.add(new JLabel("          " + IP + "          "));
        //Добавление панели к фрейму
        frame.getContentPane().add(panel);

        /*
        try {
            image = ImageIO.read(new File("assets/example.jpg"));
            JLabel picLabel = new JLabel(new ImageIcon(image));
            panel.add(picLabel);
        } catch (IOException ex) {
            // handle exception...
        }
        */

        ImageLabel label = new ImageLabel();
        label.setSize(200,200);

        panel.add(label);

        //метод рack(); сообщает Swing о том,
        //что нужно придать компонентам необходимые размеры для
        //правильного помещения их в форму.
        //Другой способ - вызвать setSize(int width, int height).
        frame.pack();
        //Для того, чтобы увидеть окно на экране
        //вы должны вызвать метод setVisible(true)
        frame.setVisible(true);
    }
}
