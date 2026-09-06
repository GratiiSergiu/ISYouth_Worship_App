package com.example.util

object MusicTransposer {

    val chromaticEnglish = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    val chromaticRomanian = listOf("Do", "Do#", "Re", "Re#", "Mi", "Fa", "Fa#", "Sol", "Sol#", "La", "La#", "Si")

    private val flatToSharpEnglish = mapOf(
        "Db" to "C#", "Eb" to "D#", "Gb" to "F#", "Ab" to "G#", "Bb" to "A#"
    )

    private val flatToSharpRomanian = mapOf(
        "Reb" to "Do#", "Mib" to "Re#", "Solb" to "Fa#", "Lab" to "Sol#", "Sib" to "La#"
    )

    private val englishToRomanianMap = mapOf(
        "C" to "Do", "C#" to "Do#", "D" to "Re", "D#" to "Re#",
        "E" to "Mi", "F" to "Fa", "F#" to "Fa#", "G" to "Sol",
        "G#" to "Sol#", "A" to "La", "A#" to "La#", "B" to "Si"
    )

    private val romanianToEnglishMap = englishToRomanianMap.entries.associate { (k, v) -> v to k }

    fun normalizeRoot(root: String): Pair<String, Boolean> {
        // Returns Pair(canonicalRoot, isRomanian)
        var normalized = root.trim()
        flatToSharpRomanian[normalized]?.let { return Pair(it, true) }
        flatToSharpEnglish[normalized]?.let { return Pair(it, false) }

        if (chromaticRomanian.contains(normalized)) {
            return Pair(normalized, true)
        }
        if (chromaticEnglish.contains(normalized)) {
            return Pair(normalized, false)
        }
        return Pair(normalized, false)
    }

    fun convertNotation(chord: String, toRomanian: Boolean): String {
        val parts = chord.split("/")
        val main = convertSingleChordNotation(parts[0], toRomanian)
        return if (parts.size > 1) {
            val bass = convertSingleChordNotation(parts[1], toRomanian)
            "$main/$bass"
        } else {
            main
        }
    }

    private fun convertSingleChordNotation(chord: String, toRomanian: Boolean): String {
        val trimmed = chord.trim()
        if (toRomanian) {
            // Find English root
            val sortedEnglishRoots = chromaticEnglish.sortedByDescending { it.length }
            for (er in sortedEnglishRoots) {
                if (trimmed.startsWith(er)) {
                    val suffix = trimmed.removePrefix(er)
                    val roRoot = englishToRomanianMap[er] ?: er
                    return roRoot + suffix
                }
            }
        } else {
            // Find Romanian root
            val sortedRomanianRoots = chromaticRomanian.sortedByDescending { it.length }
            for (rr in sortedRomanianRoots) {
                if (trimmed.startsWith(rr)) {
                    val suffix = trimmed.removePrefix(rr)
                    val enRoot = romanianToEnglishMap[rr] ?: rr
                    return enRoot + suffix
                }
            }
        }
        return chord
    }

    fun transposeSingleChord(chord: String, semitones: Int, preferRomanian: Boolean? = null): String {
        if (semitones == 0 && preferRomanian == null) return chord
        val parts = chord.split("/")
        val transposedMain = transposeSingleRootChord(parts[0], semitones, preferRomanian)
        return if (parts.size > 1) {
            val transposedBass = transposeSingleRootChord(parts[1], semitones, preferRomanian)
            "$transposedMain/$transposedBass"
        } else {
            transposedMain
        }
    }

