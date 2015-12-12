package org.spongepowered.api.util;

public interface Updater<C> {

    int getInputVersion();

    int getOutputVersion();

    C update(C content);

}
