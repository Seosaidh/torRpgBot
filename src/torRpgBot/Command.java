/**
 * <h1>Command Class (Abstract)</h1>
 * This class defines the abstract interface that all commands should adhere to.
 * <p>
 * Each class that handles a command should extend this class and define the getHelp,
 * handleCommand, and getAliases functions.
 * Only the SetConfig command should utilize the setFlag function. In the future, an 
 * attempt will be made to enforce this programmatically.
 * @author Seosaidh
 * @version 1.0
 * @since 0.0.1
 */
package torRpgBot;

import java.util.List;


import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class Command extends ListenerAdapter {
	
	private static String commandFlag;
	
	protected abstract void handleCommand(MessageReceivedEvent event); // This function should handle the command.
	protected abstract String getHelp(); // This function should return the error message in case of a command parse error.
	protected abstract List<String> getAliases(); // This function should return a list of command string aliases sans command flag.
	
	
	public Command (String flag) {
		commandFlag = flag;
	}
	
	public String getFlag() {
		return commandFlag;
	}
	
	protected void setFlag(String flag) {
		commandFlag = flag;
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		
		if (event.getAuthor().isBot() || !event.isFromType(ChannelType.TEXT) ||
				!event.getMessage().getContentDisplay().trim().startsWith(commandFlag))
		{
			return;
		}
		
		if (getAliases().contains(event.getMessage().getContentDisplay().trim().toLowerCase().split(" ")[0].substring(commandFlag.length())))
		{
			handleCommand(event);
		}
	}
	

}
