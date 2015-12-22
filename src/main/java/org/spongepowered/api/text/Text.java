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
package org.spongepowered.api.text;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.ShiftClickAction;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.translation.locale.NamedLocales;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.Nullable;

/**
 * Represents an immutable instance of formatted text that can be displayed on
 * the client. Each instance consists of content and a list of children texts
 * appended after the content of this text. The content of the text is available
 * through one of the subclasses.
 *
 * <p>Text is primarily used for sending formatted chat messages to players, but
 * also in other places like books or signs.</p>
 *
 * <p>Text instances can be either directly created through the available
 * constructor or using the {@link Builder} available through one of the
 * {@link Texts#builder()} methods, which is the recommended way.</p>
 *
 * @see Texts#builder()
 * @see Builder
 * @see LiteralText
 * @see TranslatableText
 * @see SelectorText
 * @see ScoreText
 */
public abstract class Text implements TextRepresentable {

    /**
     * The default locale used for texts when the receiver's {@link Locale} is
     * unknown.
     */
    public static final Locale DEFAULT_LOCALE = NamedLocales.ENGLISH;

    protected final TextFormat format;
    protected final ImmutableList<Text> children;
    protected final Optional<ClickAction<?>> clickAction;
    protected final Optional<HoverAction<?>> hoverAction;
    protected final Optional<ShiftClickAction<?>> shiftClickAction;

    /**
     * An {@link Iterable} providing an {@link Iterator} over this {@link Text}
     * as well as all children text and their children.
     */
    protected final Iterable<Text> childrenIterable;

    Text() {
        this(new TextFormat(), ImmutableList.<Text>of(), null, null, null);
    }

    /**
     * Constructs a new immutable {@link Text} with the specified formatting and
     * text actions applied.
     *
     * @param format The format of the text
     * @param children The immutable list of children of the text
     * @param clickAction The click action of the text, or {@code null} for none
     * @param hoverAction The hover action of the text, or {@code null} for none
     * @param shiftClickAction The shift click action of the text, or
     *        {@code null} for none
     */
    Text(TextFormat format, ImmutableList<Text> children, @Nullable ClickAction<?> clickAction,
            @Nullable HoverAction<?> hoverAction, @Nullable ShiftClickAction<?> shiftClickAction) {
        this.format = checkNotNull(format, "format");
        this.children = checkNotNull(children, "children");
        this.clickAction = Optional.<ClickAction<?>>ofNullable(clickAction);
        this.hoverAction = Optional.<HoverAction<?>>ofNullable(hoverAction);
        this.shiftClickAction = Optional.<ShiftClickAction<?>>ofNullable(shiftClickAction);
        this.childrenIterable = () ->
                this.children.isEmpty() ? Iterators.singletonIterator(this) : new TextIterator(this);
    }

    /**
     * Returns the format of this {@link Text}.
     *
     * @return The format of this text
     */
    public final TextFormat getFormat() {
        return this.format;
    }

    /**
     * Returns the color of this {@link Text}.
     *
     * @return The color of this text
     */
    public final TextColor getColor() {
        return this.format.getColor();
    }

    /**
     * Returns the style of this {@link Text}. This will return a compound
     * {@link TextStyle} if multiple different styles have been set.
     *
     * @return The style of this text
     */
    public final TextStyle getStyle() {
        return this.format.getStyle();
    }

    /**
     * Returns the immutable list of children appended after the content of this
     * {@link Text}.
     *
     * @return The immutable list of children
     */
    public final ImmutableList<Text> getChildren() {
        return this.children;
    }

    /**
     * Returns an immutable {@link Iterable} over this text and all of its
     * children. This is recursive, the children of the children will be also
     * included.
     *
     * @return An iterable over this text and the children texts
     */
    public final Iterable<Text> withChildren() {
        return this.childrenIterable;
    }

    /**
     * Returns the {@link ClickAction} executed on the client when this
     * {@link Text} gets clicked.
     *
     * @return The click action of this text, or {@link Optional#empty()} if
     *         not set
     */
    public final Optional<ClickAction<?>> getClickAction() {
        return this.clickAction;
    }

    /**
     * Returns the {@link HoverAction} executed on the client when this
     * {@link Text} gets hovered.
     *
     * @return The hover action of this text, or {@link Optional#empty()} if
     *         not set
     */
    public final Optional<HoverAction<?>> getHoverAction() {
        return this.hoverAction;
    }

    /**
     * Returns the {@link ShiftClickAction} executed on the client when this
     * {@link Text} gets shift-clicked.
     *
     * @return The shift-click action of this text, or {@link Optional#empty()}
     *         if not set
     */
    public final Optional<ShiftClickAction<?>> getShiftClickAction() {
        return this.shiftClickAction;
    }

