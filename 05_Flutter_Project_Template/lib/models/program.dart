import 'setlist_item.dart';

class Program {
  final String id;
  final String title;
  final String theme;
  final String date;
  final String status; // 'draft' | 'ready' | 'completed'
  final List<SetlistItem> setlist;
  final List<String> tags;
  final int targetDurationMinutes;
  final String notes;

  const Program({
    required this.id,
    required this.title,
    required this.theme,
    required this.date,
    this.status = 'draft',
    this.setlist = const [],
    this.tags = const [],
    this.targetDurationMinutes = 60,
    this.notes = '',
  });

  Program copyWith({
    String? id,
    String? title,
    String? theme,
    String? date,
    String? status,
    List<SetlistItem>? setlist,
    List<String>? tags,
    int? targetDurationMinutes,
    String? notes,
  }) {
    return Program(
      id: id ?? this.id,
      title: title ?? this.title,
      theme: theme ?? this.theme,
      date: date ?? this.date,
      status: status ?? this.status,
      setlist: setlist ?? this.setlist,
      tags: tags ?? this.tags,
      targetDurationMinutes: targetDurationMinutes ?? this.targetDurationMinutes,
      notes: notes ?? this.notes,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'title': title,
        'theme': theme,
        'date': date,
        'status': status,
        'setlist': setlist.map((i) => i.toJson()).toList(),
        'tags': tags,
        'targetDurationMinutes': targetDurationMinutes,
        'notes': notes,
      };

  factory Program.fromJson(Map<String, dynamic> json) => Program(
        id: json['id'] as String,
        title: json['title'] as String,
        theme: (json['theme'] as String?) ?? '',
        date: json['date'] as String,
        status: (json['status'] as String?) ?? 'draft',
        setlist: (json['setlist'] as List<dynamic>? ?? [])
            .map((i) => SetlistItem.fromJson(i as Map<String, dynamic>))
            .toList(),
        tags: List<String>.from(json['tags'] as List<dynamic>? ?? []),
        targetDurationMinutes: (json['targetDurationMinutes'] as int?) ?? 60,
        notes: (json['notes'] as String?) ?? '',
      );
}
