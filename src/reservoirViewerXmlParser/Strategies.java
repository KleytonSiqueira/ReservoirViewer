/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewerXmlParser;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author karen
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "strategies")
class Strategies implements Serializable {
    
    @XmlElement(name = "strategy")
    List<Strategy> strategies;

    public List<Strategy> getStrategy() {
        return strategies;
    }

    public void setStrategy(List<Strategy> strategy) {
        this.strategies = strategy;
    }
    
    
    
}