    /**
     * Returns a new {@link Builder} with the content, formatting and
     * actions of this text. This can be used to edit an otherwise immutable
     * {@link Text} instance.
     *
     * @return A new message builder with the content of this text
     */
    public abstract Builder toBuilder();

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Text)) {
            return false;
        }

        Text that = (Text) o;
        return this.format.equals(that.format)
                && this.children.equals(that.children)
                && this.clickAction.equals(that.clickAction)
                && this.hoverAction.equals(that.hoverAction)
                && this.shiftClickAction.equals(that.shiftClickAction);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.format, this.children, this.clickAction, this.hoverAction, this.shiftClickAction);
    }

    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(Text.class)
                .add("format", this.format)
                .add("children", this.children)
                .add("clickAction", this.clickAction)
                .add("hoverAction", this.hoverAction)
                .add("shiftClickAction", this.shiftClickAction);
    }

    @Override
    public final String toString() {
        return toStringHelper().toString();
    }

    @Override
    public final Text toText() {
        return this;
    }

    /**
     * Represents a builder class to create immutable {@link Text} instances.
     *
     * @see Text
     */
    public static abstract class Builder implements TextRepresentable {

        protected TextFormat format = new TextFormat();
        protected List<Text> children = Lists.newArrayList();
        @Nullable protected ClickAction<?> clickAction;
        @Nullable protected HoverAction<?> hoverAction;
        @Nullable protected ShiftClickAction<?> shiftClickAction;

        /**
         * Constructs a new empty {@link Builder}.
         */
        protected Builder() {
        }

        /**
         * Constructs a new {@link Builder} with the properties of the given
         * {@link Text} as initial values.
         *
         * @param text The text to copy the values from
         */
        Builder(Text text) {
            checkNotNull(text, "text");
            this.format = text.format;
            this.children = Lists.newArrayList(text.children);
            this.clickAction = text.clickAction.orElse(null);
            this.hoverAction = text.hoverAction.orElse(null);
            this.shiftClickAction = text.shiftClickAction.orElse(null);
        }

        /**
         * Returns the current format of the {@link Text} in this builder.
         *
         * @return The current format
         * @see Text#getFormat()
         */
        public final TextFormat getFormat() {
            return this.format;
        }

        /**
         * Sets the {@link TextFormat} of this text.
         *
         * @param format The new text format for this text
         * @return The text builder
         * @see Text#getFormat()
         */
        public Builder format(TextFormat format) {
            this.format = checkNotNull(format, "format");
            return this;
        }

        /**
         * Returns the current color of the {@link Text} in this builder.
         *
         * @return The current color
         * @see Text#getColor()
         */
        public final TextColor getColor() {
            return this.format.getColor();
        }

        /**
         * Sets the {@link TextColor} of this text.
         *
         * @param color The new text color for this text
         * @return This text builder
         * @see Text#getColor()
         */
        public Builder color(TextColor color) {
            this.format = this.format.color(checkNotNull(color, "color"));
            return this;
        }

        /**
         * Returns the current style of the {@link Text} in this builder.
         *
         * @return The current style
         * @see Text#getStyle()
         */
        public final TextStyle getStyle() {
            return this.format.getStyle();
        }

        /**
         * Sets the text styles of this text. This will construct a composite
         * {@link TextStyle} of the current style and the specified styles first and
         * set it to the text.
         *
         * @param styles The text styles to apply
         * @return This text builder
         * @see Text#getStyle()
         */
        // TODO: Make sure this is the correct behaviour
        public Builder style(TextStyle... styles) {
            this.format = this.format.style(this.format.getStyle().and(checkNotNull(styles, "styles")));
            return this;
        }

        /**
         * Returns the current {@link ClickAction} of this builder.
         *
         * @return The current click action or {@link Optional#empty()} if none
         * @see Text#getClickAction()
         */
        public final Optional<ClickAction<?>> getClickAction() {
            return Optional.<ClickAction<?>>ofNullable(this.clickAction);
        }

        /**
         * Sets the {@link ClickAction} that will be executed if the text is clicked
         * in the chat.
         *
         * @param clickAction The new click action for the text
         * @return This text builder
         * @see Text#getClickAction()
         */
        public Builder onClick(@Nullable ClickAction<?> clickAction) {
            this.clickAction = clickAction;
            return this;
        }

        /**
         * Returns the current {@link HoverAction} of this builder.
         *
         * @return The current hover action or {@link Optional#empty()} if none
         * @see Text#getHoverAction()
         */
        public final Optional<HoverAction<?>> getHoverAction() {
            return Optional.<HoverAction<?>>ofNullable(this.hoverAction);
        }

        /**
         * Sets the {@link HoverAction} that will be executed if the text is hovered
         * in the chat.
         *
         * @param hoverAction The new hover action for the text
         * @return This text builder
         * @see Text#getHoverAction()
         */
        public Builder onHover(@Nullable HoverAction<?> hoverAction) {
            this.hoverAction = hoverAction;
            return this;
        }

        /**
         * Returns the current {@link ShiftClickAction} of this builder.
         *
         * @return The current shift click action or {@link Optional#empty()} if
         *         none
         * @see Text#getShiftClickAction()
         */
        public final Optional<ShiftClickAction<?>> getShiftClickAction() {
            return Optional.<ShiftClickAction<?>>ofNullable(this.shiftClickAction);
        }

        /**
         * Sets the {@link ShiftClickAction} that will be executed if the text is
         * shift-clicked in the chat.
         *
         * @param shiftClickAction The new shift click action for the text
         * @return This text builder
         * @see Text#getShiftClickAction()
         */
        public Builder onShiftClick(@Nullable ShiftClickAction<?> shiftClickAction) {
            this.shiftClickAction = shiftClickAction;
            return this;
        }

        /**
         * Returns a view of the current children of this builder.
         *
         * <p>The returned list is unmodifiable, but not immutable. It will change
         * if new children get added through this builder.</p>
         *
         * @return An unmodifiable list of the current children
         * @see Text#getChildren()
         */
        public final List<Text> getChildren() {
            return Collections.unmodifiableList(this.children);
        }

        /**
         * Appends the specified {@link Text} to the end of this text.
         *
         * @param children The texts to append
         * @return This text builder
         * @see Text#getChildren()
         */
        public Builder append(Text... children) {
            for (Text child : checkNotNull(children, "children")) {
                checkNotNull(child, "child");
                this.children.add(child);
            }
            return this;
        }

        /**
         * Appends the specified {@link Text} to the end of this text.
         *
         * @param children The texts to append
         * @return This text builder
         * @see Text#getChildren()
         */
        public Builder append(Iterable<? extends Text> children) {
            for (Text child : checkNotNull(children, "children")) {
                this.children.add(checkNotNull(child, "child"));
            }
            return this;
        }

        /**
         * Inserts the specified {@link Text} at the given position of this builder.
         *
         * @param pos The position to insert the texts to
         * @param children The texts to insert
         * @return This text builder
         * @throws IndexOutOfBoundsException If the position is out of bounds
         * @see Text#getChildren()
         */
        public Builder insert(int pos, Text... children) {
            for (Text child : checkNotNull(children, "children")) {
                this.children.add(pos++, checkNotNull(child, "child"));
            }
            return this;
        }

        /**
         * Inserts the specified {@link Text} at the given position of this builder.
         *
         * @param pos The position to insert the texts to
         * @param children The texts to insert
         * @return This text builder
         * @throws IndexOutOfBoundsException If the position is out of range
         * @see Text#getChildren()
         */
        public Builder insert(int pos, Iterable<? extends Text> children) {
            for (Text child : checkNotNull(children, "children")) {
                this.children.add(pos++, checkNotNull(child, "child"));
            }
            return this;
        }

        /**
         * Removes the specified {@link Text} from this builder.
         *
         * @param children The texts to remove
         * @return This text builder
         * @see Text#getChildren()
         */
        public Builder remove(Text... children) {
            for (Text child : checkNotNull(children, "children")) {
                this.children.remove(checkNotNull(child));
            }
            return this;
        }

        /**
         * Removes the specified {@link Text} from this builder.
         *
         * @param children The texts to remove
         * @return This text builder
         * @see Text#getChildren()
         */
        public Builder remove(Iterable<? extends Text> children) {
            for (Text child : checkNotNull(children, "children")) {
                this.children.remove(checkNotNull(child));
            }
            return this;
        }

        /**
         * Removes all children from this builder.
         *
         * @return This text builder
         * @see Text#getChildren()
         */
        public Builder removeAll() {
            this.children.clear();
            return this;
        }

        /**
         * Builds an immutable instance of the current state of this text builder.
         *
         * @return An immutable {@link Text} with the current properties of this
         *         builder
         */
        public abstract Text build();

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Builder)) {
                return false;
            }

            Builder that = (Builder) o;
            return Objects.equal(this.format, that.format)
                    && Objects.equal(this.clickAction, that.clickAction)
                    && Objects.equal(this.hoverAction, that.hoverAction)
                    && Objects.equal(this.shiftClickAction, that.shiftClickAction)
                    && Objects.equal(this.children, that.children);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.format, this.clickAction, this.hoverAction, this.shiftClickAction, this.children);
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(Builder.class)
                    .add("format", this.format)
                    .add("children", this.children)
                    .add("clickAction", this.clickAction)
                    .add("hoverAction", this.hoverAction)
                    .add("shiftClickAction", this.shiftClickAction)
                    .toString();
        }

        @Override
        public final Text toText() {
            return build();
        }

    }

}
