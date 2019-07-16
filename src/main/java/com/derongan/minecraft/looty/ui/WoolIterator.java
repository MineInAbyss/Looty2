package com.derongan.minecraft.looty.ui;

import com.google.common.collect.Iterators;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class WoolIterator implements Iterator<Material> {
    private Iterator<Material> iterator;

    public WoolIterator() {
        List<Material> wools = Arrays.stream(Material.values())
                .filter(mat -> mat.name().endsWith("_WOOL"))
                .collect(toImmutableList());

        iterator = Iterators.cycle(wools);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Material next() {
        return iterator.next();
    }
}
