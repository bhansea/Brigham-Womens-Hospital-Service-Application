package edu.wpi.punchy_pegasi.generator;

import edu.wpi.punchy_pegasi.generator.schema.Facade;
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
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class  SleepThroughTheWinter {

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
    private static List<String> getParameterNames(Constructor<?> constructor) throws IOException {
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

    public static String firstLower(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    public static String firstUpper(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private static List<Field> getFieldsRecursively(Class<?> clazz) {
        //use getDeclaredFields() to get all fields in this class
        var fields = new java.util.ArrayList<>(Arrays.stream(clazz.getDeclaredFields()).toList());
        if (!clazz.getSuperclass().equals(Object.class)) fields.addAll(0, getFieldsRecursively(clazz.getSuperclass()));
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
                constructor.append("java.util.Arrays.asList((")//.append(type.getCanonicalName())
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

    private static String generateEnum(Class<?> clazz, List<Field> fields) {
        return
                """
                            @lombok.RequiredArgsConstructor
                            public enum Field {
                        """ + "        " + String.join(",\n        ", fields.stream().map(f -> camelToSnake(f.getName()).toUpperCase() + "(\"" + f.getName() + "\")").toList()) + """
                        ;
                                @lombok.Getter
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

    private static Map<Class<?>, String> classToPostgres = new HashMap<>() {{
        put(Long.class, "bigint");
        put(Integer.class, "int");
        put(String.class, "varchar");
        put(UUID.class, "uuid DEFAULT uuid_generate_v4()");
        put(List.class, "varchar ARRAY");
        put(LocalDate.class, "date");
    }};

    private static boolean fieldIsID(Field field){
        // locate the @SchemaID annotation
        return Arrays.stream(field.getAnnotations()).anyMatch(a -> a.annotationType() == SchemaID.class);
    }

    private static String generateTableInit(TableType tt){
        var clazz = tt.getClazz();
        var tableName = "teamp." + tt.name().toLowerCase();
        var sequenceName = tt.name().toLowerCase() + "_id_seq";
        var classFields = getFieldsRecursively(clazz);
        var idField = classFields.stream().filter(SleepThroughTheWinter::fieldIsID).findFirst().get();
        var tableColumns = classFields.stream().map(f -> {
            var appendText = fieldIsID(f) ?
                    f.getType() == Long.class ?
                            " DEFAULT nextval('" + sequenceName + "') PRIMARY KEY"
                            : " PRIMARY KEY"
                    : "";
            if (f.getType().isEnum())
                return "" + f.getName() + " varchar" + appendText;
            return "" + f.getName() + " " + classToPostgres.get(f.getType()) + appendText;
        }).toList();

        if (idField.getType() == UUID.class)
            return String.format("""
                    %s(%s.class, \"\"\"
                    CREATE TABLE IF NOT EXISTS %s
                    (
                      %s
                    );
                    \"\"\")
                    """, tt.name(), clazz.getCanonicalName(), tableName, String.join(",\n  ", tableColumns));
        else
            return String.format("""
                    %1$s(%2$s.class, \"\"\"
                    DO $$
                    BEGIN
                      IF to_regclass('%3$s') IS NULL THEN
                        CREATE SEQUENCE %4$s;
                        CREATE TABLE %3$s
                        (
                          %5$s
                        );
                        ALTER SEQUENCE %4$s OWNED BY %3$s.%6$s;
                      END IF;
                    END $$;
                    \"\"\")""", tt.name(), clazz.getCanonicalName(), tableName, sequenceName, String.join(",\n      ", tableColumns), idField.getName());
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

    private static void generateTable() throws IOException {
        var schemaSourcePath = Paths.get("generator/src/main/java/edu/wpi/punchy_pegasi/generator/schema", TableType.class.getSimpleName() + ".java");
        var schemaDestPath = Paths.get("src/main/java/edu/wpi/punchy_pegasi/schema", TableType.class.getSimpleName() + ".java");
        var sourceFileText = new String(Files.readAllBytes(schemaSourcePath))
                .replaceAll("edu\\.wpi\\.punchy_pegasi\\.generator\\.schema", "edu.wpi.punchy_pegasi.schema");

        for(var tt : TableType.values())
            sourceFileText = sourceFileText.replaceFirst(tt.name() + "\\([^\\)]*\\)",
                    Matcher.quoteReplacement(generateTableInit(tt)));
        Files.writeString(schemaDestPath, sourceFileText);
    }

    private static void generateEntry(Class<?> entryClass) throws IOException, IllegalStateException {
        var schemaSourcePath = Paths.get("generator/src/main/java/edu/wpi/punchy_pegasi/generator/schema", entryClass.getSimpleName() + ".java");
        var schemaDestPath = Paths.get("src/main/java/edu/wpi/punchy_pegasi/schema", entryClass.getSimpleName() + ".java");
        var daoImplSourcePath = Paths.get("src/main/java/edu/wpi/punchy_pegasi/generator/GenericRequestEntryDaoImpl.java");
        var daoImplDestPath = Paths.get("src/main/java/edu/wpi/punchy_pegasi/generated", entryClass.getSimpleName() + "DaoImpl.java");
        var sourceFileText = new String(Files.readAllBytes(schemaSourcePath))
                .replaceAll("edu\\.wpi\\.punchy_pegasi\\.generator\\.schema", "edu.wpi.punchy_pegasi.schema");

        var typeOptional = Arrays.stream(TableType.values()).filter(tt -> tt.getClazz() == entryClass).findFirst();
        if (typeOptional.isEmpty()) {
            Files.writeString(schemaDestPath, sourceFileText);
            throw new IllegalStateException("No table type found for " + entryClass.getCanonicalName());
        }
        var tt = typeOptional.get();

        var idField = getIdField(entryClass);

        var ClassText = entryClass.getSimpleName();
        // set first letter lowercase
        var classText = firstLower(ClassText);


        var classFields = getFieldsRecursively(entryClass);
        var classFieldsText = classFields.stream().map(Field::getName).toList();
        var ClassFieldsGet = classFieldsText.stream().map(f -> classText + ".get" + firstUpper(f) + "()").toList();

        var idFieldText = idField.getName();
        var idFieldType = idField.getType().getCanonicalName();


        // gen schema
        var schemaFileText = sourceFileText  // remove @SchemaID annotations
                .replaceAll("@SchemaID[.\n]*", "")
                .replaceAll("import edu\\.wpi\\.punchy_pegasi\\.generator\\.SchemaID;[.\n]*", "").trim();

        var schemaLines = new ArrayList<>(schemaFileText.lines().toList());
        schemaLines.add(schemaLines.size()-1, generateEnum(entryClass, classFields));

        schemaFileText = schemaLines.stream().reduce("", (a, b) -> a + "\n" + b)
                .replaceAll("\\.generator\\.", ".").trim();

        Files.writeString(schemaDestPath, schemaFileText);

        // gen impl
        var template = new String(Files.readAllBytes(daoImplSourcePath));
        var implFileText = template.replaceAll("edu\\.wpi\\.punchy_pegasi\\.generator", "edu.wpi.punchy_pegasi.generated")
                .replaceAll("/\\*FacadeClassName\\*/", "")
                .replaceAll("GenericRequestEntry", ClassText).replaceAll("genericRequestEntry", classText)
                .replaceAll("TableType\\.GENERIC", "TableType." + tt.name())
                .replaceAll("/\\*fields\\*/", String.join(", ", classFieldsText.stream().map(v -> "\"" + v + "\"").toList()))
                .replaceAll("(\\S+)/\\*fromResultSet\\*/[^(null;)]*null;", "$1 = " + classResultSetConstructor(entryClass))
                .replaceAll("/\\*getFields\\*/", String.join(", ", ClassFieldsGet))
                .replaceAll("\"\"/\\*idField\\*/", "\"" + idFieldText + "\"")
                .replaceAll("String/\\*idFieldType\\*/", idFieldType)
                .replaceAll("\"(\\S+)\"/\\*getID\\*/", "$1.get" + firstUpper(idFieldText) + "()")
                .replaceAll("\\.generator\\.", ".").trim();

        var lines = new java.util.ArrayList<>(implFileText.lines().toList());
        implFileText = lines.stream().reduce("", (a, b) -> a + "\n" + b).trim();
        Files.writeString(daoImplDestPath, implFileText);
    }

    private static Field getIdField(Class<?> clazz) throws IllegalStateException{
        var classFields = getFieldsRecursively(clazz);
        var idFields = classFields.stream().filter(SleepThroughTheWinter::fieldIsID).toList(); // locate id field with @SchemaID
        if (idFields.size() < 1) {
            // check if no id annotation (@SchemaID) is present
            throw new IllegalStateException("No id field found for " + clazz.getCanonicalName());
        }
        if (idFields.size() > 1) {
            // check if more than one id annotation (@SchemaID) is present
            throw new IllegalStateException("More than one id field found for " + clazz.getCanonicalName());
        }
        return idFields.get(0);
    }

    private static StringBuilder generateFacadeEntry(Class<?> entryClass) throws IOException, IllegalStateException {
        var daoImplSourcePath = Paths.get("src/main/java/edu/wpi/punchy_pegasi/generator/GenericRequestEntryDaoImpl.java");
        var template = new String(Files.readAllBytes(daoImplSourcePath));
        StringBuilder resultText = new StringBuilder();

        var ClassName = entryClass.getSimpleName();
        if (ClassName.equals("GenericRequestEntry")) return resultText;  // skip GenericRequestEntry
        var className = firstLower(ClassName);
        var id_field = getIdField(entryClass);
//        var id_field_name = id_field.getName();
        var id_field_type = id_field.getType().getCanonicalName();

        Pattern pattern = Pattern.compile("(public\\s+[^{]{3,})\\{");  // extract all public method header
        Pattern pattern1 = Pattern.compile("(\\w+)\\s*/\\*FacadeClassName\\*/");  // extract fcn name
        Pattern pattern2 = Pattern.compile("(\\([^()]*\\))");  // extract fcn args
        Pattern pattern3 = Pattern.compile("(?:,\\s*)?(\\w+)\\s*(?:=[^,)]+)?(?=[,)])");  // extract fcn args name
        Pattern pattern4 = Pattern.compile("public\\s*(\\w*)");  // extract return type
        Matcher matcher = pattern.matcher(template);

        for (int i = 0; i < 3; i++) matcher.find();  // skip first 3 matches

        while (matcher.find()) {
            var fcnHeader = matcher.group(1);

            Matcher matcher1 = pattern1.matcher(fcnHeader);
            if (!matcher1.find()) {
                throw new IllegalStateException("No function name found for " + ClassName);
            }
            var fcnName = matcher1.group(1);

            fcnHeader = fcnHeader.replaceAll("/\\*FacadeClassName\\*/", ClassName)
                    .replaceAll("String/\\*idFieldType\\*/", id_field_type)
                    .replaceAll("GenericRequestEntry", ClassName).replaceAll("genericRequestEntry", className);

            Matcher matcher2 = pattern2.matcher(fcnHeader);
            var argsField = "";
            if (matcher2.find()){
                argsField = matcher2.group(1);
            } else {
                throw new IllegalStateException("No function args found for " + ClassName);
            }

            var fcnArgs = "";
            var sb = new StringBuilder();
            Matcher matcher3 = pattern3.matcher(argsField);
            while (matcher3.find()){
                sb.append(matcher3.group(1)).append(", ");
            }
            if (sb.length() > 0)
                fcnArgs = sb.substring(0, sb.length()-2);

            Matcher matcher4 = pattern4.matcher(fcnHeader);
            if (!matcher4.find()) {
                throw new IllegalStateException("No return type found for " + ClassName);
            } else {
                if (matcher4.group(1).equals("void")) {
                    resultText.append("\t")
                            .append(fcnHeader)
                            .append("{\n")
                            .append(String.format("""
                                           \t\t%1$s.%2$s(%3$s);
                                           \t}
                                           """, className+"Dao", fcnName, fcnArgs
                            ));
                } else {
                    resultText.append("\t")
                            .append(fcnHeader)
                            .append("{\n")
                            .append(String.format("""
                                           \t\treturn %1$s.%2$s(%3$s);
                                           \t}
                                           """, className+"Dao", fcnName, fcnArgs
                            ));
                }
            }
        }
        return resultText;
    }

    private static void generateFacade() throws IOException, IllegalStateException{
        var facadeDestPath = Paths.get("src/main/java/edu/wpi/punchy_pegasi/generated/Facade.java");
        var facadeSourcePath = Paths.get("generator/src/main/java/edu/wpi/punchy_pegasi/generator/schema/Facade.java");

        StringBuilder sbDaoDec = new StringBuilder();
        StringBuilder sbDaoInit = new StringBuilder();
        for (var clazz : Arrays.stream(TableType.values()).map(TableType::getClazz).toList()){
            try {
                var ClassName = clazz.getSimpleName();
                if (ClassName.equals("GenericRequestEntry")) continue;  // skip GenericRequestEntry
                var className = firstLower(ClassName);
                sbDaoDec.append("\tprivate final " + ClassName + "DaoImpl " + className + "Dao;\n");
                sbDaoInit.append("\t\t" + className + "Dao = new " + ClassName + "DaoImpl(this.dbController);\n");
            } catch (Exception e) {
                System.err.println("Failed to initialize Dao Impl for " + clazz.getCanonicalName() + ": " + e.getMessage());
            }
        }
        var template = new String(Files.readAllBytes(facadeSourcePath))
                .replaceAll("}\n*$", "")
                .replaceAll("/\\*Dao Declarations\\*/", sbDaoDec.toString())
                .replaceAll("/\\*Dao Initialization\\*/", sbDaoInit.toString())
                .replaceAll("\\*/", "")
                .replaceAll("/\\*", "")
                .replaceAll("package edu\\.wpi\\.punchy_pegasi\\.generator\\.schema;",
                        """
                                package edu.wpi.punchy_pegasi.generated;
                                                                
                                import edu.wpi.punchy_pegasi.schema.*;
                                import edu.wpi.punchy_pegasi.backend.PdbController;
                                import java.util.Map;
                                import java.util.Optional;
                                """);
        StringBuilder sbTemplate = new StringBuilder();
        sbTemplate.append(template);
        for (var clazz : Arrays.stream(TableType.values()).map(TableType::getClazz).toList()) {
            try {
                sbTemplate.append(generateFacadeEntry(clazz));
            } catch (Exception e) {
                System.err.println("Failed to generate schema for " + clazz.getCanonicalName() + ": " + e.getMessage());
            }
        }
        var facadeFileText = sbTemplate.append("}").toString();
        Files.writeString(facadeDestPath, facadeFileText);
    }

    /***
     * Currently generates from the TableType enum, and supports any type which has one field with the @SchemaID
     * annotation, consists of only basic Objects, List<String>, Enum for parameters, and whose constructor's have
     * parameter names which EXACTLY match the corresponding field names.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // generate destination folders
        var schemaFolder = new File("src/main/java/edu/wpi/punchy_pegasi/schema");
        if (!schemaFolder.exists()) schemaFolder.mkdir();
        var generatedFolder = new File("src/main/java/edu/wpi/punchy_pegasi/generated");
        if (!generatedFolder.exists()) generatedFolder.mkdir();
        var schemaSourceDir = Paths.get("generator/src/main/java/edu/wpi/punchy_pegasi/generator/schema/");
        var schemaDestDir = Paths.get("src/main/java/edu/wpi/punchy_pegasi/schema/");
        // default, copy files over
        try (var fileWalk = Files.walk(schemaSourceDir)) {
            for (var schemaSourceFile : fileWalk.filter(Files::isRegularFile).toList()) {
                var schemaDestFile = schemaDestDir.resolve(schemaSourceDir.relativize(schemaSourceFile));
                Files.writeString(schemaDestFile, new String(Files.readAllBytes(schemaSourceFile))
                        .replaceAll("edu\\.wpi\\.punchy_pegasi\\.generator\\.schema", "edu.wpi.punchy_pegasi.schema"));
            }
        }
        generateFacade();
        generateTable();
        for (var clazz : Arrays.stream(TableType.values()).map(TableType::getClazz).toList()) {
            try {
                generateEntry(clazz);
            } catch (Exception e) {
                System.err.println("Failed to generate schema for " + clazz.getCanonicalName() + ": " + e.getMessage());
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
