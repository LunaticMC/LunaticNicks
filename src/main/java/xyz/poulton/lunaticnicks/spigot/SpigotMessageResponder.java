// 
// Decompiled by Procyon v0.5.36
// 

package xyz.poulton.lunaticnicks.spigot;

import xyz.poulton.lunaticnicks.api.message.UpdateMessage;
import xyz.poulton.lunaticnicks.api.message.RequestMessage;
import xyz.poulton.lunaticnicks.api.message.MessageResponder;

public class SpigotMessageResponder implements MessageResponder
{
    LunaticNicksSpigot pl;
    
    public SpigotMessageResponder(final LunaticNicksSpigot pl) {
        this.pl = pl;
    }
    
    @Override
    public void onRequestMessage(final RequestMessage message) {
    }
    
    @Override
    public void onUpdateMessage(final UpdateMessage message) {
        this.pl.nickCache.remove(message.uuid);
        this.pl.nickCache.put(message.uuid.toString(), message.nick);
    }
}
