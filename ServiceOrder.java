import java.io.IOException;
import java.io.PrintWriter;


public class ServiceOrder{
    public String carVIN;
    String serviceDesc;
    String partsUsed;
    int techID;
    int dealershipID;
    double partsCost;
    double totalCost;
    double laborHours;
    String fileString = "";
    //A empty service order with just the vin, techID, and dealershipID for cars that just came in so not much is know
    ServiceOrder(String carVIN, int techID, int dealershipID){
        this.carVIN = carVIN;
        this.techID = techID;
        this.dealershipID = dealershipID;
        this.serviceDesc = "None";
        this.partsUsed = "None";
        this.partsCost = 0;
        this.totalCost = 0;
        this.laborHours = 0;
    }
    // a service order for when a car is leaving the service department
    ServiceOrder(String carVIN, String serviceDesc, String partsUsed, int techID, int dealershipID, double partsCost, double totalCost, double laborHours){
        this.carVIN = carVIN;
        this.serviceDesc = serviceDesc;
        this.partsUsed = partsUsed;
        this.techID = techID;
        this.dealershipID = dealershipID;
        this.partsCost = partsCost;
        this.totalCost = totalCost;
        this.laborHours = laborHours;
    }
    //getters
    public String getCarVin(){
        return this.carVIN;
    }
    public String getServiceDesc(){
        return this.serviceDesc;
    }
    public String getPartsUsed(){
        return this.partsUsed;
    }
    public int getTechID(){
        return this.techID;
    }
    public int getDealershipID(){
        return this.dealershipID;
    }

    public double getPartsCost(){
        return this.partsCost;
    }
    public double getLaborHours(){
        return this.laborHours;
    }
    public double getTotalCost(){
        return this.totalCost;
    }

    public String getTablePartsCost(){
        return Double.toString(this.partsCost);
    }
    public String getTableLaborHours(){
        return Double.toString(this.laborHours);
    }
    public String getTableTotalCost(){
        return Double.toString(this.totalCost);
    }

    //setters
    void setServiceDesc(String serviceDesc){
        this.serviceDesc = serviceDesc;
    }
    void setPartsUsed(String partsUsed){
        this.partsUsed = partsUsed;
    }
    void setPartsCost(double partsCost){
        this.partsCost = partsCost;
    }
    void setLaborHours(double laborHours){
        this.laborHours = laborHours;
    }
    void setTotalCost(double totalCost ){
        this.totalCost = totalCost;
    }
    
    String formatForFile(){
        return this.carVIN + "^" + this.serviceDesc + "^" +  this.partsUsed + "^" + this.techID + "^"+ this.dealershipID + "^" + + this.partsCost + "^"
        + this.totalCost +  "^"  + this.laborHours + "^\n";
    }
    public void toFile(String fileString) throws IOException{
        try (PrintWriter output = new PrintWriter("serviceOrderData.txt");) {
            output.write(fileString);
            output.close();
        }
    }
  
}