#!/bin/bash

folder="./data"

for file in "$folder"/*; do
    filename=$(basename "$file")
    anotherfile="./dataUniq/${filename}"
    cat "$file" | sort | uniq > "$anotherfile"
done
#"Command substitution" in Bash is denoted by $(command) or `command` (backticks)