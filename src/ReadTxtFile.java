import java.io.*;
import java.util.ArrayList;

public class ReadTxtFile {


    public static void main(String[] args) {
    String fileName ="";

        try {
            FileReader ffw = new FileReader(fileName);
            BufferedReader bbw = new BufferedReader(ffw);
            String curr = bbw.readLine();
            //ReadXmlFile xml=new ReadXmlFile(curr);
            //BayesianNet bayesianNet= new BayesianNet(xml);

            while (curr != null) {
                curr = bbw.readLine();
                // BayesianNet bayesianNetCopy= new BayesianNet(bayesianNet);
                // bayesianNetCopy.chooseAlgo(queryValues(curr),whichAlgo(curr));
                FileWriter fw = new FileWriter(fileName);
                BufferedWriter bw = new BufferedWriter(fw);
                //bw.append(bayesianNetCopy.chooseAlgo(queryValues(curr),whichAlgo(curr));+'\n');

            }
            bbw.close();


        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public ArrayList<String> queryValues(String query){
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
     * @param query
     * @return the function we need to use
     *
     */
    public char whichAlgo(String query){
        return query.charAt(query.length()-1);
    }
}
