import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadTxtFile {


    public String readText(String fileName){
        String query="";
        try {
            FileReader ffw = new FileReader(fileName);
            BufferedReader bbw = new BufferedReader(ffw);
            String curr = bbw.readLine();
            curr = bbw.readLine();

            // while (curr != null) {
                query= curr;
            //}
        }
        catch(IOException e){
            System.out.println(e);
        }
        return query;
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
