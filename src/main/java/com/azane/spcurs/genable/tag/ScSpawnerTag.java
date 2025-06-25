package com.azane.spcurs.genable.tag;

import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.resource.helper.ITagLike;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ScSpawnerTag implements ITagLike<ResourceLocation>
{
    public static final ResourceLocation DEFAULT_ID = ResourceLocation.fromNamespaceAndPath(SpcursMod.MOD_ID,"default");

    @Getter
    @SerializedName("values")
    private Set<ResourceLocation> contentList;

    @Override
    public Class<ResourceLocation> getTagType() { return ResourceLocation.class;}

    @Override
    public void absorb(ITagLike<ResourceLocation> other)
    {
        contentList.addAll(other.getContentList());
    }

    @Nullable
    public ResourceLocation getRandom()
    {
        if(contentList.isEmpty())
            return DEFAULT_ID;
        int index = (int) (Math.random() * contentList.size());
        return contentList.stream().skip(index).findFirst().orElse(null);
    }
}
