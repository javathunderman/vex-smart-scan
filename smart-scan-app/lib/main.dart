import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
void main() => runApp(MyApp());
String sku;
String dropdownvalue;
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
      body: Container(
      child: Column(children: <Widget>
        [Padding(
          padding: const EdgeInsets.all(16.0),
          child: Text(
            'Enter the last four digits of the tournament SKU: ',
            textAlign: TextAlign.center,
            overflow: TextOverflow.ellipsis,
            style: TextStyle(fontWeight: FontWeight.bold),
          )
        ),
        Padding(
          padding: const EdgeInsets.all(16.0),
          child: TextField(
            controller: myController,
          ),
        ),
        Text(
          'Season selector: ',
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
      ]
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
    if(dropdownvalue == "Turning Point") {
      sku = "RE-VRC-18-" + sku;
    }
    else if(dropdownvalue == "In the Zone") {
      sku = "RE-VRC-17-" + sku;
    }
    else {
      sku = "RE-VRC-19-"+ sku;
    }
    var response = await http.get(
        Uri.encodeFull("http://smart-scan.mercuryrobotics.tk/tournaments/"+sku),
        headers: {
          "Accept": "application/json"
        }
    );

    this.setState(() {
      var parsedresponse =  json.decode(response.body);
      data = json.decode(parsedresponse['content']);
    });
    return "Success!";
  }

  @override
  void initState(){
    print("Running again");
    this.getData();
  }

  @override
  Widget build(BuildContext context) {
    if (data != null) {
      return new Scaffold(

        appBar: new AppBar(title: new Text("Smart Scan Rankings"),
            backgroundColor: Colors.blue),

        body:
        ListView.builder(
          itemCount: data == null ? 0 : data.length,
          itemBuilder: (BuildContext context, int index) {
            return new Card(
              child: new Text.rich(
                TextSpan(
                  text: (index + 1).toString() + ". " + data[index]['name'],
                  // default text style
                  children: <TextSpan>[
                    TextSpan(text: '\nScore: ',
                        style: TextStyle(fontStyle: FontStyle.italic)),
                    TextSpan(text: (data[index]['score']).toString(),
                        style: TextStyle(fontStyle: FontStyle.italic)),
                  ],
                ),
              ),
            );
          },
        ),
      );
    }
    else {
      return new Scaffold(

        appBar: new AppBar(title: new Text("Smart Scan Rankings"),
            backgroundColor: Colors.blue),

        body: Center( child: Text.rich(
          TextSpan(
            text: 'Error\n', // default text style
            style: TextStyle(fontSize: 25),
            children: <TextSpan>[
              TextSpan(text: 'Your request could not be processed at this time. Please try again later. ', style: TextStyle(fontStyle: FontStyle.italic, fontSize: 15)),
            ],
          ),
          textAlign: TextAlign.center,
        )),
      );
    }
  }
}