import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'providers/song_provider.dart';
import 'providers/program_provider.dart';
import 'screens/splash_screen.dart';
import 'screens/dashboard_screen.dart';
import 'screens/setlist_builder_screen.dart';
import 'screens/songs_screen.dart';
import 'screens/team_screen.dart';
import 'screens/profile_screen.dart';
import 'screens/live_mode_screen.dart';
import 'screens/practice_mode_screen.dart';
import 'screens/calendar_screen.dart';
import 'screens/statistics_screen.dart';
import 'screens/notifications_screen.dart';
import 'theme/app_theme.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  final songProvider = SongProvider();
  final programProvider = ProgramProvider();
  await Future.wait([songProvider.load(), programProvider.load()]);

  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider.value(value: songProvider),
        ChangeNotifierProvider.value(value: programProvider),
      ],
      child: const ISYouthWorshipApp(),
    ),
  );
}

class ISYouthWorshipApp extends StatelessWidget {
  const ISYouthWorshipApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ISYouth Worship',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.darkTheme,
      home: const SplashScreen(),
      routes: {
        '/dashboard': (context) => const DashboardScreen(),
        '/builder': (context) => SetlistBuilderScreen(
              programId: ModalRoute.of(context)?.settings.arguments as String?,
            ),
        '/songs': (context) => const SongsScreen(),
        '/team': (context) => const TeamScreen(),
        '/profile': (context) => const ProfileScreen(),
        '/live': (context) => const LiveModeScreen(),
        '/practice': (context) => const PracticeModeScreen(),
        '/calendar': (context) => const CalendarScreen(),
        '/statistics': (context) => const StatisticsScreen(),
        '/notifications': (context) => const NotificationsScreen(),
      },
    );
  }
}
