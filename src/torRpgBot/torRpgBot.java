package torRpgBot;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




enum COMMAND {
	ROLL(Arrays.asList(new String[] {"r"})) {
		public String handleCommand(String msg, User author, MessageReceivedEvent event) {
			return TorDice.handleRollCommand(msg, author, false);
		}
	},
	/*WEARY(new String[] {"w"}) {
		public String handleCommand(String msg, User author, MessageReceivedEvent event) {
			return TorDice.handleRollCommand(msg, author, false, true);
		}
	},*/
	ADVERSARY(Arrays.asList(new String[] {"a", "adv"})) {
		public String handleCommand(String msg, User author, MessageReceivedEvent event) {
			return TorDice.handleRollCommand(msg, author, true);
		}
	},
	/*WEARY_ADVERSARY(new String[] {"wa", "wadv", "wearyadv"}) {
		public String handleCommand(String msg, User author, MessageReceivedEvent event) {
			return TorDice.handleRollCommand(msg, author, true, true);
		}
	}*/;
	
	public abstract String handleCommand(String msg, User author, MessageReceivedEvent event);
	
	private List<String> alternates;
	
	COMMAND(List<String> alts) {
		this.alternates = alts;
	}
	
	public List<String> getAlts() {
		return this.alternates;
	}
}

public class torRpgBot extends ListenerAdapter {
	


	public static void main(String[] args) {
        //We construct a builder for a BOT account. If we wanted to use a CLIENT account
        // we would use AccountType.CLIENT
        try
        {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken("NDc0MTc1MTcxNjI4ODkyMTYz.DkPmZQ.But7C2GrQB9nrMxUYaQBBw6ZD54")           //The token of the account that is logging in.
                    .addEventListener(new torRpgBot())  //An instance of a class that will handle events.
                    .buildBlocking();  //There are 2 ways to login, blocking vs async. Blocking guarantees that JDA will be completely loaded.
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
	
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        //These are provided with every event in JDA
        JDA jda = event.getJDA();                       //JDA, the core of the api.
        long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.
        final Logger LOGGER = LogManager.getLogger(torRpgBot.class.getName());
        
        String commandFlag = "!";
        

        if (event.getAuthor().isBot())
        {
        	return;
        }
        
        //Event specific information
        User author = event.getAuthor();                //The user that sent the message
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
                                                        //  This could be a TextChannel, PrivateChannel, or Group!
        
        if (!event.isFromType(ChannelType.TEXT))
        {
        	//Don't handle anything outside of a text channel in a guild (server)
        	return;
        }
        
        Guild guild = event.getGuild();
        TextChannel textChannel = event.getTextChannel(); //The TextChannel that this message was sent to.
        Member member = event.getMember();          //This Member that sent the message. Contains Guild specific information about the User!
        String name;
        if (message.isWebhookMessage())
        {
            name = author.getName();                //If this is a Webhook message, then there is no Member associated
        }                                           // with the User, thus we default to the author for name.
        else
        {
            name = member.getEffectiveName();       //This will either use the Member's nickname if they have one,
        }                                           // otherwise it will default to their username. (User#getName())

        String msg = message.getContentDisplay();              //This returns a human readable version of the Message. Similar to
                                                        // what you would see in the client.
        
        LOGGER.debug("({})[{}]<{}>: {}\n", guild.getName(), textChannel.getName(), name, msg);
        
        msg = msg.trim();
        
        if (msg.isEmpty())
        {
        	return;
        }
        
        if (msg.startsWith(commandFlag))
        {
        	String output = "";
        	boolean processedCommand = false;
        	msg = msg.substring(commandFlag.length());
        	
        	String[] command = msg.split(" ", 2);
        	
        	if (command.length < 2)
        	{
        		command = new String[] {command[0], ""};
        	}
        	
        	LOGGER.debug("The command string is {} and the command body is {}", command[0], command[1]);
        	
        	for(COMMAND c : COMMAND.values())
        	{
        		if (c.name().equalsIgnoreCase(command[0]) || c.getAlts().contains(command[0].toLowerCase()))
        		{
        			LOGGER.debug("Command {} matched COMMAND enum {} (alts: {})", command[0], c.name(), c.getAlts());
        			output = c.handleCommand(command[1], author, event);
        			processedCommand = true;
        			break;
        		}
        	}
        	
        	if (!processedCommand)
        	{
        		output = "ERROR! Invalid command " + command[0];
        	}
        	
        	if (!output.isEmpty())
        	{
        		channel.sendMessage(output).queue();
        	}
        }

    } // End of onMessageReceived function

}
