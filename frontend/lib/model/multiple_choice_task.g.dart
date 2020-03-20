// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'multiple_choice_task.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

MultipleChoiceTask _$MultipleChoiceTaskFromJson(Map<String, dynamic> json) {
  return MultipleChoiceTask()
    ..id = json['id'] as String
    ..type = json['type'] as String
    ..title = json['title'] as String
    ..description = json['description'] as String
    ..time = json['time'] as int
    ..choices = Map<String, String>.from(json['choices'] as Map);
}

Map<String, dynamic> _$MultipleChoiceTaskToJson(MultipleChoiceTask instance) =>
    <String, dynamic>{
      'id': instance.id,
      'type': instance.type,
      'title': instance.title,
      'description': instance.description,
      'time': instance.time,
      'choices': instance.choices,
    };
