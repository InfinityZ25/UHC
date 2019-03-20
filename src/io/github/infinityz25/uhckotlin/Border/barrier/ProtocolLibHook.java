package io.github.infinityz25.uhckotlin.Border.barrier;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Client;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.StructureModifier;
import io.github.infinityz25.uhckotlin.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ProtocolLibHook
{
  public static void hook(final UHC instance)
  {
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, Client.BLOCK_PLACE)
    {
      public void onPacketReceiving(PacketEvent event)
      {
        PacketContainer packet = event.getPacket();
        StructureModifier<Integer> modifier = packet.getIntegers();
        final Player player = event.getPlayer();
        try
        {
          if ((modifier.size() < 4) || (modifier.read(3) == 255)) {
            return;
          }
          Bukkit.broadcastMessage("perhaps2");
          int face2 = 0;
          final Location location = new Location(player.getWorld(), modifier.read(0), modifier.read(1), modifier.read(2));
          VisualBlock visualBlock = instance.getVisualiseHandler().getVisualBlockAt(player, location);
          if (visualBlock == null) {
            return;
          }
          location.add(0.0D, -1.0D, 0.0D);
          event.setCancelled(true);
          ItemStack stack = packet.getItemModifier().read(0);
          if ((stack != null) && ((stack.getType().isBlock()) || (isLiquidSource(stack.getType())))) {
            player.setItemInHand(player.getItemInHand());
          }
          visualBlock = instance.getVisualiseHandler().getVisualBlockAt(player, location);
          if (visualBlock != null)
          {
            VisualBlockData visualBlockData = visualBlock.getBlockData();
            player.sendBlockChange(location, visualBlockData.getBlockType(), visualBlockData.getData());
          }
          else
          {
            BukkitRunnable bukkitRunnable = new BukkitRunnable()
            {
              public void run()
              {
                org.bukkit.block.Block block = location.getBlock();
                player.sendBlockChange(location, block.getType(), block.getData());
              }
            };
            bukkitRunnable.runTask(instance);
          }
        }
        catch (FieldAccessException ignored) {}
      }

      private boolean isLiquidSource(Material type)
      {
        return false;
      }
    });

    protocolManager.addPacketListener(
            new PacketAdapter(instance, ListenerPriority.NORMAL,
                    PacketType.Play.Server.NAMED_SOUND_EFFECT) {
              @Override
              public void onPacketSending(PacketEvent event) {
                // Item packets (id: 0x29)

                if (event.getPacketType() == PacketType.Play.Server.NAMED_SOUND_EFFECT) {

                  PacketContainer packet = event.getPacket();
                  String str = packet.getStrings().read(0);
                  if(str.equalsIgnoreCase("mob.zombie.hurt")){
                    event.setCancelled(true);
                  }
                }
              }
            });

    protocolManager.addPacketListener(
            new PacketAdapter(instance, ListenerPriority.NORMAL,
                    PacketType.Play.Server.KICK_DISCONNECT) {
              @Override
              public void onPacketSending(PacketEvent event) {
                // Item packets (id: 0x29)
                Bukkit.broadcastMessage("disc");
              }
            });




    protocolManager.addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, Client.BLOCK_DIG)

    {
      public void onPacketReceiving(PacketEvent event)
      {
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();
        try
        {

          int status = packet.getPlayerDigTypes().read(0).ordinal();
          if ((status == 0) || (status == 2))
          {
            event.setCancelled(true);

            com.comphenix.protocol.wrappers.BlockPosition position = packet.getBlockPositionModifier().read(0);
            int x = position.getX();
            int y = position.getX();
            int z = position.getZ();
            //Bukkit.broadcastMessage("" + x + " " + y + " " +z);
            Location location = new Location(player.getWorld(), x, y, z);
            if(status == 2){
              Bukkit.broadcastMessage("test\n" + location.getBlock().getType());

            }
           VisualBlock visualBlock = instance.getVisualiseHandler().getVisualBlockAt(player, x, y ,z);
            if(visualBlock != null){
              Bukkit.broadcastMessage("e");
            }
            else{
              Bukkit.broadcastMessage("e2");
            }
            if(instance.getVisualiseHandler().getStoredVisualises().contains(player.getUniqueId(), location)){
             // Bukkit.broadcastMessage("true");
            }
            else{
             // Bukkit.broadcastMessage("false");
            }
            /*
            if (visualBlock == null) {
              Bukkit.broadcastMessage("nay");
              return;
            }
            Bukkit.broadcastMessage("yes");
            event.setCancelled(true);
            VisualBlockData data = visualBlock.getBlockData();
            if (status == 2)
            {
              player.sendBlockChange(location, data.getBlockType(), data.getData());
            }
            else {
              EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
              if ((player.getGameMode() == GameMode.CREATIVE) || (net.minecraft.server.v1_8_R3.Block.getById(data.getItemTypeId()).getDamage(
                      entityPlayer, entityPlayer.world, new BlockPosition(x, y, z)) > 1.0F)) {
                player.sendBlockChange(location, data.getBlockType(), data.getData());
              }
            }
            */
          }
        }
        catch (FieldAccessException i) {
          i.printStackTrace();
        }
      }
    });
  }

  private static boolean isLiquidSource(Material material)
  {
    switch (material)
    {
      case CHAINMAIL_BOOTS:
      case COOKED_FISH:
        return true;
    }
    return false;
  }
}
