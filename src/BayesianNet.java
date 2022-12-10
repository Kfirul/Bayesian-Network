import java.util.ArrayList;

public class BayesianNet {

    private ArrayList<Variable> arrVariables= new ArrayList<Variable>();
    private ArrayList<Factor> arrFactor=new ArrayList<Factor>();

    public BayesianNet(){
        arrVariables=null;
    }

    public BayesianNet(BayesianNet bayNet){
        this.arrVariables = new ArrayList<Variable>(bayNet.arrVariables);
    }



    public BayesianNet(ReadXmlFile xmlFile){
        ArrayList<String>tempNames=xmlFile.getVariableName();

        for(String name: tempNames) {
            if (!isExistByName(name))
                arrVariables.add(new Variable(name, xmlFile.getOutcomes(name), xmlFile.getFathers(name), xmlFile.getTables(name), this));
        }

        // Add fathers as a variables
        for(Variable v: arrVariables){
            for(int i=0;i<v.getListOfFathers().size();i++){
                for(Variable v1 : arrVariables)
                if(v.getListOfFathers().get(i).equals(v1.getName()))
                v.getFathers().add(v1);
            }
        }
        for(Variable v: arrVariables) {
            v.createCPT();
        }
        for (Variable v:arrVariables) {
            arrFactor.add(new Factor(v));

        }
        }

    /**
     * The function receive query and check if the query is already at cpt we will return the result at cpt
     * If not, we will use appropriate function according to the input
     * @param query
     * @param func
     *
     */
    public String chooseAlgo(ArrayList<String> query,char func){

        //If the Information and the result of the query is already given in one of the - CPT
        if(query.size()/2==getVariableByName(query.get(0)).getListOfFathers().size()+1) {
            boolean same = false, querySame = true;
            for (int j = 0; j < getVariableByName(query.get(0)).getListOfFathers().size(); j++) {
                same=false;
                for (int i = 2; i < query.size(); i = i + 2) {

                    if (query.get(i).equals(getVariableByName(query.get(0)).getListOfFathers().get(j)))
                        same = true;
                }
                if (!same)
                    querySame = false;
            }
            if (querySame) {
                return "" + getVariableByName(query.get(0)).getCpt().getProbNum(getVariableByName(query.get(0)).getCpt().getOutcomesArr(query)) + ",0,0";
            }
        }
        if(func=='1') return simpleDeduction(query);
        else if (func=='2') return variableEliminationABC();
        return "function 3";
    }

    /**
     *
     *
     *
     */

    public String simpleDeduction(ArrayList<String> query){

        ArrayList<Variable> hidden=this.getHidden(query);

        //Calculate the result of the original query
        ArrayList<ArrayList<String>> combineOriginal=combination(hidden,query);
        ArrayList<Double>results=new ArrayList<Double>();
        results.add(resultsCombine(combineOriginal));
        Variable theQuery=getVariableByName(query.get(0));

        //Calculate the result of each combine by the outcomes of the Variable query except the original
        for(int i=0;i<theQuery.getOutcomes().size();i++) {
            if (!theQuery.getOutcomes().get(i).equals(query.get(1))) {
                ArrayList<String> complementary= new ArrayList<String>(query);
                complementary.set(1, theQuery.getOutcomes().get(i));
                ArrayList<ArrayList<String>> combine = combination(hidden, complementary);
               double simpleDec=resultsCombine(combine);
               results.add(simpleDec);
            }
        }

        //Sum all the combination results for each outcome of the query
       double sumCombination=0;
        for(int i=0;i<results.size();i++)
            sumCombination=sumCombination+results.get(i);

        double normalize=results.get(0)/sumCombination;
        normalize=100000*normalize;
        normalize=Math.round(normalize);
        normalize=normalize/100000;
        return ""+normalize+","+((combineOriginal.size()-1)*theQuery.getOutcomes().size()+theQuery.getOutcomes().size()-1)+","+((arrVariables.size()-1)*combineOriginal.size()*theQuery.getOutcomes().size());
    }

    /**
     * Returns the hidden variables for a given query
     * @param query
     * @return the hidden array
     */
    public ArrayList<Variable> getHidden(ArrayList<String> query){
        ArrayList<Variable> hidden=new ArrayList<Variable>();
        for (Variable v:arrVariables) {
            boolean found=false;
            for (int i=0;i< query.size();i=i+2) {

                if(v.getName().equals(query.get(i)))
                    found = true;

            }

            if(!found)
                hidden.add(v);
        }
        return hidden;
    }

    /**
     *
     * Create all the combination by the hidden Variables outcomes
     */

