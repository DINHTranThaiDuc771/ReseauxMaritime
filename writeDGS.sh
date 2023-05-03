javac -encoding utf8 "@compile.list"
./getDate.sh "./testData/testDGS.csv"
if [ -f "graphDynamic.dgs" ]; then
    echo "OverWrite..."
    rm -f graphDynamic.dgs
fi
java -cp ./bin Executable.WriteDGSFile
