//    Copyright © Lucy Poulton 2020.
//    This file is part of LunaticNicks.
//
//    LunaticNicks is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    LunaticNicks is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with LunaticNicks. If not, see <https://www.gnu.org/licenses/>.

package xyz.poulton.lunaticnicks.spigot;

import com.google.common.io.ByteArrayDataInput;
import xyz.poulton.lunaticnicks.api.message.MessageResponder;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import xyz.poulton.lunaticnicks.api.message.MessageHandler;
import xyz.poulton.lunaticnicks.api.message.RequestMessage;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import java.util.HashMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaticNicksSpigot extends JavaPlugin implements PluginMessageListener, Listener
{
    private final SpigotMessageResponder responder;
    HashMap<String, BaseComponent[]> nickCache;
    
    public LunaticNicksSpigot() {
        this.responder = new SpigotMessageResponder(this);
        this.nickCache = new HashMap<String, BaseComponent[]>();
    }
    
    public BaseComponent[] getNick(final UUID uuid) {
        return this.nickCache.getOrDefault(uuid.toString(), null);
    }
    
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "BungeeCord", (PluginMessageListener)this);
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        new LunaticNicksPapi(this).register();
    }
    
    public void onDisable() {
    }
    
    @EventHandler
    public void on(final PlayerJoinEvent e) {
        if (this.getNick(e.getPlayer().getUniqueId()) == null) {
            final RequestMessage msg = new RequestMessage(e.getPlayer().getUniqueId());
            e.getPlayer().sendPluginMessage((Plugin)this, "BungeeCord", MessageHandler.generateRequestMessage(msg));
        }
    }
    
    public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        final ByteArrayDataInput in = ByteStreams.newDataInput(message);
        if (!in.readUTF().equals("LunaticNicks")) {
            return;
        }
        MessageHandler.handleMessage(in, this.responder);
    }
}
