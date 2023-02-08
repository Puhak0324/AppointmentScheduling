package Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**This class controls the Database Query and contains methods pertaining to.*/
public class DatabaseQuery {

    /**This sets PreparedStatement variable named statement.*/
    private static PreparedStatement statement;

    /**This is the Set Prepared Statement method.
     * sets the Prepared Statement(statement) variable.
     * @param connection A variable that contains the Connection.
     * @param sqlStatement  variable that contains SQL Statement.
     * @throws Exception Throws an error if failed.
     */
    public static void setPreparedStatement(Connection connection, String sqlStatement) throws Exception{
        statement = connection.prepareStatement(sqlStatement);
    }

    /**This is accessor for Prepared Statement.*/
    public static PreparedStatement getPreparedStatement(){
        return statement;
    }
}

