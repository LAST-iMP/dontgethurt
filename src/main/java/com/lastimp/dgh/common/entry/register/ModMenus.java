package com.lastimp.dgh.common.entry.register;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.menu.BagMenu;
import com.lastimp.dgh.common.menu.HealthMenu;
import com.lastimp.dgh.common.menu.IMenuFactory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class ModMenus {
    public static final Supplier<MenuType<HealthMenu>> HEALTH_MENU = registerMenuType(HealthMenu::new, "health_menu");
    public static final Supplier<MenuType<BagMenu>> HEALTH_SMALL_BAG_MENU = registerMenuType(BagMenu.HealthSmallBag::new, "health_small_bag_menu");
    public static final Supplier<MenuType<BagMenu>> HEALTH_SMALL_MEDICINE_BAG_MENU = registerMenuType(BagMenu.MedicineSmallBag::new, "health_small_medicine_bag_menu");

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(IMenuFactory<T> factory, String name) {
        return PlatformService.REGISTRY_HANDLER.registerMenus(name, factory);
    }

    public static void register(){
    }
}
