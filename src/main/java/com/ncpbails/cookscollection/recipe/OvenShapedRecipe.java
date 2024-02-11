package com.ncpbails.cookscollection.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class OvenShapedRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int cookTime;
    private final boolean isSimple;
    public OvenShapedRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, int cookTime) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.cookTime = cookTime;
        this.isSimple = recipeItems.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public int getCookTime() {
        return this.cookTime;
    }

    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        for(int i = 0; i <= 3 - 3; ++i) {
            for(int j = 0; j <= 3 - 3; ++j) {
                if (this.matches(pContainer, i, j, true)) {
                    return true;
                }

                if (this.matches(pContainer, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(SimpleContainer pContainer, int numa, int numb, boolean booly) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                int k = i - numa;
                int l = j - numb;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < 3 && l < 3) {
                    if (booly) {
                        ingredient = this.recipeItems.get(3 - k - 1 + l * 3);
                    } else {
                        ingredient = this.recipeItems.get(k + l * 3);
                    }
                }

                if (!ingredient.test(pContainer.getItem(i + j * 3))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_) {
        return output;
    }

    public int getWidth() {
        return 3;
    }

    public int getRecipeWidth() {
        return getWidth();
    }

    public int getHeight() {
        return 3;
    }

    public int getRecipeHeight() {
        return getHeight();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<OvenShapedRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "baking_shaped";
    }

    static NonNullList<Ingredient> dissolvePattern(String[] p_44203_, Map<String, Ingredient> p_44204_, int p_44205_, int p_44206_) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(p_44205_ * p_44206_, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(p_44204_.keySet());
        set.remove(" ");

        for(int i = 0; i < p_44203_.length; ++i) {
            for(int j = 0; j < p_44203_[i].length(); ++j) {
                String s = p_44203_[i].substring(j, j + 1);
                Ingredient ingredient = p_44204_.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + p_44205_ * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... p_44187_) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int i1 = 0; i1 < p_44187_.length; ++i1) {
            String s = p_44187_[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (p_44187_.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[p_44187_.length - l - k];

            for(int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = p_44187_[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = this.getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter((p_151277_) -> {
            return !p_151277_.isEmpty();
        }).anyMatch((p_151273_) -> {
            return net.minecraftforge.common.ForgeHooks.hasNoElements(p_151273_);
        });
    }

    private static int firstNonSpace(String p_44185_) {
        int i;
        for(i = 0; i < p_44185_.length() && p_44185_.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String p_44201_) {
        int i;
        for(i = p_44201_.length() - 1; i >= 0 && p_44201_.charAt(i) == ' '; --i) {
        }

        return i;
    }

    static String[] patternFromJson(JsonArray p_44197_) {
        String[] astring = new String[p_44197_.size()];
        if (astring.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + 3 + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = GsonHelper.convertToString(p_44197_.get(i), "pattern[" + i + "]");
                if (s.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + 3 + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    static Map<String, Ingredient> keyFromJson(JsonObject p_44211_) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for(Map.Entry<String, JsonElement> entry : p_44211_.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static ItemStack itemStackFromJson(JsonObject p_151275_) {
        return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(p_151275_, true, true);
    }

    public static Item itemFromJson(JsonObject p_151279_) {
        String s = GsonHelper.getAsString(p_151279_, "item");
        Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + s + "'");
        });
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Invalid item: " + s);
        } else {
            return item;
        }
    }

    public static class Serializer implements RecipeSerializer<OvenShapedRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final ResourceLocation NAME = new ResourceLocation("cookscollection", "baking_shaped");
        public OvenShapedRecipe fromJson(ResourceLocation p_44236_, JsonObject json) {
            Map<String, Ingredient> map = OvenShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] astring = OvenShapedRecipe.shrink(OvenShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = OvenShapedRecipe.dissolvePattern(astring, map, i, j);
            ItemStack itemstack = OvenShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            int cookTimeIn = GsonHelper.getAsInt(json, "cooktime", 200);
            return new OvenShapedRecipe(p_44236_, itemstack, nonnulllist, cookTimeIn);
        }

        @Override
        public OvenShapedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int i = buf.readVarInt();
            int j = buf.readVarInt();
            String s = buf.readUtf();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.fromNetwork(buf));
            }

            ItemStack itemstack = buf.readItem();
            int cookTimeIn = buf.readVarInt();
            return new OvenShapedRecipe(id, itemstack, nonnulllist, cookTimeIn);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, OvenShapedRecipe recipe) {
            for(Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buf);
            }

            buf.writeItem(recipe.getResultItem());
        }
    }
}