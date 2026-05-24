#!/bin/bash
# ISYouth Worship App - GitHub Setup Script
# Rulează acest script pentru a configura și pusha proiectul pe GitHub

echo "🎸 ISYouth Worship App - GitHub Setup"
echo "======================================"
echo ""

# Verifică dacă Git este instalat
if ! command -v git &> /dev/null; then
    echo "❌ Git nu este instalat. Instalează Git mai întâi:"
    echo "   https://git-scm.com/downloads"
    exit 1
fi

echo "✅ Git detectat"

# Verifică dacă suntem în folderul corect
if [ ! -f "README.md" ]; then
    echo "❌ Nu ești în folderul corect al proiectului."
    echo "   Navighează în folderul ISYouth_Worship_App și rulează din nou."
    exit 1
fi

echo "✅ Folder proiect detectat"

# Inițializează Git (dacă nu e deja)
if [ ! -d ".git" ]; then
    echo "📦 Inițializez repository Git..."
    git init
    echo "✅ Repository inițializat"
else
    echo "✅ Repository Git existent detectat"
fi

# Configurează Git (opțional - doar dacă nu e configurat)
if [ -z "$(git config --global user.name)" ]; then
    echo ""
    read -p "Introdu numele tău pentru Git: " git_name
    git config --global user.name "$git_name"
fi

if [ -z "$(git config --global user.email)" ]; then
    echo ""
    read -p "Introdu email-ul tău pentru Git: " git_email
    git config --global user.email "$git_email"
fi

echo ""
echo "📋 Pașii următori:"
echo "1. Creează un repository nou pe GitHub: https://github.com/new"
echo "2. Copiază URL-ul repository-ului (ex: https://github.com/USERNAME/isyouth-worship-app.git)"
echo ""
read -p "Introdu URL-ul repository-ului GitHub: " repo_url

# Adaugă remote
echo "🔗 Conectez la GitHub..."
git remote add origin "$repo_url" 2>/dev/null || git remote set-url origin "$repo_url"

# Adaugă toate fișierele
echo "📁 Adaug fișierele..."
git add .

# Commit
echo "💾 Creez commit..."
git commit -m "🎸 Initial commit: ISYouth Worship App

Features included:
- Design System with ISYouth color palette
- Interactive HTML wireframes/prototype
- Firebase Firestore database schema
- REST API + WebSocket specifications
- Flutter project template with 11 screens:
  * Splash, Dashboard, Setlist Builder, Songs, Team
  * Profile, Live Mode, Practice Mode, Calendar
  * Statistics, Notifications
- Web/PWA version with Service Worker
- Complete documentation

Ready for development and deployment."

# Setează branch principal
git branch -M main

# Push
echo "🚀 Push pe GitHub..."
git push -u origin main

echo ""
echo "✅ Proiectul a fost încărcat cu succes pe GitHub!"
echo "🔗 URL: $repo_url"
echo ""
echo "💡 Sfaturi:"
echo "   - Adaugă un screenshot în README pentru a arăta UI-ul"
echo "   - Creează un Release când ai o versiune stabilă"
echo "   - Folosește Issues pentru bug-uri și feature requests"
echo ""
echo "🎸 ISYouth Worship - Planificăm cu pasiune!"
