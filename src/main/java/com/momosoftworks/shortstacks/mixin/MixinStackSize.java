package com.momosoftworks.shortstacks.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinStackSize
{
    Item item = (Item) (Object) this;

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    public void getMaxStackSize(CallbackInfoReturnable<Integer> cir)
    {
        FoodProperties foodProperties = item.getFoodProperties();
        if (foodProperties != null)
        {
            int nutrition = foodProperties.getNutrition();
            int stackSize = 64 / nutrition;
            int roundedStackSize = stackSize == 1 ? stackSize : Mth.roundToward(64 / nutrition, 2);
            cir.setReturnValue(Mth.clamp(roundedStackSize, 1, 64));
        }
    }
}