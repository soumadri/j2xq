@ECHO OFF
echo Compiling interaces...
javac -cp "j2xq.jar;marklogic-xcc-5.0.2.jar" %1
echo Generating stubs and implementation classes...
java -jar j2xq.jar