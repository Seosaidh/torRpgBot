/**
 * <h1>TorDice Class (Abstract)</h1
 * The TorDice class extends the Command class for use in rolling TOR dice. It handles all the common aspects of rolling dice,
 * but leaves the final selection of whether an adversary is rolling or a hero is rolling to the two subclasses
 * {@link torRpgBot.RollCommand RollCommand} and {@link torRpgBot.AdversaryCommand AdversaryCommand}.
 * However, this class handles parsing the roll command body, actually rolling the dice, and building the result string.
 * @author Seosaidh
 * @version 2.0
 * @since 0.0.2
 */
package torRpgBot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;


public abstract class TorDice extends Command{
	
	public interface torDiceInterface {
		public int rolld6();
		public int rolld12();
	}
	
	enum POSSIBLE{
		OPTIONS,
		SUCCESS,
		MASTERY,
		SIGN,
		MODIFIER,
		SKILL,
		GREATER_THAN,
		TN
	}
	
	private torDiceInterface diceProvider;
	private final Logger LOGGER = LogManager.getLogger(TorDice.class.getName());
	

	/**
	 * This is a private class used solely to store the parsed data from the command body.
	 * @author Seosaidh
	 *
	 */
	class CommandResults {
		public boolean isWeary = false;
		public boolean hasAdvantage = false;
		public boolean hasDisadvantage = false;
		public boolean parseSuccessful = false;
		public int numOfSuccess = 0;
		public int numOfMastery = 0;
		public int modifier = 0;
		public int targetNumber = 14;
		public String skillName = "";
		
		public String toString() {
			return "isWeary: " + isWeary + ", hasAdvantage: " + hasAdvantage + ", hasDisadvantage: " + hasDisadvantage +
					", parseSuccessful: " + parseSuccessful + ", numOfSucces: " + numOfSuccess + ", numOfMastery: " +
					numOfMastery + ", modifier: " + modifier + ", targetNumber: " + targetNumber + ", skillName: " + skillName;
		}
	}
	
	private class EmoteIds {
		public String d121 = "1";
		public String d122 = "2";
		public String d123 = "3";
		public String d124 = "4";
		public String d125 = "5";
		public String d126 = "6";
		public String d127 = "7";
		public String d128 = "8";
		public String d129 = "9";
		public String d1210 = "10";
		public String d1211 = "11";
		public String d1212 = "12";
		public String d61 = "1";
		public String d62 = "2";
		public String d63 = "3";
		public String d64 = "4";
		public String d65 = "5";
		public String d66 = "6";
		public String d61weary = "1";
		public String d62weary = "2";
		public String d63weary = "3";
	}
	
	public TorDice(List<CommandFlag> flag, torDiceInterface dice) {
		super(flag);
		diceProvider = dice;
	}

	
	/**
	 * This function is used as the interface to handle all TOR rolling commands.
	 * The commands will be in the following format:
	 * {@literal [w][a|d] NUM_OF_SUCCESS_DICE [(NUM_OF_MASTERY_DICE)] [+|- MODIFIER] SKILL_NAME [> TN]}
	 * The fields are as follows:
	 * <br>		w: The character is weary
	 * <br>		a: The character is rolling with advantage (roll two feat dice and keep the best result)
	 * <br>		d: The character is rolling with disadvantage (roll two feat dice and keep the worst result)
	 * 	If the number of mastery dice is left out, it is assumed to be 0.
	 *  If the modifier is left out, it is assumed to be 0.
	 *  If the TN is left out, it is assumed to be 14.
	 *  @param command The string containing the roll command.
	 *  @param author The User variable that contains the author. Currently unused.
	 *  @param isAdversary Boolean true if the roll is from an adversary, false otherwise.
	 *  @param guild A Guild object that will, in the future, be used to select which emojis to use for the dice faces
	 *  @return String containing the result of the roll formatted such as to be ready to send to Discord.
	 */
	public String handleRollCommand(String command, String author, boolean isAdversary, Guild guild) {

		CommandResults parsedCommand = parseCommandString(command);
		int[] successDice;
		int[] feat;
		
		if (!parsedCommand.parseSuccessful)
		{
			LOGGER.error("The command string {} is invalid. Please follow the proper syntax"
					+ " `[w][a|d] NUM_OF_SUCCESS_DICE [(NUM_OF_MASTERY_DICE)] [+|- MODIFIER] SKILL_NAME [> TN]`", command);
			
			return "The command string " + command +
					" is invalid. Please follow the proper syntax "
					+ "`[w][a|d] NUM_OF_SUCCESS_DICE [(NUM_OF_MASTERY_DICE)] [+|- MODIFIER] SKILL_NAME [> TN]`";
		}
		
		feat = rollFeat(parsedCommand, isAdversary);
		
		if(parsedCommand.numOfSuccess > 0)
		{
			successDice = rollSuccess(parsedCommand.numOfSuccess, parsedCommand.numOfMastery);
		}
		else
		{
			LOGGER.debug("No success dice to roll, setting the results to an empty array");
			successDice = new int[0];
		}
		EmoteIds emoteStrings = getEmoteStrings(guild);
		
		return compileResult(feat, successDice, isAdversary, parsedCommand, author, emoteStrings);
	}
	
