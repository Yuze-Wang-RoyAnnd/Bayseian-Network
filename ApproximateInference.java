
import java.util.*;

//evidence inputs are
//Name of node | outome
//outcome is just the name of node
public class ApproximateInference {
   //prior_Sampling
   //return a hashmap<String(Cenario), String(value)>
   public static HashMap<String, String> creatingTopDown(Baysian_Network network){
      HashMap<String, String> output = new LinkedHashMap<String, String>();
      //first sort the one doesn't have parent
      ArrayList<String> noParent = new ArrayList<String>();
      ArrayList<String> yesParent = new ArrayList<String>();
      for(String key : network.getList().keySet()){
         if(network.getNode(key).parentNodes.get(0) == null){
            noParent.add(key);
         }else{
            yesParent.add(key);
         }
      }
      //initalize the output
      for(String k1 : noParent){
         output.put(k1, null);
      }
      for(String k2 : yesParent){
         output.put(k2, null);
      }
      for(String name: output.keySet()){
         String key = name;
         String value = output.get(name);
         //System.out.println("Name: " + name + " Value: " + value);
     }
     return output;
   }

   private static boolean existInEvi(String[] evidence, String curr){
      boolean flag = false;
      for(int i = 0; i < evidence.length; i+=2){
         if(evidence[i].equals(curr)){
            flag = true;
            return flag;
         }
      }
      return flag;
   }

   public static HashMap<String, String> weighted_Sampling(HashMap<String, String> sample, Baysian_Network network, String[] evidence){
      HashMap<String, String> output = new LinkedHashMap<>();
      for(String key : sample.keySet()){
         output.put(key, sample.get(key));
      }
      float weight = 1.0f;
      for(String cenario : output.keySet()){
         Baysian_Node curNode = network.getNode(cenario);

         if(curNode.parentNodes.get(0) != null){
            int[] parentArr = new int[curNode.getParentSize()];
            int i = 0;
            for(Baysian_Node pNode : curNode.parentNodes){
               String parentOutcome = output.get(pNode.getNodeName());
               int currentindex = pNode.getIndexofoutcome(parentOutcome);
               parentArr[i] = currentindex;
               i++;
            }
            int counter = 0;
            for(int[] evidenceCombine : curNode.evidence){
               //find the row
               if(Arrays.equals(evidenceCombine, parentArr) == true){
                  //if current node exist evidence, record the evidence chance, set evidence outcome
                  if(existInEvi(evidence, curNode.getNodeName()) == true){
                     String s = evidence[Arrays.asList(evidence).indexOf(curNode.getNodeName()) + 1];
                     output.put(cenario, s);
                     weight *= curNode.getProbability(counter)[curNode.findIndexofOutCome(s)];
                  }else{
                     int outcomeIndex = random_index(curNode.getProbability(counter));
                     output.put(cenario, curNode.getoutcome(outcomeIndex));
                  }
                  break;
               }
               counter++;
            }

         }else{
            int outcomeIndex = random_index(curNode.getProbability(0));
            output.put(cenario, curNode.getoutcome(outcomeIndex));
            if(existInEvi(evidence, curNode.getNodeName()) == true){
               weight *= curNode.getProbability(0)[outcomeIndex];
            }
         }
      }
      output.put("weight", String.valueOf(weight));
      return output;
   }
   public static HashMap<String, String> prior_Sampling(HashMap<String, String> sample, Baysian_Network network){
      HashMap<String, String> output = new LinkedHashMap<>();
      //operation
      for(String key : sample.keySet()){
         output.put(key, sample.get(key));
      }
      for(String cenario : output.keySet()){
         Baysian_Node curNode = network.getNode(cenario);
         //query the parent
         if(curNode.parentNodes.get(0) != null){
            int[] parentArr = new int[curNode.getParentSize()];
            //make approximation according to parent.
            int i = 0;
            for(Baysian_Node pNode : curNode.parentNodes){
               String parentOutcome = output.get(pNode.getNodeName());
               int currentindex = pNode.getIndexofoutcome(parentOutcome);
               parentArr[i] = currentindex;
               i++;
            }
            //System.out.println("Current Array: " + Arrays.toString(parentArr));
            //compare the arrayList
            int counter = 0;
            for(int[] evdienceCombine : curNode.evidence){
               if(Arrays.equals(evdienceCombine, parentArr) == true){
                  //System.out.println("Found");
                  int outcomeIndex = random_index(curNode.getProbability(counter));
                  output.put(cenario, curNode.getoutcome(outcomeIndex));
                  //System.out.println("Node: " + curNode.getNodeName() + " Chance: " + outcomeIndex + " Result: " + curNode.getoutcome(outcomeIndex));
                  break;
               }
               counter++;
            }
            //no parent
         }else{
            //System.out.println("NULL");
            int outcomeIndex = random_index(curNode.getProbability(0));
            output.put(cenario, curNode.getoutcome(outcomeIndex));
            //System.out.println("Node: " + curNode.getNodeName() + " Chance: " + outcomeIndex + " Result: " + curNode.getoutcome(outcomeIndex));
         }
      }
      return output;
   }


