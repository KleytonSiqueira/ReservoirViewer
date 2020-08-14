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
@XmlRootElement(name = "chart")
public class Chart implements Serializable{
//    @XmlAttribute(name = "type")
    private String type;
//    @XmlAttribute(name = "layoutCurve")
    private String layoutCurve;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLayoutCurve() {
        return layoutCurve;
    }

    public void setLayoutCurve(String layoutCurve) {
        this.layoutCurve = layoutCurve;
    }
    
    @Override
    public String toString() {
        return "Chart ["
                + "type= " + type
                + ", layoutCurve= " + layoutCurve
                + "]";
    }

}
