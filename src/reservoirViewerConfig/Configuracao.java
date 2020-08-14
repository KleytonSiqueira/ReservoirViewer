/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewerConfig;

import java.util.ArrayList;
import java.util.List;
import reservoirViewerXmlParser.ClusteringConf;
import reservoirViewerXmlParser.Property;
import reservoirViewerXmlParser.Strategy;

/**
 *
 * @author Karen
 */
public class Configuracao {
    
    protected String root;
    protected String folder2d;
    protected String folderDistanceMatrix;
    protected String folderFeatureVector;
    protected String curveType;
    protected String chartType;
    protected String layoutCurve;
    protected ClusteringConf clusteringConfig;
    protected List<Property> properties = new ArrayList<Property>();
    protected List<Strategy> strategies = new ArrayList<Strategy>();
    protected String allModels;
    protected String selectedModels;
    private String highlightedModels;
    private String nullBlocks;
    protected String pngFileName;
    protected Integer pngHeight;
    protected Integer pngWidth;
    protected String progressFile;
    protected String logFile;
    protected String benchmark;

    public List<Strategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<Strategy> strategies) {
        this.strategies = strategies;
    }
    

    public String getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }
    
    

    public String getFolderFeatureVector() {
        return folderFeatureVector;
    }

    public void setFolderFeatureVector(String folderFeatureVector) {
        this.folderFeatureVector = folderFeatureVector;
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
    
    /**
     * Get the value of clusteringConfig
     *
     * @return the value of clusteringConfig
     */
    public ClusteringConf getClusteringConfig() {
        return clusteringConfig;
    }

    /**
     * Set the value of clusteringConfig
     *
     * @param clusteringConfig new value of clusteringConfig
     */
    public void setClusteringConfig(ClusteringConf clusteringConfig) {
        this.clusteringConfig = clusteringConfig;
    }
    
    /**
     * Get the value of logFile
     *
     * @return the value of logFile
     */
    public String getLogFile() {
        return logFile;
    }

    /**
     * Set the value of logFile
     *
     * @param logFile new value of logFile
     */
    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }


    /**
     * Get the value of progressFile
     *
     * @return the value of progressFile
     */
    public String getProgressFile() {
        return progressFile;
    }

    /**
     * Set the value of progressFile
     *
     * @param progressFile new value of progressFile
     */
    public void setProgressFile(String progressFile) {
        this.progressFile = progressFile;
    }


    /**
     * Get the value of pngWidth
     *
     * @return the value of pngWidth
     */
    public Integer getPngWidth() {
        return pngWidth;
    }

    /**
     * Set the value of pngWidth
     *
     * @param pngWidth new value of pngWidth
     */
    public void setPngWidth(Integer pngWidth) {
        this.pngWidth = pngWidth;
    }


    /**
     * Get the value of pngHeight
     *
     * @return the value of pngHeight
     */
    public Integer getPngHeight() {
        return pngHeight;
    }

    /**
     * Set the value of pngHeight
     *
     * @param pngHeight new value of pngHeight
     */
    public void setPngHeight(Integer pngHeight) {
        this.pngHeight = pngHeight;
    }


    /**
     * Get the value of pngFileName
     *
     * @return the value of pngFileName
     */
    public String getPngFileName() {
        return pngFileName;
    }

    /**
     * Set the value of pngFileName
     *
     * @param pngFileName new value of pngFileName
     */
    public void setPngFileName(String pngFileName) {
        this.pngFileName = pngFileName;
    }


    /**
     * Get the value of selectedModels
     *
     * @return the value of selectedModels
     */
    public String getSelectedModels() {
        return selectedModels;
    }

    /**
     * Set the value of selectedModels
     *
     * @param selectedModels new value of selectedModels
     */
    public void setSelectedModels(String selectedModels) {
        this.selectedModels = selectedModels;
    }


    /**
     * Get the value of allModels
     *
     * @return the value of allModels
     */
    public String getAllModels() {
        return allModels;
    }

    /**
     * Set the value of allModels
     *
     * @param allModels new value of allModels
     */
    public void setAllModels(String allModels) {
        this.allModels = allModels;
    }

    
    /**
     * 
     * @return 
     */
    public List<Property> getProperties() {
        return properties;
    }
    
    /**
     * 
     * @param properties 
     */
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    /**
     * Get the value of layoutCurve
     *
     * @return the value of layoutCurve
     */
    public String getLayoutCurve() {
        return layoutCurve;
    }

    /**
     * Set the value of layoutCurve
     *
     * @param layoutCurve new value of layoutCurve
     */
    public void setLayoutCurve(String layoutCurve) {
        this.layoutCurve = layoutCurve;
    }


    /**
     * Get the value of curveType
     *
     * @return the value of curveType
     */
    public String getCurveType() {
        return curveType;
    }

    /**
     * Set the value of curveType
     *
     * @param curveType new value of curveType
     */
    public void setCurveType(String curveType) {
        this.curveType = curveType;
    }


    /**
     * Get the value of folderDistanceMatrix
     *
     * @return the value of folderDistanceMatrix
     */
    public String getFolderDistanceMatrix() {
        return folderDistanceMatrix;
    }

    /**
     * Set the value of folderDistanceMatrix
     *
     * @param folderDistanceMatrix new value of folderDistanceMatrix
     */
    public void setFolderDistanceMatrix(String folderDistanceMatrix) {
        this.folderDistanceMatrix = folderDistanceMatrix;
    }


    /**
     * Get the value of folder2d
     *
     * @return the value of folder2d
     */
    public String getFolder2d() {
        return folder2d;
    }

    /**
     * Set the value of folder2d
     *
     * @param folder2d new value of folder2d
     */
    public void setFolder2d(String folder2d) {
        this.folder2d = folder2d;
    }


    /**
     * Get the value of root
     *
     * @return the value of root
     */
    public String getRoot() {
        return root;
    }

    /**
     * Set the value of root
     *
     * @param root new value of root
     */
    public void setRoot(String root) {
        this.root = root;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    
}
