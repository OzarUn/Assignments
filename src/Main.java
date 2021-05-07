import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<String> airports = new ArrayList<>();
    static ArrayList<String> flights = new ArrayList<>();
    static ArrayList<String> getCommands = new ArrayList<>();
    public static void main(String[] args){
        reader(args[0],airports);
        reader(args[1],flights);
        FlightSearchEngine.createMap(airports,flights);
        reader(args[2],getCommands);
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            FlightSearchEngine.searchEngine(getCommands,myWriter);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void reader(String fileName,ArrayList<String> arr){
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                arr.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
