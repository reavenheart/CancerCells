package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Denis on 05.04.2015.
 */
public class SoftBlend {

    int width;
    int height;

    public BufferedImage applySoftBlend(BufferedImage image, BufferedImage image2) {
        double ar, br, ag, bg, ab, bb;

        width = image.getWidth();
        height = image.getHeight();

        BufferedImage image3 = new BufferedImage(width, height, image.getType());

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c2 = new Color(image2.getRGB(j, i));
                Color c1 = new Color(image.getRGB(j, i));
                float[] components1 = new float[3];
                float[] components2 = new float[3];
                c1.getColorComponents(components1);
                c2.getColorComponents(components2);
                ar = components1[0];
                br = components2[0];

                ag = components1[1];
                bg = components2[1];
                ab = components1[2];
                bb = components2[2];

                double nr, ng, nb;

                if (br <= 0.5)
                    nr = 2 * ar * br + ar * ar * (1 - 2 * br);
                else
                    nr = 2 * ar * (1 - br) + Math.sqrt(ar) * (2 * br - 1);

                if (bg <= 0.5)
                    ng = 2 * ag * bg + ag * ag * (1 - 2 * bg);
                else
                    ng = 2 * ag * (1 - bg) + Math.sqrt(ag) * (2 * bg - 1);

                if (bb <= 0.5)
                    nb = 2 * ab * bb + ab * ab * (1 - 2 * bb);
                else
                    nb = 2 * ab * (1 - bb) + Math.sqrt(ab) * (2 * bb - 1);

                if (nr < 0)
                    nr = 0;
                else if (nr > 1)
                    nr = 1;

                if (ng < 0)
                    ng = 0;
                else if (ng > 1)
                    ng = 1;

                if (nb < 0)
                    nb = 0;
                else if (nb > 1)
                    nb = 1;

                c1 = new Color((float) nr, (float) ng, (float) nb);

                image3.setRGB(j, i, c1.getRGB());
            }
        }

        return image3;
    }
}
