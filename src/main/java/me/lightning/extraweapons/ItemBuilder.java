package me.lightning.extraweapons;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder implements Cloneable {

    private final ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder itemName(Component name) {
        meta.itemName(name);
        return this;
    }

    public ItemBuilder lore(Component... lines) {
        meta.lore(Arrays.asList(lines));
        return this;
    }

    public ItemBuilder lore(List<Component> lines) {
        meta.lore(lines);
        return this;
    }

    public ItemBuilder appendLore(Component line) {
        List<Component> currentLore = meta.hasLore() ? new ArrayList<>(meta.lore()) : new ArrayList<>();
        currentLore.add(line);
        meta.lore(currentLore);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder removeFlags(ItemFlag... flags) {
        meta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder glow(boolean glow) {
        meta.setEnchantmentGlintOverride(glow);
        return this;
    }

    public ItemBuilder customModelData(Integer data) {
        meta.setCustomModelData(data);
        return this;
    }

    public ItemBuilder rarity(ItemRarity rarity) {
        meta.setRarity(rarity);
        return this;
    }

    public ItemBuilder fireResistant(boolean fireResistant) {
        meta.setFireResistant(fireResistant);
        return this;
    }

    public <T, Z> ItemBuilder pdc(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public ItemBuilder removePdc(NamespacedKey key) {
        meta.getPersistentDataContainer().remove(key);
        return this;
    }

    public ItemBuilder attribute(Attribute attribute, AttributeModifier modifier) {
        meta.addAttributeModifier(attribute, modifier);
        return this;
    }

    public ItemBuilder removeAttribute(Attribute attribute) {
        meta.removeAttributeModifier(attribute);
        return this;
    }

    public ItemBuilder maxStackSize(int size) {
        meta.setMaxStackSize(size);
        return this;
    }

    public ItemBuilder hideTooltip(boolean hide) {
        meta.setHideTooltip(hide);
        return this;
    }

    public ItemBuilder food(FoodComponent foodComponent) {
        meta.setFood(foodComponent);
        return this;
    }

    public ItemBuilder tool(ToolComponent toolComponent) {
        meta.setTool(toolComponent);
        return this;
    }

    public ItemBuilder jukeboxPlayable(JukeboxPlayableComponent playableComponent) {
        meta.setJukeboxPlayable(playableComponent);
        return this;
    }

    public ItemBuilder damage(int damage) {
        if (meta instanceof Damageable damageable) {
            damageable.setDamage(damage);
        }
        return this;
    }

    public ItemBuilder color(Color color) {
        if (meta instanceof LeatherArmorMeta leatherArmorMeta) {
            leatherArmorMeta.setColor(color);
        }
        return this;
    }

    public ItemBuilder armorTrim(ArmorTrim trim) {
        if (meta instanceof ArmorMeta armorMeta) {
            armorMeta.setTrim(trim);
        }
        return this;
    }

    public ItemBuilder skullProfile(PlayerProfile profile) {
        if (meta instanceof SkullMeta skullMeta) {
            skullMeta.setPlayerProfile(profile);
        }
        return this;
    }

    public ItemBuilder skullOwner(OfflinePlayer player) {
        if (meta instanceof SkullMeta skullMeta) {
            skullMeta.setOwningPlayer(player);
        }
        return this;
    }

    public ItemBuilder skullTexture(String base64) {
        if (meta instanceof SkullMeta skullMeta) {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", base64));
            skullMeta.setPlayerProfile(profile);
        }
        return this;
    }

    public ItemBuilder potionType(PotionType type) {
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.setBasePotionType(type);
        }
        return this;
    }

    public ItemBuilder potionColor(Color color) {
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.setColor(color);
            meta.removeEnchantments();
        }
        return this;
    }

    public ItemBuilder customEffect(PotionEffect effect, boolean overwrite) {
        if (meta instanceof PotionMeta potionMeta) {
            potionMeta.addCustomEffect(effect, overwrite);
        }
        return this;
    }

    public ItemBuilder maxDamage(int maxDamage) {
        ((Damageable) meta).setMaxDamage(maxDamage);
        return this;
    }

    public ItemBuilder book(Component title, String author, Component... pages) {
        if (meta instanceof BookMeta bookMeta) {
            bookMeta.title(title);
            bookMeta.setAuthor(author);
            bookMeta.addPages(pages);
        }
        return this;
    }

    public <T extends ItemMeta> ItemBuilder editMeta(Class<T> metaClass, Consumer<T> consumer) {
        if (metaClass.isInstance(meta)) {
            consumer.accept(metaClass.cast(meta));
        }
        return this;
    }

    public ItemStack build() {
        meta.addAttributeModifier(Attribute.GENERIC_LUCK, new AttributeModifier(Keys.stats,0.1, AttributeModifier.Operation.ADD_NUMBER));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemBuilder clone() {
        try {
            ItemBuilder cloned = (ItemBuilder) super.clone();
            cloned.meta = this.meta.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
