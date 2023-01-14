import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class reads the XML files
 * I used the code that appears on this site to read a xml file
 * https://initialcommit.com/blog/how-to-read-xml-file-in-java
 *
 */
public class ReadXmlFile {
    private Document doc;
    private BayesianNet bayesianNet;

    /**
     *
     * Read xmlFile
     */
    public ReadXmlFile(String xmlFileName) throws ParserConfigurationException, IOException, SAXException {
        try {
            URL fileURL = Ex1.class.getResource(xmlFileName);
            File URI = new File(fileURL.toURI());
            File xmlFile = new File(xmlFileName);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            doc = builder.parse(URI);
            doc = builder.parse(xmlFile);
            doc.getDocumentElement();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Return ArrayList of the Variable names
     * @return ArrayList of the Variable names
     *
     */
    public ArrayList<String> getVariableName() {
        ArrayList<String> names = new ArrayList<String>();
        try {
            NodeList variableNodes = doc.getElementsByTagName("VARIABLE");
            for (int i = 0; i < variableNodes.getLength(); i++) {
                Node variableNode = variableNodes.item(i);
                if (variableNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element variableElement = (Element) variableNode;
                    String name = variableElement.getElementsByTagName("NAME").item(0).getTextContent();
                    names.add(name);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
            return names;
    }

    /**
     * Return the outcomes for variable
     * @param name's variable
     * @return the outcomes for variable
     *
     */
    public ArrayList<String> getOutcomes(String name) {
        ArrayList<String> outcomes = new ArrayList<String>();
        try {
            NodeList variableNodes = doc.getElementsByTagName("VARIABLE");
            for (int i = 0; i < variableNodes.getLength(); i++) {
                Node variableNode = variableNodes.item(i);
                if (variableNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element variableElement = (Element) variableNode;
                    String nameOfVariable = variableElement.getElementsByTagName("NAME").item(0).getTextContent();
                    if (name.equals(nameOfVariable)) {
                        NodeList outcomesList = variableElement.getElementsByTagName("OUTCOME");
                        for (int j = 0; j < outcomesList.getLength(); j++)
                            outcomes.add(outcomesList.item(j).getTextContent());
                    }
                }
            }
        }
        catch (Exception e){
                e.printStackTrace();
            }

        return outcomes;
    }

    /**
     * Return the fathers for variable
     * @param name's variable
     * @return the fathers for variable
     *
     */
    public ArrayList<String> getFathers(String name) {
        ArrayList<String> fathers = new ArrayList<String>();
        try {
            NodeList variableNodes = doc.getElementsByTagName("DEFINITION");
            for (int i = 0; i < variableNodes.getLength(); i++) {
                Node variableNode = variableNodes.item(i);
                if (variableNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element variableElement = (Element) variableNode;
                    String nameOfVariable = variableElement.getElementsByTagName("FOR").item(0).getTextContent();
                    if (name.equals(nameOfVariable)) {
                        NodeList fathersList = variableElement.getElementsByTagName("GIVEN");
                        for (int j = 0; j < fathersList.getLength(); j++)
                            fathers.add(fathersList.item(j).getTextContent());
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return fathers;
    }
    /**
     * Return the tables for variable
     * @param name's variable
     * @return the tables for variable
     *
     */
    public ArrayList<String> getTables(String name) {
        ArrayList<String> tables = new ArrayList<String>();
        try {
            NodeList variableNodes = doc.getElementsByTagName("DEFINITION");
            for (int i = 0; i < variableNodes.getLength(); i++) {
                Node variableNode = variableNodes.item(i);
                if (variableNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element variableElement = (Element) variableNode;
                    String nameOfVariable = variableElement.getElementsByTagName("FOR").item(0).getTextContent();
                    if (name.equals(nameOfVariable)) {

                        String table = variableElement.getElementsByTagName("TABLE").item(0).getTextContent();
                        String[] tablesSplit = table.split(" ");
                        for (String a : tablesSplit) {
                            tables.add(a);
                        }
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return tables;
    }
}

