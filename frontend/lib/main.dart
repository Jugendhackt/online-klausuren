import 'dart:convert';
import 'dart:html';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:online_klausuren_app/model/multiple_choice_task.dart';
import 'package:online_klausuren_app/model/submission.dart';
import 'package:online_klausuren_app/model/task.dart';
import 'package:online_klausuren_app/model/test.dart';
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
  String testToken;

  String apiToken;

  WebSocketChannel channel;

  String get host => window.location.protocol == 'https:'
      ? '${window.location.host}'
      : '${window.location.hostname}:8080';

  bool get useSSL => window.location.protocol == 'https:';

  String get baseUrl => '${useSSL ? 'https' : 'http'}://$host/api/v1';

  Future<String> login(String username, String password) async {
    var res = await http.post('$baseUrl/auth',
        body: json.encode({
          'type': 'api',
          'username': username,
          'password': password,
        }));
    if (res.statusCode == 200) {
      return json.decode(res.body)['token'];
    } else {
      throw 'HTTP ${res.statusCode}: ${res.body}';
    }
  }

  Future<String> generateTestToken(
    String testId,
  ) async {
    var res = await http.post(
      '$baseUrl/auth',
      body: json.encode({
        'type': 'test',
        'test': testId,
      }),
      headers: {
        'Authorization': 'Bearer $apiToken',
      },
    );
    if (res.statusCode == 200) {
      return json.decode(res.body)['token'];
    } else {
      throw 'HTTP ${res.statusCode}: ${res.body}';
    }
  }

  Future<List<Test>> getTests() async {
    var res = await http.get(
      '$baseUrl/tests',
      headers: {
        'Authorization': 'Bearer $apiToken',
      },
    );
    if (res.statusCode == 200) {
      return json.decode(res.body).map<Test>((m) => Test.fromJson(m)).toList();
    } else {
      throw 'HTTP ${res.statusCode}: ${res.body}';
    }
  }

  List<Test> tests;
  _loadTests() async {
    tests = await getTests();
  }

  // Stellt die Verbindung zum Backend her
  connect() {
    if (channel != null) return;

    channel = HtmlWebSocketChannel.connect(
      '${useSSL ? 'wss' : 'ws'}://$host/api/v1/ws?token=$testToken',
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

      currentTask = Task.fromJson(task);

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
      body: tests == null
          ? _buildLoginPage()
          : ((currentTask == null || _testFinished)
              ? (_testFinished
                  ? Center(
                      child: Text(/* _testFinished
                      ? */
                          'Dieser Test ist jetzt beendet. Vielen Dank für deine Teilnahme!'
                          /*  : 'Dein Token: $testToken. Warte auf Aufgaben...' */),
                    )
                  : _buildTestSelection())
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

  Widget _buildTestSelection() => ListView(
        children: <Widget>[
          for (var test in tests)
            ListTile(
              title: Text(test.id),
              subtitle: Text(test.toJson().toString()),
              onTap: () async {
                /*  try { */
                String token = await generateTestToken(test.id);
                print(token);
                setState(() {
                  testToken = token;
                });
                connect();
                /*  } catch (e) {
                  sState(() {
                    error = e.toString();
                  });
                } */
              },
            ),
        ],
      );

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
            var usernameCtrl = TextEditingController();
            var passwordCtrl = TextEditingController();
            String error;

            bool loginProcess = false;
            showDialog(
              context: context,
              builder: (context) => StatefulBuilder(
                builder: (context, sState) => AlertDialog(
                  title: Text('Deine Anmeldedaten?'),
                  content: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: <Widget>[
                      TextField(
                        controller: usernameCtrl,
                        decoration: InputDecoration(
                          labelText: 'Username',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      SizedBox(
                        height: 8,
                      ),
                      TextField(
                        controller: passwordCtrl,
                        obscureText: true,
                        decoration: InputDecoration(
                          labelText: 'Password',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      if (loginProcess) ...[
                        SizedBox(
                          height: 8,
                        ),
                        ListTile(
                          leading: CircularProgressIndicator(),
                          title: Text('Du wirst angemeldet...'),
                        ),
                      ],
                      if (error != null) ...[
                        SizedBox(
                          height: 8,
                        ),
                        ListTile(
                          title: Text(
                            error,
                            style: TextStyle(
                              color: Colors.red,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                      ],
                    ],
                  ),
                  actions: <Widget>[
                    FlatButton(
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                      child: Text('Abbrechen'),
                    ),
                    FlatButton(
                      onPressed: () async {
                        sState(() {
                          loginProcess = true;
                        });
                        try {
                          String token =
                              await login(usernameCtrl.text, passwordCtrl.text);
                          print(token);
                          apiToken = token;
                          await _loadTests();
                          Navigator.of(context).pop();
                          setState(() {});
                          _loadTests();
                        } catch (e) {
                          sState(() {
                            error = e.toString();
                          });
                        }
                        // await Future.delayed(Duration(seconds: 10));
                        //Navigator.of(context).pop();
                        /*    setState(() {
                          testToken = usernameCtrl.text;
                        });
                        connect(); */
                      },
                      child: Text('Login'),
                    ),
                  ],
                ),
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
