import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class LiveModeScreen extends StatefulWidget {
  const LiveModeScreen({super.key});

  @override
  State<LiveModeScreen> createState() => _LiveModeScreenState();
}

class _LiveModeScreenState extends State<LiveModeScreen> {
  bool _isMusicianView = false;
  int _currentSong = 0;
  bool _isPlaying = true;
  double _progress = 0.45;

  final List<Map<String, dynamic>> _setlist = [
    {
      'title': 'Oceans (Where Feet May Fail)',
      'artist': 'Hillsong United',
      'key': 'Do',
      'capo': 0,
      'duration': 272,
      'lyrics': '''You call me out upon the waters
The great unknown where feet may fail
And there I find You in the mystery
In oceans deep my faith will stand

And I will call upon Your name
And keep my eyes above the waves
When oceans rise
My soul will rest in Your embrace
For I am Yours and You are mine''',
      'chords': '''[Verse 1]
Do              Sol
You call me out upon the waters
La m                  Fa
The great unknown where feet may fail
Do                   Sol
And there I find You in the mystery
La m              Fa
In oceans deep my faith will stand

[Chorus]
Do
And I will call upon Your name
Sol
And keep my eyes above the waves
La m
When oceans rise
Fa
My soul will rest in Your embrace
Do             Sol        La m   Fa
For I am Yours and You are mine''',
    },
    {
      'title': 'Way Maker',
      'artist': 'Sinach',
      'key': 'Sol',
      'capo': 0,
      'duration': 345,
      'lyrics': '''You are here, moving in our midst
I worship You, I worship You
You are here, working in this place
I worship You, I worship You

You are Way Maker, Miracle Worker
Promise Keeper, Light in the darkness
My God, that is who You are''',
      'chords': '''[Verse]
Sol              Re
You are here, moving in our midst
Do               La m
I worship You, I worship You
Sol              Re
You are here, working in this place
Do               La m
I worship You, I worship You

[Chorus]
Sol
You are Way Maker, Miracle Worker
Re
Promise Keeper, Light in the darkness
Do               La m
My God, that is who You are''',
    },
  ];

