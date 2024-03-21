#!/bin/bash

pkill_steps() {
  tools="kibana elasticsearch kafka zookeeper"
  for tool in $tools; do
    echo "force stopping $tool..."
    pkill -9 -f "$tool"
  done
}

simple_steps() {
  # Stop kibana if running
  KIBANA_PID=$(pgrep -f "kibana")
  while [ -n "$KIBANA_PID" ]
  do
    echo "stopping kibana..."
    for pid in $KIBANA_PID;
    do
      kill -9 "$pid" &
      wait $!
    done
    KIBANA_PID=$(pgrep -f "kibana")
  done

  # Stop elasticsearch server if running
  ES_PID=$(pgrep -f "elasticsearch")
  curl -X DELETE "http://localhost:9200/.kibana_7.17.4_001" & # Deleting kibana index in case it was running
  wait $!
  while [ -n "$ES_PID" ]
  do
    echo "stopping elasticsearch..."
    for pid in $ES_PID; do
      kill -9 "$pid" &
      wait $!
    done
    ES_PID=$(pgrep -f "elasticsearch")
  done


  # Stop Zookeeper if running
  while [ "$(pgrep -f "zookeeper" | wc -l)" -ne 0 ]
  do
      echo "Stopping ZooKeeper Server..."
      # Start ZooKeeper (replace with your actual ZooKeeper start command)
      zkServer stop  &
      wait $!
  done


  # Stop Kafka Server if running
  while [ "$(pgrep -f kafka | wc -l)" -ne 0 ]
  do
      echo "stopping kafka server..."
      kafka-server-stop
      wait $!
  done
}

if [ "$1" == "-f" ] || [ "$1" == "--force" ]; then
  pkill_steps
else
  simple_steps
fi