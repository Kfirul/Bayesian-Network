import java.util.ArrayList;
/**
 * This class represent the Factor
 */
public class Factor {
    private ArrayList<Variable> varFactor=new ArrayList<Variable>();
    private ArrayList<Double> prob=new ArrayList<Double>();
    private ArrayList<ArrayList<String>> factorTab=new ArrayList<ArrayList<String>>();

    /**
     * This constructor build factor from variable CPT
     * @param v the variable to build from
     */
   public Factor(Variable v){
       String [][] cptCopy=v.getCpt().getTruthTable();
       for(int j=0;j<v.getFathers().size();j++){
            varFactor.add(v.getFathers().get(j));
       }
       varFactor.add(v);

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
     * This constructor get two Factors and join them to one factor
     * @param f1 the first factor to join
     * @param f2 the second factor to join
     */
   public Factor(Factor f1, Factor f2){

        //Create new var factor arr of union between f1 & f2
        this.varFactor=new ArrayList<>(f1.getVarFactor());
        for(Variable v:f2.varFactor){
            boolean found=false;
            for(Variable v2: this.varFactor){
                if(v2.getName().equals(v.getName()))
                    found = true;
            }
            if(!found)
                varFactor.add(v);
        }

        //Calculate the size of the table
        int tabSize=1;
        for(int i=0;i< varFactor.size();i++)
            tabSize=tabSize* varFactor.get(i).getOutcomes().size();

        //Initializes the array so that there is 1 in each cell
        for(int i=0;i<tabSize;i++)
            prob.add(1.0);

        //Initializes the table
        for(int i=0;i<tabSize;i++)
            factorTab.add(new ArrayList<String>());

        int loops=tabSize;
       int indexVar=0;
       for(int j=0;j<varFactor.size();j++){
           loops=loops/varFactor.get(indexVar).getOutcomes().size();
           int loopOutcome=loops;
           int indexOutcome=0;
           for(int i=0;i<tabSize;i++){
               if(loopOutcome==0){
                   indexOutcome++;
                   if(indexOutcome==varFactor.get(indexVar).getOutcomes().size())
                       indexOutcome=0;
                   loopOutcome=loops;
               }
               factorTab.get(i).add(varFactor.get(indexVar).getOutcomes().get(indexOutcome));
               loopOutcome--;
           }
           indexVar++;
       }
       //Calculate the prob

       //Add prob of f1

       ArrayList<Integer>arrIndex=createArrIndex(f1);
       probFit(f1,arrIndex);

       //Add prob of f2

       arrIndex=createArrIndex(f2);
       probFit(f2,arrIndex);

   }

    /**
     * The function receives an array of strings and if the array is equal to the array in the table
     * we multiply the desired position in the prob of the array
     * @param f the factor
     * @param arrIndex the array that include the indexes we need to compare
     *
     */
    public void probFit(Factor f,ArrayList<Integer> arrIndex){
        for(int i=0;i<f.getFactorTab().size();i++){
            for(int j=0;j<this.factorTab.size();j++) {

                if (equalArrByIndex(arrIndex, f.factorTab.get(i), this.factorTab.get(j))) {
                    this.prob.set(j, this.prob.get(j) * f.getProb().get(i));
                }
            }
        }
    }

    /**
     * Check if arrays are equal by specif index
     * @param arrIndex the specif index
     * @param arr to compare
     * @param arrFromTab to compare
     * @return true if equals otherwise false
     *
     */

    public boolean equalArrByIndex(ArrayList<Integer>arrIndex,ArrayList<String> arr,ArrayList<String>arrFromTab){
       ArrayList<String> tempArr=new ArrayList<>();
       for(int j=0;j<arr.size();j++){
       for(int i=0;i<arrFromTab.size();i++){
           if (i==arrIndex.get(j))
               tempArr.add(arrFromTab.get(i));

           }
           }
               if(tempArr.equals(arr))
                   return true;
       return false;
   }

    /**
     * Return an array with the indexes that we need to go over in the table of the factor
     * @param f the Factor we create for him array
     * @return array of index
     */
  public ArrayList<Integer> createArrIndex(Factor f){
      ArrayList<Integer>arrIndex=new ArrayList<>();
      for (int i=0;i<f.getVarFactor().size();i++)
          arrIndex.add(indexVarName(f.getVarFactor().get(i).getName()));
      return arrIndex;
  }

    /**
     * remove all the options that not equal to eviOutcome
     * @param eviName the evidence name
     * @param eviOutcome to not remove
     */
   public void removeEvidenceOutcomes(String eviName,String eviOutcome){
        int index =indexVarName(eviName);
       //If evidence is exist in the factor , remove all the outcomes that not equal to eviOutcome
        if(index!=-1){
            for(int i=0;i< factorTab.size();i++){
                if(!factorTab.get(i).get(index).equals(eviOutcome)){
                    factorTab.remove(i);
                    prob.remove(i);
                    i--;
                }
            }
            varFactor.remove(index);
            removeCol(index);
        }
   }

    /**
     * Take a factor and sum out a variable - marginalization
     * @param varName the variable's name
     */

    public void eliminate(String varName)  {
       int indexVar=indexVarName(varName);
       varFactor.remove(indexVar);

       //Remove the outcomes of varName from the factorTab
       removeCol(indexVar);
   // int count=0;
       //Summing up the probs if the arrays of the outcomes are equal
       for(int i=0;i<factorTab.size();i++){
           for (int j = i+1; j < factorTab.size(); j++) {
               if(factorTab.get(i).equals(factorTab.get(j))){
                   double sum=prob.get(i)+prob.get(j);
                  // count++;
                   prob.set(i,sum);
                   prob.remove(j);
                   factorTab.remove(j);
                   j--;
               }
           }
       }
        //System.out.println("sum:" + count);
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
            if (varName.equals(varFactor.get(i).getName()))
                return i;
                }
        return -1;
            }

    //Getters

    public ArrayList<Double> getProb () {
                return prob;
            }

            public ArrayList<ArrayList<String>> getFactorTab () {
                return factorTab;
            }

            public ArrayList<Variable> getVarFactor () {
                return varFactor;
            }

    /**
     * Print the Factor
     * @return the Factor
     *
     */
            public String toString () {
                System.out.println("Titels: ");
                for (Variable v:varFactor) {
                    System.out.print(v.getName()+"  ");
                }
                System.out.println("\n probs: " + prob + "\n table:\n");
                for (ArrayList<String> f : factorTab) {
                    System.out.println(f);

                }
                return "";
            }
        }

