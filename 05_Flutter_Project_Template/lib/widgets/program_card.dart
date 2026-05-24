import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class ProgramCard extends StatelessWidget {
  final String title;
  final String theme;
  final String date;
  final String status;
  final Color statusColor;
  final int songs;
  final int duration;
  final int? members;
  final double? rating;
  final String? status2;
  final List<dynamic>? tags;
  final List<Color> accentColors;
  final VoidCallback onTap;

  const ProgramCard({
    super.key,
    required this.title,
    required this.theme,
    required this.date,
    required this.status,
    required this.statusColor,
    required this.songs,
    required this.duration,
    this.members,
    this.rating,
    this.status2,
    this.tags,
    required this.accentColors,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
        decoration: BoxDecoration(
          color: AppColors.surface,
          borderRadius: BorderRadius.circular(16),
        ),
        child: ClipRRect(
          borderRadius: BorderRadius.circular(16),
          child: Column(
            children: [
              Row(
                children: [
                  Container(
                    width: 4,
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        colors: accentColors,
                        begin: Alignment.topCenter,
                        end: Alignment.bottomCenter,
                      ),
                    ),
                  ),
                  Expanded(
                    child: Padding(
                      padding: const EdgeInsets.all(20),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text(
                                '"$title"',
                                style: const TextStyle(
                                  fontSize: 18,
                                  fontWeight: FontWeight.w600,
                                  color: AppColors.white,
                                ),
                              ),
                              Container(
                                padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                                decoration: BoxDecoration(
                                  color: statusColor.withOpacity(0.15),
                                  borderRadius: BorderRadius.circular(20),
                                ),
                                child: Text(
                                  status,
                                  style: TextStyle(
                                    fontSize: 11,
                                    fontWeight: FontWeight.w600,
                                    color: statusColor,
                                  ),
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 4),
                          Text(
                            '$date • $theme',
                            style: const TextStyle(fontSize: 14, color: AppColors.gray300),
                          ),
                          if (tags != null && tags!.isNotEmpty) ...[
                            const SizedBox(height: 8),
                            Wrap(
                              spacing: 6,
                              runSpacing: 4,
                              children: tags!.map((tag) {
                                return Container(
                                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                                  decoration: BoxDecoration(
                                    color: AppColors.coral.withOpacity(0.1),
                                    borderRadius: BorderRadius.circular(12),
                                    border: Border.all(
                                      color: AppColors.coral.withOpacity(0.2),
                                    ),
                                  ),
                                  child: Text(
                                    tag,
                                    style: const TextStyle(
                                      fontSize: 11,
                                      color: AppColors.coral,
                                      fontWeight: FontWeight.w500,
                                    ),
                                  ),
                                );
                              }).toList(),
                            ),
                          ],
                          const SizedBox(height: 12),
                          Row(
                            children: [
                              _stat('🎵', '$songs cântări'),
                              const SizedBox(width: 12),
                              _stat('⏱', '$duration min'),
                              if (members != null) ...[
                                const SizedBox(width: 12),
                                _stat('👥', '$members membri'),
                              ],
                              if (rating != null) ...[
                                const SizedBox(width: 12),
                                _stat('⭐', '$rating/5'),
                              ],
                              if (status2 != null) ...[
                                const SizedBox(width: 12),
                                _stat('⏳', status2!),
                              ],
                            ],
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
      ),
    );
  }

  Widget _stat(String icon, String text) {
    return Row(
      children: [
        Text(icon, style: const TextStyle(fontSize: 12)),
        const SizedBox(width: 4),
        Text(
          text,
          style: const TextStyle(fontSize: 13, color: AppColors.gray300),
        ),
      ],
    );
  }
}
