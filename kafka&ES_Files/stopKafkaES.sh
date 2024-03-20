#!/bin/bash

pkill_steps() {
  echo "force killing elasticsearch..."
  pkill -15 -f elasticsearch
  echo "force killing kafka..."
  pkill -15 -f kafka
  echo "force killing zookeeper..."
  pkill -15 -f zookeeper
}

simple_steps() {
  # Stop elasticsearch server if running
  ES_PID=$(pgrep -f "elasticsearch")
  if [ -n "$ES_PID" ]; then
    echo "stopping elasticsearch..."
    for pid in $ES_PID; do
      kill -15 "$pid"
    done
  fi


  # Stop Kafka Server if running
  if [ "$(pgrep -f kafka | wc -l)" -ne 0 ]; then
      echo "stopping kafka server..."
      kafka-server-stop
  fi


  # Stop Zookeeper if running
  ZK_STATUS=$(pgrep -f "zookeeper" | wc -l)

  if [ "$ZK_STATUS" -ne 0 ]; then
      echo "Stopping ZooKeeper Server..."
      # Start ZooKeeper (replace with your actual ZooKeeper start command)
      zkServer stop
  fi
}

if [ "$1" == "-f" ] || [ "$1" == "--force" ]; then
  pkill_steps
else
  simple_steps
fi