package com.example.data

import com.example.model.*

object SampleData {
    val songs = listOf(
        Song(
            id = "song_1",
            title = "Oceans (Where Feet May Fail)",
            artist = "Hillsong United",
            key = "Do",
            durationSeconds = 272,
            tempo = 65,
            timeSignature = "4/4",
            status = "repertoire",
            usageCount = 18,
            lead = "Andrei",
            guitar = "Maria",
            drums = "Alex",
            lastUsed = "25 Mai 2025",
            isFavorite = true,
            lyrics = """You call me out upon the waters
The great unknown where feet may fail
And there I find You in the mystery
In oceans deep my faith will stand

And I will call upon Your name
And keep my eyes above the waves
When oceans rise
My soul will rest in Your embrace
For I am Yours and You are mine

Spirit lead me where my trust is without borders
Let me walk upon the waters
Wherever You would call me
Take me deeper than my feet could ever wander
And my faith will be made stronger
In the presence of my Savior""",
            chords = """[Verse 1]
Do              Sol
You call me out upon the waters
La m                  Fa
The great unknown where feet may fail
Do                   Sol
And there I find You in the mystery
La m              Fa
In oceans deep my faith will stand

[Chorus]
Do
And I will call upon Your name
Sol
And keep my eyes above the waves
La m
When oceans rise
Fa
My soul will rest in Your embrace
Do             Sol        La m   Fa
For I am Yours and You are mine

[Bridge]
La m                    Fa
Spirit lead me where my trust is without borders
Do                      Sol
Let me walk upon the waters wherever You would call me
La m                    Fa
Take me deeper than my feet could ever wander
Do                      Sol
And my faith will be made stronger in the presence of my Savior"""
        ),
        Song(
            id = "song_2",
            title = "Way Maker",
            artist = "Sinach",
            key = "Sol",
            durationSeconds = 345,
            tempo = 72,
            timeSignature = "4/4",
            status = "repertoire",
            usageCount = 16,
            lead = "Maria",
            guitar = "Andrei",
            drums = "Alex",
            lastUsed = "25 Mai 2025",
            isFavorite = true,
            lyrics = """You are here, moving in our midst
I worship You, I worship You
You are here, working in this place
I worship You, I worship You

You are Way Maker, Miracle Worker
Promise Keeper, Light in the darkness
My God, that is who You are

Even when I don't see it, You're working
Even when I don't feel it, You're working
You never stop, You never stop working""",
            chords = """[Verse]
Sol              Re
You are here, moving in our midst
Do               La m
I worship You, I worship You
Sol              Re
You are here, working in this place
Do               La m
I worship You, I worship You

[Chorus]
Sol
You are Way Maker, Miracle Worker
Re
Promise Keeper, Light in the darkness
Do               La m
My God, that is who You are"""
        ),
        Song(
            id = "song_3",
            title = "Graves Into Gardens",
            artist = "Elevation Worship",
            key = "La m",
            durationSeconds = 258,
            tempo = 68,
            timeSignature = "6/8",
            status = "repertoire",
            usageCount = 12,
            lead = "Andrei",
            guitar = "Cristina",
            drums = "Alex",
            lastUsed = "25 Mai 2025",
            isFavorite = false,
            lyrics = """I searched the world but it couldn't fill me
A man's empty praise and treasures that fade
Are never enough
Then You came along and You put me back together
And every desire is now satisfied here in Your love

Oh there's nothing better than You
There's nothing better than You
Lord there's nothing, nothing is better than You

You turn mourning to dancing
You give beauty for ashes
You turn shame into glory
You're the only one who can""",
            chords = """[Verse]
La m             Fa
I searched the world but it couldn't fill me
Do               Sol
A man's empty praise and treasures that fade
La m             Fa
Are never enough

[Chorus]
Do
Oh there's nothing better than You
Fa
There's nothing better than You
La m               Sol            Fa
Lord there's nothing, nothing is better than You"""
        ),
        Song(
            id = "song_4",
            title = "What A Beautiful Name",
            artist = "Hillsong Worship",
            key = "Re",
            durationSeconds = 320,
            tempo = 68,
            timeSignature = "4/4",
            status = "repertoire",
            usageCount = 15,
            lead = "Elena",
            guitar = "Andrei",
            drums = "Alex",
            lastUsed = "25 Mai 2025",
            isFavorite = true,
            lyrics = """You were the Word at the beginning
One with God the Lord Most High
Your hidden glory in creation
Now revealed in You our Christ

What a beautiful Name it is
What a beautiful Name it is
The Name of Jesus Christ my King
What a beautiful Name it is
Nothing compares to this
What a beautiful Name it is
The Name of Jesus""",
            chords = """[Verse]
Re
You were the Word at the beginning
Sol           Si m      La
One with God the Lord Most High
Re
Your hidden glory in creation
Sol           Si m      La
Now revealed in You our Christ

[Chorus]
Re                         La
What a beautiful Name it is, what a beautiful Name it is
Si m         La          Sol
The Name of Jesus Christ my King
Re/Fa#                     La
What a beautiful Name it is, nothing compares to this
Si m         La          Sol
What a beautiful Name it is, the Name of Jesus"""
        ),
        Song(
            id = "song_5",
            title = "Goodness of God",
            artist = "Bethel Music",
            key = "Do",
            durationSeconds = 292,
            tempo = 70,
            timeSignature = "4/4",
            status = "repertoire",
            usageCount = 14,
            lead = "Andrei",
            guitar = "Maria",
            drums = "Alex",
            lastUsed = "25 Mai 2025",
            isFavorite = true,
            lyrics = """I love You Lord
For Your mercy never fails me
All my days I've been held in Your hands
From the moment that I wake up
Until I lay my head
Oh I will sing of the goodness of God

All my life You have been faithful
All my life You have been so so good
With every breath that I am able
Oh I will sing of the goodness of God""",
            chords = """[Verse 1]
Do                  Fa       Do
I love You Lord, for Your mercy never fails me
Sol/Si   La m           Fa           Sol
All my days I've been held in Your hands
         La m             Fa
From the moment that I wake up
        Do      Sol/Si    La m
Until I lay my head
Fa          Sol         Do
Oh I will sing of the goodness of God

[Chorus]
Fa                          Do
All my life You have been faithful
Fa                          Do       Sol
All my life You have been so so good
Fa                          Do   Sol/Si  La m
With every breath that I am able
Fa          Sol         Do
Oh I will sing of the goodness of God"""
        ),
        Song(
            id = "song_6",
            title = "Build My Life",
            artist = "Pat Barrett",
            key = "Mi",
            durationSeconds = 255,
            tempo = 74,
            timeSignature = "4/4",
            status = "learning",
            usageCount = 2,
            lead = "Andrei",
            guitar = "Maria",
            drums = "Alex",
            lastUsed = "20 Mai 2025",
            isFavorite = false,
            lyrics = """Worthy of every song we could ever sing
Worthy of all the praise we could ever bring
Worthy of every breath we could ever breathe
We live for You

Holy, there is no one like You
There is none beside You
Open up my eyes in wonder
And show me who You are
And fill me with Your heart
And lead me in Your love to those around me""",
            chords = """[Verse]
Mi          La
Worthy of every song we could ever sing
Mi/Sol#     La
Worthy of all the praise we could ever bring
Mi          La
Worthy of every breath we could ever breathe
Mi/Sol#     La
We live for You"""
        ),
        Song(
            id = "song_7",
            title = "Firm Foundation",
            artist = "Cody Carnes",
            key = "Fa",
            durationSeconds = 228,
            tempo = 72,
            timeSignature = "4/4",
            status = "new",
            usageCount = 0,
            lead = "Maria",
            guitar = "Andrei",
            drums = "Alex",
            lastUsed = "-",
            isFavorite = false,
            lyrics = """Christ is my firm foundation
The rock on which I stand
When everything around me is shaking
I've never been more glad
That I put my faith in Jesus
'Cause He's never let me down
He's faithful in every season
So why would He fail now?

He won't, He won't
He won't fail, He won't fail""",
            chords = """[Chorus]
Fa
Christ is my firm foundation
Sib/Fa            Fa
The rock on which I stand
Fa
When everything around me is shaking
Sib/Fa            Fa
I've never been more glad"""
        )
    )

