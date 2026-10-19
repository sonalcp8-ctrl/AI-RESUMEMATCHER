#!/bin/sh
mkdir -p out
find src -name '*.java' > sources.txt
javac -encoding UTF-8 -d out @sources.txt && rm sources.txt && echo "Build successful. Now run ./run.sh"
