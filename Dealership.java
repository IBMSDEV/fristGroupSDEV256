import java.io.IOException;
import java.io.PrintWriter;

public class Dealership{
    static public int dealershipIDCount = 0;
    public int dealershipID;
    public String dealerName;
    public String dealerAddress;
    public String fileString = "";
    public int ServiceTechCount = 0;

    //set the information the if supplied 
    Dealership(String name, String address ){
        dealershipIDCount++;
        this.dealershipID = dealershipIDCount;
        this.dealerName = name;
        this.dealerAddress = address;
    }
    //gets the ID
    public int getDealershipID(){
        return this.dealershipID;
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
        
        public int dealershipID;
        public int techID;
        private String techName;
        String fileString = "";

        //set the information the if supplied 
        protected ServiceTech(int dealershipID, String techName ){
            ServiceTechCount++;
            this.dealershipID = dealershipID;
            this.techName = techName;
            this.techID = ServiceTechCount;
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
        
        String formatForFile(){
           return this.techID +"^" + this.techName + "^" + dealershipID + "^\n" ;
        }
        public void toFile(String fileString) throws IOException{
             
            try (PrintWriter output = new PrintWriter("ServiceTechData.txt");) {
                output.write(fileString);
                output.close();
            }
    
        }

    }

}
