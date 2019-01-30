package com.bgsoftware.superiorskyblock.schematics;

import com.bgsoftware.superiorskyblock.api.schematic.Schematic;
import com.bgsoftware.superiorskyblock.utils.jnbt.ByteTag;
import com.bgsoftware.superiorskyblock.utils.jnbt.CompoundTag;
import com.bgsoftware.superiorskyblock.utils.jnbt.ListTag;
import com.bgsoftware.superiorskyblock.utils.jnbt.Tag;
import com.bgsoftware.superiorskyblock.utils.TagUtil;

import org.bukkit.Location;

public final class SSchematic implements Schematic {

    private CompoundTag compoundTag;

    public SSchematic(CompoundTag compoundTag){
        this.compoundTag = compoundTag;
    }

    @Override
    public void pasteSchematic(Location location){
        byte xSize = ((ByteTag) compoundTag.getValue().get("xSize")).getValue();
        byte ySize = ((ByteTag) compoundTag.getValue().get("ySize")).getValue();
        byte zSize = ((ByteTag) compoundTag.getValue().get("zSize")).getValue();
        //noinspection IntegerDivisionInFloatingPointContext
        Location min = location.clone().subtract(xSize / 2, ySize / 2, zSize / 2);

        for(Tag tag : ((ListTag) compoundTag.getValue().get("blocks")).getValue()){
            TagUtil.assignIntoBlock((CompoundTag) tag, min);
        }

        for(Tag tag : ((ListTag) compoundTag.getValue().get("entities")).getValue()){
            TagUtil.spawnEntity((CompoundTag) tag, location);
        }
    }

    public CompoundTag getTag(){
        return compoundTag;
    }

    @Override
    public String toString() {
        return compoundTag.toString();
    }

}