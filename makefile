JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        ServidorCentral.java \
        Cliente.java \
        Distrito.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