    val programs = listOf(
        Program(
            id = "prog_1",
            title = "Identitate",
            theme = "Cine suntem în Hristos",
            date = "25 Mai 2025",
            status = "ready",
            tags = listOf("#tineret", "#adorare"),
            targetDurationMinutes = 60,
            notes = "Tranziție directă între Oceans și Way Maker. Moment de rugăciune după cântarea 3.",
            setlist = listOf(
                SetlistItem(id = "si_1", songId = "song_1", segment = "worship", notes = "Tranziție pian spre Do"),
                SetlistItem(id = "si_2", songId = "song_2", segment = "worship", notes = "Modulație Sol"),
                SetlistItem(id = "si_3", songId = "song_3", segment = "worship", notes = "Tempo crescător 68 BPM"),
                SetlistItem(id = "si_4", songId = "song_4", segment = "special", notes = "Acapella la refren"),
                SetlistItem(id = "si_5", songId = "song_5", segment = "sending", notes = "Toată sala în picioare")
            )
        ),
        Program(
            id = "prog_2",
            title = "Credință",
            theme = "Tineret ISYouth",
            date = "18 Mai 2025",
            status = "completed",
            tags = listOf("#tineret", "#mărturie"),
            targetDurationMinutes = 60,
            notes = "Serviciu de mărturii.",
            setlist = listOf(
                SetlistItem(id = "si_6", songId = "song_2", segment = "worship"),
                SetlistItem(id = "si_7", songId = "song_4", segment = "worship"),
                SetlistItem(id = "si_8", songId = "song_5", segment = "worship"),
                SetlistItem(id = "si_9", songId = "song_1", segment = "special"),
                SetlistItem(id = "si_10", songId = "song_3", segment = "sending"),
                SetlistItem(id = "si_11", songId = "song_6", segment = "sending")
            )
        ),
        Program(
            id = "prog_3",
            title = "Har",
            theme = "Serviciu Special",
            date = "1 Iun 2025",
            status = "draft",
            tags = listOf("#botez", "#adorare"),
            targetDurationMinutes = 45,
            notes = "Program de botez tineret.",
            setlist = listOf(
                SetlistItem(id = "si_12", songId = "song_1", segment = "worship"),
                SetlistItem(id = "si_13", songId = "song_7", segment = "worship"),
                SetlistItem(id = "si_14", songId = "song_5", segment = "sending")
            )
        ),
        Program(
            id = "prog_4",
            title = "Dragoste",
            theme = "Tineret ISYouth",
            date = "8 Iun 2025",
            status = "draft",
            tags = listOf("#tineret", "#adorare"),
            targetDurationMinutes = 50,
            notes = "Focus pe unitate.",
            setlist = listOf(
                SetlistItem(id = "si_15", songId = "song_2", segment = "worship"),
                SetlistItem(id = "si_16", songId = "song_3", segment = "worship"),
                SetlistItem(id = "si_17", songId = "song_4", segment = "special"),
                SetlistItem(id = "si_18", songId = "song_6", segment = "sending")
            )
        )
    )

