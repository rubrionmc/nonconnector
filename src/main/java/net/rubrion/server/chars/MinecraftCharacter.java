package net.rubrion.server.chars;

import lombok.NonNull;
import net.rubrion.server.protocol.Version;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface MinecraftCharacter {
    Version ROOT_VERSION = Version.V0_0_0;
    int DEFAULT_PER18 = 6;

    //char c0 = '\u2009'; // this is an 18 / 7 may use them later
    //char c1 = '\u200A'; // same for this

    Map<Character, MinecraftCharacter> CACHE = new ConcurrentHashMap<>();

    @SuppressWarnings("UnnecessaryUnicodeEscape") // for clarity
    Map<Character, MinecraftCharacter> CHARACTERS = Map.ofEntries(
            entry('!',  18),                // exclamation mark '\u0021'
            entry('|',  18),                // vertical line '\u007c'
            entry('\'', 18),                // apostrophe '\u0027'
            entry('.',  18),                // dot '\u002e'
            entry(',',  18),                // comma '\u002c'
            entry(':',  18),                // colon '\u003a'
            entry(';',  18),                // semicolon '\u003b'
            entry(' ',  9),                 // space '\u0020'
            entry('"',  9),                 // quotation mark '\u0022'
            entry('　', 4, Version.V1_16_0) // ideographic space '\u3000' added in 1.16
    );

    static @NonNull MinecraftCharacter resolve(final char character) {
        return CACHE.computeIfAbsent(character, key ->
                CHARACTERS.getOrDefault(key, entry(key).getValue()));
    }

    static @NonNull String fill(final char character, final int width) {
        return fill(resolve(character), width);
    }

    static @NonNull String fill(final @NonNull MinecraftCharacter character, final int width) {
        int count = (int) Math.floor(width / character.width());
        return character.asString().repeat(Math.max(0, count));

    }

    static int len(final @Nullable String s) {
        if(s == null) return 0;
        float counter = 0;
        for (char c : s.toCharArray())
            counter += resolve(c).width();
        return Math.max(0, Math.round(counter));
    }

    @Contract("_ -> new")
    static @NonNull Map.@Unmodifiable Entry<Character, MinecraftCharacter> entry(
            final char character
    ) {
        return Map.entry(character, new Entry(character, DEFAULT_PER18, ROOT_VERSION));
    }


    @Contract("_, _ -> new")
    static @NonNull Map.@Unmodifiable Entry<Character, MinecraftCharacter> entry(
            final char character,
            final int per18
    ) {
        return Map.entry(character, new Entry(character, per18, ROOT_VERSION));
    }

    @Contract("_, _, _ -> new")
    static @NonNull Map.@Unmodifiable Entry<Character, MinecraftCharacter> entry(
            final char character,
            final int per18,
            final @NonNull Version version
    ) {
        return Map.entry(character, new Entry(character, per18, version));
    }

    char character();
    @NonNull Version version();
    int per18();

    default @NonNull String asString() {
        return String.valueOf(character());
    }

    default float width() {
        return 18f / per18();
    }

    default boolean supported(final @NonNull Version clientVersion) {
        return version().toProtocol() <= clientVersion.toProtocol();
    }

    default boolean supported(final int protocol) {
        return version().toProtocol() <= protocol;
    }

    default @NonNull String fill(final int width) {
        return MinecraftCharacter.fill(this, width);
    }

    record Entry(
            char character,
            int per18,
            @NonNull Version version
    ) implements MinecraftCharacter {

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof MinecraftCharacter other)
                return character() == other.character();
            return false;
        }
    }

}
