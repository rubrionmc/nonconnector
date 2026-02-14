package net.rubrion.server.nonconnector;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.listener.manager.PacketListenerManager;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.handshake.ClientHandshakePacket;

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
            connection.kick(Component.text("Fallback ahhhh...."));
            connection.disconnect();
        });

        minecraftServer.start("0.0.0.0", 25575);
    }
}
