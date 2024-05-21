package org.example.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RenjuInputReader {
    private static final String RENJU_FILE_TEST = "C:\\Users\\Dmytro\\Desktop\\MPIZLocal\\MIPZ\\RenjuChecker\\src\\main\\in.txt";
    private static Integer renguBoardWidth = 19;
    private static Integer renguBoardHeight = 19;
    public List<Integer[][]> readRenjuTestCases() throws FileNotFoundException {

        BufferedReader reader = new BufferedReader(new FileReader(RENJU_FILE_TEST));
        List<Integer[][]> testCases = new ArrayList<>();

        try {
            // Read the number of test cases
            int numTestCases = Integer.parseInt(reader.readLine().trim());
            // Iterate through each test case
            for (int t = 0; t < numTestCases; t++) {
                Integer[][] board = new Integer[renjuBoardHeight][renjuBoardWidth];
                // Read the input data for the current test case
                for (int i = 0; i < renjuBoardHeight; i++) {
                    String[] row = reader.readLine().trim().split("\\s+");
                    for (int j = 0; j < renjuBoardWidth; j++) {
                        board[i][j] = Integer.parseInt(row[j]);
                    }
                }
                reader.readLine();
                testCases.add(board);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testCases;
    }

}
