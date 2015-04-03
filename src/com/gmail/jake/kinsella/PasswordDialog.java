package com.gmail.jake.kinsella;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by jakekinsella on 10/2/14.
 */
public class PasswordDialog extends JDialog {
    MemoryActions memoryActions = new MemoryActions();
    char[] lastPassword;
    int exitCode;

    public PasswordDialog(final boolean runPurge) {
        final JFrame jfrm = new JFrame("Enter Password: ");
        jfrm.setSize(250, 115);
        jfrm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();

        final JLabel passwordText = new JLabel("Enter password");

        final JPasswordField passwordField = new JPasswordField(20);

        passwordField.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (runPurge) {
                if (memoryActions.exitCode != 0) {
                    lastPassword = passwordField.getPassword();
                    int exitCode = memoryActions.purge(passwordField.getPassword());
                    if (exitCode == 0) {
                        passwordText.setText("Correct: ");
                        jfrm.dispatchEvent(new WindowEvent(jfrm, WindowEvent.WINDOW_CLOSING)); // Close password window
                    } else {
                        passwordText.setText("Incorrect: ");
                    }
                } else {
                    exitCode = memoryActions.purge(passwordField.getPassword());
                }
            } else {
                exitCode = memoryActions.checkPassword(passwordField.getPassword());
                if (exitCode == 0) {
                    passwordText.setText("Correct: ");
                    jfrm.dispatchEvent(new WindowEvent(jfrm, WindowEvent.WINDOW_CLOSING)); // Close password window
                } else {
                    passwordText.setText("Incorrect: ");
                }
            }
        }
        });

        panel.add(passwordText);
        panel.add(passwordField);

        jfrm.getContentPane().add(panel, BorderLayout.CENTER);
        jfrm.pack();
        jfrm.setResizable(false);

        // Check if you should actually run purge
        if (runPurge) {
            if (MemoryActions.exitCode != 0) {
                System.out.println("True");
                jfrm.setVisible(true);
            } else {
                System.out.println("False");
                memoryActions.purgeNoPass();
            }
        }
    }
}
