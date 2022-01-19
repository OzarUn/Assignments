import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Maze {
    public ArrayList<Vertex> map;
    public boolean[][] connect;
    public int rows,columns;
    public Maze(int row,int column){
        this.rows = row;
        this.columns = column;
        this.map = new ArrayList<>();
        this.connect = new boolean[row*column][column*column];

    }
    public static ArrayList<Vertex> fillMaze(Maze a){
        int x,y,i=0;
        System.out.println(a.columns);
        for(y=0;y<a.rows;y++) {
            for(x=0;x<a.columns;x++) {
                Vertex temp = new Cell(i,x,y,false,false);
                a.map.add(temp);
                i++;
            }
        }
        return a.map;
    }
    public static void connectCells(Vertex cur,Vertex next,Maze a){
        a.connect[cur.id][next.id] = true;
        a.connect[next.id][cur.id] = true;
    }
    public static boolean[][] getConnect(Maze a) {
        return a.connect;
    }
    public static void printMaze(ArrayList<Vertex> v, int cur,Maze a){
        int temp = cur;
        ArrayList<ArrayList<Vertex>> map = new ArrayList<>();
        for (int i = 0 ; i < a.rows ; i++ ){
            map.add(new ArrayList<>());
            for (int j = 0 ; j < a.columns ; j ++){
                map.get(i).add(v.get(cur));

                cur++;
            }
        }
        setWalls(map,a);
    }
    public static void setWalls(ArrayList<ArrayList<Vertex>> v,Maze a){
        for (Vertex cur : a.map) {
            if (0 < cur.x - 1 && cur.neighbours.contains(v.get(cur.x - 1).get(cur.y))) {
                cur.setUp(false);
            }
            if (cur.x + 1 < a.columns && cur.neighbours.contains(v.get(cur.x + 1).get(cur.y))) {
                cur.setDown(false);
            }
            if (0 < cur.y - 1 && cur.neighbours.contains(v.get(cur.x).get(cur.y - 1))) {
                cur.setLeft(false);
            }
            if (cur.y + 1 < a.columns && cur.neighbours.contains(v.get(cur.x).get(cur.y + 1))) {
                cur.setRight(false);
            }
        }
    }

    public static void markVisited(Vertex v){
        v.visited = true;
    }
    public static void randomizedDfs(Vertex v,Maze a){
        markVisited(v);
        Vertex next = chooseUnvisitedNeighbor(v.id,a);
        while (next != null){
            connectCells(v,next,a);
            randomizedDfs(next,a);
            next = chooseUnvisitedNeighbor(v.id,a);
        }
    }
    public static void fillNeighbours(boolean[][] adj,ArrayList<Vertex> vertices){
        for (int i = 0 ; i < vertices.size() ; i++){
            vertices.get(i).neighbours = new ArrayList<>();
            for (int j = 0; j < vertices.size() ; j++){
            if(adj[vertices.get(i).id][j] && i != j){
                vertices.get(i).neighbours.add(vertices.get(j));
            }
            }
        }
        for (int j = 0 ; j < vertices.size();j++){
            System.out.print("Vertex : "+vertices.get(j).id+"| neighbours--->>>>>> ");
            for (int a = 0 ; a < vertices.get(j).neighbours.size();a++){
                System.out.print(vertices.get(j).neighbours.get(a).id+" | ");
            }
            System.out.println();
        }
    }
    public static Vertex chooseUnvisitedNeighbor(int currentCell,Maze a) {
        int x = a.map.get(currentCell).x;
        int y = a.map.get(currentCell).y;
        int [] candidates = new int[4];
        int found=0;
        // left
        if( x > 0 && !a.map.get(currentCell - 1).visited) {
            candidates[found++] = currentCell-1;
        }
        // right
        if( x < a.columns-1 && !a.map.get(currentCell+1).visited ) {
            candidates[found++] = currentCell+1;
        }
        // up
        if( y > 0 && !a.map.get(currentCell-a.columns).visited ) {
            candidates[found++] = currentCell-a.columns;
        }
        // down
        if( y < a.rows-1 && !a.map.get(currentCell+a.columns).visited ) {
            candidates[found++] = currentCell+a.columns;
        }
        if(found==0) return null;
        int choice = (int)(Math.random()*found);
        assert(choice>=0 && choice < found);
        return a.map.get(candidates[choice]);
    }





    private static void initializeWindow(ArrayList<ArrayList<Vertex>> puzzle) {
        JFrame mainFrame = new JFrame("Maze Solver");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new GridLayout(puzzle.size(), puzzle.get(0).size()));// avoid null layouts
        mainFrame.setLocationRelativeTo(null);
        for (int row = 0; row < puzzle.size(); row++) {
            for (int col = 0; col < puzzle.get(row).size(); col++) {
                JLabel label = makeLabel(puzzle.get(row).get(col));
                mainFrame.add(label);
            }
        }
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private static JLabel makeLabel(Vertex c) {
        JLabel label= new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(40, 40));


        label.setBackground(Color.WHITE);


        label.setOpaque(true);
        if (c.down){

        }
        label.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        System.out.println(label.getBorder().isBorderOpaque());

        return label;
    }



}
