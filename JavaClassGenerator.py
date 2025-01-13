#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Script Name: JavaClassGenerator.py
Description: This script generates Java classes, interfaces, enums, and other related files based on a JSON configuration file.
             It supports JPA annotations, Lombok, and Spring Data JPA for generating repositories and services.
             The script also creates configuration files for Spring Boot applications.

Author: Junior ADI <@caifyoca>
Version: 1.0
Date: 2025-01-13
License: GPLv3 License

Usage:
    - Ensure Python 3.x is installed.
    - Install required dependencies: `pip install inflection`.
    - Run the script: `python JavaClassGenerator.py`.
    - Provide the path to the JSON configuration file when prompted.

Dependencies:
    - Python 3.x
    - inflection (for string manipulation)
"""
import json
import os
from inflection import camelize

############################# UTILITY FUNCTIONS #############################

def read_json_file(file_path):
    """
    Reads a JSON file and returns the data.

    Args:
        file_path (str): Path to the JSON file.

    Returns:
        dict or None: The JSON data if the file is valid, otherwise None.
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            data = json.load(file)
        return data
    except FileNotFoundError:
        print(f"Error: The file {file_path} does not exist.")
        return None
    except json.JSONDecodeError:
        print(f"Error: The file {file_path} does not contain valid JSON data.")
        return None
    except Exception as e:
        print(f"Unexpected error while reading the file {file_path}: {e}")
        return None


def verify_data(data=None, file_path='data.json'):
    """
    Verifies the data and loads the JSON file if necessary.

    Args:
        data (dict, optional): The data to verify. Defaults to None.
        file_path (str, optional): Path to the JSON file to load if data is not provided. Defaults to 'data.json'.

    Returns:
        dict or None: The verified data if valid, otherwise None.
    """
    # Check if the provided data is valid
    if data is not None and isinstance(data, dict) and "entities" in data and "configuration_variables" in data:
        print("Data loaded from the script.")
        return data

    # If the data is not valid, try to load it from a file
    if os.path.exists(file_path):
        data = read_json_file(file_path)
        if data is not None and isinstance(data, dict) and "entities" in data and "configuration_variables" in data:
            print(f"Data loaded from the file {file_path}.")
            return data
        else:
            print(f"Error: The data loaded from the file {file_path} is not valid.")
    else:
        print(f"Error: The file {file_path} does not exist.")

    return None


############################# MAIN FUNCTION #############################

def generateClasses(entities, configuration_variables):
    generated_classes = []

    # Create the project root directory if OUT_PUT_DIR is not null, otherwise use a default directory
    output_dir = configuration_variables.get("OUT_PUT_DIR", "./generated_classes")
    os.makedirs(output_dir, exist_ok=True)

    for entity in entities:
        try:
            validate_entity(entity)
            class_code = generate_class(entity, configuration_variables, entities)
            generated_classes.append(class_code)

            # Generate classes in specific directories if necessary
            if configuration_variables.get("generate_classes_following_packages"):
                package = configuration_variables.get("model_classes_package", "")
                package_path = package.replace(".", "/")
                class_output_dir = os.path.join(output_dir, package_path)
                os.makedirs(class_output_dir, exist_ok=True)
                file_path = os.path.join(class_output_dir, f"{entity['entity_name']}{configuration_variables['entity_suffix']}.java")
                with open(file_path, "w") as file:
                    file.write(class_code)
        except ValueError as e:
            print(f"Error while generating the class {entity.get('entity_name', 'Unknown')}: {e}")

    return generated_classes


############################# VALIDATION AND GENERATION FUNCTIONS #############################

def validate_entity(entity):
    required_fields = ["entity_name", "fields"]
    for field in required_fields:
        if field not in entity:
            raise ValueError(f"Missing required field: {field}")


