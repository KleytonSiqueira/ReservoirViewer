/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import reservoirViewerConfig.Configuracao;
import reservoirViewerUtils.ReservoirDrawer;
import reservoirViewerXmlParser.XmlParserRV;

/**
 *
 * @author Karen
 */
public class main_ReservoirViewer extends Application {
    
    private static String configFilePath;

    public static String getConfigFilePath() {
        return configFilePath;
    }

    public static void setConfigFilePath(String configFilePath) {
        main_ReservoirViewer.configFilePath = configFilePath;
    }
    
    
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        /**
         * Uma Classe base para a execuçâo do programa
         */
        try {
//            System.out.println(args[0]);
            configFilePath = args[0];
            launch();
        } catch (Exception e) {
            String tempDir = System.getProperty("java.io.tmpdir");
            System.out.println(tempDir);
            FileWriter logFileWriter = new FileWriter(tempDir + File.separator + "log.txt");
            PrintWriter logFilePrintWriter = new PrintWriter(logFileWriter);
            
            IllegalArgumentException exp = new IllegalArgumentException("Caminho para o XML não especificado.");
            
            exp.printStackTrace(logFilePrintWriter);
            logFileWriter.close();
            System.exit(2);

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        App app = new App(getConfigFilePath());
        
    }                                                               
}

class App {
    
    public App(String configFilePath) {
        System.out.println("Construindo App");
        
        try {
            this.createApp(configFilePath);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("App construido com sucesso");
    }

    /**
     * Um método responsável por carregar a lógica básica do programa
     */
    private void createApp(String configFilePath) throws IOException {
        
        Configuracao configuracao = new Configuracao();
        XmlParserRV xmlParserRV = new XmlParserRV();
        ReservoirDrawer reservoirDrawer = new ReservoirDrawer();
        
        try {
        if (configFilePath == null) {
            throw new IllegalStateException("Caminho para o XML não especificado.");
        } else {
            configuracao = xmlParserRV.getXmlAsConfiguracao(configFilePath);
            //arquivo de progresso
                FileWriter logFile = new FileWriter(configuracao.getRoot() + File.separator + configuracao.getProgressFile());
                PrintWriter logFileWriter = new PrintWriter(logFile);
                logFileWriter.printf("25:ProgRVParsingXML\n");
                logFile.close();

            reservoirDrawer.DrawReservoir(configuracao);
            System.exit(0);
        }  
        
        } catch (Exception e) {
            
            e.printStackTrace();
            
            //arquivo de log de erro
            System.out.println(configuracao.getRoot() + File.separator + configuracao.getLogFile());
            FileWriter logFileWriter = new FileWriter(configuracao.getRoot() + File.separator + configuracao.getLogFile());
            PrintWriter logFilePrintWriter = new PrintWriter(logFileWriter);
            e.printStackTrace(logFilePrintWriter);
            logFileWriter.close();
            System.exit(1);
        }
    }
}

