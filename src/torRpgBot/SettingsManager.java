/**
 * <h1>SettingsManager</h1>
 * This is the class that manages the settings. In particular, it will read a given file
 * to load the settings and has the ability to write to that file as well.
 */

package torRpgBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SettingsManager {
	private Settings settings = new Settings();
	private final Logger LOGGER = LogManager.getLogger(SettingsManager.class.getName());
	private String settingsFile;
	
	public SettingsManager(String filename) {
		this.settingsFile = filename;
	}
	
	
	private String javaToJson(Settings settingsObject) {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.serializeNulls();
		Gson gson = builder.create();
		String result = gson.toJson(settingsObject);
		LOGGER.debug("Converted object {} to JSON {}.", settingsObject.toString(), result);
		return result;
	}
	
	
	private Settings jsonToJava(String json) {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.serializeNulls();
		Gson gson = builder.create();
		Settings newSettings = gson.fromJson(json, Settings.class);
		LOGGER.debug("Converted JSON {} to object {}.", json, newSettings.toString());
		return newSettings;
	}
	
	
	/**
	 * This function will load the settings file from the given filename.
	 * It will handle any exceptions raised by file operations. It returns
	 * true if a settings file was successfully loaded and false otherwise.
	 * @param filename The name of the file to laod the settings from.
	 * @return boolean true if successfully loaded settings, false otherwise.
	 */
	public boolean loadSettings() {
		try (BufferedReader reader = new BufferedReader(new FileReader(settingsFile))){
			String line;
			String contents = "";
			
			LOGGER.info("Reading configuration from file {}.", settingsFile);
			
			while ((line = reader.readLine()) != null)
			{
				contents = contents + line;
			}
			
			reader.close();
			
			LOGGER.debug("File contents: {}", contents);
			settings = jsonToJava(contents);
			
			boolean hasDefault = false;
			
			for (CommandFlag cf : settings.commandFlags)
			{
				if (cf.server.equalsIgnoreCase("default"))
				{
					hasDefault = true;
					break;
				}
			}
			
			if (!hasDefault)
			{
				LOGGER.debug("Retrieved Settings doesn't have a default command flag, adding one.");
				settings.commandFlags.add(new CommandFlag());
			}
			return true;
			
		} catch (FileNotFoundException e) {
			LOGGER.error("Unable to find file {}, creating a default file.", settingsFile);
			
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(settingsFile), "utf-8"))) {
				writer.write(javaToJson(new Settings()));
				return false;
			} catch (IOException e1) {
				LOGGER.error("Unable to create default file {}. Exiting.", settingsFile);
				System.exit(1);
				return false;
			}
		}
		catch (IOException e) {
			LOGGER.error("Error in reading file {}. Using default settings", settingsFile);
			return false;
		}
	}
	
	
	/**
	 * This function will write the current Settings object stored by the SettingsManager into the given file
	 * as a JSON object. It will return true if this was successful and false otherwise.
	 * @return true if successful, false if not
	 */
	public boolean writeSettings() {
		LOGGER.info("Writing settings {} to file {}", settings.toString(), settingsFile);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(settingsFile), "utf-8")))  {
		    writer.write(javaToJson(settings));		     
		    writer.close();
		    return true;
		} catch (IOException e) {
			LOGGER.error("Unable to write settings to file {}.", settingsFile);
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * This function simply returns the Settings object controlled by the SettingsManager.
	 * @return Settings object containing current settings
	 */
	public Settings getSettings() {
		return settings;
	}
	
	/**
	 * This function will set the command flag for the server as specified in the passed in CommandFlag object.
	 * If the given server doesn't currently exist in the list of command flag settings, then it will be added.
	 * @param commandFlag The CommandFlag object to set or add to the settings.
	 * @return boolean True if the settings were updated and written, false otherwise.
	 */
	public boolean setCommandFlag(CommandFlag commandFlag) {
		
		LOGGER.info("Adding flag {} to server {}", commandFlag.commandFlag, commandFlag.server);
		boolean foundServer = false;
		for (CommandFlag cf : settings.commandFlags)
		{
			LOGGER.debug("Comparing to {}", cf.server);
			if (cf.server.equalsIgnoreCase(commandFlag.server))
			{
				LOGGER.debug("Servers matched, updating server instead of adding.");
				cf.commandFlag = commandFlag.commandFlag;
				foundServer = true;
			}
		}
		
		if (!foundServer)
		{
			LOGGER.debug("Didn't find server, adding to list.");
			settings.commandFlags.add(commandFlag);
		}
		
		return this.writeSettings();
		
	}
}
