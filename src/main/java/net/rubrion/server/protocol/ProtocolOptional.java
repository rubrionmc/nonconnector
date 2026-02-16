package net.rubrion.server.protocol;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;

public class ProtocolOptional<T> {

    public static <T> @NonNull ProtocolOptional<T> with(final @NonNull T value) {
        return new ProtocolOptional<T>()
                .since(0, value);
    }

    private final NavigableMap<Integer, T> versionMap = new TreeMap<>();

    private ProtocolOptional() { }

    public @NonNull ProtocolOptional<T> since(final @NonNull Version version,
                                     final @NonNull T value) {
        return since(version.getProtocol(), value);
    }

    public @NonNull ProtocolOptional<T> since(final int protocol,
                                     final @NonNull T value) {
        versionMap.put(protocol, value);
        return this;
    }

    public @NonNull T resolve(final int protocol) {
        Map.Entry<Integer, T> entry = versionMap.floorEntry(protocol);
        if (entry == null) {
            throw new IllegalStateException("No mapping for protocol " + protocol);
        }
        return entry.getValue();
    }

    public @Nullable T resolveOrNull(final int protocol) {
        Map.Entry<Integer, T> entry = versionMap.floorEntry(protocol);
        return entry != null ? entry.getValue() : null;
    }

    public @NonNull T resolveOrThrow(final int protocol) {
        Map.Entry<Integer, T> entry = versionMap.floorEntry(protocol);
        if (entry == null) {
            throw new IllegalStateException(
                    "ProtocolOptional: No value registered for protocol " + protocol +
                            " (available: " + versionMap.keySet() + ")"
            );
        }
        return entry.getValue();
    }

    public <E extends Throwable> @NonNull T resolveOrThrow(final int protocol, final @NonNull Supplier<E> supplier) throws E {
        Map.Entry<Integer, T> entry = versionMap.floorEntry(protocol);
        if (entry == null) {
            throw supplier.get();
        }
        return entry.getValue();
    }


    public boolean has(final int version) {
        return versionMap.floorEntry(version) != null;
    }

}
