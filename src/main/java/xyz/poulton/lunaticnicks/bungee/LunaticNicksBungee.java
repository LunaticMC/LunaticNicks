// 
// Decompiled by Procyon v0.5.36
// 

package xyz.poulton.lunaticnicks.bungee;

import net.md_5.bungee.event.EventHandler;
import com.google.common.io.ByteArrayDataInput;
import xyz.poulton.lunaticnicks.api.message.MessageResponder;
import xyz.poulton.lunaticnicks.api.message.MessageHandler;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.event.PluginMessageEvent;
import xyz.poulton.lunaticnicks.api.message.RequestMessage;
import java.util.UUID;
import net.md_5.bungee.config.Configuration;
import java.io.InputStream;
import java.sql.SQLException;
import java.io.IOException;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import java.io.FileOutputStream;
import java.io.File;
import net.md_5.bungee.api.plugin.Command;
import xyz.poulton.lunaticnicks.bungee.command.NickCommand;
import xyz.poulton.lunaticnicks.api.db.DatabaseConnector;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class LunaticNicksBungee extends Plugin implements Listener
{
    private DatabaseConnector db;
    private BungeeMessageResponder responder;
    
    public void onEnable() {
        this.getProxy().registerChannel("BungeeCord");
        this.getProxy().getPluginManager().registerListener((Plugin)this, (Listener)this);
        this.getProxy().getPluginManager().registerCommand((Plugin)this, (Command)new NickCommand(this));
        try {
            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdir();
            }
            final File configFile = new File(this.getDataFolder(), "config.yml");
            if (configFile.createNewFile()) {
                final InputStream defaultCfg = this.getResourceAsStream("bungeeconfig.yml");
                final byte[] buffer = new byte[defaultCfg.available()];
                defaultCfg.read(buffer);
                final FileOutputStream out = new FileOutputStream(configFile);
                out.write(buffer);
                out.close();
                defaultCfg.close();
            }
            final Configuration config = ConfigurationProvider.getProvider((Class)YamlConfiguration.class).load(configFile);
            this.db = new DatabaseConnector(config.getString("mysql.hostname"), config.getString("mysql.database"), config.getInt("mysql.port"), config.getString("mysql.username"), config.getString("mysql.password"));
        }
        catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.responder = new BungeeMessageResponder(this, this.db);
    }
    
    public void updateServers(final UUID uuid) {
        this.responder.onRequestMessage(new RequestMessage(uuid));
    }
    
    @EventHandler
    public void onPluginMessageReceived(final PluginMessageEvent event) {
        final ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        this.getProxy().getLogger().info("Received message");
        if (!event.getTag().equals("BungeeCord")) {
            return;
        }
        if (!in.readUTF().equals("LunaticNicks")) {
            return;
        }
        this.getProxy().getLogger().info("Handling message");
        MessageHandler.handleMessage(in, this.responder);
    }
    
    public DatabaseConnector getDatabaseConnector() {
        return this.db;
    }
}
