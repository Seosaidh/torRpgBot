package torRpgBot;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RollCommand extends TorDice{
	
	private final Logger LOGGER = LogManager.getLogger(torRpgBot.class.getName());
	
	public RollCommand(String flag) {
		super(flag);
	}

	@Override
	protected List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("r");
		aliases.add("roll");
		return aliases;
	}

	@Override
	protected void handleCommand(MessageReceivedEvent event) {
		String result;
		String command = event.getMessage().getContentDisplay().trim();
		Guild guild = event.getGuild();
		User author = event.getAuthor();
		MessageChannel channel = event.getChannel();
		
		String[] temp = command.split(" ", 2);
		
		if (temp.length < 2)
		{
			String output = "Invalid roll command. No command body is present. Please use the following syntax: " +
					getFlag() + "roll [w][a|d] NUM_OF_SUCCESS [(NUM_OF_MASTERY)] [+|- MODIFIER] SKILL_NAME [> TN]";
			LOGGER.error(output);
			channel.sendMessage(output).queue();
			return;
		}

		command = temp[1];		

		result = handleRollCommand(command, author, false, guild);
		
    	if (!result.isEmpty())
    	{
    		LOGGER.debug("Sending message {} to channel {}", result, channel.getName());
    		channel.sendMessage(result).queue();
    	}
	}

	@Override
	protected String getHelp() {
		return "**Roll Command**\n"
				+ "**Aliases:** roll, r\n"
				+ "**Syntax:** command [w][a|d] NUM_OF_SUCCESS_DICE [(NUM_OF_MASTERY_DICE)] [+|- MODIFIER] SKILL_NAME [> TN]\n"
				+ "**Description:** This command will a roll for a companion in TOR. It will roll NUM_OF_SUCCESS_DICE plus a feat "
				+ "die and then print the results. It will also handle rolling the feat die twice and keeping the best/worst result, "
				+ "as well as mastery dice and any constant modifiers. It also handles checking against the provided TN.\n"
				+ "**Options:**\n"
				+ "w: Include if the character is weary\n"
				+ "a: Include if the feat die should be rolled twice and the best result kept.\n"
				+ "d: Include if the feat die should be rolled twice and the worst result kept.\n"
				+ "NUM_OF_SUCCESS_DICE: The number of success dice to roll. This is a required field.\n"
				+ "(NUM_OF_MASTERY_DICE): This optional number describes the number of mastery dice to roll. If it is absent, it is assumed to be 0.\n"
				+ "+|- MODIFIER: This adds a positive or negative modifier to the final roll result.\n"
				+ "SKILL_NAME: This is a string describing the skill rolled. It is a required field.\n"
				+ "> TN: This optional field sets the target number that must be reached/exceeded to succeed at the roll. If absent, the TN is set to 14.";
	}
	
	

}
