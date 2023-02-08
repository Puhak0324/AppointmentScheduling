package Models;

/**This class creates a Contact as part of the Contact table.*/
public class Contacts {
    public int contactID;
    public String contactName;
    public String contactEmail;

    /**This constructor sets up the order of the Contact class.
     * All instances of the Contact class will follow this order.*/
    public Contacts(int contactID, String contactName, String contactEmail){
        this.contactID=contactID;
        this.contactName=contactName;
        this.contactEmail=contactEmail;
    }

    /**This is the accessor for Contact ID. This returns Contact ID as an integer value.*/
    public int getContactID(){
        return contactID;
    }

    /**This is the accessor for Contact Name. This returns Contact name as a string value.*/
    public String getContactName(){
        return contactName;
    }

    /**This is the accessor for Contact Email. This returns Contact Email as a string value.*/
    public String getContactEmail(){
        return contactEmail;
    }

    /**This is the mutator for Contact ID. This sets the value of Contact ID to an integer.*/
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**This is the mutator for Contact Name. This sets the value of Contact Name to a string.*/
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**This is the mutator for Contact Email. This sets the value of Contact Email to a string.*/
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}

