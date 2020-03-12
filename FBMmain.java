import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author kingtahir
 */
public class FBMmain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        FrequencyBasedModel fbm = new FrequencyBasedModel();
        fbm.freqBWfiles(fbm.getNoSTPtextList());
        Vertex[] vArray = new Vertex[594]; 
        int counter = 0;
        
        try {
            FileWriter resultFile = new FileWriter("/Users/kingtahir/Documents/top20result_FBM.txt");
            
            for(String file : fbm.fileNameList){
                vArray[counter] = new Vertex(fbm, file);
                //System.out.println("Vertex #" + (counter+1) +" Created");
                resultFile.write("Vertex #" + (counter+1) +" Created\n");
                counter++;
            }
            resultFile.write("\n");
            for(Vertex v : vArray){
                resultFile.write(v.printMap());
            }
            
            resultFile.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        
    }
    
}
