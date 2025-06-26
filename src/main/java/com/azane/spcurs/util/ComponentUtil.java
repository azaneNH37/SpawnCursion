package com.azane.spcurs.util;

import net.minecraft.ChatFormatting;

public final class ComponentUtil
{
    public static ChatFormatting getRarityColor(int rarity)
    {
        return switch (rarity) {
            case 1 -> ChatFormatting.GRAY;
            case 2 -> ChatFormatting.DARK_GREEN;
            case 3 -> ChatFormatting.BLUE;
            case 4 -> ChatFormatting.DARK_PURPLE;
            case 5 -> ChatFormatting.GOLD;
            case 6 -> ChatFormatting.DARK_RED;
            default -> ChatFormatting.BLUE;
        };
    }
}
