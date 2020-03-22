// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'test.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Test _$TestFromJson(Map<String, dynamic> json) {
  return Test()
    ..id = json['id'] as String
    ..start = json['start'] as int
    ..name = json['name'] as String
    ..tasks = (json['tasks'] as List)
        ?.map(
            (e) => e == null ? null : Task.fromJson(e as Map<String, dynamic>))
        ?.toList();
}

Map<String, dynamic> _$TestToJson(Test instance) => <String, dynamic>{
      'id': instance.id,
      'start': instance.start,
      'name': instance.name,
      'tasks': instance.tasks,
    };
