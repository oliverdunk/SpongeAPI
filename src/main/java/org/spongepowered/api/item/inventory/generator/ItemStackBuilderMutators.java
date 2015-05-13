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

package org.spongepowered.api.item.inventory.generator;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.util.SupplierUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Helper class containing mutators for {@link ItemStackBuilder}s. This class
 * can be used for {@link ItemStackGenerator}s.
 */
public final class ItemStackBuilderMutators {

    private static final Random RANDOM = new Random();

    // ItemType

    /**
     * Creates a mutator, that modifies the builders to always use the given
     * {@link ItemType}.
     *
     * @param type The item type to use
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator type(ItemType type) {
        return types(SupplierUtil.fixed(type));
    }

    /**
     * Creates a mutator, that modifies the builders to use a random
     * {@link ItemType}.
     *
     * @param types The possible item types to use
     * @return The newly generated mutator
     * @throws IllegalArgumentException If the given item types are empty
     */
    public static ItemStackBuilderMutator types(Supplier<ItemType> types) throws IllegalArgumentException {
        return new ItemTypeRandomizer(types);
    }

    /**
     * Applies a random {@link ItemType} to the {@link ItemStackBuilder}.
     */
    private static class ItemTypeRandomizer implements ItemStackBuilderMutator {

        private final Supplier<ItemType> types;

        ItemTypeRandomizer(Supplier<ItemType> types) {
            this.types = checkNotNull(types, "types");
        }

        @Override
        public void apply(ItemStackBuilder builder) {
            builder.itemType(this.types.get());
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("types", this.types)
                    .toString();
        }

    }

    // Quantity

    /**
     * Creates a mutator, that modifies the builders to always use the given
     * value as quantity.
     *
     * @param quantity The quantity to use
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator quantity(int quantity) {
        return quantity(SupplierUtil.fixed(quantity));
    }

    /**
     * Creates a mutator, that modifies the builders to set the quantity based
     * on the given supplier.
     *
     * @param supplier The supplier for the quantity
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator quantity(Supplier<Integer> supplier) {
        return new ItemCountRandomizer(supplier);
    }

    /**
     * Applies a random quantity provided by the randomizer.
     */
    private static class ItemCountRandomizer implements ItemStackBuilderMutator {

        private final Supplier<Integer> randomizer;

        ItemCountRandomizer(Supplier<Integer> randomizer) {
            this.randomizer = checkNotNull(randomizer, "randomizer");
        }

        @Override
        public void apply(ItemStackBuilder builder) {
            builder.quantity(this.randomizer.get());
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("randomizer", this.randomizer)
                    .toString();
        }

    }

    // Data

