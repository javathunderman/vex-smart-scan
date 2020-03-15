import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() {
  runApp(new MaterialApp(
      home: new HomePage()
  ));
}

class HomePage extends StatefulWidget {
  @override
  HomePageState createState() => new HomePageState();
}

class HomePageState extends State<HomePage> {

  List data;

  Future<String> getData() async {
    var response = await http.get(
        Uri.encodeFull("http://192.168.1.120:3000/users?sku=RE-VRC-18-7443"),
        headers: {
          "Accept": "application/json"
        }
    );

    this.setState(() {
      data = json.decode(response.body);
    });



    return "Success!";
  }

  @override
  void initState(){
    print("Running again");
    this.getData();
  }

  @override
  Widget build(BuildContext context){
    return new Scaffold(

      appBar: new AppBar(title: new Text("Smart Scan Rankings"), backgroundColor: Colors.blue),

      body:

      ListView.builder(
        itemCount: data == null ? 0 : data.length,
        itemBuilder: (BuildContext context, int index){
          return new Card(
            child: new Text.rich(
              TextSpan(
                text: (index+1).toString() +". " + data[index]['name'], // default text style
                children: <TextSpan>[
                  TextSpan(text: '\nScore: ', style: TextStyle(fontStyle: FontStyle.italic)),
                  TextSpan(text: (data[index]['score']).toString(), style: TextStyle(fontStyle: FontStyle.italic)),
                ],
              ),
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: initState,
        child: Icon(Icons.refresh),
      ),

    );
  }
}
