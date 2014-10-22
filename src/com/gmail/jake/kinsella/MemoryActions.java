package com.gmail.jake.kinsella;

/**
 * Created by jakekinsella on 10/2/14.
 */
public class MemoryActions {
    RunCommand cmd = new RunCommand();
    private static char[] password;
    public static int exitCode = 1;
    String output = "";

    //Run purge with password
    public int purge (char[] newPassword) {
        password = newPassword;
        output = cmd.sendShellCommand("echo " + String.copyValueOf(password) + " | sudo -S nice -n -20 purge");
        exitCode = Integer.parseInt(output);
        System.out.println(exitCode);
        return exitCode;
    }

    //Run purge with last used password
    public int purgeNoPass () {
        output = cmd.sendShellCommand("echo " + String.copyValueOf(password) + " | sudo -S nice -n -20 purge");
        exitCode = Integer.parseInt(output);
        System.out.println(exitCode);
        return exitCode;
    }

    //Check the password before running purge
    public int checkPassword (char[] newPassword) {
        output = cmd.sendShellCommand("echo " + String.copyValueOf(newPassword) + " | sudo -S cd");
        exitCode = Integer.parseInt(output);
        if (exitCode == 0) {
            password = newPassword;
        }
        return exitCode;
    }
}
