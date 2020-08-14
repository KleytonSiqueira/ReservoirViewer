/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reservoirViewerConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.clusterers.Clusterer;
import weka.core.Instances;

/**
 *
 * @author Celmar
 */
public class ClusteringData {
    
    private Map<Integer, List<Integer>> clusterMap;
    private Map<Integer, Integer> clusterAssociation;
    
    public ClusteringData(Clusterer clusterer, Instances data) {
        // Creates a map of clusterId and associated list of dataId (reservoirs' ids)
        clusterMap = new HashMap<>();
        clusterAssociation = new HashMap<>();
        for (int dataId = 0; dataId < data.numInstances(); dataId++) {
            try {
                int clusterId = clusterer.clusterInstance(data.instance(dataId));
                List<Integer> dataIdList = clusterMap.getOrDefault(clusterId, null);
                if (dataIdList == null) {
                    dataIdList = new ArrayList<>();
                }
                dataIdList.add(dataId);
                clusterMap.put(clusterId, dataIdList);
                clusterAssociation.put(dataId, clusterId);
            } catch (Exception ex) {
                System.err.println("An error occurred when reading clustering data.");
                Logger.getLogger(ClusteringData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public Set<Integer> getClusterIdSet() {
        return clusterMap.keySet();
    }
    
    public List<Integer> getClusterIdListOrderBySize() {
        /*class Cluster implements Comparable {
            int clusterId;
            int size;
            public Cluster(int clusterId, int size) {
                this.clusterId=clusterId;
                this.size=size;
            }

            @Override
            public int compareTo(Object o) {
                Cluster c = (Cluster)o;
                return (int)Math.signum(c.size-this.size); // ordem inversa
            }
            
        }*/
        Set<Integer> clusterIdSet = clusterMap.keySet();
        List<Cluster> clusterList = new ArrayList<>();
        for (Integer i: clusterIdSet) {
            clusterList.add(new Cluster(i,getInstancesId(i).size()));
        }
        Collections.sort(clusterList);
        List<Integer> clusterIdList = new ArrayList<>();
        for (Cluster c: clusterList) {
            clusterIdList.add(c.clusterId);
        }
        return clusterIdList;
    }
    
    
    
    public List<Integer> getInstancesId(int clusterId) {
        return clusterMap.get(clusterId);
    }
    
    public int getClusterId(int dataId) {
        System.out.println("getClusterId("+dataId+")=...");
        int clusterId = clusterAssociation.get(dataId);
        System.out.println("getClusterId("+dataId+")="+clusterId);
        return clusterId;
    }
    
    public void printClusters() {
         // Shows the clusters
        for (Map.Entry<Integer, List<Integer>> entry : clusterMap.entrySet()) {
            Integer clusterId = entry.getKey();
            List<Integer> dataIdList = entry.getValue();
            System.out.print("cluster " + clusterId + ":");
            for (Integer dataId : dataIdList) {
                System.out.print(dataId + " ");
            }
            System.out.println();
        }       
    }
    
}

class Cluster implements Comparable {
    int clusterId;
    int size;
    public Cluster(int clusterId, int size) {
        this.clusterId=clusterId;
        this.size=size;
    }

    @Override
    public int compareTo(Object o) {
        Cluster c = (Cluster)o;
        return (int)Math.signum(c.size-this.size); // ordem inversa
    }
            
}
