package com.example.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.regex.Pattern

data class ImportedSongResult(
    val title: String,
    val artist: String,
    val key: String,
    val content: String,
    val lyricsOnly: String,
    val sourceUrl: String,
    val hasChords: Boolean = true
)

object WebSongImporter {

    suspend fun fetchSongFromUrl(urlString: String): Result<ImportedSongResult> = withContext(Dispatchers.IO) {
        try {
            val trimmedUrl = urlString.trim().trim('"', '\'', ' ', '\n', '\r')
            if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
                return@withContext Result.failure(IllegalArgumentException("Link-ul trebuie să înceapă cu https:// sau http://"))
            }

            // Normalization for resursecrestine.ro:
            // Songs with chords reside under /acorduri/...
            val targetUrl = normalizeResurseCrestineUrl(trimmedUrl)
            val html = fetchHtmlWithFallback(targetUrl, trimmedUrl)

            // 1. Title, Artist, and SongContent extraction
            var extractedTitle: String? = null
            var extractedArtist: String? = null
            var extractedContent: String? = null
            var explicitKey: String? = null

            // STRATEGY 1: Check melodia.ro JSON-LD or JS variables or bracketed format
            if (html.contains("melodia.ro") || html.contains("MusicComposition") || html.contains("[key[") || html.contains("class=\"verse\"")) {
                // Check JSON-LD "lyrics": { "@type": "CreativeWork", "text": "..." }
                val jsonLdLyricsPattern = Pattern.compile(
                    "\"lyrics\"\\s*:\\s*\\{\\s*\"@type\"\\s*:\\s*\"CreativeWork\"\\s*,\\s*\"text\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"",
                    Pattern.DOTALL
                )
                val ldMatcher = jsonLdLyricsPattern.matcher(html)
                if (ldMatcher.find()) {
                    val rawLd = ldMatcher.group(1) ?: ""
                    val unescaped = unescapeJsonString(rawLd)
                    val (converted, keyFound) = convertBracketedTextToTwoLines(unescaped)
                    if (converted.isNotBlank()) {
                        extractedContent = converted
                        if (!keyFound.isNullOrBlank()) explicitKey = keyFound
                    }
                }

                // Check JS const lyrics = "..."
                if (extractedContent.isNullOrBlank()) {
                    val constLyricsPattern = Pattern.compile("const\\s+lyrics\\s*=\\s*`([^`]+)`", Pattern.DOTALL)
                    val clMatcher = constLyricsPattern.matcher(html)
                    if (clMatcher.find()) {
                        val rawConst = clMatcher.group(1) ?: ""
                        val (converted, keyFound) = convertBracketedTextToTwoLines(rawConst)
                        if (converted.isNotBlank()) {
                            extractedContent = converted
                            if (!keyFound.isNullOrBlank()) explicitKey = keyFound
                        }
                    }
                }

                // Check JSON-LD composer
                val jsonComposerPattern = Pattern.compile(
                    "\"composer\"\\s*:\\s*\\{\\s*\"@type\"\\s*:\\s*\"Person\"\\s*,\\s*\"name\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"",
                    Pattern.DOTALL
                )
                val compMatcher = jsonComposerPattern.matcher(html)
                if (compMatcher.find()) {
                    val candidate = unescapeJsonString(compMatcher.group(1) ?: "").trim()
                    if (candidate.isNotBlank()) extractedArtist = candidate
                }

                // Check JSON-LD name
                val jsonNamePattern = Pattern.compile(
                    "\"@type\"\\s*:\\s*\"MusicComposition\"\\s*,\\s*\"name\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"",
                    Pattern.DOTALL
                )
                val nameMatcher = jsonNamePattern.matcher(html)
                if (nameMatcher.find()) {
                    val candidate = unescapeJsonString(nameMatcher.group(1) ?: "").trim()
                    if (candidate.isNotBlank()) extractedTitle = candidate
                }
            }

            // STRATEGY 2: Check embedded songDetails JavaScript object (Resurse Creștine standard)
            if (extractedContent.isNullOrBlank()) {
                val songDetailsPattern = Pattern.compile(
                    "songDetails\\s*=\\s*\\{.*?songName:\\s*\"((?:\\\\.|[^\"\\\\])*)\".*?songContent:\\s*\"((?:\\\\.|[^\"\\\\])*)\"",
                    Pattern.DOTALL
                )
                val sdMatcher = songDetailsPattern.matcher(html)
                if (sdMatcher.find()) {
                    val rawName = sdMatcher.group(1) ?: ""
                    val rawContent = sdMatcher.group(2) ?: ""
                    extractedTitle = unescapeJsonString(rawName).trim()
                    val parsedContent = unescapeJsonString(rawContent)
                    if (parsedContent.isNotBlank()) {
                        extractedContent = parsedContent
                    }
                }
            }

            // STRATEGY 3: Check chords container span/div (<span class="stil-acorduri"> or id="acorduri_wrapper")
            if (extractedContent.isNullOrBlank()) {
                val chordsSpanPattern = Pattern.compile(
                    "<(?:span|div)[^>]+(?:class|id)=[\"']?(?:stil-acorduri|acorduri_wrapper|resized-text|text_wrapper)[^\"'>]*[\"']?[^>]*>(.*?)</(?:span|div)>",
                    Pattern.CASE_INSENSITIVE or Pattern.DOTALL
                )
                val spanMatcher = chordsSpanPattern.matcher(html)
                if (spanMatcher.find()) {
                    val rawSpan = spanMatcher.group(1) ?: ""
                    extractedContent = cleanHtmlChordsBlock(rawSpan)
                }
            }

            // STRATEGY 4: Check <pre> tags (Cântări Creștine, Ultimate-Guitar, WorshipTogether, E-Chords)
            if (extractedContent.isNullOrBlank()) {
                val prePattern = Pattern.compile("<pre[^>]*>(.*?)</pre>", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
                val preMatcher = prePattern.matcher(html)
                if (preMatcher.find()) {
                    val rawPre = preMatcher.group(1) ?: ""
                    extractedContent = cleanHtmlChordsBlock(rawPre)
                }
            }

            // STRATEGY 5: Check dedicated song/lyrics containers
            if (extractedContent.isNullOrBlank()) {
                val containerPattern = Pattern.compile(
                    "<div[^>]+(?:class|id)=[\"']?(?:song-description-body|song-description|lyrics|chord-pro-disp|chord-text|song-content|tab-content|viewline|songContentTPL)[^\"'>]*[\"']?[^>]*>(.*?)</div>",
                    Pattern.CASE_INSENSITIVE or Pattern.DOTALL
                )
                val cMatcher = containerPattern.matcher(html)
                if (cMatcher.find()) {
                    val rawDiv = cMatcher.group(1) ?: ""
                    extractedContent = cleanHtmlChordsBlock(rawDiv)
                }
            }

            // STRATEGY 6: Ultimate Guitar JSON store pattern
            if (extractedContent.isNullOrBlank() && html.contains("window.UGAPP.store.page")) {
                val ugWikiPattern = Pattern.compile("\"wiki_tab\"\\s*:\\s*\\{.*?\"content\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"", Pattern.DOTALL)
                val ugMatcher = ugWikiPattern.matcher(html)
                if (ugMatcher.find()) {
                    val ugRaw = ugMatcher.group(1) ?: ""
                    extractedContent = unescapeJsonString(ugRaw)
                        .replace("[ch]", "").replace("[/ch]", "")
                        .replace("[tab]", "").replace("[/tab]", "")
                }
            }

            // STRATEGY 7: Fallback to page text (stripping scripts, styles, navigation)
            if (extractedContent.isNullOrBlank()) {
                val stripped = html
                    .replace(Regex("<script[^>]*>.*?</script>", RegexOption.DOT_MATCHES_ALL), "")
                    .replace(Regex("<style[^>]*>.*?</style>", RegexOption.DOT_MATCHES_ALL), "")
                    .replace(Regex("<nav[^>]*>.*?</nav>", RegexOption.DOT_MATCHES_ALL), "")
                    .replace(Regex("<header[^>]*>.*?</header>", RegexOption.DOT_MATCHES_ALL), "")
                    .replace(Regex("<footer[^>]*>.*?</footer>", RegexOption.DOT_MATCHES_ALL), "")
                extractedContent = cleanHtmlChordsBlock(stripped)
            }

            // 2. Extract Author / Artist
            if (extractedArtist.isNullOrBlank()) {
                val authorPattern = Pattern.compile(
                    "(?:Autor(?:i)?|Artist)\\s*:\\s*(?:<[^>]+>)*\\s*<a[^>]*class=[\"']?blueLink[\"']?[^>]*>(.*?)</a>",
                    Pattern.CASE_INSENSITIVE or Pattern.DOTALL
                )
                val authorMatcher = authorPattern.matcher(html)
                if (authorMatcher.find()) {
                    extractedArtist = decodeHtmlEntities(authorMatcher.group(1) ?: "").trim()
                }
            }

            if (extractedArtist.isNullOrBlank()) {
                val metaDescPattern = Pattern.compile(
                    "<meta\\s+name=[\"']description[\"']\\s+content=[\"']([^\"',]+)(?:,[^\"']*)?\\s*-\\s*acorduri:",
                    Pattern.CASE_INSENSITIVE
                )
                val metaMatcher = metaDescPattern.matcher(html)
                if (metaMatcher.find()) {
                    val authorCandidate = decodeHtmlEntities(metaMatcher.group(1) ?: "").trim()
                    if (authorCandidate.isNotBlank() && authorCandidate.length < 60) {
                        extractedArtist = authorCandidate
                    }
                }
            }

            if (extractedArtist.isNullOrBlank()) {
                val metaAuthorPattern = Pattern.compile("<meta\\s+name=[\"']author[\"']\\s+content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
                val maMatcher = metaAuthorPattern.matcher(html)
                if (maMatcher.find()) {
                    val candidate = decodeHtmlEntities(maMatcher.group(1) ?: "").trim()
                    if (candidate.isNotBlank() && !candidate.contains("cantaricrestine", ignoreCase = true) && !candidate.contains("resursecrestine", ignoreCase = true) && !candidate.contains("melodia", ignoreCase = true)) {
                        extractedArtist = candidate
                    }
                }
            }

            // 3. Extract Title if not obtained
            if (extractedTitle.isNullOrBlank()) {
                val h1Pattern = Pattern.compile("<h1[^>]*>(.*?)</h1>", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
                val h1Matcher = h1Pattern.matcher(html)
                if (h1Matcher.find()) {
                    val rawH1 = h1Matcher.group(1) ?: ""
                    val cleanH1 = decodeHtmlEntities(rawH1.replace(Regex("<[^>]+>"), "")).trim()
                    if (cleanH1.isNotBlank()) {
                        extractedTitle = cleanH1
                    }
                }
            }

            if (extractedTitle.isNullOrBlank()) {
                val ogTitlePattern = Pattern.compile("<meta\\s+property=[\"']og:title[\"']\\s+content=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
                val ogMatcher = ogTitlePattern.matcher(html)
                if (ogMatcher.find()) {
                    extractedTitle = decodeHtmlEntities(ogMatcher.group(1) ?: "").trim()
                }
            }

            if (extractedTitle.isNullOrBlank()) {
                val titlePattern = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE or Pattern.DOTALL)
                val titleMatcher = titlePattern.matcher(html)
                if (titleMatcher.find()) {
                    extractedTitle = decodeHtmlEntities(titleMatcher.group(1) ?: "").trim()
                }
            }

            var finalTitle = extractedTitle ?: "Cântare Nouă"
            finalTitle = finalTitle
                .replace(Regex("\\s*,\\s*acorduri și versuri.*", RegexOption.IGNORE_CASE), "")
                .replace(Regex("\\s*-\\s*(?:Resurse Creștine|Cântări Creștine|Cantari Crestine|Melodia\\.ro|Melodia|Acorduri|Versuri|Chords|Tab|Tabs).*", RegexOption.IGNORE_CASE), "")
                .replace(Regex("\\s*\\|.*"), "")
                .trim()

            if (extractedArtist.isNullOrBlank() && finalTitle.contains(" - ")) {
                val parts = finalTitle.split(" - ")
                if (parts.size >= 2) {
                    finalTitle = parts[0].trim()
                    extractedArtist = parts[1].trim()
                }
            }

            var finalArtist = extractedArtist ?: "Autor necunoscut"
            if (finalArtist.isBlank()) finalArtist = "Autor necunoscut"

            // 4. Extract explicit Key or detect from chords
            if (explicitKey.isNullOrBlank()) {
                val keyRegex = Regex("(?:Gama|Tonalitate|Key|Tonality)\\s*[:=-]?\\s*([A-G][b#]?(?:m|maj|min)?|(?:Do|Re|Mi|Fa|Sol|La|Si)[b#]?(?:m|maj|min)?)", RegexOption.IGNORE_CASE)
                keyRegex.find(html)?.let { match ->
                    explicitKey = match.groupValues[1]
                }
            }

            val normalizedContent = normalizeSongSpacing(extractedContent ?: "")
            val finalKey = explicitKey ?: detectSongKey(normalizedContent)
            val lyricsOnly = extractLyricsOnly(normalizedContent)
            val hasChordsFound = hasChordsInContent(normalizedContent)

            Result.success(
                ImportedSongResult(
                    title = finalTitle,
                    artist = finalArtist,
                    key = finalKey,
                    content = normalizedContent,
                    lyricsOnly = lyricsOnly,
                    sourceUrl = trimmedUrl,
                    hasChords = hasChordsFound
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Converts inline bracketed chords like [[Bm]]Text or [-[D]]Text or <div class="chord">Bm</div>
     * into a two-line layout with chords aligned over the lyrics.
     */
    fun convertBracketedTextToTwoLines(rawContent: String): Pair<String, String?> {
        var detectedKey: String? = null

        val keyMatch = Regex("\\[key\\[([^\\]]+)\\]\\]", RegexOption.IGNORE_CASE).find(rawContent)
        if (keyMatch != null) {
            detectedKey = keyMatch.groupValues[1].trim()
        }

        val cleanInput = rawContent
            .replace(Regex("\\[key\\[[^\\]]+\\]\\]", RegexOption.IGNORE_CASE), "")
            .replace("\\\"", "\"")
            .replace("\\n", "\n")
            .replace("<br />", "\n")
            .replace("<br>", "\n")
            .replace("</br>", "\n")

        val lines = cleanInput.lines()
        val output = mutableListOf<String>()

        for (rawLine in lines) {
            var line = rawLine
                .replace(Regex("<div[^>]*class=[\"']chord[\"'][^>]*>(.*?)</div>", RegexOption.IGNORE_CASE), "[[$1]]")
                .replace(Regex("<[^>]+>"), "")
                .replace("&nbsp;", " ")
                .replace("\u00A0", " ")

            if (!line.contains("[[") && !line.contains("[-[")) {
                output.add(line.trimEnd())
                continue
            }

            val chordPattern = Regex("\\[-?\\[([^\\]]+)\\]\\]")
            val chordsWithPos = mutableListOf<Pair<Int, String>>()
            val cleanLyricBuilder = StringBuilder()
            var lastIdx = 0

            for (match in chordPattern.findAll(line)) {
                cleanLyricBuilder.append(line.substring(lastIdx, match.range.first))
                val chordName = match.groupValues[1].trim()
                chordsWithPos.add(Pair(cleanLyricBuilder.length, chordName))
                lastIdx = match.range.last + 1
            }
            cleanLyricBuilder.append(line.substring(lastIdx))

            val cleanLyric = cleanLyricBuilder.toString()

            if (chordsWithPos.isEmpty()) {
                output.add(cleanLyric.trimEnd())
                continue
            }

            val chordLineBuilder = StringBuilder()
            var curCol = 0

            for ((pos, chord) in chordsWithPos) {
                val targetPos = if (pos < curCol) curCol + 1 else pos
                if (targetPos > curCol) {
                    chordLineBuilder.append(" ".repeat(targetPos - curCol))
                }
                chordLineBuilder.append(chord)
                curCol = targetPos + chord.length
            }

            output.add(chordLineBuilder.toString().trimEnd())
            output.add(cleanLyric.trimEnd())
        }

        return Pair(output.joinToString("\n"), detectedKey)
    }

    private fun normalizeResurseCrestineUrl(url: String): String {
        val lower = url.lowercase()
        if (lower.contains("resursecrestine.ro")) {
            if (url.contains("/cantari/")) {
                return url.replace("/cantari/", "/acorduri/")
            }
            if (url.contains("/cantece/")) {
                return url.replace("/cantece/", "/acorduri/")
            }
        }
        return url
    }

    private fun fetchHtmlWithFallback(preferredUrl: String, originalUrl: String): String {
        try {
            return downloadHtml(preferredUrl)
        } catch (e: Exception) {
            if (preferredUrl != originalUrl) {
                return downloadHtml(originalUrl)
            }
            throw e
        }
    }

    private fun encodeUrlIfNeeded(urlString: String): String {
        return try {
            val uri = URI(urlString)
            uri.toASCIIString()
        } catch (_: Exception) {
            urlString.replace(" ", "%20")
        }
    }

    private fun downloadHtml(urlString: String): String {
        val encodedUrl = encodeUrlIfNeeded(urlString)
        val url = URL(encodedUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 12000
        connection.readTimeout = 12000
        connection.instanceFollowRedirects = true
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
        connection.setRequestProperty("Accept-Language", "ro-RO,ro;q=0.9,en-US;q=0.8,en;q=0.7")
        connection.setRequestProperty("Sec-Ch-Ua", "\"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"")
        connection.setRequestProperty("Sec-Ch-Ua-Mobile", "?0")
        connection.setRequestProperty("Sec-Ch-Ua-Platform", "\"Windows\"")
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1")

        val responseCode = connection.responseCode
        if (responseCode == 404) {
            throw Exception("Link-ul este incomplet sau pagina nu există (Eroare 404). Verificați dacă ați copiat adresa web completă (ex: https://melodia.ro/cantari/Ma-chemi-sa-umblu-peste-ape-Oceans).")
        } else if (responseCode == 403) {
            throw Exception("Acces restricționat de server (Eroare 403). Verificați dacă adresa este completă sau folosiți opțiunea 'Copy-Paste'.")
        } else if (responseCode !in 200..299) {
            throw Exception("Serverul a răspuns cu codul $responseCode.")
        }

        val reader = BufferedReader(InputStreamReader(connection.inputStream, Charsets.UTF_8))
        return reader.use { it.readText() }
    }

    /**
     * Cleans an HTML block containing chords and lyrics, preserving spacing and line breaks.
     */
    fun cleanHtmlChordsBlock(html: String): String {
        var text = html
            // Replace <br> and paragraph tags with newlines
            .replace(Regex("<br\\s*\\\\?/?>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("<p[^>]*>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("</p>", RegexOption.IGNORE_CASE), "\n")
            // Preserve chord links like <a class="nice-acord" rel="C">C</a>
            .replace(Regex("<a[^>]*rel=[\"']([^\"']+)[\"'][^>]*>.*?</a>", RegexOption.IGNORE_CASE), "$1")
            .replace(Regex("<a[^>]*>(.*?)</a>", RegexOption.IGNORE_CASE), "$1")
            // Strip remaining HTML tags
            .replace(Regex("<[^>]+>"), "")
            // Decode non-breaking spaces and common HTML entities
            .replace("&nbsp;", " ")
            .replace("\u00A0", " ")

        return decodeHtmlEntities(text)
    }

    /**
     * Normalizes line breaks and whitespace in song lyrics/chords:
     * - Preserves leading whitespace for chord alignment over syllables
     * - Preserves single blank line between stanzas and refrains
     * - Eliminates excessive 3+ consecutive empty lines
     */
    fun normalizeSongSpacing(content: String): String {
        val lines = content.replace("\r\r\n", "\n\n").replace("\r\n", "\n").replace("\r", "\n").lines()
        val result = mutableListOf<String>()
        var consecutiveEmpty = 0

        for (rawLine in lines) {
            val trimmed = rawLine.trim()
            if (trimmed.isEmpty()) {
                consecutiveEmpty++
                if (consecutiveEmpty == 1 && result.isNotEmpty()) {
                    result.add("") // preserve 1 blank line between stanzas/refrains
                }
            } else {
                consecutiveEmpty = 0
                // Preserve leading spaces and tabs; trim trailing whitespace
                result.add(rawLine.trimEnd())
            }
        }

        return result.joinToString("\n").trim()
    }

    /**
     * Extracts lyrics only without chord lines for presentation / singer view,
     * maintaining stanzas, verse numbers, refrains, and blank line separation.
     */
    fun extractLyricsOnly(contentWithChords: String): String {
        val lines = contentWithChords.lines()
        val lyricLines = mutableListOf<String>()
        var consecutiveEmpty = 0

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) {
                consecutiveEmpty++
                if (consecutiveEmpty == 1 && lyricLines.isNotEmpty()) {
                    lyricLines.add("")
                }
                continue
            }

            consecutiveEmpty = 0
            // Check if line is purely chords
            if (isChordOnlyLine(line)) {
                continue
            }

            // Remove any inline bracketed chords like [C] or [Sol]
            val cleanedLine = line
                .replace(Regex("\\[[A-Ga-g][#b]?(?:m|maj7|7|sus[24]|dim|add9|aug|2|4|6|9)?(?:/[A-Ga-g][#b]?)?\\]"), "")
                .replace(Regex("\\[(?:Do|Re|Mi|Fa|Sol|La|Si)[#b]?(?:m|maj7|7|sus[24]|dim|add9|aug|2|4|6|9)?(?:/(?:Do|Re|Mi|Fa|Sol|La|Si)[#b]?(?:m|maj7|7|sus[24]|dim|add9|aug|2|4|6|9)?)?\\]"), "")
                .trimEnd()

            if (cleanedLine.trim().isNotEmpty()) {
                lyricLines.add(cleanedLine)
            }
        }

        return lyricLines.joinToString("\n").trim()
    }

    fun hasChordsInContent(content: String): Boolean {
        return content.lines().any { isChordOnlyLine(it) || containsBracketedChords(it) }
    }

    private fun containsBracketedChords(line: String): Boolean {
        val bracketRegex = Regex("\\[(?:Do|Re|Mi|Fa|Sol|La|Si|[A-G])[b#]?(?:m|maj7|7|sus[24]|dim|add9|aug)?\\]")
        return bracketRegex.containsMatchIn(line)
    }

    /**
     * Determines whether a line contains only chord tokens and spacing.
     */
    fun isChordOnlyLine(line: String): Boolean {
        val trimmed = line.trim()
        if (trimmed.isEmpty()) return false

        val tokens = trimmed.split(Regex("\\s+"))
        if (tokens.isEmpty()) return false

        var chordCount = 0
        var nonChordCount = 0

        for (token in tokens) {
            val cleaned = token.removeSurrounding("(", ")").removeSurrounding("[", "]").removeSurrounding(":", "").trim()
            if (cleaned.isEmpty()) continue
            if (isChordToken(cleaned)) {
                chordCount++
            } else {
                nonChordCount++
            }
        }

        return chordCount > 0 && nonChordCount == 0
    }

    /**
     * Checks if a word is a valid musical chord (e.g. C, Am, F#m, Sol, Mim, Do#).
     */
    fun isChordToken(token: String): Boolean {
        val chordRegex = Regex("^((?:Do|Re|Mi|Fa|Sol|La|Si|[A-G])[b#]?(?:m|maj7|7|sus[24]|dim|add9|aug|2|4|6|9)?(?:/(?:Do|Re|Mi|Fa|Sol|La|Si|[A-G])[b#]?)?)$")
        return chordRegex.matches(token)
    }

    /**
     * Detects the tonic key of a song from its chord progressions.
     * Maps to standard church music keys (e.g. Do, Re, Mi, Fa, Sol, La, Si, or Mim, Lam).
     */
    fun detectSongKey(content: String): String {
        for (line in content.lines()) {
            val tokens = line.trim().split(Regex("\\s+"))
            for (token in tokens) {
                val cleaned = token.removeSurrounding("(", ")").removeSurrounding("[", "]").removeSurrounding(":", "").trim()
                if (isChordToken(cleaned)) {
                    // Extract root
                    val root = cleaned.split("/")[0]
                    return mapChordToKeyLabel(root)
                }
            }
        }
        return "Sol"
    }

    private fun mapChordToKeyLabel(chord: String): String {
        val roMap = mapOf(
            "C" to "Do", "Cm" to "Dom",
            "D" to "Re", "Dm" to "Rem",
            "E" to "Mi", "Em" to "Mim",
            "F" to "Fa", "Fm" to "Fam",
            "F#" to "Fa#", "F#m" to "Fa#m",
            "G" to "Sol", "Gm" to "Solm",
            "A" to "La", "Am" to "Lam",
            "B" to "Si", "Bm" to "Sim", "Bb" to "Sib", "Bbm" to "Sibm",
            "Db" to "Reb", "Eb" to "Mib", "Ab" to "Lab"
        )
        return roMap[chord] ?: chord
    }

    /**
     * Decodes standard and Romanian HTML entities.
     */
    fun decodeHtmlEntities(text: String): String {
        var result = text
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&#039;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&ndash;", "–")
            .replace("&mdash;", "—")
            .replace("&hellip;", "...")
            .replace("&icirc;", "î")
            .replace("&Icirc;", "Î")
            .replace("&acirc;", "â")
            .replace("&Acirc;", "Â")
            .replace("&abreve;", "ă")
            .replace("&Abreve;", "Ă")
            .replace("&#537;", "ș")
            .replace("&#536;", "Ș")
            .replace("&#539;", "ț")
            .replace("&#538;", "Ț")
            .replace("&#351;", "ş")
            .replace("&#350;", "Ş")
            .replace("&#355;", "ţ")
            .replace("&#354;", "Ţ")

        val numRegex = Regex("&#(\\d+);")
        result = numRegex.replace(result) { match ->
            try {
                match.groupValues[1].toInt().toChar().toString()
            } catch (_: Exception) { match.value }
        }

        val hexRegex = Regex("&#x([0-9a-fA-F]+);")
        result = hexRegex.replace(result) { match ->
            try {
                match.groupValues[1].toInt(16).toChar().toString()
            } catch (_: Exception) { match.value }
        }

        return result
    }

    /**
     * Unescapes a JSON string literal.
     */
    fun unescapeJsonString(input: String): String {
        val sb = StringBuilder()
        var i = 0
        val len = input.length
        while (i < len) {
            val c = input[i]
            if (c == '\\' && i + 1 < len) {
                when (val next = input[i + 1]) {
                    'u' -> {
                        if (i + 5 < len) {
                            val hex = input.substring(i + 2, i + 6)
                            try {
                                sb.append(hex.toInt(16).toChar())
                                i += 6
                                continue
                            } catch (_: Exception) {
                            }
                        }
                        sb.append("\\u")
                        i += 2
                    }
                    'n' -> { sb.append('\n'); i += 2 }
                    'r' -> { sb.append('\r'); i += 2 }
                    't' -> { sb.append('\t'); i += 2 }
                    'b' -> { sb.append('\b'); i += 2 }
                    'f' -> { sb.append('\u000C'); i += 2 }
                    '"' -> { sb.append('"'); i += 2 }
                    '\\' -> { sb.append('\\'); i += 2 }
                    '/' -> { sb.append('/'); i += 2 }
                    else -> { sb.append(next); i += 2 }
                }
            } else {
                sb.append(c)
                i++
            }
        }
        return sb.toString()
    }
}
