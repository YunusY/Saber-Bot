package ws.nmathe.saber.commands.admin;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import ws.nmathe.saber.Main;
import ws.nmathe.saber.commands.Command;
import ws.nmathe.saber.commands.CommandInfo;
import ws.nmathe.saber.utils.MessageUtilities;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * retrieves bot stats for the admin
 */
public class StatsCommand implements Command
{
    @Override
    public String name()
    {
        return "stats";
    }

    @Override
    public CommandInfo info(String prefix)
    {
        return null;
    }

    @Override
    public String verify(String prefix, String[] args, MessageReceivedEvent event)
    {
        return "";
    }

    @Override
    public void action(String prefix, String[] args, MessageReceivedEvent event)
    {
        JDA.ShardInfo info = event.getJDA().getShardInfo();
        Runtime rt = Runtime.getRuntime();
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();

        String msg = "```python\n" +
                "\"Database\"\n" +
                "      Entries: " + Main.getDBDriver().getEventCollection().count() + "\n" +
                "    Schedules: " + Main.getDBDriver().getScheduleCollection().count() + "\n" +
                "       Guilds: " + Main.getDBDriver().getGuildCollection().count() + "\n" +
                "\n\"Shard\"\n" +
                "      ShardId: " + info.getShardId() + "/" + info.getShardTotal() + "\n" +
                "       Guilds: " + event.getJDA().getGuilds().size() + "\n" +
                "        Users: " + event.getJDA().getUsers().size() + "\n" +
                "ResponseTotal: " + event.getJDA().getResponseTotal() + "\n" +
                "\n\"Application\"\n" +
                " Memory-total: " +rt.totalMemory()/1024/1024 + " MB\n" +
                "       -free : " + rt.freeMemory()/1024/1024 + " MB\n" +
                "       -max  : " + rt.maxMemory()/1024/1024 + " MB\n" +
                "      Threads: " + Thread.activeCount() + "\n" +
                "       Uptime: " + rb.getUptime()/1000/60 + " minute(s)" +
                "```";

        if(event.isFromType(ChannelType.PRIVATE))
        {
            MessageUtilities.sendPrivateMsg( msg, event.getAuthor(), null );
        }
        else
        {
            MessageUtilities.sendMsg( msg, event.getTextChannel(), null );
        }
    }
}
