/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.api.text.format;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

/**
 * Represents a pair of {@link TextStyle} and {@link TextColor}.
 */
public final class TextFormat {

    /**
     * The text style.
     */
    private final TextStyle style;

    /**
     * The text color.
     */
    private final TextColor color;

    /**
     * Constructs a new {@link TextFormat}.
     *
     * @param style The style
     * @param color The color
     */
    public TextFormat(TextStyle style, TextColor color) {
        this.style = checkNotNull(style, "style");
        this.color = checkNotNull(color, "color");
    }

    /**
     * Constructs a new {@link TextFormat} with the default color.
     *
     * @param style The style
     */
    public TextFormat(TextStyle style) {
        this(style, TextColors.NONE);
    }

    /**
     * Constructs a new {@link TextFormat} with the default style.
     *
     * @param color The color
     */
    public TextFormat(TextColor color) {
        this(TextStyles.NONE, color);
    }

    /**
     * Constructs a new {@link TextFormat} with the default style and color.
     */
    public TextFormat() {
        this(TextStyles.NONE, TextColors.NONE);
    }

    /**
     * Returns the {@link TextStyle} in this format.
     *
     * @return The style
     */
    public final TextStyle getStyle() {
        return this.style;
    }

    /**
     * Returns the {@link TextColor} in this format.
     *
     * @return The color
     */
    public final TextColor getColor() {
        return this.color;
    }

    /**
     * Returns a new {@link TextFormat} with the given style.
     *
     * @param style The style
     * @return The new text format
     */
    public final TextFormat style(TextStyle style) {
        return new TextFormat(style, this.color);
    }

    /**
     * Returns a new {@link TextFormat} with the given color.
     *
     * @param color The color
     * @return The new text format
     */
    public final TextFormat color(TextColor color) {
        return new TextFormat(this.style, color);
    }

    /**
     * Returns a new {@link TextFormat} that combines this and the given format.
     * The given format takes higher priority than this one. Due to this the
     * color will only fallback to this one if the given format's color is
     * {@link TextColors#NONE}. If the given format's color is
     * {@link TextColors#RESET} then {@link TextColors#NONE} will be used.
     * Styles are combined using {@link TextStyle#and(TextStyle...)}.
     *
     * @param format The format to merge
     * @return The new text format
     */
    public final TextFormat merge(TextFormat format) {
        TextColor color = format.color;
        // If the given format's color is NONE use this ones
        if (color == TextColors.NONE) {
            color = this.color;
            // If the given format's color is RESET use NONE
        } else if (color == TextColors.RESET) {
            color = TextColors.NONE;
        }
        return new TextFormat(this.style.and(format.style), color);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.style, this.color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof TextFormat) {
            TextFormat that = (TextFormat) o;
            return this.style.equals(that.style) && this.color.equals(that.color);
        }
        return false;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("style", this.style)
                .add("color", this.color)
                .toString();
    }

}
