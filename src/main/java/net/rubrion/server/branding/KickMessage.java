package net.rubrion.server.branding;

import de.leycm.linguae.Label;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.rubrion.server.protocol.ProtocolOptional;
import net.rubrion.server.protocol.Version;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public interface KickMessage {

    @Contract(" -> new")
    static @NonNull Builder builder() {
        return new Builder();
    }

    class Builder {
        protected Type type;
        protected Label description;
        protected Label reason;
        protected Component head; // this does not render on old versions so we don't need a ProtocolOptional here
        protected String author;

        protected Builder() {
            this.type = Type.DISCONNECT;
            this.description = Label.translatable("rubrion.kick.error.disconnect.description");
            this.reason = Label.translatable("rubrion.kick.error.disconnect.reason");
            this.author = "Server";
            this.head = Component.empty();
        }

        public @NonNull Builder type(final @NonNull Type type) {
            this.type = type;
            return this;
        }

        public @NonNull Builder description(final @NonNull Label description) {
            this.description = description;
            return this;
        }

        public @NonNull Builder reason(final @NonNull Label reason) {
            this.reason = reason;
            return this;
        }

        // not sure if this is the best way to do this
        public @NonNull Builder head(final @Nullable Component head) {
            this.head = head != null ? head : Component.empty();
            return this;
        }

        public @NonNull Builder author(final @NonNull String author) {
            this.author = author;
            return this;
        }

        public @NonNull TextComponent build(final int protocol, final @NonNull Locale locale) {
            Component lineComponent = ProtocolOptional.with((Component) Component.text(Label.translatable("rubrion.kick.error.line").asComponent(locale).content(), NamedTextColor.GOLD))
                    .since(Version.V1_16_0, Label.translatable("rubrion.kick.error.line").asComponent(locale)).resolve(protocol);

            TextComponent splitComponent = Component.text(" * ", NamedTextColor.WHITE);

            Component stepComponent = getStepComponent();

            return Component.empty().append(
                    lineComponent,
                    Component.newline(),
                    stepComponent,
                    Component.newline(),
                    lineComponent,
                    Component.newline(),
                    Component.text(description.in(locale), NamedTextColor.GRAY),
                    Component.newline(),
                    Component.text(reason.in(locale), NamedTextColor.GRAY),
                    Component.newline(),
                    lineComponent,
                    Component.newline(),
                    Component.text("©Rubrion", NamedTextColor.GRAY),
                    splitComponent,
                    Component.text(type.title.in(locale) + "#" + type.code, NamedTextColor.GRAY),
                    splitComponent,
                    head,
                    Component.text(author, NamedTextColor.GRAY)
            );
        }

        private @org.jspecify.annotations.NonNull Component getStepComponent() {
            Component stepComponent = Component.empty();

            for (int i = 0; i < type.steps.size(); i++) {
                if (i > 0) stepComponent = stepComponent.append(Component
                        .text(" -> ", NamedTextColor.DARK_GRAY));

                String s = type.steps.get(i);

                if (i < type.failIndex) {
                    stepComponent = stepComponent.append(Component.text(s, NamedTextColor.WHITE));
                } else if (i == type.failIndex) {
                    stepComponent = stepComponent.append(Component.text(s, NamedTextColor.RED));
                } else {
                    stepComponent = stepComponent.append(Component.text(s, NamedTextColor.GRAY));
                }
            }
            return stepComponent;
        }
    }

    record Type(
            @NonNull List<String> steps,
            @NonNull Label title,
            int failIndex,
            int code
    ) {
        public static Type DISCONNECT = new Type(List.of("Client", "Connection", "Server"), Label.translatable("rubrion.kick.error.disconnect"), 1, 400);
    }

}