def generate_class(entity, configuration_variables, entities):
    class_code = ""

    # Add the package if model_classes_package is not null
    if configuration_variables.get("model_classes_package"):
        class_code += f"package {configuration_variables['model_classes_package']};\n\n"

    # Add JPA imports if jpa_used is True
    if configuration_variables["jpa_used"]:
        if configuration_variables.get("jarkata_persistence_api"):
            class_code += "import jakarta.persistence.*;\n\n"
        else:
            class_code += "import javax.persistence.*;\n\n"

    # Add Serializable import if necessary
    if configuration_variables["jpa_used"] or (
        not configuration_variables["jpa_used"] and configuration_variables["pojo_model_beanified"]
    ):
        class_code += "import java.io.Serializable;\n\n"

    # Add Lombok imports if Lombok annotations are present
    lombok_annotations = ["@NoArgsConstructor", "@RequiredArgsConstructor", "@AllArgsConstructor", "@Getter", "@Setter", 
                          "@ToString", "@EqualsAndHashCode", "@Data", "@Value", "@Builder", "@With", "@NonNull", 
                          "@SneakyThrows", "@Synchronized"]
    present_annotations = set()

    # Check Lombok annotations in class annotations
    if "entity_supplementary_annotations" in entity:
        for annotation in entity["entity_supplementary_annotations"]:
            if annotation in lombok_annotations:
                present_annotations.add(annotation)

    # Check Lombok annotations in field annotations
    for field in entity["fields"]:
        if "field_annotations" in field:
            for annotation in field["field_annotations"]:
                if annotation in lombok_annotations:
                    present_annotations.add(annotation)

    # Add Lombok imports if Lombok annotations are present
    if present_annotations:
        class_code += "import lombok.*;\n\n"

    # Add class Lombok annotations
    if "entity_supplementary_annotations" in entity:
        for annotation in entity["entity_supplementary_annotations"]:
            if annotation in lombok_annotations:
                class_code += f"{annotation}\n"

    # Determine the class type (parent, derived, or normal)
    if entity["entity_is_parent"]:
        # Parent class
        if configuration_variables["jpa_used"]:
            class_code += generate_inheritance_annotations(entity)
        class_code += f"public abstract class {entity['entity_name']}{configuration_variables['entity_suffix']}"
    elif entity["entity_parent_name"]:
        # Derived class
        try:
            parent_entity = next(e for e in entities if e["entity_name"] == entity["entity_parent_name"])
            parent_strategy = parent_entity["entity_inheritance_strategy"]

            if configuration_variables["jpa_used"]:
                if parent_strategy == "SINGLE_TABLE":
                    class_code += f"@Entity\n@DiscriminatorValue(\"{entity['discriminator_value']}\")\n"
                elif parent_strategy == "JOINED":
                    class_code += f"@Entity\n@Table(name=\"{entity['entity_name']}s\")\n"
                elif parent_strategy == "TABLE_PER_CLASS":
                    class_code += f"@Entity\n@Table(name=\"{entity['entity_name']}s\")\n"
                elif parent_strategy in ["MAPPED_SUPERCLASS", "MAPPED_SUPER_CLASS"]:
                    class_code += f"@Entity\n@Table(name=\"{entity['entity_name']}s\")\n"

            class_code += f"public class {entity['entity_name']}{configuration_variables['entity_suffix']} extends {entity['entity_parent_name']}{configuration_variables['entity_suffix']}"
        except StopIteration:
            raise ValueError(f"Parent entity '{entity['entity_parent_name']}' not found for entity '{entity['entity_name']}'")
    else:
        # Normal class
        if configuration_variables["jpa_used"]:
            class_code += f"@Entity\n@Table(name=\"{entity['entity_name']}s\")\n"
        class_code += f"public class {entity['entity_name']}{configuration_variables['entity_suffix']}"

    # Implemented interfaces
    if entity["interfaces_implemented"] or configuration_variables["jpa_used"] or (
        not configuration_variables["jpa_used"] and configuration_variables["pojo_model_beanified"]
    ):
        class_code += " implements"
        if configuration_variables["jpa_used"] or (
            not configuration_variables["jpa_used"] and configuration_variables["pojo_model_beanified"]
        ):
            class_code += " Serializable"
            if entity["interfaces_implemented"]:
                class_code += ", " + ", ".join(entity["interfaces_implemented"])
        else:
            class_code += " " + ", ".join(entity["interfaces_implemented"])
    class_code += " {\n"

    # Class fields
    for field in entity["fields"]:
        class_code += generate_field(field, configuration_variables, entity["entity_name"])

    # Constructors (except if @NoArgsConstructor, @RequiredArgsConstructor, or @AllArgsConstructor is present)
    if configuration_variables["add_models_no_and_all_args_constructors"] and not any(
        annotation in present_annotations for annotation in ["@NoArgsConstructor", "@RequiredArgsConstructor", "@AllArgsConstructor"]
    ):
        class_code += generate_constructors(entity, configuration_variables)

    # Getters and setters (except if @Getter or @Setter is present)
    if configuration_variables["add_model_class_getters_setters"] and not any(
        annotation in present_annotations for annotation in ["@Getter", "@Setter", "@Data", "@Value"]
    ):
        class_code += generate_getters_setters(entity)

    # hashCode, equals, toString (except if @EqualsAndHashCode or @ToString is present)
    if configuration_variables["add_model_class_hashcode_equals_tostring_methods"] and not any(
        annotation in present_annotations for annotation in ["@EqualsAndHashCode", "@ToString", "@Data", "@Value"]
    ):
        class_code += generate_standard_methods(entity)

    class_code += "}\n"
    return class_code


