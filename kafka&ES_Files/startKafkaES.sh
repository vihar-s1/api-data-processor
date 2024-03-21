#!/bin/bash

# Check if ZooKeeper is running
# pgrep is used to find the process IDs of processes matching  the filter.
# -f means to compare against all arguments as well and not just process name.
# wc -l returns the number of lines returned. 0 means no process found.
ZK_STATUS=$(pgrep -f "org.apache.zookeeper.server.quorum.QuorumPeerMain" | wc -l)

if [ "$ZK_STATUS" -eq 0 ]; then
    echo "ZooKeeper is not running. Starting ZooKeeper..."
    # Start ZooKeeper (replace with your actual ZooKeeper start command)
    zkServer start
else
    echo "ZooKeeper already running..."
fi


# Start Kafka server with KafkaServer.properties
# Will match kafka and zookeeper
if [ "$(pgrep -f "kafka" | wc -l)" -eq 0 ]; then
    echo "Starting Kafka Server..."
    kafka-server-start KafkaServer.properties > kafka.log 2>&1 &
    echo "Kafka server running on pid: $!"
else
  echo "kafka already running"
  pgrep -f "kafka" | wc -l
fi


# Start elasticsearch in daemon process
if [ "$(pgrep -f "elasticsearch" | wc -l)" -eq 0 ]; then
    echo "Starting elasticsearch..."
    elasticsearch --daemonize
    echo  "elasticsearch running on pid: $(pgrep -f elasticsearch)"
fi


# If kibana is not running then, first remove the index corresponding kibana and then start it
if [ "$(pgrep -f "kibana" | wc -l)" -eq 0 ]; then
  echo "Starting kibana"
  curl -X DELETE "localhost:9200/.kibana_7.17.4_001" &
  wait $!
  kibana > kibana.log 2>&1 &
  echo "kibana running on pid: $(pgrep -f kibana)"
fi