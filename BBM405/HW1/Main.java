import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
        int l = 10;
        Maze map = new Maze(l, l);
        Maze map2 = new Maze(l,l);
        ArrayList<Vertex> maps = map.fillMaze(map);
        ArrayList<Vertex> maps2 = map.fillMaze(map2);

        map.randomizedDfs(maps.get(0),map);
        map2.randomizedDfs(maps.get(0),map2);

        boolean[][] tmp = map.getConnect(map);
        boolean[][] tmp2 = map.getConnect(map2);


        map.fillNeighbours(tmp,maps);
        System.out.println("************************************************************************************************");
        map2.fillNeighbours(tmp2,maps2);

        map.printMaze(maps,0,map);
        map2.printMaze(maps2,0,map2);

//        System.out.print("\t\t");
//        for (int z = 0 ; z < tmp.length;z++){
//            System.out.print(z + "\t");
//        }
//        System.out.println();
//        for (int i = 0 ; i < tmp.length ; i++){
//            System.out.print(i+"\t");
//            for (int j = 0 ; j < tmp[i].length;j++){
//                System.out.print(tmp[i][j]+"\t");
//            }
//            System.out.println();
//        }


        //IDS
        IDS a = new IDS();
        int goal = l*l - 1 ;
        System.out.println("Start IDS");
        long startTime = System.currentTimeMillis();
        a.IDS(maps.get(0),maps.get(goal));
        long endTime = System.currentTimeMillis();
        long seconds = (endTime - startTime) / 1000;
        System.out.println("For goal : "+goal+"Finish IDS minute"+ (seconds/60) +" Finish IDS Second:"+(seconds/60) + " MiliSecond: "+ (endTime - startTime));
        System.out.println("Start IDS for secondMaze");
        long startTime2 = System.currentTimeMillis();
        a.IDS(maps2.get(0),maps2.get(goal));
        long endTime2 = System.currentTimeMillis();
        long seconds2 = (endTime2 - startTime2) / 1000;
        System.out.println("For goal : "+goal+"Finish IDS minute "+(seconds2/60)+" Second:"+seconds2 + " MiliSecond: "+ (endTime2 - startTime2));

    }
}
