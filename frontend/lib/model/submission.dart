import 'package:json_annotation/json_annotation.dart';

part 'submission.g.dart';

@JsonSerializable(nullable: false)
class Submission {
  String taskId;
  String value;

  Submission({
    this.taskId,
    this.value,
  });

  factory Submission.fromJson(Map<String, dynamic> json) =>
      _$SubmissionFromJson(json);
  Map<String, dynamic> toJson() => _$SubmissionToJson(this);
}