	/**
	 * This function will go through the various emojis in the given guild and grab the full string needed for a bot to use
	 * a custom emoji and place it into the relevant part of EmoteIds. If an emote name doesn't exist, then it will not be
	 * placed into the EmoteIds class, and the default will be used.
	 * The string needed to use a custom emote is <:emoji_name:emoji_id>
	 * @param guild The server that the message will be sent to, and so the one needed to retrieve the emoji id from.
	 * @return EmoteIds A container class that has all the possible dice faces and the string representing the emoji.
	 */
	private EmoteIds getEmoteStrings(Guild guild) {
		EmoteIds result = new EmoteIds();
		List<Emote> emotes = guild.getEmotes();

		for (Emote e : emotes)
		{
			switch (e.getName())
			{
			case "d121":
				result.d121 = "<:d121:" + e.getId()+ ">";
				break;
			case "d122":
				result.d122 = "<:d122:" + e.getId()+ ">";
				break;
			case "d123":
				result.d123 = "<:d123:" + e.getId()+ ">";
				break;
			case "d124":
				result.d124 = "<:d124:" + e.getId()+ ">";
				break;
			case "d125":
				result.d125 = "<:d125:" + e.getId()+ ">";
				break;
			case "d126":
				result.d126 = "<:d126:" + e.getId()+ ">";
				break;
			case "d127":
				result.d127 = "<:d127:" + e.getId()+ ">";
				break;
			case "d128":
				result.d128 = "<:d128:" + e.getId()+ ">";
				break;
			case "d129":
				result.d129 = "<:d129:" + e.getId()+ ">";
				break;
			case "d1210":
				result.d1210 = "<:d1210:" + e.getId()+ ">";
				break;
			case "d1211":
				result.d1211 = "<:d1211:" + e.getId()+ ">";
				break;
			case "d1212":
				result.d1212 = "<:d1212:" + e.getId()+ ">";
				break;
			case "d61":
				result.d61 = "<:d61:" + e.getId()+ ">";
				break;
			case "d62":
				result.d62 = "<:d62:" + e.getId()+ ">";
				break;
			case "d63":
				result.d63 = "<:d63:" + e.getId()+ ">";
				break;
			case "d64":
				result.d64 = "<:d64:" + e.getId()+ ">";
				break;
			case "d65":
				result.d65 = "<:d65:" + e.getId()+ ">";
				break;
			case "d66":
				result.d66 = "<:d66:" + e.getId()+ ">";
				break;
			case "d61weary":
				result.d61weary = "<:d61weary:" + e.getId()+ ">";
				break;
			case "d62weary":
				result.d62weary = "<:d62weary:" + e.getId()+ ">";
				break;
			case "d63weary":
				result.d63weary = "<:d63weary:" + e.getId()+ ">";
				break;
			default:
				break;
			}
		}
		return result;
	}
	
	
	/**
	 * This function parses the input string to fill in a CommandResults structure and then return it.
	 * It parses the string according to the syntax defined for roll commands:
	 * [w][a|d] NUM_OF_SUCCESS_DICE [(NUM_OF_MASTERY_DICE)] [+|- MODIFIER] SKILL_NAME [> TN]
	 * @param command The string containing the command body, which should conform to the syntax above.
	 * @return A CommandResults structure containing the results of the parsed command. In particular, the parseSuccessful field will only be true if we successfully parse out a numberOfSuccess and skillName.
	 */
	/* private -> testing*/ CommandResults parseCommandString(String command) {

		CommandResults result = new CommandResults();
		
		result.parseSuccessful = false;
		
		boolean haveSuccess = false;
		boolean haveSkill = false;
		boolean negativeModifier = false;
		
		List<POSSIBLE> possibleNext = new ArrayList<POSSIBLE>();
		possibleNext.add(POSSIBLE.OPTIONS);
		possibleNext.add(POSSIBLE.SUCCESS);
		
		String[] words = command.split(" ");
		
		LOGGER.debug("Parsing string: {}", command);
		
		/*
		 * words[0] is either the options w, a|d; or is the number of success dice.
		 * words[1] is either the numOfSuccess, numOfMastery, +-, modifier, or skillName
		 * words[2] is either the numOfMastery, +-, modifier, skillName, or > or might not exist
		 * words[3] is either the +-, modifier, skillName, or > or TN or might not exist
		 * words[4] is either the modifier, skillName or > or TN or might not exist
		 * words[5] is either the skillName, > or TN, or might not exist
		 * words[6] is either > or the TN or might not exist.
		 * words[7] is either the TN or doesn't exist.
		 * 
		 * words must be at least two long and can be no more than 8 long
		 */
		
		if (words.length < 2 || words.length > 8)
		{
			LOGGER.error("Bad command body. Not enough or too many words. Full body: {}", command);
			return result;
		}
		
		for (int i = 0; i < words.length; i++)
		{
			LOGGER.debug("Word {} is being parsed.", words[i]);
			LOGGER.debug("Current possibilities: {}", possibleNext.toString());
			
			if (possibleNext.contains(POSSIBLE.OPTIONS))
			{
				if (words[i].contains("w"))
				{
					result.isWeary = true;
				}
				
				if (words[i].contains("a"))
				{
					result.hasAdvantage = true;
				}
				
				if (words[i].contains("d"))
				{
					result.hasDisadvantage = true;
				}
				
				if (result.hasAdvantage && result.hasDisadvantage)
				{
					LOGGER.error("Bad Command. Both advantage and disadvantage were set.");
					return result;
				}
				
				// Even if we don't find the options on the first iteration of the loop, remove it from the possible options anyway,
				// since it's only allowed in the first word.
				possibleNext.remove(POSSIBLE.OPTIONS);
				
				if (result.hasAdvantage || result.hasDisadvantage || result.isWeary)
				{
					continue;
				}
			}
			
			if (possibleNext.contains(POSSIBLE.SUCCESS))
			{
				try
				{
					result.numOfSuccess = Integer.parseInt(words[i]);
					haveSuccess = true;
					possibleNext.clear();
					possibleNext.add(POSSIBLE.MASTERY);
					possibleNext.add(POSSIBLE.SIGN);
					possibleNext.add(POSSIBLE.MODIFIER);
					possibleNext.add(POSSIBLE.SKILL);
					continue;
				}
				catch (NumberFormatException e)
				{
					if (i > 1)
					{
						LOGGER.error("Bad message format. Neither the first nor second word results in a number. The full command body is: {}", command);
						return result;
					}
				}
			}
			
			if (possibleNext.contains(POSSIBLE.MASTERY))
			{
				if (words[i].startsWith("("))
				{
					String mastery = words[i].substring(1, words[i].length() - 1);
					
					try
					{
						result.numOfMastery = Integer.parseInt(mastery);
						possibleNext.remove(POSSIBLE.MASTERY);
						continue;
					}
					catch (NumberFormatException e)
					{
						LOGGER.error("Bad message format. No number enclosed in the parentheses denoting number of mastery dice. Full command string: {}", command);
						return result;
					}
				}
			}
			
			if (possibleNext.contains(POSSIBLE.SIGN))
			{
				if (words[i].equals("+") || words[i].equals("-"))
				{
					possibleNext.remove(POSSIBLE.SIGN);
					possibleNext.remove(POSSIBLE.MASTERY);
					
					if (words[i].equals("-"))
					{
						negativeModifier = true;
					}
					
					continue;
				}
			}
			
			if (possibleNext.contains(POSSIBLE.MODIFIER))
			{
				try
				{
					result.modifier = Integer.parseInt(words[i]);
					
					if (negativeModifier && result.modifier > 0)
					{
						result.modifier = -result.modifier;
					}
					
					possibleNext.remove(POSSIBLE.SIGN);
					possibleNext.remove(POSSIBLE.MODIFIER);
					possibleNext.remove(POSSIBLE.MASTERY);
					
					continue;
				}
				catch (NumberFormatException e)
				{
					
				}
			}
			
			if (possibleNext.contains(POSSIBLE.SKILL))
			{
				if (words[i].matches("^([A-Z]|[a-z])+"))
				{
					if (haveSkill)
					{
						result.skillName = result.skillName.concat(" " + words[i]);
					}
					else
					{
						result.skillName = words[i];
					}
					haveSkill = true;
					possibleNext.clear();
					possibleNext.add(POSSIBLE.SKILL);
					possibleNext.add(POSSIBLE.GREATER_THAN);
					possibleNext.add(POSSIBLE.TN);
					
					continue;
				}
			}
			
			if (possibleNext.contains(POSSIBLE.GREATER_THAN))
			{
				if (words[i].startsWith(">"))
				{
					possibleNext.remove(POSSIBLE.GREATER_THAN);
					possibleNext.remove(POSSIBLE.SKILL);
					
					if (words[i].length() > 1)
					{
						String temp = words[i].substring(1);
						try
						{
							result.targetNumber = Integer.parseInt(temp);
							possibleNext.clear();
						}
						catch (NumberFormatException e)
						{
							
						}
					}
					
					continue;
				}
			}
			
			if (possibleNext.contains(POSSIBLE.TN))
			{
				try
				{
					result.targetNumber = Integer.parseInt(words[i]);
					possibleNext.clear();
				}
				catch (NumberFormatException e)
				{
					
				}
			}
		} // end loop through the command words.
		
		
		if (haveSuccess && haveSkill)
		{
			result.parseSuccessful = true;
		}
		
		LOGGER.debug("Parsed Results = {}", result.toString());
		return result;
	}
	
	
	/**
	 * This function will handle rolling the feat die and returning the integer result.
	 * It also handles advantage/disadvantage. It returns 0 on an Eye of Sauron (11) when
	 * the roll is performed by a hero and a 0 on a G-Rune (12) when the roll is performed
	 * by an adversary.
	 * 
	 * @param hasAdvantage Boolean true if the roll has advantage.
	 * @param hasDisadvantage Boolean true if the roll has disadvantage.
	 * @param isAdversary Boolean true if the roll is performed by an adversary, false otherwise.
	 * @return An int array containing the roll results. Also 0 for the appropriate roll (EoS/GRune). -1 on Error.
	 */
	/* private -> testing*/ int[] rollFeat(CommandResults command, boolean isAdversary) {
		int roll1, roll2;
		roll1 = diceProvider.rolld12();
		roll2 = diceProvider.rolld12();
		
		LOGGER.debug("The first d12 rolled came up {} and the second {}", roll1, roll2);
		
		if (command.hasAdvantage && command.hasDisadvantage)
		{
			LOGGER.error("You cannot roll with both advantage and disadvantage. The roll will be treated as if neither was applicable.");
			command.hasAdvantage = false;
			command.hasDisadvantage = false;
		}
		
		if (isAdversary)
		{
			if (roll1 == 12)
			{
				LOGGER.debug("Converting the first roll from a G-Rune to a 0");
				roll1 = 0;
			}
			
			if (roll2 == 12)
			{
				LOGGER.debug("Converting the second roll from a G-Rune to a 0");
				roll2 = 0;
			}
		}
		else
		{
			if (roll1 == 11)
			{
				LOGGER.debug("Converting the first roll from a EoS to a 0");
				roll1 = 0;
			}
			
			if (roll2 == 11)
			{
				LOGGER.debug("Converting the second roll from a Eos to a 0");
				roll2 = 0;
			}
		}
		
		if (command.hasAdvantage || command.hasDisadvantage)
		{
			LOGGER.debug("The character rolled two feat dice, returning both of them. ({}, {})", roll1, roll2);
			return new int[] {roll1, roll2};
		}
		else
		{
			LOGGER.debug("The character has neither advantage nor disadvantage, returning the first roll ({})", roll1);
			return new int[] {roll1};
		}
	}
	
	
	
