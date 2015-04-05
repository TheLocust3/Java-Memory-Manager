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
            // Run Windows specific commands
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
        } else if (isMac()) {
            // Run Mac specific commands
            memory = cmd.run("top -l 1", "PhysMem");

            usedMem = Integer.parseInt(memory.substring(memory.indexOf(" ") + 1, memory.indexOf("M used")));

            wiredMem = Integer.parseInt(memory.substring(memory.indexOf("(") + 1, memory.indexOf("M wired)")));

            memory = cmd.run("sysctl hw.memsize", " ");
            maxMem = (int) (Long.parseLong(memory.substring(12, memory.length())) / 1048576);
            
            unusedMem = maxMem - usedMem;
        } else if (isLinux()) {
        	// Run linux specific commands
        	memory = cmd.run("top -bn1", "KiB Mem");
        	
        	usedMem = Integer.parseInt(memory.substring(28, 34)) / 1024;
        	
        	maxMem = Integer.parseInt(memory.substring(11, 18)) / 1024;
        	        	
        	unusedMem = Integer.parseInt(memory.substring(43, 49)) / 1024;
        }
    }

    // Check if operating system is Windows
    public boolean isWindows () {
        return (os.indexOf("win") >= 0);
    }
    
    public boolean isMac () {
    	return (os.indexOf("mac") >= 0);
    }
    
    // Check if opertating system is Linux
    public boolean isLinux () {
    	return (!isMac() && (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0));
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
