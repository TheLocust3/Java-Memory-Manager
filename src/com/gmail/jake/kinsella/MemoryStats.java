package com.gmail.jake.kinsella;

/**
 * Created by jakekinsella on 10/1/14.
 */
public class MemoryStats {
    RunCommand cmd;
    String memory = "";
    int usedMem, unusedMem, inactiveMem, wiredMem, maxMem;
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
        	
        	memory = cmd.run("vm_stat", "Pages active");
        	usedMem = (int) ((Long.parseLong(memory.substring(memory.lastIndexOf(" ") + 1, memory.indexOf("."))) * 4096) / 1048676);
        	
        	memory = cmd.run("vm_stat", "Pages wired down");
        	wiredMem = (int) ((Long.parseLong(memory.substring(memory.lastIndexOf(" ") + 1, memory.indexOf("."))) * 4096) / 1048676);
        	
        	memory = cmd.run("vm_stat", "Pages inactive");
        	inactiveMem = (int) ((Long.parseLong(memory.substring(memory.lastIndexOf(" ") + 1, memory.indexOf("."))) * 4096) / 1048676);

        	
        	memory = cmd.run("sysctl hw.memsize", " ");
            maxMem = (int) (Long.parseLong(memory.substring(12, memory.length())) / 1048576);
        	
            unusedMem = maxMem - usedMem - inactiveMem - wiredMem;
        } else if (isLinux()) {
        	// Run linux specific commands
        	memory = cmd.run("vmstat -a", 3);
        	usedMem = Integer.parseInt(memory.substring(nthOccurrence(memory, ' ', 7) + 1, nthOccurrence(memory, ' ', 8))) / 1024;
        	
        	inactiveMem = Integer.parseInt(memory.substring(nthOccurrence(memory, ' ', 6) + 1, nthOccurrence(memory, ' ', 7))) / 1024;
        	
        	unusedMem = Integer.parseInt(memory.substring(nthOccurrence(memory, ' ', 5) + 1, nthOccurrence(memory, ' ', 6))) / 1024;
        	
        	memory = cmd.run("top -bn1", "KiB Mem");
        	maxMem = Integer.parseInt(memory.substring(nthOccurrence(memory, ' ', 3) + 1, nthOccurrence(memory, ' ', 4))) / 1024;
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
        return usedMem;
    }

    public int getInactiveMemory () {
    	return inactiveMem;
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
    
    private int nthOccurrence (String str, char c, int n) {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1)
            pos = str.indexOf(c, pos+1);
        return pos;
    }
}
