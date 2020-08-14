/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author KRodrigues
 */
public class Well {
    
    private WellType type;
    private List<IJKKey> coordinates;
    private String name;
    private int 
            iMin = Integer.MAX_VALUE,
            iMax = Integer.MIN_VALUE,
            jMin = Integer.MAX_VALUE,
            jMax = Integer.MIN_VALUE;

    public Well(String name, WellType type, List<IJKKey> coordinates) {
        this(name, type);
        this.coordinates.addAll(coordinates);
    }

    public Well(String name, WellType type) {
        this.name = name;
        this.type = type;
        this.coordinates = new ArrayList();
    }

    public WellType getType() {
        return type;
    }

    public void setType(WellType type) {
        this.type = type;
    }

    public List<IJKKey> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<IJKKey> coordinates) {
        this.coordinates = coordinates;
        updateBorders();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getiMin() {
        return iMin;
    }

    public int getiMax() {
        return iMax;
    }

    public int getjMin() {
        return jMin;
    }

    public int getjMax() {
        return jMax;
    }

    public void updateBorders() {
        for (IJKKey key : coordinates) {
            if (key.i<iMin) { iMin=key.i; }
            if (key.j<jMin) { jMin=key.j; }
            if (key.i>iMax) { iMax=key.i; }
            if (key.j>jMax) { jMax=key.j; }
        }
    }
    
}
