package io.github.infinityz25.uhckotlin.scoreboard

import net.minecraft.server.v1_8_R3.*
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.ArrayList

open class NMSBoard(var player: Player, var objectiveName: String){


    //TODO: ADD FUNCTION TO ADD PLAYERS TO TEAM TO SHOW NAMETAG ON A DIFFERENT COLOR
    private var created = false
    private var lines = arrayOfNulls<VirtualTeam>(15)

    fun create() {
        if (created)
            return

        val player = getPlayer()
        player.sendPacket(createObjectivePacket(0, objectiveName))
        player.sendPacket(setObjectiveSlot())
        var i = 0
        while (i < lines.size)
            sendLine(i++)

        created = true
    }

    /**
     * Send the packets to remove this scoreboard sign. A destroyed scoreboard sign must be recreated using [ScoreboardNMS.create] in order
     * to be used again
     */
    fun destroy() {
        if (!created)
            return

        getPlayer().sendPacket(createObjectivePacket(1, null))
        for (team in lines)
            if (team != null) {
                getPlayer().sendPacket(team.removeTeam())
                team.reset()
            }

        created = false
    }

    /**
     * Change a scoreboard line and send the packets to the player. Can be called async.
     * @param line the number of the line (0 <= line < 15)
     * @param value the new value for the scoreboard line
     */
    fun setLine(line: Int, value: String) {
        val team = getOrCreateTeam(line)
        val old = team.currentPlayer

        if (old != null && created)
            getPlayer().sendPacket(removeLine(old))

        team.value = value
        sendLine(line)
    }

    /**
     * Remove a given scoreboard line
     * @param line the line to remove
     */
    fun removeLine(line: Int) {
        val team = getOrCreateTeam(line)
        val old = team.currentPlayer

        if (old != null && created) {
            getPlayer().sendPacket(removeLine(old))
            getPlayer().sendPacket(team.removeTeam())
        }

        lines[line] = null
    }

    /**
     * Get the current value for a line
     * @param line the line
     * @return the content of the line
     */
    fun getLine(line: Int): String? {
        if (line > 14)
            return null
        return if (line < 0) null else getOrCreateTeam(line).value
    }

    /**
     * Get the team assigned to a line
     * @return the [VirtualTeam] used to display this line
     */
    fun getTeam(line: Int): VirtualTeam? {
        if (line > 14)
            return null
        return if (line < 0) null else getOrCreateTeam(line)
    }

    private fun getPlayer(): PlayerConnection {
        return (player as CraftPlayer).handle.playerConnection
    }

    private fun sendLine(line: Int) {
        if (line > 14)
            return
        if (line < 0)
            return
        if (!created)
            return

        val `val` = getOrCreateTeam(line)
        for (packet in `val`.sendLine())
            getPlayer().sendPacket(packet)
        getPlayer().sendPacket(sendScore(`val`.value, line))
        `val`.reset()
    }

    private fun getOrCreateTeam(line: Int): VirtualTeam {
        if (lines[line] == null)
            lines[line] = VirtualTeam("__fakeScore$line")

        return lines[line]!!
    }

    /*
        Factories
         */
    private fun createObjectivePacket(mode: Int, displayName: String?): PacketPlayOutScoreboardObjective {
        val packet = PacketPlayOutScoreboardObjective()
        // Nom de l'objectif
        setField(packet, "a", player.name)

        // Mode
        // 0 : créer
        // 1 : Supprimer
        // 2 : Mettre à jour
        setField(packet, "d", mode)

        if (mode == 0 || mode == 2) {
            setField(packet, "b", displayName!!)
            setField(packet, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER)
        }

        return packet
    }

    private fun setObjectiveSlot(): PacketPlayOutScoreboardDisplayObjective {
        val packet = PacketPlayOutScoreboardDisplayObjective()
        // Slot
        setField(packet, "a", 1)
        setField(packet, "b", player.name)

        return packet
    }

    private fun sendScore(line: String, score: Int): PacketPlayOutScoreboardScore {
        val packet = PacketPlayOutScoreboardScore(line)
        setField(packet, "b", player.name)
        setField(packet, "c", score)
        setField(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE)

        return packet
    }

