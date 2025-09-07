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

    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    public void getMaxStackSize(CallbackInfoReturnable<Integer> cir)
    {
        int originalStackSize = cir.getReturnValue();
        FoodProperties foodProperties = item.getFoodProperties();
        if (foodProperties != null)
        {
            int nutrition = Math.max(1, foodProperties.getNutrition());
            int stackSize = 64 / nutrition;
            int roundedStackSize = stackSize == 1 ? stackSize : Mth.roundToward(64 / nutrition, 2);
            int newStackSize = Mth.clamp(roundedStackSize, 1, 64);
            cir.setReturnValue(Math.min(originalStackSize, newStackSize));
        }
    }
}