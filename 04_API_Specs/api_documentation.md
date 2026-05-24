# ISYouth Worship App - API Specifications

## Base URL
```
Production: https://api.isyouth-worship.app/v1
Development: http://localhost:8080/v1
```

## Authentication
All endpoints require Bearer token:
```
Authorization: Bearer <firebase_id_token>
```

---

## Endpoints

### Authentication

#### POST `/auth/login`
Firebase token exchange.
```json
// Request
{
  "firebaseToken": "string"
}

// Response 200
{
  "success": true,
  "data": {
    "user": {
      "userId": "uid_123",
      "email": "andrei@example.com",
      "displayName": "Andrei Popescu",
      "role": "admin",
      "teamId": "team_456"
    },
    "token": "jwt_token_here",
    "expiresAt": "2025-06-24T10:00:00Z"
  }
}
```

#### POST `/auth/refresh`
Refresh JWT token.
```json
// Response 200
{
  "success": true,
  "data": {
    "token": "new_jwt_token",
    "expiresAt": "2025-06-25T10:00:00Z"
  }
}
```

---

### Programs (Setlists)

#### GET `/programs`
List programs for user's team.
```
Query params:
  ?status=draft|ready|live|completed
  &dateFrom=2025-05-01
  &dateTo=2025-06-30
  &limit=20
  &offset=0
```
```json
// Response 200
{
  "success": true,
  "data": {
    "programs": [
      {
        "programId": "prog_789",
        "title": "Identitate",
        "theme": "Cine suntem în Hristos",
        "date": "2025-05-25T10:00:00Z",
        "status": "ready",
        "type": "youth",
        "targetDuration": 48,
        "songCount": 5,
        "teamSize": 6,
        "createdAt": "2025-05-20T14:30:00Z"
      }
    ],
    "total": 23,
    "hasMore": true
  }
}
```

#### GET `/programs/{programId}`
Get program details.
```json
// Response 200
{
  "success": true,
  "data": {
    "programId": "prog_789",
    "title": "Identitate",
    "theme": "Cine suntem în Hristos",
    "date": "2025-05-25T10:00:00Z",
    "status": "ready",
    "type": "youth",
    "location": "Sala Tineret",
    "notes": "Focalizare pe botezuri",
    "targetDuration": 48,
    "actualDuration": null,
    "setlist": [
      {
        "position": 1,
        "songId": "song_001",
        "title": "Oceans (Where Feet May Fail)",
        "artist": "Hillsong United",
        "key": "Do",
        "capo": 0,
        "duration": 272,
        "notes": "Intro lent, build la vers 2",
        "lead": "Andrei Popescu",
        "backup": ["Maria Ionescu", "Elena Rusu"]
      }
    ],
    "segments": [
      {"name": "worship", "duration": 25, "order": 1},
      {"name": "sermon", "duration": 20, "order": 2},
      {"name": "worship", "duration": 15, "order": 3}
    ],
    "teamAssignments": [
      {
        "userId": "uid_123",
        "name": "Andrei Popescu",
        "role": "lead",
        "confirmed": true,
        "confirmedAt": "2025-05-22T10:00:00Z"
      }
    ],
    "createdBy": {
      "userId": "uid_123",
      "name": "Andrei Popescu"
    },
    "createdAt": "2025-05-20T14:30:00Z",
    "updatedAt": "2025-05-23T09:15:00Z"
  }
}
```

#### POST `/programs`
Create new program.
```json
// Request
{
  "title": "Identitate",
  "theme": "Cine suntem în Hristos",
  "date": "2025-05-25T10:00:00Z",
  "type": "youth",
  "location": "Sala Tineret",
  "notes": "",
  "targetDuration": 48,
  "setlist": [
    {
      "position": 1,
      "songId": "song_001",
      "key": "Do",
      "capo": 0,
      "notes": ""
    }
  ]
}

// Response 201
{
  "success": true,
  "data": {
    "programId": "prog_790",
    "createdAt": "2025-05-24T10:00:00Z"
  }
}
```

