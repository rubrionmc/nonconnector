package net.rubrion.server.nonconnector;

import de.leycm.linguae.CommonLinguaeProvider;
import de.leycm.linguae.Label;
import de.leycm.linguae.LinguaeProvider;
import de.leycm.linguae.exeption.FormatException;
import de.leycm.linguae.mapping.MappingRule;
import de.leycm.linguae.serialize.LabelSerializer;
import de.leycm.linguae.source.JsonFileSource;
import de.leycm.neck.instance.Initializable;

import lombok.NonNull;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.listener.manager.PacketListenerManager;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.handshake.ClientHandshakePacket;
import net.minestom.server.network.player.PlayerSocketConnection;
import net.rubrion.server.branding.KickMessage;
import net.rubrion.server.docker.DockerFile;
import net.rubrion.server.serverlist.ServerList;

import java.lang.reflect.Field;
import java.util.Locale;


public class RubrionNonConnectorService {

    @SuppressWarnings("UnstableApiUsage")
    // caused by: PlayerSocketConnection
    // reason: the protocol version is getting dropped by listening on the ClientHandshakePacket
    static void main() {

        Initializable.register(CommonLinguaeProvider.builder()
                        .mappingRule(MappingRule.MINI_MESSAGE)
                        .withSerializer(TextComponent.class, new LabelSerializer<TextComponent>() {
                            @Override
                            public @NonNull TextComponent serialize(@NonNull Label label) {
                                throw new UnsupportedOperationException("Component serialization is not supported yet");
                            }

                            @Override
                            public @NonNull Label deserialize(@NonNull TextComponent component) {
                                throw new UnsupportedOperationException("Component deserialization is not supported yet");
                            }

                            @Override
                            public @NonNull TextComponent format(@NonNull String s) throws FormatException {
                                return (TextComponent) MiniMessage.miniMessage().deserialize(s);
                            }
                        })
                        .build(new JsonFileSource(DockerFile.stringFromServerDir("lang/")))
                , LinguaeProvider.class);

        MinecraftServer minecraftServer = MinecraftServer.init();
        PacketListenerManager packetHandler = MinecraftServer.getPacketListenerManager();
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();

        packetHandler.setListener(ConnectionState.HANDSHAKE, ClientHandshakePacket.class,
                (packet, connection) -> {
            if (connection instanceof PlayerSocketConnection socketConnection) {
                try {
                    Field field = PlayerSocketConnection.class.getDeclaredField("protocolVersion");
                    field.setAccessible(true);
                    field.setInt(socketConnection, packet.protocolVersion());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            if (packet.intent().ordinal() == 0) return;

            connection.kick(KickMessage.builder()
                    .author("PreProxy")
                    .description(Label.literal("No Connection found for Rubrion pls Try again later"))
                    .type(KickMessage.Type.DISCONNECT)
                    .build(packet.protocolVersion(), Locale.US));
            connection.disconnect();
        });

        eventHandler.addListener(ServerListPingEvent.class , event -> {
            if (event.getConnection() == null) return;
            event.setStatus(ServerList.NO_CONNECTION
                   .status(event.getConnection().getProtocolVersion()));
        });

        minecraftServer.start("0.0.0.0", 25575);
    }
}
