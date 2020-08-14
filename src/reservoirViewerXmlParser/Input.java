/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewerXmlParser;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "input")
public class Input implements Serializable{
 
    private String allModels;
    private String selectedModels;
    private String highlightedModels;
    private String nullBlocks;
    
    @XmlElementRef(name = "properties")
    private Properties properties = new Properties();
    
    @XmlElementRef(name = "strategies")
    private Strategies strategies = new Strategies();

    public Strategies getStrategies() {
        return strategies;
    }

    public void setStrategies(Strategies strategies) {
        this.strategies = strategies;
    }
    

    public String getSelectedModels() {
        return selectedModels;
    }

    public void setSelectedModels(String selectedModels) {
        this.selectedModels = selectedModels;
    }
    
    
    
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getAllModels() {
        return allModels;
    }

    public void setAllModels(String allModels) {
        this.allModels = allModels;
    }

    public String getHighlightedModels() {
        return highlightedModels;
    }

    public void setHighlightedModels(String highlightedModels) {
        this.highlightedModels = highlightedModels;
    }

    public String getNullBlocks() {
        return nullBlocks;
    }

    public void setNullBlocks(String nullBlocks) {
        this.nullBlocks = nullBlocks;
    }
    
    
    @Override
    public String toString() {
        return  "properties= " + properties
                + "allModels=" + allModels
                ;
    }
}