def generate_inheritance_annotations(entity):
    annotations = ""
    if entity["entity_inheritance_strategy"] == "SINGLE_TABLE":
        annotations += f"@Entity\n@Table(name=\"{entity['entity_name']}s\")\n@Inheritance(strategy = InheritanceType.SINGLE_TABLE)\n"
    elif entity["entity_inheritance_strategy"] == "JOINED":
        annotations += f"@Entity\n@Table(name=\"{entity['entity_name']}s\")\n@Inheritance(strategy = InheritanceType.JOINED)\n"
    elif entity["entity_inheritance_strategy"] == "TABLE_PER_CLASS":
        annotations += f"@Entity\n@Table(name=\"{entity['entity_name']}s\")\n@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)\n"
    elif entity["entity_inheritance_strategy"] in ["MAPPED_SUPERCLASS", "MAPPED_SUPER_CLASS"]:
        annotations += "@MappedSuperclass\n"
    return annotations


def generate_field(field, configuration_variables, entity_name):
    """
    Generates the code for a class field based on its properties.

    Args:
        field (dict): Dictionary containing the field information.
        configuration_variables (dict): Configuration variables.
        entity_name (str): Entity name.

    Returns:
        str: Java code for the field.
    """
    field_name = field.get("field_name", "")
    field_type = field.get("field_type", "")
    annotations = field.get("field_annotations", [])

    # Retrieve the ID generation strategy from configuration variables
    id_generated_value = configuration_variables.get("id_generated_value", "IDENTITY")

    # Automatically detect ID fields
    if (field_name == "id" or field_name.lower() == f"{entity_name.lower()}id" or
        field_name.lower() == f"{entity_name.lower()}Id" or field_name.lower() == f"{entity_name.lower()}ID" or
        field_name.lower() == f"{entity_name.lower()}iD" or field_name.lower() == f"{entity_name.lower()}_id" or
        field_name.lower() == f"{entity_name.lower()}_Id" or field_name.lower() == f"{entity_name.lower()}_ID" or
        field_name.lower() == f"{entity_name.lower()}_iD") and not field_type:
        field_type = "Long"
        if not annotations:
            annotations = [f"@Id", f"@GeneratedValue(strategy = GenerationType.{id_generated_value})"]

    # Set a default type for other fields without type or annotation
    if not field_type and not annotations:
        field_type = "String"
        annotations = [f"@Column(name = \"{field_name}\")"]

    # Generate annotations
    field_code = ""
    if configuration_variables["jpa_used"]:
        field_code += add_field_annotations(annotations, field_name, field_type)

    # Add field declaration
    field_code += f"    private {field_type} {field_name};\n\n"
    return field_code


