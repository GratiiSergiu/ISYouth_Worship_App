# 🎸 ISYouth Worship App (Android Native)

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android" alt="Android">
  <img src="https://img.shields.io/badge/Kotlin-2.1.0-7F52FF?logo=kotlin" alt="Kotlin">
  <img src="https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?logo=jetpackcompose" alt="Jetpack Compose">
  <img src="https://img.shields.io/badge/License-MIT-green.svg" alt="License">
</p>

<p align="center">
  <b>Aplicație mobilă nativă Android pentru planificarea și organizarea programelor de worship ale echipei ISYouth Worship</b>
</p>

---

## 📱 Descriere

ISYouth Worship App este o aplicație mobilă nativă Android construită cu **Kotlin** și **Jetpack Compose (Material 3)**, concepută special pentru echipele de worship din biserici. Oferă un mod modern, intuitiv și colaborativ de a planifica setlist-uri, coordona echipe și analiza repertoriul — toate într-un design elegant în stil Spotify/Apple Music.

### 🎯 Pentru cine este
- Echipe de worship din biserici
- Lideri de tineret
- Muzicieni și tehnicieni de scenă
- Coordonatori de programe

---

## ✨ Caracteristici principale

### 🎵 Setlist Builder
- Creează și organizează programe de worship
- Adaugă **tag-uri** (#tineret, #adorare, #predică, #mărturie, #botez, #comuniune)
- Setează **durată target** (45/60/90 min) cu alertă de progres și avertizare la depășire
- **Flow serviciu** vizual: Intro → Worship → Moment Special → Predică → Trimitere
- Notițe per piesă și notițe generale de program

### 📋 Management programe
- Planificare servicii duminicale și întâlniri de tineret
- Statusuri: Draft → Programat / Ready → Completat
- Navigare directă între programe și lansare în Live Mode

### 🎸 Repertoriu ISYouth
- Catalog complet de cântări cu **status per piesă**: ✅ Repertoire | 🔄 În lucru | 🆕 Nou
- Căutare rapidă după titlu și autor, plus filtrare pe categorii și favorite
- **Transpunere automată în timp real**: transpunere acorduri sus/jos (+/- semitonuri)
- **Vedere Muzician (Acorduri & Versuri)** vs **Vedere Versuri**

### 📅 Practice Mode (Repetiții)
- Programarea repetițiilor cu locație, oră și durată
- **Attendance tracking interactiv**: Confirmat / Poate / Refuzat / Fără răspuns
- Agendă detaliată per repetiție cu focus (tranziții, dinamici, armonii) și durată
- Indicator înregistrări demo audio

### 📊 Calendar & Statistici
- **Calendar vizual interactiv** cu zile evidențiate pentru programe
- Selectare zi pentru a vedea sau crea program
- **Analiză repertoriu**: Top cântări frecvente vs. mai rar cântate
- Indicator de trend (crescător, stabil, descrescător) și istoric detaliat al interpretărilor

### 🔔 Notificări Push & Echipă
- Centru de notificări pentru modificări de setlist, programări de repetiții și membri noi
- Gestionare echipă: roluri (Lider Worship, Voce, Chitară, Pian, Tobe, Tehnic) și insigne de acces

### 📱 Live Mode
- Interfață dedicată pentru scenă fără distrageri (ecran complet)
- Selector de cântări cu comutare instantă
- Cronometru scenă în timp real cu play/pause
- Control dimensiune font (A- / A+) pentru citire confortabilă pe stativ

---

## 🎨 Design System

### Paletă de culori ISYouth

| Culoare | Hex | Utilizare |
|---------|-----|-----------|
| **Negru profund** | `#0A0A0F` | Background principal |
| **Suprafață** | `#1A1A24` | Card-uri, containere |
| **Suprafață ridicată** | `#252532` | Chip-uri, elevații |
| **Indigo** | `#4A3B6B` | Accente secundare, rol tehnic |
| **Coral** | `#E85D5D` | CTA, accent primar, Live Mode |
| **Coral deschis** | `#FF7A7A` | Subtitluri, highlights |
| **Alb** | `#FFFFFF` | Text principal, versuri |
| **Succes** | `#10B981` | Confirmări, completat |
| **Avertizare** | `#F59E0B` | Draft, în așteptare |

---

## 🛠️ Tehnologie & Arhitectură

- **Limbaj:** Kotlin 2.1.0
- **UI Framework:** Jetpack Compose (BOM 2025.02.00) & Material 3
- **Arhitectură:** MVVM (Model-View-ViewModel) cu StateFlow & Coroutines
- **Build System:** Gradle (Kotlin DSL), Android SDK 35/36
- **Testability:** Compose TestTags pe toate acțiunile principale

---

## 📱 Ecrane implementate

1. **Dashboard (Acasă)**: Hero card pentru următorul program, acțiuni rapide (Setlist, Cântări, Live, Repetiții, Calendar, Statistici), sumar metrici repertoriu.
2. **Repertoriu Cântări**: Căutare, filtre, adăugare cântare, foaie de detaliu cu transpunere și comutare acorduri/versuri.
3. **Setlist Builder**: Planificator complet cu calcul dinamic durată, segmente, reordonare piese, etichete și notițe.
4. **Live Mode**: Interfață de scenă cu dimensiune ajustabilă font, transpunere cheie, selector piese și timer.
5. **Practice Mode**: Agendă repetiții, urmărire prezență (RSVP) și înregistrări.
6. **Calendar**: Vizualizare lunară interactivă a programelor de worship.
7. **Statistici**: Rotație piese, clasamente cele mai cântate / rar cântate, istoric per program.
8. **Echipa**: Catalog membri, roluri pe departamente (vocal, instrumente, tehnic) și adăugare membri.
9. **Notificări**: Centru de alerte cu marcaj citire și navigare directă.
10. **Profil & Setări**: Statistici personale și setări specifice pentru scenă.

---

## 📄 Licență

Acest proiect este licențiat sub [MIT License](LICENSE).

