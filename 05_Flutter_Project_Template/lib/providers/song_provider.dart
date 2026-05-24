import 'dart:convert';
import 'dart:math';
import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/song.dart';

class SongProvider extends ChangeNotifier {
  List<Song> _songs = _initialSongs;
  bool _loaded = false;

  List<Song> get songs => _songs;

  Song? getSongById(String id) {
    try {
      return _songs.firstWhere((s) => s.id == id);
    } catch (_) {
      return null;
    }
  }

  List<Song> filteredBy(String searchQuery, String filter) {
    return _songs.where((song) {
      final q = searchQuery.toLowerCase();
      final matchesSearch = q.isEmpty ||
          song.title.toLowerCase().contains(q) ||
          song.artist.toLowerCase().contains(q);
      final matchesFilter = filter == 'Toate' ||
          (filter == 'Repertoire' && song.status == 'repertoire') ||
          (filter == 'În lucru' && song.status == 'learning') ||
          (filter == 'Nou' && song.status == 'new') ||
          (filter == 'Favorite' && song.isFavorite);
      return matchesSearch && matchesFilter;
    }).toList();
  }

  void addSong(Song song) {
    _songs = [..._songs, song];
    notifyListeners();
    _persist();
  }

  void updateSong(Song song) {
    _songs = _songs.map((s) => s.id == song.id ? song : s).toList();
    notifyListeners();
    _persist();
  }

  void deleteSong(String id) {
    _songs = _songs.where((s) => s.id != id).toList();
    notifyListeners();
    _persist();
  }

  void toggleFavorite(String id) {
    _songs = _songs
        .map((s) => s.id == id ? s.copyWith(isFavorite: !s.isFavorite) : s)
        .toList();
    notifyListeners();
    _persist();
  }

  String generateId() =>
      '${DateTime.now().millisecondsSinceEpoch}_${Random().nextInt(9999)}';

  Future<void> load() async {
    if (_loaded) return;
    _loaded = true;
    final prefs = await SharedPreferences.getInstance();
    final stored = prefs.getStringList('songs');
    if (stored != null && stored.isNotEmpty) {
      try {
        _songs = stored
            .map((s) => Song.fromJson(jsonDecode(s) as Map<String, dynamic>))
            .toList();
        notifyListeners();
      } catch (_) {
        // corrupted data — keep defaults
      }
    }
  }

  Future<void> _persist() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setStringList(
      'songs',
      _songs.map((s) => jsonEncode(s.toJson())).toList(),
    );
  }

  static const List<Song> _initialSongs = [
    Song(
      id: 'song_1',
      title: 'Oceans (Where Feet May Fail)',
      artist: 'Hillsong United',
      key: 'Do',
      durationSeconds: 272,
      status: 'repertoire',
      tempo: 65,
      usageCount: 12,
      lead: 'Andrei',
      guitar: 'Maria',
      drums: 'Alex',
      lastUsed: '18 Mai 2025',
    ),
    Song(
      id: 'song_2',
      title: 'Way Maker',
      artist: 'Sinach',
      key: 'Sol',
      durationSeconds: 345,
      status: 'repertoire',
      tempo: 72,
      usageCount: 8,
      lead: 'Maria',
      guitar: 'Andrei',
      drums: 'Alex',
      lastUsed: '11 Mai 2025',
    ),
    Song(
      id: 'song_3',
      title: 'Graves Into Gardens',
      artist: 'Elevation Worship',
      key: 'La m',
      durationSeconds: 258,
      status: 'repertoire',
      tempo: 68,
      usageCount: 5,
      lead: 'Andrei',
      guitar: 'Cristina',
      drums: 'Alex',
      lastUsed: '4 Mai 2025',
    ),
    Song(
      id: 'song_4',
      title: 'What A Beautiful Name',
      artist: 'Hillsong Worship',
      key: 'Re',
      durationSeconds: 320,
      status: 'repertoire',
      tempo: 68,
      usageCount: 15,
      lead: 'Elena',
      guitar: 'Andrei',
      drums: 'Alex',
      lastUsed: '25 Mai 2025',
    ),
    Song(
      id: 'song_5',
      title: 'Goodness of God',
      artist: 'Bethel Music',
      key: 'Do',
      durationSeconds: 292,
      status: 'repertoire',
      tempo: 70,
      usageCount: 10,
      lead: 'Andrei',
      guitar: 'Maria',
      drums: 'Alex',
      lastUsed: '18 Mai 2025',
    ),
    Song(
      id: 'song_6',
      title: 'Build My Life',
      artist: 'Pat Barrett',
      key: 'Mi',
      durationSeconds: 255,
      status: 'learning',
      tempo: 74,
      usageCount: 2,
      lead: 'Andrei',
      guitar: 'Maria',
      drums: 'Alex',
      lastUsed: '20 Mai 2025',
    ),
    Song(
      id: 'song_7',
      title: 'Firm Foundation',
      artist: 'Cody Carnes',
      key: 'Fa',
      durationSeconds: 228,
      status: 'new',
      tempo: 72,
      usageCount: 0,
      lead: 'Maria',
      guitar: 'Andrei',
      drums: 'Alex',
      lastUsed: '-',
    ),
  ];
}
