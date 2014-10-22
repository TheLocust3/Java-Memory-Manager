package com.gmail.jake.kinsella;

import java.io.*;

public class RunCommand {

    public String sendShellCommand(String command) {
        String output = "";
        saveFile("./t.command", command + "; echo Exit $?;"); //echo Exit $?; gets the exit code of the command
        run("chmod +x ./t.command", "");
        output = run("./t.command", "");
        deleteFile("./t.command");

        return output;
    }

    //Removes created shell script
    private boolean deleteFile(String path) {
        File f = new File(path);
        return f.delete();
    }

    //Created shell script
    private void saveFile(String path, String text) {
        String[] tokens_String = text.split("\n");
        PrintStream ps;
        try {
            ps = new PrintStream(path);
            for (String i : tokens_String) {
                ps.println(i);
            }
            ps.close();
        } catch (IOException e) {
        }
    }

    //Runs a command and takes an argument to search the results
    public String run (String cmd, String grep) {
        String s;
        String full = "";

        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((s = stdInput.readLine()) != null) {
                if (grep != "") {
                    full = runGrep(s, grep);
                }

                if (s.contains("Exit")) {
                    System.out.println("Here");
                    System.out.println(s);
                    full = s.substring(5);
                }

                if (full != "") {
                    return full;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //Searches output for String grep
    private String runGrep (String output, String grep) {
        String tmp = output;

        if (tmp.contains(grep)) {
            return tmp;
        }

        return "";
    }
}