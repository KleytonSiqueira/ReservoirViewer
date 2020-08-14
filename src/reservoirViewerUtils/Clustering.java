/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewerUtils;

import br.unicamp.ft.mra.IReorderableMatrix;
import br.unicamp.ft.mra.ReorderableMatrix;
import java.io.File;
import reservoirViewer.ReservoirMap;
import reservoirViewerConfig.ClusteringData;
import reservoirViewerConfig.Configuracao;
import reservoirViewerXmlParser.Property;
import weka.clusterers.RandomizableClusterer;
import weka.clusterers.XMeans;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author Karen
 */
public class Clustering {
    
    
    /**
     * Método que excecuta o algoritmo xmeans
     * @param fileName
     * arquivo de matriz de distâncias
     * @param min
     * quantidade mínima de clusters
     * @param max
     * quantidade máxima de clusters
     * @return
     * Objeto do tipo ClusteringData
     */
    public ClusteringData clusterReservoirsByMatrixFile(String fileName, Configuracao configuracao, Boolean headerRow) {
        
        System.out.println("Clustering reservoirs by matrix file: Opening file " + fileName);

        // data vinda do load do dataset do reservatório
        Instances data;

        // Vamos abrir o arquivo ;)
        try {
            CSVLoader loader = new CSVLoader();
            loader.setNoHeaderRowPresent(true);
//            loader.setOptions(new String[] {"-H"});
            loader.setFile(new File(fileName));
            loader.setFieldSeparator(",");
//            ConverterUtils.DataSource source = new ConverterUtils.DataSource(loader);
            
            DataSource source = new DataSource(loader);
//            Instances data = source.getDataSet();

            data = source.getDataSet();
        } catch (Exception e) {

            System.err.println("An error was found when opening reservoirs."
                    + "No cluster will be returned");
            System.err.println("Error: " + e.getMessage());
            System.err.println("Error: " + e.getLocalizedMessage());
            return null;
        }
        
        return getClustering(data, configuracao);
    }
    
    public ClusteringData getClustering (Instances data, Configuracao configuracao) {
        // Usando RandomizableClusterer pela herança de KMENS/XMENS
        RandomizableClusterer clusterer;

        // obtendo o metodo de clustering
        switch (configuracao.getClusteringConfig().getXsiType().toUpperCase()) {

            case "XMEANS":
                clusterer = new XMeans();
                // MIN E MAX para a execução do XMENS
                String min = configuracao.getClusteringConfig().getMin().toString();
                String max = configuracao.getClusteringConfig().getMax().toString();
                
                String Options = "-L " + min + " -H " + max ;

                try {
                    clusterer.setOptions(weka.core.Utils.splitOptions(Options));
                    clusterer.buildClusterer(data);
                    ClusteringData clusteringData = new ClusteringData(clusterer, data);

                    return clusteringData;

                } catch (Exception e) {

                    System.err.println("An error was found when clustering " + "reservoirs. No cluster will be returned");
                    System.err.println("Error: " + e.getMessage());
                    System.err.println("Error: " + e.getLocalizedMessage());
                    e.printStackTrace();
                    return null;

                }

            default:
                throw new IllegalArgumentException("Metodo de clusterização "
                        + "não implementado");
        }
    }
    
    public String createReservoirFeatureVectorMatrix(Configuracao configuracao, ReservoirMap res, Property property) {
        String folderName = configuracao.getRoot() + File.separator + "FEATUREVECTOR";
        String fileName = folderName + File.separator + "featureVectorMatrix_" + property.getName() + "_" + property.getFunction() + ".csv";
        new File(folderName).mkdir();
        try {
            // CALCULATING FEATURE VECTOR MATRIX
            IReorderableMatrix featureVectorMatrix;
            File featureVectorFile = new File(fileName);
            if (!featureVectorFile.exists()) {
                featureVectorMatrix = res.createFeatureVectorMatrix(property.getName());
                featureVectorMatrix.printCSV(fileName.replace(".csv", "")); // bypassing a bug in MRA (duplication of ".csv" in the end of filename
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ClusteringData clusterReservoirsByFeatureVectors(String fileName, Configuracao configuracao) {
        return clusterReservoirsByMatrixFile(fileName,configuracao,true);
    }

}