   public static void LikeleyHood_Weighting(String query, String[] evidence, Baysian_Network network, int trysize){
      HashMap<String, String> SampleSize = creatingTopDown(network);
      HashMap<String, Float> output = new HashMap<String, Float>();
      for(String outcomeName : network.getNode(query).getoutcome()){
         output.put(outcomeName, 0f);
      }
      int total = 0;
      for(int i = 0; i < trysize; i++){
         HashMap<String, String> sample = weighted_Sampling(SampleSize, network, evidence);
         System.out.print("<");
         for(String name: sample.keySet()){
             String key = name;
             String value = sample.get(name);
             System.out.print(name + ": " + value + "    ");
         }
         System.out.println(">");
         boolean flag = true;
         for(int k = 0; k < evidence.length; k+=2){
            if(sample.get(evidence[k]).equals(evidence[k+1]) != true){
               flag = false;
               break;
            }
         }
         if(flag == true){
            float a = output.get(sample.get(query)) + Float.parseFloat(sample.get("weight"));
            output.put(sample.get(query), a);
            total++;
         }
      }

      System.out.print("<");
      float totalV = 0;
      for(String name: output.keySet()){
          String key = name;
          float value = output.get(name);
          totalV += value;
          System.out.print(name + ": " + value + "    ");
      }
      System.out.println(">");
      System.out.println("Total Sucess: " + total);
      System.out.println("Normalized Value: ");
      for(String name: output.keySet()){
         String key = name;
         float value = output.get(name);
         System.out.print(name + ": " + value/totalV + "    ");
     }
   }


   public static void Rejection_Sampling(String query, String[] evidence, Baysian_Network network, int trysize){
      HashMap<String, String> Samplesize = creatingTopDown(network);
      HashMap<String, Integer> output = new HashMap<String, Integer>();
      for(String outcomeName : network.getNode(query).getoutcome()){
         output.put(outcomeName, 0);
      }
      int total = 0;
      for(int i = 0; i < trysize; i++){
         HashMap<String, String> sample = prior_Sampling(Samplesize, network);
         //
         System.out.print("<");
         for(String name: sample.keySet()){
             String key = name;
             String value = sample.get(name);
             System.out.print(name + ": " + value + "    ");
         }
         System.out.println(">");
         //
         boolean flag = true;
         for(int k = 0; k < evidence.length; k+=2){
            if(sample.get(evidence[k]).equals(evidence[k+1]) != true){
               flag = false;
               break;
            }
         }
         if(flag == true){
            int c = output.get(sample.get(query)) + 1;
            output.put(sample.get(query), c);
            total++;
         }
      }

      System.out.print("<");
      for(String name: output.keySet()){
          String key = name;
          int value = output.get(name);
          System.out.print(name + ": " + value + "    ");
      }
      System.out.println(">");
      System.out.println("Total Sucess: " + total);
      System.out.println("Noramlized Value: ");
      for(String name: output.keySet()){
         String key = name;
         int value = output.get(name);
         System.out.println(name + ": " + (double)value/total + "    ");
     }
   }

   //generate a random index
   private static int random_index(float[] input){
      int index = 0;
      float randNumb = (float)Math.random();
      float accumuluatedChance = 0f;
      //System.out.println("Generated Number: " + randNumb);
      for(int i = 0; i < input.length; i++){
         accumuluatedChance += input[i];
         if(accumuluatedChance >= randNumb){
            return i;
         }
      }
      return input.length-1;

   }



}
