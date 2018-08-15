/**
 * <h1>HelpCommand</h1>
 * This class extends {@link torRpgBot.Command Command}.
 * <br>
 * This class simply implements the abstract methods {@link torRpgBot.Command#getAliases() getAliases},
 * {@link torRpgBot.Command#handleCommand(MessageReceivedEvent event) handleCommand},
 * and {@link torRpgBot.Command#getHelp() getHelp}.
 * <br
 * This class implements a help command that will send a private message to whoever issues the command. This PM
 * will contain the getHelp outputs of all the registered classes.
 * @author Seosaidh
 * @version 1.1
 * @since 0.0.1
 */
package torRpgBot;

import java.util.LinkedList;
import java.util.List;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommand extends Command {
	
	List<Command> commandObjects = new LinkedList<Command>();

	public HelpCommand(List<CommandFlag> flag) {
		super(flag);
	}
	
	/**
	 * This command is used to register a Command class so that the HelpCommand class can get to it's help string.
	 * @param command to register with HelpCommand
	 * @return The command so that you can pass it through this function easily
	 */
	public Command registerCommand(Command command) {
		commandObjects.add(command);
		return command;
	}
	
	/**
	 * This function sends a private message to the given user. The message body is given as the second parameter.
	 * @param user The user to send a private message to.
	 * @param message The message to send.
	 */
	private void sendPM(User user, String message) {
		user.openPrivateChannel().queue( (channel) -> channel.sendMessage(message).queue() );
	}

	/**
	 * Override of the abstract function declared in Command. Used to handle the help command.
	 */
	@Override
	protected void handleCommand(MessageReceivedEvent event) {
	
		sendPM(event.getAuthor(), this.getHelp());
		
		for (Command c : commandObjects)
		{
			sendPM(event.getAuthor(), "\n\n" + c.getHelp());
		}
	}

	/**
	 * Override of the Command getHelp. This particular getHelp simply gives a short intro about torRpgBot.
	 */
	@Override
	protected String getHelp() {
		return "__**TOR RPG Bot Help**__\n" +
				"This bot provides various functionality relating to playing The One Ring role-playing game on Discord.\n" +
				"The primary service that this bot provides is automated dice rolls and result calculation. " +
				"The available commands are listed below.";
	}

	/**
	 * This function simply returns the aliases for the help command: "h" and "help".
	 * @return A List of Strings containing the possible aliases for the command
	 */
	@Override
	protected List<String> getAliases() {
		List<String> result = new LinkedList<String>();
		result.add("h");
		result.add("help");
		return result;
	}

}