def add_field_annotations(annotations, field_name, field_type):
    result = ""
    # Extract the base type for generic fields (e.g., Set<Message> -> Message)
    if "<" in field_type:
        base_type = field_type.split("<")[1].rstrip(">")
    else:
        base_type = field_type

    for annotation in annotations:
        if annotation == "@OneToOneJoinColumn":
            result += f"    @OneToOne\n    @JoinColumn(name = \"{field_name}_id\")\n"
        elif annotation == "@OneToOneMappedBy":
            result += f"    @OneToOne(mappedBy = \"{field_name}\")\n"
        elif annotation == "@OneToManyMappedBy":
            result += f"    @OneToMany(mappedBy = \"{field_name}\")\n"
        elif annotation == "@ManyToOneJoinColumn":
            result += f"    @ManyToOne\n    @JoinColumn(name = \"{field_name}_id\")\n"
        elif annotation == "@ManyToManyJoinTable":
            table_name = f"{field_name}_{base_type.lower()}"
            result += f"    @ManyToMany\n    @JoinTable(\n"
            result += f"        name = \"{table_name}\",\n"
            result += f"        joinColumns = @JoinColumn(name = \"{field_name}_id\"),\n"
            result += f"        inverseJoinColumns = @JoinColumn(name = \"{base_type.lower()}_id\")\n"
            result += "    )\n"
        elif annotation == "@ManyToManyMappedBy":
            result += f"    @ManyToMany(mappedBy = \"{field_name}\")\n"
        elif annotation == "@Enum" or annotation == "@Enumerated":
            result += "    @Enumerated(EnumType.STRING)\n"
        elif annotation == "@Embedded":
            result += "    @Embedded\n"
        else:
            result += f"    {annotation}\n"
    return result


def generate_constructors(entity, configuration_variables):
    constructors = "    // Constructors\n"
    constructors += f"    public {entity['entity_name']}{configuration_variables['entity_suffix']}() {{\n"
    constructors += "    }\n"
    constructors += f"    public {entity['entity_name']}{configuration_variables['entity_suffix']}("
    constructors += ", ".join([f"{field['field_type']} {field['field_name']}" for field in entity["fields"]])
    constructors += ") {\n"
    for field in entity["fields"]:
        constructors += f"        this.{field['field_name']} = {field['field_name']};\n"
    constructors += "    }\n"
    return constructors


def generate_getters_setters(entity):
    getters_setters = "    // Getters and setters\n"
    for field in entity["fields"]:
        getter = f"get{camelize(field['field_name'])}"
        setter = f"set{camelize(field['field_name'])}"
        getters_setters += f"    public {field['field_type']} {getter}() {{\n"
        getters_setters += f"        return {field['field_name']};\n"
        getters_setters += "    }\n"
        getters_setters += f"    public void {setter}({field['field_type']} {field['field_name']}) {{\n"
        getters_setters += f"        this.{field['field_name']} = {field['field_name']};\n"
        getters_setters += "    }\n"
    return getters_setters


def generate_standard_methods(entity):
    methods = "    // hashCode(), equals(), toString()\n"
    methods += "    @Override\n"
    methods += "    public int hashCode() {\n"
    if entity["fields"]:
        methods += f"        return {entity['fields'][0]['field_name']}.hashCode();\n"
    else:
        methods += "        return super.hashCode();\n"
    methods += "    }\n"
    methods += "    @Override\n"
    methods += "    public boolean equals(Object obj) {\n"
    methods += "        if (this == obj) return true;\n"
    methods += "        if (obj == null || getClass() != obj.getClass()) return false;\n"
    methods += f"        {entity['entity_name']} that = ({entity['entity_name']}) obj;\n"
    if entity["fields"]:
        methods += f"        return {entity['fields'][0]['field_name']}.equals(that.{entity['fields'][0]['field_name']});\n"
    else:
        methods += "        return super.equals(obj);\n"
    methods += "    }\n"
    methods += "    @Override\n"
    methods += "    public String toString() {\n"
    methods += f"        return \"{entity['entity_name']}{{\" +\n"
    for field in entity["fields"]:
        methods += f"                \"{field['field_name']}='\" + String.valueOf({field['field_name']}) + '\\'' +\n"
    methods += "                '}';\n"
    methods += "    }\n"
    return methods


