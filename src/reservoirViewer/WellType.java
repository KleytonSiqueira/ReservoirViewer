/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewer;

import java.util.Objects;

/**
 *
 * @author KRodrigues
 */
public class WellType {
    
    private final String type;
    private final String typeName;

    private WellType(String type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public static final WellType PRODUCER = new WellType("PRD", "Producer");
    public static final WellType INJECTOR = new WellType("INJ", "Injector");

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WellType other = (WellType) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return typeName;
    }
    
}
