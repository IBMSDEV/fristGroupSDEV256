public class Owner {
    static public int ownerIDCount = 0;
    int ownerID = 0;
    String firstName;
    String lastName;
    String phoneNumer;
    String email;

    Owner(String firstName, String lastName, String phoneNumer, String email){
        ownerIDCount++;
        this.ownerID = ownerIDCount;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumer = phoneNumer;
        this.email = email;
    }
    //getters
    public String getEmail() {
        return this.email;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public int getOwnerID() {
        return this.ownerID;
    }
    public String getTableOwnerID() {
        return Integer.toString(this.ownerID);
    }
    public String getPhoneNumer() {
        return this.phoneNumer;
    }

    //setters
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPhoneNumer(String phoneNumer) {
        this.phoneNumer = phoneNumer;
    }


}