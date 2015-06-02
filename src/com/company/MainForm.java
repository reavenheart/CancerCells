package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Denis on 02.04.2015.
 */
public class MainForm {
    private JPanel panel;

    public MainForm() {
        JFrame frame = new JFrame();

        //panel.setBackground(Color.black);

        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        file.add(eMenuItem);

        menuBar.add(file);

        frame.setJMenuBar(menuBar);

        frame.add(panel);

        frame.setSize(300,300);
        //frame.pack();
        frame.setVisible(true);
    }
}