    private fun transposeSingleRootChord(chord: String, semitones: Int, preferRomanian: Boolean?): String {
        val trimmed = chord.trim()
        if (trimmed.isEmpty()) return chord

        // Check if Romanian root first (longer strings like "Sol#", "Do#")
        val sortedRo = (chromaticRomanian + flatToSharpRomanian.keys).sortedByDescending { it.length }
        for (ro in sortedRo) {
            if (trimmed.startsWith(ro)) {
                val suffix = trimmed.removePrefix(ro)
                val canonical = flatToSharpRomanian[ro] ?: ro
                val idx = chromaticRomanian.indexOf(canonical)
                if (idx != -1) {
                    val newIdx = (idx + semitones).mod(chromaticRomanian.size)
                    val newRoRoot = chromaticRomanian[newIdx]
                    val isRo = preferRomanian ?: true
                    return if (isRo) newRoRoot + suffix else (romanianToEnglishMap[newRoRoot] ?: newRoRoot) + suffix
                }
            }
        }

        // Check English root
        val sortedEn = (chromaticEnglish + flatToSharpEnglish.keys).sortedByDescending { it.length }
        for (en in sortedEn) {
            if (trimmed.startsWith(en)) {
                val suffix = trimmed.removePrefix(en)
                val canonical = flatToSharpEnglish[en] ?: en
                val idx = chromaticEnglish.indexOf(canonical)
                if (idx != -1) {
                    val newIdx = (idx + semitones).mod(chromaticEnglish.size)
                    val newEnRoot = chromaticEnglish[newIdx]
                    val isRo = preferRomanian ?: false
                    return if (isRo) (englishToRomanianMap[newEnRoot] ?: newEnRoot) + suffix else newEnRoot + suffix
                }
            }
        }

        return chord
    }

    fun transposeKey(key: String, semitones: Int, preferRomanian: Boolean? = null): String {
        return transposeSingleChord(key, semitones, preferRomanian)
    }

    /**
     * Transposes chords throughout a multiline text or lyrics sheet.
     * Supports:
     * - Bracketed chords like [G], [Em7], [Sol], [Do#m]
     * - Chord lines (lines composed predominantly of chords)
     */
    fun transposeTextContent(text: String, semitones: Int, preferRomanian: Boolean? = null): String {
        if (text.isBlank()) return text

        val bracketRegex = Regex("\\[([A-G][b#]?(?:m|maj7|7|sus[24]|dim|add9|aug|2|4|6|9)?(?:/[A-G][b#]?)?|(?:Do|Re|Mi|Fa|Sol|La|Si)[b#]?(?:m|maj7|7|sus[24]|dim|add9|aug|2|4|6|9)?(?:/(?:Do|Re|Mi|Fa|Sol|La|Si)[b#]?)?)\\]")

        var result = bracketRegex.replace(text) { match ->
            val chordInside = match.groupValues[1]
            val transposed = transposeSingleChord(chordInside, semitones, preferRomanian)
            "[$transposed]"
        }

        // Also transpose lines that are chord lines (e.g. "G   D   Em   C" or "Sol  Re  Mim  Do")
        val lines = result.lines()
        val processedLines = lines.map { line ->
            if (isChordLine(line)) {
                transposeChordLine(line, semitones, preferRomanian)
            } else {
                line
            }
        }

        return processedLines.joinToString("\n")
    }

    private fun isChordLine(line: String): Boolean {
        val trimmed = line.trim()
        if (trimmed.isEmpty()) return false
        val tokens = trimmed.split(Regex("\\s+"))
        if (tokens.isEmpty()) return false
        var chordCount = 0
        for (token in tokens) {
            if (isLikelyChord(token)) chordCount++
        }
        return chordCount > 0 && chordCount.toDouble() / tokens.size >= 0.5
    }

    private fun isLikelyChord(token: String): Boolean {
        val cleaned = token.trim().removeSurrounding("(", ")").removeSurrounding("[", "]")
        val chordRegex = Regex("^((?:Do|Re|Mi|Fa|Sol|La|Si|[A-G])[b#]?(?:m|maj7|7|sus[24]|dim|add9|aug|2|4|6|9)?(?:/(?:Do|Re|Mi|Fa|Sol|La|Si|[A-G])[b#]?)?)$")
        return chordRegex.matches(cleaned)
    }

