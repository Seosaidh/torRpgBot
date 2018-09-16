package torRpgBot;

import net.dv8tion.jda.core.entities.Guild;

public interface EmoteInterface {
	public void reloadEmotes(Guild guild);
	public String getSuccessString(int success, boolean isWeary, boolean isAdversary, Guild guild);
	public String getFeatString(int feat, boolean isAdversary, Guild guild);
}
