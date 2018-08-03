package torRpgBot;

import java.util.Random;
import net.dv8tion.jda.core.entities.User;


public class TorDice {
	
	public static String handleRollCommand(String command, User author, boolean isAdversary, boolean isWeary) {
		return "";
	}
	
	private static int rollFeat() {
		return new Random().nextInt(12) + 1;
	}
	
	private static int rollSuccess() {
		return new Random().nextInt(6) + 1;
	}
	
	private static String compileResult(int feat, int[] success, int target, String author, String skill, boolean isAdversary, boolean isWeary) {
		String result = new String();
		int sum = feat;
		boolean isGreatSuccess = false;
		boolean isExtraordinarySuccess = false;
		
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
		
		result.concat(". \n");
		
		if ((isAdversary && feat == 11) || (!isAdversary && feat == 12) || sum >= target)
		{
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
