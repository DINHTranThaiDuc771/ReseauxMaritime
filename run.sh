#!/bin/bash

javac -encoding utf8 @compile.list

echo "Lancement du programme..."
java -cp ./bin Model.Model

echo "Fin de l'execution."