package torRpgBot;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ReloadCommand extends Command{
	
	private EmoteInterface emoteProvider;

	public ReloadCommand(List<CommandFlag> flags, EmoteInterface emoteProvider) {
		super(flags);
		this.emoteProvider = emoteProvider;
	}

	@Override
	protected void handleCommand(MessageReceivedEvent event) {
		Guild guild = event.getGuild();
		
		emoteProvider.reloadEmotes(guild);
		
	}

	@Override
	protected String getHelp() {
		return "__**Reload Emotes**__\n" +
				"**Aliases:** reloadEmotes\n" +
				"**Syntax:** command\n" +
				"**Description:** This command will reload the emotes from the current server. Only use if the emotes have changed"
				+ "since the last time the bot was re-started.";
	}

	@Override
	protected List<String> getAliases() {
		List<String> aliases = new ArrayList<String>();
		aliases.add("reloadEmotes");
		aliases.add("reloademotes");
		return aliases;
	}

}
