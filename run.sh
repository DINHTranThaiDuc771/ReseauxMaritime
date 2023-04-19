#!/bin/bash

javac -encoding utf8 "@compile.list"

if [ -z "$1" ]; then
    echo "Missing argument. Usage: $0 [file path]"
    echo "Read ./testData/testMoves.csv by default..."
    java -cp ./bin Model.Model "./testData/testMoves.csv"
    exit
fi

if [ ! -f "$1" ]; then
    echo "Error: File not found: $1"
    exit
fi

java -cp ./bin Model.Model "$1"

echo "Fin de l'execution."
