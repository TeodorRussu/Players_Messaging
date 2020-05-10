mvn clean package assembly:single

#program CL properties used for: players groups size, the initiator message limit to handle, first message text, stop message text
java -jar ./target/Players-1.0-SNAPSHOT-jar-with-dependencies.jar 4 33 hello "stop!"