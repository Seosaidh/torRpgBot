package torRpgBot;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;

public class EmoteStrings implements EmoteInterface{
	
	private final Logger LOGGER = LogManager.getLogger(EmoteStrings.class.getName());
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
		
		
	/**
	 * This function will go through the various emojis in the given guild and grab the full string needed for a bot to use
	 * a custom emoji and store it in this class. If an emote name doesn't exist, then it will not be
	 * placed into the EmoteStrings class, and the default will be used.
	 * The string needed to use a custom emote is <:emoji_name:emoji_id>
	 * @param guild The server that the message will be sent to, and so the one needed to retrieve the emoji id from.
	 * @return void
	 */
	public void getEmoteStrings(Guild guild) {
		List<Emote> emotes = guild.getEmotes();
		
		LOGGER.debug("Getting emotes from guild {}.", guild.getName());
	
		for (Emote e : emotes)
		{
			switch (e.getName())
			{
			case "d121":
				this.d121 = "<:d121:" + e.getId()+ ">";
				break;
			case "d122":
				this.d122 = "<:d122:" + e.getId()+ ">";
				break;
			case "d123":
				this.d123 = "<:d123:" + e.getId()+ ">";
				break;
			case "d124":
				this.d124 = "<:d124:" + e.getId()+ ">";
				break;
			case "d125":
				this.d125 = "<:d125:" + e.getId()+ ">";
				break;
			case "d126":
				this.d126 = "<:d126:" + e.getId()+ ">";
				break;
			case "d127":
				this.d127 = "<:d127:" + e.getId()+ ">";
				break;
			case "d128":
				this.d128 = "<:d128:" + e.getId()+ ">";
				break;
			case "d129":
				this.d129 = "<:d129:" + e.getId()+ ">";
				break;
			case "d1210":
				this.d1210 = "<:d1210:" + e.getId()+ ">";
				break;
			case "d1211":
				this.d1211 = "<:d1211:" + e.getId()+ ">";
				break;
			case "d1212":
				this.d1212 = "<:d1212:" + e.getId()+ ">";
				break;
			case "d61":
				this.d61 = "<:d61:" + e.getId()+ ">";
				break;
			case "d62":
				this.d62 = "<:d62:" + e.getId()+ ">";
				break;
			case "d63":
				this.d63 = "<:d63:" + e.getId()+ ">";
				break;
			case "d64":
				this.d64 = "<:d64:" + e.getId()+ ">";
				break;
			case "d65":
				this.d65 = "<:d65:" + e.getId()+ ">";
				break;
			case "d66":
				this.d66 = "<:d66:" + e.getId()+ ">";
				break;
			case "d61weary":
				this.d61weary = "<:d61weary:" + e.getId()+ ">";
				break;
			case "d62weary":
				this.d62weary = "<:d62weary:" + e.getId()+ ">";
				break;
			case "d63weary":
				this.d63weary = "<:d63weary:" + e.getId()+ ">";
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * This function will return the proper string to use for the result of the given success die roll.
	 * @param success The result on the success die
	 * @param isWeary Whether or not the character is weary
	 * @return String to insert into the roll results
	 */
	public String getSuccessString(int success, boolean isWeary) {
		switch(success) {
		case 1:
			if (isWeary)
			{
				return this.d61weary;
			}
			else
			{
				return this.d61;
			}
		case 2:
			if (isWeary)
			{
				return this.d62weary;
			}
			else
			{
				return this.d62;
			}
		case 3:
			if (isWeary)
			{
				return this.d63weary;
			}
			else
			{
				return this.d63;
			}
		case 4:
			return this.d64;
		case 5:
			return this.d65;
		case 6:
			return this.d66;
		default:
				LOGGER.error("Unable to find face for success result of {}", success);
				return "-1";
		}
	}
	
	/**
	 * This function returns the string representation of the given feat result given the emoji ids and whether 
	 * the roll was performed by an adversary.
	 * @param feat The result of the feat die. 0 for the relevant face
	 * @param isAdversary Whether the roll was performed by an adversary. Necessary to interpret the result of 0
	 * @return String to insert into roll results.
	 */
	public String getFeatString(int feat, boolean isAdversary) {
		if (isAdversary && feat == 0)
		{
			return this.d1212;
		}
		else if (!isAdversary && feat == 0)
		{
			return this.d1211;
		}
		else
		{
			switch (feat) {
			case 1:
				return this.d121;
			case 2: 
				return this.d122;
			case 3: 
				return this.d123;
			case 4: 
				return this.d124;
			case 5: 
				return this.d125;
			case 6: 
				return this.d126;
			case 7:
				return this.d127;
			case 8: 
				return this.d128;
			case 9: 
				return this.d129;
			case 10: 
				return this.d1210;
			case 11:
				return this.d1211;
			case 12:
				return this.d1212;
			default:
				LOGGER.error("Unable to find face for feat result of {}", feat);
				return "-1";
					
			}
		}
	}
}
