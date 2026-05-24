import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class NotificationsScreen extends StatefulWidget {
  const NotificationsScreen({super.key});

  @override
  State<NotificationsScreen> createState() => _NotificationsScreenState();
}

class _NotificationsScreenState extends State<NotificationsScreen> {
  final List<Map<String, dynamic>> _notifications = [
    {
      'id': '1',
      'type': 'program_updated',
      'title': 'Program actualizat',
      'body': '"Identitate" a fost modificat. O nouă cântare a fost adăugată.',
      'time': 'Acum 5 min',
      'read': false,
      'icon': '📝',
      'color': AppColors.coral,
      'action': '/builder',
    },
    {
      'id': '2',
      'type': 'rehearsal_scheduled',
      'title': 'Repetiție programată',
      'body': 'Repetiție pentru "Identitate" pe 23 Mai la 19:00 în Sala Tineret.',
      'time': 'Acum 2 ore',
      'read': false,
      'icon': '🎸',
      'color': AppColors.indigo,
      'action': '/practice',
    },
    {
      'id': '3',
      'type': 'team_invite',
      'title': 'Membru nou',
      'body': 'Ioana Popescu s-a alăturat echipei ISYouth Worship ca vocalist.',
      'time': 'Acum 5 ore',
      'read': true,
      'icon': '👤',
      'color': AppColors.success,
      'action': '/team',
    },
    {
      'id': '4',
      'type': 'song_added',
      'title': 'Cântare nouă adăugată',
      'body': '"Firm Foundation" (Cody Carnes) a fost adăugată în repertoriu.',
      'time': 'Ieri',
      'read': true,
      'icon': '🎵',
      'color': AppColors.warning,
      'action': '/songs',
    },
    {
      'id': '5',
      'type': 'reminder',
      'title': 'Reminder program',
      'body': 'Programul "Identitate" este mâine la 10:00. Nu uita să confirmi prezența!',
      'time': 'Ieri',
      'read': true,
      'icon': '⏰',
      'color': AppColors.coral,
      'action': '/builder',
    },
    {
      'id': '6',
      'type': 'program_updated',
      'title': 'Setlist finalizat',
      'body': 'Setlist-ul pentru "Credință" a fost finalizat de Andrei.',
      'time': '18 Mai',
      'read': true,
      'icon': '✅',
      'color': AppColors.success,
      'action': '/builder',
    },
    {
      'id': '7',
      'type': 'rehearsal_scheduled',
      'title': 'Repetiție anulată',
      'body': 'Repetiția de astăzi a fost anulată din cauza indisponibilității sălii.',
      'time': '17 Mai',
      'read': true,
      'icon': '🚫',
      'color': AppColors.coral,
      'action': '/practice',
    },
  ];

  int get _unreadCount => _notifications.where((n) => !n['read']).length;

  void _markAsRead(String id) {
    setState(() {
      final index = _notifications.indexWhere((n) => n['id'] == id);
      if (index != -1) {
        _notifications[index]['read'] = true;
      }
    });
  }

  void _markAllAsRead() {
    setState(() {
      for (var notification in _notifications) {
        notification['read'] = true;
      }
    });
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
        title: Row(
          children: [
            const Text('Notificări'),
            if (_unreadCount > 0) ...[
              const SizedBox(width: 10),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                decoration: BoxDecoration(
                  color: AppColors.coral,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Text(
                  '$_unreadCount',
                  style: const TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w700,
                    color: AppColors.white,
                  ),
                ),
              ),
            ],
          ],
        ),
        actions: [
          if (_unreadCount > 0)
            TextButton(
              onPressed: _markAllAsRead,
              child: const Text(
                'Citește tot',
                style: TextStyle(color: AppColors.coral, fontSize: 13),
              ),
            ),
        ],
      ),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: _notifications.length,
        itemBuilder: (context, index) {
          return _buildNotificationItem(_notifications[index]);
        },
      ),
    );
  }

  Widget _buildNotificationItem(Map<String, dynamic> notification) {
    final isUnread = !notification['read'];

    return GestureDetector(
      onTap: () {
        _markAsRead(notification['id']);
        final action = notification['action'] as String?;
        if (action != null && action.isNotEmpty) {
          Navigator.pushNamed(context, action);
        }
      },
      child: Container(
        margin: const EdgeInsets.only(bottom: 10),
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: isUnread ? AppColors.surface : AppColors.surface.withOpacity(0.6),
          borderRadius: BorderRadius.circular(12),
          border: Border.all(
            color: isUnread ? (notification['color'] as Color).withOpacity(0.3) : Colors.transparent,
          ),
        ),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              width: 44,
              height: 44,
              decoration: BoxDecoration(
                color: (notification['color'] as Color).withOpacity(0.15),
                borderRadius: BorderRadius.circular(12),
              ),
              child: Center(
                child: Text(
                  notification['icon'],
                  style: const TextStyle(fontSize: 22),
                ),
              ),
            ),
            const SizedBox(width: 14),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      Expanded(
                        child: Text(
                          notification['title'],
                          style: TextStyle(
                            fontSize: 15,
                            fontWeight: isUnread ? FontWeight.w600 : FontWeight.w500,
                            color: AppColors.white,
                          ),
                        ),
                      ),
                      if (isUnread)
                        Container(
                          width: 8,
                          height: 8,
                          decoration: const BoxDecoration(
                            color: AppColors.coral,
                            shape: BoxShape.circle,
                          ),
                        ),
                    ],
                  ),
                  const SizedBox(height: 4),
                  Text(
                    notification['body'],
                    style: TextStyle(
                      fontSize: 13,
                      color: AppColors.gray300,
                      height: 1.4,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    notification['time'],
                    style: const TextStyle(
                      fontSize: 11,
                      color: AppColors.gray500,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
