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
public class IJKKey {
    
    String property;
    int i;
    int j;
    int k;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.property);
        hash = 13 * hash + this.i;
        hash = 13 * hash + this.j;
        hash = 13 * hash + this.k;
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
        final IJKKey other = (IJKKey) obj;
        if (this.i != other.i) {
            return false;
        }
        if (this.j != other.j) {
            return false;
        }
        if (this.k != other.k) {
            return false;
        }
        if (!Objects.equals(this.property, other.property)) {
            return false;
        }
        return true;
    }

    public IJKKey(String property, int i, int j, int k) {
        this.property = property;
        this.i = i;
        this.j = j;
        this.k = k;
    }
    
}
