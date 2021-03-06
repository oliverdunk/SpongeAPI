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
package org.spongepowered.api.event.command;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.action.MessageEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.sink.MessageSink;
import org.spongepowered.api.command.CommandSource;

/**
 * Describes events when a {@link CommandSource} sends a {@link Text} message.
 */
public interface MessageSinkEvent extends MessageEvent {

    /**
     * Gets the original sink that this message will be sent to.
     *
     * @return The original message sink to send to
     */
    MessageSink getOriginalSink();

    /**
     * Gets the current sink that this message will be sent to.
     *
     * @return The message sink the message in this event will be sent to
     */
    MessageSink getSink();

    /**
     * Set the target for this message to go to.
     *
     * @param sink The sink to set
     */
    void setSink(MessageSink sink);

    /**
     * Fired when the {@link Text} being sent to a {@link MessageSink} was due to chatting.
     */
    interface Chat extends MessageSinkEvent, Cancellable {

        /**
         * Gets the 'raw' chat message.
         *
         * <p>This message is the original chat message, without any formatting whatsoever.
         * In Vanilla, this is equivalent to what a player typed into the chat box
         * (no name prefix or other elements).</p>
         *
         * @return The raw message
         */
        Text getRawMessage();
    }
}
