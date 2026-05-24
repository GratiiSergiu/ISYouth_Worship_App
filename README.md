# 🎸 ISYouth Worship App

<p align="center">
  <img src="https://img.shields.io/badge/Flutter-3.x-02569B?logo=flutter" alt="Flutter">
  <img src="https://img.shields.io/badge/Firebase-Cloud%20Firestore-FFCA28?logo=firebase" alt="Firebase">
  <img src="https://img.shields.io/badge/Platform-Android%20%7C%20iOS-3DDC84?logo=android" alt="Platform">
  <img src="https://img.shields.io/badge/PWA-Progressive%20Web%20App-5A0FC8?logo=pwa" alt="PWA">
  <img src="https://img.shields.io/badge/License-MIT-green.svg" alt="License">
</p>

<p align="center">
  <b>Aplicație mobilă pentru planificarea și organizarea programelor de worship ale echipei ISYouth Worship</b>
</p>

---

## 📱 Descriere

ISYouth Worship App este o aplicație **cross-platform** (Android + iOS) construită cu **Flutter** și **Firebase**, concepută special pentru echipele de worship din biserici. Oferă un mod modern, intuitiv și colaborativ de a planifica setlist-uri, coordona echipe și analiza repertoriul — toate într-un design inspirat de Spotify/Apple Music, nu de PowerPoint 2003.

### 🎯 Pentru cine este
- Echipe de worship din biserici
- Lideri de tineret
- Muzicieni și tehnicieni de scenă
- Coordonatori de programe

---

## ✨ Caracteristici principale

