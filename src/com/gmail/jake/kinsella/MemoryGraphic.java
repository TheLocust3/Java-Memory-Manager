package com.gmail.jake.kinsella;

import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by jakekinsella on 10/1/14.
 */
public class MemoryGraphic {
	private int GRAPHICS_WIDTH = 244;
	
    public MemoryStats memoryStats = new MemoryStats();

    public boolean antiAliasing = true;
    public Object antiAliasingType = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;

    public void drawGraphic (Graphics graphics) {
        // Variables for rounding
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

        // Windows does not have wired memory
        if (!memoryStats.isWindows()) {
            // Draw wired memory graphic
            drawRect(g, 3, 2, GRAPHICS_WIDTH, 50, Color.RED, true);
        }

        int unusedRamX = getSize(GRAPHICS_WIDTH, memoryStats.getMaxMemory(), memoryStats.getUnusedMemory());

        if (!memoryStats.isWindows()) {
            // Draw used memory graphic
            drawRect(g, 3, 2, getSize(GRAPHICS_WIDTH, memoryStats.getMaxMemory(), memoryStats.getUsedMemory()) + unusedRamX, 50, Color.YELLOW, false);
        } else {
            // Instead of wired memory on the bottom there is used memory
            g.setColor(Color.YELLOW);
            g.fillRoundRect(3, 2, GRAPHICS_WIDTH, 50, 10, 10);
        }

        // Draw unused memory graphic
        drawRect(g, 3, 2, unusedRamX, 50, Color.GREEN, false);

        // Draw black outline of graphic
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(2, 2, GRAPHICS_WIDTH, 50, 10, 10);

        g.setStroke(new BasicStroke(1));
        
        g.drawString("You have " + memoryStats.maxMem / 1000 + " GB of RAM", 60, 70);
        
        // Draw unused memory circle
        g.setColor(Color.GREEN);
        g.fillRoundRect(2, 81, 10, 10, 10, 10);
        g.setColor(Color.BLACK);
        String output = formatter.format((float) (memoryStats.getUnusedMemory() / 1000.0));
        g.drawString("Unused Memory: " + output + " GB", 60, 90);
        // Draw unused memory outline
        g.drawRoundRect(2, 81, 10, 10, 10, 10);
        
        // Draw used memory circle
        g.setColor(Color.YELLOW);
        g.fillRoundRect(2, 101, 10, 10, 10, 10);
        g.setColor(Color.BLACK);
        output = formatter.format((float) (memoryStats.getUsedMemory() / 1000.0));
        g.drawString("Used Memory: " + output + " GB", 60, 110);
        // Draw used memory outline
        g.drawRoundRect(2, 101, 10, 10, 10, 10);
        
        if (!memoryStats.isWindows()) { //  There is no wired memory on Windows
	        // Draw wired memory circle
	        g.setColor(Color.RED);
	        g.fillRoundRect(2, 121, 10, 10, 10, 10);
	        
	        g.setColor(Color.BLACK);
	        output = formatter.format((float) (memoryStats.getWiredMemory() / 1000.0));
	        g.drawString("Wired Memory: " + output + " GB", 60, 130);
	        
	        // Draw wired memory outline
	        g.drawRoundRect(2, 121, 10, 10, 10, 10);
        }
    }

    public void updateStats () {
        memoryStats.updateStats();
    }

    private void drawRect (Graphics2D g, int startX, int startY, int endX, int height, Color color, boolean rightSide) {
        if (rightSide) {
            g.setColor(color);
            g.fillRoundRect(startX, startY, endX, height, 10, 10);
        } else {
        	g.setColor(color);
            g.fillRoundRect(startX, startY, endX, height, 10, 10);
            g.fillRect(startX + 1, startY, endX, height);
        }
    }

    private int getSize (int width, int maxMem, int stat) {
        return (int) ((float) ((stat * width) / maxMem));
    }
}
