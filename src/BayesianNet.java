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

        int mul=1;
        for(Variable v:hidden)
            mul=mul*v.getOutcomes().size();

        ArrayList<ArrayList<String>> contain=new ArrayList<ArrayList<String>>();

        for(int i=0;i<mul;i++){
            contain.add(new ArrayList<String>(query));
        }

        int loop=mul;
        for(Variable v:hidden) {
            loop = loop / v.getOutcomes().size();
            int loopPerOutcome = loop;
            int indexOutcome = 0;
            for (int i = 0; i < mul; i++) {
                contain.get(i).add(v.getName());
                contain.get(i).add(v.getOutcomes().get(indexOutcome));
                loopPerOutcome--;
                if (loopPerOutcome == 0) {
                    loopPerOutcome = loop;
                    indexOutcome++;
                    if (indexOutcome == v.getOutcomes().size())
                        indexOutcome = 0;
                }
            }
        }
        //System.out.println(contain);
            double simpleDec=0;
            for(ArrayList<String>prob:contain){
                double mulEachConatin=1.0;
                for(Variable v:arrVariables){
                    mulEachConatin= mulEachConatin*v.getCpt().getProbNum( v.getCpt().getOutcomesArr(prob));
                }
                System.out.println(mulEachConatin);
                simpleDec=simpleDec+mulEachConatin;
            }



        return ""+simpleDec;
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
