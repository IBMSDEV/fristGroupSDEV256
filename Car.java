public class Car{
    String vin;
    String make;
    String model;
    int year;
    int mileage; 
    String owner;
    String serviceDate;
    String fileString = "";


    Car(String vin, String make, String model, int year, int mileage, String serviceDate, String owner){
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.serviceDate = serviceDate;
        this.mileage = mileage; 
        this.owner = owner;
    }
    //getters
   public  String getVin(){
        return this.vin;
    }
    public String getMake(){
        return this.make;
    }
    public String getModel(){
        return this.model;
    }
    public String getServiceDate(){
        return this.serviceDate;
    }
    public int getMileage(){
        return this.mileage;
    }
    public int getYear(){
        return this.year;
    }
    public String getOwner(){
        return this.owner;
    }
    //setters 
    void setMake(String newMake){
         this.make = newMake;
    }
    void setModel(String model){
        this.model = model;
    }
    void setMileage(String mileage){
        this.mileage = Integer.parseInt(mileage);
    }
    void setYear(int year){
        this.year = year;
    }
    void setOwner(String owner){
        this.owner = owner;
    }
    void  setServiceDate(String serviceDate){
         this.serviceDate = serviceDate;
    }

    //Makes sure that the letter in the 9th spot of the vin is a vaild Item and that the vin doesn't contain any letters that it should not
    Boolean CheckVin(String Vin){
        int [] weight = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};
        int [] value = {1, 2, 3, 4, 5, 6, 7, 8, 0, 
                        1, 2, 3, 4, 5, 0, 7, 0, 9,
                           2, 3, 4, 5, 6, 7, 8, 9};
        Vin = Vin.toUpperCase();
        if (Vin.length() != 17){
            return false;
        } 
        else if (Vin.contains("I") || Vin.contains("O") || Vin.contains("Q")  ){
            return false;
        }
        else{   int sum = 0;
            for (int i = 0; i<Vin.length(); i++){ 
                try {
                    int number = Integer.parseInt(Character.toString(Vin.charAt(i)));
                    sum += number * weight[i];
                } catch (Exception e) {
                    int letterNumber = Vin.charAt(i);

                    sum += value[letterNumber - 65] * weight[i];
                }
                
            } 
            if(Vin.charAt(8) != 'X'){
            if (sum%11 ==  Integer.parseInt(Character.toString(Vin.charAt(8)))) 
                return true;
            else 
                return false;
            }else{
                if (sum%11 ==  10)
                    return true;
                else
                    return false;
            }
            
        }

    }

}
