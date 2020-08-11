#!/bin/bash
bugIds=($(ls ../field-tests/commons-lang/))
#configurable variables
source config.conf

workspacedir=$workspacedir/lang/

for bugId in ${bugIds[@]}; do
  bugId=$(echo $bugId | cut -d'b' -f 1)
  if [ "$exclude_run_bugId43" == true ] && [ "$bugId" == "43" ]; then
    continue
  fi
  echo "Running field test experiments on buggy version $bugId"
  pushd $workspacedir/"$bugId"_buggy 
    ant run-field-trigger
  popd
  if [ "$include_fixed_version" = true ] ; then
    echo "Running field test experiments on fixed version $bugId"  
    pushd $workspacedir/"$bugId"_fixed
      ant run-field-trigger
    popd 
  fi
done
