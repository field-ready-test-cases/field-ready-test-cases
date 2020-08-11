#!/bin/bash
# configurable
source config.conf
# assuming standard locations
commonsrc=../common/src
noncommonsrc=../field-tests/commons-lang/
commonconfig=../common/commons-lang/
bugIds=($(ls ../field-tests/commons-lang/))
projectVersions=(buggy)
projectShortVersions=(b)
depdir=../common/deps
log_dir=$(readlink -f $workspacedir)/lang_logs
echo "Log data from experiments will be written to $log_dir" 
workspacedir=$workspacedir/lang/
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
    echo "Checking out $lv commons-lang for bug id $bugId"
    defects4j checkout -p Lang -v "$bugId$sv" -w "$workspacedir"/"$bugId"_"$lv"
    cp "$commonconfig"/build.properties "$workspacedir"/"$bugId"_"$lv"
    cp "$commonconfig"/build.xml  "$workspacedir"/"$bugId"_"$lv"
    if [[ -d "$workspacedir"/"$bugId"_"$lv"/src/main ]]
    then
      main=main
      cp "$commonconfig"/default.properties "$workspacedir"/"$bugId"_"$lv"
    else
      main=    
      cp "$commonconfig"/old_default.properties "$workspacedir"/"$bugId"_"$lv"/default.properties
    fi 
    cp -r $commonsrc/fieldtest "$workspacedir"/"$bugId"_"$lv"/src/$main/java/
    cp -r $noncommonsrc/"$bugId"b/fieldtest/* "$workspacedir"/"$bugId"_"$lv"/src/$main/java/fieldtest
    if [[ -d "$workspacedir"/"$bugId"_"$lv"/src/java/org/apache/commons/lang/enum ]]
    then
      rm -r "$workspacedir"/"$bugId"_"$lv"/src/java/org/apache/commons/lang/enum
      rm -r "$workspacedir"/"$bugId"_"$lv"/src/java/org/apache/commons/lang/enums
    fi
    echo "field-test-id = lang-""$bugId$sv" >> "$workspacedir"/"$bugId"_"$lv"/ft.properties
    echo "log-directory = $log_dir" >> "$workspacedir"/"$bugId"_"$lv"/ft.properties
  done
done
