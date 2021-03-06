package game;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database class to load and save game files.
 *
 * @author Addison Chan
 * @version 2.1.17
 */
public class Database {

    private Connection connection;
    private Statement statement;

    /**
     * @param programName name of program (ONE WORD)
     * @throws SQLException
     */
    public Database(String programName) throws SQLException{
        String url = String.format("jdbc:sqlite::resource:res/%s.db", programName);
        this.connection = DriverManager.getConnection(url);
        this.statement = connection.createStatement();
    }

    /**
     * Initializes the table for a given gameName
     * @param gameName name of the game
     * @throws SQLException
     */
    public void initalizeGame(String gameName) throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS " + gameName + " (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	money integer NOT NULL\n"
                + ");";
        statement.execute(sql);
    }

    /**
     * Adds a player to the given game (table)
     * @param gameName name of the game
     * @param name name of player
     * @param money money of player
     * @throws SQLException
     */
    public void addPlayer(String gameName, String name, int money) throws SQLException{
        String sql = "INSERT INTO " + gameName + " (name, money)\n"
            + "VALUES\n"
            + String.format(" ('%s', '%d')", name, money);
        statement.execute(sql);
    }

    /**
     * @param gameName name of the game
     * @param index 1-based index of the player
     * @return name of player
     */
    public String getPlayerName(String gameName, int index) throws SQLException {
        String sql = "SELECT * FROM " + gameName + " WHERE id = " + index + ";";
        ResultSet rs = statement.executeQuery(sql);
        return rs.getString("name");
    }

    /**
     * @param gameName name of the game
     * @param index 1-based index of the player
     * @return amount of money player has
     * @throws SQLException
     */
    public int getPlayerMoney(String gameName, int index) throws SQLException {
        String sql = "SELECT * FROM " + gameName + " where id=" + index + ";";
        ResultSet rs = statement.executeQuery(sql);
        return rs.getInt("money");
    }

    /**
     * @param gameName name of the game
     * @param index index of player
     * @param money money of player
     * @throws SQLException
     */
    public void setPlayerMoney(String gameName, int index, int money) throws SQLException {
        String sql = "UPDATE " + gameName + "\n"
            + "SET money = " + money + "\n"
            + "WHERE id = " + index
            + ";";
        statement.execute(sql);
    }

    /**
     * Makes a table ready by initializing it and clearing it
     * @param gameName
     * @throws SQLException
     */
    public void readyTable(String gameName) throws SQLException {
        this.deleteTable(gameName);
        this.initalizeGame(gameName);
    }

    /**
     * Deletes a table
     * @param gameName
     * @throws SQLException
     */
    public void deleteTable(String gameName) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + gameName;
        statement.execute(sql);
    }

    /**
     * Gets the names of save files from a database
     * @return names of save files in a string[]
     * @throws SQLException
     */
    public String[] getSavefiles() throws SQLException {
        ArrayList<String> output = new ArrayList<String>();
        DatabaseMetaData dbmd = connection.getMetaData();
        String[] types = {"TABLE"};
        ResultSet rs = dbmd.getTables(null, null, "%", types);
        ArrayList<String> tablesToBeDropped = new ArrayList<>();
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            String sql = "SELECT * FROM " + tableName;
            ResultSet results = statement.executeQuery(sql);
            if(results.next()){
                output.add(tableName);
            }
            else {
                tablesToBeDropped.add(tableName);
            }
        }
        for(String str : tablesToBeDropped){
            String sql = "DROP TABLE " + str;
            statement.execute(sql);
        }

        return output.toArray(new String[output.size()]);
    }

    /**
     * Gets players from a save file and then deletes the save file
     * @param savefile name of save file (table)
     * @return list of players
     * @throws SQLException
     */
    public List<Player> getPlayersFromSavefile(String savefile) throws SQLException{
        String sql = "SELECT * FROM " + savefile;
        List<Player> players = new ArrayList<Player>();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            players.add(new Player(rs.getString("name"), rs.getInt("money")));
        }

        sql = "DROP TABLE " + savefile;
        statement.execute(sql);
        return players;
    }
}
