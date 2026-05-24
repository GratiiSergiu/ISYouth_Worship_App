import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../theme/app_theme.dart';
import '../models/song.dart';
import '../providers/song_provider.dart';
import '../providers/program_provider.dart';
import '../models/setlist_item.dart';

class SongsScreen extends StatefulWidget {
  const SongsScreen({super.key});

  @override
  State<SongsScreen> createState() => _SongsScreenState();
}

class _SongsScreenState extends State<SongsScreen> {
  String _searchQuery = '';
  String _activeFilter = 'Toate';
  bool _showTranspose = false;
  int _capo = 0;
  String _currentKey = 'Do';

  final List<String> filters = ['Toate', 'Repertoire', 'În lucru', 'Nou', 'Favorite'];
  final List<String> keys = [
    'Do', 'Do#', 'Re', 'Re#', 'Mi', 'Fa', 'Fa#', 'Sol', 'Sol#', 'La', 'La#', 'Si'
  ];

  void _showSongDetail(Song song) {
    setState(() {
      _currentKey = song.key.replaceAll(' m', '');
      _capo = 0;
      _showTranspose = false;
    });

    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.surface,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (context) => StatefulBuilder(
        builder: (context, setModalState) {
          return DraggableScrollableSheet(
            initialChildSize: 0.85,
            minChildSize: 0.5,
            maxChildSize: 0.95,
            expand: false,
            builder: (context, scrollController) {
              return Container(
                padding: const EdgeInsets.all(20),
                child: SingleChildScrollView(
                  controller: scrollController,
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
                      const SizedBox(height: 20),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  song.title,
                                  style: const TextStyle(
                                    fontSize: 22,
                                    fontWeight: FontWeight.w700,
                                    color: AppColors.white,
                                  ),
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  song.artist,
                                  style: const TextStyle(
                                    fontSize: 16,
                                    color: AppColors.gray300,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          _buildStatusBadge(song.status),
                        ],
                      ),
                      const SizedBox(height: 20),
                      Container(
                        padding: const EdgeInsets.all(16),
                        decoration: BoxDecoration(
                          color: AppColors.surfaceElevated,
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceAround,
                          children: [
                            _metaItem('🎵', 'Cheie',
                                _currentKey + (_capo > 0 ? ' (capo $_capo)' : '')),
                            _metaItem(
                              '⏱',
                              'Durată',
                              '${(song.durationSeconds / 60).floor()}:${(song.durationSeconds % 60).toString().padLeft(2, '0')}',
                            ),
                            _metaItem('🥁', 'Tempo', '${song.tempo} BPM'),
                            _metaItem('📊', 'Folosit', '${song.usageCount}x'),
                          ],
                        ),
                      ),
                      const SizedBox(height: 20),
                      Container(
                        padding: const EdgeInsets.all(16),
                        decoration: BoxDecoration(
                          color: AppColors.surfaceElevated,
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                const Text(
                                  'TRANSPUNERE',
                                  style: TextStyle(
                                    fontSize: 12,
                                    fontWeight: FontWeight.w600,
                                    color: AppColors.gray300,
                                    letterSpacing: 1,
                                  ),
                                ),
                                GestureDetector(
                                  onTap: () => setModalState(
                                      () => _showTranspose = !_showTranspose),
                                  child: Text(
                                    _showTranspose ? '▲ Ascunde' : '▼ Arată',
                                    style: const TextStyle(
                                      fontSize: 13,
                                      color: AppColors.coral,
                                      fontWeight: FontWeight.w500,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                            if (_showTranspose) ...[
                              const SizedBox(height: 16),
                              const Text(
                                'Capo',
                                style: TextStyle(fontSize: 14, color: AppColors.gray300),
                              ),
                              const SizedBox(height: 8),
                              Row(
                                children: List.generate(7, (index) {
                                  return Padding(
                                    padding: const EdgeInsets.only(right: 8),
                                    child: GestureDetector(
                                      onTap: () => setModalState(() => _capo = index),
                                      child: Container(
                                        width: 40,
                                        height: 40,
                                        decoration: BoxDecoration(
                                          color: _capo == index
                                              ? AppColors.coral
                                              : AppColors.surface,
                                          borderRadius: BorderRadius.circular(10),
                                          border: Border.all(
                                            color: _capo == index
                                                ? AppColors.coral
                                                : AppColors.gray500,
                                          ),
                                        ),
                                        child: Center(
                                          child: Text(
                                            '$index',
                                            style: TextStyle(
                                              fontSize: 16,
                                              fontWeight: FontWeight.w600,
                                              color: _capo == index
                                                  ? AppColors.white
                                                  : AppColors.gray300,
                                            ),
                                          ),
                                        ),
                                      ),
                                    ),
                                  );
                                }),
                              ),
                              const SizedBox(height: 16),
                              const Text(
                                'Cheie nouă',
                                style: TextStyle(fontSize: 14, color: AppColors.gray300),
                              ),
                              const SizedBox(height: 8),
                              Wrap(
                                spacing: 8,
                                runSpacing: 8,
                                children: keys.map((key) {
                                  return GestureDetector(
                                    onTap: () => setModalState(() => _currentKey = key),
                                    child: Container(
                                      padding: const EdgeInsets.symmetric(
                                          horizontal: 14, vertical: 8),
                                      decoration: BoxDecoration(
                                        color: _currentKey == key
                                            ? AppColors.coral
                                            : AppColors.surface,
                                        borderRadius: BorderRadius.circular(8),
                                        border: Border.all(
                                          color: _currentKey == key
                                              ? AppColors.coral
                                              : AppColors.gray500,
                                        ),
                                      ),
                                      child: Text(
                                        key,
                                        style: TextStyle(
                                          fontSize: 14,
                                          fontWeight: FontWeight.w600,
                                          color: _currentKey == key
                                              ? AppColors.white
                                              : AppColors.gray300,
                                        ),
                                      ),
                                    ),
                                  );
                                }).toList(),
                              ),
                            ],
                          ],
                        ),
                      ),
                      const SizedBox(height: 20),
                      const Text(
                        'ECHIPA',
                        style: TextStyle(
                          fontSize: 12,
                          fontWeight: FontWeight.w600,
                          color: AppColors.gray300,
                          letterSpacing: 1,
                        ),
                      ),
                      const SizedBox(height: 12),
                      _teamAssignment('🎤 Lead Vocal', song.lead),
                      _teamAssignment('🎸 Chitară', song.guitar),
                      _teamAssignment('🥁 Tobe', song.drums),
                      const SizedBox(height: 20),
                      const Text(
                        'VERSURI & ACORDURI',
                        style: TextStyle(
                          fontSize: 12,
                          fontWeight: FontWeight.w600,
                          color: AppColors.gray300,
                          letterSpacing: 1,
                        ),
                      ),
                      const SizedBox(height: 12),
                      Container(
                        padding: const EdgeInsets.all(16),
                        decoration: BoxDecoration(
                          color: AppColors.black,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(color: AppColors.surfaceElevated),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text(
                              '[Verse 1]',
                              style: TextStyle(
                                fontSize: 14,
                                color: AppColors.coral,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                            const SizedBox(height: 8),
                            RichText(
                              text: TextSpan(
                                style: const TextStyle(
                                  fontSize: 16,
                                  height: 1.8,
                                  color: AppColors.white,
                                ),
                                children: [
                                  TextSpan(
                                    text: '$_currentKey         ',
                                    style: const TextStyle(
                                      color: AppColors.coral,
                                      fontWeight: FontWeight.w700,
                                      fontSize: 14,
                                    ),
                                  ),
                                  const TextSpan(text: '\n'),
                                  const TextSpan(text: 'You call me out upon the waters\n'),
                                  TextSpan(
                                    text: 'Sol                    La m',
                                    style: TextStyle(
                                      color: AppColors.coral.withOpacity(0.8),
                                      fontWeight: FontWeight.w600,
                                      fontSize: 14,
                                    ),
                                  ),
                                  const TextSpan(text: '\nThe great unknown where feet may fail\n'),
                                ],
                              ),
                            ),
                            Center(
                              child: TextButton(
                                onPressed: () {
                                  Navigator.pop(context);
                                  Navigator.pushNamed(context, '/live');
                                },
                                child: const Text(
                                  'Deschide Muzician View →',
                                  style: TextStyle(color: AppColors.coral),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 20),
                      Row(
                        children: [
                          Expanded(
                            child: ElevatedButton(
                              onPressed: () {
                                Navigator.pop(context);
                                _showAddToProgramDialog(song);
                              },
                              style: ElevatedButton.styleFrom(
                                backgroundColor: AppColors.coral,
                                foregroundColor: AppColors.white,
                                padding: const EdgeInsets.all(16),
                                shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(12)),
                              ),
                              child: const Text('Adaugă în program',
                                  style: TextStyle(fontWeight: FontWeight.w600)),
                            ),
                          ),
                          const SizedBox(width: 12),
                          _iconActionButton(
                            Icons.delete_outline,
                            AppColors.coral,
                            () {
                              Navigator.pop(context);
                              _confirmDelete(song);
                            },
                          ),
                        ],
                      ),
                      const SizedBox(height: 30),
                    ],
                  ),
                ),
              );
            },
          );
        },
      ),
    );
  }

  void _showAddToProgramDialog(Song song) {
    final programs = context.read<ProgramProvider>().programs;
    if (programs.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Nu există programe. Creează unul mai întâi.')),
      );
      return;
    }

    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.surface,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (ctx) {
        final programProvider = ctx.read<ProgramProvider>();
        return Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                'Adaugă în program',
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.w700,
                  color: AppColors.white,
                ),
              ),
              const SizedBox(height: 16),
              ...programs.map((program) => ListTile(
                    contentPadding: EdgeInsets.zero,
                    title: Text(
                      '"${program.title}"',
                      style: const TextStyle(color: AppColors.white, fontSize: 16),
                    ),
                    subtitle: Text(
                      program.date,
                      style: const TextStyle(color: AppColors.gray300, fontSize: 13),
                    ),
                    trailing: const Icon(Icons.add_circle_outline, color: AppColors.coral),
                    onTap: () {
                      final item = SetlistItem(
                        id: '${DateTime.now().millisecondsSinceEpoch}',
                        songId: song.id,
                      );
                      final updated = program.copyWith(
                        setlist: [...program.setlist, item],
                      );
                      programProvider.updateProgram(updated);
                      Navigator.pop(ctx);
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content:
                              Text('"${song.title}" adăugat în "${program.title}"'),
                        ),
                      );
                    },
                  )),
              const SizedBox(height: 8),
            ],
          ),
        );
      },
    );
  }

  void _confirmDelete(Song song) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        backgroundColor: AppColors.surface,
        title: const Text('Șterge cântare', style: TextStyle(color: AppColors.white)),
        content: Text(
          'Ești sigur că vrei să ștergi "${song.title}"?',
          style: const TextStyle(color: AppColors.gray300),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text('Anulează', style: TextStyle(color: AppColors.gray300)),
          ),
          TextButton(
            onPressed: () {
              context.read<SongProvider>().deleteSong(song.id);
              Navigator.pop(ctx);
            },
            child: const Text('Șterge', style: TextStyle(color: AppColors.coral)),
          ),
        ],
      ),
    );
  }

  void _showAddSongDialog() {
    final titleCtrl = TextEditingController();
    final artistCtrl = TextEditingController();
    final keyCtrl = TextEditingController(text: 'Do');
    final durationCtrl = TextEditingController(text: '3:30');
    final tempoCtrl = TextEditingController(text: '70');
    String status = 'new';

    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.surface,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (ctx) => StatefulBuilder(
        builder: (ctx, setSheetState) => Padding(
          padding: EdgeInsets.fromLTRB(
              20, 20, 20, MediaQuery.of(ctx).viewInsets.bottom + 20),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text(
                'Adaugă cântare nouă',
                style: TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.w700,
                  color: AppColors.white,
                ),
              ),
              const SizedBox(height: 16),
              _formField(titleCtrl, 'Titlu *'),
              const SizedBox(height: 12),
              _formField(artistCtrl, 'Artist'),
              const SizedBox(height: 12),
              Row(
                children: [
                  Expanded(child: _formField(keyCtrl, 'Cheie (ex: Do, Sol)')),
                  const SizedBox(width: 12),
                  Expanded(child: _formField(durationCtrl, 'Durată (m:ss)')),
                  const SizedBox(width: 12),
                  Expanded(child: _formField(tempoCtrl, 'BPM')),
                ],
              ),
              const SizedBox(height: 12),
              const Text(
                'Status',
                style: TextStyle(fontSize: 13, color: AppColors.gray300),
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  _statusChoice('new', 'Nou', status, (v) => setSheetState(() => status = v)),
                  const SizedBox(width: 8),
                  _statusChoice('learning', 'În lucru', status,
                      (v) => setSheetState(() => status = v)),
                  const SizedBox(width: 8),
                  _statusChoice('repertoire', 'Repertoire', status,
                      (v) => setSheetState(() => status = v)),
                ],
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: () {
                    final title = titleCtrl.text.trim();
                    if (title.isEmpty) return;

                    final parts = durationCtrl.text.split(':');
                    int seconds = 0;
                    if (parts.length == 2) {
                      seconds = (int.tryParse(parts[0]) ?? 0) * 60 +
                          (int.tryParse(parts[1]) ?? 0);
                    } else {
                      seconds = (int.tryParse(durationCtrl.text) ?? 3) * 60;
                    }

                    final song = Song(
                      id: context.read<SongProvider>().generateId(),
                      title: title,
                      artist: artistCtrl.text.trim().isEmpty
                          ? 'Necunoscut'
                          : artistCtrl.text.trim(),
                      key: keyCtrl.text.trim().isEmpty ? 'Do' : keyCtrl.text.trim(),
                      durationSeconds: seconds,
                      tempo: int.tryParse(tempoCtrl.text) ?? 70,
                      status: status,
                    );
                    context.read<SongProvider>().addSong(song);
                    Navigator.pop(ctx);
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: AppColors.coral,
                    foregroundColor: AppColors.white,
                    padding: const EdgeInsets.all(16),
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12)),
                  ),
                  child: const Text('Salvează',
                      style: TextStyle(fontSize: 16, fontWeight: FontWeight.w600)),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _formField(TextEditingController ctrl, String hint) {
    return TextField(
      controller: ctrl,
      style: const TextStyle(color: AppColors.white, fontSize: 15),
      decoration: InputDecoration(
        hintText: hint,
        filled: true,
        fillColor: AppColors.surfaceElevated,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(10),
          borderSide: BorderSide.none,
        ),
        contentPadding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
        hintStyle: const TextStyle(color: AppColors.gray500, fontSize: 14),
      ),
    );
  }

  Widget _statusChoice(
      String value, String label, String current, ValueChanged<String> onTap) {
    final selected = value == current;
    return GestureDetector(
      onTap: () => onTap(value),
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
        decoration: BoxDecoration(
          color: selected ? AppColors.coral : AppColors.surfaceElevated,
          borderRadius: BorderRadius.circular(8),
        ),
        child: Text(
          label,
          style: TextStyle(
            fontSize: 13,
            color: selected ? AppColors.white : AppColors.gray300,
            fontWeight: selected ? FontWeight.w600 : FontWeight.w400,
          ),
        ),
      ),
    );
  }

  Widget _buildStatusBadge(String status) {
    final statusConfig = {
      'repertoire': {'color': AppColors.success, 'icon': '✅', 'label': 'Repertoire'},
      'learning': {'color': AppColors.warning, 'icon': '🔄', 'label': 'În lucru'},
      'new': {'color': AppColors.coral, 'icon': '🆕', 'label': 'Nou'},
    };
    final config = statusConfig[status] ?? statusConfig['repertoire']!;
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: (config['color'] as Color).withOpacity(0.15),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Text(
        '${config['icon']} ${config['label']}',
        style: TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.w600,
          color: config['color'] as Color,
        ),
      ),
    );
  }

  Widget _metaItem(String icon, String label, String value) {
    return Column(
      children: [
        Text(icon, style: const TextStyle(fontSize: 18)),
        const SizedBox(height: 4),
        Text(label, style: const TextStyle(fontSize: 11, color: AppColors.gray300)),
        const SizedBox(height: 2),
        Text(
          value,
          style: const TextStyle(
              fontSize: 14, fontWeight: FontWeight.w600, color: AppColors.white),
        ),
      ],
    );
  }

  Widget _teamAssignment(String role, String name) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 8),
      decoration:
          BoxDecoration(border: Border(bottom: BorderSide(color: AppColors.surfaceElevated))),
      child: Row(
        children: [
          Text(role, style: const TextStyle(fontSize: 14, color: AppColors.gray300)),
          const Spacer(),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
            decoration: BoxDecoration(
              color: AppColors.surfaceElevated,
              borderRadius: BorderRadius.circular(20),
            ),
            child: Text(name,
                style: const TextStyle(
                    fontSize: 13, fontWeight: FontWeight.w500, color: AppColors.white)),
          ),
        ],
      ),
    );
  }

  Widget _iconActionButton(IconData icon, Color color, VoidCallback onPressed) {
    return GestureDetector(
      onTap: onPressed,
      child: Container(
        width: 52,
        height: 52,
        decoration: BoxDecoration(
          color: color.withOpacity(0.1),
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: color.withOpacity(0.3)),
        ),
        child: Icon(icon, color: color, size: 22),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final songProvider = context.watch<SongProvider>();
    final filteredSongs = songProvider.filteredBy(_searchQuery, _activeFilter);

    return Scaffold(
      backgroundColor: AppColors.black,
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.white),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('Repertoriu ISY'),
        actions: [
          IconButton(
            icon: const Icon(Icons.add, color: AppColors.coral),
            onPressed: _showAddSongDialog,
          ),
        ],
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: TextField(
              onChanged: (value) => setState(() => _searchQuery = value),
              style: const TextStyle(color: AppColors.white, fontSize: 15),
              decoration: InputDecoration(
                hintText: '🔍 Caută cântare...',
                filled: true,
                fillColor: AppColors.surface,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                  borderSide: BorderSide.none,
                ),
                contentPadding:
                    const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                hintStyle: const TextStyle(color: AppColors.gray500),
              ),
            ),
          ),
          SizedBox(
            height: 44,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              padding: const EdgeInsets.symmetric(horizontal: 16),
              itemCount: filters.length,
              itemBuilder: (context, index) {
                final filter = filters[index];
                final isActive = filter == _activeFilter;
                return Padding(
                  padding: const EdgeInsets.only(right: 8),
                  child: ChoiceChip(
                    label: Text(filter),
                    selected: isActive,
                    onSelected: (_) => setState(() => _activeFilter = filter),
                    backgroundColor: AppColors.surface,
                    selectedColor: AppColors.coral,
                    labelStyle: TextStyle(
                      color: isActive ? AppColors.white : AppColors.gray300,
                      fontSize: 13,
                      fontWeight: FontWeight.w500,
                    ),
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20)),
                    padding: const EdgeInsets.symmetric(horizontal: 4),
                  ),
                );
              },
            ),
          ),
          const SizedBox(height: 8),
          Expanded(
            child: filteredSongs.isEmpty
                ? Center(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Text('🎵',
                            style: TextStyle(fontSize: 48)),
                        const SizedBox(height: 12),
                        Text(
                          _searchQuery.isNotEmpty
                              ? 'Nicio cântare găsită pentru "$_searchQuery"'
                              : 'Nicio cântare în această categorie',
                          style: const TextStyle(
                              color: AppColors.gray300, fontSize: 15),
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  )
                : ListView.builder(
                    itemCount: filteredSongs.length,
                    itemBuilder: (context, index) {
                      return _buildSongItem(index + 1, filteredSongs[index]);
                    },
                  ),
          ),
        ],
      ),
    );
  }

  Widget _buildSongItem(int number, Song song) {
    final statusConfig = {
      'repertoire': {'color': AppColors.success, 'icon': '✅'},
      'learning': {'color': AppColors.warning, 'icon': '🔄'},
      'new': {'color': AppColors.coral, 'icon': '🆕'},
    };
    final config = statusConfig[song.status] ?? statusConfig['repertoire']!;

    return Dismissible(
      key: Key(song.id),
      direction: DismissDirection.endToStart,
      confirmDismiss: (_) async {
        bool confirmed = false;
        await showDialog(
          context: context,
          builder: (ctx) => AlertDialog(
            backgroundColor: AppColors.surface,
            title: const Text('Șterge cântare',
                style: TextStyle(color: AppColors.white)),
            content: Text(
              'Ești sigur că vrei să ștergi "${song.title}"?',
              style: const TextStyle(color: AppColors.gray300),
            ),
            actions: [
              TextButton(
                onPressed: () {
                  confirmed = false;
                  Navigator.pop(ctx);
                },
                child: const Text('Anulează',
                    style: TextStyle(color: AppColors.gray300)),
              ),
              TextButton(
                onPressed: () {
                  confirmed = true;
                  Navigator.pop(ctx);
                },
                child:
                    const Text('Șterge', style: TextStyle(color: AppColors.coral)),
              ),
            ],
          ),
        );
        return confirmed;
      },
      onDismissed: (_) => context.read<SongProvider>().deleteSong(song.id),
      background: Container(
        alignment: Alignment.centerRight,
        padding: const EdgeInsets.only(right: 20),
        color: AppColors.coral.withOpacity(0.2),
        child: const Icon(Icons.delete, color: AppColors.coral),
      ),
      child: GestureDetector(
        onTap: () => _showSongDetail(song),
        child: Container(
          padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 14),
          decoration: BoxDecoration(
            border: Border(
                bottom: BorderSide(color: AppColors.surfaceElevated, width: 1)),
          ),
          child: Row(
            children: [
              Container(
                width: 32,
                height: 32,
                decoration: BoxDecoration(
                  color: song.status == 'repertoire'
                      ? AppColors.coral
                      : (config['color'] as Color).withOpacity(0.2),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Center(
                  child: song.status == 'repertoire'
                      ? Text(
                          '$number',
                          style: const TextStyle(
                            color: AppColors.white,
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
                          ),
                        )
                      : Text(
                          config['icon'] as String,
                          style: const TextStyle(fontSize: 16),
                        ),
                ),
              ),
              const SizedBox(width: 14),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      song.title,
                      style: const TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.w500,
                        color: AppColors.white,
                      ),
                    ),
                    const SizedBox(height: 2),
                    Text(
                      '${song.artist} • Lead: ${song.lead}',
                      style:
                          const TextStyle(fontSize: 12, color: AppColors.gray300),
                    ),
                  ],
                ),
              ),
              GestureDetector(
                onTap: () => context.read<SongProvider>().toggleFavorite(song.id),
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 6),
                  child: Icon(
                    song.isFavorite ? Icons.favorite : Icons.favorite_border,
                    color: song.isFavorite ? AppColors.coral : AppColors.gray500,
                    size: 20,
                  ),
                ),
              ),
              _tag(song.key, isKey: true),
              const SizedBox(width: 6),
              _tag('${(song.durationSeconds / 60).ceil()}m'),
            ],
          ),
        ),
      ),
    );
  }

  Widget _tag(String text, {bool isKey = false}) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
      decoration: BoxDecoration(
        color: isKey ? AppColors.coral.withOpacity(0.15) : AppColors.surfaceElevated,
        borderRadius: BorderRadius.circular(4),
      ),
      child: Text(
        text,
        style: TextStyle(
          fontSize: 11,
          color: isKey ? AppColors.coral : AppColors.gray500,
        ),
      ),
    );
  }
}

