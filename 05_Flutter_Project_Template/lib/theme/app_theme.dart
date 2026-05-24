import 'package:flutter/material.dart';

class AppColors {
  static const Color black = Color(0xFF0A0A0F);
  static const Color surface = Color(0xFF1A1A24);
  static const Color surfaceElevated = Color(0xFF252532);
  static const Color indigo = Color(0xFF4A3B6B);
  static const Color coral = Color(0xFFE85D5D);
  static const Color coralLight = Color(0xFFFF7A7A);
  static const Color white = Color(0xFFFFFFFF);
  static const Color gray100 = Color(0xFFE5E5E5);
  static const Color gray300 = Color(0xFF9CA3AF);
  static const Color gray500 = Color(0xFF6B7280);
  static const Color success = Color(0xFF10B981);
  static const Color warning = Color(0xFFF59E0B);
}

class AppTheme {
  static ThemeData get darkTheme {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.dark,
      scaffoldBackgroundColor: AppColors.black,
      colorScheme: const ColorScheme.dark(
        primary: AppColors.coral,
        secondary: AppColors.indigo,
        surface: AppColors.surface,
        background: AppColors.black,
        onPrimary: AppColors.white,
        onSecondary: AppColors.white,
        onSurface: AppColors.white,
        onBackground: AppColors.white,
      ),
      appBarTheme: const AppBarTheme(
        backgroundColor: AppColors.black,
        elevation: 0,
        centerTitle: true,
        titleTextStyle: TextStyle(
          fontSize: 18,
          fontWeight: FontWeight.w700,
          color: AppColors.white,
          fontFamily: 'Inter',
        ),
      ),
      cardTheme: CardTheme(
        color: AppColors.surface,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        elevation: 0,
      ),
      bottomNavigationBarTheme: const BottomNavigationBarThemeData(
        backgroundColor: AppColors.black,
        selectedItemColor: AppColors.coral,
        unselectedItemColor: AppColors.gray500,
        type: BottomNavigationBarType.fixed,
        elevation: 0,
      ),
      floatingActionButtonTheme: FloatingActionButtonThemeData(
        backgroundColor: AppColors.coral,
        foregroundColor: AppColors.white,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: AppColors.surface,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: BorderSide.none,
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(12),
          borderSide: const BorderSide(color: AppColors.coral, width: 1.5),
        ),
        contentPadding: const EdgeInsets.all(16),
        hintStyle: const TextStyle(color: AppColors.gray500, fontSize: 16),
      ),
      textTheme: const TextTheme(
        displayLarge: TextStyle(fontSize: 32, fontWeight: FontWeight.w700, color: AppColors.white, letterSpacing: -0.5),
        headlineLarge: TextStyle(fontSize: 24, fontWeight: FontWeight.w700, color: AppColors.white, letterSpacing: -0.3),
        headlineMedium: TextStyle(fontSize: 20, fontWeight: FontWeight.w600, color: AppColors.white, letterSpacing: -0.2),
        titleLarge: TextStyle(fontSize: 18, fontWeight: FontWeight.w600, color: AppColors.white),
        bodyLarge: TextStyle(fontSize: 16, fontWeight: FontWeight.w400, color: AppColors.white, height: 1.5),
        bodyMedium: TextStyle(fontSize: 14, fontWeight: FontWeight.w400, color: AppColors.gray300, height: 1.4),
        labelLarge: TextStyle(fontSize: 14, fontWeight: FontWeight.w500, color: AppColors.gray300, letterSpacing: 0.2),
      ),
      fontFamily: 'Inter',
    );
  }
}
