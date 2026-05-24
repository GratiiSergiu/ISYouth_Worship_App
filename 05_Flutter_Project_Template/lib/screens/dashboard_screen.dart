import 'package:flutter/material.dart';
import '../theme/app_theme.dart';
import '../widgets/program_card.dart';
import '../widgets/section_header.dart';

class DashboardScreen extends StatefulWidget {
  const DashboardScreen({super.key});

  @override
  State<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  int _selectedIndex = 0;

  final List<Map<String, dynamic>> programs = [
    {
      'title': 'Identitate',
      'theme': 'Cine suntem în Hristos',
      'date': '25 Mai 2025',
      'status': 'DUMINICĂ',
      'statusColor': AppColors.coral,
      'songs': 5,
      'duration': 48,
      'members': 6,
      'accentColors': [AppColors.coral, AppColors.indigo],
      'tags': ['#tineret', '#adorare'],
    },
    {
      'title': 'Credință',
      'theme': 'Tineret ISYouth',
      'date': '18 Mai 2025',
      'status': 'COMPLETAT',
      'statusColor': AppColors.success,
      'songs': 6,
      'duration': 52,
      'rating': 4.8,
      'accentColors': [AppColors.indigo, AppColors.coral],
      'tags': ['#tineret', '#mărturie'],
    },
    {
      'title': 'Har',
      'theme': 'Serviciu Special',
      'date': '1 Iunie 2025',
      'status': 'ÎN AȘTEPTARE',
      'statusColor': AppColors.warning,
      'songs': 3,
      'duration': 35,
      'status2': 'Draft',
      'accentColors': [AppColors.gray500, AppColors.indigo],
      'tags': ['#botez', '#adorare'],
    },
  ];

