package org.example.util;

import org.example.entity.TestResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RenjuOutputWriter {
    private static final String RENJU_TEST_RESULT_FILE = "C:\\Users\\Dmytro\\Desktop\\MPIZLocal\\MIPZ\\RenjuChecker\\src\\main\\output.txt";
    public static void printRenjuTestResultsToFile(List<TestResult> testResults) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RENJU_TEST_RESULT_FILE))) {
            for(TestResult t : testResults) {
                writer.println(t.getWinner());
                if(t.getWinner() != 0) {
                    writer.println(t.getHorizontalPos() + t.getVerticalPos());
                }
            }
            System.out.println("Numbers printed to " + RENJU_TEST_RESULT_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
