# DOCUMENTATION
- **Author** : Junior ADI 
- **e-mail**: [rootoor.projects@gmail.com](rootoor.projects@gmail.com)
- **Twitter**: [@caifyoca](https://x.com/caifyoca)
- **LinkedIn**: [Junior ADI](www.linkedin.com/in/junior-adi-3b3246241)
- Date : January 13th, 2025 - Abidjan, Côte d'Ivoire, West Africa.
- License : GPLv3

# Why Such a Program?

When working on Java projects, even with frameworks, we often face the challenge of creating a multitude of files due to Java's principle of recommending one file per class. This process can become very tedious, especially for large-scale projects, as an increasing number of classes leads to even more files. Additionally, the relationships between classes (association, composition, aggregation, inheritance, and even dependency) can sometimes complicate development, significantly extending the time required for design and implementation.  

To alleviate these time-consuming tasks, the **"JavaClassGenerator"** program was created.  

### Benefits of such a program in the short, medium, and long term:  

#### **Short-term benefits:**  
1. **Significant time savings**: The program automates the creation of Java classes along with their relationships, reducing the time needed to kickstart a project.  
2. **Reduced human errors**: By standardizing class generation and their relationships, errors caused by manual implementation are minimized.  
3. **Ease of initialization**: Java projects can be set up more quickly, allowing developers to focus on business logic from the very beginning.  

#### **Medium-term benefits:**  
1. **Better organization**: Projects are structured consistently, making the code easier to understand for teams.  
2. **Improved collaboration**: Following standardized conventions helps developers collaborate more effectively on clear and well-defined codebases.  
3. **Increased reusability**: Generated class templates can be reused or adapted for similar projects, saving even more time.  

#### **Long-term benefits:**  
1. **Reduced maintenance costs**: A well-defined code structure simplifies maintenance and evolution of projects, even after several years.  
2. **Simplified scalability**: Managing large-scale projects becomes easier thanks to coherent and modular architecture.  
3. **Improved productivity**: By eliminating repetitive and tedious tasks, developers can focus on more creative and innovative aspects of their projects.  

In summary, **JavaClassGenerator** is a tool that addresses the pressing need for automation and standardization in Java development. It enhances productivity and project quality while simplifying the management of complex class relationships, regardless of the project's size and nature. Automating the creation of files and directories for Java projects offers numerous benefits that can significantly improve the efficiency and quality of software development. 

# The reasons why such a program is beneficial

## 1. Time Savings

### Automation of Repetitive Tasks
Manually creating files and directories for each entity (model, controller, repository, DTO, configuration) is a repetitive and time-consuming task. By automating this process, developers can focus on more complex and creative tasks, such as designing business logic.

### Reduced Development Time
By generating the basic structures automatically, the script allows for quicker initiation of new feature development. This accelerates the development cycle and enables faster product delivery.

## 2. Standardization of Structure

### Consistent Naming Conventions
Using standardized naming conventions and directory structures improves code readability and maintainability. This facilitates collaboration among team members and makes the project more understandable for new developers.

### Consistency
A standardized structure ensures that all projects follow the same conventions, which is particularly useful in large organizations where multiple teams work on different projects.

## 3. Reduction of Errors

### Fewer Typographical Errors
Automation reduces human errors, such as typos and omissions. The automatically generated files are consistent and adhere to best practices, minimizing common errors.

### Automatically Declared Packages
The script ensures that all necessary packages are correctly declared, avoiding compilation errors and dependency issues.

## 4. Flexibility and Scalability

### Easy Addition of New Entities
The script allows for easy addition of new entities without manually modifying the project structure. This makes the project more flexible and scalable.

### Extensibility
The script can be extended to include specific annotations, additional templates, or other features based on project needs. This allows the script to be adapted to various contexts and requirements.

## 5. Compatibility with Frameworks

### Adapted to Various Frameworks
The script is compatible with several Java frameworks, including Spring Boot and JPA. This allows developers to use it in a variety of projects without making significant modifications.

### Easy Integration
Integration with commonly used Java tools and libraries facilitates the adoption of the script in both new and existing projects.

## 6. Focus on Business Logic

### Feature Development
By automating basic tasks, developers can focus on developing business logic, which is often the most complex and critical part of a project. This improves code quality and end-user satisfaction.

### Innovation
Freed from repetitive tasks, developers can devote more time to innovation and improving existing features.

## Program Review

The program supports:
- Entities without attributes, hence without attributes or annotation(s)
- Entities with attributes without annotation(s)
- Entities with attributes with annotation(s)
- Entities in OneToOne, OneToMany, ManyToOne, and ManyToMany uni- or bi-directional composition relationships
- Entities in inheritance relationships
- Enumerations
- Embedded classes
- Interfaces
- Custom annotations (see below)

# DATA FORM STRUCTURE

### **Configuration Variables for Our Script or Program**

```json
"configuration_variables": {
        "jpa_used": true,
        "jarkata_persistence_api": true,
        "generate_classes_following_packages": true,
        "OUT_PUT_DIR": "./generated_classes",
        "model_classes_package": "com.example.entity",
        "controller_classes_packages": "com.example.controller",
        "repository_classes_packages": "com.example.repository",
        "service_classes_packages": "com.example.service",
        "entity_suffix": "Entity",
        "pojo_model_beanified": true,
        "id_generated_value": "IDENTITY",
        "add_models_no_and_all_args_constructors": true,
        "add_model_class_getters_setters": true,
        "add_model_class_hashcode_equals_tostring_methods": true,
        "spring_data_used_for_repositories_and_services": true
    }
```
![structure des donnees](https://github.com/user-attachments/assets/18c29d51-8bf6-4d0c-8aff-c21616a693d8)

- **JPA Usage**:
  - `"jpa_used": true` indicates that the application is configured to use JPA for object-relational mapping.

- **Jakarta Persistence API**:
  - `"jarkata_persistence_api": true` confirms that the Jakarta Persistence API is employed, facilitating data management between Java objects and relational databases. If false, the Javax Persistence API will be employed.

- **Class Generation Settings**:
  - `"generate_classes_following_packages": true` suggests that the application will generate classes based on specified package structures.

- **Output Directory**:
  - `"OUT_PUT_DIR": "./generated_classes"` specifies the directory where the generated classes will be stored.

- **Package Names**:
  - These fields define the packages for various components:
    - `"model_classes_package": "com.example.entity"`: Package for entity classes.
    - `"controller_classes_packages": "com.example.controller"`: Package for controller classes.
    - `"repository_classes_packages": "com.example.repository"`: Package for repository interfaces.
    - `"service_classes_packages": "com.example.service"`: Package for service classes.

- **Entity Configuration**:
  - `"entity_suffix": "Entity"` indicates that the generated entity classes will have this suffix if not null or empty.
  - `"pojo_model_beanified": true` suggests that Plain Old Java Objects (POJOs) will implement Serializable when `jpa_used=false` as well.

- **ID Generation Strategy**:
  - `"id_generated_value": "IDENTITY"` specifies that the primary key for entities will be generated using an identity strategy, which typically relies on database auto-increment features. Other possible values include: AUTO, SEQUENCE, etc.

- **Constructor and Method Generation**:
  - `"add_models_no_and_all_args_constructors": true`: No-argument and all-arguments constructors will be added to model classes.
  - `"add_model_class_getters_setters": true`: Getter and setter methods will be generated for model classes.
  - `"add_model_class_hashcode_equals_tostring_methods": true`: Common methods like `hashCode`, `equals`, and `toString` will be implemented in model classes.

- **Integration with Spring Data**:
  - `"spring_data_used_for_repositories_and_services": true` indicates that Spring Data is used to manage repositories and services, allowing easier data access and manipulation through Spring's abstraction layers. If false, rely on Eclipselink ORM.

---

### **JSON Structure of Entities**

1. **Entities**: Represents the list of entities in our data model. Each object represents a Java class with its annotations, implemented interfaces, relationships, and fields.

2. **Main Fields of Each Entity**:
   - **entity_name**: The name of the class (entity).
   - **entity_supplementary_annotations**: List of annotations specific to the class, such as `@NoArgsConstructor`, `@Getter`, etc. Except for **"@Entity", "@Table" and the annotations of parent and derived classes**. These are automatically managed in the implicit relationships or bindings of certain (custom) field annotations.
   - **interfaces_implemented**: List of interfaces that the class implements. Except for **"Serializable"** which is automatically managed according to the configuration variables.
   - **entity_is_parent**: Indicates if the class is a parent class in an inheritance hierarchy.
   - **entity_inheritance_strategy**: Defines the inheritance strategy (e.g., `SINGLE_TABLE`, `JOINED`, `TABLE_PER_CLASS`, and `MAPPED_SUPERCLASS`).
   - **entity_parent_name**: If the class inherits from another, this field contains the name of the parent class.
   - **discriminator_column_name**: The name of the discriminator column (useful for inheritance strategies like `SINGLE_TABLE`).
   - **discriminator_type**: The type of the discriminator column (e.g., `Char`, `String`, `Integer`, etc., to be written in capital letters).
   - **discriminator_value**: The discriminator value associated with this entity.
   - **fields**: List of the entity's fields (attributes) with their names, types, and annotations.

3. **Fields or Attributes**:
   - **field_name**: The name of the field.
   - **field_type**: The type of the field (e.g., `Long`, `String`).
   - **field_annotations**: List of annotations applied to the field (such as `@Id`, `@GeneratedValue`).

---

### **Completion with a Complex Example**

```json
{
    "entities": [
        {
            "entity_name": "Livre",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "titre",
                    "field_type": "String",
                    "field_annotations": ["@Column(name=\"titre_livre\", nullable = false)"]
                },
                {
                    "field_name": "lecteurs",
                    "field_type": "List<Lecteur>",
                    "field_annotations": ["@OneToMany(mappedBy = \"livre\", cascade = CascadeType.ALL)"]
                }
            ]
        },
        {
            "entity_name": "Lecteur",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "nom",
                    "field_type": "String",
                    "field_annotations": ["@Column(nullable = false)"]
                },
                {
                    "field_name": "livre",
                    "field_type": "Livre",
                    "field_annotations": ["@ManyToOne", "@JoinColumn(name = 'livre_id')"]
                }
            ]
        }
    ]
}
```

### **JSON Structure of Enumerations**

There are three types of enumerations supported by our program:
- Simple enumerations: case of "Presence",
- Enumerations with values without a private field: case of "Coefficient",
- Enumerations with values with a private field: case of "Statut".

Examples:

```json
"enum_classes": [
        {
            "enum_name": "Presence",
            "enum_values": ["ABSENT(0)", "PRESENT(1)", "INDISPONIBLE(2)"]
        },
        {
            "enum_name": "Coefficient",
            "enum_values": ["UN(1)", "DEUX(2)", "TROIS(3)", "QUATRE(4)","valeur"]
        },
        {
            "enum_name": "Statut",
            "enum_values": ["VALIDE", "INVALIDE", "ANNULE"]
        }
    ]
```

### **JSON Structure of Embedded Classes**

Embedded classes are also considered by our program.

Examples:

```json
"embeddable_classes": [
    {
        "embeddable_name": "AdressePostable",
        "fields": [
            {
                "field_name": "rue",
                "field_type": "String",
                "field_annotations": ["@Column(name = \"rue\", nullable = false)"]
            },
            {
                "field_name": "ville",
                "field_type": "String",
                "field_annotations": ["@Column(name = \"ville\", nullable = false)"]
            },
            {
                "field_name": "codePostal",
                "field_type": "String",
                "field_annotations": ["@Column(name = \"code_postal\", nullable = false)"]
            },
            {
                "field_name": "pays",
                "field_type": "String",
                "field_annotations": ["@Column(name = \"pays\", nullable = false)"]
            }
        ]
    },
    {
        "embeddable_name": "Coordonnees",
        "fields": [
            {
                "field_name": "telephone",
                "field_type": "String",
                "field_annotations": ["@Column(name = \"telephone\", nullable = false)"]
            },
            {
                "field_name": "email",
                "field_type": "String",
                "field_annotations": ["@Column(name = \"email\", nullable = false)"]
            }
        ]
    },
    {
        "embeddable_name": "Duree",
        "fields": [
            {
                "field_name": "heures",
                "field_type": "int",
                "field_annotations": ["@Column(name = \"heures\", nullable = false)"]
            },
            {
                "field_name": "minutes",
                "field_type": "int",
                "field_annotations": ["@Column(name = \"minutes\", nullable = false)"]
            },
            {
                "field_name": "secondes",
                "field_type": "int",
                "field_annotations": ["@Column(name = \"secondes\", nullable = false)"]
            }
        ]
    },
    {
        "embeddable_name": "Prix",
        "fields": [
            {
                "field_name": "montant",
                "field_type": "double",
                "field_annotations": ["@Column(name = \"montant\", nullable = false)"]
            },
            {
                "field_name": "devise",
                "field_type": "String",
                "field_annotations": ["@Column(name = \"devise\", nullable = false)"]
            }
        ]
    },
    {
        "embeddable_name": "Taille",
        "fields": [
            {
                "field_name": "largeur",
                "field_type": "double",
                "field_annotations": ["@Column(name = \"largeur\", nullable = false)"]
            },
            {
                "field_name": "hauteur",
                "field_type": "double",
                "field_annotations": ["@Column(name = \"hauteur\", nullable = false)"]
            },
            {
                "field_name": "profondeur",
                "field_type": "double",
                "field_annotations": ["@Column(name = \"profondeur\", nullable = false)"]
            }
        ]
    }
    ]
```

### **JSON Structure of Implementable or Implemented Interface Classes**

Interfaces are also considered by our program.

Examples:

```json
"interface_classes": [
        {
            "interface_name": "Authentifiable",
            "methods": [
                "boolean authentifier(String identifiant, String motDePasse)",
                "void deconnecter()"
            ]
        },
        {
            "interface_name": "Exportable",
            "methods": [
                "String exporterEnCSV()",
                "String exporterEnJSON()"
            ]
        },
        {
            "interface_name": "Empruntable",
            "methods": [
                "boolean estDisponible()",
                "void emprunter(String utilisateur)",
                "void retourner()"
            ]
        },
        {
            "interface_name": "Moderable",
            "methods": [
                "boolean estApproprié()",
                "void supprimer()",
                "void modifier(String nouveauContenu)"
            ]
        },
        {
            "interface_name": "Publisable",
            "methods": [
                "void publier()",
                "void retirer()",
                "boolean estPublic()"
            ]
        },
        {
            "interface_name": "Inscriptible",
            "methods": [
                "void sInscrire(Cours cours)",
                "void seDesinscrire(Cours cours)",
                "boolean estInscrit(Cours cours)"
            ]
        },
        {
            "interface_name": "Planifiable",
            "methods": [
                "void planifier(Date dateDebut, Date dateFin)",
                "boolean estPlanifie()",
                "void annuler()"
            ]
        }
    ]
```

# ENTITY AND FIELD ANNOTATIONS

## 1. **Standardized JPA Annotations**

JPA (Java Persistence API) annotations are metadata used to map Java objects to database tables. They are all supported by our script, including "lombok" annotations. Here are some of the commonly used standardized JPA annotations:

- **@Entity**: Specifies that the class is an entity and is mapped to a database table.
- **@Table**: Allows specifying the database table to which the entity is mapped.
- **@Id**: Specifies the primary identifier of an entity.
- **@GeneratedValue**: Provides the specification of the generation of the primary identifier value.
- **@Column**: Used to specify the mapping between an entity attribute and the database table column.
- **@OneToOne**: Defines a unidirectional or bidirectional one-to-one relationship.
- **@OneToMany**: Defines a unidirectional or bidirectional one-to-many relationship.
- **@ManyToOne**: Defines a unidirectional or bidirectional many-to-one relationship.
- **@ManyToMany**: Defines a unidirectional or bidirectional many-to-many relationship.
- **@JoinColumn**: Specifies a join column for an entity relationship or a collection of elements.
- **@JoinTable**: Specifies the join table for a many-to-many relationship or for a collection of elements.
- **@Embeddable**: Specifies a class whose instances are stored as an embedded object and share the same table as the owning entity.
- **@Embedded**: Specifies a persistent attribute of an entity whose type is an embeddable class.
- **@Enumerated**: Specifies that a persistent attribute should be persisted as an enumeration.
- **@Temporal**: Specifies that the persistent attribute should be persisted as a date/time value.
- **@Transient**: Specifies that the attribute or property field to which it is applied should be transient and not persisted.

## 2. **Custom Annotations and Their Roles**

These are annotations designed by us to simplify and ensure flexibility, precision, and accuracy in defining relationships between classes or entities.

- **@OneToOneJoinColumn** or **@OneToOne and @JoinColumn**: Indicates the "owning" or "owner" side of a `OneToOne` relationship with a join column.
- **@OneToOneMappedBy**: Indicates the "inverse" side of a `OneToOne` relationship. To be specified only in case of bidirectionality of the relationship.
- **@OneToManyMappedBy**: Indicates the "inverse" side of a `OneToMany` relationship. To be specified only in case of bidirectionality of the relationship.
- **@ManyToOneJoinColumn**: Indicates the "owning" or "owner" side of a `ManyToOne` relationship with a join column.
- **@ManyToManyJoinTable**: Indicates the "owning" or "owner" side of a `ManyToMany` relationship with a join table.
- **@ManyToManyMappedBy**: Indicates the "inverse" side of a `ManyToMany` relationship. A `ManyToMany` relationship is inherently bidirectional. Therefore, there can never be unidirectionality of such a relationship. NEVER!
- **@Enum** or **@Enumerated**: Indicates that an attribute is an enumeration.
- **@Embedded**: Indicates that an attribute is an embeddable class.

**NB**:
In any composition relationship, the side **"ToMany"** always carries **"MappedBy"**, while the side **"ToOne"** always carries **"JoinColumn"**. This implies the non-existence of unidirectional ManyToMany relationships and custom annotations such as:
- ~**@OneToManyJoinColumn**~. DOES NOT EXIST, WILL NEVER EXIST.
- ~**@ManyToOneMappedBy**~. DOES NOT EXIST, WILL NEVER EXIST.
- ~**Unidirectional Many-To-Many**~. DOES NOT EXIST, WILL NEVER EXIST.

# COMPOSITION RELATIONSHIPS: One-To-One, One-To-Many, Many-To-One, Many-To-Many

A relationship is always defined between two entities:
- **Source Entity**: This is the entity that contains the reference or pointer to the other entity. It is often called the "owning" or "owner" side of the relationship. In a unidirectional relationship, only the source entity has a reference to the target entity. In a bidirectional relationship, the source entity is the one that manages the relationship and contains the foreign key or join table.
- **Target Entity**: This is the entity to which the source entity points. In a unidirectional relationship, the target entity does not have a reference to the source entity. In a bidirectional relationship, the target entity also has a reference to the source entity, but this reference is marked with **`mappedBy`** to indicate that it is managed by the source entity.
![Relation de Composition](https://github.com/user-attachments/assets/9ef55d2d-d083-4eae-aff3-c707a0913f6c)

## **Recognition and Writing of Composition Relationships**

1. **Recognition of Relationships**:
   - **Unidirectional**: Only the source entity has a reference to the target entity. There is no inverse reference.
   - **Bidirectional**: Both entities have references to each other. The relationship is managed by the source entity, and the target entity uses `mappedBy` to indicate this management.

2. **Writing of Composition Relationships**:
   - **Unidirectionality of Composition Relationships**:
     - Use annotations like `@OneToOne`, `@OneToMany`, `@ManyToOne`, and `@ManyToMany` with `@JoinColumn` or `@JoinTable` to specify the join column or table.
     - Example:
       ```java
       @Entity
       public class Livre {
           @Id
           @GeneratedValue(strategy = GenerationType.IDENTITY)
           private Long id;

           @ManyToOne
           @JoinColumn(name = "lecteur_id")
           private Lecteur lecteur; // attribute to be fully copied into the mappedBy parameter
       }
       ```   
   - **Bidirectionality of Composition Relationships**:
     - Use `mappedBy` on the inverse side to indicate that the relationship is managed by the source entity.
     - Example:
       ```java
       @Entity
       public class Lecteur {
           @Id
           @GeneratedValue(strategy = GenerationType.IDENTITY)
           private Long id;

           @OneToMany(mappedBy = "lecteur") // full copy of the attribute defined in the "owning" or "owner" side
           private List<Livre> livres;
       }
       ```

## Unidirectional Relationship
In the case of a **unidirectional relationship**, only one of the two entities has a reference to the other. There is no inverse reference, so the use of `mappedBy` is not necessary. Here's how it works and how to configure a unidirectional relationship in JPA.

---

### **Key Points for Unidirectional Relationships**
1. **Owning Side**: The side that contains the reference is always the owning side.
2. **No `mappedBy`**: Since there is no inverse side, `mappedBy` is not used.
3. **Standard JPA Annotations**:
   - `@ManyToOne`: Used with `@JoinColumn` to specify the join column.
   - `@OneToMany`: Used with `@JoinColumn` or `@JoinTable` to manage the relationship.
   - `@OneToOne`: Used with `@JoinColumn` to specify the join column.
   - `@ManyToMany`: Used with `@JoinTable` to define the join table.
4. **Database**: The relationship table is managed on the owning side (e.g., a foreign key or join table).

---

### **Advantages of Unidirectional Relationships**
- **Simplicity**: Less code to write and maintain.
- **Clarity**: The relationship is clearly defined in one direction.
- **Performance**: No overhead associated with managing an inverse reference.

---

### **When to Use a Unidirectional Relationship?**
- When you don't need to navigate from the target entity to the source entity.
- When you want to simplify your data model.
- When the relationship is naturally unidirectional (e.g., a `Book` belongs to a `User`, but a `User` does not need to know all their `Book`s).

![OneToMany - ManyToOne - unidir](https://github.com/user-attachments/assets/9fd646e3-6461-4b25-bb8c-7c9c639dc986)

![OneToMany - unidir](https://github.com/user-attachments/assets/1f29c85b-4102-49d7-ab9d-c0c851b10039)

![ManyToOne - unidir](https://github.com/user-attachments/assets/07eaa25b-3db6-47be-9688-6586b2366873)
  
---

## Bidirectional Relationship
In the case of a **bidirectional relationship**, both entities have a reference to each other. This allows navigation in both directions between the entities. To configure a bidirectional relationship in JPA, it is essential to understand the concepts of the **owning side** and the **inverse side**, as well as the use of the `mappedBy` attribute.

---

### **Characteristics of a Bidirectional Relationship**
1. **Two Sides of the Relationship**: Both entities have a reference to each other.
2. **Owning Side**: The side that manages the relationship (e.g., the table that contains the foreign key).
3. **Inverse Side**: The side that is mapped by the owning side (uses `mappedBy`).
4. **Synchronization**: It is important to synchronize both sides of the relationship to avoid inconsistencies.

---

### **Key Points for Bidirectional Relationships**
1. **Owning Side**: The side that manages the relationship (e.g., the table that contains the foreign key or join table).
2. **Inverse Side**: The side that uses `mappedBy` to indicate that the relationship is managed by the other entity.
3. **Synchronization**: It is crucial to synchronize both sides of the relationship to avoid inconsistencies. Use utility methods for this.
4. **Standard JPA Annotations**:
   - `@OneToMany`: Used with `mappedBy` on the inverse side.
   - `@ManyToOne`: Used with `@JoinColumn` on the owning side.
   - `@OneToOne`: Used with `mappedBy` on the inverse side and `@JoinColumn` on the owning side.
   - `@ManyToMany`: Used with `mappedBy` on the inverse side and `@JoinTable` on the owning side.

---

### **Advantages of Bidirectional Relationships**
- **Navigation in Both Directions**: Allows easy navigation between entities.
- **Flexibility**: Useful when both sides of the relationship are frequently used.
- **Accurate Modeling**: Better represents certain real-world relationships.

---

### **When to Use a Bidirectional Relationship?**
- When you need to navigate in both directions between the entities.
- When both sides of the relationship are frequently used in your business logic.
- When the relationship is naturally bidirectional (e.g., a `User` has `Book`s, and a `Book` belongs to a `User`).

![OneToOne - bidir2](https://github.com/user-attachments/assets/867986e3-9286-4543-90be-9631c19c109e)
![OneToOne - bidir0](https://github.com/user-attachments/assets/989e1aaa-977b-4d6e-a1ae-0064f952bcb5)
![OneToOne - bidir](https://github.com/user-attachments/assets/4dd87dfe-5704-4cc5-93a8-5aaef0e8a425)
![OneToMany - ManyToOne - bidir](https://github.com/user-attachments/assets/11bc7a10-8f7b-4398-b3b5-08eabb059b9e)
![OneToMany - bidir](https://github.com/user-attachments/assets/e90362f0-2e6f-45c5-b4b8-8f1d2072f2b8)
![ManyToOne - bidir](https://github.com/user-attachments/assets/4939bc66-f6d7-4edd-a487-c9797c1685bb)
![ManyToMany - bidir](https://github.com/user-attachments/assets/05306548-8f01-4a8e-84a1-7d0860182ca6)

---

## 2. **Custom Annotations and Their Roles in Composition Relationships**

These annotations have been designed by us to simplify and ensure flexibility, precision, and accuracy in defining relationships between classes or entities.

- **@OneToOneJoinColumn** or **@OneToOne and @JoinColumn**: Indicates the "owning" or "owner" side of a `OneToOne` relationship with a join column.
- **@OneToOneMappedBy**: Indicates the "inverse" side of a `OneToOne` relationship. To be specified only in case of bidirectionality of the relationship.
- **@OneToManyMappedBy**: Indicates the "inverse" side of a `OneToMany` relationship. To be specified only in case of bidirectionality of the relationship.
- **@ManyToOneJoinColumn**: Indicates the "owning" or "owner" side of a `ManyToOne` relationship with a join column.
- **@ManyToManyJoinTable**: Indicates the "owning" or "owner" side of a `ManyToMany` relationship with a join table.
- **@ManyToManyMappedBy**: Indicates the "inverse" side of a `ManyToMany` relationship. A `ManyToMany` relationship is inherently bidirectional. Therefore, there can never be unidirectionality of such a relationship. NEVER!

**NB**:
In any composition relationship, the side **"ToMany"** always carries **"MappedBy"**, while the side **"ToOne"** always carries **"JoinColumn"**. This implies the non-existence of unidirectional ManyToMany relationships and custom annotations such as:
- ~**@OneToManyJoinColumn**~. DOES NOT EXIST, WILL NEVER EXIST.
- ~**@ManyToOneMappedBy**~. DOES NOT EXIST, WILL NEVER EXIST.
- ~**Unidirectional Many-To-Many**~. DOES NOT EXIST, WILL NEVER EXIST.

# APPLICATIONS

```plaintext
Basic Statement:

In a library, there are several readers and several books. However, each reader can only borrow one book at a time, which they return or give back after reading before borrowing another. No book has the ability to know which reader has borrowed or is borrowing it. Therefore, the relationship between Reader and Book is a unidirectional relationship. Each reader must subscribe to have the right to borrow the library's books. Each subscription is marked by the date of registration or issuance of the card and by the date of expiration of the card.
```

## **Unidirectional One-to-Many Relationship** and **Unidirectional Many-to-One Relationship**

**Statement**:

In a library, each **reader** can borrow several **books** at the same time, but a **book** can only be borrowed by one reader at any given time. No book retains information about the reader who borrowed or is currently borrowing it. This relationship between **Reader** and **Book** is therefore a **unidirectional** relationship of type **One-to-Many**.
Additionally, each reader must have a **subscription** that gives them the right to borrow books. The subscription is identified by a **registration date** and an **expiration date**.

### Analysis and UML Formalism

To formalize this relationship in **UML**, here are the key elements to represent:

- **Reader Class**:
        -- Has an association with a single Book (cardinality 0..1), as a reader can borrow at most one book at a time.
- **Book Class**:
        -- No direct link to a Reader, as the relationship is unidirectional (the book does not "know" the reader who borrowed it).

### Comparison of Relationships:
1. **One-to-Many (Reader to Book)** :
- According to the statement and its constraints, from **Reader** to **Book**, we are in a **Many-To-One** situation. Indeed,
   - Several **Readers** can be linked to One **Book**.
   - The **Book** borrowed does not know (ignores) which **Reader** borrowed it.

2. **Many-to-One (Book to Reader)** :
- According to the statement and its constraints, from **Book** to **Reader**, we are in a **One-to-Many** situation. Indeed,
   - A **Book** can be linked to several **Readers**.
   - The **Reader** is unknown to (ignored by) the **Book** they borrow or have borrowed.

### Examples:

Let's now transform this into Json according to our data structure:

- **1st Case**: Correct/compliant writing of the annotations of composition relationships with JPA

```json
{
    "entities": [
        {
            "entity_name": "Livre",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "titre",
                    "field_type": "String",
                    "field_annotations": ["@Column(name=\"titre_livre\", nullable = false)"]
                }
            ]
        },
        {
            "entity_name": "Lecteur",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "nom",
                    "field_type": "String",
                    "field_annotations": ["@Column(nullable = false)"]
                },
                {
                    "field_name": "livre",
                    "field_type": "Livre",
                    "field_annotations": ["@ManyToOne", "@JoinColumn(name = 'livre_id')"]
                }
            ]
        }
    ]
}
```

- **2nd Case**: Custom writing of the annotations of composition relationships with JPA

```json
{
    "entities": [
        {
            "entity_name": "Livre",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "titre",
                    "field_type": "String",
                    "field_annotations": ["@Column(name=\"titre_livre\", nullable = false)"]
                }
            ]
        },
        {
            "entity_name": "Lecteur",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "nom",
                    "field_type": "String",
                    "field_annotations": ["@Column(nullable = false)"]
                },
                {
                    "field_name": "livre",
                    "field_type": "Livre",
                    "field_annotations": ["@ManyToOneJoinColumn"]
                }
            ]
        }
    ]
}
```

## **Bidirectional One-to-Many Relationship** and **Bidirectional Many-to-One Relationship**

Let's take the same statement with a slight modification to allow bidirectionality of the relationships.

**Statement**:
In a library, each **reader** can borrow several **books** at the same time, but a **book** can only be borrowed by one reader at any given time. Each book retains information about the reader who borrowed or is currently borrowing it. This relationship between **Reader** and **Book** is therefore a **bidirectional** relationship of type **One-to-Many**.

```json
{
    "entities": [
        {
            "entity_name": "Livre",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "titre",
                    "field_type": "String",
                    "field_annotations": ["@Column(name=\"titre_livre\", nullable = false)"]
                },
                {
                    "field_name": "lecteurs",
                    "field_type": "List<Lecteur>",
                    "field_annotations": ["@OneToManyMappedBy"]
                }
            ]
        },
        {
            "entity_name": "Lecteur",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "nom",
                    "field_type": "String",
                    "field_annotations": ["@Column(nullable = false)"]
                },
                {
                    "field_name": "livre",
                    "field_type": "Livre",
                    "field_annotations": ["@ManyToOneJoinColumn"]
                }
            ]
        }
    ]
}
```

## **Unidirectional One-to-One Relationship**

In a library, each **reader** can have only one **subscription** at a time. The subscription is identified by a **registration date** and an **expiration date**. This relationship between **Reader** and **Subscription** is a **unidirectional** relationship of type **One-to-One**.

### Analysis and UML Formalism

To formalize this relationship in **UML**, here are the key elements to represent:

- **Reader Class**:
  - Has an association with a single Subscription (cardinality 1), as a reader can have only one subscription at a time.
- **Subscription Class**:
  - No direct link to a Reader, as the relationship is unidirectional (the subscription does not "know" the reader who owns it).

### Examples:

Let's now transform this into Json according to our data structure:

- **1st Case**: Correct/compliant writing of the annotations of composition relationships with JPA

```json
{
    "entities": [
        {
            "entity_name": "Lecteur",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "nom",
                    "field_type": "String",
                    "field_annotations": ["@Column(nullable = false)"]
                },
                {
                    "field_name": "abonnement",
                    "field_type": "Abonnement",
                    "field_annotations": ["@OneToOne", "@JoinColumn(name = 'abonnement_id')"]
                }
            ]
        },
        {
            "entity_name": "Abonnement",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "dateInscription",
                    "field_type": "Date",
                    "field_annotations": ["@Column(nullable = false)"]
                },
                {
                    "field_name": "dateExpiration",
                    "field_type": "Date",
                    "field_annotations": ["@Column(nullable = false)"]
                }
            ]
        }
    ]
}
```

- **2nd Case**: Custom writing of the annotations of composition relationships with JPA

```json
{
    "entities": [
        {
            "entity_name": "Lecteur",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "nom",
                    "field_type": "String",
                    "field_annotations": ["@Column(nullable = false)"]
                },
                {
                    "field_name": "abonnement",
                    "field_type": "Abonnement",
                    "field_annotations": ["@OneToOneJoinColumn"]
                }
            ]
        },
        {
            "entity_name": "Abonnement",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "dateInscription",
                    "field_type": "Date",
                    "field_annotations": ["@Column(nullable = false)"]
                },
                {
                    "field_name": "dateExpiration",
                    "field_type": "Date",
                    "field_annotations": ["@Column(nullable = false)"]
                }
            ]
        }
    ]
}
```

## **Bidirectional Many-to-Many Relationship**

In a library, each **reader** can borrow several **books**, and each **book** can be borrowed by several **readers**. This relationship between **Reader** and **Book** is a **bidirectional** relationship of type **Many-to-Many**.

### Analysis and UML Formalism

To formalize this relationship in **UML**, here are the key elements to represent:

- **Reader Class**:
  - Has an association with multiple Books (cardinality 0..*).
- **Book Class**:
  - Has an association with multiple Readers (cardinality 0..*).

### Examples:

Let's now transform this into Json according to our data structure:

- **1st Case**: Correct/compliant writing of the annotations of composition relationships with JPA

```json
{
    "entities": [
        {
            "entity_name": "Lecteur",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "nom",
                    "field_type": "String",
                    "field_annotations": ["@Column(nullable = false)"]
                },
                {
                    "field_name": "livres",
                    "field_type": "List<Livre>",
                    "field_annotations": ["@ManyToMany", "@JoinTable(name = 'lecteur_livre', joinColumns = @JoinColumn(name = 'lecteur_id'), inverseJoinColumns = @JoinColumn(name = 'livre_id'))"]
                }
            ]
        },
        {
            "entity_name": "Livre",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "titre",
                    "field_type": "String",
                    "field_annotations": ["@Column(name=\"titre_livre\", nullable = false)"]
                },
                {
                    "field_name": "lecteurs",
                    "field_type": "List<Lecteur>",
                    "field_annotations": ["@ManyToMany(mappedBy = 'livres')"]
                }
            ]
        }
    ]
}
```

- **2nd Case**: Custom writing of the annotations of composition relationships with JPA

```json
{
    "entities": [
        {
            "entity_name": "Lecteur",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "nom",
                    "field_type": "String",
                    "field_annotations": ["@Column(nullable = false)"]
                },
                {
                    "field_name": "livres",
                    "field_type": "List<Livre>",
                    "field_annotations": ["@ManyToManyJoinTable"]
                }
            ]
        },
        {
            "entity_name": "Livre",
            "entity_supplementary_annotations": ["@NoArgsConstructor", "@Getter", "@Setter", "@AllArgsConstructor"],
            "interfaces_implemented": [],
            "entity_is_parent": false,
            "entity_inheritance_strategy": "",
            "entity_parent_name": "",
            "discriminator_column_name": "",
            "discriminator_type": "",
            "discriminator_value": "",
            "fields": [
                {
                    "field_name": "id",
                    "field_type": "Long",
                    "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
                },
                {
                    "field_name": "titre",
                    "field_type": "String",
                    "field_annotations": ["@Column(name=\"titre_livre\", nullable = false)"]
                },
                {
                    "field_name": "lecteurs",
                    "field_type": "List<Lecteur>",
                    "field_annotations": ["@ManyToManyMappedBy"]
                }
            ]
        }
    ]
}
```

# HIERARCHICAL OR INHERITANCE RELATIONSHIPS

The program supports hierarchical relationships that exist between entities. Simply specify the `SINGLE_TABLE`, `JOINED`, `TABLE_PER_CLASS`, `MAPPED_SUPERCLASS`, and other parameters of the "entities" key to mark parent classes. For derived classes, omit the strategy and set `entity_is_parent` to false to begin with. Then specify the values of the necessary keys according to the hierarchical relationship. See the examples below for understanding.

1. **Single Table (`SINGLE_TABLE`)**
   - All entities in the hierarchy are stored in a single table.
   - A discriminator column is used to differentiate entity types.

2. **Joined Table (`JOINED`)**
   - Each entity in the hierarchy has its own table.
   - Common fields are stored in the parent table, and specific fields are stored in the child tables.

3. **Table Per Class (`TABLE_PER_CLASS`)**
   - Each entity in the hierarchy has its own table, and there is no shared table for common fields.

4. **Mapped Superclass (`MAPPED_SUPERCLASS`)**
   - The parent class is not an entity itself but provides common fields and mappings to its subclasses.

**Examples**:

```json
"entities": [
    {
        "entity_name": "Personne",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": true,
        "entity_inheritance_strategy": "SINGLE_TABLE",
        "entity_parent_name": "",
        "discriminator_column_name": "type_pers",
        "discriminator_type": "CHAR",
        "discriminator_value": "",
        "fields": [
            {
                "field_name": "id",
                "field_type": "Long",
                "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
            },
            {
                "field_name": "nom",
                "field_type": "String",
                "field_annotations": []
            },
            {
                "field_name": "prenom",
                "field_type": "String",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Employe",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": false,
        "entity_inheritance_strategy": "",
        "entity_parent_name": "Personne",
        "discriminator_column_name": "",
        "discriminator_type": "",
        "discriminator_value": "E",
        "fields": [
            {
                "field_name": "salaire",
                "field_type": "Double",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Client",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": false,
        "entity_inheritance_strategy": "",
        "entity_parent_name": "Personne",
        "discriminator_column_name": "",
        "discriminator_type": "",
        "discriminator_value": "C",
        "fields": [
            {
                "field_name": "adresse",
                "field_type": "String",
                "field_annotations": []
            },
            {
                "field_name": "contact",
                "field_type": "String",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Personne2",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": true,
        "entity_inheritance_strategy": "JOINED",
        "entity_parent_name": "",
        "discriminator_column_name": "type_pers2",
        "discriminator_type": "STRING",
        "discriminator_value": "",
        "fields": [
            {
                "field_name": "id",
                "field_type": "Long",
                "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
            },
            {
                "field_name": "nom",
                "field_type": "String",
                "field_annotations": []
            },
            {
                "field_name": "prenom",
                "field_type": "String",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Employe2",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": false,
        "entity_inheritance_strategy": "",
        "entity_parent_name": "Personne2",
        "discriminator_column_name": "",
        "discriminator_type": "",
        "discriminator_value": "EMP",
        "fields": [
            {
                "field_name": "salaire",
                "field_type": "Double",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Client2",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": false,
        "entity_inheritance_strategy": "",
        "entity_parent_name": "Personne2",
        "discriminator_column_name": "",
        "discriminator_type": "",
        "discriminator_value": "CLI",
        "fields": [
            {
                "field_name": "adresse",
                "field_type": "String",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Personne3",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": true,
        "entity_inheritance_strategy": "TABLE_PER_CLASS",
        "entity_parent_name": "",
        "discriminator_column_name": "",
        "discriminator_type": "",
        "discriminator_value": "",
        "fields": [
            {
                "field_name": "id",
                "field_type": "Long",
                "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
            },
            {
                "field_name": "nom",
                "field_type": "String",
                "field_annotations": []
            },
            {
                "field_name": "prenom",
                "field_type": "String",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Employe3",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": false,
        "entity_inheritance_strategy": "",
        "entity_parent_name": "Personne3",
        "discriminator_column_name": "",
        "discriminator_type": "",
        "discriminator_value": "",
        "fields": [
            {
                "field_name": "salaire",
                "field_type": "Double",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Client3",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": false,
        "entity_inheritance_strategy": "",
        "entity_parent_name": "Personne3",
        "discriminator_column_name": "",
        "discriminator_type": "",
        "discriminator_value": "",
        "fields": [
            {
                "field_name": "adresse",
                "field_type": "String",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Personne4",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": true,
        "entity_inheritance_strategy": "MAPPED_SUPERCLASS",
        "entity_parent_name": "",
        "discriminator_column_name": "",
        "discriminator_type": "",
        "discriminator_value": "",
        "fields": [
            {
                "field_name": "id",
                "field_type": "Long",
                "field_annotations": ["@Id", "@GeneratedValue(strategy = GenerationType.IDENTITY)"]
            },
            {
                "field_name": "nom",
                "field_type": "String",
                "field_annotations": []
            },
            {
                "field_name": "prenom",
                "field_type": "String",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Employe4",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": false,
        "entity_inheritance_strategy": "",
        "entity_parent_name": "Personne4",
        "discriminator_column_name": "",
        "discriminator_type": "",
        "discriminator_value": "",
        "fields": [
            {
                "field_name": "salaire",
                "field_type": "Double",
                "field_annotations": []
            }
        ]
    },
    {
        "entity_name": "Client4",
        "entity_supplementary_annotations": [],
        "interfaces_implemented": [],
        "entity_is_parent": false,
        "entity_inheritance_strategy": "",
        "entity_parent_name": "Personne4",
        "discriminator_column_name": "",
        "discriminator_type": "",
        "discriminator_value": "",
        "fields": [
            {
                "field_name": "adresse",
                "field_type": "String",
                "field_annotations": []
            }
        ]
    }
]
```

# Contribution and Contact

## Contribution

We strongly encourage contributions from the community to improve and extend the features of this script. Here's how you can contribute:

### Report a Bug
If you encounter a bug or issue with the script, please report it by opening an issue on our GitHub repository. Provide as much detail as possible, including steps to reproduce the problem, error messages, and any other relevant information.

### Propose a Feature
Have an idea for a new feature or improvement? Open an issue on GitHub with a detailed description of your proposal. We will review your suggestion and discuss its feasibility.

### Submit Code
1. **Fork the Repository**: Create a copy of the repository on your GitHub account.
2. **Create a Branch**: Create a new branch for your contribution.
3. **Make Changes**: Make the necessary changes and ensure your code is well-documented.
4. **Submit a Pull Request**: Once your changes are complete, submit a pull request for us to review and merge into the main repository.

### Documentation
Documentation is crucial for users to understand and use the script effectively. If you see opportunities to improve the documentation, feel free to submit changes or additions.

## Contact

If you have questions, suggestions, or need assistance, you can contact us through the following channels:

### GitHub Issues
For technical questions, bug reports, or feature proposals, please open an issue on our GitHub repository.

### E-mail

E-mail:
```
Junior ADI  <rootoor.projects@gmail.com>
```

### Social Media
Follow us on social media to stay updated on the latest developments and new features:
- **Twitter**: [@caifyoca](https://x.com/caifyoca)
- **LinkedIn**: [Junior ADI](www.linkedin.com/in/junior-adi-3b3246241)

## Acknowledgments

We would like to thank all the contributors who have helped improve this script. Your support and contributions are essential to making this project a success.

---

# Licence

```
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```
