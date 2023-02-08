package Helper;

import Models.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.FirstLevelDivision;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class FirstLevelDivisionQuery {

    /**This is the Get All First Level Divisions method.
     * An observable list firstLevelDivisionsObservableList is created.
     * A database statement to select all from the First_Level_Divisions table is created. The database is connected
     * to and statement is executed. A new Division is created including all specified columns. Each new
     * Division is added to the observable list. Observable list firstLevelDivisionsObservableList is returned.
     *
     * @return Returns the observable list firstLevelDivisionsObservableList.
     * @throws Exception Throws an error if failed.
     */
    public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() throws Exception {
        ObservableList<FirstLevelDivision> firstLevelDivisionsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS";
        PreparedStatement ps = ConnectDatabase.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            FirstLevelDivision newDivision = new FirstLevelDivision(
                    rs.getInt("Division_ID"),
                    rs.getString("Division"),
                    rs.getInt("Country_ID"));
            firstLevelDivisionsObservableList.add(newDivision);
        }
        return firstLevelDivisionsObservableList;
    }

    /**This is the Get Divisions by Country method.
     * This creates a variable newcountry that obtains the Country ID by querying the Country name.
     * An observable list named divisions is created. A Select all From First_Level_Divisions statement
     * is created. Statement is executed. All results are added to the observable list. The observable list
     * divisions is returned.
     * @param country A variable that contains the Country.
     * @return Returns the observable list divisions.
     * @throws Exception Throws an error if failed.
     */
    public static ObservableList<FirstLevelDivision> getDivisionsByCountry(String country) throws Exception {
        Country newCountry = CountryQuery.getCountryId(country);

        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();

        String queryStatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID=?;";

        DatabaseQuery.setPreparedStatement(ConnectDatabase.getConnection(), queryStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setInt(1, newCountry.getCountryId());

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {

                FirstLevelDivision newDivision = new FirstLevelDivision(
                        resultSet.getInt("Division_ID"),
                        resultSet.getString("Division"),
                        resultSet.getInt("COUNTRY_ID")
                );
                divisions.add(newDivision);
            }
            return divisions;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**This is the Get Division ID method.
     * This creates a Select all from the first level divisions table that match the specified division name.
     * Statement is executed. Any matches are returned and added as a newDivision. The newDivision variable is
     * returned.
     * @param division A variable that contains the division name.
     * @return Returns any Divisions matching the division name or returns null.
     * @throws Exception Throws an error if failed.
     */
    public static FirstLevelDivision getDivisionId(String division) throws Exception {
        String queryStatement = "SELECT * FROM first_level_divisions WHERE Division=?";

        DatabaseQuery.setPreparedStatement(ConnectDatabase.getConnection(), queryStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, division);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                FirstLevelDivision newDivision = new FirstLevelDivision(
                        resultSet.getInt("Division_ID"),
                        resultSet.getString("Division"),
                        resultSet.getInt("COUNTRY_ID")
                );
                return newDivision;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}