	/**
	 * This rolls all the d6 dice necessary to complete the roll, including mastery dice.
	 * Once all the dice have been rolled, it sorts the results and returns an array with the results in
	 * descending order (largest number first).
	 * @param numOfSuccess Number of success dice to roll.
	 * @param numOfMastery Number of mastery dice to roll.
	 * @return integer array containing the results of all dice rolled in descending order.
	 */
	/* private -> testing*/ int[] rollSuccess(int numOfSuccess, int numOfMastery) {
		int[] results = new int[numOfSuccess + numOfMastery];
		List<Integer> fullResults = new ArrayList<Integer>(numOfSuccess + numOfMastery);
		
		for (int i = 0; i < (numOfSuccess + numOfMastery); i++)
		{
			fullResults.add(new Integer(diceProvider.rolld6()));
		}
		
		Collections.sort(fullResults, Collections.reverseOrder());

		// I originally was only going to show the results of the relevant success dice, but decided to show
		// the results of the mastery dice as well. If you want to only show the success dice, remove the
		// "+ numOfMastery" from both this for loop and the declaration of the results array.
		for (int i = 0; i < numOfSuccess + numOfMastery; i++)
		{
			results[i] = fullResults.get(i).intValue();
		}		
			
		
		return results;
	}
	
	
	/**
	 * This function returns the string representation of the given feat result given the emoji ids and whether 
	 * the roll was performed by an adversary.
	 * @param feat The result of the feat die. 0 for the relevant face
	 * @param isAdversary Whether the roll was performed by an adversary. Necessary to interpret the result of 0
	 * @param emoteStrings The structure containing the strings to use for the dice faces.
	 * @return String to insert into roll results.
	 */
	private String getFeatString(int feat, boolean isAdversary, EmoteIds emoteStrings) {
		if (isAdversary && feat == 0)
		{
			return emoteStrings.d1212;
		}
		else if (!isAdversary && feat == 0)
		{
			return emoteStrings.d1211;
		}
		else
		{
			switch (feat) {
			case 1:
				return emoteStrings.d121;
			case 2: 
				return emoteStrings.d122;
			case 3: 
				return emoteStrings.d123;
			case 4: 
				return emoteStrings.d124;
			case 5: 
				return emoteStrings.d125;
			case 6: 
				return emoteStrings.d126;
			case 7:
				return emoteStrings.d127;
			case 8: 
				return emoteStrings.d128;
			case 9: 
				return emoteStrings.d129;
			case 10: 
				return emoteStrings.d1210;
			case 11:
				return emoteStrings.d1211;
			case 12:
				return emoteStrings.d1212;
			default:
				LOGGER.error("Unable to find face for feat result of {}", feat);
				return "-1";
					
			}
		}
	}
	
