// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'text_task.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

TextTask _$TextTaskFromJson(Map<String, dynamic> json) {
  return TextTask()
    ..id = json['id'] as String
    ..type = json['type'] as String
    ..title = json['title'] as String
    ..description = json['description'] as String
    ..time = json['time'] as int;
}

Map<String, dynamic> _$TextTaskToJson(TextTask instance) => <String, dynamic>{
      'id': instance.id,
      'type': instance.type,
      'title': instance.title,
      'description': instance.description,
      'time': instance.time,
    };
