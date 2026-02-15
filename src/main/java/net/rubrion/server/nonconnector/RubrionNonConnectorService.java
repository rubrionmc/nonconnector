package net.rubrion.server.nonconnector;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.listener.manager.PacketListenerManager;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.handshake.ClientHandshakePacket;
import net.minestom.server.ping.Status;

public class RubrionNonConnectorService {

    static void main(String[] args) {

        MinecraftServer minecraftServer = MinecraftServer.init();
        PacketListenerManager packetHandler = MinecraftServer.getPacketListenerManager();
        GlobalEventHandler eventHandler = MinecraftServer.getGlobalEventHandler();

        packetHandler.setListener(ConnectionState.HANDSHAKE, ClientHandshakePacket.class, (
                packet, connection) -> {
            // todo: use the common lib to build nice message
            // todo: make packet check and send a LegacyKickMessage if the client not support Components
            if (packet.intent().ordinal() == 0) {
                connection.disconnect();
                return;
            }
            System.out.println(packet.getClass().getName());
            if (packet.protocolVersion() >= 735) { // 1.16+
                connection.kick(Component.text("This is the new nice color system", TextColor.color(0xFF0000)));
                connection.disconnect();
                return;
            }
            // older than 1.16
            connection.kick(Component.text("This is only true type", NamedTextColor.DARK_PURPLE));
            connection.disconnect();
        });

        eventHandler.addListener(ServerListPingEvent.class , event -> {
            // todo: use the common lib to build status message
            event.setStatus(Status.builder()
                            .versionInfo(new Status.VersionInfo("No Connection", -1))
                            .description(Component.text("Rubrion Non-Connector Service"))
                            .enforcesSecureChat(false)
                            .playerInfo(Status.PlayerInfo.builder()
                                    .maxPlayers(-1)
                                    .onlinePlayers(0)
                                    .sample("Your mom")
                                    .build())
                    .build());
        });

        minecraftServer.start("0.0.0.0", 25575);
    }
}
