import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Flight {
    String code,duration;
    Airports dept,arr;
    int price;
    String brand;
    LocalDateTime deptDate,arrDate;
    public Flight(String code, String dept, String arr,String deptDate,String duration, int price, ArrayList<City> base) {
        this.code = code;
        this.brand = code.replaceAll("\\d","");
        this.duration = duration;
        for(City i : base){
            for(Airports a : i.airports){
                if(a.Id.equals(arr)){
                    this.arr = a;
                }
                if(a.Id.equals(dept)){
                    this.dept = a;
                }
            }
        }
        this.price = price;
        String[] tempY = deptDate.split(" ");
        String tempX = tempY[0]+" "+tempY[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(tempX, formatter);
        this.deptDate = dateTime;
        String[] dur = duration.split(":");
        LocalDateTime dateTime2 = dateTime.plusHours(Integer.parseInt(dur[0])).plusMinutes(Integer.parseInt(dur[1]));
        this.arrDate = dateTime2;
    }




    public void setCode(String code) {
        this.code = code;
    }
    public void setDeptDate(LocalDateTime deptDate) {
        this.deptDate = deptDate;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public void setArr(Airports arr) {
        this.arr = arr;
    }
    public void setDept(Airports dept) {
        this.dept = dept;
    }

}
