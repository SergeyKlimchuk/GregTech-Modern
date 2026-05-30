package com.gregtechceu.gtceu.common.machine.trait.customlogic;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidUtil;

import org.jetbrains.annotations.Nullable;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.STEAM_BOILER_RECIPES;

public enum SteamBoilerLogic implements GTRecipeType.ICustomRecipeLogic {

    INSTANCE;

    @Override
    public @Nullable GTRecipe createCustomRecipe(IRecipeCapabilityHolder holder) {
        var recipeHandlers = holder.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP);
        for (var handler : recipeHandlers) {
            for (var content : handler.getContents()) {
                if (!(content instanceof ItemStack stack)) continue;
                if (stack.isEmpty()) continue;
                if (FluidUtil.getFluidContained(stack).isPresent()) continue;
                var burnTime = GTUtil.getItemBurnTime(stack.getItem());
                if (burnTime > 0) {
                    return STEAM_BOILER_RECIPES.recipeBuilder(BuiltInRegistries.ITEM.getKey(stack.getItem()))
                            .inputItems(stack.copyWithCount(1))
                            .duration(burnTime)
                            .build();
                }
            }
        }
        return null;
    }
}
