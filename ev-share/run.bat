
@echo off

mvn integration-test -Pamp-to-war -nsu -Ddependency.surf.version=6.3
:: mvn integration-test -Pamp-to-war 
