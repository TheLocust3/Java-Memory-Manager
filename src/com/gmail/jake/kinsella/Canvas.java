package com.gmail.jake.kinsella;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jakekinsella on 10/1/14.
 */
public class Canvas extends JPanel {
    MemoryGraphic memoryGraphic = new MemoryGraphic();

    public Canvas() {
        //Set nice looking Nimbus theme
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(250, 135);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        memoryGraphic.drawGraphic(g);
    }

    public void updateStats () {
        memoryGraphic.updateStats();
    }
}
