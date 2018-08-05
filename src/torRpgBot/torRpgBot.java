package torRpgBot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;


import javax.security.auth.login.LoginException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class torRpgBot {
	


	public static void main(String[] args) {
		
		// Insert configuration loading here.
		String flag = "!";
		
        try
        {
        	JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT)
                    .setToken("NDc0MTc1MTcxNjI4ODkyMTYz.DkaAQQ.3tOaHY2kkArqJ0KlyjhN5i9hP6U");//The token of the account that is logging in.

            // Insert command registration here. Each command class needs to be registered by calling
            // jdaBuilder.addEventListener(new CommandExtension(flag));
        	jdaBuilder.addEventListener(new RollCommand(flag));
        	jdaBuilder.addEventListener(new AdversaryCommand(flag));
        	jdaBuilder.addEventListener(new Test());
            
            JDA jda = jdaBuilder.buildBlocking();
            
            final Logger LOGGER = LogManager.getLogger(torRpgBot.class.getName());
            List<Emote> emotes = jda.getEmotes();
            
            for (Emote e : emotes)
            {
            	LOGGER.debug("Emote name {}, id {} id-long {}", e.getName(), e.getId(), e.getIdLong());
            }
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
