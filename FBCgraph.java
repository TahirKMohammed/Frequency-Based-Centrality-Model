import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.algorithms.importance.*;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
/**
 *
 * @author kingtahir
 */
public class FBCgraph {
    
    private Graph<String, MyEdge> graph;
    private int threshold;
    
    public FBCgraph(FrequencyBasedModel fbm){
        int val = fbm.getFreqMap().size();
        System.out.println("total edges = " + val);
        if(val < 5){
            threshold = 1;
        }
        else{
            threshold =  val/5;
        }
        System.out.println("Threshld = " + threshold + "\n");
      
        this.graph = new UndirectedSparseGraph<>();
        int thresholdCounter = 0;
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
            if(thresholdCounter < threshold){
                graph.addEdge(new MyEdge(edgeV), str[0], str[1]);
                System.out.println("Between Vertex: " + str[0] + " and " + str[1] + " with edge: " + edgeV);
            }
            thresholdCounter++;
        }
        
    }
    
    public Graph<String, MyEdge> getGraph(){
        return this.graph;
    }
    
    public void displayGraph(Graph<String,MyEdge> graph){
        Layout<String, MyEdge> layout = new CircleLayout(graph);
        // setting the initial size of the space
        layout.setSize(new Dimension(600,600));
        BasicVisualizationServer<String, MyEdge> vv = new BasicVisualizationServer<>(layout);
        //Setting the viewing area size
        vv.setPreferredSize(new Dimension(700,700));
        JFrame frame = new JFrame("Frequency Based Centrality Model");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
        
    }
    
    public static void main(String[] args) throws IOException{
        FrequencyBasedModel fbm = new FrequencyBasedModel();
        fbm.freqBWfiles(fbm.getNoSTPtextList());
        fbm.sortMap();
        FBCgraph graph = new FBCgraph(fbm);
        graph.displayGraph(graph.getGraph());
        System.out.println("\nTotal # of Vertices: " + graph.getGraph().getVertexCount() + " Total # of Edges: " +graph.getGraph().getEdgeCount());
//        System.out.println("----- Displaying Edges -----\n");
//        Collection<MyEdge> edges = graph.getGraph().getEdges();
//        int counter = 1;
//        for(MyEdge e : edges){
//            System.out.println("edge #" + counter + " : " + e.getWeight());
//            counter++;
//        }
        
        System.out.println("\n----- Displaying Centrality Measure Result -----\n");
        BetweennessCentrality measure = new BetweennessCentrality(graph.getGraph(), true, false);
        measure.setRemoveRankScoresOnFinalize(false);
        measure.evaluate();
        List<Ranking<?>> ranking = measure.getRankings();
        Iterator it = ranking.iterator();
        // The maximum score = rank 1 score
        double maxScore = ranking.get(0).rankScore;
        // The minimum score = last rank score
        double minScore = ranking.get(ranking.size()-1).rankScore;
        int rCounter = 1;
        while(it.hasNext()){
            Ranking rank = (Ranking) it.next();
            // normalizing formula is applied here
            double normScore = (rank.rankScore - minScore)/(maxScore - minScore);
            System.out.println("Rank #" + rCounter + ": Vertex Name: " + rank.getRanked() + " Score: " + normScore);
            rCounter++;
        }
        //measure.printRankings(true, true);
        
        // Test Graph
//        Graph<String, MyEdge> g = new UndirectedSparseGraph<>();
//        g.addVertex("A");
//        g.addVertex("B");
//        g.addVertex("C");
//        g.addVertex("D");
//        g.addVertex("E");
//        g.addVertex("F");
//        g.addVertex("G");
//        g.addEdge(new MyEdge(0.7), "A", "B");
//        g.addEdge(new MyEdge(0.9), "B", "C");
//        g.addEdge(new MyEdge(0.57), "C", "D");
//        g.addEdge(new MyEdge(1.0), "D", "B");
//        g.addEdge(new MyEdge(1.3), "C", "A");
//        g.addEdge(new MyEdge(0.3), "A", "D");
//        g.addEdge(new MyEdge(0.2), "D", "F");
//        g.addEdge(new MyEdge(0.8), "D", "E");
//        g.addEdge(new MyEdge(0.4), "E", "G");
//        g.addEdge(new MyEdge(0.6), "F", "E");
//        g.addEdge(new MyEdge(0.2), "G", "F");
//        
//    
//        BetweennessCentrality m = new BetweennessCentrality(g, true, false);
//        m.setRemoveRankScoresOnFinalize(false);
//        m.evaluate();
//        rCounter = 1;
//        ranking = m.getRankings();
//        it = ranking.iterator();
//        // The maximum score = rank 1 score
//        maxScore = ranking.get(0).rankScore;
//        // The minimum score = last rank score
//        minScore = ranking.get(ranking.size()-1).rankScore;
//        while(it.hasNext()){
//            Ranking rank = (Ranking) it.next();
//            // normalizing formula is applied here
//            double normScore = (rank.rankScore - minScore)/(maxScore - minScore);
//            System.out.println("Rank #" + rCounter + ": Vertex Name: " + rank.getRanked() + " Score: " + normScore);
//            rCounter++;
//        }
//        m.printRankings(true, true);
        
    }
    
}
