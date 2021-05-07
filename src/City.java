import java.util.ArrayList;

public class City {
    String name;
    ArrayList<Airports> airports;
    public City(String name, ArrayList<Airports> airports){
        this.name = name;
        this.airports = airports;
    }


    public void setName(String name) {
        this.name = name;
    }
}
