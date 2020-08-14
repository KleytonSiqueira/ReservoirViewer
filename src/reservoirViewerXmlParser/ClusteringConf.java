/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewerXmlParser;

import java.io.Serializable;
import javax.xml.XMLConstants;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "clustering")
public class ClusteringConf implements Serializable{
    
    @XmlAttribute(name = "type", namespace = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI)
    private String xsiType;
    private String method;
    private String distanceMatrix;
    private String min;
    private String max;
    private String iterations;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    

    public String getXsiType() {
        return xsiType;
    }

    public void setXsiType(String xsiType) {
        this.xsiType = xsiType;
    }

    public String getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(String distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getIterations() {
        return iterations;
    }

    public void setIterations(String iterations) {
        this.iterations = iterations;
    }
    
    
    @Override
    public String toString() {
        return  "type= " + xsiType
                + ", distanceMatrix= " + distanceMatrix
                + ", min= " + min
                + ", max= " + max
                + ", iterations= " + iterations;
    }
            
}
