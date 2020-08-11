# Evaluation Results for Triggering Mechanisms

The directories `chart_logs` and `lang_logs` contain the raw results of the evaluation experiments of the triggering mechanisms. These results are given in XML format which aims to be self-explanatory. They are the basis of the evaluation summary presented in the paper. 
There are two XML files for each combination of triggering configuration and Bug ID. One for the faulty project version corresponding to a Bug ID, denoted by the file suffix 'b.xml',  and one for the fixed project version, denoted by 'f.xml'. The paper focuses on bug detection and therefore considers only the results for the faulty project versions.

Rerunning the field testing experiments will create the same files. The actual results will be similar but not the same due to randomness. Additionally, the experiments also produce log files that are not included in this repository due to their large combined size. Due to file size, some exception details have been deleted from the results file for Bug 17 of JFreeChart in the faulty project version. 

## Computing a Summary of the Results
The repository includes a small application that provides summaries of evaluation results. For this purpose, it reads XML files containing the 
results and computes summaries from them. The application source code can be found in the location `<repository_root>/common/src/helper`.
You can compile the application using the ant build file `<repository_root>/common/summary/build.xml`. Run the ant task `compute-summary`, i.e., 
execute `ant compute-summary` in `<repository_root>/common/summary`, to execute the application. This will print a summary to the console.
By default, the application will read the results in the subdirectories of this directory. You can override this behavior by changing Line 41 
in the build file which is `<sysproperty key="experimentloc" value="${basedir}/../../results"/>`. The value of the system property `experimentloc` 
should point to a location storing the results of field-testing experiments. 
Run `ant clean` to delete application.
