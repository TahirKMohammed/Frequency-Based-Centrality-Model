import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kingtahir
 */
public class FrequencyBasedModel {
    
    int counter = 0;
    //String dataDir = "/Users/kingtahir/Documents/TestCorpus";
    String dataDir = "/Users/kingtahir/Downloads/clueweb09PoolFilesTest";
    ArrayList<String> list = new ArrayList<String>(); 
    ArrayList<String> fileNameList = new ArrayList<String>();
    ArrayList<String> textList = new ArrayList<String>();
    ArrayList<String> noSTPtextList = new ArrayList<String>();
    HashMap<String,String> fileContentsMap = new HashMap<String, String>();
    HashMap<String,Double> freqMap = new HashMap<String, Double>();
 
    public FrequencyBasedModel() throws IOException{
        File folder = new File(dataDir);
        File[] listOfFiles = folder.listFiles();
        for(File file : listOfFiles) {
            if (file.isFile()) {
                String tmpFilePath = dataDir + "/" + file.getName();
                counter++;
                //System.out.println("Path# " + counter + ": " + tmpFilePath);
                // Adding the file contents in a format of html which I take care of later
                list.add(readFile(tmpFilePath));
                // Adding file names into an ArrayList
                fileNameList.add(file.getName());
            }
        }
        //System.out.println(list.get(0));
        
        for(String str : list){
            // Converting file contents from html to String
            Document document = Jsoup.parse(str, "ASCII");
            // Adding file contents in String format 
            textList.add(document.text());
        }
        int fileNO = 0;
        for(String str : textList){
            // Removing stop words from String file contents
            noSTPtextList.add(removeStopWords(str));
            //fileContentsMap.put(fileNameList.get(fileNO), removeStopWords(str));
            //fileNO++;
        }
        
    }
    
    private String readFile(String filePath) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filePath));
    String str = "";
    try {
        String line = br.readLine();
        
        while (line != null) {
           // queryNumTopic(line);
           if(line.matches("\\A\\p{ASCII}*\\z")){
               str = str + line;
           }
            line = br.readLine();
        }
    } finally {
        br.close();
    }
    return str;
    //return str.replaceAll("\\s+", "");
  }
    
    private String removeStopWords(String str) throws IOException{
        //String stopWords = readFile("/Users/kingtahir/NetBeansProjects/CapstoneEB02/stopwords.txt");
        String stopwords = "!! ?! ?? !? ` `` '' -lrb- -rrb- -lsb- -rsb- , . : ; \" ' ? < > { } [ ] + - ( ) & % $ @ ! ^ # * .. ... 'll 's 'm a about above after again against all am an and any are aren't as at be because been before being below between both but by can can't cannot could couldn't did didn't do does doesn't doing don't down during each few for from further had hadn't has hasn't have haven't having he he'd he'll he's her here here's hers herself him himself his how how's i i'd i'll i'm i've if in into is isn't it it's its itself let's me more most mustn't my myself no nor not of off on once only or other ought our ours ourselves out over own same shan't she she'd she'll she's should shouldn't so some such than that that's the their theirs them themselves then there there's these they they'd they'll they're they've this those through to too under until up very was wasn't we we'd we'll we're we've were weren't what what's when when's where where's which while who who's whom why why's with won't would wouldn't you you'd you'll you're you've your yours yourself yourselves ### return arent cant couldnt didnt doesnt dont hadnt hasnt havent hes heres hows im isnt its lets mustnt shant shes shouldnt thats theres theyll theyre theyve wasnt were werent whats whens wheres whos whys wont wouldnt youd youll youre youve";
        String[] allWords = str.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for(String word : allWords) {
            if(!stopwords.contains(word)) {
                builder.append(word);
                builder.append(' ');
            } 
        }
        String result = builder.toString().trim();
        return result;
    }
    
    public void freqBWfiles(ArrayList<String> noSTPtextList){
        // Generates HashMap with key: file names and value: the shared number of words
        ArrayList<String> tmpList = noSTPtextList;
        int counter1 = 0;
//        for(String fileContent : fileContentsMap.keySet()){
//            String[] fileContentArray = fileContent.split(" ");
//            double docword1 = fileContentArray.length;
//            int counter2 = 0;
//        }
        for(String element : noSTPtextList){
            String[] elementArray = element.split(" ");
            double docword1 = elementArray.length;
            int counter2 = 0;
            for(String str : tmpList){
                if(element.equals(str)){
                    continue;
                }
                if(counter2 == counter1){
                    counter2++;
                }
                int freq = 0;
                double docword2 = str.split(" ").length;
                double largerwords = docword2;
                for(String word : elementArray){
                    if(str.contains(word)){
                        freq++;
                    }
                    if(docword1 > docword2){
                        largerwords = docword1;
                    }
                }
                
                String key = fileNameList.get(counter2) + " " + fileNameList.get(counter1);
                if(freqMap.isEmpty()){
                    freqMap.put((fileNameList.get(counter1) + " " + fileNameList.get(counter2)), 1/(freq/largerwords));
                }
                
                else if(freqMap.containsKey(key)){
                    if(freq/largerwords > freqMap.get(key)){
                        freqMap.replace(key, 1/(freq/largerwords));
                    }
                }
                
                else{
                    freqMap.put((fileNameList.get(counter1) + " " + fileNameList.get(counter2)), 1/(freq/largerwords));
                }
                
                //freqMap.put(("# of shared words between " + fileNameList.get(counter1) + " and " + fileNameList.get(counter2)), freq/largerwords);
                counter2++;
            }
            counter1++;
        }
        
        System.out.println("HashMap has been made");
    }
    
    public ArrayList<String> getNoSTPtextList(){
        return this.noSTPtextList;
    }
    
    public HashMap<String,Double> getFreqMap(){
        return this.freqMap;
    }
    
    public void sortMap(){
        Vertex v = new Vertex();
        HashMap<String,Double> sortedMap = new HashMap<String, Double>();
        sortedMap = v.sortVMap(this.freqMap);
        this.freqMap = sortedMap;
    }
    
    public static void main(String[] args) throws IOException {
        FrequencyBasedModel fbm = new FrequencyBasedModel();
        fbm.freqBWfiles(fbm.getNoSTPtextList());
        
        System.out.println("----- Displaying original map -----");
        for(String key : fbm.getFreqMap().keySet()){
            System.out.println(key + " edge: " + fbm.getFreqMap().get(key));
        }
        fbm.sortMap();
        System.out.println("\n----- Displaying sorted map -----");
        for(String key : fbm.getFreqMap().keySet()){
            System.out.println(key + " edge: " + fbm.getFreqMap().get(key));
        }
    
    
    
    }
    
    
  
}
