# vex-smart-scan
Intelligent Android app for Vex Robotics Competition scouting. 

This repository consists of three parts - the smart-scan backend, the smart-scan-server, and smart-scan-app. 

The backend is compiled to a Java .jar which takes a tournament SKU as a command line argument upon execution.
After doing its calculations and making its requests to the VexDB API, the jar returns a JSON list of teams and their associated scores. 

The Node.js server takes this JSON data and returns it to a client (usually the Flutter app built for this project) upon receiving a
GET request from the client. It also caches it for 20 minutes in a text file before deleting it, minimizing the latency between
when the request is sent and when the client gets the result. 