def generate_interfaces(interface_classes, configuration_variables):
    output_dir = configuration_variables.get("OUT_PUT_DIR", "./generated_classes")
    package = configuration_variables.get("model_classes_package", "")
    package_path = package.replace(".", "/")
    interface_output_dir = os.path.join(output_dir, package_path)
    os.makedirs(interface_output_dir, exist_ok=True)

    for interface in interface_classes:
        interface_name = interface["interface_name"]
        methods = interface["methods"]

        interface_code = f"package {package};\n\n"
        interface_code += f"public interface {interface_name} {{\n"
        for method in methods:
            interface_code += f"    {method};\n"
        interface_code += "}\n"

        file_path = os.path.join(interface_output_dir, f"{interface_name}.java")
        with open(file_path, "w") as file:
            file.write(interface_code)


def generate_embeddables(embeddable_classes, configuration_variables):
    output_dir = configuration_variables.get("OUT_PUT_DIR", "./generated_classes")
    package = configuration_variables.get("model_classes_package", "")
    package_path = package.replace(".", "/")
    embeddable_output_dir = os.path.join(output_dir, package_path)
    os.makedirs(embeddable_output_dir, exist_ok=True)

    for embeddable in embeddable_classes:
        embeddable_name = embeddable["embeddable_name"]
        fields = embeddable["fields"]

        embeddable_code = f"package {package};\n\n"

        # Handle JPA imports
        if configuration_variables.get("jpa_used", False):
            if configuration_variables.get("jakarta_persistence_api", False):
                embeddable_code += "import jakarta.persistence.*;\n\n"
            else:
                embeddable_code += "import javax.persistence.*;\n\n"

        embeddable_code += "@Embeddable\n"
        embeddable_code += f"public class {embeddable_name} {{\n"

        for field in fields:
            field_name = field["field_name"]
            field_type = field["field_type"]
            annotations = field.get("field_annotations", [])

            # Add annotations only if JPA is enabled
            if configuration_variables.get("jpa_used", True):
                for annotation in annotations:
                    embeddable_code += f"    {annotation}\n"

            # Add field declaration
            embeddable_code += f"    private {field_type} {field_name};\n\n"

        embeddable_code += "}\n"

        file_path = os.path.join(embeddable_output_dir, f"{embeddable_name}.java")
        with open(file_path, "w") as file:
            file.write(embeddable_code)

def generate_enums(enum_classes, configuration_variables):
    output_dir = configuration_variables.get("OUT_PUT_DIR", "./generated_classes")
    package = configuration_variables.get("model_classes_package", "")
    package_path = package.replace(".", "/")
    enum_output_dir = os.path.join(output_dir, package_path)
    os.makedirs(enum_output_dir, exist_ok=True)

    for enum in enum_classes:
        enum_name = enum["enum_name"]
        enum_values = enum["enum_values"]

        enum_code = f"package {package};\n\n"
        enum_code += f"public enum {enum_name} {{\n"

        # Detect if an entry is different (private field)
        private_field = None
        for value in enum_values:
            if not ("(" in value and ")" in value):
                private_field = value  # This entry is the private field name
                break

        # Generate enum values
        for value in enum_values:
            if value == private_field:
                continue  # Ignore this entry, it's the private field
            if "(" in value and ")" in value:
                enum_code += f"    {value.split('(')[0]}({value.split('(')[1].rstrip(')')}),\n"
            else:
                enum_code += f"    {value},\n"
        enum_code = enum_code.rstrip(",\n") + ";\n\n"

        # Add private field and constructor if necessary
        if private_field:
            enum_code += f"    private final int {private_field};\n\n"
            enum_code += f"    {enum_name}(int {private_field}) {{\n"
            enum_code += f"        this.{private_field} = {private_field};\n"
            enum_code += "    }\n"
        elif any("(" in value and ")" in value for value in enum_values):
            # Case where all entries are of the form CONSTANT_NAME(VALUE)
            enum_code += "    private final int value;\n\n"
            enum_code += f"    {enum_name}(int value) {{\n"
            enum_code += "        this.value = value;\n"
            enum_code += "    }\n"

        enum_code += "}\n"

        file_path = os.path.join(enum_output_dir, f"{enum_name}.java")
        with open(file_path, "w") as file:
            file.write(enum_code)


############################# SERVICE, CONTROLLER, REPOSITORY CLASSES #############################