    // Do combination by the hidden variables outcomes
    public ArrayList<ArrayList<String>> combination(ArrayList<Variable> hidden,ArrayList<String> query) {
        int mul = 1;
        for (Variable v : hidden)
            mul = mul * v.getOutcomes().size();

        ArrayList<ArrayList<String>> combine = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < mul; i++) {
            combine.add(new ArrayList<String>(query));
        }

        int loop = mul;
        for (Variable v : hidden) {
            loop = loop / v.getOutcomes().size();
            int loopPerOutcome = loop;
            int indexOutcome = 0;
            for (int i = 0; i < mul; i++) {
                combine.get(i).add(v.getName());
                combine.get(i).add(v.getOutcomes().get(indexOutcome));
                loopPerOutcome--;
                if (loopPerOutcome == 0) {
                    loopPerOutcome = loop;
                    indexOutcome++;
                    if (indexOutcome == v.getOutcomes().size())
                        indexOutcome = 0;
                }
            }
        }

        return combine;
    }

    /**
     *
     * Return the result of combination by their CPT
     */

    public double resultsCombine(ArrayList<ArrayList<String>> combine){
        double simpleDec=0;
        for(ArrayList<String>prob:combine){
            double mulEachConatin=1.0;
            for(Variable v:arrVariables){
                mulEachConatin= mulEachConatin*v.getCpt().getProbNum(v.getCpt().getOutcomesArr(prob));
            }
            simpleDec=simpleDec+mulEachConatin;
        }
        return simpleDec;
    }
    public String variableEliminationABC(){
        return "";
    }

    /**
     * This function sort the Factors by their size
     * If the Factor at the same size the function sort by the ASCII value's variable
     */
    public void sortFactorAlphabetical(){
        for(int i=0;i< arrFactor.size();i++){
            int min=getASCIIValue(arrFactor.get(i));
            for(int j=i+1;j< arrFactor.size();j++) {
                if (getASCIIValue(arrFactor.get(j)) < min) {
                    min=getASCIIValue(arrFactor.get(j));
                    swap(arrFactor, i, j);
                }
            }
        }
        for(int i=0;i< arrFactor.size();i++){
            int min=arrFactor.get(i).getFactorTab().size()*arrFactor.get(i).getVarFactor().size();
            for(int j=i+1;j< arrFactor.size();j++) {
                if (arrFactor.get(j).getFactorTab().size() * arrFactor.get(j).getVarFactor().size() < min) {
                    min=arrFactor.get(j).getFactorTab().size() * arrFactor.get(j).getVarFactor().size();
                    swap(arrFactor, i, j);
                }
            }
        }

    }

    /**
     * The function return the ASCII value of factor by his Variables
     * @param f the factor
     * @return the ASCII value
     */
    public int getASCIIValue(Factor f){
        int asciiVal=1;
        for(Variable v: f.getVarFactor()){
            for(int i=0;i<v.getName().length();i++){
            asciiVal=asciiVal*v.getName().charAt(i);
    }
        }
        return asciiVal;
    }

    /**
     * The function swap the factors int the array
     * @param arr the array
     * @param i index to swap
     * @param j index to swap
     */
    public void swap(ArrayList<Factor> arr,int i,int j){
        Factor temp=arr.get(i);
        arr.set(i,arr.get(j));
        arr.set(j,temp);
    }



    public void setBayesianNet(ArrayList<Variable> bayesianNet) {
        this.arrVariables = bayesianNet;
    }
    public boolean isExist(Variable v){
        for (Variable v1:arrVariables) {
            if(v1.equals(v))
                return true;

        }
        return false;
    }

    /**
     *Return if variable exist in the bayesian network
     */

    public boolean isExistByName(String name ){
        if(arrVariables!=null) {
            for (Variable v1 : arrVariables) {
                if (v1.getName().equals(name))
                    return true;
            }
        }
        return false;
    }

    public void addVariable(Variable v){
        if(!isExist(v))
        arrVariables.add(v);
    }

    /**
     *
     * Return the variable by his name
     */
    public Variable getVariableByName(String name){
        for (Variable v1:arrVariables) {
            if(v1.getName().equals(name))
                return v1;
        }
        return null;
    }

    public ArrayList<Variable> getArrVariables() {
        return arrVariables;
    }

    public void setArrVariables(ArrayList<Variable> arrVariables) {
        this.arrVariables = arrVariables;
    }
    public ArrayList<Factor> getArrFactor() {
        return arrFactor;
    }

    public void setArrFactor(ArrayList<Factor> arrFactor) {
        this.arrFactor = arrFactor;
    }
    @Override
    public String toString() {
//        ArrayList<String> a=new ArrayList<String>();
//        for(int i=0;i<arrVariables.size();i++){
//            a.add(arrVariables.get(i).getName());
//        }
        return "BayesianNet{" +
                "arrVariables=\n" + arrVariables +
                '}';
    }
}
