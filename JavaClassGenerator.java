/**

Program Name: JavaClassGenerator.java
Description: This program generates Java classes, interfaces, enums, and other related files based on a JSON configuration file.
             It supports JPA annotations, Lombok, and Spring Data JPA for generating repositories and services.
             The script also creates configuration files for Spring Boot applications.

Author: Junior ADI <rootoor.projects@gmail.com> | 
Twitter/X : @caifyoca

Version: 1.0
Date: 2025-01-13
License: GPLv3

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <https://www.gnu.org/licenses/>.

Usage:
The `data.json` file should be placed in the **root directory** of your project (the same directory where your `JavaClassGenerator.java` file is located). This ensures that the program can locate and read the file when it runs.

### Steps to Place `data.json`:

1. **Create the `data.json` file**:
   - Copy the JSON content you provided into a file and save it as `data.json`.

2. **Place it in the root directory**:
   - If your project structure looks like this:
     ```
     MyProject/
     ├── src/
     │   └── JavaClassGenerator.java
     ├── data.json
     └── ...
     ```
   - Place `data.json` in the `MyProject` folder (the root directory).

3. **Verify the file path**:
   - In the `JavaClassGenerator.java` code, the `filePath` is set to `'data.json'`. This assumes the file is in the same directory as the Java program. If you place it elsewhere, update the `filePath` variable accordingly.

4. **Run the program**:
   - When you run the `JavaClassGenerator` program, it will read the `data.json` file from the root directory and generate the Java classes based on the configuration.

---

### Example Project Structure:

```
MyProject/
├── src/
│   └── JavaClassGenerator.java
├── data.json
├── generated_classes/
│   ├── com/
│   │   └── example/
│   │       ├── entity/
│   │       ├── controller/
│   │       ├── repository/
│   │       └── service/
│   └── ...
└── ...
```

---

### Troubleshooting:

1. **File Not Found Error**:
   - If you see an error like `Error: The file data.json does not exist or contains invalid JSON data`, double-check the location of `data.json`. It should be in the same directory as your Java program.

2. **Relative Path**:
   - If you want to place `data.json` in a different directory, update the `filePath` variable in the code to the correct relative or absolute path. For example:
     ```java
     String filePath = "path/to/your/data.json";
     ```

3. **File Permissions**:
   - Ensure that the file has read permissions so the program can access it.

---

### Example of Updated `filePath`:

If `data.json` is placed in a subdirectory called `config`, update the `filePath` like this:
```java
String filePath = "config/data.json";
```

This ensures the program can locate the file correctly.

Dependencies:
    - create `pom.xml`
    - add `jackson-databind` dependency below into the pom.xml
    
    <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.13.3</version>
    </dependency>
*/
package ci.abidjan.adi;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JavaClassGenerator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        String filePath = "data.json";
        System.out.println("\n" + "=".repeat(80));
        System.out.println(centerText("Loading data from the JSON file...", 80));
        System.out.println("=".repeat(80));

        Map<String, Object> data = verifyData(filePath);

        if (data != null) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println(centerText("Data loaded successfully. Starting class generation...", 80));
            System.out.println("=".repeat(80));

            List<String> generatedClasses = generateClasses((List<Map<String, Object>>) data.get("entities"), (Map<String, Object>) data.get("configuration_variables"));
            System.out.println("\n\t📦 " + generatedClasses.size() + " model classes generated successfully.");

            if (data.containsKey("interface_classes")) {
                generateInterfaces((List<Map<String, Object>>) data.get("interface_classes"), (Map<String, Object>) data.get("configuration_variables"));
                System.out.println("\t📜 " + ((List<?>) data.get("interface_classes")).size() + " interfaces generated successfully.");
            }

            if (data.containsKey("embeddable_classes")) {
                generateEmbeddables((List<Map<String, Object>>) data.get("embeddable_classes"), (Map<String, Object>) data.get("configuration_variables"));
                System.out.println("\t📎 " + ((List<?>) data.get("embeddable_classes")).size() + " embeddable classes generated successfully.");
            }

            if (data.containsKey("enum_classes")) {
                generateEnums((List<Map<String, Object>>) data.get("enum_classes"), (Map<String, Object>) data.get("configuration_variables"));
                System.out.println("\t🔢 " + ((List<?>) data.get("enum_classes")).size() + " enums generated successfully.");
            }

            generateRepositories((List<Map<String, Object>>) data.get("entities"), (Map<String, Object>) data.get("configuration_variables"));
            generateServices((List<Map<String, Object>>) data.get("entities"), (Map<String, Object>) data.get("configuration_variables"));
            generateControllers((List<Map<String, Object>>) data.get("entities"), (Map<String, Object>) data.get("configuration_variables"));
            generateConfigurationFiles((Map<String, Object>) data.get("configuration_variables"));

            if (askDisplay()) {
                System.out.println("\n" + "=".repeat(80));
                System.out.println(centerText("Displaying generated classes:", 80));
                System.out.println("=".repeat(80));
                for (String classCode : generatedClasses) {
                    System.out.println(classCode);
                    System.out.println("=".repeat(80));
                    System.out.println("\n");
                }
            } else {
                String outputDir = (String) ((Map<String, Object>) data.get("configuration_variables")).getOrDefault("OUT_PUT_DIR", "./generated_classes");
                System.out.println("\n" + "=".repeat(80));
                System.out.println(centerText("Generation completed!!!", 80));
                System.out.println("=".repeat(80));
                System.out.println("Go to the '" + outputDir + "' directory to see the generated files.");
                System.out.println("Thank you for using our script!");
            }
        } else {
            System.out.println("\n" + "=".repeat(80));
            System.out.println(centerText("Error: Unable to generate classes.", 80));
            System.out.println("=".repeat(80));
            System.out.println("Reason: The data is invalid or missing.");
        }
    }

    private static Map<String, Object> verifyData(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Map<String, Object> data = objectMapper.readValue(content, Map.class);
            if (data.containsKey("entities") && data.containsKey("configuration_variables")) {
                System.out.println("Data loaded from the file " + filePath + ".");
                return data;
            } else {
                System.out.println("Error: The data loaded from the file " + filePath + " is not valid.");
            }
        } catch (IOException e) {
            System.out.println("Error: The file " + filePath + " does not exist or contains invalid JSON data.");
        }
        return null;
    }

    private static List<String> generateClasses(List<Map<String, Object>> entities, Map<String, Object> configurationVariables) {
        List<String> generatedClasses = new ArrayList<>();
        String outputDir = (String) configurationVariables.getOrDefault("OUT_PUT_DIR", "./generated_classes");
        new File(outputDir).mkdirs();

        for (Map<String, Object> entity : entities) {
            try {
                validateEntity(entity);
                String classCode = generateClass(entity, configurationVariables, entities);
                generatedClasses.add(classCode);

                if (configurationVariables.containsKey("generate_classes_following_packages")) {
                    String packageName = (String) configurationVariables.getOrDefault("model_classes_package", "");
                    String packagePath = packageName.replace(".", "/");
                    String classOutputDir = outputDir + "/" + packagePath;
                    new File(classOutputDir).mkdirs();
                    String filePath = classOutputDir + "/" + entity.get("entity_name") + configurationVariables.get("entity_suffix") + ".java";
                    Files.write(Paths.get(filePath), classCode.getBytes());
                }
            } catch (Exception e) {
                System.out.println("Error while generating the class " + entity.getOrDefault("entity_name", "Unknown") + ": " + e.getMessage());
            }
        }

        return generatedClasses;
    }

    private static void validateEntity(Map<String, Object> entity) {
        String[] requiredFields = {"entity_name", "fields"};
        for (String field : requiredFields) {
            if (!entity.containsKey(field)) {
                throw new IllegalArgumentException("Missing required field: " + field);
            }
        }
    }

    private static String generateClass(Map<String, Object> entity, Map<String, Object> configurationVariables, List<Map<String, Object>> entities) {
        StringBuilder classCode = new StringBuilder();

        if (configurationVariables.containsKey("model_classes_package")) {
            classCode.append("package ").append(configurationVariables.get("model_classes_package")).append(";\n\n");
        }

        if ((Boolean) configurationVariables.get("jpa_used")) {
            if ((Boolean) configurationVariables.getOrDefault("jakarta_persistence_api", false)) {
                classCode.append("import jakarta.persistence.*;\n\n");
            } else {
                classCode.append("import javax.persistence.*;\n\n");
            }
        }

        if ((Boolean) configurationVariables.get("jpa_used") || (!(Boolean) configurationVariables.get("jpa_used") && (Boolean) configurationVariables.get("pojo_model_beanified"))) {
            classCode.append("import java.io.Serializable;\n\n");
        }

        Set<String> lombokAnnotations = new HashSet<>(Arrays.asList("@NoArgsConstructor", "@RequiredArgsConstructor", "@AllArgsConstructor", "@Getter", "@Setter",
                "@ToString", "@EqualsAndHashCode", "@Data", "@Value", "@Builder", "@With", "@NonNull", "@SneakyThrows", "@Synchronized"));
        Set<String> presentAnnotations = new HashSet<>();

        if (entity.containsKey("entity_supplementary_annotations")) {
            for (String annotation : (List<String>) entity.get("entity_supplementary_annotations")) {
                if (lombokAnnotations.contains(annotation)) {
                    presentAnnotations.add(annotation);
                }
            }
        }

        for (Map<String, Object> field : (List<Map<String, Object>>) entity.get("fields")) {
            if (field.containsKey("field_annotations")) {
                for (String annotation : (List<String>) field.get("field_annotations")) {
                    if (lombokAnnotations.contains(annotation)) {
                        presentAnnotations.add(annotation);
                    }
                }
            }
        }

        if (!presentAnnotations.isEmpty()) {
            classCode.append("import lombok.*;\n\n");
        }

        if (entity.containsKey("entity_supplementary_annotations")) {
            for (String annotation : (List<String>) entity.get("entity_supplementary_annotations")) {
                if (lombokAnnotations.contains(annotation)) {
                    classCode.append(annotation).append("\n");
                }
            }
        }

        if ((Boolean) entity.get("entity_is_parent")) {
            if ((Boolean) configurationVariables.get("jpa_used")) {
                classCode.append(generateInheritanceAnnotations(entity));
            }
            classCode.append("public abstract class ").append(entity.get("entity_name")).append(configurationVariables.get("entity_suffix"));
        } else if (entity.containsKey("entity_parent_name")) {
            try {
                Map<String, Object> parentEntity = entities.stream()
                        .filter(e -> e.get("entity_name").equals(entity.get("entity_parent_name")))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Parent entity '" + entity.get("entity_parent_name") + "' not found for entity '" + entity.get("entity_name") + "'"));

                String parentStrategy = (String) parentEntity.get("entity_inheritance_strategy");

                if ((Boolean) configurationVariables.get("jpa_used")) {
                    if (parentStrategy.equals("SINGLE_TABLE")) {
                        classCode.append("@Entity\n@DiscriminatorValue(\"").append(entity.get("discriminator_value")).append("\")\n");
                    } else if (parentStrategy.equals("JOINED")) {
                        classCode.append("@Entity\n@Table(name=\"").append(entity.get("entity_name")).append("s\")\n");
                    } else if (parentStrategy.equals("TABLE_PER_CLASS")) {
                        classCode.append("@Entity\n@Table(name=\"").append(entity.get("entity_name")).append("s\")\n");
                    } else if (parentStrategy.equals("MAPPED_SUPERCLASS") || parentStrategy.equals("MAPPED_SUPER_CLASS")) {
                        classCode.append("@Entity\n@Table(name=\"").append(entity.get("entity_name")).append("s\")\n");
                    }
                }

                classCode.append("public class ").append(entity.get("entity_name")).append(configurationVariables.get("entity_suffix")).append(" extends ").append(entity.get("entity_parent_name")).append(configurationVariables.get("entity_suffix"));
            } catch (Exception e) {
                throw new IllegalArgumentException("Parent entity '" + entity.get("entity_parent_name") + "' not found for entity '" + entity.get("entity_name") + "'");
            }
        } else {
            if ((Boolean) configurationVariables.get("jpa_used")) {
                classCode.append("@Entity\n@Table(name=\"").append(entity.get("entity_name")).append("s\")\n");
            }
            classCode.append("public class ").append(entity.get("entity_name")).append(configurationVariables.get("entity_suffix"));
        }

        if (entity.containsKey("interfaces_implemented") || (Boolean) configurationVariables.get("jpa_used") || (!(Boolean) configurationVariables.get("jpa_used") && (Boolean) configurationVariables.get("pojo_model_beanified"))) {
            classCode.append(" implements");
            if ((Boolean) configurationVariables.get("jpa_used") || (!(Boolean) configurationVariables.get("jpa_used") && (Boolean) configurationVariables.get("pojo_model_beanified"))) {
                classCode.append(" Serializable");
                if (entity.containsKey("interfaces_implemented")) {
                    classCode.append(", ").append(String.join(", ", (List<String>) entity.get("interfaces_implemented")));
                }
            } else {
                classCode.append(" ").append(String.join(", ", (List<String>) entity.get("interfaces_implemented")));
            }
        }
        classCode.append(" {\n");

        for (Map<String, Object> field : (List<Map<String, Object>>) entity.get("fields")) {
            classCode.append(generateField(field, configurationVariables, (String) entity.get("entity_name")));
        }

        if ((Boolean) configurationVariables.get("add_models_no_and_all_args_constructors") && !presentAnnotations.stream().anyMatch(annotation -> annotation.equals("@NoArgsConstructor") || annotation.equals("@RequiredArgsConstructor") || annotation.equals("@AllArgsConstructor"))) {
            classCode.append(generateConstructors(entity, configurationVariables));
        }

        if ((Boolean) configurationVariables.get("add_model_class_getters_setters") && !presentAnnotations.stream().anyMatch(annotation -> annotation.equals("@Getter") || annotation.equals("@Setter") || annotation.equals("@Data") || annotation.equals("@Value"))) {
            classCode.append(generateGettersSetters(entity));
        }

        if ((Boolean) configurationVariables.get("add_model_class_hashcode_equals_tostring_methods") && !presentAnnotations.stream().anyMatch(annotation -> annotation.equals("@EqualsAndHashCode") || annotation.equals("@ToString") || annotation.equals("@Data") || annotation.equals("@Value"))) {
            classCode.append(generateStandardMethods(entity));
        }

        classCode.append("}\n");
        return classCode.toString();
    }

    private static String generateInheritanceAnnotations(Map<String, Object> entity) {
        StringBuilder annotations = new StringBuilder();
        String inheritanceStrategy = (String) entity.get("entity_inheritance_strategy");

        if (inheritanceStrategy.equals("SINGLE_TABLE")) {
            annotations.append("@Entity\n@Table(name=\"").append(entity.get("entity_name")).append("s\")\n@Inheritance(strategy = InheritanceType.SINGLE_TABLE)\n");
        } else if (inheritanceStrategy.equals("JOINED")) {
            annotations.append("@Entity\n@Table(name=\"").append(entity.get("entity_name")).append("s\")\n@Inheritance(strategy = InheritanceType.JOINED)\n");
        } else if (inheritanceStrategy.equals("TABLE_PER_CLASS")) {
            annotations.append("@Entity\n@Table(name=\"").append(entity.get("entity_name")).append("s\")\n@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)\n");
        } else if (inheritanceStrategy.equals("MAPPED_SUPERCLASS") || inheritanceStrategy.equals("MAPPED_SUPER_CLASS")) {
            annotations.append("@MappedSuperclass\n");
        }

        return annotations.toString();
    }

    private static String generateField(Map<String, Object> field, Map<String, Object> configurationVariables, String entityName) {
        String fieldName = (String) field.getOrDefault("field_name", "");
        String fieldType = (String) field.getOrDefault("field_type", "");
        List<String> annotations = (List<String>) field.getOrDefault("field_annotations", new ArrayList<>());

        String idGeneratedValue = (String) configurationVariables.getOrDefault("id_generated_value", "IDENTITY");

        if ((fieldName.equals("id") || fieldName.toLowerCase().equals(entityName.toLowerCase() + "id") || fieldName.toLowerCase().equals(entityName.toLowerCase() + "Id") || fieldName.toLowerCase().equals(entityName.toLowerCase() + "ID") || fieldName.toLowerCase().equals(entityName.toLowerCase() + "iD") || fieldName.toLowerCase().equals(entityName.toLowerCase() + "_id") || fieldName.toLowerCase().equals(entityName.toLowerCase() + "_Id") || fieldName.toLowerCase().equals(entityName.toLowerCase() + "_ID") || fieldName.toLowerCase().equals(entityName.toLowerCase() + "_iD")) && fieldType.isEmpty()) {
            fieldType = "Long";
            if (annotations.isEmpty()) {
                annotations = Arrays.asList("@Id", "@GeneratedValue(strategy = GenerationType." + idGeneratedValue + ")");
            }
        }

        if (fieldType.isEmpty() && annotations.isEmpty()) {
            fieldType = "String";
            annotations = Arrays.asList("@Column(name = \"" + fieldName + "\")");
        }

        StringBuilder fieldCode = new StringBuilder();
        if ((Boolean) configurationVariables.get("jpa_used")) {
            fieldCode.append(addFieldAnnotations(annotations, fieldName, fieldType));
        }

        fieldCode.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n\n");
        return fieldCode.toString();
    }

    private static String addFieldAnnotations(List<String> annotations, String fieldName, String fieldType) {
        StringBuilder result = new StringBuilder();
        String baseType = fieldType.contains("<") ? fieldType.split("<")[1].replace(">", "") : fieldType;

        for (String annotation : annotations) {
            switch (annotation) {
                case "@OneToOneJoinColumn":
                    result.append("    @OneToOne\n    @JoinColumn(name = \"").append(fieldName).append("_id\")\n");
                    break;
                case "@OneToOneMappedBy":
                    result.append("    @OneToOne(mappedBy = \"").append(fieldName).append("\")\n");
                    break;
                case "@OneToManyMappedBy":
                    result.append("    @OneToMany(mappedBy = \"").append(fieldName).append("\")\n");
                    break;
                case "@ManyToOneJoinColumn":
                    result.append("    @ManyToOne\n    @JoinColumn(name = \"").append(fieldName).append("_id\")\n");
                    break;
                case "@ManyToManyJoinTable":
                    String tableName = fieldName + "_" + baseType.toLowerCase();
                    result.append("    @ManyToMany\n    @JoinTable(\n");
                    result.append("        name = \"").append(tableName).append("\",\n");
                    result.append("        joinColumns = @JoinColumn(name = \"").append(fieldName).append("_id\"),\n");
                    result.append("        inverseJoinColumns = @JoinColumn(name = \"").append(baseType.toLowerCase()).append("_id\")\n");
                    result.append("    )\n");
                    break;
                case "@ManyToManyMappedBy":
                    result.append("    @ManyToMany(mappedBy = \"").append(fieldName).append("\")\n");
                    break;
                case "@Enum":
                case "@Enumerated":
                    result.append("    @Enumerated(EnumType.STRING)\n");
                    break;
                case "@Embedded":
                    result.append("    @Embedded\n");
                    break;
                default:
                    result.append("    ").append(annotation).append("\n");
                    break;
            }
        }
        return result.toString();
    }

    private static String generateConstructors(Map<String, Object> entity, Map<String, Object> configurationVariables) {
        StringBuilder constructors = new StringBuilder();
        constructors.append("    // Constructors\n");
        constructors.append("    public ").append(entity.get("entity_name")).append(configurationVariables.get("entity_suffix")).append("() {\n");
        constructors.append("    }\n");
        constructors.append("    public ").append(entity.get("entity_name")).append(configurationVariables.get("entity_suffix")).append("(");
        constructors.append(String.join(", ", ((List<Map<String, Object>>) entity.get("fields")).stream().map(field -> field.get("field_type") + " " + field.get("field_name")).toArray(String[]::new)));
        constructors.append(") {\n");
        for (Map<String, Object> field : (List<Map<String, Object>>) entity.get("fields")) {
            constructors.append("        this.").append(field.get("field_name")).append(" = ").append(field.get("field_name")).append(";\n");
        }
        constructors.append("    }\n");
        return constructors.toString();
    }

    private static String generateGettersSetters(Map<String, Object> entity) {
        StringBuilder gettersSetters = new StringBuilder();
        gettersSetters.append("    // Getters and setters\n");
        for (Map<String, Object> field : (List<Map<String, Object>>) entity.get("fields")) {
            String fieldName = (String) field.get("field_name");
            String fieldType = (String) field.get("field_type");
            String getter = "get" + camelize(fieldName);
            String setter = "set" + camelize(fieldName);

            gettersSetters.append("    public ").append(fieldType).append(" ").append(getter).append("() {\n");
            gettersSetters.append("        return ").append(fieldName).append(";\n");
            gettersSetters.append("    }\n");
            gettersSetters.append("    public void ").append(setter).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
            gettersSetters.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            gettersSetters.append("    }\n");
        }
        return gettersSetters.toString();
    }

    private static String generateStandardMethods(Map<String, Object> entity) {
        StringBuilder methods = new StringBuilder();
        methods.append("    // hashCode(), equals(), toString()\n");
        methods.append("    @Override\n");
        methods.append("    public int hashCode() {\n");
        if (!((List<?>) entity.get("fields")).isEmpty()) {
            methods.append("        return ").append(((List<Map<String, Object>>) entity.get("fields")).get(0).get("field_name")).append(".hashCode();\n");
        } else {
            methods.append("        return super.hashCode();\n");
        }
        methods.append("    }\n");
        methods.append("    @Override\n");
        methods.append("    public boolean equals(Object obj) {\n");
        methods.append("        if (this == obj) return true;\n");
        methods.append("        if (obj == null || getClass() != obj.getClass()) return false;\n");
        methods.append("        ").append(entity.get("entity_name")).append(" that = (").append(entity.get("entity_name")).append(") obj;\n");
        if (!((List<?>) entity.get("fields")).isEmpty()) {
            methods.append("        return ").append(((List<Map<String, Object>>) entity.get("fields")).get(0).get("field_name")).append(".equals(that.").append(((List<Map<String, Object>>) entity.get("fields")).get(0).get("field_name")).append(");\n");
        } else {
            methods.append("        return super.equals(obj);\n");
        }
        methods.append("    }\n");
        methods.append("    @Override\n");
        methods.append("    public String toString() {\n");
        methods.append("        return \"").append(entity.get("entity_name")).append("{\" +\n");
        for (Map<String, Object> field : (List<Map<String, Object>>) entity.get("fields")) {
            methods.append("                \"").append(field.get("field_name")).append("='\" + String.valueOf(").append(field.get("field_name")).append(") + '\\'' +\n");
        }
        methods.append("                '}';\n");
        methods.append("    }\n");
        return methods.toString();
    }

    private static void generateInterfaces(List<Map<String, Object>> interfaceClasses, Map<String, Object> configurationVariables) {
        String outputDir = (String) configurationVariables.getOrDefault("OUT_PUT_DIR", "./generated_classes");
        String _package = (String) configurationVariables.getOrDefault("model_classes__package", "");
        String _packagePath = _package.replace(".", "/");
        String interfaceOutputDir = outputDir + "/" + _packagePath;
        new File(interfaceOutputDir).mkdirs();

        for (Map<String, Object> interfaceClass : interfaceClasses) {
            String interfaceName = (String) interfaceClass.get("interface_name");
            List<String> methods = (List<String>) interfaceClass.get("methods");

            StringBuilder interfaceCode = new StringBuilder();
            interfaceCode.append("_package ").append(_package).append(";\n\n");
            interfaceCode.append("public interface ").append(interfaceName).append(" {\n");
            for (String method : methods) {
                interfaceCode.append("    ").append(method).append(";\n");
            }
            interfaceCode.append("}\n");

            String filePath = interfaceOutputDir + "/" + interfaceName + ".java";
            try {
                Files.write(Paths.get(filePath), interfaceCode.toString().getBytes());
            } catch (IOException e) {
                System.out.println("Error writing interface file: " + e.getMessage());
            }
        }
    }

    private static void generateEmbeddables(List<Map<String, Object>> embeddableClasses, Map<String, Object> configurationVariables) {
        String outputDir = (String) configurationVariables.getOrDefault("OUT_PUT_DIR", "./generated_classes");
        String _package = (String) configurationVariables.getOrDefault("model_classes__package", "");
        String _packagePath = _package.replace(".", "/");
        String embeddableOutputDir = outputDir + "/" + _packagePath;
        new File(embeddableOutputDir).mkdirs();

        for (Map<String, Object> embeddable : embeddableClasses) {
            String embeddableName = (String) embeddable.get("embeddable_name");
            List<Map<String, Object>> fields = (List<Map<String, Object>>) embeddable.get("fields");

            StringBuilder embeddableCode = new StringBuilder();
            embeddableCode.append("_package ").append(_package).append(";\n\n");

            if ((Boolean) configurationVariables.getOrDefault("jpa_used", false)) {
                if ((Boolean) configurationVariables.getOrDefault("jakarta_persistence_api", false)) {
                    embeddableCode.append("import jakarta.persistence.*;\n\n");
                } else {
                    embeddableCode.append("import javax.persistence.*;\n\n");
                }
            }

            embeddableCode.append("@Embeddable\n");
            embeddableCode.append("public class ").append(embeddableName).append(" {\n");

            for (Map<String, Object> field : fields) {
                String fieldName = (String) field.get("field_name");
                String fieldType = (String) field.get("field_type");
                List<String> annotations = (List<String>) field.getOrDefault("field_annotations", new ArrayList<>());

                if ((Boolean) configurationVariables.getOrDefault("jpa_used", true)) {
                    for (String annotation : annotations) {
                        embeddableCode.append("    ").append(annotation).append("\n");
                    }
                }

                embeddableCode.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n\n");
            }

            embeddableCode.append("}\n");

            String filePath = embeddableOutputDir + "/" + embeddableName + ".java";
            try {
                Files.write(Paths.get(filePath), embeddableCode.toString().getBytes());
            } catch (IOException e) {
                System.out.println("Error writing embeddable file: " + e.getMessage());
            }
        }
    }

    private static void generateEnums(List<Map<String, Object>> enumClasses, Map<String, Object> configurationVariables) {
        String outputDir = (String) configurationVariables.getOrDefault("OUT_PUT_DIR", "./generated_classes");
        String _package = (String) configurationVariables.getOrDefault("model_classes__package", "");
        String _packagePath = _package.replace(".", "/");
        String enumOutputDir = outputDir + "/" + _packagePath;
        new File(enumOutputDir).mkdirs();

        for (Map<String, Object> enumClass : enumClasses) {
            String enumName = (String) enumClass.get("enum_name");
            List<String> enumValues = (List<String>) enumClass.get("enum_values");

            StringBuilder enumCode = new StringBuilder();
            enumCode.append("_package ").append(_package).append(";\n\n");
            enumCode.append("public enum ").append(enumName).append(" {\n");

            String privateField = null;
            for (String value : enumValues) {
                if (!(value.contains("(") && value.contains(")"))) {
                    privateField = value;
                    break;
                }
            }

            for (String value : enumValues) {
                if (value.equals(privateField)) {
                    continue;
                }
                if (value.contains("(") && value.contains(")")) {
                    enumCode.append("    ").append(value.split("\\(")[0]).append("(").append(value.split("\\(")[1].replace(")", "")).append("),\n");
                } else {
                    enumCode.append("    ").append(value).append(",\n");
                }
            }
            enumCode = new StringBuilder(enumCode.toString().replaceAll(",\n$", ";\n\n"));

            if (privateField != null) {
                enumCode.append("    private final int ").append(privateField).append(";\n\n");
                enumCode.append("    ").append(enumName).append("(int ").append(privateField).append(") {\n");
                enumCode.append("        this.").append(privateField).append(" = ").append(privateField).append(";\n");
                enumCode.append("    }\n");
            } else if (enumValues.stream().anyMatch(value -> value.contains("(") && value.contains(")"))) {
                enumCode.append("    private final int value;\n\n");
                enumCode.append("    ").append(enumName).append("(int value) {\n");
                enumCode.append("        this.value = value;\n");
                enumCode.append("    }\n");
            }

            enumCode.append("}\n");

            String filePath = enumOutputDir + "/" + enumName + ".java";
            try {
                Files.write(Paths.get(filePath), enumCode.toString().getBytes());
            } catch (IOException e) {
                System.out.println("Error writing enum file: " + e.getMessage());
            }
        }
    }

    private static void generateRepositories(List<Map<String, Object>> entities, Map<String, Object> configurationVariables) {
        String outputDir = (String) configurationVariables.getOrDefault("OUT_PUT_DIR", "./generated_classes");
        String _package = (String) configurationVariables.getOrDefault("repository_classes__packages", "com.example.repository");
        String _packagePath = _package.replace(".", "/");
        String repositoryOutputDir = outputDir + "/" + _packagePath;
        new File(repositoryOutputDir).mkdirs();

        boolean useSpringData = (Boolean) configurationVariables.getOrDefault("spring_data_used_for_repositories_and_services", false);

        for (Map<String, Object> entity : entities) {
            String entityName = (String) entity.get("entity_name");
            String repositoryName = entityName + "Repository";
            String entityClass = entityName + configurationVariables.get("entity_suffix");

            StringBuilder repositoryCode = new StringBuilder();
            repositoryCode.append("_package ").append(_package).append(";\n\n");

            if (useSpringData) {
                repositoryCode.append("import org.springframework.data.jpa.repository.JpaRepository;\n");
                repositoryCode.append("import org.springframework.stereotype.Repository;\n\n");
                repositoryCode.append("@Repository\n");
                repositoryCode.append("public interface ").append(repositoryName).append(" extends JpaRepository<").append(entityClass).append(", Long> {\n");
                repositoryCode.append("}\n");
            } else {
                repositoryCode.append("import jakarta.persistence.EntityManager;\n");
                repositoryCode.append("import jakarta.persistence.PersistenceContext;\n");
                repositoryCode.append("import jakarta.persistence.TypedQuery;\n");
                repositoryCode.append("import java.util.List;\n\n");
                repositoryCode.append("public class ").append(repositoryName).append(" {\n\n");
                repositoryCode.append("    @PersistenceContext\n");
                repositoryCode.append("    private EntityManager entityManager;\n\n");
                repositoryCode.append("    public List<").append(entityClass).append("> findAll() {\n");
                repositoryCode.append("        TypedQuery<").append(entityClass).append("> query = entityManager.createQuery(\"SELECT e FROM ").append(entityClass).append(" e\", ").append(entityClass).append(".class);\n");
                repositoryCode.append("        return query.getResultList();\n");
                repositoryCode.append("    }\n\n");
                repositoryCode.append("    public ").append(entityClass).append(" findById(Long id) {\n");
                repositoryCode.append("        return entityManager.find(").append(entityClass).append(".class, id);\n");
                repositoryCode.append("    }\n\n");
                repositoryCode.append("    public void save(").append(entityClass).append(" entity) {\n");
                repositoryCode.append("        entityManager.persist(entity);\n");
                repositoryCode.append("    }\n\n");
                repositoryCode.append("    public void update(").append(entityClass).append(" entity) {\n");
                repositoryCode.append("        entityManager.merge(entity);\n");
                repositoryCode.append("    }\n\n");
                repositoryCode.append("    public void delete(Long id) {\n");
                repositoryCode.append("        ").append(entityClass).append(" entity = findById(id);\n");
                repositoryCode.append("        if (entity != null) {\n");
                repositoryCode.append("            entityManager.remove(entity);\n");
                repositoryCode.append("        }\n");
                repositoryCode.append("    }\n");
                repositoryCode.append("}\n");
            }

            String filePath = repositoryOutputDir + "/" + repositoryName + ".java";
            try {
                Files.write(Paths.get(filePath), repositoryCode.toString().getBytes());
            } catch (IOException e) {
                System.out.println("Error writing repository file: " + e.getMessage());
            }
        }

        System.out.println("\t📚 " + entities.size() + " repositories generated successfully.");
    }

    private static void generateServices(List<Map<String, Object>> entities, Map<String, Object> configurationVariables) {
        String outputDir = (String) configurationVariables.getOrDefault("OUT_PUT_DIR", "./generated_classes");
        String _package = (String) configurationVariables.getOrDefault("service_classes__packages", "com.example.service");
        String _packagePath = _package.replace(".", "/");
        String serviceOutputDir = outputDir + "/" + _packagePath;
        new File(serviceOutputDir).mkdirs();

        boolean useSpringData = (Boolean) configurationVariables.getOrDefault("spring_data_used_for_repositories_and_services", false);

        for (Map<String, Object> entity : entities) {
            String entityName = (String) entity.get("entity_name");
            String serviceName = entityName + "Service";
            String repositoryName = entityName + "Repository";
            String repository_package = (String) configurationVariables.getOrDefault("repository_classes__packages", "com.example.repository");

            StringBuilder serviceCode = new StringBuilder();
            serviceCode.append("_package ").append(_package).append(";\n\n");
            serviceCode.append("import org.springframework.beans.factory.annotation.Autowired;\n");
            serviceCode.append("import org.springframework.stereotype.Service;\n");
            serviceCode.append("import ").append(repository_package).append(".").append(repositoryName).append(";\n\n");
            serviceCode.append("@Service\n");
            serviceCode.append("public class ").append(serviceName).append(" {\n\n");
            serviceCode.append("    @Autowired\n");
            serviceCode.append("    private ").append(repositoryName).append(" ").append(repositoryName.substring(0, 1).toLowerCase()).append(repositoryName.substring(1)).append(";\n\n");

            if (useSpringData) {
                serviceCode.append("    // Example method using Spring Data JPA\n");
                serviceCode.append("    public List<").append(entityName).append("> findAll() {\n");
                serviceCode.append("        return ").append(repositoryName.substring(0, 1).toLowerCase()).append(repositoryName.substring(1)).append(".findAll();\n");
                serviceCode.append("    }\n\n");
            } else {
                serviceCode.append("    // Example method using pure JPA\n");
                serviceCode.append("    public List<").append(entityName).append("> findAll() {\n");
                serviceCode.append("        return ").append(repositoryName.substring(0, 1).toLowerCase()).append(repositoryName.substring(1)).append(".findAll();\n");
                serviceCode.append("    }\n\n");
            }

            serviceCode.append("}\n");

            String filePath = serviceOutputDir + "/" + serviceName + ".java";
            try {
                Files.write(Paths.get(filePath), serviceCode.toString().getBytes());
            } catch (IOException e) {
                System.out.println("Error writing service file: " + e.getMessage());
            }
        }

        System.out.println("\t🛠️ " + entities.size() + " services generated successfully.");
    }

    private static void generateControllers(List<Map<String, Object>> entities, Map<String, Object> configurationVariables) {
        String outputDir = (String) configurationVariables.getOrDefault("OUT_PUT_DIR", "./generated_classes");
        String _package = (String) configurationVariables.getOrDefault("controller_classes__packages", "com.example.controller");
        String _packagePath = _package.replace(".", "/");
        String controllerOutputDir = outputDir + "/" + _packagePath;
        new File(controllerOutputDir).mkdirs();

        for (Map<String, Object> entity : entities) {
            String entityName = (String) entity.get("entity_name");
            String controllerName = entityName + "Controller";
            String serviceName = entityName + "Service";
            String service_package = (String) configurationVariables.getOrDefault("service_classes__packages", "com.example.service");

            StringBuilder controllerCode = new StringBuilder();
            controllerCode.append("_package ").append(_package).append(";\n\n");
            controllerCode.append("import org.springframework.beans.factory.annotation.Autowired;\n");
            controllerCode.append("import org.springframework.web.bind.annotation.*;\n");
            controllerCode.append("import ").append(service_package).append(".").append(serviceName).append(";\n\n");
            controllerCode.append("@RestController\n");
            controllerCode.append("@RequestMapping(\"/api/").append(entityName.toLowerCase()).append("s\")\n");
            controllerCode.append("public class ").append(controllerName).append(" {\n\n");
            controllerCode.append("    @Autowired\n");
            controllerCode.append("    private ").append(serviceName).append(" ").append(serviceName.substring(0, 1).toLowerCase()).append(serviceName.substring(1)).append(";\n\n");
            controllerCode.append("    // Example REST endpoint\n");
            controllerCode.append("    @GetMapping\n");
            controllerCode.append("    public List<").append(entityName).append("> findAll() {\n");
            controllerCode.append("        return ").append(serviceName.substring(0, 1).toLowerCase()).append(serviceName.substring(1)).append(".findAll();\n");
            controllerCode.append("    }\n\n");
            controllerCode.append("}\n");

            String filePath = controllerOutputDir + "/" + controllerName + ".java";
            try {
                Files.write(Paths.get(filePath), controllerCode.toString().getBytes());
            } catch (IOException e) {
                System.out.println("Error writing controller file: " + e.getMessage());
            }
        }

        System.out.println("\t🎮 " + entities.size() + " controllers generated successfully.");
    }

    private static void generateConfigurationFiles(Map<String, Object> configurationVariables) {
        String outputDir = (String) configurationVariables.getOrDefault("OUT_PUT_DIR", "./generated_classes");
        String configDir = outputDir + "/src/main/resources";
        String configJavaDir = outputDir + "/src/main/java/com/example/config";
        new File(configDir).mkdirs();
        new File(configJavaDir).mkdirs();

        try {
            Files.write(Paths.get(configDir + "/application-dev.properties"), (
                    "# Database configuration (H2 for development)\n" +
                    "spring.datasource.url=jdbc:h2:mem:testdb\n" +
                    "spring.datasource.driver-class-name=org.h2.Driver\n" +
                    "spring.datasource.username=sa\n" +
                    "spring.datasource.password=password\n" +
                    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect\n\n" +
                    "# Hibernate configuration\n" +
                    "spring.jpa.hibernate.ddl-auto=update\n" +
                    "spring.jpa.show-sql=true\n\n" +
                    "# Enable H2 console\n" +
                    "spring.h2.console.enabled=true\n" +
                    "spring.h2.console.path=/h2-console\n\n" +
                    "# Server configuration\n" +
                    "server.port=8080\n\n" +
                    "# Logging\n" +
                    "logging.level.org.springframework=DEBUG\n" +
                    "logging.level.com.example=DEBUG\n"
            ).getBytes());

            Files.write(Paths.get(configDir + "/application-prod.properties"), (
                    "# Database configuration (MySQL for production)\n" +
                    "spring.datasource.url=jdbc:mysql://prod-db:3306/mydatabase\n" +
                    "spring.datasource.username=produser\n" +
                    "spring.datasource.password=prodpassword\n" +
                    "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\n\n" +
                    "# Hibernate configuration\n" +
                    "spring.jpa.hibernate.ddl-auto=validate\n" +
                    "spring.jpa.show-sql=false\n" +
                    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect\n\n" +
                    "# Server configuration\n" +
                    "server.port=8080\n\n" +
                    "# Logging\n" +
                    "logging.level.org.springframework=INFO\n" +
                    "logging.level.com.example=INFO\n\n" +
                    "# Disable H2 console\n" +
                    "spring.h2.console.enabled=false\n"
            ).getBytes());

            Files.write(Paths.get(configDir + "/logback-spring.xml"), (
                    "<configuration>\n" +
                    "    <!-- Console appender -->\n" +
                    "    <appender name=\"STDOUT\" class=\"ch.qos.logback.core.ConsoleAppender\">\n" +
                    "        <encoder>\n" +
                    "            <pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n</pattern>\n" +
                    "        </encoder>\n" +
                    "    </appender>\n\n" +
                    "    <!-- Application logger -->\n" +
                    "    <logger name=\"com.example\" level=\"DEBUG\" />\n\n" +
                    "    <!-- Spring logger -->\n" +
                    "    <logger name=\"org.springframework\" level=\"INFO\" />\n\n" +
                    "    <!-- Root logger -->\n" +
                    "    <root level=\"INFO\">\n" +
                    "        <appender-ref ref=\"STDOUT\" />\n" +
                    "    </root>\n" +
                    "</configuration>\n"
            ).getBytes());

            Files.write(Paths.get(configJavaDir + "/SwaggerConfig.java"), (
                    "_package com.example.config;\n\n" +
                    "import org.springframework.context.annotation.Bean;\n" +
                    "import org.springframework.context.annotation.Configuration;\n" +
                    "import springfox.documentation.builders.PathSelectors;\n" +
                    "import springfox.documentation.builders.RequestHandlerSelectors;\n" +
                    "import springfox.documentation.spi.DocumentationType;\n" +
                    "import springfox.documentation.spring.web.plugins.Docket;\n" +
                    "import springfox.documentation.swagger2.annotations.EnableSwagger2;\n\n" +
                    "@Configuration\n" +
                    "@EnableSwagger2\n" +
                    "public class SwaggerConfig {\n\n" +
                    "    @Bean\n" +
                    "    public Docket api() {\n" +
                    "        return new Docket(DocumentationType.SWAGGER_2)\n" +
                    "            .select()\n" +
                    "            .apis(RequestHandlerSelectors.base_package(\"com.example.controller\"))\n" +
                    "            .paths(PathSelectors.any())\n" +
                    "            .build();\n" +
                    "    }\n" +
                    "}\n"
            ).getBytes());

            Files.write(Paths.get(configJavaDir + "/SecurityConfig.java"), (
                    "_package com.example.config;\n\n" +
                    "import org.springframework.context.annotation.Bean;\n" +
                    "import org.springframework.context.annotation.Configuration;\n" +
                    "import org.springframework.security.config.annotation.web.builders.HttpSecurity;\n" +
                    "import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;\n" +
                    "import org.springframework.security.web.SecurityFilterChain;\n\n" +
                    "@Configuration\n" +
                    "@EnableWebSecurity\n" +
                    "public class SecurityConfig {\n\n" +
                    "    @Bean\n" +
                    "    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {\n" +
                    "        http\n" +
                    "            .csrf().disable() // Disable CSRF for REST APIs\n" +
                    "            .authorizeHttpRequests()\n" +
                    "                .requestMatchers(\"/api/public/**\").permitAll() // Allow public access to certain routes\n" +
                    "                .anyRequest().authenticated() // All other routes require authentication\n" +
                    "            .and()\n" +
                    "            .httpBasic(); // Use basic authentication (username/password)\n" +
                    "        return http.build();\n" +
                    "    }\n" +
                    "}\n"
            ).getBytes());

            Files.write(Paths.get(configDir + "/application.yml"), (
                    "spring:\n" +
                    "  datasource:\n" +
                    "    url: jdbc:mysql://localhost:3306/mydatabase\n" +
                    "    username: root\n" +
                    "    password: password\n" +
                    "    driver-class-name: com.mysql.cj.jdbc.Driver\n" +
                    "  jpa:\n" +
                    "    hibernate:\n" +
                    "      ddl-auto: update\n" +
                    "    show-sql: true\n" +
                    "    properties:\n" +
                    "      hibernate:\n" +
                    "        dialect: org.hibernate.dialect.MySQL8Dialect\n" +
                    "server:\n" +
                    "  port: 8080\n" +
                    "logging:\n" +
                    "  level:\n" +
                    "    org.springframework: INFO\n" +
                    "    com.example: DEBUG\n"
            ).getBytes());

            Files.write(Paths.get(configDir + "/application.properties"), (
                    "# Database configuration\n" +
                    "spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase\n" +
                    "spring.datasource.username=root\n" +
                    "spring.datasource.password=password\n" +
                    "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver\n\n" +
                    "# Hibernate configuration\n" +
                    "spring.jpa.hibernate.ddl-auto=update\n" +
                    "spring.jpa.show-sql=true\n" +
                    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect\n\n" +
                    "# Server configuration\n" +
                    "server.port=8080\n\n" +
                    "# Logging\n" +
                    "logging.level.org.springframework=INFO\n" +
                    "logging.level.com.example=DEBUG\n"
            ).getBytes());

            System.out.println("\t✅ Configuration files generated successfully in " + outputDir + ".");
        } catch (IOException e) {
            System.out.println("Error writing configuration files: " + e.getMessage());
        }
    }

    private static boolean askDisplay() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nDo you want to display in the console, here, all the model classes created \nand generated in the dedicated directory? (Yes/No): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }

    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(padding);
    }

    private static String camelize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
