---
title: 'Annotation Sniffer: A tool to Extract Code Annotations Metrics'
tags:
   - Java
   - metadata
   - annotations
   - software-engineering
   - source-code-analysis
authors:
  - name: Phyllipe Lima
    orcid: 0000-0002-8358-4405
    affiliation: "1,2" #
  - name: Eduardo Guerra
    orcid: 0000-0001-5555-3487
    affiliation: "2" #
  - name: Paulo Meirelles
    orcid: 0000-0002-8923-2814
    affiliation: "3"
affiliations:
 - name: CDG, National Institute of Telecommunications - INATEL, Brazil
   index: 1
 - name: LAC, National Institute for Space Research - INPE, Brazil
   index: 2
 - name: DHI, Federal University of São Paulo - UNIFESP, Brazil
   index: 3
date: 24 Novembrt 2019
bibliography: paper.bib
---

# Summary
Enterprise Java frameworks and APIs such as JPA (Java Persistence API), Spring, EJB (Enterprise Java Bean), and JUnit make extensive use of code annotations as means to allow applications to configure custom metadata and execute specific behavior. Observing the top 30 ranked Java projects on GitHub, they have, on average, 76% of classes with at least one annotation. Some projects may have more than 90% of its classes annotated. To measure code annotations usage and analyze their distribution, our work in [@LIMA2018] proposed a novel suite of software metrics dedicated to code annotations. To obtain threshold values, the Percentile Rank Analysis approach was used [@meirelles2013]. 

Source code metrics retrieve information from software to assess its characteristics. Well-known techniques use metrics associated with rules to detect bad smells on the source code [@Lanza2006]. However, traditional code metrics does not recognize code annotations on programming elements, which can lead to an incomplete code assessment [@Guerra2009]. For instance, a domain class can be considered simple using current complexity metrics. However, it can contain complex annotations for object-XML mapping. Also, using a set of annotations couples the application to a framework that can interpret them and current coupling metrics does not explicitly handle this.

To automate the process of extracting the novel suite of software metrics for code annotation in [@LIMA2018], we developed an open source tool called Annotation Sniffer (`ASniffer`). It is a command line tool that reads java source code, extract the metrics values and ouputs an XML report. Potential `ASniffer` users are software engineers or researchers interested in static code analysis and mining software repositories. Additionally, given that it is an extensible tool, other developers can implement their own metrics and integrate them in the extraction process. Figure 1 presents a simple diagram of the ASniffer tool.

![ASniffer Simple Diagram](figures/asniffer.png)
Figure 1: ASniffer Simple Diagram

The first version of this tool was previously presented and published on a workshop [@LIMA2018c]. The current version has an improved extensibility mechanism as well as a more compact and complete report, to support our ongoing research about code annotations and metadata in object-oriented programming.

# Metadata and Code Annotations

The term "metadata" is used in a variety of contexts in the computer science field. In all of them, it means data referring to the data itself. In databases, the data are the ones persisted, and the metadata is their description, i.e., the structure of the table. In the object-oriented context, the data are the instances, and the metadata is their description, i.e., information that describes the class. As such, fields, methods, super-classes, and interfaces are all metadata of a class instance. A class field, in turn, has its type, access modifiers, and name as its metadata [@guerra2014]. 

Some programming languages provide features that allow custom metadata to be defined and included directly on programming elements. This feature is supported in languages such as Java, through the use of annotations and in C#, by attributes. A benefit is that the metadata definition is closer to the programming element, and its definition is less verbose than external approaches. Annotations are a feature of the Java language, which became official on version 1.5. The code on Listing 1 presents a simple ```Player``` class using code annotation to perform object-relational mapping.

```java
@Entity
@Table(name="Players")
public class Player {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "health")
    private float health;
    
    @Column(name = "name")
    private String name;
  
    //getters and setters omitted
}
```
Listing 1: Code Annotations Example

To map this ```Player``` class to a table in a database, to store the player's information, we need to pass in some `extra information` about these code elements. In other words, we need to define an object-relational mapping, and we need to configure which elements should be mapped to a column, table, and so forth. Using code annotations provided by the JPA API, this mapping is easily achieved. When this code gets executed, the framework consuming the annotations knows how to perform the expected behavior. 

# Annotation Metrics

Our work in [@LIMA2018] proposed a novel suite of software metrics dedicated to code annotations. In this section we briefly describe them and demonstrate how they are calculated. We have three categories of metrics:

- Class Metric: Outputs one value per class.
- Code Element Metric: Outputs one value per code element (fields, methods, etc.).
- Annotation Metric: Outputs one value per code annotation.

The code presented on Listing 2 will be used as an example. 

```java
import javax.persistence.AssociationOverrides;
import javax.persistence.AssociationOverride;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.DiscriminatorColumn;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

@AssociationOverrides(value = {
      @AssociationOverride(name="ex",
         joinColumns = @JoinColumn(name="EX_ID")),
      @AssociationOverride(name="other",
         joinColumns = @JoinColumn(name="O_ID"))})
@NamedQuery(name="findByName",
      query="SELECT c " +
            "FROM Country c " + 
            "WHERE c.name = :name")
@Stateless
public class Example {...

   @TransactionAttribute(SUPPORTS)
   @DiscriminatorColumn(name = "type", discriminatorType = STRING)
   public String exampleMethodA(){...}

   @TransactionAttribute(SUPPORTS)
   public String exampleMethodB(){...}

}
```
Listing 2: Example code to extract annotation metrics.

