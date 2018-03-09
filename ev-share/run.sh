#!/bin/bash

MAVEN_OPTS="" mvn integration-test -Pamp-to-war -Ddependency.surf.version=6.3
