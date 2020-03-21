import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:online_klausuren_app/model/multiple_choice_task.dart';
import 'package:online_klausuren_app/model/submission.dart';
import 'package:online_klausuren_app/model/task.dart';
import 'package:online_klausuren_app/model/text_task.dart';
import 'package:web_socket_channel/html.dart';
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

    channel = HtmlWebSocketChannel.connect(
      kReleaseMode
          ? 'wss://lisky.app/api/v1/ws?token=$bearerToken'
          : 'ws://localhost:8080/api/v1/ws?token=$bearerToken',
// TODO Other authorization [low priority]
/*    headers: {
        'Authorization': 'Bearer $bearerToken',
      }, */
    );
    channel.stream.listen(onData);

    /*  send('task', {
      'task': {
        'id': '3f1758ab-368f-475a-91b1-e9d431fb67d0',
        'type': "CHOICES",
        'title': "Wann wurde Donald Trump geboren?",
        'description': "", // In Markdown
        'time': 300,
        'choices': {
          '0': "21. November 1942",
          '1': "14. Juni 1946",
          '2': "1. Januar 1969"
        },
      },
      'deadline': 1584835199,
    }); */
  }

  // Erhält Daten vom Backend
  onData(var msgAsJson) {
    print('onData $msgAsJson');

    var msg = json.decode(msgAsJson);
    String function = msg['function'];
    var data = msg.containsKey('data') ? msg['data'] : null;

    if (function == 'task') {
      var task = data['task'];

      if (task['type'] == 'TEXT') {
        currentTask = TextTask.fromJson(task);
      } else if (task['type'] == 'CHOICES') {
        currentTask = MultipleChoiceTask.fromJson(task);
      } else {
        // Sollte nicht passieren :D
      }
      deadline = DateTime.fromMillisecondsSinceEpoch(data['deadline'] * 1000);

      _selectedChoice = null;
      _inputText = '';

      _submitted = false;

      if (mounted) setState(() {});
    } else if (function == 'test_finished') {
      _selectedChoice = null;
      _submitted = false;

      _testFinished = true;

      if (mounted) setState(() {});
    }
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

  Task currentTask;
  DateTime deadline;

  bool _testFinished = false;

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
          ? _buildLoginPage()
          : ((currentTask == null || _testFinished)
              ? Center(
                  child: Text(_testFinished
                      ? 'Dieser Test ist jetzt beendet. Vielen Dank für deine Teilnahme!'
                      : 'Dein Token: $bearerToken. Warte auf Aufgaben...'),
                )
              : ListView(
                  padding: const EdgeInsets.all(8.0),
                  children: <Widget>[
                    Text(
                      currentTask.title,
                      style: Theme.of(context).textTheme.headline5,
                    ),
                    Row(
                      children: <Widget>[
                        StreamBuilder(
                            stream: Stream.periodic(
                              Duration(milliseconds: 200),
                            ),
                            builder: (context, _) {
                              if (deadline
                                      .difference(
                                        DateTime.now(),
                                      )
                                      .inMilliseconds <
                                  0) {
                                if (!_submitted) sendSubmission();
                              }

                              return Text(
                                _printDuration(
                                      deadline.difference(
                                        DateTime.now(),
                                      ),
                                    ) +
                                    ' verbleibend',
                              );
                            }),
                        Spacer(),
                        Text(
                          _printDuration(
                                Duration(
                                  seconds: currentTask.time,
                                ),
                              ) +
                              ' insgesamt',
                        ),
                      ],
                    ),
                    if (currentTask.description.isNotEmpty)
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text(
                          currentTask.description,
                        ),
                      ),
                    SizedBox(
                      height: 16,
                    ),
                    if (currentTask is MultipleChoiceTask) ..._buildChoices(),
                    if (currentTask is TextTask) _buildTextField(),
                    if (_selectedChoice != null || currentTask is TextTask)
                      Align(
                        child: RaisedButton(
                          color: Colors.yellow,
                          child: Text('Auswahl bestätigen und abschicken'),
                          onPressed: _submitted
                              ? null
                              : () {
                                  sendSubmission();
                                },
                        ),
                      ),
                  ],
                )),
    );
  }

  sendSubmission() {
    _submitted = true;
    if (currentTask is MultipleChoiceTask) {
      if (_selectedChoice == null) {
        _selectedChoice =
            (currentTask as MultipleChoiceTask).choices.keys.first;
      }
      send(
        'submission',
        Submission(taskId: currentTask.id, value: _selectedChoice),
      );
    } else if (currentTask is TextTask) {
      send(
        'submission',
        Submission(taskId: currentTask.id, value: _inputText),
      );
    }

    if (mounted) setState(() {});
  }

  String _inputText = '';

  String _selectedChoice;
  bool _submitted = false;

  List<Widget> _buildChoices() {
    var list = <Widget>[];
    var task = currentTask as MultipleChoiceTask;

    for (var choiceId in task.choices.keys) {
      list.add(
        RaisedButton(
          color: _selectedChoice == choiceId ? Colors.blue : null,
          child: Text(task.choices[choiceId]),
          onPressed: () {
            setState(() {
              _selectedChoice = choiceId;
            });
          },
        ),
      );
    }

    return list;
  }

  Widget _buildTextField() {
    // var task = currentTask as TextTask;

    return TextField(
      controller: TextEditingController(text: _inputText),
      decoration: InputDecoration(
        border: OutlineInputBorder(),
        labelText: 'Deine Antwort',
      ),
      minLines: 5,
      maxLines: 1000,
      onChanged: (s) {
        _inputText = s;
      },
    );
  }

  Widget _buildLoginPage() => Center(
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
      );

  String _printDuration(Duration duration) {
    String twoDigits(int n) {
      if (n >= 10) return "$n";
      return "0$n";
    }

    String twoDigitMinutes = twoDigits(duration.inMinutes);
    String twoDigitSeconds = twoDigits(duration.inSeconds.remainder(60));
    return "$twoDigitMinutes:$twoDigitSeconds";
  }
}
