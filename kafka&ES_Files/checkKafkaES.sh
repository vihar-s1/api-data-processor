tools="elasticsearch kibana zookeeper kafka"

for tool in $tools;
do
  if [ -n "$(pgrep -f "$tool")" ]; then
    echo "${tool} is running"
  else
    echo "${tool} is not running"
  fi
done