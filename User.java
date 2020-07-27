public class User{
    public String userName;
    public int userType;
    public int userID;
    public Boolean passwordRest; 
 
     User(String userName, int userType, int userID){
         this.userName = userName;
         this.userType = userType;
         this.userID = userID;
         this.passwordRest = true;
     }
     User(String userName, int userType, int userID, int passwordRest){
         this.userName = userName;
         this.userType = userType;
         this.userID = userID;
 
         if(passwordRest == 0) this.passwordRest = false;
         else this.passwordRest = true;
     }
     //setters
     public void setUserName(String userName){
         this.userName = userName;
     }
     public void setUserType(int type){
         this.userType = type;
     }
     public void setPasswordRest(Boolean reset){
         this.passwordRest = reset;
     }
 
     //getters
     public String getTableUserID(){
        return Integer.toString(this.userID);
    }
     public int getUserID() {
         return this.userID;
     }
     public String getUserName() {
         return this.userName;
     }

     public String getUserType() {
        switch(this.userType) {
            case 0:
                return "Admin";
           
            case 1:
                return "Owner";
            
            case 2:
                return "Manger";
            
            case 3:
                return "Sercive Advisor";
            
            case 4:
                return "Sercive Tech";
            default:
                return "?";
        }
     }
     public Boolean getPasswordRest() {
         return this.passwordRest;
     }
 }
 