#!/bin/bash
cd com/simulator/
javac -cp . ServerNetwork.java LogGenerator.java GenerateLogs.java 
cd ../../
java com.simulator.GenerateLogs $1
