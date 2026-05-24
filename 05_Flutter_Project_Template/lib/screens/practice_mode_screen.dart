import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class PracticeModeScreen extends StatefulWidget {
  const PracticeModeScreen({super.key});

  @override
  State<PracticeModeScreen> createState() => _PracticeModeScreenState();
}

class _PracticeModeScreenState extends State<PracticeModeScreen> {
  final List<Map<String, dynamic>> _rehearsals = [
    {
      'id': '1',
      'programName': 'Identitate',
      'date': '23 Mai 2025',
      'time': '19:00',
      'location': 'Sala Tineret',
      'duration': 90,
      'status': 'scheduled',
      'attendees': [
        {'name': 'Andrei', 'status': 'confirmed', 'avatar': 'A'},
        {'name': 'Maria', 'status': 'confirmed', 'avatar': 'M'},
        {'name': 'Alex', 'status': 'maybe', 'avatar': 'A'},
        {'name': 'Cristina', 'status': 'declined', 'avatar': 'C'},
        {'name': 'David', 'status': 'no-response', 'avatar': 'D'},
        {'name': 'Elena', 'status': 'confirmed', 'avatar': 'E'},
      ],
      'agenda': [
        {'song': 'Oceans', 'focus': 'transitions', 'duration': 20},
        {'song': 'Way Maker', 'focus': 'dynamics', 'duration': 15},
        {'song': 'Graves Into Gardens', 'focus': 'harmonies', 'duration': 15},
      ],
      'hasRecording': false,
    },
    {
      'id': '2',
      'programName': 'Credință',
      'date': '16 Mai 2025',
      'time': '19:00',
      'location': 'Sala Tineret',
      'duration': 90,
      'status': 'completed',
      'attendees': [
        {'name': 'Andrei', 'status': 'confirmed', 'avatar': 'A'},
        {'name': 'Maria', 'status': 'confirmed', 'avatar': 'M'},
        {'name': 'Alex', 'status': 'confirmed', 'avatar': 'A'},
        {'name': 'Cristina', 'status': 'confirmed', 'avatar': 'C'},
        {'name': 'David', 'status': 'confirmed', 'avatar': 'D'},
        {'name': 'Elena', 'status': 'confirmed', 'avatar': 'E'},
      ],
      'agenda': [
        {'song': 'Oceans', 'focus': 'transitions', 'duration': 20},
        {'song': 'Way Maker', 'focus': 'dynamics', 'duration': 15},
      ],
      'hasRecording': true,
      'recordingUrl': 'https://...',
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.black,
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.white),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('Practice Mode'),
        actions: [
          IconButton(
            icon: const Icon(Icons.add, color: AppColors.coral),
            onPressed: () => _showScheduleRehearsal(),
          ),
        ],
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: _rehearsals.length,
        itemBuilder: (context, index) {
          return _buildRehearsalCard(_rehearsals[index]);
        },
      ),
    );
  }

  Widget _buildRehearsalCard(Map<String, dynamic> rehearsal) {
    final isCompleted = rehearsal['status'] == 'completed';
    final isScheduled = rehearsal['status'] == 'scheduled';

    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      decoration: BoxDecoration(
        color: AppColors.surface,
        borderRadius: BorderRadius.circular(16),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Header
          Container(
            padding: const EdgeInsets.all(16),
            decoration: BoxDecoration(
              gradient: LinearGradient(
                colors: isCompleted 
                    ? [AppColors.success.withOpacity(0.3), AppColors.surface]
                    : [AppColors.coral.withOpacity(0.3), AppColors.surface],
              ),
              borderRadius: const BorderRadius.vertical(top: Radius.circular(16)),
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Repetiție: "${rehearsal['programName']}"',
                      style: const TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.w600,
                        color: AppColors.white,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      '${rehearsal['date']} • ${rehearsal['time']} • ${rehearsal['location']}',
                      style: const TextStyle(fontSize: 13, color: AppColors.gray300),
                    ),
                  ],
                ),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                  decoration: BoxDecoration(
                    color: isCompleted 
                        ? AppColors.success.withOpacity(0.15) 
                        : AppColors.coral.withOpacity(0.15),
                    borderRadius: BorderRadius.circular(20),
                  ),
                  child: Text(
                    isCompleted ? '✅ Completat' : '📅 Programat',
                    style: TextStyle(
                      fontSize: 11,
                      fontWeight: FontWeight.w600,
                      color: isCompleted ? AppColors.success : AppColors.coral,
                    ),
                  ),
                ),
              ],
            ),
          ),

          // Attendance
          Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      'PREZENȚĂ',
                      style: TextStyle(
                        fontSize: 12,
                        fontWeight: FontWeight.w600,
                        color: AppColors.gray300,
                        letterSpacing: 1,
                      ),
                    ),
                    _attendanceSummary(rehearsal['attendees']),
                  ],
                ),
                const SizedBox(height: 12),
                Wrap(
                  spacing: 8,
                  runSpacing: 8,
                  children: (rehearsal['attendees'] as List).map((attendee) {
                    return _attendeeChip(attendee);
                  }).toList(),
                ),
                const SizedBox(height: 16),

                // Agenda
                const Text(
                  'AGENDĂ',
                  style: TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w600,
                    color: AppColors.gray300,
                    letterSpacing: 1,
                  ),
                ),
                const SizedBox(height: 12),
                ...(rehearsal['agenda'] as List).map((item) {
                  return _agendaItem(item);
                }).toList(),

                // Recording
                if (rehearsal['hasRecording'] == true) ...[
                  const SizedBox(height: 16),
                  Container(
                    padding: const EdgeInsets.all(12),
                    decoration: BoxDecoration(
                      color: AppColors.surfaceElevated,
                      borderRadius: BorderRadius.circular(12),
                    ),
                    child: Row(
                      children: [
                        Container(
                          width: 40,
                          height: 40,
                          decoration: BoxDecoration(
                            color: AppColors.coral.withOpacity(0.2),
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: const Icon(Icons.play_arrow, color: AppColors.coral),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                'Înregistrare repetiție',
                                style: TextStyle(
                                  fontSize: 14,
                                  fontWeight: FontWeight.w500,
                                  color: AppColors.white,
                                ),
                              ),
                              Text(
                                '${rehearsal['duration']} min • MP3',
                                style: const TextStyle(fontSize: 12, color: AppColors.gray300),
                              ),
                            ],
                          ),
                        ),
                        IconButton(
                          icon: const Icon(Icons.download, color: AppColors.coral),
                          onPressed: () {},
                        ),
                      ],
                    ),
                  ),
                ],

                // Actions
                if (isScheduled) ...[
                  const SizedBox(height: 16),
                  Row(
                    children: [
                      Expanded(
                        child: ElevatedButton(
                          onPressed: () => _showAttendanceSheet(rehearsal),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: AppColors.coral,
                            foregroundColor: AppColors.white,
                            padding: const EdgeInsets.all(12),
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                          ),
                          child: const Text('Confirmă prezența', style: TextStyle(fontWeight: FontWeight.w600)),
                        ),
                      ),
                      const SizedBox(width: 10),
                      Expanded(
                        child: OutlinedButton(
                          onPressed: () {},
                          style: OutlinedButton.styleFrom(
                            foregroundColor: AppColors.coral,
                            side: const BorderSide(color: AppColors.coral),
                            padding: const EdgeInsets.all(12),
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                          ),
                          child: const Text('Upload demo', style: TextStyle(fontWeight: FontWeight.w600)),
                        ),
                      ),
                    ],
                  ),
                ],
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _attendanceSummary(List attendees) {
    final confirmed = attendees.where((a) => a['status'] == 'confirmed').length;
    final total = attendees.length;
    return Text(
      '$confirmed/$total',
      style: const TextStyle(fontSize: 13, color: AppColors.gray300),
    );
  }

  Widget _attendeeChip(Map<String, dynamic> attendee) {
    final statusColors = {
      'confirmed': AppColors.success,
      'maybe': AppColors.warning,
      'declined': AppColors.coral,
      'no-response': AppColors.gray500,
    };

    final statusLabels = {
      'confirmed': '✓',
      'maybe': '?',
      'declined': '✕',
      'no-response': '•',
    };

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        color: (statusColors[attendee['status']] ?? AppColors.gray500).withOpacity(0.15),
        borderRadius: BorderRadius.circular(20),
        border: Border.all(
          color: (statusColors[attendee['status']] ?? AppColors.gray500).withOpacity(0.3),
        ),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Container(
            width: 20,
            height: 20,
            decoration: BoxDecoration(
              color: statusColors[attendee['status']] ?? AppColors.gray500,
              shape: BoxShape.circle,
            ),
            child: Center(
              child: Text(
                statusLabels[attendee['status']] ?? '•',
                style: const TextStyle(fontSize: 10, color: AppColors.white, fontWeight: FontWeight.w700),
              ),
            ),
          ),
          const SizedBox(width: 6),
          Text(
            attendee['name'],
            style: TextStyle(
              fontSize: 12,
              color: statusColors[attendee['status']] ?? AppColors.gray500,
              fontWeight: FontWeight.w500,
            ),
          ),
        ],
      ),
    );
  }

  Widget _agendaItem(Map<String, dynamic> item) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 8),
      decoration: BoxDecoration(
        border: Border(bottom: BorderSide(color: AppColors.surfaceElevated)),
      ),
      child: Row(
        children: [
          Container(
            width: 32,
            height: 32,
            decoration: BoxDecoration(
              color: AppColors.surfaceElevated,
              borderRadius: BorderRadius.circular(8),
            ),
            child: const Icon(Icons.music_note, color: AppColors.coral, size: 16),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  item['song'],
                  style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w500, color: AppColors.white),
                ),
                Text(
                  'Focus: ${item['focus']} • ${item['duration']} min',
                  style: const TextStyle(fontSize: 12, color: AppColors.gray300),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  void _showAttendanceSheet(Map<String, dynamic> rehearsal) {
    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.surface,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (context) => StatefulBuilder(
        builder: (context, setModalState) {
          return Container(
            padding: const EdgeInsets.all(20),
            child: Column(
              mainAxisSize: MainAxisSize.min,
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
                const Text(
                  'Confirmă prezența',
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.w700,
                    color: AppColors.white,
                  ),
                ),
                const SizedBox(height: 8),
                Text(
                  rehearsal['programName'],
                  style: const TextStyle(fontSize: 14, color: AppColors.gray300),
                ),
                const SizedBox(height: 20),
                ...(rehearsal['attendees'] as List).map((attendee) {
                  return _attendanceRow(attendee, setModalState);
                }).toList(),
                const SizedBox(height: 20),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: () => Navigator.pop(context),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.coral,
                      foregroundColor: AppColors.white,
                      padding: const EdgeInsets.all(16),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                    ),
                    child: const Text('Salvează', style: TextStyle(fontWeight: FontWeight.w600)),
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }

  Widget _attendanceRow(Map<String, dynamic> attendee, Function setModalState) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 10),
      child: Row(
        children: [
          Container(
            width: 36,
            height: 36,
            decoration: BoxDecoration(
              color: AppColors.coral.withOpacity(0.2),
              shape: BoxShape.circle,
            ),
            child: Center(
              child: Text(
                attendee['avatar'],
                style: const TextStyle(color: AppColors.coral, fontWeight: FontWeight.w700),
              ),
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Text(
              attendee['name'],
              style: const TextStyle(fontSize: 16, color: AppColors.white),
            ),
          ),
          Row(
            children: [
              _statusButton('✓', 'confirmed', attendee, setModalState),
              const SizedBox(width: 4),
              _statusButton('?', 'maybe', attendee, setModalState),
              const SizedBox(width: 4),
              _statusButton('✕', 'declined', attendee, setModalState),
            ],
          ),
        ],
      ),
    );
  }

  Widget _statusButton(String label, String status, Map<String, dynamic> attendee, Function setModalState) {
    final isSelected = attendee['status'] == status;
    final colors = {
      'confirmed': AppColors.success,
      'maybe': AppColors.warning,
      'declined': AppColors.coral,
    };

    return GestureDetector(
      onTap: () => setModalState(() => attendee['status'] = status),
      child: Container(
        width: 32,
        height: 32,
        decoration: BoxDecoration(
          color: isSelected ? (colors[status] ?? AppColors.gray500) : AppColors.surfaceElevated,
          borderRadius: BorderRadius.circular(8),
        ),
        child: Center(
          child: Text(
            label,
            style: TextStyle(
              fontSize: 14,
              fontWeight: FontWeight.w700,
              color: isSelected ? AppColors.white : AppColors.gray500,
            ),
          ),
        ),
      ),
    );
  }

  void _showScheduleRehearsal() {
    // Implementation for scheduling new rehearsal
    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.surface,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (context) => Container(
        padding: const EdgeInsets.all(20),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Text(
              'Programează repetiție',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.w700, color: AppColors.white),
            ),
            const SizedBox(height: 20),
            _inputField('Program'),
            const SizedBox(height: 12),
            _inputField('Data și ora'),
            const SizedBox(height: 12),
            _inputField('Locație'),
            const SizedBox(height: 12),
            _inputField('Durată (minute)'),
            const SizedBox(height: 20),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: () => Navigator.pop(context),
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.coral,
                  foregroundColor: AppColors.white,
                  padding: const EdgeInsets.all(16),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                child: const Text('Programează', style: TextStyle(fontWeight: FontWeight.w600)),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _inputField(String hint) {
    return TextField(
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
}
