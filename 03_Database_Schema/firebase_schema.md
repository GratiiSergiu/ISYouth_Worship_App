# ISYouth Worship App - Database Schema

## Platform: Firebase Firestore (NoSQL)

---

## Collections

### 1. `users` - Membri echipei
```json
{
  "userId": "string (auto)",
  "email": "string",
  "displayName": "string",
  "firstName": "string",
  "lastName": "string",
  "avatarUrl": "string (URL)",
  "role": "string [admin | leader | musician | technician | vocalist]",
  "instruments": ["string"],
  "teamId": "string (reference to teams)",
  "createdAt": "timestamp",
  "lastLogin": "timestamp",
  "preferences": {
    "darkMode": "boolean",
    "notifications": "boolean",
    "autoScrollLyrics": "boolean",
    "offlineDownload": "boolean",
    "defaultKey": "string"
  }
}
```

**Indexes:**
- `teamId` (ascending)
- `role` (ascending)
- `email` (ascending, unique)

---

### 2. `teams` - Echipe/Biserici
```json
{
  "teamId": "string (auto)",
  "name": "string",
  "slug": "string (URL-friendly)",
  "description": "string",
  "logoUrl": "string (URL)",
  "primaryColor": "string (hex)",
  "secondaryColor": "string (hex)",
  "createdBy": "string (userId)",
  "createdAt": "timestamp",
  "members": ["string (userIds)"],
  "settings": {
    "allowPublicPrograms": "boolean",
    "defaultProgramDuration": "number (minutes)",
    "autoNotifyOnChanges": "boolean"
  }
}
```

---

### 3. `songs` - Repertoriu
```json
{
  "songId": "string (auto)",
  "title": "string",
  "artist": "string",
  "album": "string (optional)",
  "key": "string [Do | Do# | Re | Re# | Mi | Fa | Fa# | Sol | Sol# | La | La# | Si]",
  "tempo": "number (BPM)",
  "timeSignature": "string [4/4 | 6/8 | 3/4 | etc]",
  "duration": "number (seconds)",
  "lyrics": {
    "original": "string (with chords inline)",
    "transposed": "string (optional)",
    "language": "string [ro | en | other]"
  },
  "chords": {
    "original": "string (ChordPro format)",
    "transposed": "string (optional)",
    "capo": "number (fret)"
  },
  "status": "string [repertoire | learning | new | archived]",
  "tags": ["string"],
  "teamId": "string (reference)",
  "addedBy": "string (userId)",
  "addedAt": "timestamp",
  "lastUsed": "timestamp",
  "usageCount": "number",
  "youtubeUrl": "string (optional)",
  "spotifyUrl": "string (optional)",
  "sheetMusicUrl": "string (optional)",
  "notes": "string"
}
```

**Indexes:**
- `teamId` + `status` (composite)
- `title` (ascending)
- `artist` (ascending)
- `tags` (array-contains)

---

### 4. `programs` - Programe/Servicii
```json
{
  "programId": "string (auto)",
  "title": "string",
  "theme": "string",
  "date": "timestamp",
  "teamId": "string (reference)",
  "createdBy": "string (userId)",
  "createdAt": "timestamp",
  "updatedAt": "timestamp",
  "status": "string [draft | ready | live | completed | cancelled]",
  "type": "string [sunday | youth | special | conference | other]",
  "location": "string",
  "notes": "string",
  "targetDuration": "number (minutes)",
  "actualDuration": "number (minutes, filled after)",
  "setlist": [
    {
      "position": "number",
      "songId": "string (reference)",
      "key": "string (performance key)",
      "capo": "number",
      "notes": "string",
      "duration": "number (seconds)",
      "lead": "string (userId)",
      "backup": ["string (userIds)"]
    }
  ],
  "segments": [
    {
      "name": "string [worship | prayer | sermon | offering | communion | etc]",
      "duration": "number (minutes)",
      "order": "number"
    }
  ],
  "teamAssignments": [
    {
      "userId": "string",
      "role": "string [lead | vocals | guitar | bass | drums | keys | sound | projection]",
      "confirmed": "boolean",
      "confirmedAt": "timestamp"
    }
  ],
  "ratings": [
    {
      "userId": "string",
      "rating": "number (1-5)",
      "comment": "string",
      "createdAt": "timestamp"
    }
  ],
  "sharedWith": ["string (userIds)"],
  "isPublic": "boolean"
}
```

**Indexes:**
- `teamId` + `date` (descending)
- `status` + `date`
- `createdBy`

---

### 5. `rehearsals` - Repetiții
```json
{
  "rehearsalId": "string (auto)",
  "programId": "string (reference)",
  "teamId": "string (reference)",
  "date": "timestamp",
  "location": "string",
  "duration": "number (minutes)",
  "status": "string [scheduled | in-progress | completed | cancelled]",
  "attendees": [
    {
      "userId": "string",
      "status": "string [confirmed | maybe | declined | no-response]",
      "notes": "string"
    }
  ],
  "agenda": [
    {
      "songId": "string",
      "focus": "string [chords | transitions | dynamics | harmonies]",
      "duration": "number (minutes)"
    }
  ],
  "recordingUrl": "string (optional)",
  "notes": "string",
  "createdBy": "string (userId)",
  "createdAt": "timestamp"
}
```