  @override
  Widget build(BuildContext context) {
    final song = _setlist[_currentSong];

    return Scaffold(
      backgroundColor: AppColors.black,
      body: SafeArea(
        child: Column(
          children: [
            // Header
            Padding(
              padding: const EdgeInsets.all(16),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text(
                    '"Identitate" • 25 Mai',
                    style: TextStyle(fontSize: 14, color: AppColors.gray300),
                  ),
                  Row(
                    children: [
                      // Toggle Muzician View
                      GestureDetector(
                        onTap: () => setState(() => _isMusicianView = !_isMusicianView),
                        child: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                          decoration: BoxDecoration(
                            color: _isMusicianView ? AppColors.coral.withOpacity(0.2) : AppColors.surface,
                            borderRadius: BorderRadius.circular(20),
                            border: Border.all(
                              color: _isMusicianView ? AppColors.coral : Colors.transparent,
                            ),
                          ),
                          child: Row(
                            children: [
                              Icon(
                                _isMusicianView ? Icons.music_note : Icons.text_fields,
                                color: _isMusicianView ? AppColors.coral : AppColors.gray300,
                                size: 16,
                              ),
                              const SizedBox(width: 4),
                              Text(
                                _isMusicianView ? 'Muzician' : 'Versuri',
                                style: TextStyle(
                                  fontSize: 12,
                                  color: _isMusicianView ? AppColors.coral : AppColors.gray300,
                                  fontWeight: FontWeight.w600,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                      const SizedBox(width: 12),
                      Row(
                        children: [
                          Container(
                            width: 8,
                            height: 8,
                            decoration: const BoxDecoration(
                              color: AppColors.coral,
                              shape: BoxShape.circle,
                            ),
                          ),
                          const SizedBox(width: 6),
                          const Text(
                            'LIVE',
                            style: TextStyle(fontSize: 12, color: AppColors.coral, fontWeight: FontWeight.w600),
                          ),
                        ],
                      ),
                    ],
                  ),
                ],
              ),
            ),

            // Song Info
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              child: Column(
                children: [
                  Text(
                    song['title'],
                    style: const TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.w700,
                      color: AppColors.white,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 6),
                  Text(
                    '${song['artist']} • ${song['key']} major${song['capo'] > 0 ? ' (capo ${song['capo']})' : ''}',
                    style: const TextStyle(fontSize: 14, color: AppColors.gray300),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 20),

            // Content - Lyrics or Muzician View
            Expanded(
              child: _isMusicianView 
                  ? _buildMusicianView(song)
                  : _buildLyricsView(song),
            ),

            // Progress
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              child: Column(
                children: [
                  Container(
                    height: 4,
                    decoration: BoxDecoration(
                      color: AppColors.surfaceElevated,
                      borderRadius: BorderRadius.circular(2),
                    ),
                    child: FractionallySizedBox(
                      alignment: Alignment.centerLeft,
                      widthFactor: _progress,
                      child: Container(
                        decoration: BoxDecoration(
                          gradient: const LinearGradient(
                            colors: [AppColors.coral, AppColors.coralLight],
                          ),
                          borderRadius: BorderRadius.circular(2),
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 8),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        '${((_progress * (song['duration'] as int)) / 60).floor()}:${((_progress * (song['duration'] as int)) % 60).floor().toString().padLeft(2, '0')}',
                        style: const TextStyle(fontSize: 12, color: AppColors.gray300),
                      ),
                      Text(
                        '${((song['duration'] as int) / 60).floor()}:${((song['duration'] as int) % 60).toString().padLeft(2, '0')}',
                        style: const TextStyle(fontSize: 12, color: AppColors.gray300),
                      ),
                    ],
                  ),
                ],
              ),
            ),

            // Controls
            Padding(
              padding: const EdgeInsets.all(20),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  _controlButton(Icons.skip_previous, () {
                    if (_currentSong > 0) {
                      setState(() {
                        _currentSong--;
                        _progress = 0;
                      });
                    }
                  }),
                  const SizedBox(width: 32),
                  Container(
                    width: 64,
                    height: 64,
                    decoration: const BoxDecoration(
                      gradient: LinearGradient(
                        colors: [AppColors.coral, AppColors.coralLight],
                        begin: Alignment.topLeft,
                        end: Alignment.bottomRight,
                      ),
                      shape: BoxShape.circle,
                    ),
                    child: IconButton(
                      icon: Icon(
                        _isPlaying ? Icons.pause : Icons.play_arrow,
                        color: AppColors.white,
                        size: 28,
                      ),
                      onPressed: () => setState(() => _isPlaying = !_isPlaying),
                    ),
                  ),
                  const SizedBox(width: 32),
                  _controlButton(Icons.skip_next, () {
                    if (_currentSong < _setlist.length - 1) {
                      setState(() {
                        _currentSong++;
                        _progress = 0;
                      });
                    }
                  }),
                  const SizedBox(width: 32),
                  _controlButton(Icons.repeat, () {}),
                ],
              ),
            ),

            // Next Song
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                border: Border(top: BorderSide(color: AppColors.surfaceElevated)),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text('Următor: ', style: TextStyle(fontSize: 13, color: AppColors.gray300)),
                  Text(
                    _currentSong < _setlist.length - 1 
                        ? _setlist[_currentSong + 1]['title'] 
                        : 'Sfârșit program',
                    style: const TextStyle(fontSize: 13, color: AppColors.coral, fontWeight: FontWeight.w600),
                  ),
                ],
              ),
            ),

            // Close Button
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
              child: TextButton(
                onPressed: () => Navigator.pop(context),
                style: TextButton.styleFrom(
                  backgroundColor: AppColors.surfaceElevated,
                  foregroundColor: AppColors.white,
                  padding: const EdgeInsets.all(16),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  minimumSize: const Size(double.infinity, 50),
                ),
                child: const Text('Închide Live Mode', style: TextStyle(fontSize: 16, fontWeight: FontWeight.w600)),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildLyricsView(Map<String, dynamic> song) {
    return SingleChildScrollView(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: Text(
        song['lyrics'],
        style: const TextStyle(
          fontSize: 22,
          height: 1.8,
          color: AppColors.white,
          fontWeight: FontWeight.w400,
        ),
        textAlign: TextAlign.center,
      ),
    );
  }

  Widget _buildMusicianView(Map<String, dynamic> song) {
    final lines = (song['chords'] as String).split('
');

    return SingleChildScrollView(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: lines.map((line) {
          if (line.startsWith('[')) {
            // Section label
            return Padding(
              padding: const EdgeInsets.only(top: 16, bottom: 8),
              child: Text(
                line,
                style: const TextStyle(
                  fontSize: 14,
                  fontWeight: FontWeight.w700,
                  color: AppColors.coral,
                  letterSpacing: 1,
                ),
              ),
            );
          } else if (line.trim().isEmpty) {
            return const SizedBox(height: 8);
          } else {
            // Check if line has chords (contains uppercase letters at start)
            final parts = line.split(RegExp(r'\s{2,}'));
            if (parts.length > 1 && _looksLikeChords(parts[0])) {
              // Chord + lyrics line
              return Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Chords
                  Text(
                    parts[0],
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w700,
                      color: AppColors.coral,
                      height: 1.4,
                    ),
                  ),
                  // Lyrics
                  if (parts.length > 1)
                    Text(
                      parts.sublist(1).join('   '),
                      style: const TextStyle(
                        fontSize: 18,
                        color: AppColors.white,
                        height: 1.4,
                      ),
                    ),
                  const SizedBox(height: 4),
                ],
              );
            } else {
              return Text(
                line,
                style: const TextStyle(
                  fontSize: 18,
                  color: AppColors.white,
                  height: 1.6,
                ),
              );
            }
          }
        }).toList(),
      ),
    );
  }

  bool _looksLikeChords(String text) {
    final chordPattern = RegExp(r'^[A-G][#b]?[mM]?[0-9]?(/[A-G][#b]?)?$');
    return chordPattern.hasMatch(text.trim());
  }

  Widget _controlButton(IconData icon, VoidCallback onPressed) {
    return IconButton(
      icon: Icon(icon, color: AppColors.white, size: 28),
      onPressed: onPressed,
    );
  }
}
