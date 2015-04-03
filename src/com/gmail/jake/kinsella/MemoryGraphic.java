package com.gmail.jake.kinsella;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by jakekinsella on 10/1/14.
 */
public class MemoryGraphic {
    public MemoryStats memoryStats = new MemoryStats();

    public boolean antiAliasing = true;

    public Object antiAliasingType = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;

    public void drawGraphic (Graphics graphics) {
        //Variables for rounding
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        Graphics2D g = (Graphics2D) graphics.create();

        if (antiAliasing) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, antiAliasingType);
        } else {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        //Windows does not have wired memory
        if (!memoryStats.isWindows()) {
            //Draw wired memory graphic
            drawRect(g, 5, 2, 246, 50, Color.RED, 2);
        }

        int unusedRamX = getSize(246, memoryStats.getMaxMemory(), memoryStats.getUnusedMemory());

        if (!memoryStats.isWindows()) {
            //Draw used memory graphic
            drawRect(g, unusedRamX, 2, getSize(246, memoryStats.getMaxMemory(), memoryStats.getUsedMemory()), 50, Color.YELLOW, 1);
        } else {
            //Instead of wired memory on the bottom there is used memory
            g.setColor(Color.YELLOW);
            g.fillRoundRect(4, 2, 246, 50, 10, 10);
        }

        //Draw unused memory graphic
        drawRect(g, 2, 2, unusedRamX, 50, Color.GREEN, 0);

        //Draw black outline of graphic
        g.setColor(Color.BLACK);
        g.drawRoundRect(2, 2, 246, 50, 10, 10);

        //Draw unused memory circle
        g.setColor(Color.GREEN);
        g.fillRoundRect(2, 81, 10, 10, 10, 10);
        //Draw used memory circle
        g.setColor(Color.YELLOW);
        g.fillRoundRect(2, 101, 10, 10, 10, 10);
        //Draw wired memory circle
        g.setColor(Color.RED);
        g.fillRoundRect(2, 121, 10, 10, 10, 10);

        //Draw labels
        g.setColor(Color.BLACK);
        g.drawString("Max Ram: " + memoryStats.maxMem / 1000 + " GB", 80, 70);
        String output = formatter.format((float) (memoryStats.getUnusedMemory() / 1000.0));
        g.drawString("Unused Memory: " + output + " GB", 60, 90);
        output = formatter.format((float) (memoryStats.getUsedMemory() / 1000.0));
        g.drawString("Used Memory: " + output + " GB", 60, 110);
        output = formatter.format((float) (memoryStats.getWiredMemory() / 1000.0));
        g.drawString("Wired Memory: " + output + " GB", 60, 130);

        g.setColor(Color.BLACK);
        //Draw unused memory outline
        g.drawRoundRect(2, 81, 10, 10, 10, 10);
        //Draw used memory outline
        g.drawRoundRect(2, 101, 10, 10, 10, 10);
        //Draw wired memory outline
        g.drawRoundRect(2, 121, 10, 10, 10, 10);
    }

    public void updateStats () {
        memoryStats.updateStats();
    }

    private void drawRect (Graphics2D g, int startX, int startY, int endX, int height, Color color, int position) {
        if (position == 0) {
            g.setColor(color);
            g.fillRoundRect(startX, startY, endX - 10, height, 10, 10);
            g.fillRect(startX + 1, startY, endX, height);
        } else if (position == 1) {
            g.setColor(color);
            g.fillRect(startX, startY, endX, height);
        } else {
            g.setColor(color);
            g.fillRoundRect(startX, startY, endX, height, 10, 10);
        }
    }

    private int getSize (int width, int maxMem, int stat) {
        return (int) (((float) stat / (float) maxMem) * width);
    }
}
