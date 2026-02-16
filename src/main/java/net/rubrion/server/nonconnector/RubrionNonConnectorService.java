package net.rubrion.server.nonconnector;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.listener.manager.PacketListenerManager;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.handshake.ClientHandshakePacket;
import net.minestom.server.network.player.PlayerSocketConnection;
import net.rubrion.server.serverlist.ServerList;

import java.lang.reflect.Field;


public class RubrionNonConnectorService {

    @SuppressWarnings("UnstableApiUsage")
    // caused by: PlayerSocketConnection
    // reason: the protocol version is getting dropped by listening on the ClientHandshakePacket
    static void main(String[] args) {

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

            // todo: use the common lib to build nice message
            connection.disconnect();
        });

        eventHandler.addListener(ServerListPingEvent.class , event -> {
            if (event.getConnection() == null) return;
            event.setStatus(ServerList.NO_CONNECTION
                   .status(event.getConnection().getProtocolVersion()));
        });

        minecraftServer.start("0.0.0.0", 25575);
        System.out.println("Rubrion Non-Connector Service started on port 25575");
    }
}
