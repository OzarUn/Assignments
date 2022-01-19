class Cell extends Vertex {
    int x,y,id;
    boolean visited,isStack;
    public Cell(int id,int x , int y ,boolean visited,boolean isStack){
        super(id,x,y,visited);
        this.isStack = isStack;
    }

}
