package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.client.gui.menu.BagMenu;
import com.lastimp.dgh.source.client.gui.menu.HealthMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, DontGetHurt.MODID);

    public static final Supplier<MenuType<HealthMenu>> HEALTH_MENU = registerMenuType(HealthMenu::new, "health_menu");
    public static final Supplier<MenuType<BagMenu>> HEALTH_CARE_BAG_MENU = registerMenuType(BagMenu.HealthCareBag::new, "health_care_bag_menu");
    public static final Supplier<MenuType<BagMenu>> SURGERY_TOOL_BAG_MENU = registerMenuType(BagMenu.SurgeryToolBag::new, "surgery_tool_bag_menu");

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENU_TYPES.register(eventBus);
    }
}
