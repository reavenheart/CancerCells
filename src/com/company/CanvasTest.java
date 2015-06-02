package com.company;

import javax.swing.*;

/**
 * Created by Denis on 02.04.2015.
 */
public class CanvasTest {
    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //MainForm mainForm = new MainForm();
        //GrayScale gs = new GrayScale();

        //Gauss gauss = new Gauss();

        //Median median = new Median();

        Sobel sobel = new Sobel();
    }
}
