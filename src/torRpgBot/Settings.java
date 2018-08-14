package torRpgBot;

import java.util.ArrayList;
import java.util.List;

public class Settings {

	String botToken = "";
	List<CommandFlag> commandFlags;
	
	Settings() {
		commandFlags = new ArrayList<CommandFlag>();
		commandFlags.add(new CommandFlag());
	}
	
	public String toString() {
		String result = "botToken=" + botToken;
		
		for (CommandFlag flag : commandFlags)
		{
			result = result + " " + flag.server + " flag=" + flag.commandFlag;
		}
		return result;
	}
}
