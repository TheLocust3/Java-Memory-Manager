package com.gmail.jake.kinsella;

/**
 * Created by jakekinsella on 10/1/14.
 */
public class MemoryStats {
    RunCommand cmd;
    String memory = "";
    int usedMem, unusedMem, wiredMem, maxMem;
    String os = System.getProperty("os.name").toLowerCase();

    public MemoryStats () {
        cmd = new RunCommand();
        updateStats();
    }

    public void updateStats () {
        if (isWindows()) {
            //Run Windows specific commands
            String sMaxMem = cmd.run("systeminfo", "Physical Memory");
            sMaxMem = sMaxMem.replace(" ", "");
            sMaxMem = sMaxMem.substring(sMaxMem.indexOf(":") + 1, sMaxMem.indexOf("MB")).replace(",", "");
            maxMem = Integer.parseInt(sMaxMem);

            String sUnusedMem = cmd.run("systeminfo", "Available Physical Memory");
            sUnusedMem = sUnusedMem.replace(" ", "");
            sUnusedMem = sUnusedMem.substring(sUnusedMem.indexOf(":") + 1, sUnusedMem.indexOf("MB")).replace(",", "");
            unusedMem = Integer.parseInt(sUnusedMem);

            usedMem = maxMem - unusedMem;

            System.out.println(memory);

        } else {
            //Run Mac specific commands
            memory = cmd.run("top -l 1", "PhysMem");

            usedMem = Integer.parseInt(memory.substring(memory.indexOf(" ") + 1, memory.indexOf("M used")));

            wiredMem = Integer.parseInt(memory.substring(memory.indexOf("(") + 1, memory.indexOf("M wired)")));

            memory = cmd.run("/usr/sbin/system_profiler SPHardwareDataType", "Memory");
            maxMem = Integer.parseInt(memory.substring(memory.indexOf("Mem") + 8, memory.indexOf(" GB"))) * 1000;

            unusedMem = maxMem - usedMem;
        }
    }

    //Check if operating system is Windows
    public boolean isWindows () {
        return (os.indexOf("win") >= 0);
    }

    public int getUsedMemory () {
        return usedMem - wiredMem;
    }

    public int getUnusedMemory () {
        return unusedMem;
    }

    public int getWiredMemory () {
        return wiredMem;
    }

    public int getMaxMemory () {
        return maxMem;
    }
}
