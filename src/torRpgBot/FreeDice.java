/**
 * <h1>FreeDice Class</h1>
 * This class implements the ability to freely roll TOR dice without reference to a skill or a TN.
 * It also doesn't require rolling a feat die.
 * @author Seosaidh
 */

package torRpgBot;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class FreeDice extends Command {
	
	private EmoteInterface emotes;
	private TorDiceInterface diceRoller;
	private final Logger LOGGER = LogManager.getLogger(FreeDice.class.getName());

	/**
	 * The class constructor. It uses the flags to pass to the Command class, the emotes are used 
	 * to grab the proper emotes for the dice faces, and the torDice is used to actually roll the dice.
	 * @param flags Passed to Command class
	 * @param emotes Interface to class that provides dice faces
	 * @param torDice Interface to class that rolls dice
	 */
	public FreeDice(List<CommandFlag> flags, EmoteInterface emotes, TorDiceInterface torDice) {
		super(flags);
		this.emotes = emotes;
		this.diceRoller = torDice;
	}

	/**
	 * This will first call a couple of functions to pull out the number of d6s and d12s to roll, then it will
	 * roll the dice and compile a result string. Finally, it sends this result string back on the text channel.
	 * If there are no dice to roll, an error message is sent.
	 */
	@Override
	protected void handleCommand(MessageReceivedEvent event) {
		int numd12 = getNumD12(event.getMessage().getContentDisplay());
		int numd6 = getNumD6(event.getMessage().getContentDisplay());
		emotes.getEmoteStrings(event.getGuild());
		
		LOGGER.info("Received free dice command: {}.", event.getMessage().getContentDisplay());
		LOGGER.debug("Num of d12: {}, num of d6: {}", numd12, numd6);
		
		String result = "Results: ";
		
		if (numd12 > 0)
		{
			result = result.concat("\nD12: ");
			
			for (int num = 0; num < numd12; num++)
			{
				if (num == 0)
				{
					result = result.concat(emotes.getFeatString(diceRoller.rolld12(), false));
				}
				else
				{
					result = result.concat(", " + emotes.getFeatString(diceRoller.rolld12(), false));
				}
			}
		}
		
		if (numd6 > 0)
		{
			result = result.concat("\nD6: ");
			
			for (int num = 0; num < numd6; num++)
			{
				if (num == 0)
				{
					result = result.concat(emotes.getSuccessString(diceRoller.rolld6(), false));
				}
				else
				{
					result = result.concat(", " + emotes.getSuccessString(diceRoller.rolld6(), false));
				}
			}
		}
		
		if (numd6 == 0 && numd12 == 0)
		{
			result = "Please enter at least one number to roll using the format: Xd6 or Xd12.";
		}
		
		LOGGER.debug("Result string: {}", result);
		event.getChannel().sendMessage(result).queue();

	}
	
	/**
	 * This will find the Xd12 string and then pull the number X out.
	 * @param command The full command string to look through.
	 * @return The number of d12s to roll.
	 */
	private int getNumD12(String command) {
		String[] words = command.split(" ");
		
		for (String word: words)
		{
			if (word.contains("d12"))
			{
				String num = word.substring(0, word.indexOf("d12"));
				return Integer.parseInt(num);
			}
		}
		
		return 0;
	}
	
	/**
	 * This will find the string Xd6 and pull out the X and return it
	 * @param command The full command string to look through
	 * @return The number of d6s to roll
	 */
	private int getNumD6(String command) {
		String[] words = command.split(" ");
		
		for (String word: words)
		{
			if (word.contains("d6"))
			{
				String num = word.substring(0, word.indexOf("d6"));
				return Integer.parseInt(num);
			}
		}
		
		return 0;
	}

	/**
	 * This returns a string representing the help message for this class.
	 * @return String containing the help message.
	 */
	@Override
	protected String getHelp() {
		return "__**Free Dice Roller**__\n" +
				"**Aliases:** dice, d\n" +
				"**Syntax:** command xd12 yd6\n" + 
				"**Description:** This command will roll the specified number of d12s and d6s. x or y can be 0, " +
				"in which case the pertinent dZ should be ommitted. However, you must roll at least one die, " +
				"otherwise an error will be generated.";
	}

	/**
	 * This returns a list of strings that are valid command strings
	 * @return List<String> containing the valid command strings for this class.
	 */
	@Override
	protected List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("d");
		aliases.add("dice");
		return aliases;
	}

}
