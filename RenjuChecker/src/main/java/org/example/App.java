package org.example;

import org.example.service.RenjuWinChecker;
import org.example.util.RenjuInputReader;
import org.example.util.RenjuOutputWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        RenjuInputReader reader = new RenjuInputReader();
        RenjuWinChecker renjuWinChecker = new RenjuWinChecker();
        RenjuOutputWriter writer = new RenjuOutputWriter();

        writer.printRenjuTestResultsToFile(
                renjuWinChecker.checkRenjuTests(
                        reader.readRenjuTestCases()
                )
        );
    }
}
