// 
// Decompiled by Procyon v0.5.36
// 

package xyz.poulton.lunaticnicks.api.db;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import xyz.poulton.lunaticnicks.api.message.MessageHandler;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import java.util.UUID;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DatabaseConnector
{
    private Connection connection;
    private final String dbInitString;
    
    public DatabaseConnector(final String hostname, final String db, final int port, final String username, final String password) throws SQLException, ClassNotFoundException {
        this.connection = null;
        this.dbInitString = "CREATE TABLE IF NOT EXISTS `" + db + "`.`lunaticnicks` (\n  `uuid` VARCHAR(36) NOT NULL,\n  `nick` TEXT NULL,\n  PRIMARY KEY (`uuid`));";
        this.init(hostname, db, port, username, password);
    }
    
    public void init(final String hostname, final String db, final int port, final String username, final String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + db + "?useSSL=false", username, password);
        this.connection.prepareStatement(this.dbInitString).execute();
    }
    
    public BaseComponent[] getPlayerNick(final UUID uuid) {
        try {
            final PreparedStatement statement = this.connection.prepareStatement("select * from `lunaticnicks` where uuid=?");
            statement.setString(1, uuid.toString());
            final ResultSet set = statement.executeQuery();
            if (set.next()) {
                return ComponentSerializer.parse(set.getString("nick"));
            }
        }
        catch (SQLException ex) {}
        return MessageHandler.getDefault();
    }
    
    public void setPlayerNick(final UUID uuid, final BaseComponent[] nick) {
        try {
            final PreparedStatement statement = this.connection.prepareStatement("insert into `lunaticnicks` (uuid, nick) values (?,?) on duplicate key update nick = ?");
            statement.setString(1, uuid.toString());
            final String serialised = ComponentSerializer.toString(nick);
            statement.setString(2, serialised);
            statement.setString(3, serialised);
            statement.executeUpdate();
        }
        catch (SQLException e) {}
    }
    
    private void close() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        }
        catch (Exception ex) {}
    }
}
