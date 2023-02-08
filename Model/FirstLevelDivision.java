package Models;

import java.lang.constant.Constable;
import java.time.LocalDateTime;

/**This class creates a division as part of the First Level Division table.*/
public class FirstLevelDivision {
    private int divisionID;
    private String division;
    public int country_ID;

    /**This constructor sets up the order of the First Level Division class.
     * All instances of the Division class will follow this order.*/
    public FirstLevelDivision(int divisionID, String division, int country_ID){
        this.divisionID=divisionID;
        this.division=division;
        this.country_ID=country_ID;
    }

    /**This is the accessor for Division ID. This returns Division ID as an integer value.*/
    public int getDivisionID(){
        return divisionID;
    }

    /**This is the accessor for Division. This returns Division as a string value.*/
    public String getDivision(){
        return division;
    }

    /**This is the accessor for Country ID. This returns Country ID as an integer value.*/
    public int getCountry_ID(){
        return country_ID;
    }

    /**This is the mutator for Division ID. This sets the value of Division ID as an integer.*/
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**This is the mutator for Country ID. This sets the value of Country ID as an integer.*/
    public void setCountry_ID(int country_ID) {
        this.country_ID = country_ID;
    }

    /**This is the mutator for Division. This sets the value of Division as a string.*/
    public void setDivision(String division) {
        this.division = division;
    }

    /**This is the To String method.
     *
     * @return Returns the string value of Division ID and Division Name.
     */
    @Override
    public String toString(){
        return (divisionID + " " + division);
    }


}
