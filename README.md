# FEUP-SDIS2

Inside SDIS 2 Folder

javac -d out -sourcepath src src/peer/Peer.java src/testapp/TestApp.java

Initialize Peer

java -classpath out peer.Peer 1.0 1 45000

Call a Backup Service

java -classpath out testapp.TestApp 1 BACKUP test.txt 1
