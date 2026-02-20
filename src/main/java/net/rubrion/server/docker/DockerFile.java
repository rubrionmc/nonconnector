package net.rubrion.server.docker;

import lombok.NonNull;

import java.io.File;

public final class DockerFile {
    private static final boolean isInDocker = !(new File(".server").isDirectory());

    public static @NonNull File fileFromServerDir(final @NonNull String pathname) {
        return new File(isInDocker ? pathname : ".server/" + pathname);
    }

    public static @NonNull String stringFromServerDir(final @NonNull String pathname) {
        return isInDocker ? pathname : ".server/" + pathname;
    }

}
