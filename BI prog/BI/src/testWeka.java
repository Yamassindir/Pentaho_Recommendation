/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MANDOUR FADWA
 */
import entities.LigneDonnees;
import java.util.ArrayList;
import java.util.List;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class testWeka {

    private Instances ins = null;
    private Instances[][] split;
    private Instances[] trainingSplits, testingSplits;
    private SimpleKMeans kmeans;
    private List<LigneDonnees> list = new ArrayList<>();
    private LigneDonnees ligne;
    private static int minCl0;
    private static int maxCl0;
    private static int minCl1;
    private static int maxCl1;
    private static int minCl2;
    private static int maxCl2;

    public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {

        Instances[][] split = new Instances[2][numberOfFolds];

        for (int i = 0; i < numberOfFolds; i++) {
            split[0][i] = data.trainCV(numberOfFolds, i);
            split[1][i] = data.testCV(numberOfFolds, i);
        }

        return split;
    }

    public void loadArff(String arffInput) {
        DataSource ds = null;
        try {
            ds = new DataSource(arffInput);
            ins = ds.getDataSet();
            split = crossValidationSplit(ins, 2);

            trainingSplits = split[0];
            testingSplits = split[1];

        } catch (Exception e1) {
        }
    }

    public void clusterData() {
        kmeans = new SimpleKMeans();
        ClusterEvaluation eval = new ClusterEvaluation();
        try {
            kmeans.setPreserveInstancesOrder(true);
            kmeans.setNumClusters(3);
            for (int i = 0; i < trainingSplits.length; i++) {

                kmeans.buildClusterer(trainingSplits[i]);
                eval.setClusterer(kmeans);
                eval.evaluateClusterer(testingSplits[i]);
            }
            System.out.println(eval.clusterResultsToString());

            int[] assignments = kmeans.getAssignments();
            int i = 0;
            for (int clusterNum : assignments) {
                ligne = new LigneDonnees();
                String maChaine = trainingSplits[1].get(i).toString();
                String[] sousChaines = maChaine.split(",");
                ligne.setIdProduct(sousChaines[0]);
                ligne.setEvenement(sousChaines[1]);
                ligne.setQt_sup(Integer.parseInt(sousChaines[2]));
                ligne.setCluster(clusterNum);
                list.add(ligne);
                i++;
            }

        } catch (Exception e1) {
        }
    }

    public static void main(String[] args) throws Exception {
        testWeka test = new testWeka();
        test.loadArff("FinalJSONtoCSV.arff");
        test.clusterData();
    }

    public int getCluster(int qtSup) {
        this.getLimit();
        ligne = new LigneDonnees();
        ligne.setQt_sup(qtSup);
        if (ligne.getQt_sup() >= this.minCl1) {
            if (ligne.getQt_sup() >= this.minCl2) {
                if (ligne.getQt_sup() >= this.minCl0) {
                    ligne.setCluster(0);
                } else {
                    ligne.setCluster(2);
                }
            } else {
                ligne.setCluster(1);
            }
        }
        return ligne.getCluster();
    }

    public void getLimit() {
        minCl0 = Integer.MIN_VALUE;
        maxCl0 = Integer.MAX_VALUE;
        minCl1 = Integer.MIN_VALUE;
        maxCl1 = Integer.MAX_VALUE;
        minCl2 = Integer.MIN_VALUE;
        maxCl2 = Integer.MAX_VALUE;
        for (int j = 0; j < list.size(); j++) {
            ligne = new LigneDonnees();
            ligne = list.get(j);
            switch (ligne.getCluster()) {

                case 0:
                    if (ligne.getQt_sup() < maxCl0) {
                        maxCl0 = ligne.getQt_sup();
                    }
                    if (ligne.getQt_sup() > minCl0) {
                        minCl0 = ligne.getQt_sup();
                    }
                    break;
                case 1:
                    if (ligne.getQt_sup() < maxCl1) {
                        maxCl1 = ligne.getQt_sup();
                    }
                    if (ligne.getQt_sup() > minCl1) {
                        minCl1 = ligne.getQt_sup();
                    }
                    break;
                case 2:
                    if (ligne.getQt_sup() < maxCl2) {
                        maxCl2 = ligne.getQt_sup();
                    }
                    if (ligne.getQt_sup() > minCl2) {
                        minCl2 = ligne.getQt_sup();
                    }
                    break;
            }

        }
        
        int inter = minCl0;
        minCl0 = maxCl0;
        maxCl0 = inter;
        inter = minCl1;
        minCl1 = maxCl1;
        maxCl1 = inter;
        inter = minCl2;
        minCl2 = maxCl2;
        maxCl2 = inter;

    }

}
