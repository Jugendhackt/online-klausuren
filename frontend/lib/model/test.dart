import 'package:json_annotation/json_annotation.dart';

import 'task.dart';

part 'test.g.dart';

@JsonSerializable(nullable: false)
class Test {
  String id;

  @JsonKey(nullable: true)
  int start;

  @JsonKey(nullable: true)
  String name;

  @JsonKey(nullable: true)
  List<Task> tasks;

  Test();

  factory Test.fromJson(Map<String, dynamic> json) => _$TestFromJson(json);
  Map<String, dynamic> toJson() => _$TestToJson(this);
}
