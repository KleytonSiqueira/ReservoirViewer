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
 * @author Celmar
 */
public class PseudoPeanoHilbertCurve extends AbstractCurve implements ICurve {
    
    private final int UP=0;
    private final int RIGHT=1;
    private final int DOWN=2;
    private final int LEFT=3;
    
    private final int TOP_RIGHT=UP;
    private final int BOTTOM_RIGHT=RIGHT;
    private final int BOTTOM_LEFT=DOWN;
    private final int TOP_LEFT=LEFT;
    
    
    
    private final boolean CLOCKWISE=true;
    private final boolean COUNTER_CLOCKWISE=!CLOCKWISE;

    private int[][] map; // maps from xy to d
    private Coordinate[] mapDtoXY; // maps from d to xy

    public PseudoPeanoHilbertCurve(int numberOfElements) {
        super(numberOfElements);
        Dimension dimension = getDimension();
        map = fillMap(dimension);
        mapDtoXY = fillMapDtoXY(numberOfElements);
    }

    public PseudoPeanoHilbertCurve(int numberOfElements, Dimension dim) {
        super(numberOfElements);
        dimension.x = dim.x;
        dimension.y = dim.y;
        map = fillMap(dimension);
        mapDtoXY = fillMapDtoXY(numberOfElements);
        
    }

