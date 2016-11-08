#!/bin/bash
cd com/parser/
javac LogParser.java 
cd ../../
java com.parser.LogParser $1