def generate_controllers(entities, configuration_variables):
    output_dir = configuration_variables.get("OUT_PUT_DIR", "./generated_classes")
    package = configuration_variables.get("controller_classes_packages", "com.example.controller")
    package_path = package.replace(".", "/")
    controller_output_dir = os.path.join(output_dir, package_path)
    os.makedirs(controller_output_dir, exist_ok=True)

    for entity in entities:
        entity_name = entity["entity_name"]
        controller_name = f"{entity_name}Controller"
        service_name = f"{entity_name}Service"
        service_package = configuration_variables.get("service_classes_packages", "com.example.service")

        controller_code = f"package {package};\n\n"
        controller_code += "import org.springframework.beans.factory.annotation.Autowired;\n"
        controller_code += "import org.springframework.web.bind.annotation.*;\n"
        controller_code += f"import {service_package}.{service_name};\n\n"
        controller_code += "@RestController\n"
        controller_code += f"@RequestMapping(\"/api/{entity_name.lower()}s\")\n"
        controller_code += f"public class {controller_name} {{\n\n"
        controller_code += f"    @Autowired\n"
        controller_code += f"    private {service_name} {service_name[0].lower() + service_name[1:]};\n\n"
        controller_code += "    // Example REST endpoint\n"
        controller_code += "    @GetMapping\n"
        controller_code += "    public List<Utilisateur> findAll() {\n"
        controller_code += "        return utilisateurService.findAll();\n"
        controller_code += "    }\n\n"
        controller_code += "}\n"

        file_path = os.path.join(controller_output_dir, f"{controller_name}.java")
        with open(file_path, "w") as file:
            file.write(controller_code)

    print(f"\t🎮 {len(entities)} controllers generated successfully.")


def generate_repositories(entities, configuration_variables):
    output_dir = configuration_variables.get("OUT_PUT_DIR", "./generated_classes")
    package = configuration_variables.get("repository_classes_packages", "com.example.repository")
    package_path = package.replace(".", "/")
    repository_output_dir = os.path.join(output_dir, package_path)
    os.makedirs(repository_output_dir, exist_ok=True)

    use_spring_data = configuration_variables.get("spring_data_used_for_repositories_and_services", False)

    for entity in entities:
        entity_name = entity["entity_name"]
        repository_name = f"{entity_name}Repository"
        entity_class = f"{entity_name}{configuration_variables['entity_suffix']}"

        repository_code = f"package {package};\n\n"

        if use_spring_data:
            # Generate a Spring Data JPA repository
            repository_code += "import org.springframework.data.jpa.repository.JpaRepository;\n"
            repository_code += "import org.springframework.stereotype.Repository;\n\n"
            repository_code += f"@Repository\n"
            repository_code += f"public interface {repository_name} extends JpaRepository<{entity_class}, Long> {{\n"
            repository_code += "}\n"
        else:
            # Generate a pure JPA repository (EclipseLink)
            repository_code += "import jakarta.persistence.EntityManager;\n"
            repository_code += "import jakarta.persistence.PersistenceContext;\n"
            repository_code += "import jakarta.persistence.TypedQuery;\n"
            repository_code += "import java.util.List;\n\n"
            repository_code += f"public class {repository_name} {{\n\n"
            repository_code += "    @PersistenceContext\n"
            repository_code += "    private EntityManager entityManager;\n\n"
            repository_code += f"    public List<{entity_class}> findAll() {{\n"
            repository_code += f"        TypedQuery<{entity_class}> query = entityManager.createQuery(\"SELECT e FROM {entity_class} e\", {entity_class}.class);\n"
            repository_code += "        return query.getResultList();\n"
            repository_code += "    }\n\n"
            repository_code += f"    public {entity_class} findById(Long id) {{\n"
            repository_code += f"        return entityManager.find({entity_class}.class, id);\n"
            repository_code += "    }\n\n"
            repository_code += f"    public void save({entity_class} entity) {{\n"
            repository_code += "        entityManager.persist(entity);\n"
            repository_code += "    }\n\n"
            repository_code += f"    public void update({entity_class} entity) {{\n"
            repository_code += "        entityManager.merge(entity);\n"
            repository_code += "    }\n\n"
            repository_code += f"    public void delete(Long id) {{\n"
            repository_code += f"        {entity_class} entity = findById(id);\n"
            repository_code += "        if (entity != null) {{\n"
            repository_code += "            entityManager.remove(entity);\n"
            repository_code += "        }\n"
            repository_code += "    }\n"
            repository_code += "}\n"

        file_path = os.path.join(repository_output_dir, f"{repository_name}.java")
        with open(file_path, "w") as file:
            file.write(repository_code)

    print(f"\t📚 {len(entities)} repositories generated successfully.")

