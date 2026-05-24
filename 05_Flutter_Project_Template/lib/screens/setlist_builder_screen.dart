import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../theme/app_theme.dart';
import '../models/song.dart';
import '../models/program.dart';
import '../models/setlist_item.dart';
import '../providers/song_provider.dart';
import '../providers/program_provider.dart';

class SetlistBuilderScreen extends StatefulWidget {
  final String? programId;
  const SetlistBuilderScreen({super.key, this.programId});

  @override
  State<SetlistBuilderScreen> createState() => _SetlistBuilderScreenState();
}

class _SetlistBuilderScreenState extends State<SetlistBuilderScreen> {
  late TextEditingController _themeController;
  late TextEditingController _dateController;
  late TextEditingController _notesController;

  int _targetDuration = 60;
  List<String> _tags = [];
  List<SetlistItem> _setlist = [];
  String? _programId;

  final List<String> availableTags = [
    '#tineret', '#adorare', '#predică', '#mărturie',
    '#botez', '#comuniune', '#rugăciune', '#laude',
  ];

  final List<Map<String, dynamic>> _segments = [
    {'name': 'Intro', 'type': 'intro', 'color': AppColors.gray500},
    {'name': 'Worship', 'type': 'worship', 'color': AppColors.coral},
    {'name': 'Moment Special', 'type': 'special', 'color': AppColors.indigo},
    {'name': 'Predică', 'type': 'sermon', 'color': AppColors.warning},
    {'name': 'Trimitere', 'type': 'sending', 'color': AppColors.success},
  ];

