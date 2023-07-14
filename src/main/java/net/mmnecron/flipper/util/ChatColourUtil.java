package net.mmnecron.flipper.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.mmnecron.flipper.Flipper;

public class ChatColourUtil {
    public static String formatString(String text) {
        String[] parts = text.split("&");
        TextFormatting formatting = TextFormatting.WHITE;
        boolean isBold = false;
        boolean isObfuscated = false;
        boolean isStrikethrough = false;
        boolean isUnderline = false;
        boolean isItalic = false;
        StringBuilder fullText = new StringBuilder();
        for (String part: parts) {
            if (!part.equals("")) {
                String code = String.valueOf(part.charAt(0));
                switch (code) {
                    case "0":
                        formatting = (TextFormatting.BLACK);
                        break;
                    case "1":
                        formatting = (TextFormatting.DARK_BLUE);
                        break;
                    case "2":
                        formatting = (TextFormatting.DARK_GREEN);
                        break;
                    case "3":
                        formatting = (TextFormatting.DARK_AQUA);
                        break;
                    case "4":
                        formatting = (TextFormatting.DARK_RED);
                        break;
                    case "5":
                        formatting = (TextFormatting.DARK_PURPLE);
                        break;
                    case "6":
                        formatting = (TextFormatting.GOLD);
                        break;
                    case "7":
                        formatting = (TextFormatting.GRAY);
                        break;
                    case "8":
                        formatting = (TextFormatting.DARK_GRAY);
                        break;
                    case "9":
                        formatting = (TextFormatting.BLUE);
                        break;
                    case "a":
                        formatting = (TextFormatting.GREEN);
                        break;
                    case "b":
                        formatting = (TextFormatting.AQUA);
                        break;
                    case "c":
                        formatting = (TextFormatting.RED);
                        break;
                    case "d":
                        formatting = (TextFormatting.LIGHT_PURPLE);
                        break;
                    case "e":
                        formatting = (TextFormatting.YELLOW);
                        break;
                    case "f":
                        formatting = (TextFormatting.WHITE);
                        break;
                    case "k":
                        isObfuscated = true;
                        break;
                    case "l":
                        isBold = true;
                        break;
                    case "m":
                        isStrikethrough = true;
                        break;
                    case "n":
                        isUnderline = true;
                        break;
                    case "o":
                        isItalic = true;
                        break;
                    case "r":
                        formatting = TextFormatting.WHITE;
                        isObfuscated = false;
                        isBold = false;
                        isStrikethrough = false;
                        isUnderline = false;
                        isItalic = false;
                        break;
                }
                String codelessPart = part.replaceFirst(code, "");
                ITextComponent textComponent = new TextComponentString(codelessPart).setStyle(new Style()
                        .setColor(formatting)
                        .setObfuscated(isObfuscated)
                        .setBold(isBold)
                        .setStrikethrough(isStrikethrough)
                        .setUnderlined(isUnderline)
                        .setItalic(isItalic));
                fullText.append(textComponent.getFormattedText());
            }
        }
        return fullText.toString();
    }
}
