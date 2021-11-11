
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Baysian_Dom_parser {
    public static Baysian_Network writeNetWork(String name) throws IOException, ParserConfigurationException, SAXException{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(name);

        NodeList list = doc.getElementsByTagName("VARIABLE");
        //create the framework for the BaysianNetwork
        Baysian_Network bNetwork = new Baysian_Network(list.getLength());
        int nodeIndex = 0;
        //instantiate the node and add them into the network
        for(int i = 0; i < list.getLength(); i++){
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                String Nodename = element.getElementsByTagName("NAME").item(0).getTextContent();
                Baysian_Node bNode = new Baysian_Node(Nodename);
                for(int k = 0; k < element.getElementsByTagName("OUTCOME").getLength(); k++){
                    bNode.addoutcome(element.getElementsByTagName("OUTCOME").item(k).getTextContent());
                }
                bNetwork.setNode(Nodename, bNode);
                nodeIndex++;
            }
        }
        //System.out.println("Size of network: "+ bNetwork.getsize());
        //Modify each node
        NodeList list2 = doc.getElementsByTagName("DEFINITION");
        for(int j = 0; j < list2.getLength(); j++){
            Node node1 = list2.item(j);
            if(node1.getNodeType() == Node.ELEMENT_NODE){
                Element element1 = (Element) node1;
                String nodename = element1.getElementsByTagName("FOR").item(0).getTextContent();
                Baysian_Node bNode = bNetwork.getNode(nodename);
                //add parent connection
                if(element1.getElementsByTagName("GIVEN").getLength() > 0){
                    //System.out.println("There is parent");
                    for(int s = 0; s < element1.getElementsByTagName("GIVEN").getLength(); s++){
                        //element1.getElementsByTagName("GIVEN").item(s).getTextContent() returns the parent node of the currentnode
                        bNode.addParent(bNetwork.getNode(element1.getElementsByTagName("GIVEN").item(s).getTextContent()));
                        //add link from parent node to child node
                        Baysian_Node parent = bNetwork.getNode(element1.getElementsByTagName("GIVEN").item(s).getTextContent());
                    }
                }else{
                    //System.out.println("No parent");
                    bNode.addParent(null);
                }
                //add table
                String numb = element1.getElementsByTagName("TABLE").item(0).getTextContent();
                String[] numbArr = numb.split(" |\\n");
                List<String> numbList = new ArrayList<String>(Arrays.asList(numbArr));
                numbList.removeAll(Arrays.asList(" ", null, ""));
                float[] nuArr = new float[numbList.size()];
                int count = 0;
                for(String s : numbList){
                    nuArr[count] = Float.parseFloat(s);
                    count++;
                }
                bNode.initializeBaysianTable(nuArr);
            }
        }

        return bNetwork;
    }
}
