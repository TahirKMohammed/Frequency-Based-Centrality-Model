import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kingtahir
 */
public class Vertex {
    
    private HashMap<String,Double> vertexMap = new HashMap<String, Double>();
    private String vertexName = "";
    
    public Vertex(FrequencyBasedModel fbm, String vName){
        this.vertexName = vName;

        for (String key: fbm.getFreqMap().keySet()){
            if(key.contains(this.vertexName)){
               // System.out.println(key);
                double value = fbm.getFreqMap().get(key);
                String[] str = key.split(" ");
                if(str[0].equals(this.vertexName)){
                    this.vertexMap.put(str[1], value);
                }
                else{
                    this.vertexMap.put(str[0], value);
                }
            } 
        }
        
        HashMap<String, Double> sortedMap = sortVMap(this.vertexMap);
        this.vertexMap = sortedMap;
    }
    
    public HashMap<String, Double> sortVMap(HashMap<String, Double> unsortedVMap){
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortedVMap.entrySet());
        
        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>(){
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2){
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        
        // Putting data from sorted list to hashmap
        HashMap<String, Double> sortedVMap = new LinkedHashMap<String, Double>();
        for(Map.Entry<String, Double> aa : list){
            sortedVMap.put(aa.getKey(), aa.getValue());
        }
        
        return sortedVMap;
    }
    
    public HashMap<String,Double> getVMap(){
        return this.vertexMap;
    }
    
    public String getVName(){
        return this.vertexName;
    }
    
    public String printMap(){
        String print = "Showing Graph for " + getVName() + "  Total Connected Vertices: " + vertexMap.size() + "\n";
        //System.out.println("Showing Graph for " + getVName() + "  Total Vertices: " + vertexMap.size());
        if(vertexMap.isEmpty()){
            //System.out.println("Empty Graph");
            print = print + "Empty Graph\n";
        }
        else{
            // convert to ArrayList of key set
            List<String> allKeys = new ArrayList<String>(vertexMap.keySet());
 
            // reverse order of keys
            Collections.reverse(allKeys);
        
            // iterate vertexMap using reverse order of keys
            int counter = 0;
            for (String key: allKeys){
                double value = vertexMap.get(key);  
                //System.out.println("Vertex: " + key + "     Edge: " + value);
                if(counter < 20){
                    print = print + "Vertex: " + key + "            Edge: " + value + "\n";
                    counter++;
                }
                else{
                    counter++;
                    break;
                }
                
            }
            //System.out.println();
            print = print + "\n";
        }
        return print;
    }
}