	/**
	 * This function will return the proper string to use for the result of the given success die roll.
	 * @param success The result on the success die
	 * @param emoteStrings The structure of strings for the die faces
	 * @param isWeary Whether or not the character is weary
	 * @return String to insert into the roll results
	 */
	private String getSuccessString(int success, EmoteIds emoteStrings, boolean isWeary) {
		switch(success) {
		case 1:
			if (isWeary)
			{
				return emoteStrings.d61weary;
			}
			else
			{
				return emoteStrings.d61;
			}
		case 2:
			if (isWeary)
			{
				return emoteStrings.d62weary;
			}
			else
			{
				return emoteStrings.d62;
			}
		case 3:
			if (isWeary)
			{
				return emoteStrings.d63weary;
			}
			else
			{
				return emoteStrings.d63;
			}
		case 4:
			return emoteStrings.d64;
		case 5:
			return emoteStrings.d65;
		case 6:
			return emoteStrings.d66;
		default:
				LOGGER.error("Unable to find face for success result of {}", success);
				return "-1";
		}
	}
	
	/**
	 * This function takes the feat result and the array of success results,
	 * along with the various modifiers and then constructs the string describing the result of the roll.
	 * @param feat The integer describing the feat die result. It will be 0 for the appropriate roll, so no conversion is needed while adding.
	 * @param success The integer array of the relevant success dice.
	 * @param isAdversary Boolean true if the roll was performed by an adversary. Necessary to know what emoji to use for 0 on the feat die.
	 * @param command The command structure containing all the relevant information to interpret the dice.
	 * @param author The User structure for the user who sent the command.
	 * @return String to be sent to Discord describing the roll and the result.
	 */
	/* private -> testing*/ String compileResult(int[] feat, int[] success, boolean isAdversary,
			CommandResults command, String author, EmoteIds emoteStrings) {
		String result = author;
		// Final String: NAME [wearily] rolled SKILL and got: FEAT; SUCCESS1, SUCCESS2, ... = SUM >|< TN A [Great|Extraordinary] Success!\n Unused dice: unusedFeat, mastery
		int sum = 0;
		String featString = "";
		String successString = "";
		String unusedFeat = "";
		String mastery = "";
		int finalFeatResult = 0;
		
		if (isAdversary)
		{
			result = "The Enemy";
		}
		
		if (feat.length > 1 && command.hasAdvantage)
		{
			finalFeatResult = sum = Math.max(feat[0], feat[1]);
		}
		else if (feat.length > 1 && command.hasDisadvantage)
		{
			finalFeatResult = sum = Math.min(feat[0], feat[1]);
		}
		else
		{
			finalFeatResult = sum = feat[0];
		}
		
		featString = getFeatString(finalFeatResult, isAdversary, emoteStrings);
		boolean isGreatSuccess = false;
		boolean isExtraordinarySuccess = false;
		
		if (command.hasAdvantage)
		{
			unusedFeat = getFeatString(Math.min(feat[0], feat[1]), isAdversary, emoteStrings);
		}
		else if (command.hasDisadvantage)
		{
			unusedFeat = getFeatString(Math.max(feat[0], feat[1]), isAdversary, emoteStrings);
		}
		
		for (int i = 0; i < success.length; i++)
		{
			if (i >= command.numOfSuccess)
			{
				mastery = mastery.concat(", " + getSuccessString(success[i], emoteStrings, command.isWeary));
			}
			else
			{
				successString = successString.concat(", " + getSuccessString(success[i], emoteStrings, command.isWeary));
			}
			
			if (i >= command.numOfSuccess)
			{
				LOGGER.debug("We are now in the mastery dice, so don't add them in.");
			}
			else if (command.isWeary && success[i] <= 3)
			{
				LOGGER.debug("The character is weary, so a result of {} is actually 0.", success[i]);
			}
			else
			{
				sum = sum + success[i];
				
				if (success[i] == 6)
				{
					if (isGreatSuccess)
					{
						isExtraordinarySuccess = true;
					}
					else
					{
						isGreatSuccess = true;
					}
				}
			}
		}
		
		if (command.numOfSuccess > 0)
		{
			successString = successString.substring(2);
		}
		
		if (command.numOfMastery > 0)
		{
			mastery = mastery.substring(2);
		}
		
		sum += command.modifier;
		
		
		LOGGER.debug("Final sum: {}", sum);
		
		if (command.isWeary)
		{
			result = result.concat(" wearily");
		}
		result = result.concat(" rolled ");
		result = result.concat(command.skillName);
		result = result.concat(" and got");
		result = result.concat(": ");
		result = result.concat(featString);
		result = result.concat("; ");
		result = result.concat(successString);
		
		if (command.modifier != 0)
		{
			if (command.modifier > 0)
			{
				result = result.concat(" + ");
			}
			else
			{
				result = result.concat(" - ");
			}
			result = result.concat(Integer.toString(Math.abs(command.modifier)));
		}
		
		result = result.concat(" = ");
		result = result.concat(Integer.toString(sum));
		if (sum >= command.targetNumber)
		{
			result = result.concat(" > ");
		}
		else
		{
			result = result.concat(" < ");
		}
		result = result.concat(Integer.toString(command.targetNumber));
		result = result.concat("; ");
		
		if ((isAdversary && finalFeatResult == 11) || (!isAdversary && finalFeatResult == 12) || sum >= command.targetNumber)
		{
			if (isExtraordinarySuccess)
			{
				result = result.concat("an Extraordinary Success!");
			}
			else if (isGreatSuccess)
			{
				result = result.concat("a Great Success!");
			}
			else
			{
				result = result.concat("a Success!");
			}
		}
		else
		{
			result = result.concat("a Failure!");
		}
		
		if (command.hasAdvantage || command.hasDisadvantage || command.numOfMastery > 0)
		{
			result = result.concat("\n");
			result = result.concat("Unused dice: ");
			
			if (command.hasAdvantage || command.hasDisadvantage)
			{
				result = result.concat(unusedFeat + "; ");
			}
			
			if (command.numOfMastery > 0)
			{
				result = result.concat(mastery);
			}
		}
		
		LOGGER.debug("Fully compiled results string: {}", result);
		return result;
	}

}
