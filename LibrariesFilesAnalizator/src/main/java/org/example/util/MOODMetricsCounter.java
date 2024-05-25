package org.example.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class MOODMetricsCounter extends LibraryMetricsCounter{

    public void countMOODMetrics(String packagePath) {
        Set<Class<?>> allClasses = loadAllClasses(packagePath);
        Map<Class<?>, Set<Class<?>>> allParents = getAllParents(allClasses);


    }

    private int countFullyInheritedMethods(Class<?> clazz, Set<Class<?>> parents){
        return getAllParentsMethods(parents).size() - countOverridedMethods(clazz, parents);
    }
    private int countFullyInheritedAttributes(Class<?> clazz, Set<Class<?>> parents) {
        return getAllParentsAttributes(parents).size() - countOverridedAttributes(clazz, parents);
    }
    private int countOverridedMethods(Class<?> clazz, Set<Class<?>> parents) {
        Set<Method> classMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> !Modifier.isPrivate(method.getModifiers()))
                .collect(Collectors.toSet());
        Set<Method> allParentsMethods = getAllParentsMethods(parents);
        int numberOfOverrided = 0;
        for(Method parMethod : allParentsMethods) {
            if(classMethods.contains(parMethod)) {
                numberOfOverrided++;
            }
        }
        return numberOfOverrided;
    }
    private int countOverridedAttributes(Class<?> clazz, Set<Class<?>> parents) {
        Set<Field> classMethods = Arrays.stream(clazz.getDeclaredFields())
                .filter(method -> !Modifier.isPrivate(method.getModifiers()))
                .collect(Collectors.toSet());
        Set<Field> allParentsFields = getAllParentsAttributes(parents);
        int numberOfOverrided = 0;
        for(Field parFields : allParentsFields) {
            if(classMethods.contains(parFields)) {
                numberOfOverrided++;
            }
        }
        return numberOfOverrided;
    }
    private int countHiddenAttributes(Class<?> clazz) {
        return (int) Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> Modifier.isPrivate(field.getModifiers()))
                .count();
    }
    private int countHiddenMethods(Class<?> clazz) {
        return (int) Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> Modifier.isPrivate(method.getModifiers()))
                .count();
    }

    private double countNewMethods(Class<?> clazz, Set<Class<?>> parents) {
        return Arrays.stream(clazz.getDeclaredMethods()).count() - countOverridedMethods(clazz, parents);
    }

    private Set<Method> getAllParentsMethods(Set<Class<?>> parents) {
        return parents.stream()
                .flatMap(par -> Arrays.stream(par.getDeclaredMethods()))
                .filter(method -> !Modifier.isPrivate(method.getModifiers()))
                .collect(Collectors.toSet());
    }

    private Set<Field> getAllParentsAttributes(Set<Class<?>> parents) {
        return parents.stream()
                .flatMap(par -> Arrays.stream(par.getDeclaredFields()))
                .filter(field -> !Modifier.isPrivate(field.getModifiers()))
                .collect(Collectors.toSet());
    }

    private double countMethodInheritanceFactor(Class<?> clazz, Set<Class<?>> parents) {
        return (double) countFullyInheritedMethods(clazz, parents) / Arrays.stream(clazz.getDeclaredMethods()).count();
    }

    private double countAttributeInheritanceFactor(Class<?> clazz, Set<Class<?>> parents) {
        return (double) countFullyInheritedAttributes(clazz, parents) / Arrays.stream(clazz.getDeclaredFields()).count();
    }


    private double countMethodHidingFactor(Class<?> clazz, Set<Class<?>> allClasses) {
        Method[] methods = clazz.getDeclaredMethods();
        int totalMethods = methods.length;
        int totalClasses = allClasses.size();

        if (totalMethods == 0) {
            return 1.0;
        }

        double sumInvisibilities = 0.0;

        for (Method method : methods) {
            int modifiers = method.getModifiers();
            double invisibility = calculateInvisibilityMethods(modifiers, totalClasses);
            sumInvisibilities += invisibility;
        }

        return sumInvisibilities / totalMethods;
    }


    private double countAttributeHidingFactor(Class<?> clazz, Set<Class<?>> allClasses) {
        Field[] fields = clazz.getDeclaredFields();
        int totalFields = fields.length;
        int totalClasses = allClasses.size();

        if (totalFields == 0) {
            return 1.0;
        }

        double sumInvisibilities = 0.0;

        for (Field field : fields) {
            int modifiers = field.getModifiers();
            double invisibility = calculateInvisibilityFields(modifiers, totalClasses);
            sumInvisibilities += invisibility;
        }

        return sumInvisibilities / totalFields;
    }

    private double countPolymorphismObjectFactor(Class<?> clazz, Set<Class<?>> parents, Set<Class<?>> allClasses) {
        return countFullyInheritedMethods(clazz, parents) / (countNewMethods(clazz, parents) * super.countAllChildrens(super.getAllChildrens(clazz, allClasses, 1, new HashMap<>())));
    }

    private double calculateInvisibilityFields(int modifiers, int totalClasses) {
        if (Modifier.isPrivate(modifiers)) {
            return 1.0;
        } else if (Modifier.isProtected(modifiers)) {
            return (totalClasses - 1) / (double) totalClasses;
        } else if (Modifier.isPublic(modifiers)) {
            return 0.0;
        } else {
            return (totalClasses - 1) / (double) totalClasses;
        }
    }
    private double calculateInvisibilityMethods(int modifiers, int totalClasses) {
        if (Modifier.isPrivate(modifiers)) {
            return 1.0;
        } else if (Modifier.isProtected(modifiers)) {
            return (totalClasses - 1) / (double) totalClasses;
        } else if (Modifier.isPublic(modifiers)) {
            return 0.0;
        } else {
            return (totalClasses - 1) / (double) totalClasses;
        }
    }

}
