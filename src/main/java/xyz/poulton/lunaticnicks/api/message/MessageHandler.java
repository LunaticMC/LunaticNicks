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

package xyz.poulton.lunaticnicks.api.message;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.chat.ComponentSerializer;
import java.util.UUID;
import com.google.common.io.ByteArrayDataInput;
import net.md_5.bungee.api.chat.BaseComponent;

public class MessageHandler
{
    private static final BaseComponent[] useUsername;
    
    public static void handleMessage(final ByteArrayDataInput in, final MessageResponder responder) {
        final String action = in.readUTF();
        System.out.println("Message received; action is " + action);
        if (action.equalsIgnoreCase(MessageType.Request.name())) {
            responder.onRequestMessage(new RequestMessage(UUID.fromString(in.readUTF())));
        }
        else if (action.equalsIgnoreCase(MessageType.Update.name())) {
            final UUID uuid = UUID.fromString(in.readUTF());
            final String nickUTF = in.readUTF();
            BaseComponent[] nick;
            if (nickUTF.equals("None")) {
                nick = null;
            }
            else {
                nick = ComponentSerializer.parse(nickUTF);
            }
            responder.onUpdateMessage(new UpdateMessage(uuid, nick));
        }
    }
    
    public static byte[] generateRequestMessage(final RequestMessage message) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LunaticNicks");
        out.writeUTF(MessageType.Request.name());
        out.writeUTF(message.uuid.toString());
        return out.toByteArray();
    }
    
    public static byte[] generateUpdateMessage(final UpdateMessage message) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LunaticNicks");
        out.writeUTF(MessageType.Update.name());
        out.writeUTF(message.uuid.toString());
        if (message.nick == null) {
            out.writeUTF("None");
        }
        else {
            out.writeUTF(ComponentSerializer.toString(message.nick));
        }
        return out.toByteArray();
    }
    
    public static BaseComponent[] getDefault() {
        return MessageHandler.useUsername;
    }
    
    public static String componentToString(final BaseComponent[] components) {
        final StringBuilder tagString = new StringBuilder();
        for (final BaseComponent component : components) {
            tagString.append(component.toLegacyText());
        }
        return tagString.toString();
    }
    
    static {
        useUsername = ComponentSerializer.parse("{\"text\":\"_USEDEFAULT_\"}");
    }
}
