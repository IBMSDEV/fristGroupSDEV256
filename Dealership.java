import java.io.IOException;
import java.io.PrintWriter;

public class Dealership{
    static public int dealershipIDCount = 0;
    static public int totalTechNumber = 0;
    public int dealershipID;
    public String dealerName;
    public String dealerAddress;
    public String dealerPhoneNumber;
    public String fileString = "";
    public int ServiceTechCount = 0;

    //set the information the if supplied 
    Dealership(String name, String address, String dealerPhoneNumber ){
        dealershipIDCount++;
        this.dealershipID = dealershipIDCount;
        this.dealerName = name;
        this.dealerAddress = address;
        this.dealerPhoneNumber = dealerPhoneNumber;
    }
    //gets the ID
    public int getDealershipID(){
        return this.dealershipID;
    }
    public String getDealerPhoneNumber(){
        return this.dealerPhoneNumber;
    }
    void setDealerPhoneNumber(String dealerPhoneNumber){
        this.dealerPhoneNumber = dealerPhoneNumber;
    }
    public String getDealerName(){
        return this.dealerName;
    }

    void setDealerName(String newName){
        this.dealerName = newName;
    }
  
    public String getDealerAddress(){
        return this.dealerAddress;
    }
    
    void setDealerAddress(String newAddress){
        this.dealerAddress = newAddress;
    }
    String formatForFile(){
        return  dealershipID +"^" + this.dealerName + "^" + this.dealerAddress + "^\n" ;
    }
    public void toFile(String fileString) throws IOException{
        try (PrintWriter output = new PrintWriter("dealerData.txt");) {
            output.write(fileString);
            output.close();
        }

    }
   
    //Service Tech is inside of the dealership class because it lets the more easily be linked together for the information to be shared between them
    public class ServiceTech{
        public int DBtechNum;
        public int dealershipID;
        public int techID;
        private String techName;
        String fileString = "";

        //set the information the if supplied 
        protected ServiceTech(int dealershipID, String techName){
            ServiceTechCount++;
            totalTechNumber++;
            this.dealershipID = dealershipID;
            this.techName = techName;
            this.techID = ServiceTechCount;
            this.DBtechNum = totalTechNumber;
        }
        //gets the ID
        public  int getDealershipID(){
            return this.dealershipID;
        }
        public String getTableDealershipID(){
            return Integer.toString(this.dealershipID);
        }
        //sets the id
        void setDealershipID(int newID){
            this.dealershipID = newID;
        }
        
    
        public String getTechName(){
            return this.techName;
        }
    
        void setTechName(String newName){
            this.techName = newName;
        }
      
        public int getTechId(){
            return this.techID;
        }
        public String getTableTechId(){
            return Integer.toString(this.techID);
        }
        void setTechId(int newTechID){
            this.techID = newTechID;
        }
        
        public int getDBtechNum(){
            return this.DBtechNum;
        }

    }

}
