package com.urbancomputing.checkin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Main
 *
 * @author yuzisheng
 * @date 2021/11/22
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<CheckIn> checkIns = getTestData();
        EBMModel ebmModel = new EBMModel(3, 10, checkIns);
        double[][] socialStrengthVector = ebmModel.compute();
        System.out.println("ok");
    }

    private static ArrayList<CheckIn> getTestData() throws IOException {
        String filePath = "./src/main/resources/checkin.txt";
        FileInputStream in = new FileInputStream(filePath);
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(reader);
        ArrayList<CheckIn> data = new ArrayList<>();
        bufferedReader.readLine();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] items = line.split(",");
            data.add(new CheckIn(Integer.parseInt(items[0]) - 1, Integer.parseInt(items[1]) - 1, Long.parseLong(items[2])));
        }
        return data;
    }
}
