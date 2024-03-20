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
    echo "Restarting ZooKeeper Server..."
    zkServer restart
fi


# Start Kafka server with KafkaServer.properties
# Will match kafka and zookeeper
if [ "$(pgrep -f "kafka" | wc -l)" -eq 0 ]; then
    echo "Starting Kafka Server..."
    kafka-server-start KafkaServer.properties > kafka.log &
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