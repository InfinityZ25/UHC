package io.github.infinityz25.uhckotlin.Border.barrier;

import com.google.common.base.Predicate;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import io.github.infinityz25.uhckotlin.Border.barrier.cuboid.Cuboid;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.ChunkLoadCallback;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class VisualiseHandler
{
  private final Table<UUID, Location, VisualBlock> storedVisualises = HashBasedTable.create();

  public final Map<UUID, os> visualizer = new HashMap<>();



  public Table<UUID, Location, VisualBlock> getStoredVisualises()
  {
    return this.storedVisualises;
  }

  @Deprecated
  public VisualBlock getVisualBlockAt(Player player, int x, int y, int z)
    throws NullPointerException
  {
    return getVisualBlockAt(player, new Location(player.getWorld(), x, y, z));
  }

  /* Error */
  public VisualBlock getVisualBlockAt(Player player, Location location)
    throws NullPointerException
  {
    return storedVisualises.get(player, location);
  }

  public Map getVisualBlocks(Player player, final VisualType visualType)
  {
    return Maps.filterValues(getVisualBlocks(player, visualType), new Predicate()
    {
      public boolean apply(VisualBlock visualBlock)
      {
        return visualType == visualBlock.getVisualType();
      }

      public boolean apply(Object arg0)
      {
        return false;
      }
    });
  }
  
  public LinkedHashMap<Location, VisualBlockData> generate(Player player, Cuboid cuboid, VisualType visualType, boolean canOverwrite)
  {
    Collection<Location> locations = new HashSet(cuboid.getSizeX() * cuboid.getSizeY() * cuboid.getSizeZ());
    Iterator<Location> iterator = cuboid.locationIterator();
    while (iterator.hasNext()) {
      locations.add(iterator.next());
    }
    return generate(player, locations, visualType, canOverwrite);
  }
  
  public LinkedHashMap<Location, VisualBlockData> generateAsync(Player player, Cuboid cuboid, VisualType visualType, boolean canOverwrite)
  {
    Collection<Location> locations = new HashSet(cuboid.getSizeX() * cuboid.getSizeY() * cuboid.getSizeZ());
    Iterator<Location> iterator = cuboid.locationIterator();
    while (iterator.hasNext()) {
      locations.add(iterator.next());
    }
    return generateAsync(player, locations, visualType, canOverwrite);
  }
  
  public LinkedHashMap<Location, VisualBlockData> generate(Player player, Iterable<Location> locations, VisualType visualType, boolean canOverwrite)
  {
    synchronized (this.storedVisualises)
    {
      LinkedHashMap<Location, VisualBlockData> results = new LinkedHashMap();
      
      ArrayList<VisualBlockData> filled = visualType.blockFiller().bulkGenerate(player, locations);
      if (filled != null)
      {
        int count = 0;
        for (Location location : locations) {
          if ((canOverwrite) || (!this.storedVisualises.contains(player.getUniqueId(), location)))
          {
            Material previousType = location.getBlock().getType();
            if ((!previousType.isSolid()) && (previousType == Material.AIR))
            {
              VisualBlockData visualBlockData = filled.get(count++);
              results.put(location, visualBlockData);
              player.sendBlockChange(location, visualBlockData.getBlockType(), visualBlockData.getData());
              this.storedVisualises.put(player.getUniqueId(), location, new VisualBlock(visualType, visualBlockData, location));
            }
          }
        }
      }
      return results;
    }
  }
  
  public LinkedHashMap<Location, VisualBlockData> generateAsync(final Player player, Iterable<Location> locations, final VisualType visualType, boolean canOverwrite)
  {
    synchronized (this.storedVisualises)
    {
      final LinkedHashMap<Location, VisualBlockData> results = new LinkedHashMap();
      
      final ArrayList<VisualBlockData> filled = visualType.blockFiller().bulkGenerate(player, locations);
      if (filled != null) {
        for (final Location location : locations) {
          if ((canOverwrite) || (!this.storedVisualises.contains(player.getUniqueId(), location))) {
            location.getWorld().getChunkAtAsync(location, new ChunkLoadCallback()
            {
              int count = 0;
              
              public void onLoad(org.bukkit.Chunk chunk)
              {
                Material previousType = CraftMagicNumbers.getMaterial(((CraftChunk)chunk).getHandle().getType(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ())));



                if ((previousType.isSolid()) || (previousType != Material.AIR)) {
                  return;
                }
                VisualBlockData visualBlockData = filled.get(this.count++);
                results.put(location, visualBlockData);
                player.sendBlockChange(location, visualBlockData.getBlockType(), visualBlockData.getData());
                VisualiseHandler.this.storedVisualises.put(player.getUniqueId(), location, new VisualBlock(visualType, visualBlockData, location));
              }
            });
          }
        }
      }
      return results;
    }
  }
  
  public boolean clearVisualBlock(Player player, Location location, boolean sendRemovalPacket)
  {
    synchronized (this.storedVisualises)
    {
      VisualBlock visualBlock = (VisualBlock)this.storedVisualises.remove(player.getUniqueId(), location);
      if ((sendRemovalPacket) && (visualBlock != null))
      {
        Block block = location.getBlock();
        VisualBlockData visualBlockData = visualBlock.getBlockData();
        if ((visualBlockData.getBlockType() != block.getType()) || (visualBlockData.getData() != block.getData())) {
          player.sendBlockChange(location, block.getType(), block.getData());
        }
        return true;
      }
    }
    return false;
  }

  
  public Map<Location, VisualBlock> clearVisualBlocks(Player player, @Nullable VisualType visualType, @Nullable Predicate<VisualBlock> predicate)
  {
    return clearVisualBlocks(player, visualType, predicate, true);
  }
  
  @Deprecated
  public Map<Location, VisualBlock> clearVisualBlocks(Player player, @Nullable VisualType visualType, @Nullable Predicate<VisualBlock> predicate, boolean sendRemovalPackets)
  {
    synchronized (this.storedVisualises)
    {
      if (!this.storedVisualises.containsRow(player.getUniqueId())) {
        return Collections.emptyMap();
      }
      Map<Location, VisualBlock> results = new HashMap(this.storedVisualises.row(player.getUniqueId()));
      HashMap removed = new HashMap();

      results.forEach((location, visualBlock) -> {
        if (((predicate == null) || (predicate.apply(visualBlock))) && ((visualType == null) || (visualBlock.getVisualType() == visualType))) {
          if (removed.put(location, visualBlock) == null) {
            clearVisualBlock(player, location, sendRemovalPackets);
          }
        }

      });
      return removed;
    }
  }
}
