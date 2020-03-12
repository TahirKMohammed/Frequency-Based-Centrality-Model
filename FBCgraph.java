import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Collection;
import javax.swing.JFrame;
/**
 *
 * @author kingtahir
 */
public class FBCgraph {
    
    private Graph<String, MyEdge> graph;
    
    
    public FBCgraph(FrequencyBasedModel fbm){
      
       // int counter = 1;
        this.graph = new SparseMultigraph<>();
        
        for(String key: fbm.getFreqMap().keySet()){
            double edgeV = fbm.getFreqMap().get(key);
            String[] str = key.split(" ");
            if(graph.containsVertex(str[0]) && graph.containsVertex(str[1])){
                // vertices are already added
                // here, we are adding the socond edge (with opposite direction)
            }
            else{
                // adding vertices and the first edge
                graph.addVertex(str[0]);
                graph.addVertex(str[1]);
            }
            graph.addEdge(new MyEdge(edgeV), str[0], str[1], EdgeType.DIRECTED);
            //System.out.println("Added edge: " + counter);
            //counter++;
            
        }
        
    }
    
    public Graph<String, MyEdge> getGraph(){
        return this.graph;
    }
    
    public void displayGraph(Graph<String,MyEdge> graph){
        Layout<String, MyEdge> layout = new CircleLayout(graph);
        // setting the initial size of the space
        layout.setSize(new Dimension(800,800));
        BasicVisualizationServer<String, MyEdge> vv = new BasicVisualizationServer<>(layout);
        //Setting the viewing area size
        vv.setPreferredSize(new Dimension(900,900));
        JFrame frame = new JFrame("Frequency Based Centrality Model");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
        
    }
    
    public static void main(String[] args) throws IOException{
        FrequencyBasedModel fbm = new FrequencyBasedModel();
        fbm.freqBWfiles(fbm.getNoSTPtextList());
        FBCgraph graph = new FBCgraph(fbm);
        graph.displayGraph(graph.getGraph());
        System.out.println("Total # of Vertices: " + graph.getGraph().getVertexCount() + " Total # of Edges: " +graph.getGraph().getEdgeCount());
        System.out.println("----- Displaying Edges -----\n");
        Collection<MyEdge> edges = graph.getGraph().getEdges();
        int counter = 1;
        for(MyEdge e : edges){
            System.out.println("edge #" + counter + " : " + e.getWeight());
            counter++;
        }
        System.out.println("\n----- Displaying Centrality Measure Result -----\n");
        BetweennessCentrality measure = new BetweennessCentrality(graph.getGraph());
        measure.evaluate();
        measure.printRankings(true, true);
        
    }
    
}
