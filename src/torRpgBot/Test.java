package torRpgBot;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Test extends Command{
	
	private final Logger LOGGER = LogManager.getLogger(torRpgBot.class.getName());
	public Test()
	{
		super("!");
	}

	@Override
	protected List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("t");
		aliases.add("test");
		return aliases;
	}

	@Override
	protected void handleCommand(MessageReceivedEvent event) {
		String result;
		String command = event.getMessage().getContentDisplay().trim();
		Guild guild = event.getGuild();
		User author = event.getAuthor();
		MessageChannel channel = event.getChannel();
	
		result = "(" + guild.getName() +")[" + channel.getName() + "]<" + author.getName() + ">: " + command + "\n";
		
    	if (!result.isEmpty())
    	{
    		LOGGER.debug("Sending message {} to channel {}", result, channel.getName());
    		channel.sendMessage(result).queue();
    	}
		
	}

	@Override
	protected String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}

}
