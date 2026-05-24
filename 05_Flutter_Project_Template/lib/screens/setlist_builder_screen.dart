import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class SetlistBuilderScreen extends StatefulWidget {
  const SetlistBuilderScreen({super.key});

  @override
  State<SetlistBuilderScreen> createState() => _SetlistBuilderScreenState();
}

class _SetlistBuilderScreenState extends State<SetlistBuilderScreen> {
  final TextEditingController _themeController = TextEditingController(text: 'Identitate');
  final TextEditingController _dateController = TextEditingController(text: '25 Mai 2025');
  final TextEditingController _notesController = TextEditingController();

  int _targetDuration = 48; // minutes
  List<String> _tags = ['#tineret', '#adorare'];
  String _selectedTag = '';

  final List<String> availableTags = [
    '#tineret', '#adorare', '#predică', '#mărturie', 
    '#botez', '#comuniune', '#rugăciune', '#laude'
  ];

  // Service Flow Segments
  final List<Map<String, dynamic>> _segments = [
    {'name': 'Intro', 'type': 'intro', 'duration': 5, 'color': AppColors.gray500},
    {'name': 'Worship', 'type': 'worship', 'duration': 25, 'color': AppColors.coral},
    {'name': 'Moment Special', 'type': 'special', 'duration': 10, 'color': AppColors.indigo},
    {'name': 'Predică', 'type': 'sermon', 'duration': 20, 'color': AppColors.warning},
    {'name': 'Trimitere', 'type': 'sending', 'duration': 8, 'color': AppColors.success},
  ];

  final List<Map<String, dynamic>> _setlist = [
    {
      'title': 'Oceans (Where Feet May Fail)', 
      'artist': 'Hillsong United', 
      'key': 'Do', 
      'duration': 272,
      'status': 'repertoire',
      'lead': 'Andrei',
      'guitar': 'Maria',
      'drums': 'Alex',
      'segment': 'worship'
    },
    {
      'title': 'Way Maker', 
      'artist': 'Sinach', 
      'key': 'Sol', 
      'duration': 345,
      'status': 'repertoire',
      'lead': 'Maria',
      'guitar': 'Andrei',
      'drums': 'Alex',
      'segment': 'worship'
    },
    {
      'title': 'Graves Into Gardens', 
      'artist': 'Elevation Worship', 
      'key': 'La m', 
      'duration': 258,
      'status': 'repertoire',
      'lead': 'Andrei',
      'guitar': 'Cristina',
      'drums': 'Alex',
      'segment': 'worship'
    },
    {
      'title': 'What A Beautiful Name', 
      'artist': 'Hillsong Worship', 
      'key': 'Re', 
      'duration': 320,
      'status': 'repertoire',
      'lead': 'Elena',
      'guitar': 'Andrei',
      'drums': 'Alex',
      'segment': 'special'
    },
    {
      'title': 'Goodness of God', 
      'artist': 'Bethel Music', 
      'key': 'Do', 
      'duration': 292,
      'status': 'repertoire',
      'lead': 'Andrei',
      'guitar': 'Maria',
      'drums': 'Alex',
      'segment': 'sending'
    },
  ];

  int get _totalDuration {
    return _setlist.fold(0, (sum, song) => sum + (song['duration'] as int));
  }

  String get _durationText {
    final minutes = (_totalDuration / 60).ceil();
    return '$minutes min';
  }

