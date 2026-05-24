import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class StatisticsScreen extends StatefulWidget {
  const StatisticsScreen({super.key});

  @override
  State<StatisticsScreen> createState() => _StatisticsScreenState();
}

class _StatisticsScreenState extends State<StatisticsScreen> {
  String _selectedPeriod = '6 luni';
  String _sortBy = 'top'; // top | rare

  final List<String> periods = ['3 luni', '6 luni', '1 an', 'Tot timpul'];

  // Song analytics data
  final List<Map<String, dynamic>> _songStats = [
    {
      'title': 'Oceans (Where Feet May Fail)',
      'artist': 'Hillsong United',
      'key': 'Do',
      'playCount': 18,
      'lastPlayed': '25 Mai 2025',
      'firstPlayed': '15 Ian 2024',
      'programs': [
        {'date': '25 Mai 2025', 'program': 'Identitate', 'position': 1},
        {'date': '18 Mai 2025', 'program': 'Credință', 'position': 1},
        {'date': '11 Mai 2025', 'program': 'Speranță', 'position': 1},
        {'date': '4 Mai 2025', 'program': 'Harul', 'position': 3},
        {'date': '27 Apr 2025', 'program': 'Înviere', 'position': 2},
        {'date': '20 Apr 2025', 'program': 'Paște', 'position': 1},
      ],
      'trend': 'up',
    },
    {
      'title': 'Way Maker',
      'artist': 'Sinach',
      'key': 'Sol',
      'playCount': 16,
      'lastPlayed': '25 Mai 2025',
      'firstPlayed': '20 Feb 2024',
      'programs': [
        {'date': '25 Mai 2025', 'program': 'Identitate', 'position': 2},
        {'date': '18 Mai 2025', 'program': 'Credință', 'position': 2},
        {'date': '11 Mai 2025', 'program': 'Speranță', 'position': 2},
        {'date': '4 Mai 2025', 'program': 'Harul', 'position': 2},
        {'date': '27 Apr 2025', 'program': 'Înviere', 'position': 3},
        {'date': '13 Apr 2025', 'program': 'Palm Sunday', 'position': 1},
      ],
      'trend': 'stable',
    },
    {
      'title': 'Goodness of God',
      'artist': 'Bethel Music',
      'key': 'Do',
      'playCount': 14,
      'lastPlayed': '25 Mai 2025',
      'firstPlayed': '10 Mar 2024',
      'programs': [
        {'date': '25 Mai 2025', 'program': 'Identitate', 'position': 5},
        {'date': '18 Mai 2025', 'program': 'Credință', 'position': 5},
        {'date': '11 Mai 2025', 'program': 'Speranță', 'position': 4},
        {'date': '4 Mai 2025', 'program': 'Harul', 'position': 1},
        {'date': '30 Mar 2025', 'program': 'Familie', 'position': 3},
      ],
      'trend': 'up',
    },
    {
      'title': 'Graves Into Gardens',
      'artist': 'Elevation Worship',
      'key': 'La m',
      'playCount': 12,
      'lastPlayed': '25 Mai 2025',
      'firstPlayed': '5 Apr 2024',
      'programs': [
        {'date': '25 Mai 2025', 'program': 'Identitate', 'position': 3},
        {'date': '18 Mai 2025', 'program': 'Credință', 'position': 3},
        {'date': '11 Mai 2025', 'program': 'Speranță', 'position': 6},
        {'date': '27 Apr 2025', 'program': 'Înviere', 'position': 1},
        {'date': '23 Mar 2025', 'program': 'Primăvară', 'position': 2},
      ],
      'trend': 'up',
    },
    {
      'title': 'What A Beautiful Name',
      'artist': 'Hillsong Worship',
      'key': 'Re',
      'playCount': 10,
      'lastPlayed': '25 Mai 2025',
      'firstPlayed': '1 Feb 2024',
      'programs': [
        {'date': '25 Mai 2025', 'program': 'Identitate', 'position': 4},
        {'date': '18 Mai 2025', 'program': 'Credință', 'position': 4},
        {'date': '20 Apr 2025', 'program': 'Paște', 'position': 2},
        {'date': '16 Mar 2025', 'program': 'Iubire', 'position': 1},
        {'date': '2 Feb 2025', 'program': 'An Nou', 'position': 3},
      ],
      'trend': 'stable',
    },
    {
      'title': 'Build My Life',
      'artist': 'Pat Barrett',
      'key': 'Mi',
      'playCount': 5,
      'lastPlayed': '11 Mai 2025',
      'firstPlayed': '15 Mar 2025',
      'programs': [
        {'date': '11 Mai 2025', 'program': 'Speranță', 'position': 3},
        {'date': '27 Apr 2025', 'program': 'Înviere', 'position': 4},
        {'date': '23 Mar 2025', 'program': 'Primăvară', 'position': 4},
        {'date': '9 Mar 2025', 'program': 'Post', 'position': 2},
        {'date': '23 Feb 2025', 'program': 'Tineret', 'position': 1},
      ],
      'trend': 'up',
    },
    {
      'title': 'Firm Foundation',
      'artist': 'Cody Carnes',
      'key': 'Fa',
      'playCount': 2,
      'lastPlayed': '18 Mai 2025',
      'firstPlayed': '18 Mai 2025',
      'programs': [
        {'date': '18 Mai 2025', 'program': 'Credință', 'position': 6},
        {'date': '11 Mai 2025', 'program': 'Speranță', 'position': 5},
      ],
      'trend': 'new',
    },
    {
      'title': 'King of Kings',
      'artist': 'Hillsong Worship',
      'key': 'Sol',
      'playCount': 1,
      'lastPlayed': '4 Mai 2025',
      'firstPlayed': '4 Mai 2025',
      'programs': [
        {'date': '4 Mai 2025', 'program': 'Harul', 'position': 4},
      ],
      'trend': 'new',
    },
  ];

