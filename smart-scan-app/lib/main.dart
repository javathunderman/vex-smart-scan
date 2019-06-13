import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
void main() => runApp(MyApp());
String sku;
class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Smart Scan Rankings',
      home: MyCustomForm(),
    );
  }
}


class MyCustomForm extends StatefulWidget {
  @override
  _MyCustomFormState createState() => _MyCustomFormState();
}
class _MyCustomFormState extends State<MyCustomForm> {
  final myController = TextEditingController();

  @override
  void dispose() {
    myController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Smart Scan Rankings'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: TextField(
          controller: myController,
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          sku = myController.text;
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => HomePage()),
          );
        },
        child: Icon(Icons.refresh),
      ),

    );
  }
}
class HomePage extends StatefulWidget {
  @override
  HomePageState createState() => new HomePageState();
}

class HomePageState extends State<HomePage> {

  List data;

  Future<String> getData() async {
    var response = await http.get(
        Uri.encodeFull("http://192.168.1.120:3000/users?sku="+sku),
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