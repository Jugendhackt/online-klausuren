import 'package:json_annotation/json_annotation.dart';

import 'task.dart';

part 'multiple_choice_task.g.dart';

@JsonSerializable(nullable: false)
class MultipleChoiceTask extends Task {
  Map<String, String> choices;

  MultipleChoiceTask() {
    this.type = "CHOICES";
  }

  factory MultipleChoiceTask.fromJson(Map<String, dynamic> json) => _$MultipleChoiceTaskFromJson(json);
  Map<String, dynamic> toJson() => _$MultipleChoiceTaskToJson(this);
}
