package net.rubrion.server.branding;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.rubrion.server.protocol.ProtocolOptional;
import net.rubrion.server.protocol.Version;

public interface Brands {
    // todo: move this to labels and load from there as a mini message
    TextComponent RUBRION_FLAT = Component.text("Rubrion", TextColor.color(0xdf4e22))
            .decorate(TextDecoration.BOLD);
    TextComponent RUBRION_NAMED = Component.text("Rubrion", NamedTextColor.GOLD)
            .decorate(TextDecoration.BOLD);

    ProtocolOptional<TextComponent> RUBRION = ProtocolOptional.with(RUBRION_NAMED)
            .since(Version.V1_16_0, RUBRION_FLAT);

    TextComponent RUBRION_ERROR_FLAT = Component.text("Rubrion", TextColor.color(0xFF0000))
            .decorate(TextDecoration.BOLD);
    TextComponent RUBRION_ERROR_NAMED = Component.text("Rubrion", NamedTextColor.DARK_RED)
            .decorate(TextDecoration.BOLD);

    ProtocolOptional<TextComponent> RUBRION_ERROR = ProtocolOptional.with(RUBRION_ERROR_NAMED)
            .since(Version.V1_16_0, RUBRION_ERROR_FLAT);
}
