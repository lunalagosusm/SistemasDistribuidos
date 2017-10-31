JC = javac
JVM= java 
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        ServidorCentral.java \
        Cliente.java \
        Distrito.java \

MAIN = ServidorCentral
		#Cliente
		#Distrito
		
default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)

clean:
	$(RM) *.class