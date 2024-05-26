package org.example;

import nonapi.io.github.classgraph.utils.Assert;
import org.example.util.LibraryMetrics;
import org.example.util.LibraryMetricsCounter;
import org.example.util.MOODMetricsCounter;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Hello world!
 */
public class App {

    public static void main( String[] args ) throws IOException, ClassNotFoundException {
        String libraryPackage = "io.github.classgraph";
        LibraryMetricsCounter libraryMetrics = new LibraryMetricsCounter();
//        libraryMetrics.printDepthOfInheritanceTreeRoots(libraryPackage);
//        libraryMetrics.printDistanceOfInheritanceTreeLeafs(libraryPackage);

//        libraryMetrics.printNumberOfChildrens(libraryPackage);
        MOODMetricsCounter moodMetricsCounter = new MOODMetricsCounter();
        moodMetricsCounter.countMOODMetrics(libraryPackage);
    }
}
