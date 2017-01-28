#!/usr/bin/env bash

case "$(uname)" in
"Darwin")
  FLAGS="-XstartOnFirstThread"
  PROFILE="macosx"
  ;;
"Linux")
  FLAGS=""
  PROFILE="linux"
  ;;
"MINGW32_NT") # Cygwin
  FLAGS=""
  PROFILE="win32"
  ;;
*)
  FLAGS=""
  PROFILE="linux"
  ;;
esac

JAR="target/sophie-*-$PROFILE-with-dependencies.jar"

if [ ! -f $JAR ]; then
  echo "Unable to find file $JAR"
  echo "Did you compile the project ? Maybe try: mvn clean install -P $PROFILE"
  exit 1
fi

java $FLAGS -jar $JAR
