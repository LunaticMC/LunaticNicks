// 
// Decompiled by Procyon v0.5.36
// 

package xyz.poulton.lunaticnicks.bungee;

import java.util.Iterator;
import net.md_5.bungee.api.config.ServerInfo;
import xyz.poulton.lunaticnicks.api.message.MessageHandler;
import xyz.poulton.lunaticnicks.api.message.UpdateMessage;
import xyz.poulton.lunaticnicks.api.message.RequestMessage;
import xyz.poulton.lunaticnicks.api.db.DatabaseConnector;
import xyz.poulton.lunaticnicks.api.message.MessageResponder;

public class BungeeMessageResponder implements MessageResponder
{
    private DatabaseConnector db;
    private LunaticNicksBungee pl;
    
    public BungeeMessageResponder(final LunaticNicksBungee pl, final DatabaseConnector db) {
        this.db = db;
        this.pl = pl;
    }
    
    @Override
    public void onRequestMessage(final RequestMessage message) {
        final byte[] byteMessage = MessageHandler.generateUpdateMessage(new UpdateMessage(message.uuid, this.db.getPlayerNick(message.uuid)));
        for (final ServerInfo server : this.pl.getProxy().getServers().values()) {
            server.sendData("BungeeCord", byteMessage);
        }
    }
    
    @Override
    public void onUpdateMessage(final UpdateMessage message) {
    }
}
