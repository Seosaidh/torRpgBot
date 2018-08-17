/**
 * <h1>SetFlag Command</h1>
 * This class will execute the setflag command, setting a custom command flag on a per-server basis by updating
 * the settings manager. It extends the Command class.
 */

package torRpgBot;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SetFlag extends Command{
	
	private final Logger LOGGER = LogManager.getLogger(SetFlag.class.getName());
	private SettingsManager settingsManager;

	public SetFlag(List<CommandFlag> flags, SettingsManager settingsManager) {
		super(flags);
		this.settingsManager = settingsManager;
	}

	/**
	 * This overrides the handleCommand function of the Command class.
	 * It simply pulls the second word out after the command string and then attempts to set it in the settings manager
	 * with the current server name. It sends a reply message back indicating success or failure.
	 */
	@Override
	protected void handleCommand(MessageReceivedEvent event) {
		
		String command = event.getMessage().getContentDisplay().trim();
		Guild guild = event.getGuild();
		MessageChannel channel = event.getChannel();
		
		String[] temp = command.split(" ", 2);
		
		if (temp.length < 2)
		{
			String output = "Invalid format, please use the following syntax for the SetFlag command: setflag FLAG.";
			LOGGER.error(output);
			channel.sendMessage(output).queue();
			return;
		}
		
		LOGGER.info("Attempting to set flag {} on server {}.", temp[1], guild.getName());
		
		if (settingsManager.setCommandFlag(new CommandFlag(guild.getName(), temp[1])))
		{
			LOGGER.debug("Successfully set flag.");
			channel.sendMessage("Successfully set the new flag.").queue();
		}
		else
		{
			LOGGER.debug("Failed to set flag.");
			channel.sendMessage("ERROR! Unable to set the new flag.").queue();
		}
		
	}

	/**
	 * This returns the help string for the SetFlag command.
	 * @return String containing the help message.
	 */
	@Override
	protected String getHelp() {
		return "__**Set Flag**__\n" +
				"**Aliases:** setflag, setFlag\n" +
				"**Syntax:** command FLAG_TO_SET\n" +
				"**Description:** This command will set the command flag to use for the current server. " +
				"The flag is a string of 1-X characters where X is some number equal to or greater than 1." +
				" It is advisable to keep the command flag to only one character long, however, for ease of use.";
	}

	
	/**
	 * The aliases that will match the command string.
	 * @return List of strings where each string represents a valid command form for this command.
	 */
	@Override
	protected List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("setFlag");
		aliases.add("setflag");
		return aliases;
	}

}
