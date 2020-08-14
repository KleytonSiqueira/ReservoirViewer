/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewerXmlParser;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "property")
public class Property implements Serializable{
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "function")
    private String function;
    @XmlAttribute(name = "fileDistanceMatrix")
    private String fileDistanceMatrix;
    @XmlAttribute(name = "file2d")
    private String file2d;
    @XmlAttribute(name = "sortingAlgorithm")
    private String sortingAlgorithm;
    @XmlAttribute(name = "fileFeatureVector")
    private String fileFeatureVector;

    public String getFileFeatureVector() {
        return fileFeatureVector;
    }

    public void setFileFeatureVector(String fileFeatureVector) {
        this.fileFeatureVector = fileFeatureVector;
    }
    

    public String getSortingAlgorithm() {
        return sortingAlgorithm;
    }

    public void setSortingAlgorithm(String sortingAlgorithm) {
        this.sortingAlgorithm = sortingAlgorithm;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFileDistanceMatrix() {
        return fileDistanceMatrix;
    }

    public void setFileDistanceMatrix(String fileDistanceMatrix) {
        this.fileDistanceMatrix = fileDistanceMatrix;
    }

    public String getFile2d() {
        return file2d;
    }

    public void setFile2d(String file2d) {
        this.file2d = file2d;
    }
    
    @Override
    public String toString() {
        return  "name= " + name
                + ", function= " + function
                + ", fileDistanceMatrix= " + fileDistanceMatrix
                + ", file2d= " + file2d;
    }
}
