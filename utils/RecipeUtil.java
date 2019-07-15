package xyz;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.*;

import java.util.*;

public class RecipeUtil {

    public static void register(Recipe r) {
        if(r != null)
            Bukkit.addRecipe(r);
    }

    public static void removeRecipe(Recipe recipe) {
        if(recipe == null || getRecipesFor(recipe.getResult()).isEmpty())
            return;
        new Thread(() -> {
            boolean shallReturn = true;
            List<Recipe> modified = new ArrayList<>();
            Iterator<Recipe> iterator = Bukkit.recipeIterator();
            while(iterator.hasNext()) {
                Recipe next = iterator.next();
                if(!compare(next, recipe))
                    modified.add(next);
                else
                    shallReturn = false;
            }
            if(shallReturn)
                return;
            Bukkit.resetRecipes();
            List<Recipe> after = new ArrayList<>();
            Bukkit.recipeIterator().forEachRemaining(after::add);
            for(Recipe current : modified) {
                boolean shallAdd = true;
                for(int i = 0; i < after.size(); i++) {
                    if(compare(after.get(i), current)) {
                        shallAdd = false;
                        after.remove(i);
                        break;
                    }
                }
                if(shallAdd)
                    try {
                        Bukkit.addRecipe(current);
                    } catch (IllegalStateException ignored) {}
            }
        }).start();
    }

    public static void removeRecipes(List<Recipe> recipes) {
        if(recipes == null || recipes.isEmpty())
            return;
        new Thread(() -> {
            boolean shallReturn = true;
            List<Recipe> modified = new ArrayList<>();
            Iterator<Recipe> iterator = Bukkit.recipeIterator();
            while(iterator.hasNext()) {
                Recipe next = iterator.next();
                if(recipes.stream().noneMatch(recipe -> compare(next, recipe)))
                    modified.add(next);
                else
                    shallReturn = false;
            }
            if(shallReturn)
                return;
            Bukkit.resetRecipes();
            List<Recipe> after = new ArrayList<>();
            Bukkit.recipeIterator().forEachRemaining(after::add);
            for(Recipe current : modified) {
                boolean shallAdd = true;
                for(int i = 0; i < after.size(); i++) {
                    if(compare(after.get(i), current)) {
                        shallAdd = false;
                        after.remove(i);
                        break;
                    }
                }
                if(shallAdd)
                    try {
                        Bukkit.addRecipe(current);
                    } catch (IllegalStateException ignored) {}
            }
        }).start();
    }
    
    public static void removeRecipes(Recipe... recipes) {
        if(recipes.length == 0)
            return;
        removeRecipes(Arrays.asList(recipes));
    }

    public static void removeRecipesFor(ItemStack is) {
        if(is == null || is.getType() == Material.AIR)
            return;
        removeRecipes(getRecipesFor(is));
    }
    
    public static List<Recipe> getRecipesFor(ItemStack is) {
        if(is == null || is.getType() == Material.AIR)
            return Collections.emptyList();
        return Bukkit.getRecipesFor(is);
    }

    private static boolean compare(Recipe cbRcp, Recipe anyRcp) {
        if(cbRcp instanceof ShapedRecipe && anyRcp instanceof ShapedRecipe) {
            ShapedRecipe crftbukkit = (ShapedRecipe) cbRcp, any = (ShapedRecipe) anyRcp;
            Iterator<ItemStack> ingredientIterator = crftbukkit.getIngredientMap().values().iterator();
            for(char c : String.join("", any.getShape()).toCharArray()) {
                ItemStack a = (ingredientIterator.hasNext()) ? ingredientIterator.next() : null,  b = any.getIngredientMap().get(c);
                if(!(Objects.equals(a, b) || (a == null && b.getType() == Material.AIR) || (b == null && a.getType() == Material.AIR)))
                    return false;
            }
            return crftbukkit.getResult().equals(any.getResult()) && crftbukkit.getKey().equals(any.getKey());
        } else if(cbRcp instanceof ShapelessRecipe && anyRcp instanceof ShapelessRecipe) {
            ShapelessRecipe crftbukkit = (ShapelessRecipe) cbRcp, any = (ShapelessRecipe) anyRcp;
            return crftbukkit.getIngredientList().equals(any.getIngredientList()) && crftbukkit.getKey().equals(any.getKey()) && crftbukkit.getResult().equals(any.getResult());
        } else if(cbRcp instanceof FurnaceRecipe && anyRcp instanceof FurnaceRecipe) {
            FurnaceRecipe crftbukkit = (FurnaceRecipe) cbRcp, any = (FurnaceRecipe) anyRcp;
            return crftbukkit.getResult().equals(any.getResult()) && crftbukkit.getInput().equals(any.getInput());
        }
        return false;
    }

}
