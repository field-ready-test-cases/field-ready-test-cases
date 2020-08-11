#!/bin/bash
# configurable
source config.conf
# assuming standard locations
commonsrc=../common/src
noncommonsrc=../field-tests/JFreeChart/
commonconfig=../common/JFreeChart/
bugIds=($(ls ../field-tests/JFreeChart/))
projectVersions=(buggy)
projectShortVersions=(b)
depdir=../common/deps
log_dir=$(readlink -f $workspacedir)/chart_logs
echo "Log data from experiments will be written to $log_dir" 
workspacedir=$workspacedir/chart/
echo "Project will be copied into $workspacedir" 

if [ "$include_fixed_version" = true ] ; then
    projectVersions+=(fixed)
    projectShortVersions+=(f)
fi

source init_dependencies.sh

for bugId in ${bugIds[@]}; do
  bugId=$(echo $bugId | cut -d'b' -f 1)
  for (( versionI=0; versionI<${#projectVersions[@]}; versionI++ ));
  do
    lv=${projectVersions[$versionI]}
    sv=${projectShortVersions[$versionI]}
    echo "Checking out $lv JFreeChart for bug id $bugId"
    defects4j checkout -p Chart -v "$bugId""$sv" -w "$workspacedir"/"$bugId"_"$lv"
    cp $commonconfig/ft.properties "$workspacedir"/"$bugId"_"$lv"/ant
    echo "field-test-id = chart-""$bugId""$sv" >> "$workspacedir"/"$bugId"_"$lv"/ant/ft.properties
    echo "log-directory = $log_dir" >> "$workspacedir"/"$bugId"_"$lv"/ant/ft.properties

    cp $commonconfig/build.xml  "$workspacedir"/"$bugId"_"$lv"/ant
    cp -r $commonsrc/fieldtest "$workspacedir"/"$bugId"_"$lv"/source/
    cp -r $noncommonsrc/"$bugId"b/fieldtest/* "$workspacedir"/"$bugId"_"$lv"/source/fieldtest
  done
done
