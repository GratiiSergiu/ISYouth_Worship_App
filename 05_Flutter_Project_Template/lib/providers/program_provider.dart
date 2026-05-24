import 'dart:convert';
import 'dart:math';
import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/program.dart';
import '../models/setlist_item.dart';

class ProgramProvider extends ChangeNotifier {
  List<Program> _programs = _initialPrograms;
  bool _loaded = false;

  List<Program> get programs => _programs;

  Program? getProgramById(String id) {
    try {
      return _programs.firstWhere((p) => p.id == id);
    } catch (_) {
      return null;
    }
  }

  void addProgram(Program program) {
    _programs = [..._programs, program];
    notifyListeners();
    _persist();
  }

  void updateProgram(Program program) {
    _programs = _programs.map((p) => p.id == program.id ? program : p).toList();
    notifyListeners();
    _persist();
  }

  void deleteProgram(String id) {
    _programs = _programs.where((p) => p.id != id).toList();
    notifyListeners();
    _persist();
  }

  void reorderSetlist(String programId, int oldIndex, int newIndex) {
    final program = getProgramById(programId);
    if (program == null) return;
    final setlist = [...program.setlist];
    if (newIndex > oldIndex) newIndex--;
    final item = setlist.removeAt(oldIndex);
    setlist.insert(newIndex, item);
    updateProgram(program.copyWith(setlist: setlist));
  }

  String generateId() =>
      '${DateTime.now().millisecondsSinceEpoch}_${Random().nextInt(9999)}';

  Future<void> load() async {
    if (_loaded) return;
    _loaded = true;
    final prefs = await SharedPreferences.getInstance();
    final stored = prefs.getStringList('programs');
    if (stored != null && stored.isNotEmpty) {
      try {
        _programs = stored
            .map((s) => Program.fromJson(jsonDecode(s) as Map<String, dynamic>))
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
      'programs',
      _programs.map((p) => jsonEncode(p.toJson())).toList(),
    );
  }

  static final List<Program> _initialPrograms = [
    Program(
      id: 'prog_1',
      title: 'Identitate',
      theme: 'Cine suntem în Hristos',
      date: '25 Mai 2025',
      status: 'ready',
      tags: ['#tineret', '#adorare'],
      targetDurationMinutes: 60,
      setlist: [
        SetlistItem(id: 'si_1', songId: 'song_1', segment: 'worship'),
        SetlistItem(id: 'si_2', songId: 'song_2', segment: 'worship'),
        SetlistItem(id: 'si_3', songId: 'song_3', segment: 'worship'),
        SetlistItem(id: 'si_4', songId: 'song_4', segment: 'special'),
        SetlistItem(id: 'si_5', songId: 'song_5', segment: 'sending'),
      ],
    ),
    Program(
      id: 'prog_2',
      title: 'Credință',
      theme: 'Tineret ISYouth',
      date: '18 Mai 2025',
      status: 'completed',
      tags: ['#tineret', '#mărturie'],
      targetDurationMinutes: 60,
      setlist: [
        SetlistItem(id: 'si_6', songId: 'song_2', segment: 'worship'),
        SetlistItem(id: 'si_7', songId: 'song_4', segment: 'worship'),
        SetlistItem(id: 'si_8', songId: 'song_5', segment: 'worship'),
        SetlistItem(id: 'si_9', songId: 'song_1', segment: 'special'),
        SetlistItem(id: 'si_10', songId: 'song_3', segment: 'sending'),
        SetlistItem(id: 'si_11', songId: 'song_6', segment: 'sending'),
      ],
    ),
    Program(
      id: 'prog_3',
      title: 'Har',
      theme: 'Serviciu Special',
      date: '1 Iunie 2025',
      status: 'draft',
      tags: ['#botez', '#adorare'],
      targetDurationMinutes: 45,
      setlist: [
        SetlistItem(id: 'si_12', songId: 'song_1', segment: 'worship'),
        SetlistItem(id: 'si_13', songId: 'song_7', segment: 'worship'),
        SetlistItem(id: 'si_14', songId: 'song_5', segment: 'sending'),
      ],
    ),
  ];
}
