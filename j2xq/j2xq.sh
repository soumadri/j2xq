echo Compiling interfaces...
javac -cp "dist/j2xq.jar;lib/marklogic-xcc-5.0.2.jar" $1
echo Generating stubs and implementation classes...
java -jar dist/j2xq.jar