// 
// Decompiled by Procyon v0.5.36
// 

package xyz.poulton.lunaticnicks.bungee.command;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.poulton.lunaticnicks.api.message.MessageHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import xyz.poulton.lunaticnicks.bungee.LunaticNicksBungee;
import net.md_5.bungee.api.plugin.Command;

public class NickCommand extends Command
{
    private final LunaticNicksBungee pl;
    private final int MAX_LENGTH = 48;
    private final int MAX_DISPLAY_LENGTH = 16;
    
    public NickCommand(final LunaticNicksBungee pl) {
        super("nickname", "lunaticnicks.nick", new String[] { "nick" });
        this.pl = pl;
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /nickname <nickname>|off");
            return;
        }
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatColor.RED + "This command cannot be used from the console");
            return;
        }
        if (args[0].equals("off")) {
            this.pl.getDatabaseConnector().setPlayerNick(((ProxiedPlayer)sender).getUniqueId(), MessageHandler.getDefault());
            sender.sendMessage((BaseComponent)new TextComponent(ChatColor.GREEN + "Nickname cleared."));
            this.pl.updateServers(((ProxiedPlayer)sender).getUniqueId());
        }
        else {
            final StringBuilder builder = new StringBuilder();
            for (final String arg : args) {
                builder.append(arg).append(" ");
            }
            builder.setLength(builder.length() - 1);
            String nickString = builder.toString().replaceAll("[^A-Za-z0-9-_!~*#&]", "");
            if (nickString.length() > 48 || ChatColor.stripColor(nickString).length() > 16) {
                sender.sendMessage(ChatColor.RED + "This nick is too long.");
                return;
            }
            if (!sender.hasPermission("lunaticnicks.nick.format")) {
                nickString = ChatColor.stripColor(nickString);
            }
            final BaseComponent[] nick = { (BaseComponent)new TextComponent(ChatColor.translateAlternateColorCodes('&', nickString)) };
            this.pl.getDatabaseConnector().setPlayerNick(((ProxiedPlayer)sender).getUniqueId(), nick);
            this.pl.updateServers(((ProxiedPlayer)sender).getUniqueId());
            sender.sendMessage(ChatColor.GREEN + "Nickname set to " + ChatColor.translateAlternateColorCodes('&', MessageHandler.componentToString(nick)));
        }
    }
}
