package torRpgBot;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.core.entities.User;


public class TorDice {
	
	private static Random rand = new Random();
	private static final Logger LOGGER = LogManager.getLogger(torRpgBot.class.getName());
	
	/**
	 * This function is used as the interface to handle all TOR rolling commands.
	 * The commands will be in the following format:
	 * [w][a|d] NUM_OF_SUCCESS_DICE [+NUM_OF_MASTERY_DICE] SKILL_NAME [> TN]
	 * The fields are as follows:
	 * 		w: The character is weary
	 * 		a: The character is rolling with advantage (roll two feat dice and keep the best result)
	 * 		d: The character is rolling with disadvantage (roll two feat dice and keep the worst result)
	 * 	If the number of mastery dice is left out, it is assumed to be 0.
	 *  If the TN is left out, it is assumed to be 14.
	 *  @param command The string containing the roll command.
	 *  @param author The User variable that contains the author. Currently unused.
	 *  @param isAdversary Boolean true if the roll is from an adversary, false otherwise.
	 *  @return String containing the result of the roll formatted such as to be ready to send to Discord.
	 */
	public static String handleRollCommand(String command, User author, boolean isAdversary) {
		return "To be implemented";
	}
	
	/*
	 * This function simply rolls a d12 and returns the integer result.
	 */
	private static int rolld12() {
		return rand.nextInt(12) + 1;
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
	 * @return The correct integer value of the feat die roll, including any selection due to advantage/disadvantage. Also 0 for the appropriate roll (EoS/GRune). -1 on Error.
	 */
	private static int rollFeat(boolean hasAdvantage, boolean hasDisadvantage, boolean isAdversary) {
		int roll1, roll2;
		roll1 = rolld12();
		roll2 = rolld12();
		
		LOGGER.debug("The first d12 rolled came up {} and the second {}", roll1, roll2);
		
		if (hasAdvantage && hasDisadvantage)
		{
			LOGGER.error("You cannot roll with both advantage and disadvantage. The roll will be treated as if neither was applicable.");
			hasAdvantage = false;
			hasDisadvantage = false;
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
		
		if (hasAdvantage)
		{
			LOGGER.debug("The character has advantage, returning the best roll ({})", Math.max(roll1, roll2));
			return Math.max(roll1, roll2);
		}
		else if (hasDisadvantage)
		{
			LOGGER.debug("The character has disadvantage, returning the worst roll ({})", Math.min(roll1, roll2));
			return Math.min(roll1, roll2);
		}
		else
		{
			LOGGER.debug("The character has neither advantage nor disadvantage, returning the first roll ({})", roll1);
			return roll1;
		}
	}
	
	/*
	 * This function simply rolls a d6 and returns the result as an integer.
	 */
	private static int rolld6() {
		return rand.nextInt(6) + 1;
	}
	
	/**
	 * This function takes the feat result and the array of success results,
	 * along with the various modifiers and then constructs the string describing the result of the roll.
	 * @param feat The integer describing the feat die result. It will be 0 for the appropriate roll, so no conversion is needed while adding.
	 * @param success The integer array of the relevant success dice (with mastery dice already removed).
	 * @param target The target number that must be beaten.
	 * @param author The string with the name of the person who issued the roll command.
	 * @param skill The string of the skill rolled.
	 * @param isAdversary Boolean true if the roll was performed by an adversary. Necessary to know what emoji to use for 0 on the feat die.
	 * @param isWeary Boolean true if the character is weary. Necessary since we have to know the numbers rolled for the emojis, but must also know how to sum the result.
	 * @return String to be sent to Discord describing the roll and the result.
	 */
	private static String compileResult(int feat, int[] success, int target, String author, String skill,
			boolean isAdversary, boolean isWeary) {
		String result = new String();
		int sum = feat;
		boolean isGreatSuccess = false;
		boolean isExtraordinarySuccess = false;
		boolean succeeded = false;
		
		result.concat(author);
		result.concat(" rolled ");
		result.concat(skill);
		result.concat(" ");
		result.concat(Integer.toString(success.length));
		result.concat(": ");
		result.concat(Integer.toString(feat));
		
		for (int i = 0; i < success.length; i++)
		{
			result.concat(", ");
			result.concat(Integer.toString(success[i]));
			
			if (isWeary && success[i] <= 3)
			{
				LOGGER.debug("The character is weary, so a result of {} is actually 0.", success[i]);
			}
			else
			{
				sum = sum + success[i];
			}
			
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
		
		result.concat(". \n");
		
		if ((isAdversary && feat == 11) || (!isAdversary && feat == 12) || sum >= target)
		{
			succeeded = true;
			if (isExtraordinarySuccess)
			{
				result.concat("Extraordinary Success!");
			}
			else if (isGreatSuccess)
			{
				result.concat("Great Success!");
			}
			else
			{
				result.concat("Success!");
			}
		}
		else
		{
			result.concat("Failure!");
		}
		
		
		return result;
	}

}
