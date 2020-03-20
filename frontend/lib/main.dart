import 'package:flutter/material.dart';

void main() {
  runApp(
    MaterialApp(
      title: 'Online Klausuren',
      home: HomePage(),
    ),
  );
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  String bearerToken;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Online Klausuren'),
      ),
      body: bearerToken == null
          ? Center(
              child: RaisedButton(
                onPressed: () {
                  var ctrl = TextEditingController();
                  showDialog(
                    context: context,
                    builder: (context) => AlertDialog(
                      title: Text('Dein Token?'),
                      content: TextField(
                        controller: ctrl,
                        decoration: InputDecoration(
                          border: OutlineInputBorder(),
                        ),
                      ),
                      actions: <Widget>[
                        FlatButton(
                          onPressed: () {
                            Navigator.of(context).pop();
                          },
                          child: Text('Abbrechen'),
                        ),
                        FlatButton(
                          onPressed: () {
                            Navigator.of(context).pop();
                            setState(() {
                              bearerToken = ctrl.text;
                            });
                          },
                          child: Text('Login'),
                        ),
                      ],
                    ),
                  );
                },
                child: Text('Login'),
              ),
            )
          : Center(
              child: Text('Dein Token: $bearerToken'),
            ),
    );
  }
}
