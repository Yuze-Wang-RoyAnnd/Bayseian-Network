
import java.util.*;


public class Baysian_Network {

    private HashMap<String, Baysian_Node> nodeList;

    //initialize with number of nodes exist
    public Baysian_Network (int nodenumber){
        nodeList = new HashMap<String, Baysian_Node>();
    }

    public void setNode(String nodeName, Baysian_Node node){
        nodeList.put(nodeName, node);
    }

    public Baysian_Node getNode(String nodeName){
        return nodeList.get(nodeName);
    }

    public HashMap<String, Baysian_Node> getList(){
        return nodeList;
    }

    public int getsize(){
        return nodeList.size();
    }

    public String toString(){
        String output = "";
        for(Baysian_Node node : nodeList.values()){
            output += node.toString() + "\n";
        }
        return output;
    }
}
