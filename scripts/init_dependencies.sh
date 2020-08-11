# should only be called from init scripts
if [[ ! -d $workspacedir/common/dependency ]] 
  then
  echo "Common initialization - copy dependencies"
  mkdir -p $workspacedir
  mkdir -p $workspacedir/common/dependency
  pushd ../common/deps
  mvn dependency:copy-dependencies
  popd
  cp $depdir/target/dependency/* $workspacedir/common/dependency
  echo "Finished copying dependencies"
fi