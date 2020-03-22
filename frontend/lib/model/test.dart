import 'package:json_annotation/json_annotation.dart';

import 'task.dart';

part 'test.g.dart';

@JsonSerializable(nullable: false)
class Test {
  int start;

  List<Task> tasks;

  Test();

  factory Test.fromJson(Map<String, dynamic> json) => _$TestFromJson(json);
  Map<String, dynamic> toJson() => _$TestToJson(this);
}
