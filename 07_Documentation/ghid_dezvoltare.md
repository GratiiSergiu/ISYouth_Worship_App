# Ghid Dezvoltare ISYouth Worship App

## 1. Setup mediu dezvoltare

### Instalare Flutter
```bash
# macOS
brew install flutter

# Windows
# Descarcă de la https://flutter.dev/docs/get-started/install

# Verificare
flutter doctor
flutter --version  # >= 3.0.0
```

### Instalare dependențe
```bash
cd 05_Flutter_Project_Template/
flutter pub get
```

### Configurare Firebase
1. Creează proiect în [Firebase Console](https://console.firebase.google.com)
2. Adaugă aplicații Android și iOS
3. Descarcă `google-services.json` (Android) și `GoogleService-Info.plist` (iOS)
4. Plasează fișierele în locațiile corespunzătoare
5. Activează Authentication (Google Sign-In) și Firestore Database

## 2. Arhitectură aplicație

### State Management (Provider)
```
lib/
├── providers/
│   ├── auth_provider.dart
│   ├── program_provider.dart
│   ├── song_provider.dart
│   └── team_provider.dart
```

### Services (API layer)
```
├── services/
│   ├── firebase_service.dart
│   ├── program_service.dart
│   ├── song_service.dart
│   └── notification_service.dart
```

### Models
```
├── models/
│   ├── user.dart
│   ├── song.dart
│   ├── program.dart
│   └── rehearsal.dart
```

## 3. Flow utilizator

```
[Splash] → [Login/Google] → [Dashboard]
                              ↓
                    ┌────────┼────────┐
                    ↓        ↓        ↓
                [Programe] [Cântări] [Echipa]
                    ↓        ↓        ↓
              [Builder]  [Detalii] [Profil]
                    ↓
               [Live Mode]
```

## 4. Feature roadmap

### MVP (Săptămâna 1-2)
- [x] Splash screen
- [x] Dashboard cu programe
- [x] Setlist builder basic
- [x] Repertoriu cu căutare
- [x] Live mode prototype

### V1.1 (Săptămâna 3-4)
- [ ] Autentificare Firebase
- [ ] Sincronizare real-time
- [ ] Notificări push
- [ ] Transpunere acorduri
- [ ] Import ChordPro/PDF

### V1.2 (Luna 2)
- [ ] Calendar integrat
- [ ] Repetiții scheduling
- [ ] Raportare statistici
- [ ] Mod offline
- [ ] Partajare program (link)

### V2.0 (Luna 3)
- [ ] WebSocket live sync
- [ ] Multi-team support
- [ ] Integrare Spotify/YouTube
- [ ] Audio player integrat
- [ ] Recording rehearsals

## 5. Testare

```bash
# Unit tests
flutter test

# Integration tests
flutter test integration_test/

# Build release
flutter build apk --release      # Android
flutter build ios --release      # iOS
```

## 6. Deployment

### Android
```bash
flutter build appbundle
# Upload pe Google Play Console
```

### iOS
```bash
flutter build ipa
# Upload pe App Store Connect
```

## 7. Resurse utile

- [Flutter Documentation](https://docs.flutter.dev)
- [Firebase Flutter](https://firebase.google.com/docs/flutter/setup)
- [Firestore Security Rules](https://firebase.google.com/docs/firestore/security/get-started)
- [Material Design 3](https://m3.material.io)

---

**Contact:** ISYouth Worship Team
