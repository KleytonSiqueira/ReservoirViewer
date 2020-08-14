/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewerConfig;

/**
 * Classe referente a parte de clustering, que pode ou não estar presente ao gerar a visualização
 * @author Karen
 */
public class ClusteringConfig {
    
    private String method;
    private String distanceMatrix;
    private Integer min;
    private Integer max;
    private Integer numClusters;
    private Integer iterations;

    /**
     * Get the value of numClusters
     *
     * @return the value of numClusters
     */
    public Integer getNumClusters() {
        return numClusters;
    }

    /**
     * Set the value of numClusters
     *
     * @param numClusters new value of numClusters
     */
    public void setNumClusters(Integer numClusters) {
        this.numClusters = numClusters;
    }


    /**
     * Get the value of max
     *
     * @return the value of max
     */
    public Integer getMax() {
        return max;
    }

    /**
     * Set the value of max
     *
     * @param max new value of max
     */
    public void setMax(Integer max) {
        this.max = max;
    }


    /**
     * Get the value of min
     *
     * @return the value of min
     */
    public Integer getMin() {
        return min;
    }

    /**
     * Set the value of min
     *
     * @param min new value of min
     */
    public void setMin(Integer min) {
        this.min = min;
    }


    /**
     * Get the value of distanceMatrix
     *
     * @return the value of distanceMatrix
     */
    public String getDistanceMatrix() {
        return distanceMatrix;
    }

    /**
     * Set the value of distanceMatrix
     *
     * @param distanceMatrix new value of distanceMatrix
     */
    public void setDistanceMatrix(String distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }


    /**
     * Get the value of method
     *
     * @return the value of method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Set the value of method
     *
     * @param method new value of method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getIterations() {
        return iterations;
    }

    public void setIterations(Integer iterations) {
        this.iterations = iterations;
    }
    
    
}
