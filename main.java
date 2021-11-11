=
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import bn.base.Assignment;
import bn.base.BayesianNetwork;
import bn.base.StringValue;
import bn.core.*;
import bn.inference.ExactInference;
import bn.parser.XMLBIFParser;
import bn.parser.BIFParser;

import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException{
        //Rejection_Sampling hailfinder.xml 1000 combvermo no
        MakeInference(args);
    }

    //Inquiry arppoximaionType networkfile trysize query evidence
    public static void MakeInference(String[] args)throws IOException, ParserConfigurationException, SAXException{
        if(args[0].equals("Rejection_Sampling")){
            Baysian_Network network = Baysian_Dom_parser.writeNetWork(args[1]);
            int trysize = Integer.parseInt(args[2]);
            String query = args[3];
            String[] evidence = Arrays.copyOfRange(args, 4, args.length);
            ApproximateInference.Rejection_Sampling(query, evidence, network, trysize);
        }else if(args[0].equals("LikelyHood_Weighting")){
            Baysian_Network network = Baysian_Dom_parser.writeNetWork(args[1]);
            int trysize = Integer.parseInt(args[2]);
            String query = args[3];
            String[] evidence = Arrays.copyOfRange(args, 4, args.length);
            ApproximateInference.LikeleyHood_Weighting(query, evidence, network, trysize);
        }else{
            System.out.println("No Function Found, Program Exit");
        }
    }
}
