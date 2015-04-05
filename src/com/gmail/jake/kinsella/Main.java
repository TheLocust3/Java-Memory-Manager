package com.gmail.jake.kinsella;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.n9mtq4.demo.notifcationmac.Notification;

/**
 * Created by jakekinsella on 10/1/14.
 */
public class Main implements ActionListener {
    JTabbedPane tabbedPane;
    JCheckBox alertLowCheck, purgeLowCheck, antiAliasingCheck, antiAliasingChoiceCheck;
    JTextField alertLowInput, purgeLowInput, timeInput;
    JComboBox antiAliasingCombo;

    Canvas canvas;
    Notification n;

    boolean sentNotifaction = false, purgedOnLow = false;
    int ramOnAlert = 512, ramOnPurge = 256, timeout = 3000;

    Main () {
        // Set nice looking Nimbus theme
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            //  If Nimbus is not available, you can set the GUI to another look and feel.
        }
    	
        JButton purgeButton, saveButton;
        JFrame jfrm;
        JPanel settingsPanel, memoryPanel, quickButtonsPanel, options1, options2, options3, options4 = new JPanel();

        Constructions constructions = new Constructions();

        jfrm = new JFrame("Memory Manager");
        jfrm.setSize(270, 250);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrm.setLayout(new FlowLayout());

        // Init canvas, so it gets the Nimbus them
        canvas = new Canvas();

        // Create purge button
        purgeButton = new JButton("Purge");
        purgeButton.setActionCommand("Purge");
        purgeButton.addActionListener(this);

        quickButtonsPanel = new JPanel();
        quickButtonsPanel.add(purgeButton);

        // Create Memory tab
        Component[] tmp = {canvas, quickButtonsPanel};
        memoryPanel = constructions.createTab(tmp, 2, 1);
        tmp = null; // Make sure it gets de-allocated quick

        // Create line one of options
        alertLowCheck = constructions.createOptionsCheckBox(this, "AlertLowCheck", "Alert when unused ram is <", true);
        alertLowInput = constructions.createOptionsTextField(this, "AlertLowInput", "512", 3, true);

        // Create line two of options
        purgeLowCheck = constructions.createOptionsCheckBox(this, "PurgeLowCheck", "Purge when unused ram is <", false);
        purgeLowInput = constructions.createOptionsTextField(this, "PurgeLowInput", "256", 3, false);

        // Create line three of options
        antiAliasingCheck = constructions.createOptionsCheckBox(this, "AntiAliasingCheck", "Anti-aliasing", true);
        String[] tmpOptions = {"Bicubic", "Bilinear", "Nearest Neighbor"};
        antiAliasingCombo = constructions.createOptionsComboBox(this, "AntiAliasingCombo", tmpOptions, 2, true);

        // Create line four of options
        JLabel timeLabel = new JLabel("Update Statistics Every (Seconds): ");
        timeInput = constructions.createOptionsTextField(this, "TimeInput", "3", 3, true);
        options4.add(timeLabel);
        
        // Create save button
        saveButton = new JButton("Save Options");
        saveButton.setActionCommand("SaveButton");
        saveButton.addActionListener(this);
        
        // Create containers for options
        options1 = constructions.createOption(alertLowCheck, alertLowInput, false);
        options2 = constructions.createOption(purgeLowCheck, purgeLowInput, false);
        options3 = constructions.createOption(antiAliasingCheck, antiAliasingCombo, true);

        String os = System.getProperty("os.name").toLowerCase();
        // Create Settings tab
        if (!(os.indexOf("win") >= 0)) {
        	Component[] tmp2 = {options1, options2, options3, options4, saveButton};
        	settingsPanel = constructions.createTab(tmp2, 7, 1);
        } else {
        	Component[] tmp2 = {options1, options3, options4, saveButton};
        	settingsPanel = constructions.createTab(tmp2, 6, 1);
        }

        // Setup tabbedPane
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Memory", memoryPanel);
        tabbedPane.addTab("Settings", settingsPanel);

        jfrm.getContentPane().add(tabbedPane);
        
        jfrm.setVisible(true);

        startThreads();
    }

    private void startThreads() {
        // Thread that controls the memory and the settings tabs
        SwingWorker<Void, Void> memoryAndSettingsThread = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                while (true) {
                    Thread.sleep(timeout);
                    if (tabbedPane.getSelectedIndex() == 0) {
                        canvas.updateStats();
                        canvas.repaint(0, 0, canvas.getWidth(), (int) (canvas.getHeight() / 2.25)); //  Repaint memory graphic
                        canvas.repaint(canvas.getWidth() / 2, (int) (canvas.getHeight() / 2.25), canvas.getWidth(), canvas.getHeight()); //  Repaint memory graphic
                    }

                    //  Notifaction check and execution
                    if (alertLowCheck.isSelected()) {
                        if (canvas.memoryGraphic.memoryStats.getUnusedMemory() < ramOnAlert) {
                            if (!sentNotifaction) {
                                n = new Notification("Low Ram Alert", "You have less than " + ramOnAlert + "MB of unused ram left");
                                n.setSoundName("default");
                                n.display();
                                sentNotifaction = true;
                            }
                        } else {
                            sentNotifaction = false;
                        }
                    }

                    //  Purge check and execution
                    if (purgeLowCheck.isSelected()) {
                        if (canvas.memoryGraphic.memoryStats.getUnusedMemory() < ramOnPurge) {
                            if (!purgedOnLow) {
                                PasswordDialog passwordDialog = new PasswordDialog(true);
                            }
                        }
                    } else {
                        purgedOnLow = false;
                    }
                }
            }
        };

        memoryAndSettingsThread.execute();
    }

    public static void main (String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("AlertLowCheck")) {
            if (alertLowCheck.isSelected()) {
                alertLowInput.setEnabled(true);
            } else {
                alertLowInput.setEnabled(false);
            }
        } else if (actionEvent.getActionCommand().equals("PurgeLowCheck")) {
            if (purgeLowCheck.isSelected()) {
                PasswordDialog passwordDialog = new PasswordDialog(false);
                purgeLowInput.setEnabled(true);
            } else {
                purgeLowInput.setEnabled(false);
            }
        } else if (actionEvent.getActionCommand().equals("AntiAliasingCheck")) {
            if (antiAliasingCheck.isSelected()) {
                canvas.memoryGraphic.antiAliasing = true;
            } else {
                canvas.memoryGraphic.antiAliasing = false;
            }
        }  else if (actionEvent.getActionCommand().equals("AntiAliasingCombo")) {
            if (antiAliasingCombo.getSelectedItem().equals("Bilinear")) {
                canvas.memoryGraphic.antiAliasingType = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            } else if (antiAliasingCombo.getSelectedItem().equals("Bicubic")) {
                canvas.memoryGraphic.antiAliasingType = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
            } else {
                canvas.memoryGraphic.antiAliasingType = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            }
        } else if (actionEvent.getActionCommand().equals("Purge")) {
            PasswordDialog passwordDialog = new PasswordDialog(true);
        } else if (actionEvent.getActionCommand().equals("SaveButton")) {
            ramOnPurge = parseInt(purgeLowInput.getText(), 256);
            ramOnAlert = parseInt(alertLowInput.getText(), 512);
            timeout = (int) parseDouble(timeInput.getText(), 3) * 1000;
        }
    }

    private int parseInt (String toParse, int defaultValue) {
        try {
           return Integer.parseInt(toParse);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    private double parseDouble (String toParse, double defaultValue) {
        try {
           return Double.parseDouble(toParse);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
