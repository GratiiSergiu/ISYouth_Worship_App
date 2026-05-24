class SetlistItem {
  final String id;
  final String songId;
  final String segment; // 'intro' | 'worship' | 'special' | 'sermon' | 'sending'
  final String notes;

  const SetlistItem({
    required this.id,
    required this.songId,
    this.segment = 'worship',
    this.notes = '',
  });

  SetlistItem copyWith({
    String? id,
    String? songId,
    String? segment,
    String? notes,
  }) {
    return SetlistItem(
      id: id ?? this.id,
      songId: songId ?? this.songId,
      segment: segment ?? this.segment,
      notes: notes ?? this.notes,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'songId': songId,
        'segment': segment,
        'notes': notes,
      };

  factory SetlistItem.fromJson(Map<String, dynamic> json) => SetlistItem(
        id: json['id'] as String,
        songId: json['songId'] as String,
        segment: (json['segment'] as String?) ?? 'worship',
        notes: (json['notes'] as String?) ?? '',
      );
}
