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

        return ""+this.getVariableByName(query.get(0)).getCpt().getProbNum(this.getVariableByName(query.get(0)).getCpt().getOutcomesArr(query));
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