def generate_services(entities, configuration_variables):
    output_dir = configuration_variables.get("OUT_PUT_DIR", "./generated_classes")
    package = configuration_variables.get("service_classes_packages", "com.example.service")
    package_path = package.replace(".", "/")
    service_output_dir = os.path.join(output_dir, package_path)
    os.makedirs(service_output_dir, exist_ok=True)

    use_spring_data = configuration_variables.get("spring_data_used_for_repositories_and_services", False)

    for entity in entities:
        entity_name = entity["entity_name"]
        service_name = f"{entity_name}Service"
        repository_name = f"{entity_name}Repository"
        repository_package = configuration_variables.get("repository_classes_packages", "com.example.repository")

        service_code = f"package {package};\n\n"
        service_code += "import org.springframework.beans.factory.annotation.Autowired;\n"
        service_code += "import org.springframework.stereotype.Service;\n"
        service_code += f"import {repository_package}.{repository_name};\n\n"
        service_code += "@Service\n"
        service_code += f"public class {service_name} {{\n\n"
        service_code += f"    @Autowired\n"
        service_code += f"    private {repository_name} {repository_name[0].lower() + repository_name[1:]};\n\n"

        if use_spring_data:
            # Use Spring Data JPA methods
            service_code += "    // Example method using Spring Data JPA\n"
            service_code += f"    public List<{entity_name}> findAll() {{\n"
            service_code += f"        return {repository_name[0].lower() + repository_name[1:]}.findAll();\n"
            service_code += "    }\n\n"
        else:
            # Use pure JPA methods
            service_code += "    // Example method using pure JPA\n"
            service_code += f"    public List<{entity_name}> findAll() {{\n"
            service_code += f"        return {repository_name[0].lower() + repository_name[1:]}.findAll();\n"
            service_code += "    }\n\n"

        service_code += "}\n"

        file_path = os.path.join(service_output_dir, f"{service_name}.java")
        with open(file_path, "w") as file:
            file.write(service_code)

    print(f"\t🛠️ {len(entities)} services generated successfully.")

############################# CONFIGURATION FILES #############################


def generate_configuration_files(configuration_variables):
    output_dir = configuration_variables.get("OUT_PUT_DIR", "./generated_classes")
    config_dir = os.path.join(output_dir, "./src/main/resources")
    config_java_dir = os.path.join(output_dir, "./src/main/java/com/example/config")
    
    # Create necessary directories
    os.makedirs(config_dir, exist_ok=True)
    os.makedirs(config_java_dir, exist_ok=True)

    # Generate application-dev.properties
    with open(os.path.join(config_dir, "application-dev.properties"), "w") as file:
        file.write("""# Database configuration (H2 for development)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Hibernate configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Enable H2 console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Server configuration
server.port=8080

# Logging
logging.level.org.springframework=DEBUG
logging.level.com.example=DEBUG
""")

    # Generate application-prod.properties
    with open(os.path.join(config_dir, "application-prod.properties"), "w") as file:
        file.write("""# Database configuration (MySQL for production)
spring.datasource.url=jdbc:mysql://prod-db:3306/mydatabase
spring.datasource.username=produser
spring.datasource.password=prodpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server configuration
server.port=8080

# Logging
logging.level.org.springframework=INFO
logging.level.com.example=INFO

# Disable H2 console
spring.h2.console.enabled=false
""")

    # Generate logback-spring.xml
    with open(os.path.join(config_dir, "logback-spring.xml"), "w") as file:
        file.write("""<configuration>
    <!-- Console appender -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Application logger -->
    <logger name="com.example" level="DEBUG" />

    <!-- Spring logger -->
    <logger name="org.springframework" level="INFO" />

    <!-- Root logger -->
    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
""")

    # Generate SwaggerConfig.java
    with open(os.path.join(config_java_dir, "SwaggerConfig.java"), "w") as file:
        file.write("""package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.example.controller"))
            .paths(PathSelectors.any())
            .build();
    }
}
""")

    # Generate SecurityConfig.java
    with open(os.path.join(config_java_dir, "SecurityConfig.java"), "w") as file:
        file.write("""package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for REST APIs
            .authorizeHttpRequests()
                .requestMatchers("/api/public/**").permitAll() // Allow public access to certain routes
                .anyRequest().authenticated() // All other routes require authentication
            .and()
            .httpBasic(); // Use basic authentication (username/password)
        return http.build();
    }
}
""")

    # Generate application.yml
    with open(os.path.join(config_dir, "application.yml"), "w") as file:
        file.write("""spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydatabase
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
server:
  port: 8080
logging:
  level:
    org.springframework: INFO
    com.example: DEBUG
""")

    # Generate application.properties
    with open(os.path.join(config_dir, "application.properties"), "w") as file:
        file.write("""# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server configuration
server.port=8080

# Logging
logging.level.org.springframework=INFO
logging.level.com.example=DEBUG
""")

    print(f"\t✅ Configuration files generated successfully in {output_dir}.")


