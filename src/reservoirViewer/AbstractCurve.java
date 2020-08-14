/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewer;

/**
 *
 * @author Celmar
 */
public abstract class AbstractCurve implements ICurve {

    protected final Dimension dimension;
    protected final int numberOfElements;
    
    public AbstractCurve (int numberOfElements, Dimension dimension) {
        this.dimension = dimension;
        this.numberOfElements = numberOfElements;
    }

    public AbstractCurve(int numberOfElements) {
        this.numberOfElements = numberOfElements;
        this.dimension = defineDimension(numberOfElements);
    }

    
    @Override
    public int getNumberOfElements() {
        return numberOfElements;
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public void print() {
        Dimension d = getDimension();
        for (int y = 0; y < d.y; y++) {
            for (int x = 0; x < d.x; x++) {
            
                System.out.printf("%d \t", getD(x, y));
            }
            System.out.println();
        }
    }
    

}