    val rehearsals = listOf(
        Rehearsal(
            id = "reh_1",
            programName = "Identitate",
            date = "23 Mai 2025",
            time = "19:00",
            location = "Sala Tineret",
            duration = 90,
            status = "scheduled",
            attendees = listOf(
                Attendee(name = "Andrei Popescu", status = "confirmed", avatar = "A"),
                Attendee(name = "Maria Ionescu", status = "confirmed", avatar = "M"),
                Attendee(name = "Alex Dumitru", status = "maybe", avatar = "A"),
                Attendee(name = "Cristina Marin", status = "declined", avatar = "C"),
                Attendee(name = "David Stan", status = "no-response", avatar = "D"),
                Attendee(name = "Elena Rusu", status = "confirmed", avatar = "E")
            ),
            agenda = listOf(
                AgendaItem(song = "Oceans", focus = "Tranziții pian și pod", duration = 20),
                AgendaItem(song = "Way Maker", focus = "Dinamica strofei și modulație", duration = 15),
                AgendaItem(song = "Graves Into Gardens", focus = "Armonii vocale refren", duration = 15),
                AgendaItem(song = "What A Beautiful Name", focus = "Acapella bridge", duration = 15)
            ),
            hasRecording = false,
            recordingUrl = ""
        ),
        Rehearsal(
            id = "reh_2",
            programName = "Credință",
            date = "16 Mai 2025",
            time = "19:00",
            location = "Sala Tineret",
            duration = 90,
            status = "completed",
            attendees = listOf(
                Attendee(name = "Andrei Popescu", status = "confirmed", avatar = "A"),
                Attendee(name = "Maria Ionescu", status = "confirmed", avatar = "M"),
                Attendee(name = "Alex Dumitru", status = "confirmed", avatar = "A"),
                Attendee(name = "Cristina Marin", status = "confirmed", avatar = "C"),
                Attendee(name = "David Stan", status = "confirmed", avatar = "D"),
                Attendee(name = "Elena Rusu", status = "confirmed", avatar = "E")
            ),
            agenda = listOf(
                AgendaItem(song = "Way Maker", focus = "Repetiție completă", duration = 25),
                AgendaItem(song = "Goodness of God", focus = "Armonii refren", duration = 20)
            ),
            hasRecording = true,
            recordingUrl = "audio_reh_credinta.mp3"
        )
    )

