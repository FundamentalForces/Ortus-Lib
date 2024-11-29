package team.lodestar.lodestone.recipe.builder;

import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import team.lodestar.lodestone.helpers.ReflectionHelper;

import java.util.Objects;

public class LodestoneShapelessRecipeBuilder extends ShapelessRecipeBuilder implements LodestoneRecipeBuilder<ShapelessRecipe> {
    public LodestoneShapelessRecipeBuilder(ShapelessRecipeBuilder parent) {
        super(parent.category, parent.getResult(), parent.count);
        ReflectionHelper.copyFields(parent, this);
    }

    @Override
    public ShapelessRecipe build(ResourceLocation id) {
        return new ShapelessRecipe(
                Objects.requireNonNullElse(this.group, ""),
                RecipeBuilder.determineBookCategory(this.category),
                new ItemStack(this.getResult()), this.ingredients
        );
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        this.ensureValid(id);
        defaultSaveFunc(recipeOutput, id);
    }
}
