package Models;

/**This class creates a Country as part of the Country table.*/
public class Country {
    private int countryID;
    private String country;

    /**This constructor sets up the order of the Country class.
     * All instances of the Country class will follow this order.*/
    public Country(int countryID, String country) {
        this.country = country;
        this.countryID = countryID;
    }

    /**This is the accessor for Country. This returns Country as a string value.*/
    public String getCountry() {
        return country;
    }
    /**This is the accessor for Customer ID. This returns Customer ID as an integer value.*/
    public int getCountryId() {
        return  countryID;
    }

    /**This is the mutator for Country. This sets the value of Country as a string.*/
    public void setCountry(String country) {
            this.country = country;
    }

    /**This is the mutator for Country ID. This sets the value of Country ID to an integer.*/
    public void setCountryID(int countryID) {
            this.countryID = countryID;
    }

    /**This is the To String method.
     *
     * @return Returns the String value of Country.
     */
     @Override
     public String toString() {
        return String.valueOf((country));
     }
}

