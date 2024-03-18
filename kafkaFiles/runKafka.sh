#!/bin/bash

# Check if ZooKeeper is running
# 1. ps aux --> grab all running processes
# 2. grep -v grep --> ignore entries matching the pattern "grep" to avoid the grep command matching the command 'grep -c "org...Main"'
# 3. grep -c "org.apa...Main" --> return count of entries matching given filter
ZK_STATUS=$(ps aux | grep -v grep | grep -c "org.apache.zookeeper.server.quorum.QuorumPeerMain")

if [ "$ZK_STATUS" -eq 0 ]; then
    echo "ZooKeeper is not running. Starting ZooKeeper..."
    # Start ZooKeeper (replace with your actual ZooKeeper start command)
    zkServer start
else
    echo "Restarting ZooKeeper Server..."
    zkServer restart
fi

# Start Kafka server with KafkaServer.properties
echo "Starting Kafka Server..."
kafka-server-start KafkaServer.properties
