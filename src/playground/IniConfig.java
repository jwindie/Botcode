package playground;

import java.nio.file.*;
import java.util.*;

public class IniConfig {

    private final Map<String, String> config = new HashMap<>();

    public IniConfig(String filePath) {
			List<String> lines;
			try {
				lines = Files.readAllLines(Paths.get(filePath));
			}
			catch (Exception e) {
				lines = defaultConfig();
			}
			for (String line : lines) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#") || line.startsWith(";")) continue;

				String[] parts = line.split("=", 2);
				if (parts.length == 2) {
					config.put(parts[0].trim().toUpperCase(), parts[1].trim());
				}
			}
    }

    public String getString(String key) {
    	return config.getOrDefault(key.toUpperCase(), "");
    }

	public int getInt(String key) {
		try {
			return Integer.parseInt(getString(key));
		} 
		catch (Exception e) {
			return 0;
		}
	}

	public float getFloat(String key) {
		try {
			return Float.parseFloat(getString(key));
		}
		catch (Exception e){
			return 0f;
		}
	}

	public boolean getBoolean(String key) {
		return getString(key).equalsIgnoreCase("true");
	}

	public List<String> defaultConfig () {
		return Arrays.asList(
			"WIDTH = 1080",
			"HEIGHT = 720",
			"OPENGL = TRUE",
			"PIXEL_DENSITY = 2",
			"RESIZE_WINDOW = FALSE",
			"FULLSCREEN = FALSE",
			"SMOOTH = 2"
    );
	}
}
