[![Build Status](https://travis-ci.com/phillima/asniffer.svg?branch=master)](https://travis-ci.com/phillima/asniffer)

Annotation Sniffer
==================

Annotation Sniffer is a tool that extracts code annotation metrics from java source code. 

### How to download

Download the source code and generate an executable jar file.

```
mvn install
mvn clean package
```
### How to use

```
java -jar asniffer.jar <path to project> <path to xml report> <single/multi>
```

The path must be a root folder that contains other projects directories, for the "multi" case. 
Follow the directory arrangement below.

    .
    ├── projects                # Root directory for projects. This is the path to be provided
        ├── project1            # Contains the source files for project1
        ├── project2            # Contains the source files for project2
        └── ...         

If the option "single" is used, then the path provided references only a single project, with all the source files inside that folder. 

    .
    ├── project                # Directory containing the source file for the project. This is the path provided
      

For each project an xml output is generated with the report. The reports will be placed on the provided "path to xml".

Annotation Metrics
==================

The Annotations Sniffer was developed to aid research in code annotations analysis. It collects 9 annotation metrics. These metrics were proposed and defined in the the paper [A Metrics Suite for Code Annoation Assessment](https://www.sciencedirect.com/science/article/pii/S016412121730273X)

### Collected metrics

* AC: Annotations in Class
* UAC: Unique Annotations in Class
* ASC: Annotation Schema in Class
* AED: Annotation in Element Declaration
* AA: Attributes in Annotation
* ANL: Annotation Nesting Level
* LOCAD: LOC in Annotation Declaration
* NEC: Number of Elements in Class
* NAEC: Number of Annotated Elements in Class

### XML Output Format

* Class Metrics: These metrics have one value per class, they are AC, UAC, ASC, NAEC and NEC
* Code Element Metrics: These metrics have one value per code element (method, field, enum, type). Our suite has one metric, AED (Annotations in Element Declaration), that measures the number of annotations declared in any given code element.
* Annotation Metrics: These metrics have one value values per annotation declared in the class. They evaluate the annotation itself (AA, LOCAD, ANL). 

* For each code element, the report contains the element name, type (field, method, enum, etc), the source code line where the element is located and "code element metric values" (for now, only AED fits this category)
* If the AED is greater than zero, then the code element contains annotations, and so the "annotation metrics" values printed on the XML. The report has the annotation name,source-code line and the values for AA, ANL and LOCAD.
                   
* In case of multiple projects, one XML file is generated for each one of them

* Following is an example of an XML report

```
<project name="project1">
    <package name="pacakge1">
        <class name="pacakge1.Class1" type="class">
            <schema>java.lang</schema>
            <schema>javax.persistence</schema>
            ...
            <metric name="LOC" value="50"/>
            <metric name="ASC" value="5"/>
            <metric name="AC" value="28"/>
            <metric name="NAEC" value="16"/>
            <metric name="UAC" value="18"/>
            <metric name="NEC" value="32"/>
            <code-elements>
                <code-element name="method1" type="method" code-line="20" aed="1"/>
                    <annotation name="Override" code-line="201" schema="java.lang">
                        <annotation-metrics>
                            <item metric="AA" value="0"/>
                            <item metric="LOCAD" value="1"/>
                            <item metric="ANL" value="0"/>
                        </annotation-metrics>
                    </annotation>
                </code-element>
            ...
            </codelements>
        ...
        </class>
    ...
    </package>
...
</project>  

```

### Creating a new Metric for Annotation Sniffer

The Annotation Sniffer uses Reflection to know which metrics it should collect. If you wish to use Annotation Sniffer on your project and create you owrn custom metrics, follow these steps:

* Class Metrics: If you wish to create your own Metric Class, your class must:
    - Extend ASTVisitor (to visit the compilation unit)
    - Implement the ```IClassMetricCollector```interface. It contains two methods, ```execute(CompilationUnit, MetricResult, AMReport)``` and ```setResult(MetricResult)```. The ```MetricResult``` class is where you want to store your value, as well as the name of your custom metric. Check the code for AC, ASC and UAC for examplee
    - Annotate the class with ```@ClassMetric```.


* If you wish to create new Annotation Metrics, then you need to:
    - Annotate the class with @AnnotationMetric.
    - Implement the interface ```IAnnotationMetricCollector```. This interface has only one method, ```execute(CompilationUnit, AnnotationMetricModel, Annotation)```. The ```AnnotionMetricModel``` class is where you will store the metric value and name. The ```Annotation``` class is the JDT (Java Development Tools) representation of the annotation that you can perform your analysis. Check the code for: ANL, AA and LOCAD for more examples.

* Check the metrics included in the package br.inpe.cap.asniffer.metric for more information.