#### PUT `/programs/{programId}`
Update program.
```json
// Request (partial update)
{
  "title": "Identitate - Updated",
  "status": "ready"
}

// Response 200
{
  "success": true,
  "data": {
    "programId": "prog_789",
    "updatedAt": "2025-05-24T10:05:00Z"
  }
}
```

#### DELETE `/programs/{programId}`
Delete program (admin only).
```json
// Response 200
{
  "success": true,
  "message": "Program deleted successfully"
}
```

#### POST `/programs/{programId}/duplicate`
Duplicate program.
```json
// Request
{
  "newDate": "2025-06-01T10:00:00Z",
  "newTitle": "Identitate - Repetare"
}

// Response 201
{
  "success": true,
  "data": {
    "programId": "prog_791"
  }
}
```

#### POST `/programs/{programId}/share`
Share program with team.
```json
// Request
{
  "userIds": ["uid_456", "uid_789"],
  "permissions": "view" // view | edit
}

// Response 200
{
  "success": true,
  "data": {
    "sharedCount": 2
  }
}
```

#### POST `/programs/{programId}/go-live`
Start live mode.
```json
// Response 200
{
  "success": true,
  "data": {
    "liveSessionId": "live_123",
    "startedAt": "2025-05-25T10:00:00Z",
    "currentSong": 0
  }
}
```

---

### Songs

#### GET `/songs`
List songs.
```
Query params:
  ?status=repertoire|learning|new|archived
  &key=Do
  &tag=worship
  &search=oceans
  &limit=50
  &offset=0
```
```json
// Response 200
{
  "success": true,
  "data": {
    "songs": [
      {
        "songId": "song_001",
        "title": "Oceans (Where Feet May Fail)",
        "artist": "Hillsong United",
        "key": "Do",
        "tempo": 65,
        "timeSignature": "4/4",
        "duration": 272,
        "status": "repertoire",
        "tags": ["worship", "slow", "intimacy"],
        "usageCount": 12,
        "lastUsed": "2025-05-18T10:00:00Z"
      }
    ],
    "total": 47
  }
}
```

#### GET `/songs/{songId}`
Get song details.
```json
// Response 200
{
  "success": true,
  "data": {
    "songId": "song_001",
    "title": "Oceans (Where Feet May Fail)",
    "artist": "Hillsong United",
    "album": "Zion",
    "key": "Do",
    "tempo": 65,
    "timeSignature": "4/4",
    "duration": 272,
    "lyrics": {
      "original": "You call me out upon the waters...",
      "language": "en"
    },
    "chords": {
      "original": "[Verse 1]\n{Do} You call me out upon the {Sol} waters...",
      "capo": 0
    },
    "status": "repertoire",
    "tags": ["worship", "slow", "intimacy"],
    "youtubeUrl": "https://youtube.com/...",
    "spotifyUrl": "https://spotify.com/...",
    "notes": "Build dramatic la bridge",
    "usageCount": 12,
    "lastUsed": "2025-05-18T10:00:00Z",
    "addedBy": "Andrei Popescu",
    "addedAt": "2024-01-15T10:00:00Z"
  }
}
```

#### POST `/songs`
Add new song.
```json
// Request
{
  "title": "Firm Foundation",
  "artist": "Cody Carnes",
  "key": "Fa",
  "tempo": 72,
  "timeSignature": "4/4",
  "duration": 228,
  "lyrics": {
    "original": "Christ is my firm foundation...",
    "language": "en"
  },
  "chords": {
    "original": "[Verse 1]\n{Fa} Christ is my firm foundation...",
    "capo": 0
  },
  "tags": ["worship", "declaration"],
  "youtubeUrl": "",
  "notes": ""
}

// Response 201
{
  "success": true,
  "data": {
    "songId": "song_048",
    "createdAt": "2025-05-24T10:10:00Z"
  }
}
```

#### PUT `/songs/{songId}`
Update song.

#### DELETE `/songs/{songId}`
Delete song.

