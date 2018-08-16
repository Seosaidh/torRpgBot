package torRpgBot;

import net.dv8tion.jda.core.entities.Guild;

public interface EmoteInterface {

	public void getEmoteStrings(Guild guild);
	public String getSuccessString(int success, boolean isWeary);
	public String getFeatString(int feat, boolean isAdversary);
}
