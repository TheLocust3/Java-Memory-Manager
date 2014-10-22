package com.gmail.jake.kinsella;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by jakekinsella on 10/8/14.
 */
public class Constructions {
    String os = System.getProperty("os.name").toLowerCase();

    //Creates a one line option for the settings tab with a text field
    public JPanel createOption (JCheckBox checkBox, JTextField textField, boolean windowsComp) {
        JPanel tmp = new JPanel();

        if (!windowsComp && !isWindows() || windowsComp) {
            tmp.add(checkBox);
            tmp.add(textField);
        } else {
            tmp.setVisible(false);
        }

        return tmp;
    }

    //Creates a one line option for the settings tab with a combo box
    public JPanel createOption (JCheckBox checkBox, JComboBox comboBox ,boolean windowsComp) {
        JPanel tmp = new JPanel();

        if (!windowsComp && !isWindows() || windowsComp) {
            tmp.add(checkBox);
            tmp.add(comboBox);
        }

        return tmp;
    }

    //Creates a one line option for the settings tab with ONLY a checkbox
    public JPanel createOption (JCheckBox checkBox, boolean windowsComp) {
        JPanel tmp = new JPanel();

        if (!windowsComp && !isWindows() || windowsComp) {
            tmp.add(checkBox);
        }

        return tmp;
    }

    public JCheckBox createOptionsCheckBox (ActionListener a, String name, String text, boolean selected) {
        JCheckBox tmp = new JCheckBox(text);
        tmp.setSelected(selected);
        tmp.setActionCommand(name);
        tmp.addActionListener(a);

        return tmp;
    }

    public JTextField createOptionsTextField (ActionListener a, String name, String text, int length, boolean enabled) {
        JTextField tmp = new JTextField(length);
        tmp.setEnabled(enabled);
        tmp.setText(text);
        tmp.setActionCommand(name);
        tmp.addActionListener(a);

        return tmp;
    }

    public JComboBox createOptionsComboBox (ActionListener a, String name, String[] items, int selected, boolean enabled) {
        JComboBox tmp = new JComboBox(items);
        tmp.setEnabled(enabled);
        tmp.setSelectedIndex(selected);
        tmp.setActionCommand(name);
        tmp.addActionListener(a);

        return tmp;
    }

    public JPanel createTab (Component[] components, int rows, int columns) {
        JPanel tmp = new JPanel();
        tmp.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < components.length; i++) {
            tmp.add(components[i]);
        }

        return tmp;
    }

    private boolean isWindows () {
        return (os.indexOf("win") >= 0);
    }
}
