/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewer;

/**
 *
 * @author KRodrigues
 */
public class PeanoHilbertCurve extends AbstractCurve implements ICurve {
    private final int[][] map; // maps from xy to d

    public static void main(String[] args) {

        int numElements = 16;
        PeanoHilbertCurve curve = new PeanoHilbertCurve(numElements);
        curve.print();
        for (int k=0; k<curve.getNumberOfElements();k++) {
            Coordinate coord = curve.getCoordinate(k);
            System.out.println("k="+k+" --> ("+coord.x+","+coord.y+")");
        }

    }
    


    
    public void print() {
        Dimension dimension = getDimension();
        for (int i=0; i<dimension.x; i++) {
            for (int j=0; j<dimension.y; j++) {
                System.out.printf("%d \t",getD(j, i));
            }
            System.out.println();
        }
    }
    
    public PeanoHilbertCurve(int numberOfElements) {
        super(numberOfElements);
        Dimension dimension = getDimension();
        map = new int[dimension.x][dimension.y];
        for (int y=0; y<dimension.y; y++) {
            for (int x=0; x<dimension.x; x++) {
                map[x][y]=calculateD(x,y);
                if (map[x][y]>=numberOfElements) {
                    map[x][y]=-1; // não alocado.
                }
            }
        }
    }
    
    @Override
    public Dimension defineDimension(int numberOfElements) {
        int power = 1;
        while (power<numberOfElements) {
            power *= 4;
        }
        //this.dimension=(int)Math.sqrt(power);
        int d = (int)Math.sqrt(power);
        return new Dimension(d,d);
    }
    
    public int getD(int x, int y) {
        if (x>=map.length || y>=map[0].length || x<0 || y<0) {
            return -1;
        } else {
            return map[x][y];
        }
    }
    
    private int calculateD(int x, int y) {
        //convert (x,y) to d
        Dimension dimension = getDimension();
        int rx, ry, s, d=0;
        int[] xyArray;
        for (s=dimension.x/2; s>0; s/=2) {
            rx = (x & s) > 0 ? 1 : 0;
            ry = (y & s) > 0 ? 1 : 0;
            d += s * s * ((3 * rx) ^ ry);
            
            xyArray = rot(s, x, y, rx, ry);
            x=xyArray[0];
            y=xyArray[1];
        }
        return d;
    }
    
    @Override
    public Coordinate getCoordinate(int d) { // wrong?
        int n = dimension.x;
        int rx, ry, s, t=d;
        int x=0, y=0;
        int[] xyArray;
        for (s=1; s<n; s*=2) {
            rx = (1 & (t/2));//> 0 ? 1 : 0; // 
            ry = (1 & (t ^ rx));//> 0 ? 1 : 0; // 
            xyArray = rot(s, x, y, rx, ry);
            x=xyArray[0];
            y=xyArray[1];
            x += s * rx;
            y += s * ry;
            t /= 4;
        }
        return new Coordinate(x,y);
    }

    
    
    
    private int[] rot(int n, int x, int y, int rx, int ry) {
        if (ry == 0) {
            if (rx == 1) {
                x = n-1 - x;
                y = n-1 - y;
            }

            //Swap x and y
            int t  = x;
            x = y;
            y = t;
        }
        int[] answer = new int[2];
        answer[0] = x;
        answer[1] = y;
        return answer;
    }

//    public class Coordinate {
//        public int x;
//        public int y;
//        
//        public Coordinate(int x, int y) {
//            this.x=x;
//            this.y=y;
//        }
//    }
    
}
    

/*
    
    FROM WIKIPEDIA (https://en.wikipedia.org/wiki/Hilbert_curve)
    
    //convert (x,y) to d
int xy2d (int n, int x, int y) {
    int rx, ry, s, d=0;
    for (s=n/2; s>0; s/=2) {
        rx = (x & s) > 0;
        ry = (y & s) > 0;
        d += s * s * ((3 * rx) ^ ry);
        rot(s, &x, &y, rx, ry);
    }
    return d;
}

//convert d to (x,y)
void d2xy(int n, int d, int *x, int *y) {
    int rx, ry, s, t=d;
    *x = *y = 0;
    for (s=1; s<n; s*=2) {
        rx = 1 & (t/2);
        ry = 1 & (t ^ rx);
        rot(s, x, y, rx, ry);
        *x += s * rx;
        *y += s * ry;
        t /= 4;
    }
}

//rotate/flip a quadrant appropriately
void rot(int n, int *x, int *y, int rx, int ry) {
    if (ry == 0) {
        if (rx == 1) {
            *x = n-1 - *x;
            *y = n-1 - *y;
        }

        //Swap x and y
        int t  = *x;
        *x = *y;
        *y = t;
    }
    
} */
