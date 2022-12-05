import java.util.ArrayList;

public class BayesianNet {

    private ArrayList<Variable> arrVariables= new ArrayList<Variable>();

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

        }


    public String simpleDeduction(ArrayList<String> query){
        //If the Information and the result of the query is already given in one of the - CPT
       if(query.size()/2==getVariableByName(query.get(0)).getListOfFathers().size()+1){
            boolean same = false, querySame = true;
            for (int j = 0; j < getVariableByName(query.get(0)).getListOfFathers().size(); j++) {

                for (int i = 2; i < query.size(); i = i + 2) {

                    if (query.get(i).equals(getVariableByName(query.get(0)).getListOfFathers().get(j)))
                        same = true;
                }
                if (!same)
                    querySame = false;
            }
            if (querySame)
                return "" + getVariableByName(query.get(0)).getCpt().getProbNum(getVariableByName(query.get(0)).getCpt().getOutcomesArr(query)) + ",0,0";
        }
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

        //Calculate the result of the original query
        ArrayList<ArrayList<String>> combineOriginal=combination(hidden,query);
        ArrayList<Double>results=new ArrayList<Double>();
        results.add(resultsCombine(combineOriginal));
        Variable theQuery=getVariableByName(query.get(0));

        //Calculate the result of each combine by the outcomes of the Variable query
        for(int i=0;i<theQuery.getOutcomes().size();i++) {
            if (!theQuery.getOutcomes().get(i).equals(query.get(1))) {
                query.set(1, theQuery.getOutcomes().get(i));
                ArrayList<ArrayList<String>> combine = combination(hidden, query);
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
    //Return the result of combination
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