#### POST `/songs/{songId}/transpose`
Transpose chords.
```json
// Request
{
  "newKey": "Sol",
  "capo": 3
}

// Response 200
{
  "success": true,
  "data": {
    "originalKey": "Do",
    "newKey": "Sol",
    "capo": 3,
    "transposedChords": "[Verse 1]\n{Sol} You call me out upon the {Re} waters..."
  }
}
```

---

### Rehearsals

#### GET `/rehearsals`
List rehearsals.

#### GET `/rehearsals/{rehearsalId}`
Get rehearsal details.

#### POST `/rehearsals`
Schedule rehearsal.
```json
// Request
{
  "programId": "prog_789",
  "date": "2025-05-23T19:00:00Z",
  "location": "Sala Tineret",
  "duration": 90,
  "agenda": [
    {
      "songId": "song_001",
      "focus": "transitions",
      "duration": 20
    }
  ]
}
```

#### POST `/rehearsals/{rehearsalId}/attendance`
Confirm attendance.
```json
// Request
{
  "status": "confirmed", // confirmed | maybe | declined
  "notes": "Vin cu 10 min întârziere"
}
```

---

### Team

#### GET `/team`
Get team details.
```json
// Response 200
{
  "success": true,
  "data": {
    "teamId": "team_456",
    "name": "ISYouth Worship",
    "slug": "isyouth-worship",
    "description": "Echipa de worship a tineretului ISYouth",
    "logoUrl": "https://...",
    "memberCount": 6,
    "songCount": 47,
    "programCount": 23,
    "members": [
      {
        "userId": "uid_123",
        "name": "Andrei Popescu",
        "role": "admin",
        "instruments": ["chitară", "voce"],
        "avatarUrl": "https://..."
      }
    ]
  }
}
```

#### POST `/team/invite`
Invite member.
```json
// Request
{
  "email": "newmember@example.com",
  "role": "musician",
  "instruments": ["pian"]
}
```

---

### Live Mode (WebSocket)

#### WebSocket `/ws/live/{programId}`
Real-time live mode synchronization.

**Client → Server:**
```json
{
  "type": "next_song",
  "timestamp": "2025-05-25T10:05:00Z"
}
```

**Server → Client:**
```json
{
  "type": "song_change",
  "data": {
    "currentSong": 1,
    "songId": "song_002",
    "title": "Way Maker",
    "progress": 0,
    "isPlaying": true
  }
}
```

**Events:**
- `song_change` - Changed to next/previous song
- `progress_update` - Current playback position
- `lyrics_scroll` - Auto-scroll position
- `team_message` - Message to all team members
- `panic` - Emergency stop/reset

---

### Notifications

#### GET `/notifications`
Get user notifications.
```
Query params:
  ?unreadOnly=true
  &limit=20
```

#### POST `/notifications/{notificationId}/read`
Mark as read.

#### POST `/notifications/read-all`
Mark all as read.

---

## Error Responses

### Standard Error Format
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Title is required",
    "details": [
      {
        "field": "title",
        "message": "Title must be between 1 and 100 characters"
      }
    ]
  }
}
```

### Error Codes
| Code | HTTP Status | Description |
|------|-------------|-------------|
| `UNAUTHORIZED` | 401 | Invalid or missing token |
| `FORBIDDEN` | 403 | Insufficient permissions |
| `NOT_FOUND` | 404 | Resource not found |
| `VALIDATION_ERROR` | 422 | Invalid request data |
| `CONFLICT` | 409 | Resource already exists |
| `RATE_LIMITED` | 429 | Too many requests |
| `INTERNAL_ERROR` | 500 | Server error |

---

## Rate Limits

| Endpoint | Limit |
|----------|-------|
| Auth | 10/min |
| Programs | 60/min |
| Songs | 60/min |
| Rehearsals | 30/min |
| Live WebSocket | 100/min |

---

## Versioning

API versions are URL-based:
- `/v1/` - Current stable
- `/v2/` - Future (breaking changes)

Deprecation notice: 6 months advance warning
