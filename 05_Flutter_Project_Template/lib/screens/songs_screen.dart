import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class SongsScreen extends StatefulWidget {
  const SongsScreen({super.key});

  @override
  State<SongsScreen> createState() => _SongsScreenState();
}

class _SongsScreenState extends State<SongsScreen> {
  String _searchQuery = '';
  String _activeFilter = 'Toate';
  String? _selectedSongId;
  bool _showTranspose = false;
  int _capo = 0;
  String _currentKey = 'Do';

  final List<String> filters = ['Toate', 'Repertoire', 'În lucru', 'Nou', 'Favorite'];

  final List<String> keys = ['Do', 'Do#', 'Re', 'Re#', 'Mi', 'Fa', 'Fa#', 'Sol', 'Sol#', 'La', 'La#', 'Si'];

  final List<Map<String, dynamic>> songs = [
    {
      'id': '1',
      'title': 'Oceans (Where Feet May Fail)', 
      'artist': 'Hillsong United', 
      'key': 'Do', 
      'duration': 272,
      'status': 'repertoire',
      'lead': 'Andrei',
      'guitar': 'Maria',
      'drums': 'Alex',
      'tempo': 65,
      'timeSignature': '4/4',
      'usageCount': 12,
      'lastUsed': '18 Mai 2025',
    },
    {
      'id': '2',
      'title': 'Way Maker', 
      'artist': 'Sinach', 
      'key': 'Sol', 
      'duration': 345,
      'status': 'repertoire',
      'lead': 'Maria',
      'guitar': 'Andrei',
      'drums': 'Alex',
      'tempo': 72,
      'timeSignature': '4/4',
      'usageCount': 8,
      'lastUsed': '11 Mai 2025',
    },
    {
      'id': '3',
      'title': 'Graves Into Gardens', 
      'artist': 'Elevation Worship', 
      'key': 'La m', 
      'duration': 258,
      'status': 'repertoire',
      'lead': 'Andrei',
      'guitar': 'Cristina',
      'drums': 'Alex',
      'tempo': 68,
      'timeSignature': '4/4',
      'usageCount': 5,
      'lastUsed': '4 Mai 2025',
    },
    {
      'id': '4',
      'title': 'What A Beautiful Name', 
      'artist': 'Hillsong Worship', 
      'key': 'Re', 
      'duration': 320,
      'status': 'repertoire',
      'lead': 'Elena',
      'guitar': 'Andrei',
      'drums': 'Alex',
      'tempo': 68,
      'timeSignature': '4/4',
      'usageCount': 15,
      'lastUsed': '25 Mai 2025',
    },
    {
      'id': '5',
      'title': 'Goodness of God', 
      'artist': 'Bethel Music', 
      'key': 'Do', 
      'duration': 292,
      'status': 'repertoire',
      'lead': 'Andrei',
      'guitar': 'Maria',
      'drums': 'Alex',
      'tempo': 70,
      'timeSignature': '4/4',
      'usageCount': 10,
      'lastUsed': '18 Mai 2025',
    },
    {
      'id': '6',
      'title': 'Build My Life', 
      'artist': 'Pat Barrett', 
      'key': 'Mi', 
      'duration': 255,
      'status': 'learning',
      'lead': 'Andrei',
      'guitar': 'Maria',
      'drums': 'Alex',
      'tempo': 74,
      'timeSignature': '4/4',
      'usageCount': 2,
      'lastUsed': '20 Mai 2025',
    },
    {
      'id': '7',
      'title': 'Firm Foundation', 
      'artist': 'Cody Carnes', 
      'key': 'Fa', 
      'duration': 228,
      'status': 'new',
      'lead': 'Maria',
      'guitar': 'Andrei',
      'drums': 'Alex',
      'tempo': 72,
      'timeSignature': '4/4',
      'usageCount': 0,
      'lastUsed': '-',
    },
  ];

