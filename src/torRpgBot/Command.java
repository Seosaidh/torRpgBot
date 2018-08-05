package torRpgBot;

import java.util.List;


import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class Command extends ListenerAdapter {
	
	protected abstract List<String> getAliases();
	private static String commandFlag;
	
	protected abstract void handleCommand(MessageReceivedEvent event); // This function should handle the command.
	protected abstract String getHelp(); // This function should return the error message in case of a command parse error.
	
	
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
