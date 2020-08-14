/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KRodrigues
 */
public class WellList extends ArrayList<Well> {
    
    private String strategyName;

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public void loadFile(String fileName) {
        BufferedReader inputFile;
        String values[];

        // Define reservoir valid cells.
        try {
            inputFile = new BufferedReader(new FileReader(fileName));
            String line;
            Well well = null;
            while ((line = inputFile.readLine()) != null) {
                values = line.split(";");

                if (line.startsWith("PRD") || line.startsWith("INJ") || line.startsWith("PRODUCER")  || line.startsWith("INJECTOR")) {
                    // add previously defined well to wellList
                    if (well != null) {
                        well.updateBorders();
                        add(well);
                    }

                    // new well
                    String wellName;
                    String wellTypeString;
                    wellName = values[1]; 
                    wellTypeString = values[0];
                    WellType wellType;
                    switch (wellTypeString) {
                        case "PRD":
                            wellType = WellType.PRODUCER;
                            break;
                        case "PRODUCER":
                            wellType = WellType.PRODUCER;
                            break;
                        case "INJ":
                            wellType = WellType.INJECTOR;
                            break;
                        case "INJECTOR":
                            wellType = WellType.INJECTOR;
                            break;
                        default:
                            wellType = null;
                    }
                    well = new Well(wellName, wellType);
                } else {
                    // a coordinate of a well
                    int i = Integer.parseInt(values[0]);
                    int j = Integer.parseInt(values[1]);
                    int k = Integer.parseInt(values[2]);
                    IJKKey key = new IJKKey(null, i, j, k);
                    well.getCoordinates().add(key);
                }

            }
            if (well != null) {
                well.updateBorders();
                add(well);
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }

    }
    
    public void print() {
        for (int i=0; i<this.size(); i++) {
            Well well = this.get(i);
            System.out.println("Well name:"+well.getName());
            System.out.println("Well type:"+well.getType());
            System.out.println("Well coordinates:");
            List<IJKKey> coord;
            coord = well.getCoordinates();
            
            for (int j=0; j<coord.size(); j++) {
                IJKKey key = coord.get(j);
                System.out.printf("i: %d\tj: %d\tk:%d\n", key.i, key.j, key.k);
            }
            System.out.println();
        }
        
    }
}
