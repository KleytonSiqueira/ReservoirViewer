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
public class SnakeCurve extends AbstractCurve implements ICurve {
    
    private int[][] map; // maps from xy to d
    private Coordinate[] mapDtoXY; // maps from d to xy
    
    public SnakeCurve (int numberOfElements, Dimension dimension) {
        super(numberOfElements, dimension);
    }

    public SnakeCurve(int numberOfElements) {
        super(numberOfElements);
        Dimension dimension = getDimension();
//        map = fillMap(dimension);
//        mapDtoXY = fillMapDtoXY(numberOfElements);
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
        ICurve curve = new SnakeCurve(numElements);
        curve.print();
        for (int k=0; k<curve.getNumberOfElements();k++) {
            Coordinate coord = curve.getCoordinate(k);
            System.out.println("k="+k+" --> ("+coord.x+","+coord.y+")");
        }

    }
}
