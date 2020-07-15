// 
// Decompiled by Procyon v0.5.36
// 

package xyz.poulton.lunaticnicks.api.message;

import net.md_5.bungee.api.chat.BaseComponent;
import java.util.UUID;

public class UpdateMessage
{
    public UUID uuid;
    public BaseComponent[] nick;
    
    public UpdateMessage(final UUID uuid, final BaseComponent[] nick) {
        this.uuid = uuid;
        this.nick = nick;
    }
}
