package Models;

import Helper.CustomerQuery;

/**This class creates a Customer as part of the Customer table.*/
public class Customer {

    private static int customerIDCount = 1;
    private int customerID, divisionID;
    private String customerName, address, postalCode, phoneNumber, division, country;

    /**This constructor sets up the order of the Customer class.
     * All instances of the Customer class will follow this order.*/
    public Customer(int customerID, String customerName, String address, String postalCode,
                    String phoneNumber, String division, String country, int divisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.division = division;
        this.country = country;

    }


    /**This is the accessor for Customer ID. This returns Customer ID as an integer value.*/
    public int getCustomerID() {
        return customerID;
    }

    /**This is the accessor for Customer Name. This returns Customer Name as a string value.*/
    public String getCustomerName() {
        return customerName;
    }

    /**This is the accessor for Address. This returns Address as a string value.*/
    public String getAddress() {
        return address;
    }

    /**This is the accessor for Division ID. This returns Division ID as an integer value.*/
    public int getDivisionID() {
        return divisionID;
    }

    /**This is the accessor for Postal Code. This returns Postal Code as a string value.*/
    public String getPostalCode() {
        return postalCode;
    }

    /**This is the accessor for Phone Number. This returns Phone Number as a string value.*/
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**This is the accessor for Country. This returns Country as a string value.*/
    public String getCountry() {
        return country;
    }

    /**This is the accessor for Division. This returns Division as a string value.*/
    public String getDivision() {
        return division;
    }

    /**This is the mutator for Customer ID. This sets the value of Customer ID as an integer.*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**This is the mutator for Customer Name. This sets the value of Customer Name as a string.*/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**This is the mutator for Address. This sets the value of Address as a string.*/
    public void setAddress(String address) {
        this.address = address;
    }

    /**This is the mutator for Postal Code. This sets the value of Postal Code as a string.*/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**This is the mutator for Phone Number. This sets the value of Phone Number as a string.*/
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**This is the mutator for Division ID. This sets the value of Division ID as an integer.*/
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**This is the mutator for Country. This sets the value of Country as a string.*/
    public void setCountry(String country) {
        this.country = country;
    }

    /**This is the mutator for Division. This sets the value of Division as a string.*/
    public void setDivision(String division) {
        this.division = division;
    }

    /**This is the To String method.
     *
     * @return Returns the String value of Customer ID.
     */
    @Override
    public String toString() {
        return String.valueOf((customerID));
    }

    /**This is the Get Customer ID Count Method.
     * This queries to get all Customers. It then enters a for loop to determine if the variable
     * customerIDCount is less than the current size of all Customers. If it is smaller, The count is
     * increased by 1. Customer ID Count is returned when it is not less than or equal to the size of
     * all Customers.
     * @return Returns the current Customer ID Count.
     * @throws Exception Throws an error if failed.
     */
    public static int getCustomerIDCount() throws Exception {
        CustomerQuery.getCustomers();
        for (int i = 0; i <= CustomerQuery.getCustomers().size(); i++) {
            if (customerIDCount <= CustomerQuery.getCustomers().size()) {
                customerIDCount++;
            }
        }
        return customerIDCount;
    }
}
