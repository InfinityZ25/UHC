package io.github.infinityz25.uhckotlin.Border.barrier;

import org.bukkit.Location;

public class VisualBlock
{
    private VisualType visualType;
    private VisualBlockData blockData;
    private Location location;
    
    public VisualBlock(final VisualType visualType, final VisualBlockData blockData, final Location location) {
        this.visualType = visualType;
        this.blockData = blockData;
        this.location = location;
    }
    
    public VisualType getVisualType() {
        return this.visualType;
    }
    
    public VisualBlockData getBlockData() {
        return this.blockData;
    }
    
    public Location getLocation() {
        return this.location;
    }
}
