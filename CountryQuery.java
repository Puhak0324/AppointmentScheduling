package Helper;

import Models.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**This is the Country Query class. This contains all methods pertaining to the Country Table.*/
public class CountryQuery {

    /**This is the Get All Countries method.
     * It creates an observable list called CountriesObservableList, then creates a Select All statement
     * from Countries table. The Select statement is executed and all columns/rows are returned and added
     * to the observable list. Observable list CountriesObservableList is returned.
     * @return Returns the CountriesObservableList
     * @throws SQLException Throws an error if failed.
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> CountriesObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM COUNTRIES";
        PreparedStatement ps = ConnectDatabase.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Country newCountry = new Country(
                    rs.getInt("Country_ID"),
                    rs.getString("Country"));
                    CountriesObservableList.add(newCountry);
        }
        return CountriesObservableList;
    }

    /**This is the Get Country ID method.
     * This creates a Select statement based off Country. Statement is executed and added to a new Country model.
     * New Country model is returned if a matching Country is found. If not, null is returned.
     * @param country  A variable that contains the Country.
     * @return Returns the Country ID or nothing.
     * @throws Exception Throws an error if failed.
     */
    public static Country getCountryId(String country) throws Exception {

        String queryStatement = "SELECT * FROM countries WHERE Country=?";

        DatabaseQuery.setPreparedStatement(ConnectDatabase.getConnection(), queryStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, country);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Country newCountry = new Country(
                        resultSet.getInt("Country_ID"),
                        resultSet.getString("Country")
                );
                return newCountry;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
