import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;

void main() => runApp(MyApp());
String team;
String dropdownvalue;
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
      appBar: AppBar(title: Text('Smart Scan Rankings'), actions: <Widget>[
        // action button
        IconButton(
          icon: Icon(Icons.info),
          onPressed: () {
            Navigator.push(
              context,
              MaterialPageRoute(builder: (context) => infoPage()),
            );
          },
        ),
      ]),
      body: Container(
        child: Column(children: <Widget>[
          Padding(
              padding: const EdgeInsets.all(16.0),
              child: Text(
                'Enter a team ID to look up events: ',
                textAlign: TextAlign.center,
                overflow: TextOverflow.ellipsis,
                style: TextStyle(fontWeight: FontWeight.bold),
              )),
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: TextField(
              controller: myController,
            ),
          ),
          Text(
            'Select a season: ',
            textAlign: TextAlign.center,
            overflow: TextOverflow.ellipsis,
            style: TextStyle(fontWeight: FontWeight.bold),
          ),
          DropdownButton<String>(
            value: dropdownvalue,
            onChanged: (String newValue) {
              setState(() {
                dropdownvalue = newValue;
              });
            },
            items: <String>['Tower Takeover', 'Turning Point', 'In the Zone']
                .map<DropdownMenuItem<String>>((String value) {
              return DropdownMenuItem<String>(
                value: value,
                child: Text(value),
              );
            }).toList(),
          ),
          Text(
            '\n\nCreated by Arjun (javathunderman) of 4001A',
            textAlign: TextAlign.center,
            overflow: TextOverflow.ellipsis,
            style: TextStyle(fontWeight: FontWeight.bold),
          ),
        ]),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          team = myController.text;
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => HomePage()),
          );
        },
        child: Icon(Icons.arrow_forward),
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
        Uri.encodeFull("https://api.vexdb.io/v1/get_events?team=" +
            team +
            "&season=" +
            dropdownvalue),
        headers: {"Accept": "application/json"});

    this.setState(() {
      var parsedresponse = json.decode(response.body);
      data = (parsedresponse['result']);
    });
    return "Success!";
  }

  @override
  void initState() {
    print("Running again");
    this.getData();
  }

  @override
  Widget build(BuildContext context) {
    if (data != null) {
      return new Scaffold(
        appBar: new AppBar(
            title: new Text("Smart Scan Rankings"),
            backgroundColor: Colors.blue),
        body: ListView.builder(
          itemCount: data == null ? 0 : data.length,
          itemBuilder: (BuildContext context, int index) {
            return GestureDetector(
                child: Card(
                    child: Padding(
                        padding: EdgeInsets.all(10.0),
                        child: Text(data[index]['name']))),
                onTap: () => runSKU(data[index]['sku'], context));
          },
        ),
      );
    } else {
      return new Scaffold(
        appBar: new AppBar(
            title: new Text("Smart Scan Rankings"),
            backgroundColor: Colors.blue),
        body: Center(
            child: Text.rich(
          TextSpan(
            text: 'Please wait...\n\n', // default text style
            style: TextStyle(fontSize: 25),
            children: <TextSpan>[
              TextSpan(
                  text:
                      'Your request is currently being processed. If this message does not go away automatically, please try again later. ',
                  style: TextStyle(fontStyle: FontStyle.italic, fontSize: 15)),
            ],
          ),
          textAlign: TextAlign.center,
        )),
      );
    }
  }
}

class infoPage extends StatefulWidget {
  @override
  infoPageState createState() => new infoPageState();
}

class infoPageState extends State<infoPage> {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
          title: new Text("Smart Scan Rankings"), backgroundColor: Colors.blue),
      body: Center(
          child: Text.rich(
        TextSpan(
          text: 'Information and license\n\n', // default text style
          style: TextStyle(fontSize: 25),
          children: <TextSpan>[
            TextSpan(
                text:
                    'Smart Scan was created by javathunderman (Arjun V.) of Vex Robotics Team 4001A. \n\nThe code for this project is available at https://github.com/javathunderman/vex-smart-scan\n\nLicensed under GNU GPL v3.0. ',
                style: TextStyle(fontStyle: FontStyle.italic, fontSize: 15)),
          ],
        ),
        textAlign: TextAlign.center,
      )),
    );
  }
}

class resultsPage extends StatefulWidget {
  @override
  resultsPageState createState() => new resultsPageState();
}

class resultsPageState extends State<resultsPage> {
  List data;

  Future<String> getData() async {
    var response = await http.get(
        Uri.encodeFull("http://smartscan.tk/tournaments/" + sku),
        headers: {"Accept": "application/json"});

    this.setState(() {
      var parsedresponse = json.decode(response.body);
      data = json.decode(parsedresponse['content']);
    });
    return "Success!";
  }

  @override
  void initState() {
    print("Running again - smart scan server");
    this.getData();
  }

  @override
  Widget build(BuildContext context) {
    if (data != null) {
      return new Scaffold(
        appBar: new AppBar(
            title: new Text("Smart Scan Rankings"),
            backgroundColor: Colors.blue),
        body: ListView.builder(
          itemCount: data == null ? 0 : data.length,
          itemBuilder: (BuildContext context, int index) {
            return new Card(
              child: new Text.rich(
                TextSpan(
                  text: (index + 1).toString() + ". " + data[index]['team'],
                  // default text style
                  children: <TextSpan>[
                    TextSpan(
                        text: '\nScore: ',
                        style: TextStyle(fontStyle: FontStyle.italic)),
                    TextSpan(
                        text: (data[index]['score']).toString(),
                        style: TextStyle(fontStyle: FontStyle.italic)),
                  ],
                ),
              ),
            );
          },
        ),
      );
    } else {
      return new Scaffold(
        appBar: new AppBar(
            title: new Text("Smart Scan Rankings"),
            backgroundColor: Colors.blue),
        body: Center(
            child: Text.rich(
          TextSpan(
            text: 'Please wait...\n\n', // default text style
            style: TextStyle(fontSize: 25),
            children: <TextSpan>[
              TextSpan(
                  text:
                      'Your request is currently being processed. If this message does not go away automatically, please try again later. ',
                  style: TextStyle(fontStyle: FontStyle.italic, fontSize: 15)),
            ],
          ),
          textAlign: TextAlign.center,
        )),
      );
    }
  }
}

void runSKU(String skuinput, BuildContext context) {
  sku = skuinput;
  Navigator.push(
    context,
    MaterialPageRoute(builder: (context) => resultsPage()),
  );
}