    private fun transposeChordLine(line: String, semitones: Int, preferRomanian: Boolean?): String {
        val chordWordRegex = Regex("((?:Do|Re|Mi|Fa|Sol|La|Si|[A-G])[b#]?(?:m|maj7|7|sus[24]|dim|add9|aug|2|4|6|9)?(?:/(?:Do|Re|Mi|Fa|Sol|La|Si|[A-G])[b#]?)?)")
        return chordWordRegex.replace(line) { match ->
            transposeSingleChord(match.value, semitones, preferRomanian)
        }
    }

    data class TranspositionOption(
        val englishKey: String,
        val romanianKey: String,
        val combinedLabel: String,
        val semitonesDelta: Int,
        val isOriginal: Boolean,
        val isCurrent: Boolean
    )

    fun getPitchClass(chordOrKey: String): Int {
        val trimmed = chordOrKey.trim().split("/")[0]
        // Check Romanian roots
        val sortedRo = (chromaticRomanian + flatToSharpRomanian.keys).sortedByDescending { it.length }
        for (ro in sortedRo) {
            if (trimmed.startsWith(ro)) {
                val canonical = flatToSharpRomanian[ro] ?: ro
                val idx = chromaticRomanian.indexOf(canonical)
                if (idx != -1) return idx
            }
        }
        // Check English roots
        val sortedEn = (chromaticEnglish + flatToSharpEnglish.keys).sortedByDescending { it.length }
        for (en in sortedEn) {
            if (trimmed.startsWith(en)) {
                val canonical = flatToSharpEnglish[en] ?: en
                val idx = chromaticEnglish.indexOf(canonical)
                if (idx != -1) return idx
            }
        }
        return -1
    }

    fun isMinorKey(chordOrKey: String): Boolean {
        val trimmed = chordOrKey.trim()
        val pitch = getPitchClass(trimmed)
        if (pitch == -1) return false
        val rootEn = chromaticEnglish[pitch]
        val rootRo = chromaticRomanian[pitch]
        val suffix = when {
            trimmed.startsWith(rootRo) -> trimmed.removePrefix(rootRo)
            trimmed.startsWith(rootEn) -> trimmed.removePrefix(rootEn)
            else -> trimmed
        }
        return suffix.startsWith("m") && !suffix.startsWith("maj")
    }

    fun getTranspositionOptions(originalKey: String, currentKey: String): List<TranspositionOption> {
        val origClean = originalKey.trim().ifBlank { "Do" }
        val currClean = currentKey.trim().ifBlank { origClean }

        val origPitch = getPitchClass(origClean).let { if (it == -1) 0 else it }
        val currPitch = getPitchClass(currClean).let { if (it == -1) origPitch else it }
        val isMinor = isMinorKey(origClean)
        val suffix = if (isMinor) "m" else ""

        // Generate all 12 chromatic options
        return (0 until 12).map { pitchIndex ->
            val enKey = chromaticEnglish[pitchIndex] + suffix
            val roKey = chromaticRomanian[pitchIndex] + suffix
            val combined = "$roKey ($enKey)"

            // Calculate semitones delta from original
            var delta = (pitchIndex - origPitch) % 12
            if (delta > 6) delta -= 12
            if (delta < -5) delta += 12

            val isOrig = pitchIndex == origPitch
            val isCurr = pitchIndex == currPitch

            TranspositionOption(
                englishKey = enKey,
                romanianKey = roKey,
                combinedLabel = combined,
                semitonesDelta = delta,
                isOriginal = isOrig,
                isCurrent = isCurr
            )
        }
    }

    fun calculateSemitonesBetween(fromKey: String, toKey: String): Int {
        val fromPitch = getPitchClass(fromKey)
        val toPitch = getPitchClass(toKey)
        if (fromPitch == -1 || toPitch == -1) return 0
        var delta = (toPitch - fromPitch) % 12
        if (delta > 6) delta -= 12
        if (delta < -5) delta += 12
        return delta
    }
}
