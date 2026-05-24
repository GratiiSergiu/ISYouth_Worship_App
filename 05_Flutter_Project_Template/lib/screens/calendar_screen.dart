import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class CalendarScreen extends StatefulWidget {
  const CalendarScreen({super.key});

  @override
  State<CalendarScreen> createState() => _CalendarScreenState();
}

class _CalendarScreenState extends State<CalendarScreen> {
  DateTime _currentMonth = DateTime(2025, 5);
  DateTime? _selectedDate;

  // Programs data with dates
  final List<Map<String, dynamic>> _programs = [
    {
      'date': DateTime(2025, 5, 4),
      'title': 'Harul',
      'theme': 'Tineret ISYouth',
      'songs': 5,
      'duration': 45,
      'status': 'completed',
      'tags': ['#tineret', '#adorare'],
      'setlist': ['Goodness of God', 'Way Maker', 'Oceans', 'Graves Into Gardens', 'What A Beautiful Name'],
    },
    {
      'date': DateTime(2025, 5, 11),
      'title': 'Speranță',
      'theme': 'Tineret ISYouth',
      'songs': 6,
      'duration': 50,
      'status': 'completed',
      'tags': ['#tineret', '#mărturie'],
      'setlist': ['Oceans', 'Way Maker', 'Build My Life', 'Goodness of God', 'Firm Foundation', 'Graves Into Gardens'],
    },
    {
      'date': DateTime(2025, 5, 18),
      'title': 'Credință',
      'theme': 'Tineret ISYouth',
      'songs': 6,
      'duration': 52,
      'status': 'completed',
      'tags': ['#tineret', '#mărturie'],
      'setlist': ['Oceans', 'Way Maker', 'Graves Into Gardens', 'What A Beautiful Name', 'Goodness of God', 'Firm Foundation'],
    },
    {
      'date': DateTime(2025, 5, 25),
      'title': 'Identitate',
      'theme': 'Cine suntem în Hristos',
      'songs': 5,
      'duration': 48,
      'status': 'ready',
      'tags': ['#tineret', '#adorare'],
      'setlist': ['Oceans', 'Way Maker', 'Graves Into Gardens', 'What A Beautiful Name', 'Goodness of God'],
    },
    {
      'date': DateTime(2025, 6, 1),
      'title': 'Har',
      'theme': 'Serviciu Special',
      'songs': 3,
      'duration': 35,
      'status': 'draft',
      'tags': ['#botez', '#adorare'],
      'setlist': ['Oceans', 'Way Maker', 'Goodness of God'],
    },
    {
      'date': DateTime(2025, 6, 8),
      'title': 'Dragoste',
      'theme': 'Tineret ISYouth',
      'songs': 5,
      'duration': 47,
      'status': 'draft',
      'tags': ['#tineret', '#adorare'],
      'setlist': ['Way Maker', 'Graves Into Gardens', 'What A Beautiful Name', 'Build My Life', 'Goodness of God'],
    },
  ];

  List<Map<String, dynamic>> get _eventsForMonth {
    return _programs.where((p) {
      final date = p['date'] as DateTime;
      return date.year == _currentMonth.year && date.month == _currentMonth.month;
    }).toList();
  }

  bool _hasEvent(DateTime date) {
    return _programs.any((p) {
      final eventDate = p['date'] as DateTime;
      return eventDate.year == date.year && 
             eventDate.month == date.month && 
             eventDate.day == date.day;
    });
  }

  Map<String, dynamic>? _getEvent(DateTime date) {
    try {
      return _programs.firstWhere((p) {
        final eventDate = p['date'] as DateTime;
        return eventDate.year == date.year && 
               eventDate.month == date.month && 
               eventDate.day == date.day;
      });
    } catch (e) {
      return null;
    }
  }

  void _previousMonth() {
    setState(() {
      _currentMonth = DateTime(_currentMonth.year, _currentMonth.month - 1);
    });
  }

  void _nextMonth() {
    setState(() {
      _currentMonth = DateTime(_currentMonth.year, _currentMonth.month + 1);
    });
  }

