import java.util.ArrayList;

public class Factor {
    private ArrayList<String> varFactor=new ArrayList<String>();
    private ArrayList<Double> prob=new ArrayList<Double>();
    private ArrayList<ArrayList<String>> factorTab=new ArrayList<ArrayList<String>>();

    public Factor(){
        varFactor=new ArrayList<String>();
        prob=new ArrayList<Double>();
        factorTab=new ArrayList<ArrayList<String>>();
    }

   public Factor(Variable v){
       String [][] cptCopy=v.getCpt().getTruthTable();
       for(int j=0;j<cptCopy[0].length-1;j++){
            varFactor.add(cptCopy[0][j]);
       }

       for (String s:v.getTables()) {
           prob.add(Double.parseDouble(s));

       }
       for(int i=1;i<cptCopy.length;i++){
           factorTab.add(new ArrayList<String>());
           for (int j=0;j<cptCopy[0].length-1;j++){
               factorTab.get(i-1).add(cptCopy[i][j]);
           }
       }
   }

    /**
     * remove all the options that not equal to eviOutcome
     * @param eviName the evidence name
     * @param eviOutcome to not remove
     */
   public void removeEvidenceOutcomes(String eviName,String eviOutcome){
        int index =indexVarName(eviName);
       //If evidence is exist in the factor , remove all the outcomes that not equal to eviOutcome
        if(indexVarName(eviName)!=-1){
            for(int i=0;i< factorTab.size();i++){
                if(!factorTab.get(i).get(index).equals(eviOutcome)){
                    factorTab.remove(i);
                    prob.remove(i);
                }
            }
            varFactor.remove(index);
            removeCol(index);
        }
   }
    public void eliminate(String varName)  {
       int indexVar=indexVarName(varName);
       varFactor.remove(indexVar);

       //Remove the outcomes of varName from the factorTab
       removeCol(indexVar);

       //Summing up the probs if the arrays of the outcomes are equal
       for(int i=0;i<factorTab.size();i++){
           for (int j = i+1; j < factorTab.size(); j++) {
               if(factorTab.get(i).equals(factorTab.get(j))){
                   double sum=prob.get(i)+prob.get(j);
                   prob.set(i,sum);
                   prob.remove(j);
                   factorTab.remove(j);
               }
           }
       }
    }

    /**
     * Remove the outcomes of varName from the factorTab.
     * The function goes through the entire matrix and deletes the corresponding index from each array
     * @param index to remove
     */
    public void removeCol(int index) {
        for (int i = 0; i < factorTab.size(); i++) {
            for (int j = 0; j < factorTab.get(i).size(); j++) {
                if (j == index)
                    factorTab.get(i).remove(j);
            }
        }
    }

    /**
     * return the index of the variable
     * @param varName to found
     * @return the index of the variable
     * @throws Exception if index not found
     */
            public int indexVarName (String varName) {
                for (int i = 0; i < varFactor.size(); i++) {
                    if (varName.equals(varFactor.get(i)))
                        return i;
                }
                return -1;
            }

            public ArrayList<Double> getProb () {
                return prob;
            }

            public void setProb (ArrayList < Double > prob) {
                this.prob = prob;
            }

            public ArrayList<ArrayList<String>> getFactorTab () {
                return factorTab;
            }

            public void setFactorTab (ArrayList < ArrayList < String >> factorTab) {
                this.factorTab = factorTab;
            }

            public ArrayList<String> getVarFactor () {
                return varFactor;
            }

            public void setVarFactor (ArrayList < String > varFactor) {
                this.varFactor = varFactor;
            }

            public String toString () {
                System.out.println("Titels: " + varFactor + "\n probs: " + prob + "\n table:\n");
                for (ArrayList<String> f : factorTab) {
                    System.out.println(f);

                }
                return "";
            }
        }

