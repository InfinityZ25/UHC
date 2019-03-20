package io.github.infinityz25.uhckotlin.Border.barrier;

import io.github.infinityz25.uhckotlin.Border.barrier.cuboid.Cuboid;
import io.github.infinityz25.uhckotlin.UHC;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.Collection;

public class WallBorderListener implements Listener
{
    private UHC instance;
    public WallBorderListener(UHC instance){
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(final PlayerMoveEvent event) {
            final Location to = event.getTo();
            final int toX = to.getBlockX();
            final int toY = to.getBlockY();
            final int toZ = to.getBlockZ();
            final Location from = event.getFrom();
            if (from.getBlockX() != toX || from.getBlockY() != toY || from.getBlockZ() != toZ) {
                this.handlePositionChanged(event.getPlayer(), to.getWorld(), toX, toY, toZ);
            }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        this.onPlayerMove(event);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
      //  Bukkit.broadcastMessage("Yes\n" + e.getBlock().getType());
    }

    private void handlePositionChanged(final Player player, final World toWorld, final int toX, final int toY, final int toZ) {
        final VisualType visualType = VisualType.UHC_BORDER;
        instance.getVisualiseHandler().clearVisualBlocks(player, visualType, visualBlock -> {
            assert visualBlock != null;
            final Location other = visualBlock.getLocation();
            return other.getWorld().equals(toWorld) && (Math.abs(toX - other.getBlockX()) > 5 || Math.abs(toY - other.getBlockY()) > 4 || Math.abs(toZ - other.getBlockZ()) > 5);
        });
        final int minHeight = toY - 3;
        final int maxHeight = toY + 4;
        final double x = 100;
        final double z = 100;
        final Location loc1 = new Location(toWorld, x, 0.0, -z);
        final Location loc2 = new Location(toWorld, -x, 0.0, z);
        final Cuboid cb = new Cuboid(loc1, loc2);
        final Collection<Vector> edges = cb.edges();
        for (final Vector edge : edges) {
            if (Math.abs(edge.getBlockX() - toX) <= 10 && Math.abs(edge.getBlockZ() - toZ) <= 10) {
                final Location location = edge.toLocation(toWorld);
                if (location == null) {
                    continue;
                }
                final Location first = location.clone();
                first.setY((double)minHeight);
                final Location second = location.clone();
                second.setY((double)maxHeight);
                instance.getVisualiseHandler().generateAsync(player, new Cuboid(first, second), visualType, false).size();
            }
        }
    }
}