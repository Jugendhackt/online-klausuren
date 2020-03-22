import 'package:online_klausuren_app/model/multiple_choice_task.dart';
import 'package:online_klausuren_app/model/text_task.dart';

class Task {
  String id;
  String type;
  String title;
  String description;
  int time;

  Task();

  toJson() {}

  factory Task.fromJson(Map<String, dynamic> json) {
    if (json['type'] == 'TEXT') {
      return TextTask.fromJson(json);
    } else if (json['type'] == 'CHOICES') {
      return MultipleChoiceTask.fromJson(json);
    } else {
      return Task.fromJson(json);
    }
  }
}
