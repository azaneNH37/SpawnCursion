package com.azane.spcurs.registry;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.block.EraserBlock;
import com.azane.spcurs.block.SpcursSpawnerBlock;
import com.azane.spcurs.block.TransformSpawnBlock;
import com.azane.spcurs.block.WrapperBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlock
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SpcursMod.MOD_ID);

    public static final ItemBlock SPAWNER = registerBlockItem("spawner", SpcursSpawnerBlock::new,new Item.Properties().stacksTo(1));
    public static final ItemBlock WRAPPER = registerBlockItem("wrapper", WrapperBlock::new);
    public static final ItemBlock ERASER = registerBlockItem("eraser", EraserBlock::new);

    public static final ItemBlock TRANSFORMER = registerBlockItem("transformer", TransformSpawnBlock::new);
    //public static final ItemBlock DEBUG_STATE = registerBlockItem("debug_state_change", StateChangeBlock::new);

    public static RegistryObject<Block> register(String name, Supplier<? extends Block> supplier)
    {
        return BLOCKS.register(name,supplier);
    }

    public static ItemBlock registerBlockItem(String name, Supplier<? extends Block> supplier,Item.Properties properties)
    {
        return new ItemBlock(name,supplier,properties);
    }
    public static ItemBlock registerBlockItem(String name, Supplier<? extends Block> supplier)
    {
        return registerBlockItem(name,supplier,new Item.Properties());
    }

    public static class ItemBlock
    {
        public final RegistryObject<Block> block;
        public final RegistryObject<Item> item;

        public ItemBlock(String name, Supplier<? extends Block> supplier, Item.Properties properties)
        {
            this.block = register(name,supplier);
            this.item = ModItem.register(name,()-> new BlockItem(this.block.get(),properties));
        }
    }
}
