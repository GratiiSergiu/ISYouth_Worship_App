class Song {
  final String id;
  final String title;
  final String artist;
  final String key;
  final int durationSeconds;
  final int tempo;
  final String timeSignature;
  final String status; // 'repertoire' | 'learning' | 'new'
  final int usageCount;
  final String lead;
  final String guitar;
  final String drums;
  final String lastUsed;
  final bool isFavorite;

  const Song({
    required this.id,
    required this.title,
    required this.artist,
    required this.key,
    required this.durationSeconds,
    this.tempo = 70,
    this.timeSignature = '4/4',
    required this.status,
    this.usageCount = 0,
    this.lead = '-',
    this.guitar = '-',
    this.drums = '-',
    this.lastUsed = '-',
    this.isFavorite = false,
  });

  Song copyWith({
    String? id,
    String? title,
    String? artist,
    String? key,
    int? durationSeconds,
    int? tempo,
    String? timeSignature,
    String? status,
    int? usageCount,
    String? lead,
    String? guitar,
    String? drums,
    String? lastUsed,
    bool? isFavorite,
  }) {
    return Song(
      id: id ?? this.id,
      title: title ?? this.title,
      artist: artist ?? this.artist,
      key: key ?? this.key,
      durationSeconds: durationSeconds ?? this.durationSeconds,
      tempo: tempo ?? this.tempo,
      timeSignature: timeSignature ?? this.timeSignature,
      status: status ?? this.status,
      usageCount: usageCount ?? this.usageCount,
      lead: lead ?? this.lead,
      guitar: guitar ?? this.guitar,
      drums: drums ?? this.drums,
      lastUsed: lastUsed ?? this.lastUsed,
      isFavorite: isFavorite ?? this.isFavorite,
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'title': title,
        'artist': artist,
        'key': key,
        'durationSeconds': durationSeconds,
        'tempo': tempo,
        'timeSignature': timeSignature,
        'status': status,
        'usageCount': usageCount,
        'lead': lead,
        'guitar': guitar,
        'drums': drums,
        'lastUsed': lastUsed,
        'isFavorite': isFavorite,
      };

  factory Song.fromJson(Map<String, dynamic> json) => Song(
        id: json['id'] as String,
        title: json['title'] as String,
        artist: json['artist'] as String,
        key: json['key'] as String,
        durationSeconds: json['durationSeconds'] as int,
        tempo: (json['tempo'] as int?) ?? 70,
        timeSignature: (json['timeSignature'] as String?) ?? '4/4',
        status: json['status'] as String,
        usageCount: (json['usageCount'] as int?) ?? 0,
        lead: (json['lead'] as String?) ?? '-',
        guitar: (json['guitar'] as String?) ?? '-',
        drums: (json['drums'] as String?) ?? '-',
        lastUsed: (json['lastUsed'] as String?) ?? '-',
        isFavorite: (json['isFavorite'] as bool?) ?? false,
      );
}
