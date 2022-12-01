import org.w3c.dom.ls.LSOutput;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        ReadXmlFile xmlFile = new ReadXmlFile("C:\\Users\\USER\\Desktop\\alarm_net.xml");
        ReadXmlFile xmlFile2 = new ReadXmlFile("C:\\Users\\USER\\Desktop\\big_net.xml");

        // xmlFile.getListVariable();

        ArrayList<String> a = xmlFile.getVariableName();
////        System.out.println(a);
//        for (int i = 0; i < a.size(); i++) {
////            System.out.println("Variable : " +a.get(i));
//           // ArrayList<String> b = xmlFile.getOutcomes(a.get(i));
//           // System.out.println("Outcomes : " + b);
////            ArrayList<String> c = xmlFile.getFathers(a.get(i));
////            System.out.println("Fathers : "+c);
////            ArrayList<String> d = xmlFile.getTables(a.get(i));
////            System.out.println("Tables : "+d);
////            System.out.println("----------------");
//
             BayesianNet net=new BayesianNet(xmlFile2);
        System.out.println( net.getVariableByName("C3").getCpt());
//        ArrayList<String>z=new ArrayList<>();
//        z.add("B");
//        z.add("T");
//        z.add("B");
//        z.add("F");
//        z.add("E");
//        z.add("F");
   //    System.out.println(net.simpleDeduction(z));
        //System.out.println(net.getVariableByName("A"));
        //System.out.println(net);
          //  System.out.println(net.getVariableByName("B1").getFathers());
//            ArrayList<String> z = new ArrayList<String>(b);
//            System.out.println(z);
//        }
        //  ReadTxtFile t=new ReadTxtFile();
       // System.out.println(t.queryValues("P(B=T|J=T,M=T),1"));
       // String str=t.readText("C:\\Users\\USER\\Desktop\\input.txt");


    }
       // System.out.println(xmlFile.getTables(a.get(2)).size());


    }