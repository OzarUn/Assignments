import java.util.ArrayList;

public class IDS {
    public Vertex DLS(Vertex current,Vertex goal ,int depth) {
        if (depth == 0 && current == goal) {
            return current;
        }
        if (depth > 0) {
            for (Vertex child : current.neighbours) {
                Vertex found = DLS(child, goal,depth - 1);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    public Vertex IDS(Vertex root,Vertex goal) {
        for (int depth = 0; depth < Integer.MAX_VALUE; depth++) {
            Vertex found = DLS(root,goal,depth);
            if (found != null) {

                return found;
            }
        }
        return null;
    }

}