############################# USER INTERACTION #############################

def ask_display():
    """
    Asks the user if they want to display the generated classes.
    """
    response = input("\nDo you want to display in the console, here, all the model classes created \nand generated in the dedicated directory? (Yes/No): ").strip().lower()
    return response in ["yes", "y"]


############################# SCRIPT ENTRY POINT #############################

if __name__ == "__main__":
    ###########################################################
    # Load and verify data
    ###########################################################
    file_path = 'data.json'  # Path to the JSON file containing the data
    print("\n" + "=" * 80)
    print("Loading data from the JSON file...".center(80))
    print("=" * 80)
    data = verify_data(file_path=file_path)

    if data is not None:
        print("\n" + "=" * 80)
        print("Data loaded successfully. Starting class generation...".center(80))
        print("=" * 80)

        ###########################################################
        # Generate classes, interfaces, embeddables, and enums
        ###########################################################
        # Generate model classes
        generated_classes = generateClasses(data["entities"], data["configuration_variables"])
        print(f"\n\t📦 {len(generated_classes)} model classes generated successfully.")

        # Generate interfaces
        if "interface_classes" in data:
            generate_interfaces(data["interface_classes"], data["configuration_variables"])
            print(f"\t📜 {len(data['interface_classes'])} interfaces generated successfully.")

        # Generate embeddable classes
        if "embeddable_classes" in data:
            generate_embeddables(data["embeddable_classes"], data["configuration_variables"])
            print(f"\t📎 {len(data['embeddable_classes'])} embeddable classes generated successfully.")

        # Generate enums
        if "enum_classes" in data:
            generate_enums(data["enum_classes"], data["configuration_variables"])
            print(f"\t🔢 {len(data['enum_classes'])} enums generated successfully.")

        # Generate repositories
        generate_repositories(data["entities"], data["configuration_variables"])

        # Generate services
        generate_services(data["entities"], data["configuration_variables"])

        # Generate controllers
        generate_controllers(data["entities"], data["configuration_variables"])

         # Generate configuration files
        generate_configuration_files(data["configuration_variables"])

        ###########################################################
        # Display generated classes (optional)
        ###########################################################
        if ask_display():
            print("\n" + "=" * 80)
            print("Displaying generated classes:".center(80))
            print("=" * 80)
            for class_code in generated_classes:
                print(class_code)
                print("=" * 80)  # Visual separator
                print("\n")
        else:
            output_dir = data["configuration_variables"].get("OUT_PUT_DIR", "./generated_classes")
            print("\n" + "=" * 80)
            print("Generation completed!!!".center(80))
            print("=" * 80)
            print(f"Go to the '{output_dir}' directory to see the generated files.")
            print("Thank you for using our script!")

    else:
        ###########################################################
        # Error handling
        ###########################################################
        print("\n" + "=" * 80)
        print("Error: Unable to generate classes.".center(80))
        print("=" * 80)
        print("Reason: The data is invalid or missing.")
