package edu.wpi.punchy_pegasi.generators;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.frontend.FoodServiceRequestEntry;
import edu.wpi.punchy_pegasi.frontend.RequestEntry;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DaoImplGenerator {
    static String firstLower(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    static String firstUpper(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    static List<Field> getFieldsRecursively(Class<?> clazz) {
        //use getDeclaredFields() to get all fields in this class
        var fields = new java.util.ArrayList<>(Arrays.stream(clazz.getDeclaredFields()).toList());
        if (!clazz.getSuperclass().equals(Object.class))
            fields.addAll(getFieldsRecursively(clazz.getSuperclass()));
        //combine the 2 arrays
        return fields;
    }

    static void genereateFrom(Class<?> clazz, PdbController.TableType tt) throws IOException {
        var ClassText = clazz.getSimpleName();
        // set first letter lowercase
        var classText = firstLower(ClassText);

        var classFields = getFieldsRecursively(clazz).stream().map(f -> f.getName()).toList();
        var ClassFieldsGet = classFields.stream().map(f -> classText + ".get" + firstUpper(f) + "()").toList();

        //read template from GenericDaoImpl
        var template = new BufferedReader(new FileReader("src/main/java/edu/wpi/punchy_pegasi/backend/GenericRequestEntryDaoImpl.java")).lines().toList().stream().reduce("", (a, b) -> a + "\n" + b);

        var fileText = template
                .replaceAll("edu\\.wpi\\.punchy_pegasi\\.backend", "edu.wpi.punchy_pegasi.backend.generated")
                .replaceAll("edu\\.wpi\\.punchy_pegasi\\.frontend\\.GenericRequestEntry", clazz.getName())
                .replaceAll("GenericRequestEntry", ClassText)
                .replaceAll("genericRequestEntry", classText)
                .replaceAll("PdbController\\.TableType\\.GENERIC", "PdbController.TableType." + tt.name())
                .replaceAll("/\\*fields\\*/", String.join(", ", classFields.stream().map(v -> "\"" + v + "\"").toList()))
                .replaceAll("/\\*getFields\\*/", String.join(", ", ClassFieldsGet));

        var lines = new java.util.ArrayList<>(fileText.lines().toList());
        lines.add(2, "import edu.wpi.punchy_pegasi.backend.IDao;");
        lines.add(3, "import edu.wpi.punchy_pegasi.backend.PdbController;");
        lines.add(4, "import edu.wpi.punchy_pegasi.backend.TestDB;");
        lines.add(5, "");
        fileText = lines.stream().reduce("", (a, b) -> a + "\n" + b);


        var fileName = "src/main/java/edu/wpi/punchy_pegasi/backend/generated/" + ClassText + "DaoImpl.java";
        var path = Path.of(DaoImplGenerator.class.getProtectionDomain().getCodeSource().getLocation().getPath(), fileName);
        // write text to file fileName.java
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(fileText);
        writer.close();
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    public static Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    public static void main(String[] args) throws IOException {
        Class<? extends RequestEntry> entryClass = FoodServiceRequestEntry.class;
        PdbController.TableType tableType = PdbController.TableType.FOODREQUESTS;

        // create generated folder if it doesnt exist
        var generatedFolder = new File("src/main/java/edu/wpi/punchy_pegasi/backend/generated");
        if (!generatedFolder.exists())
            generatedFolder.mkdir();

        for (var c : PdbController.TableType.values()) {
            genereateFrom(c.getClazz(), PdbController.TableType.valueOf(c.name()));
        }
    }
}
