package net.holefloor.arena.powerup;

import net.holefloor.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public final class ArenaPowerBooster {
    public final Arena arena;
    public ArmorStand stand;
    public Boolean isSpawned;
    public ArenaBoosterType type;

    public ArenaPowerBooster(Arena arena) {
        this.arena = arena;
        this.isSpawned = false;
    }

    public void spawn(ArenaBoosterType type) {
        this.type = type;
        this.stand = this.arena.properties.world.spawn(this.arena.properties.type.getBoosterSpawn(), ArmorStand.class);
        this.stand.setCustomNameVisible(true);
        this.stand.setCustomName("Powerup");
        this.stand.setMarker(true);
        this.stand.setGravity(false);
        this.stand.setVisible(false);

        switch (type.getRate()) {
            case COMMON -> this.stand.getEquipment().setHelmet(new ItemStack(Material.IRON_BLOCK));
            case RARE -> this.stand.getEquipment().setHelmet(new ItemStack(Material.GOLD_BLOCK));
            case UNIQUE -> this.stand.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
            case LEGENDARY -> this.stand.getEquipment().setHelmet(new ItemStack(Material.EMERALD_BLOCK));
        }
        this.isSpawned = true;
    }

    public void collect(Player player) {
        this.arena.properties.citizens.players.forEach(consumer -> {
            consumer.playSound(consumer.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1.5F);
            switch (this.type.getRate()) {
                case COMMON -> {
                    consumer.sendMessage(this.arena.instance.locale.getString("arena.booster")
                            .replace("%rate%", "§7" + this.type.getRate())
                            .replace("%player%", player.getName()));
                }
                case RARE -> {
                    consumer.sendMessage(this.arena.instance.locale.getString("arena.booster")
                            .replace("%rate%", "§a" + this.type.getRate())
                            .replace("%player%", player.getName())
                    );
                }
                case UNIQUE -> {
                    consumer.sendMessage(this.arena.instance.locale.getString("arena.booster")
                            .replace("%rate%", "§e" + this.type.getRate())
                            .replace("%player%", player.getName())
                    );
                }
                case LEGENDARY -> {
                    consumer.sendMessage(this.arena.instance.locale.getString("arena.booster")
                            .replace("%rate%", "§6" + this.type.getRate())
                            .replace("%player%", player.getName())
                    );
                }
            }
        });

        player.setVelocity(player.getVelocity().setY(1));
        switch (this.type) {
            case JUMP -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 15, 2));
            }
            case SPEED -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 2));
            }
            case BOW -> {
                ItemStack itemStack = new ItemStack(Material.BOW);
                ItemMeta itemMeta = itemStack.getItemMeta();
                ((Damageable) itemMeta).setDamage(375);
                itemStack.setItemMeta(itemMeta);
                player.getInventory().addItem(itemStack);
                player.getInventory().addItem(new ItemStack(Material.ARROW, 5));
            }
            case EXPLORER -> {
                player.getWorld().strikeLightning(player.getLocation());
                for (int i = 0; i < 15; i++) {
                    TNTPrimed tntPrimed = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
                    tntPrimed.setVelocity(new Vector(
                            tntPrimed.getVelocity().getX() + new Random().nextInt(-2 - 1 + 1) + 1,
                            tntPrimed.getVelocity().getY() + new Random().nextInt(2 - 1 + 1) + 1,
                            tntPrimed.getVelocity().getZ() + new Random().nextInt(-2 - 1 + 1) + 1));
                }
            }

            case INVISIBLE -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 15, 2));
            }

            case LEVITATION -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 5, 2));
            }
        }

        this.arena.scheduler.timerLifetime.hashMap.get(player).lifetime = this.arena.scheduler.timerLifetime.hashMap.get(player).lifetime + 1000 + new Random().nextInt(100);
        this.isSpawned = false;
        this.stand.remove();
    }
}
