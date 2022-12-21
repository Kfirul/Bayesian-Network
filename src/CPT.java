import java.util.ArrayList;
import java.util.Arrays;

public class CPT {
    
    private String[][]truthTable;
    private ArrayList<String> tables=new ArrayList<String>();

    public CPT(String nameVar ,ArrayList<String> outcomes,ArrayList<Variable> fathers,ArrayList<String> tables){
        this.tables=new ArrayList<String>(tables);
        int loops= tables.size();
        int indexFather=0;
        truthTable=new String [tables.size()+1][fathers.size()+2];
        for(int name=0;name<fathers.size();name++){
            truthTable[0][name]=fathers.get(name).getName();
        }

        for(int j=0;j<truthTable[0].length-2;j++){
            loops=loops/fathers.get(indexFather).getOutcomes().size();
            int loopOutcome=loops;
            int indexOutcome=0;
            for(int i=1;i<truthTable.length;i++){
                if(loopOutcome==0){
                    indexOutcome++;
                    if(indexOutcome==fathers.get(indexFather).getOutcomes().size())
                        indexOutcome=0;
                    loopOutcome=loops;
                }
                truthTable[i][j]=fathers.get(indexFather).getOutcomes().get(indexOutcome);
                loopOutcome--;
            }
            indexFather++;
        }
        truthTable[0][truthTable[0].length - 2]=nameVar;
        int indexOutcome=0;
        for(int i=1;i<truthTable.length;i++) {
            truthTable[i][truthTable[0].length - 2] = outcomes.get(indexOutcome);
            indexOutcome++;
            if (indexOutcome == outcomes.size())
                indexOutcome = 0;
        }
        truthTable[0][truthTable[0].length - 1]="Prob";
        for(int i=1;i<truthTable.length;i++) {
            truthTable[i][truthTable[0].length - 1] = tables.get(i-1);
        }
    }

    public String[][] getTruthTable() {
        return truthTable;
    }

    public void setCpt(String[][] truthTable) {
        this.truthTable = truthTable;
    }

    /**
     * Receives an arraylist of the query and returns an array with the values of Outcomes only
     * @param probs the arraylist
     * @return  array with the values of Outcomes only
     *
     */
    public String[] getOutcomesArr(ArrayList<String>probs){

        String []outcomesArr=new String[truthTable[0].length-1];
        for(int j=0;j<truthTable[0].length-1;j++) {
            for (int i = 0; i < probs.size();i=i+2) {
                if (probs.get(i).equals(truthTable[0][j]))
                    outcomesArr[j]=probs.get(i+1);
            }
        }
        return outcomesArr;
    }

    /**
     * Found the coorect row at the CPT and return the appropriate prob
     * @param outcomesArr the array of outcomes to found
     * @return the appropriate prob
     *
     */
    public double getProbNum(String[] outcomesArr){
        for(int i=1;i< truthTable.length;i++){
            if(equalArr(outcomesArr,truthTable[i]))
                return Double.parseDouble(truthTable[i][truthTable[0].length - 1]);

        }
        return 0;
    }

    /**
     * Equals between to arrays
     * @param a first array to compare
     * @param b second array to compare
     * @return true if equals, otherwise false
     *
     */
    public boolean equalArr(String[]a,String[]b){
        for(int i=0;i<a.length;i++){
            if(!a[i].equals(b[i]))
                return false;
        }
        return true;
    }

    /**
     * Print the CPT
     * @return the CPT
     *
     */
    @Override
    public String toString() {

        for (int i = 0; i < truthTable.length; i++) {
            System.out.println(Arrays.toString(truthTable[i]));
        }
        return"";
    }

}