    /**
     * Creates a mutator, that modifies the builders to add the given
     * {@link DataManipulator} to the item stack.
     *
     * @param data The data to add
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator data(DataManipulator<?, ?> data) {
        return data(() -> checkNotNull(data, "data"));
    }

    /**
     * Creates a mutator, that modifies the builders to add the given
     * {@link DataManipulator} to the item stack.
     *
     * @param supplier The data supplier to wrap
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator data(Supplier<? extends DataManipulator<?, ?>> supplier) {
        return new DataRandomizer(supplier);
    }

    private static class DataRandomizer implements ItemStackBuilderMutator {

        private final Supplier<? extends DataManipulator<?, ?>> itemData;

        DataRandomizer(Supplier<? extends DataManipulator<?, ?>> itemData) {
            super();
            this.itemData = checkNotNull(itemData, "itemData");
        }

        @Override
        public void apply(ItemStackBuilder builder) {
            builder.itemData(this.itemData.get());
        }

    }

    // Enchantments

    /**
     * Creates a mutator, that modifies the builders to add the given
     * enchantment with a random level to the item stack, if it is applicable.
     * If the builder already has this enchantment the current level will be
     * overwritten.
     *
     * @param enchantment The enchantment to add
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator enchantment(Enchantment enchantment) {
        return enchantments(1, checkNotNull(enchantment, "enchantment"));
    }

    /**
     * Creates a mutator, that modifies the builders to add a given number of
     * {@link Enchantment}s. The {@link Enchantment}s are chosen at random, but
     * it is ensured that the enchantments are applicable to the item stacks. If
     * the builder already has the enchantments the current level will be
     * overwritten.
     *
     * @param count The maximum number of enchantments to add
     * @param enchantments The list of possible enchantments
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator enchantments(int count, Enchantment... enchantments) {
        return enchantments(count, Arrays.asList(checkNotNull(enchantments, "enchantments")));
    }

    /**
     * Creates a mutator, that modifies the builders to add a given number of
     * {@link Enchantment}s. The {@link Enchantment}s are chosen at random, but
     * it is ensured that the enchantments are applicable to the item stacks. If
     * the builder already has the enchantments the current level will be
     * overwritten.
     *
     * @param count The maximum number of enchantments to add
     * @param enchantments The list of possible enchantments
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator enchantments(int count, Collection<Enchantment> enchantments) {
        return enchantments(SupplierUtil.fixed(count), enchantments);
    }

    /**
     * Creates a mutator, that modifies the builders to add a given number of
     * {@link Enchantment}s. The {@link Enchantment}s are chosen at random, but
     * it is ensured that the enchantments are applicable to the item stacks. If
     * the builder already has the enchantments the current level will be
     * overwritten.
     *
     * @param count The maximum number of enchantments to add
     * @param enchantments The list of possible enchantments
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator enchantments(Supplier<Integer> count, Enchantment... enchantments) {
        return enchantments(count, Arrays.asList(checkNotNull(enchantments, "enchantments")));
    }

    /**
     * Creates a mutator, that modifies the builders to add a given number of
     * {@link Enchantment}s. The {@link Enchantment}s are chosen at random, but
     * it is ensured that the enchantments are applicable to the item stacks. If
     * the builder already has the enchantments the current level will be
     * overwritten.
     *
     * @param count The maximum number of enchantments to add
     * @param enchantments The list of possible enchantments
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator enchantments(Supplier<Integer> count, Collection<Enchantment> enchantments) {
        return new EnchantmentRandomizer(count, SupplierUtil.randomized(checkNotNull(enchantments, "enchantments")));
    }

    /**
     * Creates a mutator, that modifies the builders to add a given number of
     * {@link Enchantment}s. The {@link Enchantment}s are chosen at random, but
     * it is ensured that the enchantments are applicable to the item stacks. If
     * the builder already has the enchantments the current level will be
     * overwritten.
     *
     * @param count The maximum number of enchantments to add
     * @param enchantments The supplier of possible enchantments
     * @return The newly generated mutator
     */
    public static ItemStackBuilderMutator enchantments(Supplier<Integer> count, Supplier<? extends Iterable<Enchantment>> enchantments) {
        return new EnchantmentRandomizer(count, enchantments);
    }

    private static class EnchantmentRandomizer implements ItemStackBuilderMutator {

        private final Supplier<Integer> count;
        private final Supplier<? extends Iterable<Enchantment>> enchantments;

        EnchantmentRandomizer(Supplier<Integer> count, Supplier<? extends Iterable<Enchantment>> enchantments) {
            super();
            this.count = checkNotNull(count, "count");
            this.enchantments = checkNotNull(enchantments, "enchantments");
        }

        @Override
        public void apply(ItemStackBuilder builder) {
            ItemStack stack = builder.build();
            final Optional<EnchantmentData> optionalData = stack.getOrCreate(EnchantmentData.class);
            if (optionalData.isPresent()) {
                final EnchantmentData data = optionalData.get();
                int count = this.count.get();
                List<ItemEnchantment> enchantments = new ArrayList<>();
                for (Enchantment enchantment : this.enchantments.get()) {
                    if (enchantment.canBeAppliedToStack(stack)) {
                        count--;
                        enchantments.add(new ItemEnchantment(enchantment, randomLevel(enchantment)));
                        data.set(Keys.ITEM_ENCHANTMENTS, enchantments);
                        builder.itemData(data);
                        if (count <= 0) {
                            break;
                        }
                        stack = builder.build();
                    }
                }
                builder.itemData(data);
            }
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("count", this.count)
                    .add("enchantments", this.enchantments)
                    .toString();
        }

        int randomLevel(Enchantment enchantment) {
            int min = enchantment.getMinimumLevel();
            int max = enchantment.getMaximumLevel();
            return min + RANDOM.nextInt(max - min + 1);
        }

    }

}