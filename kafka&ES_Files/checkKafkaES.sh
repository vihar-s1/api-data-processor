#!/bin/bash

# Define tools and their corresponding processes
tools=(
    "elasticsearch:elasticsearch"
    "kibana:kibana"
    "zookeeper:org.apache.zookeeper"
    "kafka:kafka"
)

# Iterate over the tools
for tool in "${tools[@]}"; do
    # Split the tool entry into name and process
    IFS=':' read -r tool_name tool_process <<< "$tool"

    if [ "$(pgrep -f "$tool_process" | wc -l)" -gt 0 ]; then
        echo "$tool_name is running"
    else
        echo "$tool_name is not running"
    fi
done
