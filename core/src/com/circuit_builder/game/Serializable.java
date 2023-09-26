package com.circuit_builder.game;

import com.badlogic.gdx.files.FileHandle;

public interface Serializable {
    public void save(FileHandle handle);
    public void load(FileHandle handle);
}
