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
package org.spongepowered.api.entity.projectile;

import org.spongepowered.api.entity.Entity;

import java.util.Optional;

import javax.annotation.Nullable;

/**
 * Represents a fish hook.
 */
public interface FishHook extends Projectile {

    /**
     * Gets the hooked entity for this fish hook.
     *
     * <p>Fishooks can attach to {@link Entity} objects in the world,
     * as though they are temporarily leashed. The hooked entity may
     * also be null.</p>
     *
     * @return The hooked item, if available
     */
    Optional<Entity> getHookedEntity();

    /**
     * Sets the hooked entity for this fish hook.
     *
     * <p>Fishooks can attach to {@link Entity} objects in the world,
     * as though they are temporarily leashed. The hooked entity may
     * also be null.</p>
     *
     * @param entity The hooked entity
     */
    void setHookedEntity(@Nullable Entity entity);

}
