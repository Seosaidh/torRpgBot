package torRpgBot;

import java.util.Hashtable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;

public class EmoteStrings implements EmoteInterface{
	
	private final Logger LOGGER = LogManager.getLogger(EmoteStrings.class.getName());
	static private Hashtable<String, Hashtable<String, String>> servers = new Hashtable<String, Hashtable<String, String>>();
		
		
	/**
	 * This function will go through the various emojis in the given guild and grab the full string needed for a bot to use
	 * a custom emoji and store it in this class. If an emote name doesn't exist, then it will not be
	 * placed into the EmoteStrings class, and the default will be used.
	 * The string needed to use a custom emote is <:emoji_name:emoji_id>
	 * @param guild The server that the message will be sent to, and so the one needed to retrieve the emoji id from.
	 * @return void
	 */
	private void getEmoteStrings(Guild guild, boolean forceReload) {
		if (servers.containsKey(guild.getId()) && !forceReload)
		{
			return;
		}
		
		LOGGER.debug("Getting emote strings for server {}", guild.getName());
		Hashtable<String, String> emoteTable = new Hashtable<String, String>();
		
		List<Emote> emotes = guild.getEmotes();
		
		LOGGER.debug("Getting emotes from guild {}.", guild.getName());
	
		for (Emote e : emotes)
		{
			if (e.getName().matches("^(a){0,1}d((12)|(6))[1-9]([012]{0,1}(weary){0,1})"))
			{
				LOGGER.debug("Inserting emote string {} for emote {}.", ("<:" + e.getName() + ":" + e.getId() + ">"), e.getName());
				emoteTable.putIfAbsent(e.getName(), "<:" + e.getName() + ":" + e.getId() + ">");
			}
		}
		
		emoteTable.putIfAbsent("d121", "1");
		emoteTable.putIfAbsent("d122", "2");
		emoteTable.putIfAbsent("d123", "3");
		emoteTable.putIfAbsent("d124", "4");
		emoteTable.putIfAbsent("d125", "5");
		emoteTable.putIfAbsent("d126", "6");
		emoteTable.putIfAbsent("d127", "7");
		emoteTable.putIfAbsent("d128", "8");
		emoteTable.putIfAbsent("d129", "9");
		emoteTable.putIfAbsent("d1210", "10");
		emoteTable.putIfAbsent("d1211", "11");
		emoteTable.putIfAbsent("d1212", "12");
		emoteTable.putIfAbsent("d61", "1");
		emoteTable.putIfAbsent("d62", "2");
		emoteTable.putIfAbsent("d63", "3");
		emoteTable.putIfAbsent("d64", "4");
		emoteTable.putIfAbsent("d65", "5");
		emoteTable.putIfAbsent("d66", "6");
		emoteTable.putIfAbsent("d61weary", "1");
		emoteTable.putIfAbsent("d62weary", "2");
		emoteTable.putIfAbsent("d63weary", "3");
		
		emoteTable.putIfAbsent("ad121", emoteTable.get("d121"));
		emoteTable.putIfAbsent("ad122", emoteTable.get("d122"));
		emoteTable.putIfAbsent("ad123", emoteTable.get("d123"));
		emoteTable.putIfAbsent("ad124", emoteTable.get("d124"));
		emoteTable.putIfAbsent("ad125", emoteTable.get("d125"));
		emoteTable.putIfAbsent("ad126", emoteTable.get("d126"));
		emoteTable.putIfAbsent("ad127", emoteTable.get("d127"));
		emoteTable.putIfAbsent("ad128", emoteTable.get("d128"));
		emoteTable.putIfAbsent("ad129", emoteTable.get("d129"));
		emoteTable.putIfAbsent("ad1210", emoteTable.get("d1210"));
		emoteTable.putIfAbsent("ad1211", emoteTable.get("d1211"));
		emoteTable.putIfAbsent("ad1212", emoteTable.get("d1212"));
		emoteTable.putIfAbsent("ad61", emoteTable.get("d61"));
		emoteTable.putIfAbsent("ad62", emoteTable.get("d62"));
		emoteTable.putIfAbsent("ad63", emoteTable.get("d63"));
		emoteTable.putIfAbsent("ad64", emoteTable.get("d64"));
		emoteTable.putIfAbsent("ad65", emoteTable.get("d65"));
		emoteTable.putIfAbsent("ad66", emoteTable.get("d66"));
		emoteTable.putIfAbsent("ad61weary", emoteTable.get("d61weary"));
		emoteTable.putIfAbsent("ad62weary", emoteTable.get("d62weary"));
		emoteTable.putIfAbsent("ad63weary", emoteTable.get("d63weary"));
		
		servers.put(guild.getId(), emoteTable);
	}
	
	/**
	 * This function will return the proper string to use for the result of the given success die roll.
	 * @param success The result on the success die
	 * @param isWeary Whether or not the character is weary
	 * @param isAdversary Whether an adversary is rolling
	 * @param guild The guild to retrieve the string from
	 * @return String to insert into the roll results
	 */
	public String getSuccessString(int success, boolean isWeary, boolean isAdversary, Guild guild) {
		getEmoteStrings(guild, false);
		String key = "";
		
		if (isAdversary)
		{
			key = key.concat("a");
		}
		
		key = key.concat("d6");
		
		key = key.concat(String.valueOf(success));
		
		if (success <= 3 && isWeary)
		{
			key = key.concat("weary");
		}
		
		LOGGER.debug("Getting emote string for success die result of {}. Key is {}", success, key);
		return servers.get(guild.getId()).get(key);
	}
	
	/**
	 * This function returns the string representation of the given feat result given the emoji ids and whether 
	 * the roll was performed by an adversary.
	 * @param feat The result of the feat die. 0 for the relevant face
	 * @param isAdversary Whether the roll was performed by an adversary. Necessary to interpret the result of 0
	 * @param guild The guild requesting the string
	 * @return String to insert into roll results.
	 */
	public String getFeatString(int feat, boolean isAdversary, Guild guild) {
		getEmoteStrings(guild, false);
		String key = "";
		
		if (isAdversary)
		{
			key = key.concat("a");
		}
		
		key = key.concat("d12");
		
		if (feat != 0)
		{
			key = key.concat(String.valueOf(feat));
		}
		else
		{
			if (isAdversary)
			{
				key = key.concat(String.valueOf(12));
			}
			else
			{
				key = key.concat(String.valueOf(11));
			}
		}
		
		LOGGER.debug("Getting emote string for feat die result of {}. Key is {}", feat, key);
		
		return servers.get(guild.getId()).get(key);
	}
	
	public void reloadEmotes(Guild guild)
	{
		getEmoteStrings(guild, true);
	}
}
