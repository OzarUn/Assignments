import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class FlightSearchEngine {
    static ArrayList<City> baseList = new ArrayList<>();
    static HashMap<City, Boolean> baseBool = new HashMap<>();
    static HashMap<String,ArrayList<ArrayList<Flight>>> allFlights = new HashMap<>();
    static HashMap<String,ArrayList<ArrayList<Flight>>> allFlightsProper = new HashMap<>();
    static ArrayList<ArrayList<Flight>> arrFlights = new ArrayList<>();

    public static void createMap(ArrayList<String> airports, ArrayList<String> flights) {
        for (String i : airports) {
            String[] tempCit = i.split("\t");
            if (tempCit.length == 2) {
                ArrayList<Airports> tmp = new ArrayList<>();
                tmp.add(new Airports(tempCit[1], new ArrayList<Flight>()));
                City tempC = new City(tempCit[0], tmp);
                baseList.add(tempC);
            } else {
                ArrayList<Airports> tmp = new ArrayList<>();
                for (int j = 1; j < tempCit.length; j++) {
                    tmp.add(new Airports(tempCit[j], new ArrayList<Flight>()));
                }
                City tempC = new City(tempCit[0], tmp);
                baseList.add(tempC);
            }
        }
        addFlight(flights);
    }
    public static void addFlight(ArrayList<String> fli) {
        for (String i : fli) {
            String[] tempR = i.split("\t");
            String[] tempR2 = tempR[1].split("->");
            for (City j : baseList) {
                for (int k = 0; k < j.airports.size(); k++) {
                    ArrayList<Flight> tmp = new ArrayList<>();
                    if (tempR2[0].equals(j.airports.get(k).Id)) {
                        j.airports.get(k).flights.add(new Flight(tempR[0], tempR2[0], tempR2[1], tempR[2], tempR[3], Integer.parseInt(tempR[4]), baseList));
                    }
                }
            }
        }
    }
    public static void fillNull() {
        for (City i : baseList) {
            baseBool.put(i, false);
        }
    }
    public static String timer(long hour, long minute, boolean a) {
        if (minute < 60 && a) {
            if (hour < 10) {
                if (minute < 10) {
                    return "0" + hour + ":0" + minute;
                }
                return "0" + hour + ":" + minute;
            }
            else {
                if (minute < 10) {
                    return hour + ":0" + minute;
                }
            }
            return hour + ":" + minute;
        }
        if (minute > 60) {
            minute = minute - 60;
            hour++;
        }
        return timer(hour, minute, true);
    }
    public static int calcPathCost(ArrayList<Flight> path){
        int cost = 0;
        for (Flight i : path){
            if (path.size() == 1){
                return i.price;
            }
            cost = cost + i.price;
        }
        return cost;
    }
    public static String calcDur(ArrayList<Flight> path){
        for (Flight i : path){
            if (path.size() == 1){
                return i.duration;
            }
        }
        long hours = ChronoUnit.HOURS.between(path.get(0).deptDate, path.get(path.size() - 1).arrDate);
        long minutes = ChronoUnit.MINUTES.between(path.get(0).deptDate, path.get(path.size() - 1).arrDate) - hours * 60;
        String time = timer(hours, minutes, false);
        return time;
    }
    public static void findProper(String element){
        ArrayList<ArrayList<Flight>> temper = new ArrayList<>();
        ArrayList<Flight> temp  =  allFlights.get(element).get(0);
        temper.add(temp);
        for (int i = 1 ; i < allFlights.get(element).size() ; i++){
            if(durIsSmall(calcDur(allFlights.get(element).get(i)),calcDur(temp)) && calcPathCost(allFlights.get(element).get(i)) < calcPathCost(temp)){
                temper.remove(temp);
                temp = allFlights.get(element).get(i);
            }
            else if(durIsSmall(calcDur(allFlights.get(element).get(i)),calcDur(temp)) || calcPathCost(allFlights.get(element).get(i)) < calcPathCost(temp)){
                if (!temper.contains(temp)){
                    temper.add(temp);
                }
                if (!temper.contains(allFlights.get(element).get(i))){
                    temper.add(allFlights.get(element).get(i));
                }
            }
        }
        allFlightsProper.put(element,temper);
    }
    public static boolean durIsSmall(String first, String second){
        String[] temp = first.split(":");
        String[] temp2 = second.split(":");
        if (Integer.parseInt(temp[0]) < Integer.parseInt(temp2[0])){
            if (Integer.parseInt(temp[1]) < Integer.parseInt(temp2[1])){
                return true;
            }
            return true;
        }
        return false;
    }
    public static void cityVisited(Airports t) {
        for (int i = 0; i < baseList.size(); i++) {
            if (baseList.get(i).airports.contains(t)) {
                baseBool.put(baseList.get(i), true);
                break;
            }
        }
    }
    public static ArrayList<Flight> quickestPath(String fr){
        ArrayList<Flight> min = allFlights.get(fr).get(0);
        for (int i = 1 ; i < allFlights.get(fr).size() ; i++){
            if (durIsSmall(calcDur(allFlights.get(fr).get(i)),calcDur(min))){
                min = allFlights.get(fr).get(i);
            }
        }
        return min;
    }
    public static ArrayList<Flight> cheapestPath(String fr){
        ArrayList<Flight> min = allFlights.get(fr).get(0);
        for (int i = 1 ; i < allFlights.get(fr).size() ; i++){
            if (calcPathCost(allFlights.get(fr).get(i)) < calcPathCost(min)){
                min = allFlights.get(fr).get(i);
            }
        }
        return min;
    }
    public static boolean listCheaper(ArrayList<Flight> tm,int limit){
        return calcPathCost(tm) < limit;
    }
    public static boolean listQuicker(ArrayList<Flight> tm,LocalDateTime lt){
        return tm.get(tm.size()-1).arrDate.isBefore(lt);
    }
    public static boolean exlusiveInc(ArrayList<Flight> tm, String except){
        for (Flight i : tm){
            if(i.brand.equals(except)){
                return false;
            }
        }
        return true;
    }
    public static boolean listOnly(ArrayList<Flight> tmp,String except){
        for (Flight i : tmp){
            if (!i.brand.equals(except)){
                return false;
            }
        }
        return true;
    }
    public static void printPath(ArrayList<Flight> path,FileWriter flw) throws IOException {
        for (int z = 0 ; z < path.size() ; z++){
            if (z == path.size()-1){
                flw.write(path.get(z).code+"\t"+path.get(z).dept.Id+"->"+path.get(z).arr.Id+"\t"+calcDur(path) +"/"+ calcPathCost(path)+"\n");
                break;
            }
            flw.write(path.get(z).code+"\t"+path.get(z).dept.Id+"->"+path.get(z).arr.Id+"\t||");
        }
    }

    public static void listAllPaths(Airports d, Airports a, ArrayList<Flight> path, LocalDateTime time) {
        d.visited = true;
        if (d == a) {
            if (!path.isEmpty()) {
                ArrayList<Flight> tmp = new ArrayList<>(path);
                arrFlights.add(tmp);
                d.visited = false;
                return;
            }
        }
        Iterator<Flight> itr = d.flights.iterator();
        while (itr.hasNext()) {
            Flight temp = itr.next();
            if (!temp.arr.visited && (temp.deptDate.isAfter(time) || temp.deptDate.isEqual(time)) ) {
                path.add(temp);
                listAllPaths(temp.arr, a, path, temp.arrDate);
                path.remove(path.size()-1);
            }
        }
        d.visited =false;
    }

    public static void searchEngine(ArrayList<String> comands, FileWriter flw) throws IOException {
        fillNull();
        for (String i : comands) {
            String[] tempAr = i.split("\t");
            flw.write("command : "+i+"\n");
            switch (tempAr[0]) {
                case "listAll":
                    String[] destAr = tempAr[1].split("->");
                    for (City j : baseList) {
                        if (j.name.equals(destAr[0])) {
                            for (City k : baseList) {
                                if (k.name.equals(destAr[1])) {
                                    for (int z = 0; z < j.airports.size(); z++) {
                                        for (int g = 0; g < k.airports.size(); g++) {
                                            ArrayList<Flight> tm = new ArrayList<>();
                                            String str = tempAr[2] + " 00:00";
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                                            LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
                                            listAllPaths(j.airports.get(z), k.airports.get(g), tm, dateTime);
                                            allFlights.put(tempAr[1],arrFlights);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (ArrayList<Flight> j : allFlights.get(tempAr[1])){
                        printPath(j,flw);
                    }
                    break;
                case "listProper":
                    findProper(tempAr[1]);
                    for (ArrayList<Flight> L : allFlightsProper.get(tempAr[1])){
                        printPath(L,flw);
                    }
                    break;
                case "listCheapest":
                    ArrayList<Flight> temp = cheapestPath(tempAr[1]);
                    printPath(temp,flw);
                    break;
                case "listQuickest":
                    ArrayList<Flight> temper = quickestPath(tempAr[1]);
                    printPath(temper,flw);
                    break;
                case "listCheaper":
                    boolean y = false;
                    for (ArrayList<Flight> L : allFlightsProper.get(tempAr[1])){
                        if (listCheaper(L,Integer.parseInt(tempAr[3]))){
                            y=true;
                            printPath(L,flw);
                        }
                    }
                    if(!y){
                        flw.write("No suitable flight plan is found\n");
                    }
                    break;
                case "listQuicker":
                    boolean o =false;
                    String[] tt = tempAr[3].split(" ");
                    String time = tt[0]+" "+tt[1];
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
                    for (ArrayList<Flight> L : allFlightsProper.get(tempAr[1])){
                        if(listQuicker(L,dateTime)){
                            o=true;
                            printPath(L,flw);
                        }
                    }
                    if(!o){
                        flw.write("No suitable flight plan is found\n");
                    }
                    break;
                case "listExcluding":
                    boolean a = false;
                    for (ArrayList<Flight> L : allFlightsProper.get(tempAr[1])){
                        if(exlusiveInc(L,tempAr[3])){
                            printPath(L,flw);
                            a = true;
                        }
                    }
                    if (!a){
                        flw.write("No suitable flight plan is found\n");
                    }
                break;
                case "listOnlyFrom":
                    boolean l = false;
                    for (ArrayList<Flight> L : allFlightsProper.get(tempAr[1])){
                        if(listOnly(L,tempAr[3])){
                            printPath(L,flw);
                            l = true;
                        }
                    }
                    if (!l){
                        flw.write("No suitable flight plan is found\n");
                    }
                    break;

            }
            flw.write("\n");
        }
    }
}
