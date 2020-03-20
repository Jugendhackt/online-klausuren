import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

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
  WebSocketChannel channel;

  // Stellt die Verbindung zum Backend her
  connect() {
    if (channel != null) return;

    channel = IOWebSocketChannel.connect(
      'ws://echo.websocket.org',
      headers: {
        'Authorization': 'Bearer $bearerToken',
      },
    );
    channel.stream.listen(onData);
  }

  // Erhält Daten vom Backend
  onData(var msg) {
    print(msg);
  }

  // Sendet Daten zum Backend
  send(String function, var data) {
    channel.sink.add(
      json.encode(
        {
          'function': function,
          'data': data,
        },
      ),
    );
  }

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
                            connect();
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
