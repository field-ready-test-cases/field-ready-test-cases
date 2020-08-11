#!/bin/bash
bugIds=($(ls ../field-tests/JFreeChart/))
#configurable variables
source config.conf
workspacedir=$workspacedir/chart/

for bugId in ${bugIds[@]}; do
  bugId=$(echo $bugId | cut -d'b' -f 1)
  echo "Running field test experiments on buggy version $bugId"
  pushd $workspacedir/"$bugId"_buggy/ant 
    ant run-field-trigger
  popd
  if [ "$include_fixed_version" = true ] ; then
    echo "Running field test experiments on fixed version $bugId"  
    pushd $workspacedir/"$bugId"_fixed/ant
      ant run-field-trigger
    popd 
  fi
done
