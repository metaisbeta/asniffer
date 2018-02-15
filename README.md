Annotation Sniffer
==================

Annotation Sniffer is a tool that extracts code annotation metrics from java source code. 

### How to download

Download the source code and generate an executable jar file.

### How to use

```
java -jar asniffer.jar <path to project> <path to xml report>
```

The path must be a root folder that contains other projects directories. Even if only one project will be used in the extraction. 
Follow the directory arrangement below.

    .
    ├── projects                # Root directory for projects. This is the path to be provided
        ├── project1            # Contains the source files for project1
        ├── project2            # Contains the source files for project2
        └── ...         

For each project an xml output is generated with the report. The reports will be placed on the provided "path to xml".

Annotation Metrics
==================

The Annotations Sniffer was developed to aid research in code annotations analysis. It collects 7 annotation metrics. These metrics were proposed and defined in the the paper [A Metrics Suite for Code Annoation Assessment](https://www.sciencedirect.com/science/article/pii/S016412121730273X)

### Collected metrics

* AC: Annotations in Class
* UAC: Unique Annotations in Class
* ASC: Annotation Schema in Class
* AED: Annotation in Element Declaration
* AA: Attributes in Annotation
* ANL: Annotation Nesting Level
* LOCAD: LOC in Annotation Declaration

### XML Output Format

* Class Metrics: These metrics have one value per class, they are AC, UAC, ASC
* Element Metrics: These metrics have multiple values per class. They evaluate the annotation itself (AA, LOCAD, ANL) or a code element (AED).
                   The report contains the element name, element type (field, method, etc), the source code line where the element is located and the metric value.
                   For metrics measuring code annotations itself (AA,ANL,LOCAD), the element type is "annotation", and therefore not explicitly informed on the report

One XML file is generated for each project, containing both class metrics and element metrics in the report. 
```
<project name="project1">
    <class name="className">
        <class-metric>
            <metric>
                <metric name="AC">VALUE</metric>
                ...
            </metric>
        </class-metric>
        <element-metric>
            <metric name="AA">
                <elements name="name" code-line="code-line">value</elements>
                ...
            </metric>
            <metric name="AED">
                <elements name="name" type="type" code-line="code-line">VALUE</elements>
            ...
       </element-metric>
    </class>
    ...
</project>
```

### Creating new Metric for Annotation Sniffer

The Annotation Sniffer uses Reflection to know which metrics it should collect. If you wish to use Annotation Sniffer on your project and create custom metrics, follow these steps:

* Your new metric class must extend ASTVisitor, implement MetricCollector and be annotated with @AnnotationMetric
* The MetricCollector interface contains two methods: execute(CompilationUnity, MetricResult, AMReport) and setResult(MetricResult). The MetricResult class is where you want to store your value.
  * Class Metric: If your metric outputs only one value per class, then, in this context, it is a class metric. The result must be store using addClassMetric(string metricName, integer metricValue).
  * Element Metric: If your metric measures elements, then it outputs more than one value per class, therefore it is a element metric. The result must be store using addElementMetric(string name, List<ElementMetric> elementMetric)
  The ElementMetric class is a JavaBean with properties such as metricValue, type, source-code line and element name.
  
  Check the metrics included in the package br.inpe.cap.asniffer.metric for more information.
