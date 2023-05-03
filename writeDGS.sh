javac -encoding utf8 "@compile.list"
./getDate.sh "./testData/testDGS.csv"
java -cp ./bin Executable.WriteDGSFile