- Annotations in Class (AC): This metric counts the number of annotations declared on all code elements in a class, including nested annotations. In our example code, the value of AC is equal to 10. It is a ```Class Metric```.

- Unique Annotations in Class (UAC): While AC counts all annotations, even repeated ones, UAC counts only distinct annotations. Two annotations are equal if they have the same name, and all arguments match. For instance, both annotations \texttt{@AssociationOverride} are different, for they have a nested annotation \texttt{@JoinColumn} that have different arguments. The first is \texttt{EX\_ID} while the latter is \texttt{O\_ID}. Hence they are distinct annotations and will be computed separately. The UAC value for the example class is nine. Note that the annotaiton \texttt{@TransactionAttribute()} is counted only once. It is a ```Class Metric```.

- Annotations Schemas in Class (ASC): An annotation schema represents a set of related annotations provided by a framework or tool. This measures how coupled a class is to a framework since different schemas on a class imply the class is using different frameworks. This value is obtained by tracking the imports used for the annotations. On the example code, the ASC value is two. The import \texttt{javax.persistence} is a schema provided by the JPA, and the import \texttt{javax.ejb} is provided by EJB. It is a ```Class Metric```.

- Arguments in Annotations (AA): Annotations may contain arguments. They can be a string, integer, or even another annotation. The AA metric counts the number of arguments contained in the annotation. For each annotation in the class, an AA value will be generated. For example, the \texttt{@AssociationOverrides} has only one argument named \texttt{value}, so the AA value is equal one. But \texttt{@AssociationOverride}, contains two arguments, \texttt{name} and \texttt{joinColumns}, so the AA value is two. It is an ```Annotation Metric```.

- Annotations in Element Declaration (AED): The AED metric counts how many annotations are declared in each code element, including nested annotations. In the example code, the method \texttt{exampleMethodA} has an AED value of two, it has the \texttt{@TransactionAttribute} and \texttt{@DiscriminatorColumn}. It is a ```Code Element Metric```.

- Annotation Nesting Level (ANL): Annotations can have other annotations as arguments, which translates into nested annotations. ANL measures how deep an annotation is nested. The root level is considered value zero. The annotations \texttt{@Stateless} has ANL value of zero, while \texttt{@JoinColumn} has ANL equals two. This data is because it has \texttt{@AssociationOverride} as a first level, and then the \texttt{@AssociationOverrides} adds another nesting level, hence the value ANL is two. It is an ```Annotation Metric```.

- LOC in Annotation Declaration (LOCAD): LOC (Line of Code), is a well-known metric that counts the number of code lines. The LOCAD is proposed as a variant of LOC that counts the number of lines used in an annotation declaration. \texttt{@AssociationOverrides} has a LOCAD value of five, while \texttt{@NamedQuery} has LOCAD equals four. It is an ```Annotation Metric```.

# Annotation Sniffer 

The ASniffer tool uses the JDT[^1](Java Development Tools) API to build the Abstract Syntax Tree (AST) from text file contaning the source code. The ASniffer traverses this AST, visiting the nodes and gathering information about the code elements. After the processing is done, an ouput XML is generated. 

[^1]: \url{https://www.eclipse.org/jdt/} 

To create the AST (Abstract Syntax Tree), we use the method \texttt{ASTParser.createASTs}. This method is exposed by the JDT and receives an array of strings containing the file path of each and every source code that we wish to analyze. Another parameter for the method is a class that will handle the compilation units. Our class is the \texttt{MetricsExecutor} and this class must extend the \texttt{FileASTRequestor}. From inside \texttt{MetricsExecutor} we call every metric class and pass in the compilation unit (generated by the \texttt{ASTParser}).

To understand the extraction process, we will use a snippet from the code that collects the `Annotations in Class` metric, presented on Listing 3. Since this is a ```Class Metric```, i.e, outputs one value per class, it must extend the \texttt{ASTVisitor} class and implement our custom interface ```IClassMetricCollector```. The superclass provides methods that are used to visit the nodes from the Compilation Unit. For instance, for the AC metric we visit every annotation encountered, and simply increment the value for ```annotations```. Our custom interface provides two methods, the first one, (\texttt{execute()}), initializes the extraction process, while the second one, (\texttt{setResult()}), is where the result is stored. 

```java
@ClassMetric
public class AC extends ASTVisitor implements IClassMetricCollector{
  //We also visist MarkerAnnotation and SingleMemberAnnotation
	private int annotations = 0;
	@Override
	public boolean visit(NormalAnnotation node) { 
		annotations++;
		return super.visit(node);
	}
	@Override
	public void execute(CompilationUnit cu, MetricResult result, AMReport report) {
		cu.accept(this);
	}
	@Override
	public void setResult(MetricResult result) {
		result.addClassMetric("AC",annotations);
	}
}
```
Listing 3: Snippet from the code that implements the Annotations in Class metric 

Following is the command line to run the ASniffer:

```java -jar asniffer.jar <path to project> <path to xml report> <single/multi>}```

- The third parameter tells the ASniffer whether the <path to project> contains only one or several java projects.

# License 
Annotation Sniffer is licensed under the GNU Lesser General Public License v3.0

# Acknowledgements
This work is supported by FAPESP (Fundação de Amparo à Pequisa do Estado de São Paulo), grant 2014/16236-6 and CAPES (Coordenação de Aperfeiçoamento de Pessoal de Nível Superior )

# References
