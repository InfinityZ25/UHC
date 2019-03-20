package io.github.infinityz25.uhckotlin.Border.barrier;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum VisualType
{
    UHC_BORDER(0) {
        private final BlockFiller blockFiller;
        
        {
            this.blockFiller = new BlockFiller() {
                @SuppressWarnings("deprecation")
                @Override
                VisualBlockData generate(final Player player, final Location location) {
                    return new VisualBlockData(Material.STAINED_GLASS, DyeColor.ORANGE.getData());
                }
            };
        }
        
        @Override
        BlockFiller blockFiller() {
            return this.blockFiller;
        }
    };

	 VisualType(final int n) {
    }

    abstract BlockFiller blockFiller();
}
