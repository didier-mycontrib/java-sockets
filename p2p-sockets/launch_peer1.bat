set JAVA_HOME=C:\Program Files\Java\jdk-21
set PATH=%JAVA_HOME%\bin;%PATH%
set CP=target/p2p-sockets-0.0.1-SNAPSHOT.jar
REM java -cp %CP% tp.PeerTCP peer_1
java -cp %CP% tp.PeerUDP peer_1