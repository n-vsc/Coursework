#!/usr/bin/env bash

if [ $# != 1 ]
then
  echo "Script requires 1 argument (module name)"
  echo "Usage: $0 module_name"
  exit 1
fi

echo "Deploy module $1"

echo "Build project"

gradle "$1":clean --warning-mode=all
# shellcheck disable=SC2181
if [ $? != 0 ]
then
  echo "Clean failed! Exit..."
  exit 1
fi

gradle "$1":build --warning-mode=all
# shellcheck disable=SC2181
if [ $? != 0 ]
then
  echo "Build failed! Exit..."
  exit 1
fi

#--------------------------------------------------

echo "Copy files"

ssh lv_reg_all@35.217.7.122 << EOF

mkdir /home/lv_reg_all/"$1"

EOF

scp "$1"/build/libs/"$1".jar lv_reg_all@35.217.7.122:/home/lv_reg_all/"$1"/
# shellcheck disable=SC2181
if [ $? != 0 ]
then
  echo "Copy failed! Exit..."
  exit 1
fi

scp starter.sh lv_reg_all@35.217.7.122:/home/lv_reg_all/"$1"/
# shellcheck disable=SC2181
if [ $? != 0 ]
then
  echo "Copy failed! (starter.sh) Exit..."
  exit 1
fi

#--------------------------------------------------

echo "Restart application"

ssh lv_reg_all@35.217.7.122 << EOF

cd "$1"
chmod +x starter.sh
./starter.sh "$1"

EOF

echo 'Success'