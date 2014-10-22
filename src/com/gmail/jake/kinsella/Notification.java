package com.gmail.jake.kinsella;

import java.io.*;

/**
 * Created by Will on 10/1/14.
 */
public class Notification {
	
	private String title;
	private String text;
	private String subtitle;
	private String soundName;
	
	public Notification(String title, String text) {
		
		this.title = title;
		this.text = text;
		
	}
	
	public Notification() {
//		dummy
	}
	
	public boolean display() {
		
		if (sc(text) && sc(title)) {
			
			if (sc(subtitle) && sc(soundName)) {
				return displayTitleTextSubtitleSoundname(title, text, subtitle, soundName);
			}else if (sc(subtitle)) {
				return displayTitleTextSubtitle(title, text, subtitle);
			}else if (sc(soundName)) {
				return displayTitleTextSoundname(title, text, soundName);
			}else {
				return displayTitleText(title, text);
			}
			
		}else {
			return false;
		}
		
	}
	
	private boolean displayTitleText(String title, String text) {
		sendShellCommand("echo \"display notification \\\"" + text + "\\\" with title \\\"" + title + "\\\"\" | osascript");
		return true;
	}
	
	private boolean displayTitleTextSubtitle(String title, String text, String subtitle) {
		sendShellCommand("echo \"display notification \\\"" + text + "\\\" with title \\\"" + title + "\\\" subtitle \\\"" + subtitle + "\\\"\" | osascript");
		return true;
	}
	
	private boolean displayTitleTextSoundname(String title, String text, String soundName) {
		sendShellCommand("echo \"display notification \\\"" + text + "\\\" with title \\\"" + title + "\\\" sound name \\\"" + soundName + "\\\"\" | osascript");
		return true;
	}
	
	private boolean displayTitleTextSubtitleSoundname(String title, String text, String subtitle, String soundName) {
		sendShellCommand("echo \"display notification \\\"" + text + "\\\" with title \\\"" + title + "\\\" subtitle \\\"" + subtitle + "\\\" sound name \\\"" + soundName + "\\\"\" | osascript");
		return true;
	}

	public String sendShellCommand(String command) {
		String output = "";
		saveFile("./t.command", command);
		run("chmod +x ./t.command");
		run("./t.command");
		deleteFile("./t.command");
		return output;
	}
	
	private boolean deleteFile(String path) {
		File f = new File(path);
		return f.delete();
	}
	
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
	
	private void run(String command) {
		Runtime cmd_Runtime = Runtime.getRuntime();
		try {
			Process cmdReturn_Process = cmd_Runtime.exec(command);
			InputStream inputStream = cmdReturn_Process.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
			}
		} catch (IOException e1) {
		}
	}
	
	private boolean sc(String text) {
		if (text == null) return false;
		return !(text.trim().equalsIgnoreCase(""));
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getSubtitle() {
		return subtitle;
	}
	
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public String getSoundName() {
		return soundName;
	}
	
	public void setSoundName(String soundName) {
		this.soundName = soundName;
	}
	
}
