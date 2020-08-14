/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewerXmlParser;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "output")
public class Output implements Serializable{

    private String progressFile;
    private String logFile;
    private Png png = new Png();

    public String getProgressFile() {
        return progressFile;
    }

    public void setProgressFile(String progressFile) {
        this.progressFile = progressFile;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public Png getPng() {
        return png;
    }

    public void setPng(Png png) {
        this.png = png;
    }
    
    @Override
    public String toString() {
        return  "progressfile= " + progressFile
                + ", logFile= " + logFile
                + ", png= " + png;
    }
    
}
