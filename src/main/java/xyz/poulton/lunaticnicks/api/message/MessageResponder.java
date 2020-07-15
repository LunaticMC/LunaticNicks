// 
// Decompiled by Procyon v0.5.36
// 

package xyz.poulton.lunaticnicks.api.message;

public interface MessageResponder
{
    void onRequestMessage(final RequestMessage p0);
    
    void onUpdateMessage(final UpdateMessage p0);
}