    private fun removeLine(line: String): PacketPlayOutScoreboardScore {
        return PacketPlayOutScoreboardScore(line)
    }


    inner class VirtualTeam

    internal constructor(
        val name: String,
        private var prefix: String? = "",
        private var suffix: String? = ""
    ) {
        var currentPlayer: String? = null
            private set
        private var oldPlayer: String? = null

        private var prefixChanged: Boolean = false
        private var suffixChanged: Boolean = false
        private var playerChanged = false
        private var first = true

        var value: String
            get() = getPrefix() + currentPlayer + getSuffix()
            set(value) = if (value.length <= 16) {
                setPrefix("")
                setSuffix("")
                setPlayer(value)
            } else if (value.length <= 32) {
                setPrefix(value.substring(0, 16))
                setPlayer(value.substring(16))
                setSuffix("")
            } else if (value.length <= 48) {
                setPrefix(value.substring(0, 16))
                setPlayer(value.substring(16, 32))
                setSuffix(value.substring(32))
            } else {
                throw IllegalArgumentException("Too long value ! Max 48 characters, value was " + value.length + " !")
            }

        fun getPrefix(): String? {
            return prefix
        }

        fun setPrefix(prefix: String) {
            if (this.prefix == null || this.prefix != prefix)
                this.prefixChanged = true
            this.prefix = prefix
        }

        fun getSuffix(): String? {
            return suffix
        }

        fun setSuffix(suffix: String) {
            if (this.suffix == null || this.suffix != prefix)
                this.suffixChanged = true
            this.suffix = suffix
        }

        private fun createPacket(mode: Int): PacketPlayOutScoreboardTeam {
            val packet = PacketPlayOutScoreboardTeam()
            setField(packet, "a", name)
            setField(packet, "h", mode)
            setField(packet, "b", "")
            setField(packet, "c", prefix!!)
            setField(packet, "d", suffix!!)
            setField(packet, "i", 0)
            setField(packet, "e", "always")
            setField(packet, "f", 0)

            return packet
        }

        fun createTeam(): PacketPlayOutScoreboardTeam {
            return createPacket(0)
        }

        fun updateTeam(): PacketPlayOutScoreboardTeam {
            return createPacket(2)
        }

        fun removeTeam(): PacketPlayOutScoreboardTeam {
            val packet = PacketPlayOutScoreboardTeam()
            setField(packet, "a", name)
            setField(packet, "h", 1)
            first = true
            return packet
        }

        fun setPlayer(name: String) {
            if (this.currentPlayer == null || this.currentPlayer != name)
                this.playerChanged = true
            this.oldPlayer = this.currentPlayer
            this.currentPlayer = name
        }

        fun sendLine(): Iterable<PacketPlayOutScoreboardTeam> {
            val packets = ArrayList<PacketPlayOutScoreboardTeam>()

            if (first) {
                packets.add(createTeam())
            } else if (prefixChanged || suffixChanged) {
                packets.add(updateTeam())
            }

            if (first || playerChanged) {
                if (oldPlayer != null)
                // remove these two lines ?
                    packets.add(addOrRemovePlayer(4, oldPlayer))    //
                packets.add(changePlayer())
            }

            if (first)
                first = false

            return packets
        }

        fun reset() {
            prefixChanged = false
            suffixChanged = false
            playerChanged = false
            oldPlayer = null
        }

        fun changePlayer(): PacketPlayOutScoreboardTeam {
            return addOrRemovePlayer(3, currentPlayer)
        }

        fun addOrRemovePlayer(mode: Int, playerName: String?): PacketPlayOutScoreboardTeam {
            val packet = PacketPlayOutScoreboardTeam()
            setField(packet, "a", name)
            setField(packet, "h", mode)

            try {
                val f = packet.javaClass.getDeclaredField("g")
                f.isAccessible = true
                (f.get(packet) as MutableList<String>).add(playerName!!)
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            return packet
        }

    }


    private fun setField(edit: Any, fieldName: String, value: Any) {
        try {
            val field = edit.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(edit, value)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

}