  List<Map<String, dynamic>> get filteredSongs {
    return songs.where((song) {
      final matchesSearch = song['title'].toLowerCase().contains(_searchQuery.toLowerCase()) ||
          song['artist'].toLowerCase().contains(_searchQuery.toLowerCase());
      final matchesFilter = _activeFilter == 'Toate' ||
          (_activeFilter == 'Repertoire' && song['status'] == 'repertoire') ||
          (_activeFilter == 'În lucru' && song['status'] == 'learning') ||
          (_activeFilter == 'Nou' && song['status'] == 'new');
      return matchesSearch && matchesFilter;
    }).toList();
  }

  void _showSongDetail(Map<String, dynamic> song) {
    setState(() {
      _selectedSongId = song['id'];
      _currentKey = song['key'].toString().replaceAll(' m', '');
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
                                  song['title'],
                                  style: const TextStyle(
                                    fontSize: 22,
                                    fontWeight: FontWeight.w700,
                                    color: AppColors.white,
                                  ),
                                ),
                                const SizedBox(height: 4),
                                Text(
                                  song['artist'],
                                  style: const TextStyle(
                                    fontSize: 16,
                                    color: AppColors.gray300,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          _buildStatusBadge(song['status'] as String),
                        ],
                      ),
                      const SizedBox(height: 20),

                      // Song metadata
                      Container(
                        padding: const EdgeInsets.all(16),
                        decoration: BoxDecoration(
                          color: AppColors.surfaceElevated,
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceAround,
                          children: [
                            _metaItem('🎵', 'Cheie', _currentKey + (_capo > 0 ? ' (capo $_capo)' : '')),
                            _metaItem('⏱', 'Durată', '${(song['duration'] / 60).ceil()}:${(song['duration'] % 60).toString().padLeft(2, '0')}'),
                            _metaItem('🥁', 'Tempo', '${song['tempo']} BPM'),
                            _metaItem('📊', 'Folosit', '${song['usageCount']}x'),
                          ],
                        ),
                      ),
                      const SizedBox(height: 20),

                      // Transpose Section
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
                                  onTap: () => setModalState(() => _showTranspose = !_showTranspose),
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
                                style: TextStyle(
                                  fontSize: 14,
                                  color: AppColors.gray300,
                                ),
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
                                          color: _capo == index ? AppColors.coral : AppColors.surface,
                                          borderRadius: BorderRadius.circular(10),
                                          border: Border.all(
                                            color: _capo == index ? AppColors.coral : AppColors.gray500,
                                          ),
                                        ),
                                        child: Center(
                                          child: Text(
                                            '$index',
                                            style: TextStyle(
                                              fontSize: 16,
                                              fontWeight: FontWeight.w600,
                                              color: _capo == index ? AppColors.white : AppColors.gray300,
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
                                style: TextStyle(
                                  fontSize: 14,
                                  color: AppColors.gray300,
                                ),
                              ),
                              const SizedBox(height: 8),
                              Wrap(
                                spacing: 8,
                                runSpacing: 8,
                                children: keys.map((key) {
                                  return GestureDetector(
                                    onTap: () => setModalState(() => _currentKey = key),
                                    child: Container(
                                      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
                                      decoration: BoxDecoration(
                                        color: _currentKey == key ? AppColors.coral : AppColors.surface,
                                        borderRadius: BorderRadius.circular(8),
                                        border: Border.all(
                                          color: _currentKey == key ? AppColors.coral : AppColors.gray500,
                                        ),
                                      ),
                                      child: Text(
                                        key,
                                        style: TextStyle(
                                          fontSize: 14,
                                          fontWeight: FontWeight.w600,
                                          color: _currentKey == key ? AppColors.white : AppColors.gray300,
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

                      // Team Assignments
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
                      _teamAssignment('🎤 Lead Vocal', song['lead']),
                      _teamAssignment('🎸 Chitară', song['guitar']),
                      _teamAssignment('🥁 Tobe', song['drums']),
                      const SizedBox(height: 20),

                      // Lyrics Preview (Muzician View teaser)
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
                            Text(
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
                                  const TextSpan(text: '
'),
                                  const TextSpan(text: 'You call me out upon the waters
'),
                                  TextSpan(
                                    text: 'Sol                    La m',
                                    style: TextStyle(
                                      color: AppColors.coral.withOpacity(0.8),
                                      fontWeight: FontWeight.w600,
                                      fontSize: 14,
                                    ),
                                  ),
                                  const TextSpan(text: '
The great unknown where feet may fail
'),
                                  TextSpan(
                                    text: 'Do                     Fa',
                                    style: TextStyle(
                                      color: AppColors.coral.withOpacity(0.8),
                                      fontWeight: FontWeight.w600,
                                      fontSize: 14,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                            const SizedBox(height: 12),
                            Center(
                              child: TextButton(
                                onPressed: () {},
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

                      // Action buttons
                      Row(
                        children: [
                          Expanded(
                            child: ElevatedButton(
                              onPressed: () {},
                              style: ElevatedButton.styleFrom(
                                backgroundColor: AppColors.coral,
                                foregroundColor: AppColors.white,
                                padding: const EdgeInsets.all(16),
                                shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                              ),
                              child: const Text('Adaugă în program', style: TextStyle(fontWeight: FontWeight.w600)),
                            ),
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
        Text(
          label,
          style: const TextStyle(fontSize: 11, color: AppColors.gray300),
        ),
        const SizedBox(height: 2),
        Text(
          value,
          style: const TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.w600,
            color: AppColors.white,
          ),
        ),
      ],
    );
  }

  Widget _teamAssignment(String role, String name) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 8),
      decoration: BoxDecoration(
        border: Border(bottom: BorderSide(color: AppColors.surfaceElevated)),
      ),
      child: Row(
        children: [
          Text(
            role,
            style: const TextStyle(fontSize: 14, color: AppColors.gray300),
          ),
          const Spacer(),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
            decoration: BoxDecoration(
              color: AppColors.surfaceElevated,
              borderRadius: BorderRadius.circular(20),
            ),
            child: Text(
              name,
              style: const TextStyle(
                fontSize: 13,
                fontWeight: FontWeight.w500,
                color: AppColors.white,
              ),
            ),
          ),
        ],
      ),
    );
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
        title: const Text('Repertoriu ISY'),
        actions: [
          IconButton(
            icon: const Icon(Icons.add, color: AppColors.coral),
            onPressed: () {},
          ),
        ],
      ),
      body: Column(
        children: [
          // Search Bar
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
                contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                hintStyle: const TextStyle(color: AppColors.gray500),
              ),
            ),
          ),
          // Filter Tabs
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
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                    padding: const EdgeInsets.symmetric(horizontal: 4),
                  ),
                );
              },
            ),
          ),
          const SizedBox(height: 8),
          // Song List
          Expanded(
            child: ListView.builder(
              itemCount: filteredSongs.length,
              itemBuilder: (context, index) {
                final song = filteredSongs[index];
                return _buildSongItem(index + 1, song);
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSongItem(int number, Map<String, dynamic> song) {
    final statusConfig = {
      'repertoire': {'color': AppColors.success, 'icon': '✅'},
      'learning': {'color': AppColors.warning, 'icon': '🔄'},
      'new': {'color': AppColors.coral, 'icon': '🆕'},
    };

    final config = statusConfig[song['status']] ?? statusConfig['repertoire']!;

    return GestureDetector(
      onTap: () => _showSongDetail(song),
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 14),
        decoration: BoxDecoration(
          border: Border(bottom: BorderSide(color: AppColors.surfaceElevated, width: 1)),
        ),
        child: Row(
          children: [
            Container(
              width: 32,
              height: 32,
              decoration: BoxDecoration(
                color: song['status'] == 'repertoire' 
                    ? AppColors.coral 
                    : (config['color'] as Color).withOpacity(0.2),
                borderRadius: BorderRadius.circular(8),
              ),
              child: Center(
                child: song['status'] == 'repertoire'
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
                    song['title'],
                    style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w500,
                      color: AppColors.white,
                    ),
                  ),
                  const SizedBox(height: 2),
                  Text(
                    '${song['artist']} • Lead: ${song['lead']}',
                    style: const TextStyle(fontSize: 12, color: AppColors.gray300),
                  ),
                ],
              ),
            ),
            Row(
              children: [
                _tag(song['key'], isKey: true),
                const SizedBox(width: 6),
                _tag('${(song['duration'] / 60).ceil()}m'),
              ],
            ),
          ],
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
