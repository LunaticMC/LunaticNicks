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
