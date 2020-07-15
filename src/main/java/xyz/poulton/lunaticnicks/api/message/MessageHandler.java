// 
// Decompiled by Procyon v0.5.36
// 

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