### 🎵 Setlist Builder
- Creează programe cu **drag & drop**
- Adaugă **tag-uri** (#tineret, #adorare, #predică, #mărturie)
- Setează **durată target** (45/60/90 min) cu alertă când depășești
- **Flow serviciu** vizual: Intro → Worship → Moment Special → Predică → Trimitere
- Timer per segment

### 📋 Management programe
- Planificare pe săptămâni/luni în avans
- Statusuri: Draft → Ready → Live → Completat
- Duplicare rapidă a programelor
- Partajare cu echipa

### 🎸 Repertoriu ISYouth
- Catalog complet cu **status per piesă**: ✅ Repertoire | 🔄 În lucru | 🆕 Nou
- **Asignări muzicieni**: Lead, Chitară, Tobe, etc.
- Căutare și filtre avansate
- **Transpunere one-tap**: capo 0-6 + schimbare cheie (Do, Re, Mi, Fa, Sol, La, Si)

### 📅 Practice Mode
- Programarea repetițiilor cu **reminder automat**
- **Attendance tracking**: Confirmed / Maybe / Declined / No-response
- Upload **înregistrări demo** și practice sessions
- Agenda per repetiție (focus: transitions, dynamics, harmonies)

### 📊 Calendar & Statistici
- **Calendar vizual** cu zile evidențiate (punct colorat = program)
- Click pe zi → detalii complete program
- **Analiză repertoriu**: Top cântate vs. Mai puțin cântate
- Alertă "Frecventă" pentru piesele cântate des (≥15x)
- Istoric complet per piesă: date, programe, poziții în setlist

### 🔔 Notificări Push
- Alertări pentru întreaga echipă
- Tipuri: program actualizat, repetiție programată, membru nou, reminder
- Badge cu notificări necitite

### 📱 Live Mode
- **Muzician View**: acorduri deasupra versurilor, font mare
- **Versuri View**: fullscreen, scroll automat
- Progress bar, controale playback
- Dark mode obligatoriu pentru scenă

### 🌐 Versiune Web/PWA
- Funcționează în browser
- Instalabilă ca aplicație nativă
- Offline support via Service Worker

---

## 🎨 Design System

### Paletă de culori ISYouth

| Culoare | Hex | Utilizare |
|---------|-----|-----------|
| **Negru profund** | `#0A0A0F` | Background principal |
| **Suprafață** | `#1A1A24` | Card-uri, input-uri |
| **Suprafață ridicată** | `#252532` | Hover, elevated |
| **Indigo** | `#4A3B6B` | Gradient, accent secundar |
| **Coral** | `#E85D5D` | CTA, accent principal, live indicator |
| **Coral deschis** | `#FF7A7A` | Hover, highlights |
| **Alb** | `#FFFFFF` | Text principal, versuri |
| **Succes** | `#10B981` | Confirmări, completat |
| **Avertizare** | `#F59E0B` | Draft, în așteptare |

### Identitate vizuală
- **Vibe**: Clean, modern, tineresc — mai mult Spotify, mai puțin PowerPoint 2003
- **Font**: Inter (Regular, Medium, SemiBold, Bold)
- **Border radius**: 12-16px
- **Animații**: 200-300ms, ease-out, cubic-bezier

---

## 🛠️ Tehnologie

| Componentă | Tehnologie |
|------------|-----------|
| **Frontend** | Flutter 3.x (Dart) |
| **Backend** | Firebase (Firestore, Auth, Storage, Cloud Functions) |
| **State Management** | Provider |
| **Real-time** | WebSocket pentru Live Mode |
| **PWA** | HTML5, Service Worker, Manifest |

---

## 📂 Structură proiect

```
ISYouth_Worship_App/
│
├── 📁 01_Design_System/
│   └── design_system.md              # Paletă culori, tipografie, componente, animații
│
├── 📁 02_Wireframes_Interactive/
│   └── prototip_interactiv.html      # Prototip HTML complet funcțional (deschide în browser)
│
├── 📁 03_Database_Schema/
│   └── firebase_schema.md            # Schema Firebase Firestore + Security Rules
│
├── 📁 04_API_Specs/
│   └── api_documentation.md          # Documentație API REST + WebSocket
│
├── 📁 05_Flutter_Project_Template/
│   ├── lib/
│   │   ├── main.dart                  # Entry point + routes
│   │   ├── theme/
│   │   │   └── app_theme.dart         # Design system Flutter (culori, tipografie)
│   │   ├── screens/
│   │   │   ├── splash_screen.dart     # Splash animat cu logo ISYouth
│   │   │   ├── dashboard_screen.dart  # Dashboard cu quick actions, programe, statistici
│   │   │   ├── setlist_builder_screen.dart  # Builder cu tags, flow serviciu, asignări
│   │   │   ├── songs_screen.dart      # Repertoriu cu transpunere, Muzician View
│   │   │   ├── team_screen.dart       # Echipa cu roluri și badge-uri
│   │   │   ├── profile_screen.dart    # Profil cu statistici și setări
│   │   │   ├── live_mode_screen.dart  # Live Mode: Muzician View + Versuri
│   │   │   ├── practice_mode_screen.dart  # Repetiții, attendance, upload
│   │   │   ├── calendar_screen.dart   # Calendar cu programe evidențiate
│   │   │   ├── statistics_screen.dart # Analiză repertoriu, istoric per piesă
│   │   │   └── notifications_screen.dart  # Notificări push pentru echipă
│   │   └── widgets/
│   │       ├── program_card.dart      # Card program reutilizabil
│   │       └── section_header.dart    # Header secțiune reutilizabil
│   ├── pubspec.yaml                   # Dependențe Flutter
│   └── assets/                        # Imagini, fonturi (adaugă manual)
│
├── 📁 06_Assets/
│   └── README.md                      # Ghid pentru logo, fonturi, imagini
│
├── 📁 07_Documentation/
│   ├── README.md                      # Documentație proiect (acest fișier)
│   └── ghid_dezvoltare.md            # Ghid complet dezvoltare
│
├── 📁 08_Web_PWA/
│   ├── index.html                     # Pagină web responsive, instalabilă
│   ├── manifest.json                  # Config PWA
│   └── sw.js                          # Service Worker pentru offline
│
├── .gitignore                         # Fișiere ignorate de Git
└── README.md                          # Acest fișier
```

---

## 🚀 Cum să începi

### 1. Instalare Flutter
```bash
# Verifică versiunea (necesar ≥ 3.0.0)
flutter --version

# Dacă nu ai Flutter: https://flutter.dev/docs/get-started/install
```

### 2. Clonează proiectul
```bash
git clone https://github.com/USERNAME/isyouth-worship-app.git
cd isyouth-worship-app/05_Flutter_Project_Template
```

### 3. Instalează dependențe
```bash
flutter pub get
```

### 4. Configurează Firebase
1. Creează proiect în [Firebase Console](https://console.firebase.google.com)
2. Adaugă aplicații Android și iOS
3. Descarcă `google-services.json` (Android) și `GoogleService-Info.plist` (iOS)
4. Plasează fișierele în locațiile corespunzătoare
5. Activează Authentication (Google Sign-In) și Firestore Database

### 5. Adaugă fonturile
```bash
# Descarcă Inter de la https://fonts.google.com/specimen/Inter
# Copiază fișierele în 05_Flutter_Project_Template/assets/fonts/
# Asigură-te că pubspec.yaml include fonturile (deja configurat)
```

### 6. Rulează aplicația
```bash
# Android
flutter run

# iOS (necesită macOS + Xcode)
flutter run -d ios

# Web
flutter run -d chrome
```

---

## 📱 Ecrane implementate

| # | Ecran | Status | Funcționalități |
|---|-------|--------|----------------|
| 1 | Splash Screen | ✅ | Animație logo ISYouth, particule, gradient rotativ |
| 2 | Dashboard | ✅ | Quick actions, programe, repetiții, statistici |
| 3 | Setlist Builder | ✅ | Tags, target duration, flow serviciu, asignări muzicieni |
| 4 | Repertoriu | ✅ | Căutare, filtre, transpunere, Muzician View preview |
| 5 | Echipa | ✅ | Membri, roluri, badge-uri colorate |
| 6 | Profil | ✅ | Statistici personale, toggle setări |
| 7 | Live Mode | ✅ | Toggle Muzician View/Versuri, controale playback |
| 8 | Practice Mode | ✅ | Repetiții, attendance, upload demo, înregistrări |
| 9 | Calendar | ✅ | Zile evidențiate, click → detalii program |
| 10 | Statistici | ✅ | Top/rare, perioadă, istoric complet per piesă |
| 11 | Notificări | ✅ | Push notifications, badge necitite, acțiuni |

---

## 🧪 Testare

```bash
# Unit tests
flutter test

# Integration tests
flutter test integration_test/

# Build release Android
flutter build apk --release
flutter build appbundle

# Build release iOS
flutter build ios --release
flutter build ipa

# Build web/PWA
flutter build web --release
```

---

## 🌐 Versiune Web/PWA

Pentru a folosi versiunea web fără Flutter:

```bash
cd 08_Web_PWA
# Deschide index.html într-un server local
python3 -m http.server 8000
# Sau: npx serve .
# Accesează http://localhost:8000
```

Pentru instalare pe mobil:
- **Android Chrome**: Meniu → "Adaugă pe ecranul de start"
- **iOS Safari**: Share → "Adaugă pe ecranul de pornire"

---

## 🤝 Contribuție

1. Fork repository
2. Creează un branch: `git checkout -b feature/nume-feature`
3. Commit modificările: `git commit -am 'Adaugă feature nou'`
4. Push pe branch: `git push origin feature/nume-feature`
5. Deschide un Pull Request

---

## 📄 Licență

Acest proiect este licențiat sub [MIT License](LICENSE).

---

<p align="center">
  <b>ISYouth Worship Team</b><br>
  <i>Planificăm worship-ul cu pasiune și excelență</i>
</p>
