package com.derongan.minecraft.ui;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Size {
    public abstract int getWidth();
    public abstract int getHeight();

    public static Size create(int newWidth, int newHeight) {
        return new AutoValue_Size(newWidth, newHeight);
    }
}
