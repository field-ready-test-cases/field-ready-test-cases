# Evaluation Results for Triggering Mechanisms

The directories `chart_logs` and `lang_logs` contain the raw results of the evaluation experiments of the triggering mechanisms. These results are given in xml format which aims to be self-explanatory. They are the basis of the evaluation summary presented in the paper. 
There are two xml files for each combination of triggering configuration and Bug ID. One for the buggy project version corresponding to a Bug ID, denoted by the file suffix 'b.xml',  and one for the fixed project version, denoted by 'f.xml'. The paper focuses on bug detection and therefore considers only the results for the buggy project versions.


Rerunning the field testing experiments will create the same files. The actual results will be similar but not the same due to randomness. Additionally, the experiments also produce log files that are not included in this repository due to the large combined size. Due to file size, some exception details have been deleted from the results file for Bug 17 of JFreeChart in the buggy project version. 