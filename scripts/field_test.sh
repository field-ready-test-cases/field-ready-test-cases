#!/bin/bash
if [[ $# != 2 ]]
  then 
  echo "Usage: field_test.sh (init|run) (Lang|Chart)"
  exit
fi
if [ $1 = "run" -a $2 = "Lang" ]
  then
  echo "Going to run field-testing experiments for Apache Commons Lang"
  source run_fieldtests_commons.sh
elif [ $1 = "run" -a $2 = "Chart" ]
  then
  echo "Going to run field-testing experiments for JFreeChart"
  source run_fieldtests_chart.sh
elif [ $1 = "init" -a $2 = "Lang" ]
  then
  echo "Going to initialize field-testing experiments for Apache Commons Lang"
  source checkout_and_initialize_commons.sh
elif [ $1 = "init" -a $2 = "Chart" ]
  then
  echo "Going to initialize field-testing experiments for JFreeChart"
  source checkout_and_initialize_chart.sh
else
  echo "Usage: field_test.sh (init|run) (Lang|Chart)"
fi
