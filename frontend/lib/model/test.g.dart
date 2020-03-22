// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'test.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Test _$TestFromJson(Map<String, dynamic> json) {
  return Test()
    ..start = json['start'] as int
    ..tasks = (json['tasks'] as List)
        .map((e) => Task.fromJson(e as Map<String, dynamic>))
        .toList();
}

Map<String, dynamic> _$TestToJson(Test instance) => <String, dynamic>{
      'start': instance.start,
      'tasks': instance.tasks,
    };