---

### 6. `notifications` - Notificări
```json
{
  "notificationId": "string (auto)",
  "userId": "string",
  "type": "string [program_created | program_updated | rehearsal_scheduled | song_added | team_invite | reminder]",
  "title": "string",
  "body": "string",
  "data": {
    "programId": "string (optional)",
    "songId": "string (optional)",
    "rehearsalId": "string (optional)"
  },
  "read": "boolean",
  "createdAt": "timestamp",
  "expiresAt": "timestamp (optional)"
}
```

---

### 7. `activity_logs` - Jurnal activitate
```json
{
  "logId": "string (auto)",
  "userId": "string",
  "teamId": "string",
  "action": "string [create_program | update_program | add_song | update_song | delete_song | schedule_rehearsal | confirm_attendance]",
  "targetType": "string [program | song | rehearsal | team]",
  "targetId": "string",
  "details": "string (JSON)",
  "createdAt": "timestamp"
}
```

---

## Security Rules

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    // Helper functions
    function isAuthenticated() {
      return request.auth != null;
    }

    function isTeamMember(teamId) {
      return isAuthenticated() && 
        get(/databases/$(database)/documents/teams/$(teamId)).data.members.hasAny([request.auth.uid]);
    }

    function isTeamAdmin(teamId) {
      return isAuthenticated() && 
        get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role in ['admin', 'leader'];
    }

    function ownsDocument(userId) {
      return isAuthenticated() && request.auth.uid == userId;
    }

    // Users collection
    match /users/{userId} {
      allow read: if isAuthenticated();
      allow create: if isAuthenticated() && request.auth.uid == userId;
      allow update: if ownsDocument(userId);
      allow delete: if false; // Never delete users
    }

    // Teams collection
    match /teams/{teamId} {
      allow read: if isAuthenticated() && isTeamMember(teamId);
      allow create: if isAuthenticated();
      allow update: if isTeamAdmin(teamId);
      allow delete: if isTeamAdmin(teamId);
    }

    // Songs collection
    match /songs/{songId} {
      allow read: if isAuthenticated() && isTeamMember(resource.data.teamId);
      allow create: if isAuthenticated() && isTeamMember(request.resource.data.teamId);
      allow update: if isAuthenticated() && isTeamMember(resource.data.teamId);
      allow delete: if isAuthenticated() && isTeamAdmin(resource.data.teamId);
    }

    // Programs collection
    match /programs/{programId} {
      allow read: if isAuthenticated() && 
        (isTeamMember(resource.data.teamId) || resource.data.isPublic == true);
      allow create: if isAuthenticated() && isTeamMember(request.resource.data.teamId);
      allow update: if isAuthenticated() && isTeamMember(resource.data.teamId);
      allow delete: if isAuthenticated() && isTeamAdmin(resource.data.teamId);
    }

    // Rehearsals collection
    match /rehearsals/{rehearsalId} {
      allow read: if isAuthenticated() && isTeamMember(resource.data.teamId);
      allow create: if isAuthenticated() && isTeamMember(request.resource.data.teamId);
      allow update: if isAuthenticated() && isTeamMember(resource.data.teamId);
      allow delete: if isAuthenticated() && isTeamAdmin(resource.data.teamId);
    }

    // Notifications
    match /notifications/{notificationId} {
      allow read: if isAuthenticated() && resource.data.userId == request.auth.uid;
      allow create, update, delete: if false; // Server-side only
    }

    // Activity logs
    match /activity_logs/{logId} {
      allow read: if isAuthenticated() && isTeamMember(resource.data.teamId);
      allow create, update, delete: if false; // Server-side only
    }
  }
}
```

---

## Data Flow Diagram

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Client    │────▶│  Firebase   │────▶│  Firestore  │
│  (Flutter)  │◀────│   Auth      │◀────│   DB        │
└─────────────┘     └─────────────┘     └─────────────┘
       │                                    │
       │                              ┌────┴────┐
       │                              │         │
       ▼                              ▼         ▼
┌─────────────┐                 ┌────────┐ ┌────────┐
│   Cloud     │                 │ songs  │ │programs│
│ Functions   │                 └────────┘ └────────┘
└─────────────┘                 ┌────────┐ ┌────────┐
                                │rehears.│ │  users │
                                └────────┘ └────────┘
```

---

## Offline Strategy

1. **Enable offline persistence:** Firestore caches automatically
2. **Critical data sync:** Songs, programs, team members
3. **Optimistic updates:** UI updates immediately, syncs in background
4. **Conflict resolution:** Last-write-wins with timestamp checks
5. **Queue operations:** Failed writes retry when connection restored
