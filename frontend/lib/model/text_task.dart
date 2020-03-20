import 'package:json_annotation/json_annotation.dart';

import 'task.dart';

part 'text_task.g.dart';

@JsonSerializable(nullable: false)
class TextTask extends Task {
  TextTask() {
    this.type = "TEXT";
  }

  factory TextTask.fromJson(Map<String, dynamic> json) =>
      _$TextTaskFromJson(json);
  Map<String, dynamic> toJson() => _$TextTaskToJson(this);
}
