#!/bin/sh
play clean compile dist
scp dist/quizz-1.0-SNAPSHOT.zip pi@192.168.0.39:/home/pi
git push heroku master
