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

import java.util.List;
import net.md_5.bungee.chat.ComponentSerializer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Arrays;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.poulton.lunaticnicks.api.message.MessageHandler;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.BaseComponent;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class LunaticNicksPapi extends PlaceholderExpansion
{
    private LunaticNicksSpigot pl;
    
    public LunaticNicksPapi(final LunaticNicksSpigot pl) {
        this.pl = pl;
    }
    
    public String toTabFormat(final BaseComponent[] tags) {
        final StringBuilder output = new StringBuilder();
        for (final BaseComponent tag : tags) {
            if (tag.getColor().name().startsWith("#")) {
                output.append(tag.getColor().name());
            }
            else {
                output.append(tag.getColor().toString());
            }
            if (tag.isBold()) {
                output.append("&l");
            }
            if (tag.isItalic()) {
                output.append("&o");
            }
            if (tag.isUnderlined()) {
                output.append("&n");
            }
            if (tag.isStrikethrough()) {
                output.append("&m");
            }
            if (tag.isObfuscated()) {
                output.append("&k");
            }
            output.append(tag.toPlainText());
        }
        return output.toString();
    }
    
    public boolean persist() {
        return true;
    }
    
    public String getIdentifier() {
        return "lunaticnicks";
    }
    
    public String getAuthor() {
        return this.pl.getDescription().getAuthors().toString();
    }
    
    public String getVersion() {
        return this.pl.getDescription().getVersion();
    }
    
    public String onPlaceholderRequest(final Player player, final String identifier) {
        if (player == null) {
            return "";
        }
        BaseComponent[] nick = this.pl.getNick(player.getUniqueId());
        if (nick == null || nick[0].toPlainText().equals(MessageHandler.getDefault()[0].toPlainText())) {
            nick = new BaseComponent[] { (BaseComponent)new TextComponent(player.getDisplayName()) };
        }
        else {
            final List<BaseComponent> addedStar = new LinkedList<BaseComponent>(Arrays.asList(nick));
            addedStar.add(0, (BaseComponent)new TextComponent("*"));
            nick = addedStar.toArray(new BaseComponent[0]);
        }
        if (identifier.equals("nickname")) {
            return ComponentSerializer.toString(nick);
        }
        if (identifier.equals("nickname_legacy")) {
            return MessageHandler.componentToString(nick);
        }
        if (identifier.equals("nickname_tab")) {
            return this.toTabFormat(nick);
        }
        return null;
    }
}
