package com.gmail.jake.kinsella;

import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by jakekinsella on 10/1/14.
 */
public class MemoryGraphic {
	private int GRAPHICS_WIDTH = 250;
	
	private final Color GREEN = new Color(38, 194, 129);
	private final Color BLUE = new Color(25, 181, 254);
	private final Color YELLOW = new Color(244, 208, 63);
	private final Color RED = new Color(242, 38, 19);
	
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
        if (!memoryStats.isWindows() && !memoryStats.isLinux()) {
            // Draw wired memory graphic
            drawRect(g, 3, 2, GRAPHICS_WIDTH, 50, RED, true);
        }

        int unusedRamX = getSize(GRAPHICS_WIDTH, memoryStats.getMaxMemory(), memoryStats.getUnusedMemory());
        int inactiveRamX = getSize(GRAPHICS_WIDTH, memoryStats.getMaxMemory(), memoryStats.getInactiveMemory()) + unusedRamX;

        if (!memoryStats.isWindows() && !memoryStats.isLinux()) {
            // Draw used memory graphic
            drawRect(g, 3, 2, getSize(GRAPHICS_WIDTH, memoryStats.getMaxMemory(), memoryStats.getUsedMemory()) + inactiveRamX, 50, YELLOW, false);
            
        	// Draw inactive memory graphic
            drawRect(g, 3, 2, inactiveRamX, 50, BLUE, false);
        } else {
            // Instead of wired memory on the bottom there is used memory
        	g.setColor(YELLOW);
            g.fillRoundRect(3, 2, GRAPHICS_WIDTH, 50, 10, 10);
        }
        
        // Draw unused memory graphic
        drawRect(g, 3, 2, unusedRamX, 50, GREEN, false);

        // Draw black outline of graphic
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(2, 2, GRAPHICS_WIDTH, 50, 10, 10);

        g.setStroke(new BasicStroke(1));
        
        String output = formatter.format(memoryStats.maxMem / 1024.0);
        g.drawString("You have " + output + " GB of RAM", 60, 70);
        
        // Draw unused memory circle
        g.setColor(GREEN);
        g.fillRoundRect(2, 81, 10, 10, 10, 10);
        g.setColor(Color.BLACK);
        output = formatter.format((float) (memoryStats.getUnusedMemory() / 1024.0));
        g.drawString("Unused Memory: " + output + " GB", 60, 90);
        // Draw unused memory outline
        g.drawRoundRect(2, 81, 10, 10, 10, 10);
        
        if (!memoryStats.isWindows() && !memoryStats.isLinux()) {
	        // Draw inactive memory circle
	        g.setColor(BLUE);
	        g.fillRoundRect(2, 101, 10, 10, 10, 10);
	        g.setColor(Color.BLACK);
	        output = formatter.format((float) (memoryStats.getInactiveMemory() / 1024.0));
	        g.drawString("Inactive Memory: " + output + " GB", 60, 110);
	        // Draw inactive memory outline
	        g.drawRoundRect(2, 101, 10, 10, 10, 10);
        }
        
        // Draw used memory circle
        g.setColor(YELLOW);
        g.fillRoundRect(2, 121, 10, 10, 10, 10);
        g.setColor(Color.BLACK);
        output = formatter.format((float) (memoryStats.getUsedMemory() / 1024.0));
        g.drawString("Used Memory: " + output + " GB", 60, 130);
        // Draw used memory outline
        g.drawRoundRect(2, 121, 10, 10, 10, 10);
        
        if (!memoryStats.isWindows() && !memoryStats.isLinux()) { //  There is no wired memory on Windows
	        // Draw wired memory circle
	        g.setColor(RED);
	        g.fillRoundRect(2, 141, 10, 10, 10, 10);
	        
	        g.setColor(Color.BLACK);
	        output = formatter.format((float) (memoryStats.getWiredMemory() / 1024.0));
	        g.drawString("Wired Memory: " + output + " GB", 60, 150);
	        
	        // Draw wired memory outline
	        g.drawRoundRect(2, 141, 10, 10, 10, 10);
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