  List<Map<String, dynamic>> get _sortedSongs {
    final sorted = List<Map<String, dynamic>>.from(_songStats);
    if (_sortBy == 'top') {
      sorted.sort((a, b) => (b['playCount'] as int).compareTo(a['playCount'] as int));
    } else {
      sorted.sort((a, b) => (a['playCount'] as int).compareTo(b['playCount'] as int));
    }
    return sorted;
  }

  void _showSongHistory(Map<String, dynamic> song) {
    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.surface,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (context) => DraggableScrollableSheet(
        initialChildSize: 0.8,
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
                    crossAxisAlignment: CrossAxisAlignment.start,
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
                              '${song['artist']} • ${song['key']} major',
                              style: const TextStyle(fontSize: 15, color: AppColors.gray300),
                            ),
                          ],
                        ),
                      ),
                      Container(
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: _getTrendColor(song['trend']).withOpacity(0.2),
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Column(
                          children: [
                            Text(
                              '${song['playCount']}',
                              style: TextStyle(
                                fontSize: 28,
                                fontWeight: FontWeight.w700,
                                color: _getTrendColor(song['trend']),
                              ),
                            ),
                            Text(
                              'ori',
                              style: TextStyle(
                                fontSize: 12,
                                color: _getTrendColor(song['trend']),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 20),

                  // Stats cards
                  Row(
                    children: [
                      _statCard('🎤', 'Prima dată', song['firstPlayed']),
                      const SizedBox(width: 10),
                      _statCard('📅', 'Ultima dată', song['lastPlayed']),
                    ],
                  ),
                  const SizedBox(height: 20),

                  // Frequency warning
                  if (song['playCount'] >= 15)
                    Container(
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: AppColors.warning.withOpacity(0.15),
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(color: AppColors.warning.withOpacity(0.3)),
                      ),
                      child: Row(
                        children: [
                          Icon(Icons.warning_amber, color: AppColors.warning, size: 20),
                          const SizedBox(width: 10),
                          Expanded(
                            child: Text(
                              'Cântare cântată des - ia în considerare să o incluzi mai rar pentru diversitate',
                              style: TextStyle(
                                fontSize: 13,
                                color: AppColors.warning,
                                fontWeight: FontWeight.w500,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  if (song['playCount'] <= 2)
                    Container(
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: AppColors.success.withOpacity(0.15),
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(color: AppColors.success.withOpacity(0.3)),
                      ),
                      child: Row(
                        children: [
                          Icon(Icons.auto_awesome, color: AppColors.success, size: 20),
                          const SizedBox(width: 10),
                          Expanded(
                            child: Text(
                              'Cântare nouă sau rar cântată - excelentă pentru diversitate',
                              style: TextStyle(
                                fontSize: 13,
                                color: AppColors.success,
                                fontWeight: FontWeight.w500,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  const SizedBox(height: 20),

                  // History table
                  const Text(
                    'ISTORIC PROGRAMĂRI',
                    style: TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w600,
                      color: AppColors.gray300,
                      letterSpacing: 1,
                    ),
                  ),
                  const SizedBox(height: 12),
                  ...(song['programs'] as List).map((program) {
                    return _historyItem(program);
                  }).toList(),
                  const SizedBox(height: 30),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Color _getTrendColor(String trend) {
    switch (trend) {
      case 'up': return AppColors.success;
      case 'stable': return AppColors.coral;
      case 'new': return AppColors.indigo;
      default: return AppColors.gray300;
    }
  }

  Widget _statCard(String icon, String label, String value) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.all(14),
        decoration: BoxDecoration(
          color: AppColors.surfaceElevated,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Column(
          children: [
            Text(icon, style: const TextStyle(fontSize: 20)),
            const SizedBox(height: 4),
            Text(label, style: const TextStyle(fontSize: 11, color: AppColors.gray300)),
            const SizedBox(height: 2),
            Text(
              value,
              style: const TextStyle(
                fontSize: 13,
                fontWeight: FontWeight.w600,
                color: AppColors.white,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _historyItem(Map<String, dynamic> program) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 12),
      decoration: BoxDecoration(
        border: Border(bottom: BorderSide(color: AppColors.surfaceElevated)),
      ),
      child: Row(
        children: [
          Container(
            width: 36,
            height: 36,
            decoration: BoxDecoration(
              color: AppColors.coral.withOpacity(0.2),
              borderRadius: BorderRadius.circular(10),
            ),
            child: Center(
              child: Text(
                '${program['position']}',
                style: const TextStyle(
                  color: AppColors.coral,
                  fontWeight: FontWeight.w700,
                  fontSize: 14,
                ),
              ),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  '"${program['program']}"',
                  style: const TextStyle(
                    fontSize: 15,
                    fontWeight: FontWeight.w500,
                    color: AppColors.white,
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  program['date'],
                  style: const TextStyle(fontSize: 12, color: AppColors.gray300),
                ),
              ],
            ),
          ),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
            decoration: BoxDecoration(
              color: AppColors.surfaceElevated,
              borderRadius: BorderRadius.circular(8),
            ),
            child: Text(
              'Poziția ${program['position']}',
              style: const TextStyle(fontSize: 11, color: AppColors.gray300),
            ),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final maxPlays = _songStats.map((s) => s['playCount'] as int).reduce((a, b) => a > b ? a : b);

    return Scaffold(
      backgroundColor: AppColors.black,
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.white),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('Statistici & Analiză'),
      ),
      body: Column(
        children: [
          // Period selector
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
            child: Row(
              children: [
                const Text(
                  'Perioada:',
                  style: TextStyle(fontSize: 14, color: AppColors.gray300),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: SingleChildScrollView(
                    scrollDirection: Axis.horizontal,
                    child: Row(
                      children: periods.map((period) {
                        final isSelected = period == _selectedPeriod;
                        return Padding(
                          padding: const EdgeInsets.only(right: 8),
                          child: ChoiceChip(
                            label: Text(period),
                            selected: isSelected,
                            onSelected: (_) => setState(() => _selectedPeriod = period),
                            backgroundColor: AppColors.surface,
                            selectedColor: AppColors.coral,
                            labelStyle: TextStyle(
                              color: isSelected ? AppColors.white : AppColors.gray300,
                              fontSize: 13,
                            ),
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                          ),
                        );
                      }).toList(),
                    ),
                  ),
                ),
              ],
            ),
          ),

          // Sort toggle
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Row(
              children: [
                Expanded(
                  child: GestureDetector(
                    onTap: () => setState(() => _sortBy = 'top'),
                    child: Container(
                      padding: const EdgeInsets.symmetric(vertical: 10),
                      decoration: BoxDecoration(
                        color: _sortBy == 'top' ? AppColors.coral : AppColors.surface,
                        borderRadius: const BorderRadius.horizontal(left: Radius.circular(10)),
                      ),
                      child: Center(
                        child: Text(
                          '🔥 Top cântate',
                          style: TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
                            color: _sortBy == 'top' ? AppColors.white : AppColors.gray300,
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
                Expanded(
                  child: GestureDetector(
                    onTap: () => setState(() => _sortBy = 'rare'),
                    child: Container(
                      padding: const EdgeInsets.symmetric(vertical: 10),
                      decoration: BoxDecoration(
                        color: _sortBy == 'rare' ? AppColors.coral : AppColors.surface,
                        borderRadius: const BorderRadius.horizontal(right: Radius.circular(10)),
                      ),
                      child: Center(
                        child: Text(
                          '❄️ Mai puțin cântate',
                          style: TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
                            color: _sortBy == 'rare' ? AppColors.white : AppColors.gray300,
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 8),

          // Summary stats
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Row(
              children: [
                _summaryCard('🎵', '${_songStats.length}', 'Cântări totale'),
                const SizedBox(width: 10),
                _summaryCard('🎤', '$_maxPlays', 'Max repetări'),
                const SizedBox(width: 10),
                _summaryCard('📊', '${_songStats.where((s) => s['playCount'] == 1).length}', 'Cântate 1x'),
              ],
            ),
          ),

          // Song list
          Expanded(
            child: ListView.builder(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              itemCount: _sortedSongs.length,
              itemBuilder: (context, index) {
                final song = _sortedSongs[index];
                return _buildSongStatItem(index + 1, song, maxPlays);
              },
            ),
          ),
        ],
      ),
    );
  }

  int get _maxPlays => _songStats.map((s) => s['playCount'] as int).reduce((a, b) => a > b ? a : b);

  Widget _summaryCard(String icon, String value, String label) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: AppColors.surface,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Column(
          children: [
            Text(icon, style: const TextStyle(fontSize: 20)),
            Text(
              value,
              style: const TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.w700,
                color: AppColors.white,
              ),
            ),
            Text(
              label,
              style: const TextStyle(fontSize: 11, color: AppColors.gray300),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSongStatItem(int rank, Map<String, dynamic> song, int maxPlays) {
    final playCount = song['playCount'] as int;
    final percentage = playCount / maxPlays;
    final isFrequent = playCount >= 15;
    final isRare = playCount <= 2;

    return GestureDetector(
      onTap: () => _showSongHistory(song),
      child: Container(
        margin: const EdgeInsets.only(bottom: 10),
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: AppColors.surface,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(
            color: isFrequent 
                ? AppColors.warning.withOpacity(0.3)
                : isRare
                    ? AppColors.success.withOpacity(0.3)
                    : Colors.transparent,
          ),
        ),
        child: Column(
          children: [
            Row(
              children: [
                Container(
                  width: 32,
                  height: 32,
                  decoration: BoxDecoration(
                    color: rank <= 3 ? AppColors.coral.withOpacity(0.2) : AppColors.surfaceElevated,
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Center(
                    child: Text(
                      '$rank',
                      style: TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w700,
                        color: rank <= 3 ? AppColors.coral : AppColors.gray300,
                      ),
                    ),
                  ),
                ),
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
                        '${song['artist']} • ${song['key']} major',
                        style: const TextStyle(fontSize: 12, color: AppColors.gray300),
                      ),
                    ],
                  ),
                ),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Text(
                      '$playCount',
                      style: TextStyle(
                        fontSize: 22,
                        fontWeight: FontWeight.w700,
                        color: isFrequent 
                            ? AppColors.warning 
                            : isRare 
                                ? AppColors.success 
                                : AppColors.coral,
                      ),
                    ),
                    Text(
                      'ori',
                      style: TextStyle(
                        fontSize: 11,
                        color: isFrequent 
                            ? AppColors.warning 
                            : isRare 
                                ? AppColors.success 
                                : AppColors.gray300,
                      ),
                    ),
                  ],
                ),
              ],
            ),
            const SizedBox(height: 10),
            // Progress bar
            ClipRRect(
              borderRadius: BorderRadius.circular(4),
              child: LinearProgressIndicator(
                value: percentage,
                backgroundColor: AppColors.surfaceElevated,
                valueColor: AlwaysStoppedAnimation<Color>(
                  isFrequent 
                      ? AppColors.warning 
                      : isRare 
                          ? AppColors.success 
                          : AppColors.coral,
                ),
                minHeight: 6,
              ),
            ),
            const SizedBox(height: 8),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'Ultima: ${song['lastPlayed']}',
                  style: const TextStyle(fontSize: 11, color: AppColors.gray300),
                ),
                if (isFrequent)
                  Text(
                    '⚠️ Frecventă',
                    style: TextStyle(
                      fontSize: 11,
                      color: AppColors.warning,
                      fontWeight: FontWeight.w500,
                    ),
                  )
                else if (isRare)
                  Text(
                    '✨ Rară',
                    style: TextStyle(
                      fontSize: 11,
                      color: AppColors.success,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
