# Ghid de Contribuție

## Cum să contribui

### 1. Raportează un bug
- Folosește secțiunea **Issues** pe GitHub
- Descrie pas cu pas cum să reproduci bug-ul
- Include screenshot-uri dacă este relevant
- Specifică versiunea Flutter și platforma (Android/iOS/Web)

### 2. Sugerează o funcționalitate
- Deschide un Issue cu label `enhancement`
- Explică de ce este utilă funcționalitatea
- Descrie cum ar funcționa

### 3. Contribuie cu cod

#### Setup mediu dezvoltare
```bash
# 1. Fork repository-ul
# 2. Clonează fork-ul tău
git clone https://github.com/USERNAME/isyouth-worship-app.git

# 3. Creează un branch nou
git checkout -b feature/nume-feature

# 4. Fă modificările
# 5. Testează
flutter test

# 6. Commit
git add .
git commit -m "feat: descriere scurtă a modificărilor"

# 7. Push
git push origin feature/nume-feature

# 8. Deschide Pull Request pe GitHub
```

#### Convenții de cod
- Folosește **Dart style guide**
- Numele variabilelor: `camelCase`
- Numele claselor: `PascalCase`
- Numele fișierelor: `snake_case.dart`
- Adaugă **documentație** pentru funcții publice
- Folosește **const** unde este posibil

#### Structura commit-urilor
```
feat: adaugă funcționalitate nouă
fix: repară bug
 docs: modifică documentație
style: formatare cod (fără modificări logice)
refactor: refactorizare cod
perf: îmbunătățire performanță
test: adaugă/modifică teste
chore: modificări de build, dependențe, etc.
```

---

## Echipa ISYouth Worship
Mulțumim pentru contribuția ta! 🙏
