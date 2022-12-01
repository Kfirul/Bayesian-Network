import java.util.ArrayList;
import java.util.Arrays;

public class CPT {
    
    private String[][]cpt;
    private ArrayList<String> tables=new ArrayList<String>();

    public CPT(String nameVar ,ArrayList<String> outcomes,ArrayList<Variable> fathers,ArrayList<String> tables){
        this.tables=new ArrayList<String>(tables);
        int loops= tables.size();
        int indexFather=0;
        cpt=new String [tables.size()+1][fathers.size()+2];
        for(int name=0;name<fathers.size();name++){
            cpt[0][name]=fathers.get(name).getName();
        }

        for(int j=0;j<cpt[0].length-2;j++){
            loops=loops/fathers.get(indexFather).getOutcomes().size();
            int loopOutcome=loops;
            int indexOutcome=0;
            for(int i=1;i<cpt.length;i++){
                if(loopOutcome==0){
                    indexOutcome++;
                    if(indexOutcome==fathers.get(indexFather).getOutcomes().size())
                        indexOutcome=0;
                    loopOutcome=loops;
                }
                cpt[i][j]=fathers.get(indexFather).getOutcomes().get(indexOutcome);
                loopOutcome--;
            }
            indexFather++;
        }
        cpt[0][cpt[0].length - 2]=nameVar;
        int indexOutcome=0;
        for(int i=1;i<cpt.length;i++) {
            cpt[i][cpt[0].length - 2] = outcomes.get(indexOutcome);
            indexOutcome++;
            if (indexOutcome == outcomes.size())
                indexOutcome = 0;
        }
        cpt[0][cpt[0].length - 1]="Prob";
        for(int i=1;i<cpt.length;i++) {
            cpt[i][cpt[0].length - 1] = tables.get(i-1);
        }
    }

    public String[][] getCpt() {
        return cpt;
    }

    public void setCpt(String[][] cpt) {
        this.cpt = cpt;
    }


    public String[] getOutcomesArr(ArrayList<String>probs){
        String []outcomesArr=new String[cpt[0].length-1];
        for(int j=0;j<cpt[0].length-1;j++) {
            for (int i = 0; i < probs.size(); i++) {
                if (probs.get(i).equals(cpt[0][j]))
                    outcomesArr[j]=probs.get(i+1);
            }
        }
        return outcomesArr;
    }

    public double getProbNum(String[] outcomesArr){
        for(int i=1;i< cpt.length;i++){
            if(equalArr(outcomesArr,cpt[i])) {
                System.out.println(cpt[i][cpt[0].length - 1]);
                return Double.parseDouble(cpt[i][cpt[0].length - 1]);

            }
        }
        return -1;
    }
    public boolean equalArr(String[]a,String []b){
        for(int i=0;i<a.length;i++){
            if(!a[i].equals(b[i]))
                return false;
        }
        return true;
    }













    @Override
    public String toString() {

        for (int i = 0; i < cpt.length; i++) {
            System.out.println(Arrays.toString(cpt[i]));
        }
        return"";
    }

}
