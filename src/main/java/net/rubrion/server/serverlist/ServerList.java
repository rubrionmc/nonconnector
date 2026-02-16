package net.rubrion.server.serverlist;

import lombok.NonNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;

import net.minestom.server.ping.Status;
import net.minestom.server.utils.identity.NamedAndIdentified;

import net.rubrion.server.branding.Brands;
import net.rubrion.server.chars.MinecraftCharacter;
import net.rubrion.server.protocol.ProtocolOptional;
import net.rubrion.server.protocol.Version;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface ServerList {

    // todo: move messages to labels and may make them translatable
    Response NO_CONNECTION = response()
            .playerInfo(new PlayerInfo(0, 0))
            .toolTipInfo(List.of("§fNo Connection Found!",
                    "§7Fail to connect you to",
                    "§7a healthy §fRubrion §7server."))
            .stateColor(ProtocolOptional.with(new StateColor(NamedTextColor.DARK_RED))
                    .since(Version.V1_16_0, new StateColor(TextColor.color(0xFF0000),
                            ShadowColor.shadowColor(0xFF820000)))) // 1.16+ supports true type colors and shadows
            .versionInfo(ProtocolOptional.with(new VersionInfo("§70§8/§40§r", -1)))
            .messages(List.of("Houston, we have a problem.",
                    "No connection detected, even the aliens gave up.",
                    "Our hamsters stopped running the server wheels.",
                    "Signal lost somewhere between Earth and Mars.",
                    "Proxy tried to phone home... no answer.",
                    "We asked NASA for help, they left us on read.",
                    "Connection failed harder than Pluto's planet status.",
                    "The server went to buy milk and never came back.",
                    "Gravity too strong, packets can't escape.",
                    "Black hole detected in network layer.",
                    "Our satellites are on coffee break.",
                    "The proxy slipped on a banana peel.",
                    "Quantum tunneling failed. Try again later.",
                    "The server is currently abducted by aliens.",
                    "We lost the server in hyperspace.",
                    "Even Elon Musk can't reach this server.",
                    "Server is on vacation on the Moon.",
                    "The connection went faster than light... and got lost.",
                    "Packets stuck in an asteroid field.",
                    "The proxy forgot the server's address.",
                    "Server not found, try yelling into space.",
                    "Our router fell into a wormhole.",
                    "The internet cable was eaten by a space cow.",
                    "Proxy.exe has stopped believing in itself.",
                    "Connection failed successfully.",
                    "We tried sending pigeons, even they got lost.",
                    "Server is hiding behind Jupiter.",
                    "The packets achieved escape velocity.",
                    "Server unreachable, please sacrifice a rubber duck.",
                    "Proxy ran out of fuel.",
                    "The server joined the dark side.",
                    "We asked the stars, they didn't answer.",
                    "Server currently in stealth mode.",
                    "Connection lost in the multiverse.",
                    "Our proxy is socially distancing from servers.",
                    "Packets are stuck in low orbit.",
                    "Server is AFK in another galaxy.",
                    "Even WiFi from Mars is faster than this.",
                    "The proxy forgot how networking works.",
                    "Server is playing hide and seek.",
                    "Connection blocked by cosmic radiation.",
                    "Server lost in deep space.",
                    "Proxy had an existential crisis.",
                    "Packets went the wrong direction in spacetime.",
                    "Server unreachable, try summoning it.",
                    "The proxy tried turning it off and on again.",
                    "Connection failed, blame the universe.",
                    "Server is currently loading... forever.",
                    "Network error: reality.exe stopped responding.",
                    "Server got stuck in warp speed.",
                    "Proxy lost the server in the void.",
                    "Server unreachable, vibes are bad.",
                    "Packets entered the shadow realm.",
                    "Proxy is confused but trying its best.",
                    "Server is on strike for better ping.",
                    "Connection lost in translation.",
                    "Proxy can't find the space coordinates.",
                    "Server unreachable, try again in another timeline.",
                    "Network error: cosmic interference.",
                    "Server is currently in another dimension.",
                    "Proxy went full potato mode.",
                    "Connection failed, space-time anomaly detected.",
                    "Server not responding, probably building a Death Star.",
                    "Proxy tried to connect... server said 'nah'.",
                    "Packets got lost in hyperspace.",
                    "Server unreachable, reality unstable.",
                    "Proxy is screaming internally.",
                    "Connection lost, blame dark matter."))
            .versionRange("1.16-1.21.11")
            .locale(ProtocolOptional.with("N/A")
                    .since(Version.V1_16_0, "ɴ/ᴀ"))
            .enforceSecureChat(true);


    static @NonNull Response response() {
        return new Response();
    }

    class Response {
        protected static final int MOTD_LINE_SIZE = 7 * 18 + 3; // keep in mind that we have 7 * 18 + 7 = 133 but with padding its just 7 * 18 + 3
        protected static final ProtocolOptional<MinecraftCharacter> SPACE = ProtocolOptional.with(MinecraftCharacter.resolve(' '))
                .since(Version.V1_16_0, MinecraftCharacter.resolve('\u3000')); // 1.16+ supports this char

        protected ProtocolOptional<VersionInfo> versionInfo;
        protected PlayerInfo playerInfo;
        protected List<String> toolTipInfo;
        protected List<String> messages;
        protected String versionRange;
        protected ProtocolOptional<String> locale;
        protected boolean enforceSecureChat;
        protected ProtocolOptional<StateColor> stateColor;

        // todo: add favicon support, for now we just return a placeholder

        protected Response() {
            versionInfo = ProtocolOptional.with(new VersionInfo("Rubrion v1.0", 0));
            toolTipInfo = List.of("Welcome to Rubrion!");
            playerInfo = new PlayerInfo(0, 0);
            messages = List.of("A Rubrion Server");
            versionRange = "1.20-1.21.11";
            enforceSecureChat = false; // I think this is not even used by the client, but we'll set it to false just in case
            stateColor = ProtocolOptional.with(new StateColor(NamedTextColor.GREEN))
                    .since(Version.V1_16_0, new StateColor(TextColor.color(0x00FF00), ShadowColor.shadowColor(0xFF000000))); // 1.16+ supports true type colors and shadows
            locale = ProtocolOptional.with("DE")
                    .since(Version.V1_16_0, "ᴅᴇ"); // 1.16+ supports small fonts
        }

        public @NonNull Response versionInfo(final @NonNull VersionInfo versionInfo) {
            this.versionInfo =  ProtocolOptional.with(versionInfo);
            return this;
        }

        public @NonNull Response versionInfo(final @NonNull ProtocolOptional<VersionInfo> versionInfo) {
            this.versionInfo = versionInfo;
            return this;
        }

        public @NonNull Response stateColor(final @NonNull StateColor stateColor) {
            this.stateColor = ProtocolOptional.with(new StateColor(NamedTextColor.nearestTo(stateColor.textColor()), null))
                    .since(Version.V1_16_0, stateColor); // 1.16+ supports true type colors
            return this;
        }

        public @NonNull Response stateColor(final @NonNull ProtocolOptional<StateColor> stateColor) {
            this.stateColor = stateColor;
            return this;
        }

        public @NonNull Response toolTipInfo(final @NonNull List<String> toolTip) {
            this.toolTipInfo = toolTip;
            return this;
        }

        public @NonNull  Response playerInfo(final @NonNull PlayerInfo playerInfo) {
            this.playerInfo = playerInfo;
            return this;
        }

        public @NonNull Response messages(final @NonNull List<String> messages) {
            this.messages = messages;
            return this;
        }

        public @NonNull Response versionRange(final @NonNull String versionRange) {
            this.versionRange = versionRange;
            return this;
        }

        public @NonNull Response locale(final @NonNull String locale) {
            this.locale = ProtocolOptional.with(locale);
            return this;
        }

        public @NonNull Response locale(final @NonNull ProtocolOptional<String> locale) {
            this.locale = locale;
            return this;
        }

        public @NonNull Response enforceSecureChat(final boolean secureChar) {
            this.enforceSecureChat = secureChar;
            return this;
        }

        public @NonNull Status status(final @NonNull Version version) {
            return status(version.getProtocol());
        }

        protected @NonNull Component buildDescription(final int protocol) {
            final String randomMessage = messages.get((int) (Math.random() * messages.size()));

            final int fillWight = MOTD_LINE_SIZE - MinecraftCharacter.len("  ")
                    - MinecraftCharacter.len(Brands.RUBRION.resolve(protocol).content())
                    - MinecraftCharacter.len(versionRange)
                    - MinecraftCharacter.len(locale.resolve(protocol));

            final Component spaceComponent = Component.text(SPACE.resolve(protocol).fill(fillWight));

            final TextComponent localeComponent = Component.text(locale.resolve(protocol),
                    stateColor.resolve(protocol).textColor());

            if (stateColor.resolve(protocol).shadowColor() != null) {
                //noinspection ResultOfMethodCallIgnored because: is applyed and dont have to be assigned again
                localeComponent.shadowColor(stateColor.resolve(protocol).shadowColor());
            }

            final TextComponent title = Component.empty().append(
                    Brands.RUBRION.resolve(protocol),
                    Component.space(),
                    Component.text(versionRange, NamedTextColor.DARK_GRAY),
                    spaceComponent,
                    Component.space(),
                    Component.space(), // extra space for padding (this is cheating)
                    localeComponent);

            final TextComponent message = Component.empty().append(
                    Component.text("» ", NamedTextColor.DARK_GRAY),
                    Component.text(randomMessage, NamedTextColor.GRAY)
            );


            if (MinecraftCharacter.len(title.content()) > MOTD_LINE_SIZE) {
                throw new UnsupportedOperationException("Version range is too long, max " + MOTD_LINE_SIZE + " char size");
            }

            return Component.empty().append(title, Component.newline(), message);
        }


        protected @NonNull List<NamedAndIdentified> buildToolTipMinestom() {
            List<NamedAndIdentified> sample = new ArrayList<>();
            toolTipInfo.forEach(s -> sample
                    .add(NamedAndIdentified.named(s)));
            return sample;
        }

        @Contract(pure = true)
        protected byte @NonNull [] buildFaviconMinestom() {
            // todo: add build favicon logic, for now we just return a placeholder
            return new byte[20];
        }

        public @NonNull Status status(final int protocol) {
            VersionInfo versionInfo = this.versionInfo.resolve(protocol);

            return Status.builder()
                    .enforcesSecureChat(enforceSecureChat)
                    .description(buildDescription(protocol))
                    .favicon(buildFaviconMinestom())
                    .versionInfo(new Status.VersionInfo(
                            versionInfo.name(),
                            versionInfo.protocol()))
                    .playerInfo(new Status.PlayerInfo(
                            playerInfo.onlinePlayers(),
                            playerInfo.maxPlayers(),
                            buildToolTipMinestom()))
                    .build();
        }

    }

    record VersionInfo(
            @NonNull String name,
            int protocol
    ) { }

    record StateColor(
            @NonNull TextColor textColor,
            @Nullable ShadowColor shadowColor
            ) {
            public StateColor(final @NonNull TextColor textColor) {
                this(textColor, null);
            }
    }

    record PlayerInfo(
            int maxPlayers,
            int onlinePlayers
    ) { }

}