    val teamMembers = listOf(
        TeamMember("tm_1", "Andrei Popescu", "Lider Worship • Chitară", "A", "Admin", 0xFFE85D5D, 0xFF4A3B6B),
        TeamMember("tm_2", "Maria Ionescu", "Voce • Pian", "M", "Membru", 0xFF4A3B6B, 0xFF6B5B95),
        TeamMember("tm_3", "Alex Dumitru", "Tobe • Percuție", "A", "Membru", 0xFF10B981, 0xFF059669),
        TeamMember("tm_4", "Cristina Marin", "Voce • Chitară bass", "C", "Membru", 0xFFF59E0B, 0xFFD97706),
        TeamMember("tm_5", "David Stan", "Tehnică sunet • Proiecții", "D", "Tehnic", 0xFF3B82F6, 0xFF1D4ED8),
        TeamMember("tm_6", "Elena Rusu", "Voce • Backing vocals", "E", "Membru", 0xFF8B5CF6, 0xFF6D28D9)
    )

    val notifications = listOf(
        NotificationItem("n1", "program_updated", "Program actualizat", "\"Identitate\" a fost modificat. O nouă cântare a fost adăugată.", "Acum 5 min", false, "📝", 0xFFE85D5D, "builder"),
        NotificationItem("n2", "rehearsal_scheduled", "Repetiție programată", "Repetiție pentru \"Identitate\" pe 23 Mai la 19:00 în Sala Tineret.", "Acum 2 ore", false, "🎸", 0xFF4A3B6B, "practice"),
        NotificationItem("n3", "team_invite", "Membru nou", "Ioana Popescu s-a alăturat echipei ISYouth Worship ca vocalist.", "Acum 5 ore", true, "👤", 0xFF10B981, "team"),
        NotificationItem("n4", "song_added", "Cântare nouă adăugată", "\"Firm Foundation\" (Cody Carnes) a fost adăugată în repertoriu.", "Ieri", true, "🎵", 0xFFF59E0B, "songs"),
        NotificationItem("n5", "reminder", "Reminder program", "Programul \"Identitate\" este duminică la 10:00. Verifică setlist-ul!", "Ieri", true, "⏰", 0xFFE85D5D, "builder")
    )

    val songStats = listOf(
        SongStat(
            title = "Oceans (Where Feet May Fail)",
            artist = "Hillsong United",
            key = "Do",
            playCount = 18,
            lastPlayed = "25 Mai 2025",
            firstPlayed = "15 Ian 2024",
            trend = "up",
            programs = listOf(
                SongStatProgram("25 Mai 2025", "Identitate", 1),
                SongStatProgram("18 Mai 2025", "Credință", 4),
                SongStatProgram("11 Mai 2025", "Speranță", 1),
                SongStatProgram("4 Mai 2025", "Harul", 3)
            )
        ),
        SongStat(
            title = "Way Maker",
            artist = "Sinach",
            key = "Sol",
            playCount = 16,
            lastPlayed = "25 Mai 2025",
            firstPlayed = "20 Feb 2024",
            trend = "stable",
            programs = listOf(
                SongStatProgram("25 Mai 2025", "Identitate", 2),
                SongStatProgram("18 Mai 2025", "Credință", 1),
                SongStatProgram("11 Mai 2025", "Speranță", 2)
            )
        ),
        SongStat(
            title = "What A Beautiful Name",
            artist = "Hillsong Worship",
            key = "Re",
            playCount = 15,
            lastPlayed = "25 Mai 2025",
            firstPlayed = "1 Feb 2024",
            trend = "up",
            programs = listOf(
                SongStatProgram("25 Mai 2025", "Identitate", 4),
                SongStatProgram("18 Mai 2025", "Credință", 2)
            )
        ),
        SongStat(
            title = "Goodness of God",
            artist = "Bethel Music",
            key = "Do",
            playCount = 14,
            lastPlayed = "25 Mai 2025",
            firstPlayed = "10 Mar 2024",
            trend = "up",
            programs = listOf(
                SongStatProgram("25 Mai 2025", "Identitate", 5),
                SongStatProgram("18 Mai 2025", "Credință", 3)
            )
        ),
        SongStat(
            title = "Graves Into Gardens",
            artist = "Elevation Worship",
            key = "La m",
            playCount = 12,
            lastPlayed = "25 Mai 2025",
            firstPlayed = "5 Apr 2024",
            trend = "stable",
            programs = listOf(
                SongStatProgram("25 Mai 2025", "Identitate", 3),
                SongStatProgram("18 Mai 2025", "Credință", 5)
            )
        ),
        SongStat(
            title = "Build My Life",
            artist = "Pat Barrett",
            key = "Mi",
            playCount = 2,
            lastPlayed = "20 Mai 2025",
            firstPlayed = "15 Mai 2025",
            trend = "down",
            programs = listOf(
                SongStatProgram("18 Mai 2025", "Credință", 6)
            )
        ),
        SongStat(
            title = "Firm Foundation",
            artist = "Cody Carnes",
            key = "Fa",
            playCount = 0,
            lastPlayed = "-",
            firstPlayed = "-",
            trend = "stable",
            programs = emptyList()
        )
    )
}
