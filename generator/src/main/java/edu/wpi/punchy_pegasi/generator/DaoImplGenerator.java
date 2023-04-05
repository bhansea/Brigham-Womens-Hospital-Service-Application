package edu.wpi.punchy_pegasi.generator;

import edu.wpi.punchy_pegasi.generator.schema.TableType;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DaoImplGenerator {

    /**
     * Returns a list containing one parameter name for each argument accepted
     * by the given constructor. If the class was compiled with debugging
     * symbols, the parameter names will match those provided in the Java source
     * code. Otherwise, a generic "arg" parameter name is generated ("arg0" for
     * the first argument, "arg1" for the second...).
     * <p>
     * This method relies on the constructor's class loader to locate the
     * bytecode resource that defined its class.
     *
     * @param constructor
     * @return
     * @throws IOException
     */
    public static List<String> getParameterNames(Constructor<?> constructor) throws IOException {
        Class<?> declaringClass = constructor.getDeclaringClass();
        ClassLoader declaringClassLoader = declaringClass.getClassLoader();

        Type declaringType = Type.getType(declaringClass);
        String constructorDescriptor = Type.getConstructorDescriptor(constructor);
        String url = declaringType.getInternalName() + ".class";

        InputStream classFileInputStream = declaringClassLoader.getResourceAsStream(url);
        if (classFileInputStream == null) {
            throw new IllegalArgumentException("The constructor's class loader cannot find the bytecode that defined the constructor's class (URL: " + url + ")");
        }

        ClassNode classNode;
        try {
            classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classFileInputStream);
            classReader.accept(classNode, 0);
        } finally {
            classFileInputStream.close();
        }

        @SuppressWarnings("unchecked") List<MethodNode> methods = classNode.methods;
        for (MethodNode method : methods) {
            if (method.name.equals("<init>") && method.desc.equals(constructorDescriptor)) {
                Type[] argumentTypes = Type.getArgumentTypes(method.desc);
                List<String> parameterNames = new ArrayList<String>(argumentTypes.length);

                @SuppressWarnings("unchecked") List<LocalVariableNode> localVariables = method.localVariables;
                for (int i = 0; i < argumentTypes.length; i++) {
                    // The first local variable actually represents the "this" object
//                    transform(localVariables.get(i + 1).signature);
                    parameterNames.add(localVariables.get(i + 1).name);
                }

                return parameterNames;
            }
        }

        return null;
    }

    static String firstLower(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    static String firstUpper(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    static List<Field> getFieldsRecursively(Class<?> clazz) {
        //use getDeclaredFields() to get all fields in this class
        var fields = new java.util.ArrayList<>(Arrays.stream(clazz.getDeclaredFields()).toList());
        if (!clazz.getSuperclass().equals(Object.class)) fields.addAll(getFieldsRecursively(clazz.getSuperclass()));
        //combine the 2 arrays
        return fields;
    }

    private static String classResultSetConstructor(Class<?> clazz) throws IOException {
        var ctor = Arrays.stream(clazz.getConstructors()).max(Comparator.comparing(t -> t.getParameterTypes().length)).get();
        var ctorParameterNames = getParameterNames(ctor);

        StringBuilder constructor = new StringBuilder();
        constructor.append("new ").append(clazz.getSimpleName()).append("(\n");
        for (int i = 0; i < ctor.getParameterCount(); i++) {
            var type = ctor.getParameterTypes()[i];
            var column = ctorParameterNames.get(i);
            constructor.append("                    ");
            if (type.isEnum())
                constructor.append(type.getCanonicalName())
                        .append(".valueOf((String)rs.getObject(\"").append(column).append("\"))");
            else if (List.class.isAssignableFrom(type))
                constructor.append("Arrays.asList((")//.append(type.getCanonicalName())
                        .append("String[])rs.getArray(\"").append(column).append("\").getArray())");
            else
                constructor.append("(").append(type.getCanonicalName())
                        .append(")rs.getObject(\"").append(column).append("\")");
            constructor.append(",\n");

        }
        constructor.setLength(constructor.length() - 2);
        constructor.append(");");

        return constructor.toString();
    }

    public static String camelToSnake(String str) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        str = str.replaceAll(regex, replacement).toLowerCase();
        return str;
    }
    static String generateEnum(Class<?> clazz, List<Field> fields) {
        return
"""
    @RequiredArgsConstructor
    public enum Field {
""" + "        " + String.join(",\n        ", fields.stream().map(f -> camelToSnake(f.getName()).toUpperCase() + "(\"" + f.getName() + "\")").toList()) + """
;
        @Getter
        private final String colName;
        public Object getValue(""" + clazz.getCanonicalName() + """
 ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
""" + "            " + String.join("            ", fields.stream().map(f -> "case " + camelToSnake(f.getName()).toUpperCase() + " -> get" + firstUpper(f.getName()) + "();\n").toList()) + """
        };
    }
""";
    }

    static void generateFrom(Class<?> clazz, TableType tt) throws IOException {
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    public static List<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet()).stream().toList();
    }

    private static void generateSchema(Class<?> clazz) throws IOException {
        var schemaSourcePath = Paths.get("generator/src/main/java/edu/wpi/punchy_pegasi/generator/schema", clazz.getSimpleName() + ".java");
        var schemaDestPath = Paths.get("src/main/java/edu/wpi/punchy_pegasi/schema", clazz.getSimpleName() + ".java");
        var daoImplSourcePath = Paths.get("src/main/java/edu/wpi/punchy_pegasi/generator/GenericRequestEntryDaoImpl.java");
        var daoImplDestPath = Paths.get("src/main/java/edu/wpi/punchy_pegasi/generated", clazz.getSimpleName() + "DaoImpl.java");

        var sourceFileText = new String(Files.readAllBytes(schemaSourcePath))
                .replaceAll("edu\\.wpi\\.punchy_pegasi\\.generator\\.schema", "edu.wpi.punchy_pegasi.schema");

        var typeOptional = Arrays.stream(TableType.values()).filter(tt -> tt.getClazz() == clazz).findFirst();
        if(typeOptional.isEmpty()) {
            Files.writeString(schemaDestPath, sourceFileText);
            return;
        }
        var tt = typeOptional.get();

        var ClassText = clazz.getSimpleName();
        // set first letter lowercase
        var classText = firstLower(ClassText);

        var classFields = getFieldsRecursively(clazz);
        var classFieldsText = classFields.stream().map(f -> f.getName()).toList();
        var ClassFieldsGet = classFieldsText.stream().map(f -> classText + ".get" + firstUpper(f) + "()").toList();

        var idFields = classFields.stream().filter(f -> Arrays.stream(f.getAnnotations()).anyMatch(a -> a.annotationType() == SchemaID.class)).toList();
        if (idFields.size() < 1) {
            System.err.println("No id field found for " + clazz.getCanonicalName());
            return;
        }
        if (idFields.size() > 1) {
            System.err.println("More than one id field found for " + clazz.getCanonicalName());
            return;
        }
        var idFieldText = idFields.get(0).getName();
        var idFieldType = idFields.get(0).getType().getCanonicalName();


        // gen schema
        var schemaFileText = sourceFileText
                .replaceAll("@SchemaID[.\n]*", "")
                .replaceAll("import edu\\.wpi\\.punchy_pegasi\\.generator\\.SchemaID;[.\n]*", "").trim();

        var schemaLines = new ArrayList<>(schemaFileText.lines().toList());
        schemaLines.add(3, "import lombok.Getter;\n");
        schemaLines.add(4, "import lombok.RequiredArgsConstructor;\n");
        schemaLines.add(schemaLines.size()-1, generateEnum(clazz, classFields));

        schemaFileText = schemaLines.stream().reduce("", (a, b) -> a + "\n" + b)
                .replaceAll("\\.generator\\.", ".").trim();

        Files.writeString(schemaDestPath, schemaFileText);

        // gen impl
        var template = new String(Files.readAllBytes(daoImplSourcePath));
        var implFileText = template.replaceAll("edu\\.wpi\\.punchy_pegasi\\.generator", "edu.wpi.punchy_pegasi.generated")
                .replaceAll("GenericRequestEntry", ClassText).replaceAll("genericRequestEntry", classText)
                .replaceAll("TableType\\.GENERIC", "TableType." + tt.name())
                .replaceAll("/\\*fields\\*/", String.join(", ", classFieldsText.stream().map(v -> "\"" + v + "\"").toList()))
                .replaceAll("(\\S+)/\\*fromResultSet\\*/.+", "$1 = " + classResultSetConstructor(clazz))
                .replaceAll("/\\*getFields\\*/", String.join(", ", ClassFieldsGet))
                .replaceAll("\"\"/\\*idField\\*/", "\"" + idFieldText + "\"")
                .replaceAll("String/\\*idFieldType\\*/", idFieldType)
                .replaceAll("\"(\\S+)\"/\\*getID\\*/", "$1.get" + firstUpper(idFieldText) + "()")
                .replaceAll("\\.generator\\.", ".").trim();

        var lines = new java.util.ArrayList<>(implFileText.lines().toList());
        lines.add(5, "import java.util.Arrays;");
        lines.add(5, "import java.util.Arrays;");
        implFileText = lines.stream().reduce("", (a, b) -> a + "\n" + b).trim();
        Files.writeString(daoImplDestPath, implFileText);
    }

    /***
     * Currently generates from the TableType enum, and supports any type which has one field with the @SchemaID
     * annotation, consists of only basic Objects, List<String>, Enum for parameters, and whose constructor's have
     * parameter names which EXACTLY match the corresponding field names.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        // generate schema
        var schemaFolder = new File("src/main/java/edu/wpi/punchy_pegasi/schema");
        if (!schemaFolder.exists()) schemaFolder.mkdir();
        var classes = findAllClassesUsingClassLoader("edu.wpi.punchy_pegasi.generator.schema");
        for (var clazz : classes) {
            try {
                generateSchema(clazz);
            } catch (Exception e){
                System.out.println("Failed to generate shema for " + clazz.getCanonicalName());
            }
        }

        // generate DaoImpls
        var generatedFolder = new File("src/main/java/edu/wpi/punchy_pegasi/generated");
        if (!generatedFolder.exists()) generatedFolder.mkdir();
        for (var c : Arrays.stream(TableType.values()).filter(v -> v != TableType.GENERIC).toList()) {
            try {
                generateFrom(c.getClazz(), TableType.valueOf(c.name()));
            } catch (Exception e) {
                System.err.println("Failed to generate impl for: " + c.getClazz().getCanonicalName());
                e.printStackTrace();
            }
        }
    }

    public static String transform(String signature) {
        SignatureReader reader = new SignatureReader(signature);
        SignatureWriter writer = new SignatureWriter();

        SignatureVisitor visitor = new SignatureVisitor(Opcodes.ASM9) {
            StringBuilder sb = new StringBuilder();

            @Override
            public void visitClassType(String name) {
                sb.append(name.replace('/', '.'));
                sb.append('.');
            }

            @Override
            public void visitTypeArgument() {
                sb.append('<');
            }

            @Override
            public SignatureVisitor visitTypeArgument(char wildcard) {
                sb.append(wildcard);
                return this;
            }

            @Override
            public void visitEnd() {
                if (sb.length() > 0) {
                    sb.setLength(sb.length() - 1); // remove the last '.'
                }
                sb.append('>');
                writer.visitClassType(sb.toString());
                sb.setLength(0);
            }
        };

        reader.accept(visitor);

        return writer.toString();
    }
}
