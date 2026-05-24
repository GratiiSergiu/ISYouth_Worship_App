import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class TeamScreen extends StatelessWidget {
  const TeamScreen({super.key});

  final List<Map<String, dynamic>> members = [
    {'name': 'Andrei Popescu', 'role': 'Lider Worship • Chitară', 'initial': 'A', 'colors': [AppColors.coral, AppColors.indigo], 'badge': 'Admin'},
    {'name': 'Maria Ionescu', 'role': 'Voce • Pian', 'initial': 'M', 'colors': [AppColors.indigo, Color(0xFF6B5B95)], 'badge': 'Membru'},
    {'name': 'Alex Dumitru', 'role': 'Tobe • Percuție', 'initial': 'A', 'colors': [AppColors.success, Color(0xFF059669)], 'badge': 'Membru'},
    {'name': 'Cristina Marin', 'role': 'Voce • Chitară bass', 'initial': 'C', 'colors': [AppColors.warning, Color(0xFFD97706)], 'badge': 'Membru'},
    {'name': 'David Stan', 'role': 'Tehnică sunet • Proiecții', 'initial': 'D', 'colors': [Color(0xFF3B82F6), Color(0xFF1D4ED8)], 'badge': 'Tehnic'},
    {'name': 'Elena Rusu', 'role': 'Voce • Backing vocals', 'initial': 'E', 'colors': [Color(0xFF8B5CF6), Color(0xFF6D28D9)], 'badge': 'Membru'},
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
        title: const Text('Echipa ISY'),
        actions: [
          IconButton(
            icon: const Icon(Icons.person_add, color: AppColors.coral),
            onPressed: () {},
          ),
        ],
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'MEMBRI ACTIVI',
                  style: TextStyle(
                    fontSize: 14,
                    fontWeight: FontWeight.w600,
                    color: AppColors.gray300,
                    letterSpacing: 1,
                  ),
                ),
                Text(
                  '${members.length} membri',
                  style: const TextStyle(fontSize: 13, color: AppColors.gray300),
                ),
              ],
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: members.length,
              itemBuilder: (context, index) {
                final member = members[index];
                return _buildMemberItem(member);
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildMemberItem(Map<String, dynamic> member) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
      decoration: BoxDecoration(
        border: Border(bottom: BorderSide(color: AppColors.surfaceElevated, width: 1)),
      ),
      child: Row(
        children: [
          Container(
            width: 40,
            height: 40,
            decoration: BoxDecoration(
              gradient: LinearGradient(
                colors: [member['colors'][0], member['colors'][1]],
                begin: Alignment.topLeft,
                end: Alignment.bottomRight,
              ),
              borderRadius: BorderRadius.circular(50),
            ),
            child: Center(
              child: Text(
                member['initial'],
                style: const TextStyle(
                  color: AppColors.white,
                  fontWeight: FontWeight.w700,
                  fontSize: 16,
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
                  member['name'],
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.w500,
                    color: AppColors.white,
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  member['role'],
                  style: const TextStyle(fontSize: 13, color: AppColors.gray300),
                ),
              ],
            ),
          ),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
            decoration: BoxDecoration(
              color: member['badge'] == 'Admin'
                  ? AppColors.coral.withOpacity(0.15)
                  : AppColors.surfaceElevated,
              borderRadius: BorderRadius.circular(20),
            ),
            child: Text(
              member['badge'],
              style: TextStyle(
                fontSize: 11,
                color: member['badge'] == 'Admin' ? AppColors.coral : AppColors.gray300,
                fontWeight: FontWeight.w500,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
