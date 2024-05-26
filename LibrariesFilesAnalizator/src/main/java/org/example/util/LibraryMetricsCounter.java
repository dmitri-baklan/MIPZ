package org.example.util;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import lombok.AllArgsConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class LibraryMetricsCounter implements LibraryMetrics {
    public static final String LIB_PREFIX = "C:\\Users\\Dmytro\\Desktop\\MPIZLocal\\MIPZ\\LibrariesFilesAnalizator\\src\\main\\java\\org\\example\\";
    protected static Map<String, Class<?>> classesList = new HashMap<>();

    public static void loadAllClassesFromLibrary(String sourceDirectoryPath) throws IOException {
        File classesDir = new File(sourceDirectoryPath);
        DynamicClassLoader classLoader = new DynamicClassLoader();
        for (File file : classesDir.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String classFilePath = file.getAbsolutePath();
                String className = sourceDirectoryPath.replace(LIB_PREFIX, "").replace("\\", ".") + "."
                        + file.getName().replace(".class", "");
                try {
                    Class<?> cls = classLoader.loadClassFromFile(classFilePath, className);
                    classesList.put(className, cls);
                } catch (NoClassDefFoundError ignored) {
//                    System.out.println(""); 
                }
            } else if (file.isDirectory()) {
                loadAllClassesFromLibrary(file.getAbsolutePath());
            }
        }
    }

    protected Set<Class<?>> loadAllClasses(String libraryPackage) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(libraryPackage))
                .setScanners(new SubTypesScanner(false)));

        return reflections.getSubTypesOf(Object.class);
    }

    public void printDepthOfInheritanceTreeRoots(String packagePath) {
        Set<Class<?>> allClasses = loadAllClasses(packagePath);
        Map<Class<?>, Integer> subclassCountMap = new HashMap<>();
        for (Class<?> clazz : allClasses) {
            Integer depth = calculateDIT(clazz, allClasses, 0);
            if (depth != 0) {
                subclassCountMap.put(clazz, depth);
            }
        }

        for (Map.Entry<Class<?>, Integer> entry : subclassCountMap.entrySet()) {
            System.out.println("Class: " + entry.getKey().getName() + " has depth " + entry.getValue());
        }
    }

    public void printDistanceOfInheritanceTreeLeafs(String packagePath) {
        Set<Class<?>> allClasses = loadAllClasses(packagePath);
        Map<Class<?>, Set<Class<?>>> subclassCountMap = getAllParents(allClasses);

        for (Map.Entry<Class<?>, Set<Class<?>>> entry : subclassCountMap.entrySet()) {
            System.out.println("Class: " + entry.getKey().getName() + " has " + entry.getValue().size() + " parents:");
            ;
            entry.getValue().forEach(s -> System.out.println("    " + s));
        }
    }

    public void printNumberOfChildrens(String packagePath) {
        Set<Class<?>> allClasses = loadAllClasses(packagePath);
        Map<Class<?>, Map<Integer, List<Class<?>>>> subclassCountMap = new HashMap<>();
//        allClasses = allClasses.stream().filter(s -> s.getName().equals("io.github.classgraph.TypeSignature")
//        || s.getName().equals("io.github.classgraph.ReferenceTypeSignature")
//        || s.getName().equals("io.github.classgraph.BaseTypeSignature")
//        || s.getName().equals("io.github.classgraph.ClassRefOrTypeVariableSignature")
//        || s.getName().equals("io.github.classgraph.ArrayTypeSignature")
//        || s.getName().equals("io.github.classgraph.TypeVariableSignature")
//        || s.getName().equals("io.github.classgraph.ClassRefTypeSignature")
//        ).collect(Collectors.toSet());

        for (Class<?> clazz : allClasses) {

            Map<Integer, List<Class<?>>> newTree = new HashMap<>();
            Map<Integer, List<Class<?>>> childrenTree = getAllChildrens(clazz, allClasses, 1, newTree);
            if (!childrenTree.isEmpty()) {
                subclassCountMap.put(clazz, childrenTree);
            }
        }

        for (Map.Entry<Class<?>, Map<Integer, List<Class<?>>>> entry : subclassCountMap.entrySet()) {
            System.out.println("Root Class: " + entry.getKey().getName() + " has " + countAllChildrens(entry.getValue()) + " child: ");
            System.out.println("{");
            for(Map.Entry<Integer, List<Class<?>>> valueEntry : entry.getValue().entrySet()) {
                for(Class<?> children : valueEntry.getValue()) {
                    for(int i = 0; i < valueEntry.getKey(); i++) {
                        System.out.print("++++");
                    }
                    System.out.println(children);
                }
            }
            System.out.println("}");
        }
    }

    protected int calculateDIT(Class<?> clazz, Set<Class<?>> allClasses, int depth) {
        int maxDepth = depth;
        for (Class<?> cls : allClasses) {
            Class<?> parent = cls.getSuperclass();
            if (parent != null && !cls.isEnum() && parent.equals(clazz)) {
                int subclassDepth = calculateDIT(cls, allClasses, depth+1);
                maxDepth = Math.max(maxDepth, subclassDepth);
            }
        }
        return maxDepth;
    }

    protected Set<Class<?>> getAllClassParents(Class<?> clazz) {
        Set<Class<?>> allParents = new LinkedHashSet<>();
        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null && !superclass.isEnum() && superclass != Object.class) {
            allParents.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return allParents;
    }

    protected Map<Integer, List<Class<?>>> getAllChildrens(Class<?> root, Set<Class<?>> allClasses, int depth, Map<Integer, List<Class<?>>> tree) {
        tree.computeIfAbsent(depth, k -> new ArrayList<>());
        for (Class<?> cls : allClasses) {
            Class<?> parent = cls.getSuperclass();
            if (parent != null && !cls.isEnum() && parent.getName().equals(root.getName())) {
                tree = getAllChildrens(cls, allClasses, depth + 1, tree);
                tree.get(depth).add(cls);
            }
        }
        return tree;
    }

    protected double countAllChildrens(Map<Integer, List<Class<?>>> childrenTree) {
        return (double) childrenTree.values().stream().mapToLong(Collection::size).sum();
    }

    protected Map<Class<?>, Set<Class<?>>> getAllParents(Set<Class<?>> allClasses) {
        Map<Class<?>, Set<Class<?>>> subclassCountMap = new HashMap<>();
        for (Class<?> clazz : allClasses) {
            Set<Class<?>> parents = getAllClassParents(clazz);
            if (!parents.isEmpty()) {
                subclassCountMap.put(clazz, parents);
            }
        }
        return subclassCountMap;
    }
}
