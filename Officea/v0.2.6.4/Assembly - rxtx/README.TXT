========================
RXTX Assembly
========================
The RXTX assembly holds needed components for RXTX usage inside jbaxi.jar.
But please notice that you still need to do necessary installation of RXTX in your jre. 

When using the jbaxi.jar assembly with RXTX Java needs to know where the native parts of the RXTX are. 
On Linux this is the file librxtxSerial.so and on Windows rxtxSerial.dll. 
One way is to put it in an application subfolder named “lib” and starting the application 
with -Djava.library.path=lib. Furthermore just add the file “jbaxi.jar” to the 
java classpath and to set the “baxi.ini” file to hold the right settings and it should work.

Driver Windows32=rxtxSerial.dll
Driver Linux32=librxtxSerial.so

A start up .sh-file example on Linux could look like this:
java -classpath .:jbaxi-gui.jar:lib/* -Djava.library.path=lib eu.nets.baxi.gui.JbaxiFrame

