package org.olavrik.charmer.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public final class HeaderHistogram extends JPanel {
    private HashMap<String, int[]> dataBins;
    private final int maxVal = 256;
    private String current;

    public void setCurrent(final String futureCur) {
        this.current = futureCur;
    }


    public HeaderHistogram() {
        dataBins = new HashMap<>();
        final int width = 40;
        final int height = 80;
        setPreferredSize(new Dimension(width, height));
    }

    private int[] normilezeData(final int[] data) {
        int max = 0;
        for (int i = 0; i < data.length; i++) {
            max = Math.max(max, data[i]);
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = (int) (((double) data[i] / (double) max) * maxVal);
        }

        return data;
    }

    public void set(final HashMap<String, int[]> data) {
        for (String key : data.keySet()) {
            dataBins.put(key, normilezeData(data.get(key)));
        }
    }

    @Override
    public void paint(final Graphics g) {
        if (current != null) {
            int[] data = this.dataBins.get(current);
            int size = data.length;

            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(Color.white);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setStroke(new BasicStroke(2));

            double xInterval = (double) getWidth() / ((double) size + 1);

            g2.setColor(Color.black);

            final int rChannel = 76;
            final int gChannel = 198;
            final int bChannel = 255;

            g2.setColor(new Color(rChannel, gChannel, bChannel));


            for (int i = 0; i < size; i++) {
                int value = (int) (((double) data[i] / maxVal) * getHeight());

                g2.fillRect((int) (i * xInterval), getHeight() - value, Math.max((int) xInterval + 1, 1), value);
            }
        } else {
            super.paint(g);
        }
    }
}
