package com.urbancomputing.checkin;

import java.util.Arrays;
import java.util.List;

/**
 * entropy-based model to infer social strength from spatio-temporal data
 *
 * @author yuzisheng
 * @date 2021/11/22
 */
public class EBMModel {
    /**
     * user number: user id from zero to (userNum - 1)
     */
    int userNum;
    /**
     * location number: location id from zero to (locNum - 1)
     */
    int locationNum;
    /**
     * list of check-in
     */
    List<CheckIn> checkIns;
    /**
     * co-occurrence vector
     */
    int[][][] coVector;
    /**
     * number of occurrences per user per location
     */
    int[][] locUserVector;
    /**
     * shannon entropy vector for each pair users
     */
    double[][] shannonEntropyVector;
    /**
     * diversity for each pair users: quantify how many effective locations the co-occurrences between two people represent
     */
    double[][] diversityVector;
    /**
     * location entropy: a high value indicates a popular place with many visitors and is not specific to anyone
     */
    double[] locationEntropyVector;
    /**
     * weighted frequency: a high value indicates a small uncrowded place
     */
    double[][] weightedFrequencyVector;

    public EBMModel(int userNum, int locationNum, List<CheckIn> checkIns) {
        this.userNum = userNum;
        this.locationNum = locationNum;
        this.checkIns = checkIns;
    }

    public double[][] compute() {
        init();
        computeDiversity();
        computeWeightedFrequency();

        // todo: linear regression training
        double[][] socialStrengthVector = new double[userNum][userNum];
        for (int i = 0; i < userNum; i++) {
            for (int j = 0; j < userNum; j++) {
                socialStrengthVector[i][j] = diversityVector[i][j] + weightedFrequencyVector[i][j];
            }
        }
        return socialStrengthVector;
    }

    /**
     * compute co-occurrence vector and count occurrence per user per location
     */
    private void init() {
        coVector = new int[userNum][userNum][locationNum];
        locUserVector = new int[locationNum][userNum];
        for (int i = 0; i < checkIns.size(); i++) {
            // count occurrence per user per location
            locUserVector[checkIns.get(i).lid][checkIns.get(i).uid]++;
            for (int j = i + 1; j < checkIns.size(); j++) {
                // compute co-occurrence vector
                CheckIn a = checkIns.get(i);
                CheckIn b = checkIns.get(j);
                if (a.uid != b.uid && a.lid == b.lid && a.time == b.time) {
                    coVector[a.uid][b.uid][a.lid]++;
                    coVector[b.uid][a.uid][a.lid]++;
                }
            }
        }
    }

    /**
     * compute shannon entropy for each pair
     */
    private void computeShannonEntropy() {
        shannonEntropyVector = new double[userNum][userNum];
        for (int i = 0; i < userNum; i++) {
            for (int j = i + 1; j < userNum; j++) {
                int coLocNum = Arrays.stream(coVector[i][j]).sum();
                if (coLocNum == 0) continue;
                double shannonEntropy = 0.0;
                for (int k = 0; k < locationNum; k++) {
                    if (coVector[i][j][k] == 0) continue;
                    double probability = coVector[i][j][k] / (double) coLocNum;
                    shannonEntropy += (-probability * Math.log(probability));
                }
                shannonEntropyVector[i][j] = shannonEntropy;
                shannonEntropyVector[j][i] = shannonEntropy;
            }
        }
    }

    /**
     * compute diversity for each pair
     */
    private void computeDiversity() {
        computeShannonEntropy();
        diversityVector = new double[userNum][userNum];
        for (int i = 0; i < userNum; i++) {
            for (int j = i + 1; j < userNum; j++) {
                double diversity = Math.exp(shannonEntropyVector[i][j]);
                diversityVector[i][j] = diversity;
                diversityVector[j][i] = diversity;
            }
        }
    }

    /**
     * compute location entropy for each location
     */
    private void computeLocationEntropy() {
        locationEntropyVector = new double[locationNum];
        for (int i = 0; i < locationNum; i++) {
            int totalUserNumPerLoc = Arrays.stream(locUserVector[i]).sum();
            if (totalUserNumPerLoc == 0) continue;
            double locationEntropy = 0.0;
            for (int j = 0; j < userNum; j++) {
                if (locUserVector[i][j] == 0) continue;
                double probability = locUserVector[i][j] / (double) totalUserNumPerLoc;
                locationEntropy += (-probability * Math.log(probability));
            }
            locationEntropyVector[i] = locationEntropy;
        }
    }

    /**
     * compute weighted frequency for each pair
     */
    private void computeWeightedFrequency() {
        computeLocationEntropy();
        weightedFrequencyVector = new double[userNum][userNum];
        for (int i = 0; i < userNum; i++) {
            for (int j = i + 1; j < userNum; j++) {
                double weightedFrequency = 0.0;
                for (int k = 0; k < locationNum; k++) {
                    weightedFrequency += (coVector[i][j][k] * Math.exp(-locationEntropyVector[k]));
                }
                weightedFrequencyVector[i][j] = weightedFrequency;
                weightedFrequencyVector[j][i] = weightedFrequency;
            }
        }
    }

    private double log2(double x) {
        return Math.log(x) / Math.log(2);
    }
}
