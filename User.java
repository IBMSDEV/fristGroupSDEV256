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
     public int getUserType() {
         return this.userType;
     }
     public String getTableUserType() {
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
     public int getDBPasswordRest() {
         if (passwordRest) return 1;
         else return 0;
       
    }
	public void setUserType(String newValue) {
        
            if (newValue.equals("Owner")) this.userType = 1;
            else if (newValue.equals("Manger")) this.userType = 2;
            else if (newValue.equals("Sercive Advisor")) this.userType = 3;
            else  this.userType = 4;
           
    }
	
 }
 