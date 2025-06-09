package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class FileHandler {

	public static String getAppDataPath(String appName) {
		String base;

		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			base = System.getenv("APPDATA");  // e.g. C:\Users\You\AppData\Roaming
		} 
		else if (os.contains("mac")) {
			base = System.getProperty("user.home") + "/Library/Application Support";
		} 
		else {
			base = System.getProperty("user.home") + "/.config";
		}

		return base + "/" + appName + "/";
	}

	public static void writeFile(String fileName, String contents) {

    String path = FileHandler.getAppDataPath("Botcode");
		File directory = new File (path);
		directory.mkdirs();

		path += fileName;
		
		// System.out.println("Writing to file: " + path);
    
		//opens a new file
		try {
			//write base directory first
      File file = new File(path); //crea
			file.createNewFile();
			//write to the file
			FileWriter writer = new FileWriter(path);
      writer.write(contents);
      writer.close();
    } 
		catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

		// //try open it on the computer
		// try {
		// 	File file = new File(path); // Replace with your file path
		// 	if (!Desktop.isDesktopSupported()) {
		// 		System.out.println("Desktop is not supported.");
		// 		return;
		// 	}
		// 	Desktop desktop = Desktop.getDesktop();
		// 	if (file.exists()) {
		// 		desktop.open(file);
		// 	} 
		// 	else System.out.println("File does not exist: " + file.getAbsolutePath());
		// } 
		// catch (IOException e) {
		// 	System.out.println("An error occurred: " + e.getMessage());
		// }
  }
}
