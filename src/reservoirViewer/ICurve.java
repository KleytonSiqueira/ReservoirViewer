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
public interface ICurve {

    public Coordinate getCoordinate(int d);

    public int getD(int x, int y);

    public Dimension getDimension();

    public int getNumberOfElements();

    public void print();
    
    public Dimension defineDimension(int numberOfElements);
    
}
