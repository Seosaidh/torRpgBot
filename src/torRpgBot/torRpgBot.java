/**
 * <h1>torRpgBot</h1>
 * This is the root class for the torRpgBot package, containing the main function.
 * The main function simply creates a JDABuilder object, registers all the command listeners,
 * and then builds the JDA object. In the future, this function will also call the configuration
 * singleton class to load configuration from a file.
 * @author Seosaidh
 * @version 1.0
 * @since 0.0.1
 */
package torRpgBot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import javax.security.auth.login.LoginException;



public class torRpgBot {
	


	public static void main(String[] args) {
		
		DiceProvider dice = new DiceProvider();
		SettingsManager settingsManager = new SettingsManager("torRpgBotSettings.json");
		EmoteStrings emoteProvider = new EmoteStrings();
		EmoteInterface emoteRollProvider = emoteProvider;
		EmoteInterface emoteAdversaryProvider = emoteProvider;
		EmoteInterface emoteFreeProvider = emoteProvider;
		
		settingsManager.loadSettings();
		
        try
        {
        	JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT)
                    .setToken(settingsManager.getSettings().botToken);
        	
        	HelpCommand helpCommand = new HelpCommand(settingsManager.getSettings().commandFlags);

            // Insert command registration here. Each command class needs to be registered by calling
            // jdaBuilder.addEventListener(helpCommand.registerCommand(new CommandExtension(flag)));
        	jdaBuilder.addEventListener(helpCommand);
        	jdaBuilder.addEventListener(helpCommand.registerCommand(new RollCommand(settingsManager.getSettings().commandFlags, dice, emoteRollProvider)));
        	jdaBuilder.addEventListener(helpCommand.registerCommand(new AdversaryCommand(settingsManager.getSettings().commandFlags, dice, emoteAdversaryProvider)));
        	jdaBuilder.addEventListener(helpCommand.registerCommand(new SetFlag(settingsManager.getSettings().commandFlags, settingsManager)));
        	jdaBuilder.addEventListener(helpCommand.registerCommand(new FreeDice(settingsManager.getSettings().commandFlags, emoteFreeProvider, dice)));
        	jdaBuilder.addEventListener(helpCommand.registerCommand(new ReloadCommand(settingsManager.getSettings().commandFlags, emoteProvider)));
        	
            @SuppressWarnings("unused")
			JDA jda = jdaBuilder.buildBlocking();
            
        }
        catch (LoginException e)
        {
            //If anything goes wrong in terms of authentication, this is the exception that will represent it
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            //Due to the fact that buildBlocking is a blocking method, one which waits until JDA is fully loaded,
            // the waiting can be interrupted. This is the exception that would fire in that situation.
            //As a note: in this extremely simplified example this will never occur. In fact, this will never occur unless
            // you use buildBlocking in a thread that has the possibility of being interrupted (async thread usage and interrupts)
            e.printStackTrace();
        }
        


	}

}