    private int[][] fillMap(Dimension dimension) {
        map = new int[dimension.x][dimension.y];
        recursiveFillMap(new Rectangle(0, 0, dimension.x - 1, dimension.y - 1),  false, 0, 0);
        return map;
    }

    
    //private int recursiveFillMap(int x1, int y1, int x2, int y2,  boolean senseOfRotation, int direction, int d) {
    private int recursiveFillMap(Rectangle r,  boolean senseOfRotation, int direction, int d) {
        // y1<=y2, x1<=x2, (xStart,yStart) é um dos 4 cantos do retângulo ((x1,y1),(x2,y2))
        // d é o índice da curva sendo gerada.
        
        System.out.println("[debug] recursiveFillMap "+r.x1+","+r.y1+"-->"+r.x2+","+r.y2);
        int xStart=r.x1;
        int yStart=r.y1;
        
        // Define starting point
        if ((direction==UP && senseOfRotation==COUNTER_CLOCKWISE) ||
                (direction==LEFT && senseOfRotation==CLOCKWISE)) {
            xStart=r.x1;
            yStart=r.y1;
        } else if ((direction==LEFT && senseOfRotation==COUNTER_CLOCKWISE) ||
                (direction==DOWN && senseOfRotation==CLOCKWISE)) {
            xStart=r.x1;
            yStart=r.y2;
        } else if ((direction==DOWN && senseOfRotation==COUNTER_CLOCKWISE) ||
                (direction==RIGHT && senseOfRotation==CLOCKWISE)) {
            xStart=r.x2;
            yStart=r.y2;
        } else if ((direction==RIGHT && senseOfRotation==COUNTER_CLOCKWISE) ||
                (direction==UP && senseOfRotation==CLOCKWISE)) {
            xStart=r.x2;
            yStart=r.y1;
        }

        
        
        int deltaX = Math.abs(r.x2 - r.x1) + 1;
        int deltaY = Math.abs(r.y2 - r.y1) + 1;

        if (deltaX == 1 && deltaY == 1) {
            System.out.println("deltaX=1, deltaY=1");
            map[r.x1][r.y1] = d++;

        } else if (deltaX == 1 && deltaY > 1) {
            System.out.println("deltaX=1, deltaY>1");
            int sentidoY=1;
            if (direction==DOWN ||
                    (direction==RIGHT && senseOfRotation==COUNTER_CLOCKWISE) ||
                    (direction==LEFT && senseOfRotation==CLOCKWISE)) {
                sentidoY=1;
                yStart=r.y1;
            } else if (direction==UP ||
                    (direction==RIGHT && senseOfRotation==CLOCKWISE) ||
                    (direction==LEFT && senseOfRotation==COUNTER_CLOCKWISE)) {
                sentidoY=-1; 
                yStart=r.y2;
            } 
//            int sentidoY = (yStart == r.y1) ? 1 : -1;
            int yEnd = (yStart == r.y1) ? r.y2 : r.y1;
            for (int y = yStart; y != yEnd+sentidoY; y += sentidoY) {
                map[r.x1][y] = d++;
            }

        } else if (deltaX > 1 && deltaY == 1) {
            System.out.println("deltaX>1, deltaY=1");
            int sentidoX=1;
            if (direction==RIGHT ||
                    (direction==UP && senseOfRotation==COUNTER_CLOCKWISE) ||
                    (direction==DOWN && senseOfRotation==CLOCKWISE)) {
                sentidoX=1;
                xStart=r.x1;
            } else if (direction==LEFT ||
                    (direction==UP && senseOfRotation==CLOCKWISE) ||
                    (direction==DOWN && senseOfRotation==COUNTER_CLOCKWISE)) {
                sentidoX=-1; 
                xStart=r.x2;
            } 
            //int sentidoX = (xStart == r.x1) ? 1 : -1; //TODO: REVER CONFORME SIMILAR EM Y
            int xEnd = (xStart == r.x1) ? r.x2 : r.x1;
            for (int x = xStart; x != xEnd+sentidoX; x += sentidoX) {
                map[x][r.y1] = d++;
            }

        } else if (deltaX == 2 && deltaY >= 2) {
            System.out.println("deltaX=2, deltaY>=2");
            // dois casos: U-shape ou Snake
            if ((xStart == r.x1 && yStart == r.y1 && senseOfRotation==COUNTER_CLOCKWISE)
                    || (xStart == r.x2 && yStart == r.y1 && senseOfRotation==CLOCKWISE)
                    || (xStart == r.x2 && yStart == r.y2 && senseOfRotation==COUNTER_CLOCKWISE)
                    || (xStart == r.x1 && yStart == r.y2 && senseOfRotation==CLOCKWISE)) {
                // U-shape
                // coluna 1
                int sentidoY = (yStart == r.y1) ? 1 : -1;
                int yEnd = (yStart == r.y1) ? r.y2 : r.y1;
                int x = xStart;
                for (int y = yStart; y != yEnd+sentidoY; y += sentidoY) {
                    map[x][y] = d++;
                }
                // coluna 2 - sentido inverso
                x = (xStart == r.x1) ? r.x2 : r.x1;
                sentidoY = -sentidoY;
                for (int y = yEnd; y != yStart+sentidoY; y += sentidoY) {
                    map[x][y] = d++;
                }

            } else {
                // Snake.
                int sentidoY = (yStart == r.y1) ? 1 : -1;
                int sentidoX = (xStart == r.x1) ? 1 : -1;
                int yEnd = (yStart == r.y1) ? r.y2 : r.y1;
                //int evenYEnd = ((int)(yEnd/2))*2;
//                boolean yIsEven = deltaY%2==0;
                //int xEnd = (xStart == x1) ? x2 : x1;
                for (int y = yStart; y != yEnd+sentidoY; y += sentidoY) {
                    if (y==yEnd) { // na última linha, no caso impar, precisa repetir o sentido.
                        sentidoX=-sentidoX;
                    }
                    if (sentidoX==1) {
                        map[r.x1][y]=d++;
                        map[r.x2][y]=d++;
                    } else {
                        map[r.x2][y]=d++;
                        map[r.x1][y]=d++;
                    }
                }

            }

        } else if (deltaX >= 2 && deltaY == 2) {
            System.out.println("deltaX>=2, deltaY=2");
            // similar ao anterior, mas com os eixos invertidos.
            // dois casos: U-shape ou Snake
//            if ((yStart == r.y1 && xStart == r.x1 && !senseOfRotation)
//                    || (yStart == r.y2 && xStart == r.x1 && senseOfRotation)
//                    || (yStart == r.y2 && xStart == r.x2 && !senseOfRotation)
//                    || (yStart == r.y1 && xStart == r.x2 && senseOfRotation)) {
                if ((yStart == r.y1 && xStart == r.x1 && senseOfRotation==CLOCKWISE)
                    || (yStart == r.y2 && xStart == r.x1 && senseOfRotation==COUNTER_CLOCKWISE)
                    || (yStart == r.y2 && xStart == r.x2 && senseOfRotation==CLOCKWISE)
                    || (yStart == r.y1 && xStart == r.x2 && senseOfRotation==COUNTER_CLOCKWISE)) {
                // U-shape
                // coluna 1
                int sentidoX = (xStart == r.x1) ? 1 : -1;
                int xEnd = (xStart == r.x1) ? r.x2 : r.x1;
                int y = yStart;
                for (int x = xStart; x != xEnd+sentidoX; x += sentidoX) {
                    map[x][y] = d++;
                }
                // coluna 2 - sentido inverso
                y = (yStart == r.y1) ? r.y2 : r.y1;
                sentidoX = -sentidoX;
                for (int x = xEnd; x != xStart+sentidoX; x += sentidoX) {
                    map[x][y] = d++;
                }

            } else {
                // Snake.
                int sentidoX = (xStart == r.x1) ? 1 : -1;
                int sentidoY = (yStart == r.y1) ? 1 : -1;
                int xEnd = (xStart == r.x1) ? r.x2 : r.x1;
//                boolean xIsEven = deltaX%2==0;
                for (int x = xStart; x != xEnd+sentidoX; x += sentidoX) {
                    if (x==xEnd) { // na última coluna, no caso impar, precisa repetir o sentido.
                        sentidoY=-sentidoY;
                    }
                    if (sentidoY==1) {
                        map[x][r.y1]=d++;
                        map[x][r.y2]=d++;
                    } else {
                        map[x][r.y2]=d++;
                        map[x][r.y1]=d++;
                    }
                    sentidoY=-sentidoY;
                }

            }

        } else if (deltaX > 2 && deltaY > 2) {
            System.out.println("deltaX>2, deltaY>2");
            
            // CASO RECURSIVO
            
            
            int xMean = (r.x1+r.x2)/2;
            int yMean = (r.y1+r.y2)/2;
            int currentDirection = direction;
            
            Rectangle topLeft = new Rectangle(r.x1,r.y1,xMean,yMean);
            Rectangle bottomLeft = new Rectangle(r.x1,yMean+1,xMean,r.y2);
            Rectangle bottomRight = new Rectangle(xMean+1,yMean+1,r.x2,r.y2);
            Rectangle topRight = new Rectangle(xMean+1,r.y1,r.x2,yMean);
            
            List<Rectangle> rectangles = new ArrayList<>();
            rectangles.add(topRight);
            rectangles.add(bottomRight);
            rectangles.add(bottomLeft);
            rectangles.add(topLeft);
            

            int currentRectangleIndex;
            Rectangle currentRectangle;
            int rotationStep=1;
            if (!senseOfRotation) {
                currentDirection=rotateDirection(currentDirection, -1);
                rotationStep=-1;
            } else {
                rotationStep=1;
            }
            
            currentRectangleIndex=currentDirection%4;
            currentRectangle=rectangles.get(currentRectangleIndex);
            d=recursiveFillMap(currentRectangle,!senseOfRotation,rotateDirection(direction, senseOfRotation, 1),d); //45 graus 
            
            currentRectangleIndex=rotateDirection(currentDirection, rotationStep);
            currentRectangle=rectangles.get(currentRectangleIndex);
            d=recursiveFillMap(currentRectangle,senseOfRotation,direction,d); // 135 graus
            
            currentRectangleIndex=rotateDirection(currentDirection, rotationStep*2);
            currentRectangle=rectangles.get(currentRectangleIndex);
            d=recursiveFillMap(currentRectangle,senseOfRotation,direction,d); // 225 graus
            
            currentRectangleIndex=rotateDirection(currentDirection, rotationStep*3);;
            currentRectangle=rectangles.get(currentRectangleIndex);
            d=recursiveFillMap(currentRectangle,!senseOfRotation,rotateDirection(direction, !senseOfRotation, 1),d); // 315 graus
            
            
            
            
        } else {
            // caso não tratado... bug?
            System.err.println("There was a problem when creating pseudo Peano-Hilbert curve.");
        }
        return d;

    }