  void _onNavTap(int index) {
    setState(() => _selectedIndex = index);
    switch (index) {
      case 0: break;
      case 1: Navigator.pushNamed(context, '/songs'); break;
      case 2: Navigator.pushNamed(context, '/builder'); break;
      case 3: Navigator.pushNamed(context, '/team'); break;
      case 4: Navigator.pushNamed(context, '/profile'); break;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.black,
      body: SafeArea(
        child: CustomScrollView(
          slivers: [
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    _headerButton(Icons.menu),
                    const Text(
                      'ISYouth Worship',
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.w700,
                        color: AppColors.white,
                      ),
                    ),
                    _headerButton(Icons.notifications_none),
                  ],
                ),
              ),
            ),
            // Quick Actions Row 1
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
                child: Row(
                  children: [
                    _quickAction('🎸', 'Repetiții', () => Navigator.pushNamed(context, '/practice')),
                    const SizedBox(width: 12),
                    _quickAction('📝', 'Program Nou', () => Navigator.pushNamed(context, '/builder')),
                    const SizedBox(width: 12),
                    _quickAction('🎵', 'Cântări', () => Navigator.pushNamed(context, '/songs')),
                  ],
                ),
              ),
            ),
            // Quick Actions Row 2
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
                child: Row(
                  children: [
                    _quickAction('📅', 'Calendar', () => Navigator.pushNamed(context, '/calendar')),
                    const SizedBox(width: 12),
                    _quickAction('📊', 'Statistici', () => Navigator.pushNamed(context, '/statistics')),
                    const SizedBox(width: 12),
                    _quickAction('🔔', 'Alerte', () => Navigator.pushNamed(context, '/notifications')),
                  ],
                ),
              ),
            ),
            SliverToBoxAdapter(
              child: SectionHeader(
                title: '🎵 Următorul Program',
                action: 'Vezi toate',
                onAction: () {},
              ),
            ),
            SliverToBoxAdapter(
              child: ProgramCard(
                title: programs[0]['title'],
                theme: programs[0]['theme'],
                date: programs[0]['date'],
                status: programs[0]['status'],
                statusColor: programs[0]['statusColor'],
                songs: programs[0]['songs'],
                duration: programs[0]['duration'],
                members: programs[0]['members'],
                accentColors: programs[0]['accentColors'],
                tags: programs[0]['tags'],
                onTap: () => Navigator.pushNamed(context, '/builder'),
              ),
            ),
            SliverToBoxAdapter(
              child: SectionHeader(
                title: '📋 Programe Recente',
                action: null,
                onAction: null,
              ),
            ),
            SliverList(
              delegate: SliverChildBuilderDelegate(
                (context, index) => ProgramCard(
                  title: programs[index + 1]['title'],
                  theme: programs[index + 1]['theme'],
                  date: programs[index + 1]['date'],
                  status: programs[index + 1]['status'],
                  statusColor: programs[index + 1]['statusColor'],
                  songs: programs[index + 1]['songs'],
                  duration: programs[index + 1]['duration'],
                  members: programs[index + 1].containsKey('members') ? programs[index + 1]['members'] : null,
                  rating: programs[index + 1].containsKey('rating') ? programs[index + 1]['rating'] : null,
                  status2: programs[index + 1].containsKey('status2') ? programs[index + 1]['status2'] : null,
                  tags: programs[index + 1].containsKey('tags') ? programs[index + 1]['tags'] : null,
                  accentColors: programs[index + 1]['accentColors'],
                  onTap: () {},
                ),
                childCount: 2,
              ),
            ),
            SliverToBoxAdapter(
              child: SectionHeader(
                title: '🎸 Repertoriu ISY',
                action: 'Toate cântările',
                onAction: () => Navigator.pushNamed(context, '/songs'),
              ),
            ),
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Row(
                  children: [
                    _statCard('47', 'Cântări totale', AppColors.coral),
                    const SizedBox(width: 16),
                    _statCard('12', 'În lucru', AppColors.success),
                  ],
                ),
              ),
            ),
            // Upcoming Rehearsals
            SliverToBoxAdapter(
              child: SectionHeader(
                title: '📅 Repetiții',
                action: 'Vezi toate',
                onAction: () => Navigator.pushNamed(context, '/practice'),
              ),
            ),
            SliverToBoxAdapter(
              child: GestureDetector(
                onTap: () => Navigator.pushNamed(context, '/practice'),
                child: Container(
                  margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
                  padding: const EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    color: AppColors.surface,
                    borderRadius: BorderRadius.circular(16),
                    border: Border.all(color: AppColors.coral.withOpacity(0.3)),
                  ),
                  child: Row(
                    children: [
                      Container(
                        width: 48,
                        height: 48,
                        decoration: BoxDecoration(
                          color: AppColors.coral.withOpacity(0.2),
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: const Icon(Icons.event, color: AppColors.coral),
                      ),
                      const SizedBox(width: 16),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text(
                              'Repetiție: "Identitate"',
                              style: TextStyle(
                                fontSize: 16,
                                fontWeight: FontWeight.w600,
                                color: AppColors.white,
                              ),
                            ),
                            const SizedBox(height: 4),
                            const Text(
                              '23 Mai • 19:00 • Sala Tineret',
                              style: TextStyle(fontSize: 13, color: AppColors.gray300),
                            ),
                            const SizedBox(height: 4),
                            Text(
                              '4/6 confirmați',
                              style: TextStyle(fontSize: 12, color: AppColors.success),
                            ),
                          ],
                        ),
                      ),
                      const Icon(Icons.chevron_right, color: AppColors.gray300),
                    ],
                  ),
                ),
              ),
            ),
            const SliverPadding(padding: EdgeInsets.only(bottom: 100)),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showAddModal(context),
        child: const Icon(Icons.add, size: 28),
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _selectedIndex,
        onTap: _onNavTap,
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.home), label: 'Acasă'),
          BottomNavigationBarItem(icon: Icon(Icons.music_note), label: 'Cântări'),
          BottomNavigationBarItem(icon: Icon(Icons.edit_note), label: 'Program'),
          BottomNavigationBarItem(icon: Icon(Icons.people), label: 'Echipa'),
          BottomNavigationBarItem(icon: Icon(Icons.person), label: 'Profil'),
        ],
      ),
    );
  }

  Widget _headerButton(IconData icon) {
    return Container(
      width: 36,
      height: 36,
      decoration: BoxDecoration(
        color: AppColors.surface,
        borderRadius: BorderRadius.circular(10),
      ),
      child: Icon(icon, color: AppColors.white, size: 20),
    );
  }

  Widget _quickAction(String emoji, String label, VoidCallback onTap) {
    return Expanded(
      child: GestureDetector(
        onTap: onTap,
        child: Container(
          padding: const EdgeInsets.symmetric(vertical: 14),
          decoration: BoxDecoration(
            color: AppColors.surface,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Column(
            children: [
              Text(emoji, style: const TextStyle(fontSize: 24)),
              const SizedBox(height: 4),
              Text(
                label,
                style: const TextStyle(fontSize: 12, color: AppColors.gray300, fontWeight: FontWeight.w500),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _statCard(String value, String label, Color color) {
    return Expanded(
      child: Container(
        padding: const EdgeInsets.all(20),
        decoration: BoxDecoration(
          color: AppColors.surface,
          borderRadius: BorderRadius.circular(16),
        ),
        child: Column(
          children: [
            Text(
              value,
              style: TextStyle(
                fontSize: 28,
                fontWeight: FontWeight.w700,
                color: color,
              ),
            ),
            const SizedBox(height: 4),
            Text(
              label,
              style: const TextStyle(fontSize: 12, color: AppColors.gray300),
            ),
          ],
        ),
      ),
    );
  }

  void _showAddModal(BuildContext context) {
    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.surface,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (context) => Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              width: 40,
              height: 4,
              decoration: BoxDecoration(
                color: AppColors.gray500,
                borderRadius: BorderRadius.circular(2),
              ),
            ),
            const SizedBox(height: 20),
            const Text(
              'Adaugă în program',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.w700,
                color: AppColors.white,
              ),
            ),
            const SizedBox(height: 20),
            _modalOption(Icons.music_note, 'Din repertoriu', 'Alege din cântările ISYouth'),
            _modalOption(Icons.star, 'Cântare nouă', 'Adaugă manual titlu și versuri'),
            _modalOption(Icons.download, 'Import', 'Din ChordPro, PDF sau CCLI'),
            const SizedBox(height: 12),
            SizedBox(
              width: double.infinity,
              child: TextButton(
                onPressed: () => Navigator.pop(context),
                style: TextButton.styleFrom(
                  backgroundColor: AppColors.surfaceElevated,
                  padding: const EdgeInsets.all(16),
                  shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                ),
                child: const Text('Anulează', style: TextStyle(color: AppColors.white, fontSize: 16, fontWeight: FontWeight.w600)),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _modalOption(IconData icon, String title, String desc) {
    return ListTile(
      leading: Container(
        width: 40,
        height: 40,
        decoration: BoxDecoration(
          color: AppColors.surfaceElevated,
          borderRadius: BorderRadius.circular(10),
        ),
        child: Icon(icon, color: AppColors.coral),
      ),
      title: Text(title, style: const TextStyle(color: AppColors.white, fontWeight: FontWeight.w500)),
      subtitle: Text(desc, style: const TextStyle(color: AppColors.gray300, fontSize: 13)),
      onTap: () => Navigator.pop(context),
    );
  }
}
