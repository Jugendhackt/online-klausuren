// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'submission.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Submission _$SubmissionFromJson(Map<String, dynamic> json) {
  return Submission(
    taskId: json['taskId'] as String,
    value: json['value'] as String,
  );
}

Map<String, dynamic> _$SubmissionToJson(Submission instance) =>
    <String, dynamic>{
      'taskId': instance.taskId,
      'value': instance.value,
    };
