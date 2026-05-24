import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:math';
import '../theme/app_theme.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> with TickerProviderStateMixin {
  late AnimationController _logoController;
  late AnimationController _gradientController;
  late AnimationController _particleController;

  late Animation<double> _logoScale;
  late Animation<double> _logoFade;
  late Animation<double> _subtitleFade;
  late Animation<double> _gradientRotation;

  final List<Particle> _particles = [];
  final Random _random = Random();

  @override
  void initState() {
    super.initState();

    // Initialize particles
    for (int i = 0; i < 30; i++) {
      _particles.add(Particle(
        x: _random.nextDouble(),
        y: _random.nextDouble(),
        size: _random.nextDouble() * 3 + 1,
        speed: _random.nextDouble() * 0.5 + 0.2,
        opacity: _random.nextDouble() * 0.5 + 0.2,
      ));
    }

    _logoController = AnimationController(
      duration: const Duration(milliseconds: 1800),
      vsync: this,
    );

    _gradientController = AnimationController(
      duration: const Duration(seconds: 8),
      vsync: this,
    );

    _particleController = AnimationController(
      duration: const Duration(seconds: 4),
      vsync: this,
    )..repeat();

    _logoScale = Tween<double>(begin: 0.6, end: 1.0).animate(
      CurvedAnimation(
        parent: _logoController,
        curve: const Interval(0.0, 0.6, curve: Curves.easeOutBack),
      ),
    );

    _logoFade = Tween<double>(begin: 0, end: 1).animate(
      CurvedAnimation(
        parent: _logoController,
        curve: const Interval(0.0, 0.5, curve: Curves.easeOut),
      ),
    );

    _subtitleFade = Tween<double>(begin: 0, end: 1).animate(
      CurvedAnimation(
        parent: _logoController,
        curve: const Interval(0.4, 0.8, curve: Curves.easeOut),
      ),
    );

    _gradientRotation = Tween<double>(begin: 0, end: 2 * pi).animate(
      CurvedAnimation(parent: _gradientController, curve: Curves.linear),
    );

    _logoController.forward();
    _gradientController.repeat();

    Timer(const Duration(seconds: 3), () {
      Navigator.pushReplacementNamed(context, '/dashboard');
    });
  }

  @override
  void dispose() {
    _logoController.dispose();
    _gradientController.dispose();
    _particleController.dispose();
    super.dispose();
    }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.black,
      body: Stack(
        children: [
          // Animated gradient background - ISYouth colors
          AnimatedBuilder(
            animation: _gradientRotation,
            builder: (context, child) {
              return Container(
                decoration: BoxDecoration(
                  gradient: SweepGradient(
                    center: Alignment.center,
                    transform: GradientRotation(_gradientRotation.value),
                    colors: [
                      const Color(0xFF0A0A0F),
                      const Color(0xFF1A1A2E).withOpacity(0.4),
                      const Color(0xFF4A3B6B).withOpacity(0.3),
                      const Color(0xFFE85D5D).withOpacity(0.15),
                      const Color(0xFFFF7A7A).withOpacity(0.1),
                      const Color(0xFF0A0A0F),
                    ],
                  ),
                ),
              );
            },
          ),

          // Radial glows
          Positioned(
            top: -100,
            left: -100,
            child: Container(
              width: 400,
              height: 400,
              decoration: BoxDecoration(
                gradient: RadialGradient(
                  colors: [
                    const Color(0xFF4A3B6B).withOpacity(0.4),
                    Colors.transparent,
                  ],
                ),
              ),
            ),
          ),
          Positioned(
            bottom: -100,
            right: -100,
            child: Container(
              width: 400,
              height: 400,
              decoration: BoxDecoration(
                gradient: RadialGradient(
                  colors: [
                    const Color(0xFFE85D5D).withOpacity(0.3),
                    Colors.transparent,
                  ],
                ),
              ),
            ),
          ),

          // Floating particles
          AnimatedBuilder(
            animation: _particleController,
            builder: (context, child) {
              return Stack(
                children: _particles.map((particle) {
                  final offset = (_particleController.value + particle.x) % 1.0;
                  return Positioned(
                    left: particle.x * MediaQuery.of(context).size.width,
                    top: (particle.y + offset * particle.speed) % 1.0 * MediaQuery.of(context).size.height,
                    child: Opacity(
                      opacity: particle.opacity * (0.5 + 0.5 * sin(offset * 2 * pi)),
                      child: Container(
                        width: particle.size,
                        height: particle.size,
                        decoration: BoxDecoration(
                          color: const Color(0xFFE85D5D),
                          borderRadius: BorderRadius.circular(particle.size / 2),
                          boxShadow: [
                            BoxShadow(
                              color: const Color(0xFFE85D5D).withOpacity(0.5),
                              blurRadius: particle.size * 2,
                              spreadRadius: particle.size / 2,
                            ),
                          ],
                        ),
                      ),
                    ),
                  );
                }).toList(),
              );
            },
          ),

          // Main content
          Center(
            child: FadeTransition(
              opacity: _logoFade,
              child: ScaleTransition(
                scale: _logoScale,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    // ISYouth Logo Text - exact colors from uploaded logo
                    ShaderMask(
                      shaderCallback: (bounds) {
                        return const LinearGradient(
                          colors: [
                            Color(0xFF0A0A0F),  // Black
                            Color(0xFF4A3B6B),  // Indigo
                            Color(0xFFE85D5D),  // Coral/Red
                            Color(0xFFFF7A7A),  // Light coral
                          ],
                          stops: [0.0, 0.35, 0.7, 1.0],
                          begin: Alignment.centerLeft,
                          end: Alignment.centerRight,
                        ).createShader(bounds);
                      },
                      child: const Text(
                        'ISYouth',
                        style: TextStyle(
                          fontSize: 56,
                          fontWeight: FontWeight.w900,
                          color: Colors.white,
                          letterSpacing: -1,
                          height: 1.1,
                        ),
                      ),
                    ),
                    const SizedBox(height: 8),
                    // Decorative line
                    Container(
                      width: 120,
                      height: 3,
                      decoration: BoxDecoration(
                        gradient: const LinearGradient(
                          colors: [Color(0xFF4A3B6B), Color(0xFFE85D5D)],
                        ),
                        borderRadius: BorderRadius.circular(2),
                      ),
                    ),
                    const SizedBox(height: 20),
                    FadeTransition(
                      opacity: _subtitleFade,
                      child: Column(
                        children: [
                          const Text(
                            'WORSHIP',
                            style: TextStyle(
                              fontSize: 28,
                              fontWeight: FontWeight.w300,
                              color: Colors.white,
                              letterSpacing: 8,
                            ),
                          ),
                          const SizedBox(height: 6),
                          Text(
                            'PLANNING',
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.w600,
                              color: const Color(0xFFE85D5D).withOpacity(0.8),
                              letterSpacing: 6,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),

          // Bottom loading indicator
          Positioned(
            bottom: 60,
            left: 0,
            right: 0,
            child: FadeTransition(
              opacity: _subtitleFade,
              child: Center(
                child: Column(
                  children: [
                    SizedBox(
                      width: 120,
                      child: LinearProgressIndicator(
                        backgroundColor: const Color(0xFF1A1A24),
                        valueColor: const AlwaysStoppedAnimation<Color>(Color(0xFFE85D5D)),
                        borderRadius: BorderRadius.circular(2),
                      ),
                    ),
                    const SizedBox(height: 12),
                    Text(
                      'Încărcare...',
                      style: TextStyle(
                        fontSize: 12,
                        color: const Color(0xFF9CA3AF),
                        letterSpacing: 2,
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class Particle {
  final double x;
  final double y;
  final double size;
  final double speed;
  final double opacity;

  Particle({
    required this.x,
    required this.y,
    required this.size,
    required this.speed,
    required this.opacity,
  });
}
