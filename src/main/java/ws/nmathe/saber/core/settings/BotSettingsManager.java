package ws.nmathe.saber.core.settings;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import ws.nmathe.saber.utils.Logging;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * contains configurable variables for the bot
 * file should be auto-generated with required fields if the file is found
 * not to exist.
 */
public class BotSettingsManager
{
    private static final String FILENAME = "saber.toml";
    private BotSettings settings;

    /**
     * attempts to read the settings file,
     * on failure, generate a new default file
     */
    public BotSettingsManager()
    {
        InputStream input = null;
        try
        {
            input = new FileInputStream("./" + FILENAME);
            settings = (new Toml()).read(input).to(BotSettings.class);
        }
        catch (IOException ex)
        {
            this.generateFile();
            settings = null;
        }
        finally
        {
            if (input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * write out to file a new toml file with default settings
     */
    private void generateFile()
    {
        OutputStream output = null;
        try
        {
            output = new FileOutputStream(FILENAME);
            (new TomlWriter()).write(new BotSettings(), output);
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
        catch (Exception e)
        {
            Logging.exception(this.getClass(), e);
        }
        finally
        {
            if (output != null)
            {
                try
                {
                    output.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * reload settings from toml file
     */
    public void reloadSettings()
    {
        InputStream input = null;
        try
        {
            input = new FileInputStream("./" + FILENAME);
            settings = (new Toml()).read(input).to(BotSettings.class);
        }
        catch (Exception e)
        {
            Logging.exception(this.getClass(), e);
        }
        finally
        {
            if (input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Class to store bot settings information
     */
    private class BotSettings
    {
        String discord_token;
        String web_token;
        String mongodb;
        String prefix;
        String admin_prefix;
        String admin_id;
        String bot_control_chan;
        int max_entries;
        int max_schedules;
        String default_announce_chan;
        String default_announce_msg;
        String default_clock_format;
        String default_time_zone;
        List<String> nowplaying_list;
        Set<String> blacklist;
        long cooldown_threshold;
        String rsvp_yes;
        String rsvp_no;
        String rsvp_clear;
        String google_service_key;
        String google_oauth_secret;
        int log_level;
        List<Integer> shards;
        int shard_total;

        BotSettings()
        {
            discord_token = "BOT_TOKEN";
            web_token = null;
            google_service_key = "./saber-g-id.json";
            google_oauth_secret = "./oath2-secret";
            mongodb = "mongodb://localhost:27017/?w=majority";
            log_level = 4;

            shards = new ArrayList<>();
            shard_total = 0;

            prefix = "!";
            admin_prefix = "s.";
            admin_id = "ADMIN_USER_ID";
            max_entries = 25;
            max_schedules = 5;
            bot_control_chan = "saber_control";

            default_announce_chan = "general";
            default_time_zone = "America/New_York";
            default_clock_format = "12";
            default_announce_msg = "Event %a: ``%t``";

            nowplaying_list = new ArrayList<>();
            blacklist = new HashSet<>();
            cooldown_threshold = 1000;

            rsvp_yes = "\u2705";
            rsvp_no = "\u274c";
            rsvp_clear = "\u2754";
        }
    }

    public boolean hasSettings()
    {
        return settings == null;
    }

    public String getToken()
    {
        return settings.discord_token;
    }

    public String getWebToken()
    {
        return settings.web_token;
    }

    public String getAdminId()
    {
        return settings.admin_id;
    }

    public int getMaxEntries()
    {
        return settings.max_entries;
    }

    public String getCommandPrefix()
    {
        return settings.prefix;
    }

    public String getAdminPrefix()
    {
        return settings.admin_prefix;
    }

    public String getAnnounceChan()
    {
        return settings.default_announce_chan;
    }

    public String getControlChan()
    {
        return settings.bot_control_chan;
    }

    public String getAnnounceFormat()
    {
        return settings.default_announce_msg;
    }

    public String getClockFormat()
    {
        return settings.default_clock_format;
    }

    public String getTimeZone()
    {
        return settings.default_time_zone;
    }

    public String getMongoURI()
    {
        return settings.mongodb;
    }

    public List<String> getNowPlayingList()
    {
        return settings.nowplaying_list;
    }

    public Set<String> getBlackList()
    {
        return settings.blacklist;
    }

    public long getCooldownThreshold()
    {
        return settings.cooldown_threshold;
    }

    public int getMaxSchedules()
    {
        return settings.max_schedules;
    }

    public String getYesEmoji()
    {
        return settings.rsvp_yes;
    }

    public String getNoEmoji()
    {
        return settings.rsvp_no;
    }

    public String getClearEmoji()
    {
        return settings.rsvp_clear;
    }

    public String getGoogleServiceKey()
    {
        return settings.google_service_key;
    }

    public String getGoogleOAuthSecret()
    {
        return settings.google_oauth_secret;
    }

    public int getLogLevel()
    {
        return settings.log_level;
    }

    public List<Integer> getShards()
    {
        return settings.shards;
    }

    public int getShardTotal()
    {
        return settings.shard_total;
    }
}
