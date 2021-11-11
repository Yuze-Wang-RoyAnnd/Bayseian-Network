
import java.util.*;


public class Baysian_Node {
    //nodeName = the name of the nodes
    //ParentNodes = a list of parent node
    //outcome = a list of
    //[Row][Column]    [-][|]
    private String nodeName;
    private ArrayList<String> outcome;
    private float[][] ProbabilityDefination;
    //evidence contain the outcome index of parent
    //if A have two parent B and C where each parent have two outcome, then the evidence will be
    // 0 0              The number is the index of the outcome of the parent
    // 0 1
    // 1 0
    // 1 1
    public int[][] evidence;
    public ArrayList<Baysian_Node> parentNodes;

    private void pRecursion(int row, int parentCounter, int rowcounter, int pIndex){
        if(parentCounter != 0){
            //System.out.println("Current cur:" + row + " parentCounter:"+ parentCounter + " rowcounter:" + rowcounter + " pIndex:" + pIndex);
            if(parentNodes.get(pIndex) == null){
                evidence[rowcounter][pIndex] = -1;
            }else{
                int cur = row / parentNodes.get(pIndex).getoutcomesize();
                evidence[rowcounter][pIndex] = row % parentNodes.get(pIndex).getoutcomesize();
                pRecursion(cur, parentCounter-1, rowcounter, pIndex - 1);
            }
        }
    }

    //initalize the node
    public Baysian_Node (String nodeName){
        this.nodeName = nodeName;
        outcome = new ArrayList<String>();
        parentNodes = new ArrayList<Baysian_Node>();
    }

    //add outcome
    public void addoutcome (String outcomeType){
        outcome.add(outcomeType);
    }

    //add parent
    //if there is no parent, insert null
    public void addParent (Baysian_Node bayNode){
        parentNodes.add(bayNode);
    }

    public int getParentSize(){
        return parentNodes.size();
    }

    public int getoutcomesize (){
        return outcome.size();
    }

    public int getIndexofoutcome(String outcomein){
        return outcome.indexOf(outcomein);
    }

    public String getNodeName(){
        return nodeName;
    }

    public float[] getProbability(int row){
        return ProbabilityDefination[row];
    }

    public String getoutcome(int index){
        return outcome.get(index);
    }

    public ArrayList<String> getoutcome(){
        return outcome;
    }

    public int findIndexofOutCome(String s){
        return outcome.indexOf(s);
    }


    //initalize the probability table
    public void initializeBaysianTable (float[] inputArr){
        if(parentNodes.get(0) != null){
            //total parent combination = parent1.outcome * parent2.outcome * ....
            //this part get the total combination of parents outcome
            int[] parentNumber = new int[parentNodes.size()];
            int counter = 0;
            for(Baysian_Node pNode : parentNodes){
                parentNumber[counter] = pNode.getoutcomesize();
                counter++;
            }
            int combination = 1;
            for(int i : parentNumber){
                combination *= i;
            }
            // this part initialize the 2d array
            ProbabilityDefination = new float[combination][outcome.size()];
        }else{
            ProbabilityDefination = new float[1][outcome.size()];
        }
        //<=
        evidence = new int[ProbabilityDefination.length][parentNodes.size()];
        //fill the 2d array
        int column = 0;
        int row = 0;
        for(float k : inputArr){
            if(column >= outcome.size()){
                column = 0;
                row++;
                ProbabilityDefination[row][column] = k;
                column++;
            }else{
                ProbabilityDefination[row][column] = k;
                column++;
            }
        }
        //fill the evidence
        for(int i = 0; i < ProbabilityDefination.length; i++){
            //System.out.println("Current Node: " + nodeName);
            pRecursion(i, parentNodes.size(), i, parentNodes.size() - 1);
            //System.out.println(" ");
        }
    }

    public String toString(){
        String output = "Node Name: " + nodeName + "\nParent Node: ";
        String parentNodeName = "";
        if(parentNodes.get(0) != null){
            for(Baysian_Node pNode : parentNodes){
                parentNodeName += " " + pNode.getNodeName();
            }
        }else{
            parentNodeName = "NO PARENT";
        }


        //outcome distribution
        String outcomeN = "outcome: ";
        for(String cName : outcome){
            outcomeN += cName + " ";
        }

        output += parentNodeName + "\n" +outcomeN + "\nNodeTable:\n" ;
        String nodeTable= "";
        for(int i = 0; i < ProbabilityDefination.length; i++){
            for(int j = 0; j < evidence[i].length; j++){
                nodeTable += evidence[i][j] + " ";
            }
            nodeTable += "  :";
            for(int k = 0; k < ProbabilityDefination[i].length; k++){
                nodeTable +=  ProbabilityDefination[i][k] + " ";
            }
            nodeTable += "\n";
        }
        output += nodeTable;
        return output;
    }
}
