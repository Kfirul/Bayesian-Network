import java.util.ArrayList;
import java.util.HashMap;

public class Factor {
    private ArrayList<String> varFactor=new ArrayList<String>();
    private ArrayList<Double> prob=new ArrayList<Double>();
    private ArrayList<ArrayList<String>> factor=new ArrayList<ArrayList<String>>();


   public Factor(Variable v){
       String [][] cptCopy=v.getCpt().gettruthTable();
       for(int j=0;j<cptCopy[0].length-1;j++){
            varFactor.add(cptCopy[0][j]);
       }

       for(int i=1;i<cptCopy[i].length;i++){
           prob.add(Double.parseDouble(cptCopy[i][cptCopy.length-1]));
       }
       for(int i=1;i<cptCopy.length;i++){
           for (int j=0;j<cptCopy[0].length-1;j++){
               factor.get(i-1).add(cptCopy[i][j]);
           }
       }
   }
}
