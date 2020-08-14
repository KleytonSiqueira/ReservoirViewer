package reservoirViewerXmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import reservoirViewerConfig.ClusteringConfig;
import reservoirViewerConfig.Configuracao;
import reservoirViewerXmlParser.Property;

public class XmlParserRV {

//    public static void main(String[] args) throws Exception {
//        //File xmlFile = new File("C:\\Users\\karen\\Documents\\NetBeansProjects\\ReservoirViewerBranchKaren\\ReservoirViewerKaren\\ReservoirViewerKaren\\data\\config_files\\SM_PH_config.xml");
//
//        JAXBContext jaxbContext;
//        try {
//            jaxbContext = JAXBContext.newInstance(SepiaRv.class);
//
//            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//
//            SepiaRv sepiaRVXml = (SepiaRv) jaxbUnmarshaller.unmarshal(xmlFile);
//
//            System.out.println(sepiaRVXml);                 
//            
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }
    
    private SepiaRv getXmlAsSepiaRV(String path) throws FileNotFoundException{
        System.out.println("Abrindo o arquivo XML");
        File xmlFile = new File(path);
        System.out.println("XML aberto com sucesso");
        System.out.println("Iniciando o parsing do XML");
        
        JAXBContext jaxbContext;
        try {
//            jaxbContext = JAXBContext.newInstance(SepiaRv.class);
//
//            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller;
                    
            InputStream is = new FileInputStream(path);
            JAXBContext jc = JAXBContext.newInstance(SepiaRv.class);
            Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
            StringBuffer xmlStr = new StringBuffer(is.toString().replace("xsi:", ""));
//            Object o = u.unmarshal( is );
            
            System.setProperty("org.xml.sax.driver", "com.sun.org.apache.xerces.internal.parsers.SAXParser");
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
            System.setProperty("javax.xml.parsers.SAXParserFactory","com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");

            SepiaRv sepiaRVXmlI = (SepiaRv) jaxbUnmarshaller.unmarshal(is);
            
            System.out.println("XML parseado para SepiaRV com sucesso!");
            
            System.out.println(sepiaRVXmlI);
                       
            return sepiaRVXmlI;
            
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Configuracao getXmlAsConfiguracao(String xmlPath) throws FileNotFoundException{
        System.out.println("Iniciando o Parsing do XML para Configuração");
        // Configuracao usada pelo RV
        Configuracao configuracao = new Configuracao();
        //ClusteringConfig usada pela configuracao
        ClusteringConf clusteringConfig = new ClusteringConf();
        // Xml parseado
        SepiaRv sepiaRv = getXmlAsSepiaRV(xmlPath);
        //Array de propriedades
        List<Property> propriedades = new ArrayList<Property>();
        
        List<Strategy> strategies = new ArrayList<Strategy>();
        
        System.out.println("Parseando propriedade root");
        configuracao.setRoot(sepiaRv.getParameters().getRoot());
        if(configuracao.getRoot() != null){
            System.out.println("propriedade root Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade folder2d");
        configuracao.setFolder2d(sepiaRv.getParameters().getFolder2d());
        if(configuracao.getFolder2d()!= null){
            System.out.println("propriedade folder2d Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade folderDistanceMatrix");
        configuracao.setFolderDistanceMatrix(sepiaRv.getParameters().getFolderDistanceMatrix());
        if(configuracao.getFolderDistanceMatrix() != null){
            System.out.println("propriedade folderDistanceMatrix Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade Chart - Type");
        configuracao.setChartType(sepiaRv.getParameters().getChart().getType());
        if(configuracao.getChartType() != null){
            System.out.println("propriedade Chart - Type Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade Chart - layoutCurve");
        configuracao.setLayoutCurve(sepiaRv.getParameters().getChart().getLayoutCurve());
        if(configuracao.getLayoutCurve() != null){
            System.out.println("propriedade layoutCurve Parseado com Sucesso");
        }
        
        //ARRUMAR A PARTIR DAQUI
        
        System.out.println("Parseando propriedade clustering - method");
        clusteringConfig.setMethod(sepiaRv.getParameters().getClustering().getMethod());
        if(clusteringConfig.getMethod()!= null){
            System.out.println("propriedade clustering - method Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade clustering - type");
        clusteringConfig.setXsiType(sepiaRv.getParameters().getClustering().getXsiType());
        if(clusteringConfig.getXsiType()!= null){
            System.out.println("propriedade clustering - type Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade clustering - distance matrix");
        clusteringConfig.setDistanceMatrix(sepiaRv.getParameters().getClustering().getDistanceMatrix());
        if(clusteringConfig.getDistanceMatrix()!= null){
            System.out.println("propriedade clustering - distance matrix Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade clustering - min");
        clusteringConfig.setMin(sepiaRv.getParameters().getClustering().getMin());
        if(clusteringConfig.getMin()!= null){
            System.out.println("propriedade clustering - min Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade clustering - max");
        clusteringConfig.setMax(sepiaRv.getParameters().getClustering().getMax());
        if(clusteringConfig.getMax()!= null){
            System.out.println("propriedade clustering - max Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade clustering - iterations");
        clusteringConfig.setIterations(sepiaRv.getParameters().getClustering().getIterations());
        if(clusteringConfig.getIterations()!= null){
            System.out.println("propriedade clustering - iterations Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade all models");
        configuracao.setAllModels(sepiaRv.getInput().getAllModels());
        if(configuracao.getAllModels()!= null){
            System.out.println("propriedade all models Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade selected models");
        configuracao.setSelectedModels(sepiaRv.getInput().getSelectedModels());
        if(configuracao.getSelectedModels()!= null){
            System.out.println("propriedade selected models Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade pgn file name");
        configuracao.setPngFileName(sepiaRv.getOutput().getPng().getFilename());
        if(configuracao.getPngFileName()!= null){
            System.out.println("propriedade png file name Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade pgn height");
        configuracao.setPngHeight(Integer.valueOf(sepiaRv.getOutput().getPng().getHeight()));
        if(configuracao.getPngHeight()!= null){
            System.out.println("propriedade png height Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade pgn widht");
        configuracao.setPngWidth(Integer.valueOf(sepiaRv.getOutput().getPng().getWidth()));
        if(configuracao.getPngWidth()!= null){
            System.out.println("propriedade png widht Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade progress file");
        configuracao.setProgressFile(sepiaRv.getOutput().getProgressFile());
        if(configuracao.getProgressFile()!= null){
            System.out.println("propriedade progress file Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade log file");
        configuracao.setLogFile(sepiaRv.getOutput().getLogFile());
        if(configuracao.getLogFile()!= null){
            System.out.println("propriedade log file Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade nullBlocks");
        configuracao.setNullBlocks(sepiaRv.getInput().getNullBlocks());
        if(configuracao.getNullBlocks()!= null){
            System.out.println("propriedade nullBlocks Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade highlightedModels");
        configuracao.setHighlightedModels(sepiaRv.getInput().getHighlightedModels());
        if(configuracao.getHighlightedModels()!= null){
            System.out.println("propriedade highlightedModels Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade folderFeatureVector");
        configuracao.setFolderFeatureVector(sepiaRv.getParameters().getFolderFeatureVector());
        if(configuracao.getFolderFeatureVector()!= null){
            System.out.println("propriedade folderFeatureVector Parseado com Sucesso");
        }
        
        System.out.println("Parseando propriedade Benchmark");
        configuracao.setBenchmark(sepiaRv.getParameters().getBenchmark());
        if(configuracao.getBenchmark()!= null){
            System.out.println("propriedade Benchmark Parseado com Sucesso");
        }
        
        propriedades = sepiaRv.getInput().getProperties().getProperties();
        
        configuracao.setProperties(propriedades);
        
        strategies = sepiaRv.getInput().getStrategies().getStrategy();
        
        configuracao.setStrategies(strategies);
        
        configuracao.setClusteringConfig(clusteringConfig);
        
        System.out.println("Finalizado o Parsing do XML para Configuração");
        return configuracao;
    }
    
}
