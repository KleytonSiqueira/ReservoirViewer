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
 
@XmlRootElement(name = "sepiaRv")
@XmlAccessorType(XmlAccessType.FIELD)
public class SepiaRv implements Serializable {

    private Parameters parameters;
    private Input input;
    private Output output;

    //Setters and Getters

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }
    
    
 
    @Override
    public String toString() {
        return "SepiaRV [" + "parameters="+ parameters
                + "input= " + input
                + " "
                + "output=" + output
                + "]";
    }
}
