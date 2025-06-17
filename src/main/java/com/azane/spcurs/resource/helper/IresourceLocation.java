package com.azane.spcurs.resource.helper;

import net.minecraft.resources.ResourceLocation;

public interface IresourceLocation
{
    String TAG_RL = "id";
    /**
     * 获取database的ID
     * @return ID
     */
    ResourceLocation getId();
    /**
     * 设置database的ID
     * @param id ID
     */
    void setId(ResourceLocation id);
}
