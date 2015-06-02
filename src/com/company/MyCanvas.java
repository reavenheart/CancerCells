package com.company;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Denis on 02.04.2015.
 */
public class MyCanvas extends Canvas {

    public void paint(Graphics g) {

        Toolkit t=Toolkit.getDefaultToolkit();
        Image i=t.getImage("assets/example.jpg");
        g.drawImage(i, 0,0,this);

    }
    public static void main(String[] args) {
        MyCanvas m=new MyCanvas();
        JFrame f=new JFrame();
        f.add(m);
        //f.setSize(400,400);
        f.pack();
        f.setVisible(true);
    }

}
