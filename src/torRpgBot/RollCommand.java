/**
 * <h1>RollCommand Class</h1>
 * This class extends {@link torRpgBot.TorDice TorDice}, which extends {@link torRpgBot.Command Command}.
 * The purpose of this command is to handle hero and friendly NPC roll commands.
 * <p>
 * This class simply implements the abstract methods {@link torRpgBot.Command#getAliases() getAliases},
 * {@link torRpgBot.Command#handleCommand(MessageReceivedEvent event) handleCommand},
 * and {@link torRpgBot.Command#getHelp() getHelp}. Since this is a roll command, this class
 * also extends {@link torRpgBot.TorDice TorDice}, and the handleCommand function passes most of the work
 * on to the {@link torRpgBot.TorDice TorDice#handleRollCommand(String command, User author, boolean isAdversary, Guild guild) handleRollCommand}
 * function, simply passing in the correct boolean to ensure that this is a hero roll, not an adversary roll.
 * @author Seosaidh
 * @version 1.1
 * @since 0.0.2
 */
package torRpgBot;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RollCommand extends TorDice{
	
	private final Logger LOGGER = LogManager.getLogger(RollCommand.class.getName());
	
	public RollCommand(List<CommandFlag> flag, torDiceInterface dice, EmoteInterface emoteProvider) {
		super(flag, dice, emoteProvider);
	}

	
	/**
	* This is an override of the {@link torRpgBot.Command#getAliases Command class getAliases} function. 
	* It simply returns a hard-coded list of command aliases for the Roll command. The possible aliases
	* are: "r" and"roll". If the first word (less the commandFlag string) matches one of these
	* strings, then the RollCommand class will handle the event.
	* @return List of String objects containing the roll aliases.
	*/
	@Override
	protected List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("r");
		aliases.add("roll");
		return aliases;
	}

	
	/**
	* This is an override of the {@link torRpgBot.Command#handleCommand Command class handleCommand} function. 
	* This function will first pull out the Guild, the User author, and the String message string from the event.
	* Then, it performs a quick initial check on the command string before passing it (along with the Guild and author)
	* to the {@link torRpgBot.TorDice#handleRollCommand handleRollCommand} function with the isAdversary parameter
	* set to false. Once that function returns, this function will check to see if there is a string response to send back
	* on the Discord channel. If so, it will send this response.
	* @param event A MessageReceivedEvent class that contains all the information from the Discord event that will be handled by this function
	*/
	@Override
	protected void handleCommand(MessageReceivedEvent event) {
		String result;
		String command = event.getMessage().getContentDisplay().trim();
		Guild guild = event.getGuild();
		String author;
		MessageChannel channel = event.getChannel();
		
		String[] temp = command.split(" ", 2);
		
		if (temp.length < 2)
		{
			String output = "Invalid roll command. No command body is present. Please use the following syntax: " +
					getFlag(event.getGuild().getName()) + "roll [w][a|d] NUM_OF_SUCCESS [(NUM_OF_MASTERY)] [+|- MODIFIER] SKILL_NAME [> TN]";
			LOGGER.error(output);
			channel.sendMessage(output).queue();
			return;
		}
		
		if (event.getMessage().isWebhookMessage())
		{
			author = event.getAuthor().getName();
		}
		else
		{
			author = event.getMember().getEffectiveName();
		}

		command = temp[1];
		
		LOGGER.info("Processing roll command received on channel {} on server {} from user {} with body {}.",
				channel.getName(), guild.getName(), author, command);

		result = handleRollCommand(command, author, false, guild);
		
    	if (!result.isEmpty())
    	{
    		LOGGER.info("Sending message {} to channel {}", result, channel.getName());
    		channel.sendMessage(result).queue();
    	}
	}

	/**
	* This is an override of the {@link torRpgBot.Command#getHelp Command class getHelp} function. 
	* This function simply returns a hard-coded string that describes the proper way to call the Adversary command.
	* @return String containing the help text.
	*/
	@Override
	protected String getHelp() {
		return "__**Roll Command**__\n"
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
