package ws.nmathe.saber.core.command;

import ws.nmathe.saber.commands.Command;
import ws.nmathe.saber.commands.admin.*;
import ws.nmathe.saber.utils.MessageUtilities;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import ws.nmathe.saber.commands.general.*;
import ws.nmathe.saber.utils.__out;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles MessageEvents which contain user commands
 */
public class CommandHandler
{
    private final CommandParser commandParser = new CommandParser();      // parses command strings into containers
    private final ExecutorService executor = Executors.newCachedThreadPool(); // thread pool for running commands
    private HashMap<String, Command> commands;         // maps Command to invoke string
    private HashMap<String, Command> adminCommands;    // ^^ but for admin commands

    public CommandHandler()
    {
        commands = new HashMap<>();
        adminCommands = new HashMap<>();
    }

    /**
     * Loads all commands into the command table
     */
    public void init()
    {
        // add bot commands with their lookup name
        commands.put("init", new InitCommand());
        commands.put("create", new CreateCommand());
        commands.put("delete", new DeleteCommand());
        commands.put("edit", new EditCommand());
        commands.put("help", new HelpCommand());
        commands.put("config", new ConfigCommand());
        commands.put("zones", new TimeZonesCommand());

        // add administrator commands with their lookup name
        adminCommands.put("announcement", new GlobalMsgCommand());
        adminCommands.put("stats", new StatsCommand());
        adminCommands.put("reload", new ReloadSettingsCommand());
    }

    public void handleCommand( MessageReceivedEvent event, Integer type )
    {
        CommandParser.CommandContainer cc = commandParser.parse( event );
        if( type == 0 )
        {
            handleGeneralCommand( cc );
        }
        else if( type == 1 )
        {
            handleAdminCommand( cc );
        }

    }

    private void handleGeneralCommand(CommandParser.CommandContainer cc)
    {
        // if the invoking command appears in commands
        if(commands.containsKey(cc.invoke))
        {
            String err = commands.get(cc.invoke).verify(cc.args, cc.event);

            // do command action if valid arguments
            if(err.isEmpty())
            {
                executor.submit( () ->
                {
                    try
                    {
                        commands.get(cc.invoke).action(cc.args, cc.event);
                    }
                    catch(Exception e)
                    {
                        __out.printOut(this.getClass(), e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                });
            }
            // otherwise send error message
            else
            {
                String msg = "Error : " + err;
                MessageUtilities.sendMsg( msg, cc.event.getChannel(), null );
            }
        }
        // else the invoking command is invalid
        else
        {
            String msg = "``" + cc.invoke + "`` is not a command!";
            MessageUtilities.sendMsg( msg, cc.event.getChannel(), null );
        }
    }

    private void handleAdminCommand(CommandParser.CommandContainer cc)
    {
        // for admin commands
        if(adminCommands.containsKey(cc.invoke))
        {
            String err = adminCommands.get(cc.invoke).verify(cc.args, cc.event);

            // do command action if valid arguments
            if (err.equals(""))
            {
                executor.submit( () ->
                {
                    try
                    {
                        adminCommands.get(cc.invoke).action(cc.args, cc.event);
                    }
                    catch(Exception e)
                    {
                        __out.printOut(e.getClass(), e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public Collection<Command> getCommands()
    {
        return commands.values();
    }

    public Command getCommand( String invoke )
    {
        // check if command exists, if so return it
        if( commands.containsKey(invoke) )
            return commands.get(invoke);

        else    // otherwise return null
            return null;
    }

    public void putSync() {
        commands.put("sync", new SyncCommand());
    }
}
