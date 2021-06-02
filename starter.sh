pgrep -f "$1".jar

# shellcheck disable=SC2181
if [ $? != 0 ]
then
  echo "No process with name $1 found"
else
  echo "Kill running process"
  # shellcheck disable=SC2046
  kill -9 $(pgrep -f "$1".jar)
fi

rm *log*

nohup java -jar -XX:+PrintGCDetails -Xloggc:gc.log -Xms128m -Xmx256m "$1".jar > "$1".log &
