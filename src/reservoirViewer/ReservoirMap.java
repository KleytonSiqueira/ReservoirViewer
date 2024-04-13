/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewer;

import br.unicamp.ft.mra.ReorderableMatrix;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author KRodrigues
 */
public class ReservoirMap {
    
    public static final int ARITHMETIC_MEAN = 7;
    public static final int GEOMETRIC_MEAN = 8;
    public static final int HARMONIC_MEAN = 9;
    public static final int MINIMUM = 10;
    public static final int MAXIMUM = 11;
    public static final int SUM = 12;
    public static final int STANDARD_DEVIATION = 13;
    public static final int MOD = 14;

    //private int number_of_k_values = 214; // TODO number_of_k_values should be calculated. 
    private Map<Integer, Integer> realKIndex = new HashMap<>(); // map from externalK to realK

    private final Map<IJKKey, Double> map = new HashMap<>();

    public ReservoirMap() {

    }

    public void loadValidCellsDefinition(String validCellsFile) {
        loadStaticMap("valid", validCellsFile);
    }

    public void loadValidCellsDefinitionManyModels(String validCellsFile, int indexOfValueField) {
        loadStaticMapManyModels("valid", validCellsFile, indexOfValueField);
    }

    public void loadStaticMap(String propertyName, String propertyFile) {

        BufferedReader inputFile;
        String values[];

        // Define reservoir valid cells.
        try {
            inputFile = new BufferedReader(new FileReader(propertyFile));
            String line;
            inputFile.readLine(); //skip header
            while ((line = inputFile.readLine()) != null) {
                values = line.split(",");
                int i = Integer.parseInt(values[0]);
                int j = Integer.parseInt(values[1]);
                int k = Integer.parseInt(values[2]);
                double value = Double.parseDouble(values[3]);
                this.setRealKPointer(k, k);
                this.setValue(propertyName, i, j, k, value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadStaticMapManyModels(String propertyName, String propertyFile, int indexOfValueField) {

        BufferedReader inputFile;
        String values[];

        // Define reservoir valid cells.
        try {
            inputFile = new BufferedReader(new FileReader(propertyFile));
            String line;
            inputFile.readLine(); //skip header
            while ((line = inputFile.readLine()) != null) {
                values = line.split(";");
                //System.out.println(line);
                int i = Integer.parseInt(values[0]);
                int j = Integer.parseInt(values[1]);
                int k = Integer.parseInt(values[2]);
                //double value = Double.parseDouble(values[indexOfValueField]) / number_of_k_values; 
                // Obs.: After 2018-08-24, file has mean values instead of only sum values.
                double value;
                if (values[indexOfValueField].equals("NaN")) {
                    value = 0;
                } else {
                    value = Double.parseDouble(values[indexOfValueField]);
                }

//if (k<=10) {
                this.setRealKPointer(k, k);
                this.setValue(propertyName, i, j, k, value);
//}
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public double getValue(String property, int i, int j, int k) {
        //return map.getOrDefault(new IJKKey(property, i, j, k), 0d);
        return map.getOrDefault(new IJKKey(property, i, j, getRealK(k)), 0d);
    }

    public double getValueOrSmallerMin(String property, int i, int j, int k, double min) {
        //return map.getOrDefault(new IJKKey(property, i, j, k), 0d);
        return map.getOrDefault(new IJKKey(property, i, j, getRealK(k)), min - 1.0);
    }

    public void setValue(String property, int i, int j, int k, double value) {
        map.put(new IJKKey(property, i, j, k), value);
    }

    public boolean isValid(int i, int j, int k) {
        double value = map.getOrDefault(new IJKKey("valid", i, j, getRealK(k)), 0d);
        return value > 0;
    }
    
    public boolean isNull(int i, int j, int k) {
        IJKKey key = new IJKKey ("null", i, j, getRealK(k));
        Double v = (map.get(key));
        if (v == null) {
            return true;
        } else {
            return v == 0.0;
        }
//        double value = map.getOrDefault(new IJKKey("null", i, j, getRealK(k)), 0d);
//        return value > 0;
    }

    public int getMaxI() {
        int maxI = 0;
        for (IJKKey key : this.map.keySet()) {
            if (key.i > maxI) {
                maxI = key.i;
            }
        }
        return maxI;
    }

    public int getMaxJ() {
        int maxJ = 0;
        for (IJKKey key : this.map.keySet()) {
            if (key.j > maxJ) {
                maxJ = key.j;
            }
        }
        return maxJ;
    }

    
    //TODO Trabalhar tanto com ID dos reservatÃ³rios quanto com o "K" desta classe.
    /* OBS: NÃ£o podemos perder, nas alteraÃ§Ãµes, a capacidade de ver um Ãºnico 
    reservatÃ³rio usando as tÃ©cnicas de visualizaÃ§Ã£o (ou seja, o K nÃ£o pode ser 
    simplesmente substituido    */
    public int getMaxK() {
        int maxK = 0;
        for (IJKKey key : this.map.keySet()) {
            if (key.k > maxK) {
                maxK = key.k;
            }
        }
        return maxK;
    }

    public double getMaxValue(String property) {
        double max = 0;
        for (Map.Entry entry : this.map.entrySet()) {
            IJKKey key = (IJKKey) entry.getKey();
            double value = (double) entry.getValue();
            if (key.property.equals(property) && value > max) {
                max = value;
            }
        }
        System.out.println("max(" + property + ")=" + max);
        return max;
    }

    public double getMinValue(String property) {
        double min = Double.POSITIVE_INFINITY;
        for (Map.Entry entry : this.map.entrySet()) {
            IJKKey key = (IJKKey) entry.getKey();
            double value = (double) entry.getValue();
            if (key.property.equals(property) && value < min) {
                min = value;
            }
        }
        System.out.println("min(" + property + ")=" + min);
        return min;
    }

    public double[] getPercentile(String property, double percentile1, double percentile2) {
        List<Double> c = new ArrayList();//new ArrayList(this.map.values());
        for (Map.Entry entry : this.map.entrySet()) {
            IJKKey key = (IJKKey) entry.getKey();
            if (key.property.equals(property)) {
                double value = (double) entry.getValue();
                if (!Double.isNaN(value)) {
                    c.add(value);
                }

            }
        }
        Collections.sort(c); // Demora...
        
        int size = c.size();
        
        return new double[] { c.get((int) (size * percentile1)), c.get((int) (size * percentile2)), c.get(0) };
    }

    public Interval getInterval(String property, double pStart, double pEnd) {
        // returns an interval of reservoir value, where pStart is the starting
        // percentile and pEnd the ending percentile
        // 0<=pStart<=pEnd<=1

        // It will not work properly if the reservoir is entirely empty
        List<Double> c = new ArrayList();//new ArrayList(this.map.values());
        for (Map.Entry entry : this.map.entrySet()) {
            IJKKey key = (IJKKey) entry.getKey();
            if (key.property.equals(property)) {
                double value = (double) entry.getValue();
                if (!Double.isNaN(value)) {
                    c.add(value);
                }

            }
        }
        Collections.sort(c); // Demora...
        double startValue = c.get((int) (c.size() * pStart));
        double endValue = c.get((int) (c.size() * pEnd));
        Interval result = new Interval(startValue, endValue);
        return result;
    }

    public class Interval {

        public double start, end;

        public Interval(double start, double end) {
            this.start = start;
            this.end = end;
        }
    }

    public List<Integer> getFeatureVector(int reservoirIndex, String property, double[] thresholdLevel) {

        System.out.println("Generating feature vector from Reservoir " + reservoirIndex);
        int maxI = getMaxI();
        int maxJ = getMaxJ();

        int[] iFeatureVectorLevel1 = new int[maxI]; // low values
        int[] iFeatureVectorLevel2 = new int[maxI]; // median values
        int[] iFeatureVectorLevel3 = new int[maxI]; // high values

        int[] jFeatureVectorLevel1 = new int[maxJ]; // low values
        int[] jFeatureVectorLevel2 = new int[maxJ]; // median values
        int[] jFeatureVectorLevel3 = new int[maxJ]; // high values

//        List<Integer> iFeatureVectorLevel2 = new ArrayList<>(getMaxI());
//        List<Integer> iFeatureVectorLevel3 = new ArrayList<>(getMaxI());
//        List<Integer> jFeatureVectorLevel1 = new ArrayList<>(getMaxJ());
//        List<Integer> jFeatureVectorLevel2 = new ArrayList<>(getMaxJ());
//        List<Integer> jFeatureVectorLevel3 = new ArrayList<>(getMaxJ());
        
        double thresholdLevel1to2 = thresholdLevel[0];
        double thresholdLevel2to3 = thresholdLevel[1];
        double min = thresholdLevel[2];
        
//        double thresholdLevel1to2 = 10;
//        double thresholdLevel2to3 = 100;

/*

Por default, os arrays já são preenchidos com 0;
        for (int i = 0; i < maxI; i++) {
            iFeatureVectorLevel1[i] = 0;
            iFeatureVectorLevel2[i] = 0;
            iFeatureVectorLevel3[i] = 0;
//            iFeatureVectorLevel1.set(i-1,0);
//            iFeatureVectorLevel2.set(i-1,0);
//            iFeatureVectorLevel3.set(i-1,0);
        }
        for (int j = 0; j < maxJ; j++) {
            jFeatureVectorLevel1[j] = 0;
            jFeatureVectorLevel2[j] = 0;
            jFeatureVectorLevel3[j] = 0;
//            jFeatureVectorLevel1.set(j-1,0);
//            jFeatureVectorLevel2.set(j-1,0);
//            jFeatureVectorLevel3.set(j-1,0);
        }*/

        for (int i = 0; i < maxI; i++) {
            //System.out.println("   i=" + i);
            for (int j = 0; j < maxJ; j++) {
                double value = this.getValueOrSmallerMin(property, i + 1, j + 1, reservoirIndex, min);
                if (value < min) continue;
                
                if (value < thresholdLevel1to2) {
                    iFeatureVectorLevel1[i]++;
                    jFeatureVectorLevel1[j]++;
                } else if (value < thresholdLevel2to3) {
                    iFeatureVectorLevel2[i]++;
                    jFeatureVectorLevel2[j]++;
                } else {
                    iFeatureVectorLevel3[i]++;
                    jFeatureVectorLevel3[j]++;
                }
            }
        }

        System.out.println("   Concatenating parcial feature vectors...");
        List<Integer> featureVector = new ArrayList<>();
        for (int x : iFeatureVectorLevel1) {
            featureVector.add(x);
        }
        for (int x : iFeatureVectorLevel2) {
            featureVector.add(x);
        }
        for (int x : iFeatureVectorLevel3) {
            featureVector.add(x);
        }
        for (int x : jFeatureVectorLevel1) {
            featureVector.add(x);
        }
        for (int x : jFeatureVectorLevel2) {
            featureVector.add(x);
        }
        for (int x : jFeatureVectorLevel3) {
            featureVector.add(x);
        }
        System.out.println("... generated");
        return featureVector;

    }

    public ReorderableMatrix createFeatureVectorMatrix(String property) {
        System.out.println("Generating feature vector matrix...");
        ReorderableMatrix matrix = new ReorderableMatrix(getMaxK(), 3 * getMaxI() + 3 * getMaxJ());
        
        double[] thresholdLevel = this.getPercentile(property, 0.33, 0.67);
        // Each row represents a feature vector of a reservoir 
        for (int row = 0; row < matrix.getNumberOfRows(); row++) {
            int reservoirIndex = row + 1;
            System.out.println("Generating feature vector matrix row for Reservoir " + reservoirIndex);
            List<Integer> featureVector = getFeatureVector(reservoirIndex, property, thresholdLevel);
            for (int column = 0; column < matrix.getNumberOfColumns(); column++) {
                matrix.setValue(row, column, featureVector.get(column));
            }
            matrix.setRowLabels(row, "Reservoir " + row);
        }
        System.out.println("Feature vector matrix was created");
        return matrix;
    }

//    public IReorderableMatrix getDistanceMatrix() {
//        // create matrix with feature vectors of each reservoir
//        IReorderable
//        // create a distance matrix
//        
//    }
    public int getRealK(int externalK) {
        // Returns the realK to which externalK points.
        return realKIndex.getOrDefault(externalK, 0);
    }

    public void setRealKPointer(int externalK, int realK) {
        // Defines that externalK index points to realK index.
        realKIndex.put(externalK, realK);
    }

    
    public static String getMeanTypeName(int meanType) {
        String name;
        switch (meanType) {
            case ReservoirMap.ARITHMETIC_MEAN:
                name = "arithmetic_mean";
                break;
            case ReservoirMap.GEOMETRIC_MEAN:
                name = "geometric_mean";
                break;
            case ReservoirMap.HARMONIC_MEAN:
                name = "harmonic_mean";
                break;
            case ReservoirMap.MINIMUM:
                name = "minimum";
                break;
            case ReservoirMap.MAXIMUM:
                name = "maximum";
                break;
            case ReservoirMap.SUM:
                name = "sum";
                break;
            case ReservoirMap.STANDARD_DEVIATION:
                name = "standard deviation";
                break;
            case ReservoirMap.MOD:
                name = "mod";
                break;
            default:
                name = "unknown_mean_type";
        }
        return name;
    }
    
    
    public static int getMeanTypeNameAsInt(String meanType) {
        int name;
        switch (meanType.toUpperCase()) {
            case "ARITHMETIC_MEAN":
                name = ReservoirMap.ARITHMETIC_MEAN;
                break;
            case "GEOMETRIC_MEAN":
                name = ReservoirMap.GEOMETRIC_MEAN;
                break;
            case "HARMONIC_MEAN":
                name =  ReservoirMap.ARITHMETIC_MEAN;
                break;
            case "MINIMUM":
                name = ReservoirMap.MINIMUM;
                break;
            case "MAXIMUM":
                name = ReservoirMap.MAXIMUM;
                break;
            case "SUM":
                name = ReservoirMap.SUM;
                break;
            case "STANDARD_DEVIATION":
                name = ReservoirMap.STANDARD_DEVIATION;
                break;
            case "MOD":
                name = ReservoirMap.MOD;
                break;
            default:
                throw new IllegalArgumentException("meanType Não implementado");
        }
        return name;
    }
    
}