  bool get _isOverTarget {
    return (_totalDuration / 60).ceil() > _targetDuration;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.black,
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.white),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('Program Nou'),
        actions: [
          IconButton(
            icon: const Icon(Icons.check, color: AppColors.coral),
            onPressed: () {},
          ),
        ],
      ),
      body: Column(
        children: [
          // Program Info Section
          Container(
            color: AppColors.surface,
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _inputField('Tema programului', _themeController),
                const SizedBox(height: 12),
                _inputField('Data', _dateController),
                const SizedBox(height: 12),
                _inputField('Notițe pentru echipă...', _notesController, maxLines: 2),
                const SizedBox(height: 16),

                // Tags
                Text(
                  'TAG-URI',
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                    color: AppColors.gray300,
                    letterSpacing: 1,
                  ),
                ),
                const SizedBox(height: 8),
                Wrap(
                  spacing: 8,
                  runSpacing: 8,
                  children: [
                    ..._tags.map((tag) => _buildTagChip(tag, true)),
                    ...availableTags
                        .where((t) => !_tags.contains(t))
                        .map((tag) => _buildTagChip(tag, false)),
                  ],
                ),
                const SizedBox(height: 16),

                // Target Duration with Alert
                Row(
                  children: [
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            'DURATĂ TARGET',
                            style: TextStyle(
                              fontSize: 12,
                              fontWeight: FontWeight.w600,
                              color: AppColors.gray300,
                              letterSpacing: 1,
                            ),
                          ),
                          const SizedBox(height: 8),
                          Row(
                            children: [
                              _durationButton(45),
                              const SizedBox(width: 8),
                              _durationButton(60),
                              const SizedBox(width: 8),
                              _durationButton(90),
                            ],
                          ),
                        ],
                      ),
                    ),
                    Container(
                      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                      decoration: BoxDecoration(
                        color: _isOverTarget 
                            ? AppColors.coral.withOpacity(0.15) 
                            : AppColors.success.withOpacity(0.15),
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(
                          color: _isOverTarget ? AppColors.coral : AppColors.success,
                          width: 1,
                        ),
                      ),
                      child: Column(
                        children: [
                          Text(
                            _durationText,
                            style: TextStyle(
                              fontSize: 20,
                              fontWeight: FontWeight.w700,
                              color: _isOverTarget ? AppColors.coral : AppColors.success,
                            ),
                          ),
                          Text(
                            '/ $_targetDuration min',
                            style: TextStyle(
                              fontSize: 12,
                              color: AppColors.gray300,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
                if (_isOverTarget)
                  Padding(
                    padding: const EdgeInsets.only(top: 8),
                    child: Row(
                      children: [
                        Icon(Icons.warning_amber, color: AppColors.coral, size: 16),
                        const SizedBox(width: 6),
                        Text(
                          'Depășești targetul cu ${(_totalDuration / 60).ceil() - _targetDuration} min!',
                          style: TextStyle(
                            fontSize: 13,
                            color: AppColors.coral,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                      ],
                    ),
                  ),
              ],
            ),
          ),

          // Service Flow Segments
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'FLOW SERVICIU',
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                    color: AppColors.gray300,
                    letterSpacing: 1,
                  ),
                ),
                const SizedBox(height: 8),
                SizedBox(
                  height: 80,
                  child: ListView.builder(
                    scrollDirection: Axis.horizontal,
                    itemCount: _segments.length,
                    itemBuilder: (context, index) {
                      final segment = _segments[index];
                      final segmentSongs = _setlist.where((s) => s['segment'] == segment['type']).toList();
                      final segmentDuration = segmentSongs.fold(0, (sum, s) => sum + (s['duration'] as int));

                      return Container(
                        width: 120,
                        margin: const EdgeInsets.only(right: 10),
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: AppColors.surface,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(
                            color: segment['color'] as Color,
                            width: 2,
                          ),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              segment['name'] as String,
                              style: TextStyle(
                                fontSize: 13,
                                fontWeight: FontWeight.w600,
                                color: segment['color'] as Color,
                              ),
                            ),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text(
                                  '${segmentSongs.length} 🎵',
                                  style: const TextStyle(
                                    fontSize: 12,
                                    color: AppColors.gray300,
                                  ),
                                ),
                                Text(
                                  '${(segmentDuration / 60).ceil()}m',
                                  style: const TextStyle(
                                    fontSize: 12,
                                    color: AppColors.gray300,
                                  ),
                                ),
                              ],
                            ),
                          ],
                        ),
                      );
                    },
                  ),
                ),
              ],
            ),
          ),

          // Setlist Header
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'SETLIST',
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                    color: AppColors.gray300,
                    letterSpacing: 1,
                  ),
                ),
                Text(
                  '${_setlist.length} cântări • $_durationText',
                  style: const TextStyle(fontSize: 13, color: AppColors.gray300),
                ),
              ],
            ),
          ),

          // Setlist Items
          Expanded(
            child: ReorderableListView(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              children: _setlist.asMap().entries.map((entry) {
                final index = entry.key;
                final song = entry.value;
                return _buildSetlistItem(index, song);
              }).toList(),
              onReorder: (oldIndex, newIndex) {
                setState(() {
                  if (newIndex > oldIndex) newIndex--;
                  final item = _setlist.removeAt(oldIndex);
                  _setlist.insert(newIndex, item);
                });
              },
            ),
          ),

          // Footer Buttons
          Container(
            padding: const EdgeInsets.all(20),
            decoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.bottomCenter,
                end: Alignment.topCenter,
                colors: [AppColors.black, AppColors.black.withOpacity(0)],
              ),
            ),
            child: Column(
              children: [
                _primaryButton('▶  Live Mode', () => Navigator.pushNamed(context, '/live')),
                const SizedBox(height: 10),
                _secondaryButton('💾  Salvează & Trimite Echipei', () {}),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _inputField(String hint, TextEditingController controller, {int maxLines = 1}) {
    return TextField(
      controller: controller,
      maxLines: maxLines,
      style: const TextStyle(color: AppColors.white, fontSize: 16),
      decoration: InputDecoration(
        hintText: hint,
        filled: true,
        fillColor: AppColors.surfaceElevated,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
        contentPadding: const EdgeInsets.all(14),
        hintStyle: const TextStyle(color: AppColors.gray500, fontSize: 15),
      ),
    );
  }

  Widget _buildTagChip(String tag, bool isSelected) {
    return GestureDetector(
      onTap: () {
        setState(() {
          if (isSelected) {
            _tags.remove(tag);
          } else {
            _tags.add(tag);
          }
        });
      },
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
        decoration: BoxDecoration(
          color: isSelected ? AppColors.coral.withOpacity(0.2) : AppColors.surfaceElevated,
          borderRadius: BorderRadius.circular(20),
          border: Border.all(
            color: isSelected ? AppColors.coral : Colors.transparent,
            width: 1,
          ),
        ),
        child: Text(
          tag,
          style: TextStyle(
            fontSize: 13,
            color: isSelected ? AppColors.coral : AppColors.gray300,
            fontWeight: isSelected ? FontWeight.w600 : FontWeight.w400,
          ),
        ),
      ),
    );
  }

  Widget _durationButton(int minutes) {
    final isSelected = _targetDuration == minutes;
    return GestureDetector(
      onTap: () => setState(() => _targetDuration = minutes),
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        decoration: BoxDecoration(
          color: isSelected ? AppColors.coral : AppColors.surfaceElevated,
          borderRadius: BorderRadius.circular(10),
        ),
        child: Text(
          '$minutes min',
          style: TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.w600,
            color: isSelected ? AppColors.white : AppColors.gray300,
          ),
        ),
      ),
    );
  }

  Widget _buildSetlistItem(int index, Map<String, dynamic> song) {
    final statusColors = {
      'repertoire': AppColors.success,
      'learning': AppColors.warning,
      'new': AppColors.coral,
    };
    final statusIcons = {
      'repertoire': '✅',
      'learning': '🔄',
      'new': '🆕',
    };

    return Container(
      key: ValueKey(index),
      margin: const EdgeInsets.only(bottom: 10),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: AppColors.surface,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: statusColors[song['status']] ?? AppColors.gray500,
          width: 1,
        ),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(Icons.drag_handle, color: AppColors.gray500, size: 20),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      song['title'],
                      style: const TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.w500,
                        color: AppColors.white,
                      ),
                    ),
                    const SizedBox(height: 2),
                    Text(
                      '${song['artist']} • ${song['key']} major • ${(song['duration'] / 60).ceil()}:${(song['duration'] % 60).toString().padLeft(2, '0')}',
                      style: const TextStyle(fontSize: 12, color: AppColors.gray300),
                    ),
                  ],
                ),
              ),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                decoration: BoxDecoration(
                  color: (statusColors[song['status']] ?? AppColors.gray500).withOpacity(0.15),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Text(
                  '${statusIcons[song['status']]} ${song['status']}',
                  style: TextStyle(
                    fontSize: 11,
                    color: statusColors[song['status']] ?? AppColors.gray500,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 10),
          // Musician assignments
          Container(
            padding: const EdgeInsets.all(10),
            decoration: BoxDecoration(
              color: AppColors.surfaceElevated,
              borderRadius: BorderRadius.circular(8),
            ),
            child: Wrap(
              spacing: 12,
              runSpacing: 6,
              children: [
                _musicianBadge('🎤 Lead', song['lead']),
                _musicianBadge('🎸 Chitară', song['guitar']),
                _musicianBadge('🥁 Tobe', song['drums']),
              ],
            ),
          ),
          const SizedBox(height: 8),
          Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              _actionButton(Icons.edit, AppColors.gray300),
              const SizedBox(width: 8),
              _actionButton(Icons.delete, AppColors.coral),
            ],
          ),
        ],
      ),
    );
  }

  Widget _musicianBadge(String role, String name) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(
          role,
          style: const TextStyle(
            fontSize: 11,
            color: AppColors.gray300,
          ),
        ),
        const SizedBox(width: 4),
        Text(
          name,
          style: const TextStyle(
            fontSize: 12,
            color: AppColors.white,
            fontWeight: FontWeight.w500,
          ),
        ),
      ],
    );
  }

  Widget _actionButton(IconData icon, Color color) {
    return Container(
      width: 32,
      height: 32,
      decoration: BoxDecoration(
        color: AppColors.surfaceElevated,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Icon(icon, color: color, size: 16),
    );
  }

  Widget _primaryButton(String text, VoidCallback onPressed) {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        onPressed: onPressed,
        style: ElevatedButton.styleFrom(
          backgroundColor: AppColors.coral,
          foregroundColor: AppColors.white,
          padding: const EdgeInsets.all(16),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          elevation: 0,
        ),
        child: Text(
          text,
          style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
        ),
      ),
    );
  }

  Widget _secondaryButton(String text, VoidCallback onPressed) {
    return SizedBox(
      width: double.infinity,
      child: OutlinedButton(
        onPressed: onPressed,
        style: OutlinedButton.styleFrom(
          foregroundColor: AppColors.coral,
          side: const BorderSide(color: AppColors.coral, width: 1.5),
          padding: const EdgeInsets.all(16),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        ),
        child: Text(
          text,
          style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600),
        ),
      ),
    );
  }
}