  @override
  void initState() {
    super.initState();
    _programId = widget.programId;
    _themeController = TextEditingController(text: 'Program Nou');
    _dateController = TextEditingController(text: _formatDate(DateTime.now()));
    _notesController = TextEditingController();

    if (widget.programId != null) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        final program = context.read<ProgramProvider>().getProgramById(widget.programId!);
        if (program != null) {
          _themeController.text = program.title;
          _dateController.text = program.date;
          _notesController.text = program.notes;
          setState(() {
            _targetDuration = program.targetDurationMinutes;
            _tags = List.from(program.tags);
            _setlist = List.from(program.setlist);
          });
        }
      });
    }
  }

  @override
  void dispose() {
    _themeController.dispose();
    _dateController.dispose();
    _notesController.dispose();
    super.dispose();
  }

  String _formatDate(DateTime dt) {
    const months = [
      '', 'Ian', 'Feb', 'Mar', 'Apr', 'Mai', 'Iun',
      'Iul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec',
    ];
    return '${dt.day} ${months[dt.month]} ${dt.year}';
  }

  int _totalDurationSeconds(SongProvider songProvider) {
    return _setlist.fold(0, (sum, item) {
      final song = songProvider.getSongById(item.songId);
      return sum + (song?.durationSeconds ?? 0);
    });
  }

  String _durationText(SongProvider songProvider) {
    final total = _totalDurationSeconds(songProvider);
    return '${(total / 60).ceil()} min';
  }

  bool _isOverTarget(SongProvider songProvider) {
    return (_totalDurationSeconds(songProvider) / 60).ceil() > _targetDuration;
  }

  void _addFromRepertoire() {
    final songProvider = context.read<SongProvider>();
    final allSongs = songProvider.songs;

    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.surface,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (ctx) {
        String query = '';
        return StatefulBuilder(
          builder: (ctx, setSheetState) => DraggableScrollableSheet(
            initialChildSize: 0.7,
            minChildSize: 0.5,
            maxChildSize: 0.95,
            expand: false,
            builder: (ctx, scrollCtrl) => Padding(
              padding: const EdgeInsets.all(20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Center(
                    child: Container(
                      width: 40,
                      height: 4,
                      decoration: BoxDecoration(
                        color: AppColors.gray500,
                        borderRadius: BorderRadius.circular(2),
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  const Text(
                    'Alege din repertoriu',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.w700,
                      color: AppColors.white,
                    ),
                  ),
                  const SizedBox(height: 12),
                  TextField(
                    onChanged: (v) => setSheetState(() => query = v),
                    style: const TextStyle(color: AppColors.white),
                    decoration: InputDecoration(
                      hintText: '🔍 Caută...',
                      filled: true,
                      fillColor: AppColors.surfaceElevated,
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(10),
                        borderSide: BorderSide.none,
                      ),
                      contentPadding: const EdgeInsets.symmetric(
                          horizontal: 14, vertical: 10),
                      hintStyle: const TextStyle(color: AppColors.gray500),
                    ),
                  ),
                  const SizedBox(height: 12),
                  Expanded(
                    child: ListView(
                      controller: scrollCtrl,
                      children: allSongs
                          .where((s) =>
                              query.isEmpty ||
                              s.title.toLowerCase().contains(query.toLowerCase()) ||
                              s.artist.toLowerCase().contains(query.toLowerCase()))
                          .map((song) => ListTile(
                                contentPadding: const EdgeInsets.symmetric(
                                    vertical: 4),
                                title: Text(
                                  song.title,
                                  style: const TextStyle(
                                      color: AppColors.white, fontSize: 15),
                                ),
                                subtitle: Text(
                                  '${song.artist} • ${song.key}',
                                  style: const TextStyle(
                                      color: AppColors.gray300, fontSize: 12),
                                ),
                                trailing: Container(
                                  padding: const EdgeInsets.symmetric(
                                      horizontal: 8, vertical: 4),
                                  decoration: BoxDecoration(
                                    color: AppColors.coral.withOpacity(0.15),
                                    borderRadius: BorderRadius.circular(8),
                                  ),
                                  child: const Text(
                                    '+ Adaugă',
                                    style: TextStyle(
                                      color: AppColors.coral,
                                      fontSize: 12,
                                      fontWeight: FontWeight.w600,
                                    ),
                                  ),
                                ),
                                onTap: () {
                                  setState(() {
                                    _setlist.add(SetlistItem(
                                      id: '${DateTime.now().millisecondsSinceEpoch}_${_setlist.length}',
                                      songId: song.id,
                                      segment: 'worship',
                                    ));
                                  });
                                  Navigator.pop(ctx);
                                },
                              ))
                          .toList(),
                    ),
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }

  void _removeFromSetlist(String itemId) {
    setState(() {
      _setlist.removeWhere((i) => i.id == itemId);
    });
  }

  void _saveProgram() {
    final programProvider = context.read<ProgramProvider>();
    final id = _programId ?? programProvider.generateId();

    final title = _themeController.text.trim();
    final program = Program(
      id: id,
      title: title.isEmpty ? 'Program' : title,
      theme: title.isEmpty ? 'Program' : title,
      date: _dateController.text.trim(),
      status: _programId == null ? 'draft' : 'ready',
      setlist: _setlist,
      tags: _tags,
      targetDurationMinutes: _targetDuration,
      notes: _notesController.text.trim(),
    );

    if (_programId == null) {
      programProvider.addProgram(program);
      setState(() => _programId = id);
    } else {
      programProvider.updateProgram(program);
    }

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('"${program.title}" salvat!'),
        backgroundColor: AppColors.success,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final songProvider = context.watch<SongProvider>();
    final totalMin = (_totalDurationSeconds(songProvider) / 60).ceil();
    final overTarget = _isOverTarget(songProvider);

    return Scaffold(
      backgroundColor: AppColors.black,
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.white),
          onPressed: () => Navigator.pop(context),
        ),
        title: Text(_programId != null ? 'Editează Program' : 'Program Nou'),
        actions: [
          IconButton(
            icon: const Icon(Icons.check, color: AppColors.coral),
            onPressed: _saveProgram,
          ),
        ],
      ),
      body: Column(
        children: [
          // Program Info
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
                const Text(
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
                  children: availableTags
                      .map((tag) => _buildTagChip(tag, _tags.contains(tag)))
                      .toList(),
                ),
                const SizedBox(height: 16),
                Row(
                  children: [
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text(
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
                      padding: const EdgeInsets.symmetric(
                          horizontal: 16, vertical: 12),
                      decoration: BoxDecoration(
                        color: overTarget
                            ? AppColors.coral.withOpacity(0.15)
                            : AppColors.success.withOpacity(0.15),
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(
                          color: overTarget ? AppColors.coral : AppColors.success,
                          width: 1,
                        ),
                      ),
                      child: Column(
                        children: [
                          Text(
                            '$totalMin min',
                            style: TextStyle(
                              fontSize: 20,
                              fontWeight: FontWeight.w700,
                              color: overTarget ? AppColors.coral : AppColors.success,
                            ),
                          ),
                          Text(
                            '/ $_targetDuration min',
                            style: const TextStyle(
                                fontSize: 12, color: AppColors.gray300),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
                if (overTarget)
                  Padding(
                    padding: const EdgeInsets.only(top: 8),
                    child: Row(
                      children: [
                        const Icon(Icons.warning_amber,
                            color: AppColors.coral, size: 16),
                        const SizedBox(width: 6),
                        Text(
                          'Depășești targetul cu ${totalMin - _targetDuration} min!',
                          style: const TextStyle(
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

          // Service Flow
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text(
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
                      final seg = _segments[index];
                      final segItems = _setlist
                          .where((i) => i.segment == seg['type'])
                          .toList();
                      final segDuration = segItems.fold(0, (sum, item) {
                        final s = songProvider.getSongById(item.songId);
                        return sum + (s?.durationSeconds ?? 0);
                      });
                      return Container(
                        width: 120,
                        margin: const EdgeInsets.only(right: 10),
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: AppColors.surface,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(
                            color: seg['color'] as Color,
                            width: 2,
                          ),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              seg['name'] as String,
                              style: TextStyle(
                                fontSize: 13,
                                fontWeight: FontWeight.w600,
                                color: seg['color'] as Color,
                              ),
                            ),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text('${segItems.length} 🎵',
                                    style: const TextStyle(
                                        fontSize: 12, color: AppColors.gray300)),
                                Text('${(segDuration / 60).ceil()}m',
                                    style: const TextStyle(
                                        fontSize: 12, color: AppColors.gray300)),
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
                const Text(
                  'SETLIST',
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                    color: AppColors.gray300,
                    letterSpacing: 1,
                  ),
                ),
                Row(
                  children: [
                    Text(
                      '${_setlist.length} cântări • $totalMin min',
                      style:
                          const TextStyle(fontSize: 13, color: AppColors.gray300),
                    ),
                    const SizedBox(width: 12),
                    GestureDetector(
                      onTap: _addFromRepertoire,
                      child: Container(
                        padding: const EdgeInsets.symmetric(
                            horizontal: 10, vertical: 4),
                        decoration: BoxDecoration(
                          color: AppColors.coral,
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: const Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Icon(Icons.add, color: AppColors.white, size: 14),
                            SizedBox(width: 4),
                            Text(
                              'Adaugă',
                              style: TextStyle(
                                color: AppColors.white,
                                fontSize: 12,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),

          // Setlist
          Expanded(
            child: _setlist.isEmpty
                ? Center(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Text('🎵', style: TextStyle(fontSize: 48)),
                        const SizedBox(height: 12),
                        const Text(
                          'Nicio cântare adăugată încă',
                          style: TextStyle(
                              color: AppColors.gray300, fontSize: 15),
                        ),
                        const SizedBox(height: 16),
                        ElevatedButton.icon(
                          onPressed: _addFromRepertoire,
                          icon: const Icon(Icons.add),
                          label: const Text('Alege din repertoriu'),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: AppColors.coral,
                            foregroundColor: AppColors.white,
                            shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(12)),
                          ),
                        ),
                      ],
                    ),
                  )
                : ReorderableListView(
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    children: _setlist.asMap().entries.map((entry) {
                      final index = entry.key;
                      final item = entry.value;
                      final song = songProvider.getSongById(item.songId);
                      return _buildSetlistItem(index, item, song);
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

          // Footer
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
                _primaryButton(
                  '▶  Live Mode',
                  _setlist.isEmpty
                      ? null
                      : () => Navigator.pushNamed(context, '/live'),
                ),
                const SizedBox(height: 10),
                _secondaryButton('💾  Salvează & Trimite Echipei', _saveProgram),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _inputField(String hint, TextEditingController controller,
      {int maxLines = 1}) {
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
          color: isSelected
              ? AppColors.coral.withOpacity(0.2)
              : AppColors.surfaceElevated,
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

  Widget _buildSetlistItem(int index, SetlistItem item, Song? song) {
    if (song == null) {
      return Container(
        key: ValueKey(item.id),
        margin: const EdgeInsets.only(bottom: 10),
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: AppColors.surface,
          borderRadius: BorderRadius.circular(12),
        ),
        child: const Text('Cântare ștearsă',
            style: TextStyle(color: AppColors.gray500)),
      );
    }

    const statusColors = {
      'repertoire': AppColors.success,
      'learning': AppColors.warning,
      'new': AppColors.coral,
    };
    const statusIcons = {
      'repertoire': '✅',
      'learning': '🔄',
      'new': '🆕',
    };

    final statusColor = statusColors[song.status] ?? AppColors.gray500;
    final statusIcon = statusIcons[song.status] ?? '🎵';

    return Container(
      key: ValueKey(item.id),
      margin: const EdgeInsets.only(bottom: 10),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: AppColors.surface,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: statusColor, width: 1),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              const Icon(Icons.drag_handle, color: AppColors.gray500, size: 20),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      song.title,
                      style: const TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.w500,
                        color: AppColors.white,
                      ),
                    ),
                    const SizedBox(height: 2),
                    Text(
                      '${song.artist} • ${song.key} • ${(song.durationSeconds / 60).floor()}:${(song.durationSeconds % 60).toString().padLeft(2, '0')}',
                      style: const TextStyle(
                          fontSize: 12, color: AppColors.gray300),
                    ),
                  ],
                ),
              ),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                decoration: BoxDecoration(
                  color: statusColor.withOpacity(0.15),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Text(
                  '$statusIcon ${song.status}',
                  style: TextStyle(
                    fontSize: 11,
                    color: statusColor,
                    fontWeight: FontWeight.w600,
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 10),
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
                _musicianBadge('🎤 Lead', song.lead),
                _musicianBadge('🎸 Chitară', song.guitar),
                _musicianBadge('🥁 Tobe', song.drums),
              ],
            ),
          ),
          const SizedBox(height: 8),
          Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              _actionButton(
                Icons.swap_vert,
                AppColors.gray300,
                () => _changeSegment(item),
              ),
              const SizedBox(width: 8),
              _actionButton(
                Icons.delete,
                AppColors.coral,
                () => _removeFromSetlist(item.id),
              ),
            ],
          ),
        ],
      ),
    );
  }

  void _changeSegment(SetlistItem item) {
    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.surface,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (ctx) => Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Schimbă segment',
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.w700,
                color: AppColors.white,
              ),
            ),
            const SizedBox(height: 12),
            ..._segments.map((seg) => ListTile(
                  contentPadding: EdgeInsets.zero,
                  title: Text(
                    seg['name'] as String,
                    style: TextStyle(
                      color: item.segment == seg['type']
                          ? seg['color'] as Color
                          : AppColors.white,
                      fontWeight: item.segment == seg['type']
                          ? FontWeight.w600
                          : FontWeight.w400,
                    ),
                  ),
                  trailing: item.segment == seg['type']
                      ? const Icon(Icons.check, color: AppColors.coral)
                      : null,
                  onTap: () {
                    setState(() {
                      final idx = _setlist.indexWhere((i) => i.id == item.id);
                      if (idx >= 0) {
                        _setlist[idx] = item.copyWith(segment: seg['type'] as String);
                      }
                    });
                    Navigator.pop(ctx);
                  },
                )),
          ],
        ),
      ),
    );
  }

  Widget _musicianBadge(String role, String name) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(role,
            style: const TextStyle(fontSize: 11, color: AppColors.gray300)),
        const SizedBox(width: 4),
        Text(name,
            style: const TextStyle(
                fontSize: 12, color: AppColors.white, fontWeight: FontWeight.w500)),
      ],
    );
  }

  Widget _actionButton(IconData icon, Color color, VoidCallback onPressed) {
    return GestureDetector(
      onTap: onPressed,
      child: Container(
        width: 32,
        height: 32,
        decoration: BoxDecoration(
          color: AppColors.surfaceElevated,
          borderRadius: BorderRadius.circular(8),
        ),
        child: Icon(icon, color: color, size: 16),
      ),
    );
  }

  Widget _primaryButton(String text, VoidCallback? onPressed) {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        onPressed: onPressed,
        style: ElevatedButton.styleFrom(
          backgroundColor: onPressed != null ? AppColors.coral : AppColors.gray500,
          foregroundColor: AppColors.white,
          padding: const EdgeInsets.all(16),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          elevation: 0,
        ),
        child: Text(text,
            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600)),
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
        child: Text(text,
            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w600)),
      ),
    );
  }
}
