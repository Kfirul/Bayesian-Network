import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Variable implements Comparable <Variable> {
   private String name;
   private ArrayList<String> outcomes=new ArrayList<String>();
   private ArrayList<Variable> fathers=new ArrayList<Variable>();
   private ArrayList<String> listOfFathers=new ArrayList<String>();
   private ArrayList<String> tables=new ArrayList<String>();
   private CPT cpt;
   private BayesianNet bn;


   public Variable(String name, ArrayList<String> outcomes, ArrayList<String> fathers, ArrayList<String> tables, BayesianNet bn){
      this.name=name;
      this.bn=bn;
      this.outcomes = new ArrayList<String>(outcomes);
      this.listOfFathers = new ArrayList<String>(fathers);
      this.tables=new ArrayList<String>(tables);
   }

   /**
    * Create cpt
    * 
    */
    public void createCPT() {
       cpt=new CPT(name,outcomes,fathers,tables);
    }
//Getters

   public String getName() {
      return name;
   }


   public ArrayList<String> getOutcomes() {
      return outcomes;
   }


   public ArrayList<Variable> getFathers() {
      return fathers;
   }


   public CPT getCpt() {
      return cpt;
   }


   public BayesianNet getBn() {
      return bn;
   }

   public ArrayList<String> getTables() {
      return tables;
   }


   public ArrayList<String> getListOfFathers() {
      return listOfFathers;
   }
   /**
    * Sort the variables by names
    * @param v the object to be compared.
    * @return
    *
    */
   public int compareTo(Variable v){
      return (this.name.compareTo(v.name));
   }

   /**
    * Print the variable
    * @return the variable
    *
    */
   @Override
   public String toString() {
      ArrayList<String> listOfFathersCheck=new ArrayList<>();
      for(int i=0;i<fathers.size();i++){
         listOfFathersCheck.add(fathers.get(i).getName());
      }
      ArrayList<String> a=new ArrayList<String>();
      for(int i=0;i<bn.getArrVariables().size();i++){
         a.add(bn.getArrVariables().get(i).getName());
      }
      return "Variable: " + "name=" + name  + "\n, outcomes=" + outcomes + "\n,fathers=" + listOfFathersCheck+"\n"+ "bn=" + a+"\n\n";
   }
}
