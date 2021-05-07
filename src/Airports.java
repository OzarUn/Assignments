import java.util.ArrayList;

public class Airports {
    String Id;
    boolean visited;
    ArrayList<Flight> flights;
    public Airports(String Id,ArrayList<Flight> fl){
        this.Id = Id;
        this.visited = false;
        this.flights = fl;
    }

    public void setId(String id) {
        Id = id;
    }

}
