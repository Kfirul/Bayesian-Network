import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;

public class ReadTxtFile {


    public static void main(String[] args) {

        try {

            URL fileURL = ReadTxtFile.class.getResource("input.txt");
            File text = new File(fileURL.toURI());
            FileReader ffw = new FileReader(text);
            BufferedReader bbw = new BufferedReader(ffw);
            String curr = bbw.readLine();
            System.out.println(curr);
            ReadXmlFile xml=new ReadXmlFile(curr);
            BayesianNet bayesianNet= new BayesianNet(xml);

            FileWriter fw = new FileWriter("output.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            curr = bbw.readLine();
            while (curr != null) {
                bw.append(bayesianNet.chooseAlgo(queryValues(curr),whichAlgo(curr))+'\n');
                curr = bbw.readLine();
            }
            bbw.close();
            bw.close();

        } catch (IOException e) {
            System.out.println(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param query
     * @return
     */
    public static ArrayList<String> queryValues(String query){
        ArrayList<String> queryArr=new ArrayList<String>();
        String str="";
        for(int i=2;i<query.length() && query.charAt(i)!=')';i++){

            if(query.charAt(i)!='|' && query.charAt(i)!='='&&query.charAt(i)!=',')
                str=str+String.valueOf(query.charAt(i));
              else{
                queryArr.add(str);
                str="";
            }
        }
        queryArr.add(str); //add the last outcome
        return queryArr;
    }

    /**
     *
     * @param query received from input file
     * @return the function we need to use
     *
     */
    public static char whichAlgo(String query){
        return query.charAt(query.length()-1);
    }
}