  void _showProgramDetail(Map<String, dynamic> program) {
    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.surface,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (context) => DraggableScrollableSheet(
        initialChildSize: 0.7,
        minChildSize: 0.5,
        maxChildSize: 0.9,
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
                              '"${program['title']}"',
                              style: const TextStyle(
                                fontSize: 24,
                                fontWeight: FontWeight.w700,
                                color: AppColors.white,
                              ),
                            ),
                            const SizedBox(height: 4),
                            Text(
                              program['theme'],
                              style: const TextStyle(fontSize: 16, color: AppColors.gray300),
                            ),
                          ],
                        ),
                      ),
                      _buildStatusBadge(program['status'] as String),
                    ],
                  ),
                  const SizedBox(height: 16),
                  Container(
                    padding: const EdgeInsets.all(16),
                    decoration: BoxDecoration(
                      color: AppColors.surfaceElevated,
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceAround,
                      children: [
                        _detailItem('📅', 'Data', '${program['date'].day}/${program['date'].month}/2025'),
                        _detailItem('🎵', 'Cântări', '${program['songs']}'),
                        _detailItem('⏱', 'Durată', '${program['duration']} min'),
                      ],
                    ),
                  ),
                  const SizedBox(height: 20),
                  if (program['tags'] != null) ...[
                    Wrap(
                      spacing: 8,
                      runSpacing: 8,
                      children: (program['tags'] as List).map((tag) {
                        return Container(
                          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                          decoration: BoxDecoration(
                            color: AppColors.coral.withOpacity(0.15),
                            borderRadius: BorderRadius.circular(20),
                          ),
                          child: Text(
                            tag,
                            style: const TextStyle(
                              fontSize: 13,
                              color: AppColors.coral,
                              fontWeight: FontWeight.w500,
                            ),
                          ),
                        );
                      }).toList(),
                    ),
                    const SizedBox(height: 20),
                  ],
                  const Text(
                    'SETLIST',
                    style: TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w600,
                      color: AppColors.gray300,
                      letterSpacing: 1,
                    ),
                  ),
                  const SizedBox(height: 12),
                  ...(program['setlist'] as List).asMap().entries.map((entry) {
                    final index = entry.key;
                    final song = entry.value as String;
                    return _buildSetlistItem(index + 1, song);
                  }).toList(),
                  const SizedBox(height: 20),
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
                          child: const Text('Editează program', style: TextStyle(fontWeight: FontWeight.w600)),
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
      ),
    );
  }

  Widget _buildStatusBadge(String status) {
    final config = {
      'completed': {'color': AppColors.success, 'label': 'Completat'},
      'ready': {'color': AppColors.coral, 'label': 'Ready'},
      'draft': {'color': AppColors.warning, 'label': 'Draft'},
    };
    final c = config[status] ?? config['draft']!;
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: (c['color'] as Color).withOpacity(0.15),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Text(
        c['label'] as String,
        style: TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.w600,
          color: c['color'] as Color,
        ),
      ),
    );
  }

  Widget _detailItem(String icon, String label, String value) {
    return Column(
      children: [
        Text(icon, style: const TextStyle(fontSize: 20)),
        const SizedBox(height: 4),
        Text(label, style: const TextStyle(fontSize: 11, color: AppColors.gray300)),
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

  Widget _buildSetlistItem(int position, String song) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 10),
      decoration: BoxDecoration(
        border: Border(bottom: BorderSide(color: AppColors.surfaceElevated)),
      ),
      child: Row(
        children: [
          Container(
            width: 28,
            height: 28,
            decoration: BoxDecoration(
              color: AppColors.coral,
              borderRadius: BorderRadius.circular(8),
            ),
            child: Center(
              child: Text(
                '$position',
                style: const TextStyle(
                  color: AppColors.white,
                  fontSize: 13,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
          ),
          const SizedBox(width: 12),
          Text(
            song,
            style: const TextStyle(
              fontSize: 15,
              fontWeight: FontWeight.w500,
              color: AppColors.white,
            ),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final daysInMonth = DateTime(_currentMonth.year, _currentMonth.month + 1, 0).day;
    final firstWeekday = DateTime(_currentMonth.year, _currentMonth.month, 1).weekday % 7;

    final monthNames = [
      '', 'Ianuarie', 'Februarie', 'Martie', 'Aprilie', 'Mai', 'Iunie',
      'Iulie', 'August', 'Septembrie', 'Octombrie', 'Noiembrie', 'Decembrie'
    ];

    return Scaffold(
      backgroundColor: AppColors.black,
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.white),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('Calendar'),
        actions: [
          IconButton(
            icon: const Icon(Icons.today, color: AppColors.coral),
            onPressed: () {
              setState(() {
                _currentMonth = DateTime.now();
              });
            },
          ),
        ],
      ),
      body: Column(
        children: [
          // Month selector
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                IconButton(
                  icon: const Icon(Icons.chevron_left, color: AppColors.white),
                  onPressed: _previousMonth,
                ),
                Text(
                  '${monthNames[_currentMonth.month]} ${_currentMonth.year}',
                  style: const TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w700,
                    color: AppColors.white,
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.chevron_right, color: AppColors.white),
                  onPressed: _nextMonth,
                ),
              ],
            ),
          ),

          // Weekday headers
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: ['D', 'L', 'M', 'M', 'J', 'V', 'S'].map((day) {
                return SizedBox(
                  width: 40,
                  child: Text(
                    day,
                    textAlign: TextAlign.center,
                    style: const TextStyle(
                      fontSize: 13,
                      fontWeight: FontWeight.w600,
                      color: AppColors.gray300,
                    ),
                  ),
                );
              }).toList(),
            ),
          ),
          const SizedBox(height: 8),

          // Calendar grid
          Expanded(
            child: GridView.builder(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 7,
                childAspectRatio: 0.85,
              ),
              itemCount: firstWeekday + daysInMonth,
              itemBuilder: (context, index) {
                if (index < firstWeekday) {
                  return const SizedBox.shrink();
                }
                final day = index - firstWeekday + 1;
                final date = DateTime(_currentMonth.year, _currentMonth.month, day);
                final hasEvent = _hasEvent(date);
                final event = hasEvent ? _getEvent(date) : null;
                final isSelected = _selectedDate != null &&
                    _selectedDate!.year == date.year &&
                    _selectedDate!.month == date.month &&
                    _selectedDate!.day == date.day;
                final isToday = DateTime.now().year == date.year &&
                    DateTime.now().month == date.month &&
                    DateTime.now().day == date.day;

                return GestureDetector(
                  onTap: () {
                    if (hasEvent && event != null) {
                      setState(() => _selectedDate = date);
                      _showProgramDetail(event);
                    }
                  },
                  child: Container(
                    margin: const EdgeInsets.all(2),
                    decoration: BoxDecoration(
                      color: isSelected 
                          ? AppColors.coral.withOpacity(0.2)
                          : isToday
                              ? AppColors.coral.withOpacity(0.1)
                              : Colors.transparent,
                      borderRadius: BorderRadius.circular(12),
                      border: Border.all(
                        color: isSelected 
                            ? AppColors.coral 
                            : isToday
                                ? AppColors.coral.withOpacity(0.5)
                                : Colors.transparent,
                        width: isSelected || isToday ? 2 : 0,
                      ),
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          '$day',
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: isToday ? FontWeight.w700 : FontWeight.w500,
                            color: isToday ? AppColors.coral : AppColors.white,
                          ),
                        ),
                        if (hasEvent) ...[
                          const SizedBox(height: 4),
                          Container(
                            width: 6,
                            height: 6,
                            decoration: BoxDecoration(
                              color: event != null && event['status'] == 'completed'
                                  ? AppColors.success
                                  : event != null && event['status'] == 'ready'
                                      ? AppColors.coral
                                      : AppColors.warning,
                              shape: BoxShape.circle,
                            ),
                          ),
                          const SizedBox(height: 2),
                          Text(
                            event?['title'] ?? '',
                            style: const TextStyle(
                              fontSize: 9,
                              color: AppColors.gray300,
                            ),
                            maxLines: 1,
                            overflow: TextOverflow.ellipsis,
                            textAlign: TextAlign.center,
                          ),
                        ],
                      ],
                    ),
                  ),
                );
              },
            ),
          ),

          // Events list for current month
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              color: AppColors.surface,
              borderRadius: const BorderRadius.vertical(top: Radius.circular(24)),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Programe în ${monthNames[_currentMonth.month]}',
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.w600,
                    color: AppColors.white,
                  ),
                ),
                const SizedBox(height: 12),
                if (_eventsForMonth.isEmpty)
                  const Center(
                    child: Padding(
                      padding: EdgeInsets.all(20),
                      child: Text(
                        'Niciun program programat',
                        style: TextStyle(color: AppColors.gray300),
                      ),
                    ),
                  )
                else
                  ..._eventsForMonth.map((event) {
                    return _buildEventListItem(event);
                  }).toList(),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildEventListItem(Map<String, dynamic> event) {
    final statusConfig = {
      'completed': {'color': AppColors.success, 'icon': '✅'},
      'ready': {'color': AppColors.coral, 'icon': '🔥'},
      'draft': {'color': AppColors.warning, 'icon': '📝'},
    };
    final config = statusConfig[event['status']] ?? statusConfig['draft']!;

    return GestureDetector(
      onTap: () => _showProgramDetail(event),
      child: Container(
        margin: const EdgeInsets.only(bottom: 8),
        padding: const EdgeInsets.all(14),
        decoration: BoxDecoration(
          color: AppColors.surfaceElevated,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Row(
          children: [
            Container(
              width: 48,
              height: 48,
              decoration: BoxDecoration(
                color: (config['color'] as Color).withOpacity(0.2),
                borderRadius: BorderRadius.circular(12),
              ),
              child: Center(
                child: Text(
                  '${event['date'].day}',
                  style: TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.w700,
                    color: config['color'] as Color,
                  ),
                ),
              ),
            ),
            const SizedBox(width: 14),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    '"${event['title']}"',
                    style: const TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.w600,
                      color: AppColors.white,
                    ),
                  ),
                  const SizedBox(height: 2),
                  Text(
                    '${event['theme']} • ${event['songs']} cântări • ${event['duration']} min',
                    style: const TextStyle(fontSize: 12, color: AppColors.gray300),
                  ),
                ],
              ),
            ),
            Text(
              config['icon'] as String,
              style: const TextStyle(fontSize: 20),
            ),
          ],
        ),
      ),
    );
  }
}
