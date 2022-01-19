import java.util.ArrayList;

public class Vertex {
    int x,y,id;
    boolean up,left,right,down;
    boolean visited;
    ArrayList<Vertex> neighbours;
    public Vertex(int id,int x,int y, boolean visited){
    this.id = id;
    this.x = x;
    this.y = y;
    this.visited = visited;
    this.up =true;
    this.left = true;
    this.right = true;
    this.down = true;

    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
