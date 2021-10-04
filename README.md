# Field-Ready Test Cases
This repository contains the source code needed to replicate the experiments 
discussed in the paper "Testing Software in Production Environments with Data 
from the Field". Additionally, it also contains the raw experimental results
presented in the paper. Note that for general information on our approach to 
field testing, we refer to our paper.

## Experiments
In the following, we first provide a guide on how to run the field-testing 
experiments. After that, we provide a brief overview of the repository structure 
focussing on the files required by the field-testing experiments. This 
information is not required to run the experiments, though. 

### Performing the Experiments
The following guide explains how to run the field-testing experiments. 

#### Requirements
The guide assumes a Unix-based operating system with GNU Bash. The experiments 
have a few requirements which are:
1. Defects4j,
2. a JDK, 
3. Apache Ant for compiling, 
4. and Apache Maven for dependencies

For installation instruction of Defects4j, we refer to 
<https://github.com/rjust/defects4j>. Make sure that `defects4j` is 
available from the command line by trying the command 
`defects4j info -p Chart -b 2` which should print information about the second
bug of JFreeChart. The JDK version used for the experiments should be at least 8, 
which is required by Defects4j. We performed the experiments with Apache
Ant 1.10.3 and Apache Maven 3.5.2, but older versions should be sufficient as 
well. Finally, we used Ubuntu Linux 18.04. as operating system. 

#### Actually Running the Experiments
To ease running the experiments, we created a few Bash scripts in the directory
`scripts`. The directory also contains a configuration file called `config.conf`,
which is initialized with some standard settings. 

You can run the field-testing experiments either for Apache Commons Lang or JFreeChart.
In both cases you need to execute the script `scripts/field_test.sh` twice. 
For Apache Commons Lang:
1. First run `scripts/field_test.sh init Lang`. 

This will download various versions
of Apache Commons Lang into the directory `experiments/lang` (unless otherwise specified).
It will also copy our field-testing source code into the project directories and 
it will adapt build files accordingly. This process may take a few minutes.
2. Once the process has finished, run `scripts/field_test.sh run Lang`. 

This will simulate all downloaded project versions to perform field-testing 
experiments. It will also print debug information on the console and it will 
store experimental results in the directory `experiments/lang_logs/` by default. 
For more information on the results, we refer to the readme in the directory 
`results` that includes the results presented in the paper. Running the 
experiments may take between a few minutes and an hour.

To perform experiments for JFreeChart, substitute `Lang` by `Chart` in the 
above commands, i.e., execute `scripts/field_test.sh init Chart` followed 
`scripts/field_test.sh run Chart`.

The different project versions and the result files have a combined size of 
about 1.5 Gigabyte.

#### Configuration 
The file `config.conf` includes three variables controlling the experiments. 
The variable `workspacedir` defines the directory where the project versions are
stored and the experiments are run. By default, this is 
`<rootdir_of_repo>/experiments`. The variable `exclude_run_bugId43` affects the
experiments for Apache Commons Lang. If it is set to true, the experiments for 
the bug with ID 43 will not be performed. This is the default setting, because the 
corresponding experiments take very long due to Bug 43 causing the 
application under test to run out of memory. Finally, the last variable is 
`include_fixed_version`. Defects4j contains a fixed program version and a faulty
program version for each bug ID. If `include_fixed_version` is set to `true`, 
experiments are performed for both the faulty and the fixed version, otherwise 
only for the faulty one. The variable is by default set to `false`.

### Repository Structure
The source code required by the experiments resides in the following locations.
* `common/src` includes source code that is used by all field-testing 
experiments. This includes the implementation of triggers and some code to 
simulate the applications under test.
* `common/commons-lang` and `common/JFreeChart` includes build files and 
properties files that are used by experiments with Apache Commons Lang and 
JFreeChart, respectively. 
* `field-tests/commons-lang` and `field-tests/JFreeChart` includes field-testing
code and simulation code for each revealed fault in Apache Commons Lang and 
JFreeChart, respectively.
* `scripts` contains some Bash scripts to run experiments, as discussed above.

## Results
The results reported in the paper can be found in the directory `results`. 
Navigate to this directory for more information (see `results/README.md`). 
