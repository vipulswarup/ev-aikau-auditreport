@echo off

set MAVEN_OPTS=-Xms256m -Xmx2G

mvn integration-test -Pamp-to-war -nsu
:: mvn integration-test -Pamp-to-war 
