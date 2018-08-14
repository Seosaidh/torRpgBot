package torRpgBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.reporters.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SettingsManager {
	private Settings settings = new Settings();
	private final Logger LOGGER = LogManager.getLogger(SettingsManager.class.getName());
	
	private String javaToJson(Settings settingsObject) {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.serializeNulls();
		Gson gson = builder.create();
		return gson.toJson(settingsObject);
	}
	
	private Settings jsonToJava(String json) {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.serializeNulls();
		Gson gson = builder.create();
		Settings newSettings = gson.fromJson(json, Settings.class);
		return newSettings;
	}
	
	public void loadSettings(String filename) {
		try {
			FileReader fileReader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fileReader);
			String line;
			String contents = "";
			
			while ((line = reader.readLine()) != null)
			{
				contents = contents + line;
			}
			
			reader.close();
			
			LOGGER.debug("File contents: {}", contents);
			settings = jsonToJava(contents);
			LOGGER.debug("Retrieved Settings object: {}", settings.toString());
			
		} catch (FileNotFoundException e) {
			LOGGER.error("Unable to find file {}, creating a default file.", filename);
			try {
				Files.writeFile(javaToJson(new Settings()), Paths.get(filename).toFile());
			} catch (IOException e1) {
				LOGGER.error("Unable to create default file {}. Exiting.", filename);
				System.exit(1);
			}
		}
		catch (IOException e) {
			LOGGER.error("Error in reading file {}. Using default settings", filename);
			return;
		}
	}
	
	public void writeSettings(String filename) {
		LOGGER.debug("Writing settings {} to file {}", settings.toString(), filename);
		try {
		    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		    writer.write(javaToJson(settings));
		     
		    writer.close();
		} catch (IOException e) {
			LOGGER.error("Unable to write settings to file {}.", filename);
			e.printStackTrace();
		}
	}
	
	public Settings getSettings() {
		return settings;
	}
}
