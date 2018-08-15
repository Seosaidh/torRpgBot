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
	
	private static List<CommandFlag> commandFlags;
	
	/**
	 * This is the abstract function that must be defined by the subclasses in order to personalize a response to the event.
	 * @param event to respond to.
	 */
	protected abstract void handleCommand(MessageReceivedEvent event);
	
	/**
	 * This is the abstract function that will return the command string.
	 * @return String containing the commands help documentation.
	 */
	protected abstract String getHelp();

	/**
	 * This abstract function will be defined by the subclass to return a list of strings containing the command strings 
	 * that select the defined command. These aliases for the command string should not include the command flag.
	 * @return A list of strings that are used to select which Command class will respond to an event.
	 */
	protected abstract List<String> getAliases();
	
	
	/**
	 * The class constructor. The only parameter is used to set the command flag.
	 * @param flag A string that is set as the commandFlag member variable.
	 */
	public Command (List<CommandFlag> flags) {
		commandFlags = flags;
	}
	
	/**
	 * This function returns the commandFlag variable, which is used to decide if a message is a command or not.
	 * @return String containing the command flag.
	 */
	public List<CommandFlag> getFlags() {
		return commandFlags;
	}
	
	public String getFlag(String server) {
		String defaultFlag = "!";
		for (CommandFlag cf : commandFlags)
		{
			if (cf.server.equalsIgnoreCase(server))
			{
				return cf.commandFlag;
			}
			else if (cf.server.equalsIgnoreCase("default"))
			{
				defaultFlag = cf.commandFlag;
			}
		}
		return defaultFlag;
	}
	
	/**
	 * This function sets the commandFlag variable. Since the commandFlag variable is static, all subclasses of Command
	 * share a single instance of commandFlag. Only one subclass should actually call this function.
	 * @param flag String used to set the commandFlag member variable.
	 */
	protected void setFlags(List<CommandFlag> flag) {
		commandFlags = flag;
	}
	
	/**
	 * This function overrides the function in ListenerAdapter. It will drop the event if it is from a bot or not a message
	 * sent in a regular server text channel, or if the message does not begin with the commandFlag string.
	 * However, if it is from a text channel and begins with the commandFlag, then it checks to see if the first word
	 * (less the commandFlag) matches any of the strings returned by getAliases (which will be defined by the subclasses).
	 * If there is a match, then the function handleCommand is called. Since handleCommand is abstract and defined by the subclass,
	 * this allows each subclass to not only define their own list of command aliases (via getAliases), but also the behavior
	 * of the command by defining handleCommand.
	 * @param event The MessageReceivedEvent passed by the JDA API from Discord.
	 */
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		String flag = getFlag(event.getGuild().getName());
		if (event.getAuthor().isBot() || !event.isFromType(ChannelType.TEXT) ||
				!event.getMessage().getContentDisplay().trim().startsWith(flag))
		{
			return;
		}
		
		if (getAliases().contains(event.getMessage().getContentDisplay().trim().toLowerCase().split(" ")[0].substring(flag.length())))
		{
			handleCommand(event);
		}
	}
	

}
