import java.util.ArrayList;
import java.util.Arrays;

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
            if (!isExistByName(arrVariables,name))
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
        else if (func=='2') return variableEliminationABC(query);
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

        return ""+round5(normalize)+","+((combineOriginal.size()-1)*theQuery.getOutcomes().size()+theQuery.getOutcomes().size()-1)+","+((arrVariables.size()-1)*combineOriginal.size()*theQuery.getOutcomes().size());
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

    /**
     *
     * @param query
     * @return
     */
    public String variableEliminationABC(ArrayList<String> query){
        ArrayList<Variable> hidden= getHidden(query);
        int countMul=0;
        int countPlus=0;

        //Remove variables that not have effect on the query
        removeIrrelevantVariables(query);

        //Remove irrelevant outcomes of evidence
        for (int i=2;i< query.size();i=i+2){
            for(Factor f:this.arrFactor){
                f.removeEvidenceOutcomes(query.get(i),query.get(i+1));
            }
        }

        for(int i=0;i<arrFactor.size();i++){
            if(arrFactor.get(i).getFactorTab().size()==1){
                arrFactor.remove(i);
                i--;
            }
        }

        hidden.sort(null);

        for (Variable hid: hidden){
            ArrayList<Factor> temp= new ArrayList<Factor>();

            //Creates an array for all the factors that contain the hidden
            for (int i=0;i<arrFactor.size();i++) {
                if (this.isExistByName(arrFactor.get(i).getVarFactor(), hid.getName())) {
                    temp.add(arrFactor.get(i));
                    arrFactor.remove(i);
                    i--;
                }
            }

            //Joins them by size until one factor remains
            sortFactorSizeAlphabetical(temp);

            while(temp.size()>1){
                temp.add(join(temp.get(0),temp.get(1)));
                temp.remove(0);
                temp.remove(0);
                countMul=countMul+temp.get(temp.size()-1).getFactorTab().size();
                sortFactorSizeAlphabetical(temp);
            }
            if(temp.size()>0) {
                temp.get(0).eliminate(hid.getName());
                countPlus=countPlus+temp.get(0).getFactorTab().size()*(hid.getOutcomes().size()-1);
                arrFactor.add(temp.get(0));
            }
        }

        //Joins the query factors by size until one factor remains
        while(arrFactor.size()>1){
            arrFactor.add(join(arrFactor.get(0),arrFactor.get(1)));
            arrFactor.remove(0);
            arrFactor.remove(0);
            countMul=countMul+arrFactor.get(arrFactor.size()-1).getFactorTab().size();
            sortFactorSizeAlphabetical(arrFactor);
        }
        countPlus=countPlus+arrFactor.get(0).getFactorTab().size()-1;

        return ""+round5(normalResult(arrFactor.get(0),query.get(1)))+","+countPlus+","+countMul;
    }

    /**
     *
     * @param f
     * @param qOutcome
     * @return
     */

    public double normalResult(Factor f,String qOutcome){
        double sum=0;
        double queryOutcome=0;
        for(int i=0;i<f.getProb().size();i++){
            sum=sum+f.getProb().get(i);
            if(f.getFactorTab().get(i).get(0).equals(qOutcome))
                queryOutcome=f.getProb().get(i);
        }

        return queryOutcome/sum;
    }
    /**
     * The function receive two factors and join them to one union factor
     * @param f1 the first factor to join
     * @param f2 the second factor to join
     * @return the new factor
     */
    public Factor join(Factor f1, Factor f2){
        return new Factor(f1,f2);
    }

    /**
     * Sort arr according alphabetical order
     * @param arr the array to sort
     */
    public void sortArrAlphabetical(ArrayList<Variable> arr){
        for(int i=0;i< arr.size();i++){
            int min=getASCIIValString(arr.get(i).getName());
            for(int j=i+1;j< arr.size();j++) {
                if (getASCIIValString(arr.get(j).getName()) < min) {
                    min=getASCIIValString(arr.get(j).getName());
                    swapVar(arr, i, j);
                }
            }
        }
    }

    /**
     * This function sort the Factors by their size
     * If the Factor at the same size the function sort by the ASCII value's variable
     */
    public void sortFactorSizeAlphabetical(ArrayList<Factor> arr){
        for(int i=0;i< arr.size();i++){
            int min=getASCIIVal(arr.get(i).getVarFactor());
            for(int j=i+1;j< arr.size();j++) {
                if (getASCIIVal(arr.get(j).getVarFactor()) < min) {
                    min=getASCIIVal(arr.get(j).getVarFactor());
                    swapFac(arr, i, j);
                }
            }
        }
        for(int i=0;i< arr.size();i++){
            int min=arr.get(i).getFactorTab().size()*arr.get(i).getVarFactor().size();
            for(int j=i+1;j< arr.size();j++) {
                if (arr.get(j).getFactorTab().size() * arr.get(j).getVarFactor().size() < min) {
                    min=arr.get(j).getFactorTab().size() * arr.get(j).getVarFactor().size();
                    swapFac(arr, i, j);
                }
            }
        }
    }

    /**
     *
     * @param query
     * @return
     */
    public String variableEliminationMin(ArrayList<String> query){
        ArrayList<Variable> hidden= getHidden(query);
        int countMul=0;
        int countPlus=0;

        //Remove variables that not have effect on the query
        removeIrrelevantVariables(query);

        //Remove irrelevant outcomes of evidence
        for (int i=2;i< query.size();i=i+2){
            for(Factor f:this.arrFactor){
                f.removeEvidenceOutcomes(query.get(i),query.get(i+1));
            }
        }

        for(int i=0;i<arrFactor.size();i++){
            if(arrFactor.get(i).getFactorTab().size()==1){
                arrFactor.remove(i);
                i--;
            }
        }

       // hidden.sort(null);
        //need to add function to sort the hidden

        for (Variable hid: hidden){
            ArrayList<Factor> temp= new ArrayList<Factor>();

            //Creates an array for all the factors that contain the hidden
            for (int i=0;i<arrFactor.size();i++) {
                if (this.isExistByName(arrFactor.get(i).getVarFactor(), hid.getName())) {
                    temp.add(arrFactor.get(i));
                    arrFactor.remove(i);
                    i--;
                }
            }

            //Joins them by size until one factor remains
            sortFactorSizeAlphabetical(temp);

            while(temp.size()>1){
                temp.add(join(temp.get(0),temp.get(1)));
                temp.remove(0);
                temp.remove(0);
                countMul=countMul+temp.get(temp.size()-1).getFactorTab().size();
                sortFactorSizeAlphabetical(temp);
            }
            if(temp.size()>0) {
                temp.get(0).eliminate(hid.getName());
                countPlus=countPlus+temp.get(0).getFactorTab().size()*(hid.getOutcomes().size()-1);
                arrFactor.add(temp.get(0));
            }
        }

        //Joins the query factors by size until one factor remains
        while(arrFactor.size()>1){
            arrFactor.add(join(arrFactor.get(0),arrFactor.get(1)));
            arrFactor.remove(0);
            arrFactor.remove(0);
            countMul=countMul+arrFactor.get(arrFactor.size()-1).getFactorTab().size();
            sortFactorSizeAlphabetical(arrFactor);
        }
        countPlus=countPlus+arrFactor.get(0).getFactorTab().size()-1;

        return ""+round5(normalResult(arrFactor.get(0),query.get(1)))+","+countPlus+","+countMul;
    }


    /**
     * The function return the ASCII value of arr by his Variables
     * @param arr the factor
     * @return the ASCII value
     */
    public int getASCIIVal(ArrayList<Variable> arr){
        int asciiVal=1;
        for(Variable v: arr){
            asciiVal=asciiVal*getASCIIValString(v.getName());
        }
        return asciiVal;
    }
    /**
     * return ascii value of string
     * @param str the string
     * @return scii value of string
     */
    public int getASCIIValString(String str){
        int asciiVal=1;
        for(int i=0;i<str.length();i++){
            asciiVal=asciiVal*str.charAt(i);
        }
        return asciiVal;
    }

    /**
     * The function swap the factors int the array
     * @param arr the array
     * @param i index to swap
     * @param j index to swap
     */
    public void swapFac(ArrayList<Factor> arr,int i,int j){
        Factor temp=arr.get(i);
        arr.set(i,arr.get(j));
        arr.set(j,temp);
    }
    /**
     * The function swap the Variables int the array
     * @param arr the array
     * @param i index to swap
     * @param j index to swap
     */
    public void swapVar(ArrayList<Variable> arr,int i,int j){
        Variable temp=arr.get(i);
        arr.set(i,arr.get(j));
        arr.set(j,temp);
    }

    /**
     *Removes variables that are not part of the query and are not an ancestor of the query or evidence
     * @param query the query
     */
    public void removeIrrelevantVariables(ArrayList<String> query){
        ArrayList<String> arrQueryVariables= new ArrayList<>();
        for(int i=0;i< query.size();i=i+2)
            arrQueryVariables.add(query.get(i));
        int cLeavesNot;
        do{
             cLeavesNot=0;
            for (Variable v: arrVariables){
                if(isLeaf(v.getName()) && !isExistByNameStr(arrQueryVariables,v.getName()))
                    arrFactor.remove(inFactor(v.getName()));
            }
            for(Variable v: arrVariables){
                if(isLeaf(v.getName())&& !isExistByNameStr(arrQueryVariables,v.getName()))
                    cLeavesNot++;
            }
        }
        while(cLeavesNot>0);

    }

    /**
     * Check if variable is leaf or not
     * If he appears one time is leaf
     * @param name the variable name to check
     * @return true if leaf ,false if not
     */
    public Boolean isLeaf(String name){
        int count=0;
        for(Factor f:arrFactor){
            for(Variable v:f.getVarFactor()){
                if(name.equals(v.getName()))
                    count++;
            }
        }
        if(count==1)
            return true;
        return false;
    }

    /**
     *
     * @param name
     * @return
     */
    public int inFactor(String name){
        for(int i=0; i<arrFactor.size();i++) {
            for (Variable v : arrFactor.get(i).getVarFactor()) {
                if (name.equals(v.getName()))
                    return i;
            }
        }
        return -1;
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
     *Return if variable exist in the array
     */

    public boolean isExistByName(ArrayList<Variable> arr,String name ){
        if(arr!=null) {
            for (Variable v1 : arr) {
                if (v1.getName().equals(name))
                    return true;
            }
        }
        return false;
    }
    public boolean isExistByNameStr(ArrayList<String> arr,String name ){
        if(arr!=null) {
            for (String s : arr) {
                if (s.equals(name))
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
     * Return the variable by his name at the bayesian network
     */
    public Variable getVariableByName(String name){
        for (Variable v1:arrVariables) {
            if(v1.getName().equals(name))
                return v1;
        }
        return null;
    }
    public double round5(double d){
        d=100000*d;
        d=Math.round(d);
        d=d/100000;
        return d;
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
