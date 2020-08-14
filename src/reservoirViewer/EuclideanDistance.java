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
public class EuclideanDistance {
    
    private static final EuclideanDistance distance = new EuclideanDistance();
    
    private EuclideanDistance() {
        
    }
    
    public static double getDistance(ReservoirMap reservoir, String property, int reservoirK1, int reservoirK2) {
        long time1 = System.currentTimeMillis();
        double d=0;
        double value1, value2;
        System.out.println("Calculating distance between reservoirs "+reservoirK1+" and "+reservoirK2+"...");
        int stepI = 1;
        int stepJ = 1;
        int maxI = reservoir.getMaxI();
        int maxJ = reservoir.getMaxJ();
        for (int i=1;i<=maxI;i+=stepI) {
            System.out.println("   i="+i);
            for (int j=1;j<=maxJ;j+=stepJ) {
                if (reservoir.isValid(i,j,reservoirK1) && 
                        reservoir.isValid(i,j,reservoirK2)) {
                    value1=reservoir.getValue(property, i,j,reservoirK1);
                    value2=reservoir.getValue(property, i,j,reservoirK2);
                    
                    d += Math.pow(value1-value2,2);
                }
            }
            
        }
        d = Math.sqrt(d);
        long time2 = System.currentTimeMillis();
        System.out.println("Time to calculate distance from "+reservoirK1+" to "+reservoirK2+":"+(time2-time1)+"ms");
        return d;
    }
    
    public static double getApproximateDistance(ReservoirMap reservoir, String property, int reservoirK1, int reservoirK2) {
        // FOR TESTING PURPOSES ONLY.
        long time1 = System.currentTimeMillis();
        double d=0;
        double value1, value2;
        int stepI = 10;
        int stepJ = 10;
        int maxI = reservoir.getMaxI();
        int maxJ = reservoir.getMaxJ();
        for (int i=1;i<=maxI;i+=stepI) {
            System.out.println("   i="+i);
            for (int j=1;j<=maxJ;j+=stepJ) {
                if (reservoir.isValid(i,j,reservoirK1) && 
                        reservoir.isValid(i,j,reservoirK2)) {
                    value1=reservoir.getValue(property, i,j,reservoirK1);
                    value2=reservoir.getValue(property, i,j,reservoirK2);
                    
                    d += Math.pow(value1-value2,2);
                }
            }
            
        }
        d = Math.sqrt(d);
        long time2 = System.currentTimeMillis();
        System.out.println("Time to calculate approx. distance from "+reservoirK1+" to "+reservoirK2+":"+(time2-time1)+"ms");
        return d;
    }
    
}