    private int rotateDirection(int direction, int clockwiseStep) {
        // -3<=clockwiseStep<=3
        return (direction+clockwiseStep+4)%4;
    }
    
    private int rotateDirection(int direction, boolean clockwise, int step) {
        if (clockwise) {
            return (direction + step) % 4;
        } else {
            return (direction - step + 4) % 4; 
        }
    }

    private Coordinate[] fillMapDtoXY(int numberOfElements) {
        mapDtoXY = new Coordinate[numberOfElements];
        int d;
        for (int y=0; y<dimension.y; y++) {
            for (int x=0; x<dimension.x; x++) {
                d= getD(x,y);
                if (d>=0 && d<numberOfElements) {
                    mapDtoXY[d]=new Coordinate(x,y);
                }
            }
        }

        return mapDtoXY;
    }

    
    @Override
    public Coordinate getCoordinate(int d) {
        Dimension dimension = getDimension();
        return mapDtoXY[d];
    }

    @Override
    public int getD(int x, int y) {
        if (x >= map.length || y >= map[0].length || x < 0 || y < 0) {
            return -1;
        } else {
            return map[x][y];
        }
    }

//    private int calculateD(int x, int y) {
//        int d;
//        //...
//        return d;
//    }

    @Override
    public Dimension defineDimension(int numberOfElements) {
        int d = (int) Math.ceil(Math.sqrt(numberOfElements));
        return new Dimension(d, d);
    }

    
    class Rectangle {
        public int x1,y1,x2,y2;
        public Rectangle(int x1, int y1, int x2, int y2) {
            this.x1=x1;
            this.y1=y1;
            this.x2=x2;
            this.y2=y2;
        }
    }
    
    
    public static void main(String[] args) {
        //int numElements = 42;
        //Dimension dim = new Dimension(7,6);
//        int numElements = 100;
//        Dimension dim = new Dimension(10,10);
        int numElements = 36;
        Dimension dim = new Dimension(6,6);
        
        
        ICurve curve = new PseudoPeanoHilbertCurve(numElements,dim);
        curve.print();
        for (int k = 0; k < curve.getNumberOfElements(); k++) {
            Coordinate coord = curve.getCoordinate(k);
            if (coord==null) {
                System.out.println("k=" + k + " --> (null)");
            } else {
                System.out.println("k=" + k + " --> (" + coord.x + "," + coord.y + ")");
            }
        }

    }
}
