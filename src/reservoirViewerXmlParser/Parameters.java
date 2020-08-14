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

@XmlRootElement(name = "parameters")
@XmlAccessorType(XmlAccessType.FIELD)
public class Parameters implements Serializable {
     
    private static final long serialVersionUID = 1L;
    
    private String root;
    private String folder2d;
    private String folderDistanceMatrix;
    private String folderFeatureVector;
    @XmlElementRef(name = "chart")
    private Chart chart = new Chart();
    @XmlElementRef(name = "clustering")
    private ClusteringConf clustering = new ClusteringConf();
    private String benchmark;
    

    public String getBenchmark() {
        return benchmark;
    }

    //Setters and Getters
    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }

    public String getFolderFeatureVector() {
        return folderFeatureVector;
    }

    public void setFolderFeatureVector(String folderFeatureVector) {
        this.folderFeatureVector = folderFeatureVector;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getFolder2d() {
        return folder2d;
    }

    public void setFolder2d(String folder2d) {
        this.folder2d = folder2d;
    }

    public String getFolderDistanceMatrix() {
        return folderDistanceMatrix;
    }

    public void setFolderDistanceMatrix(String folderDistanceMatrix) {
        this.folderDistanceMatrix = folderDistanceMatrix;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public ClusteringConf getClustering() {
        return clustering;
    }

    public void setClustering(ClusteringConf clustering) {
        this.clustering = clustering;
    }
    
    
    @Override
    public String toString() {
        return "Parameters [Root=" + root
                + ", folder2d=" + folder2d
                + ", folderDistanceMatrix=" + folderDistanceMatrix
                + ", Chart[" + chart + "]"
                + ", Clustering["  + clustering + "]"
                + "]";
    }
}