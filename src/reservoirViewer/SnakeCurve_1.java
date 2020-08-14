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
public class SnakeCurve_1 extends AbstractCurve implements ICurve {

    public SnakeCurve_1(int numberOfElements) {
        super(numberOfElements);
    }

    @Override
    public Coordinate getCoordinate(int d) {
        Dimension dimension = getDimension();
        int x, y;
        y = d/dimension.x;
        x = d - y*dimension.x;
        if (y%2==1) {
            x = (dimension.x-1)-x; // reverses direction
        }
        return new Coordinate(x,y);
    }

    @Override
    public int getD(int x, int y) {
        int d;
        d = y*dimension.x;
        if (y%2==0) {
            d+=x;
        } else {
            d+=(dimension.x-1)-x;
        }
        if (d>=numberOfElements) {
            return -1;
        } else {
            return d;
        }
    }

    @Override
    public Dimension defineDimension(int numberOfElements) {
        int d = (int)Math.ceil(Math.sqrt(numberOfElements));
        return new Dimension(d,d);
    }
    
    
    public static void main(String[] args) {

        int numElements = 57;
        ICurve curve = new SnakeCurve_1(numElements);
        curve.print();
        for (int k=0; k<curve.getNumberOfElements();k++) {
            Coordinate coord = curve.getCoordinate(k);
            System.out.println("k="+k+" --> ("+coord.x+","+coord.y+")");
        }

    }
}
