package com.azane.spcurs.genable.data.sc.effects;

import com.azane.cjsop.annotation.JsonClassTypeBinder;
import com.azane.spcurs.SpcursMod;
import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.genable.data.ISpcursPlugin;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonClassTypeBinder(fullName = "efc.attr",simpleName = "attr",namespace = SpcursMod.MOD_ID)
public class EfcAttrModifier implements ISpcursPlugin
{
    @SerializedName("attr")
    private ResourceLocation attr;
    @SerializedName("op")
    private AttributeModifier.Operation op;
    @SerializedName("val")
    private double val;

    @Expose(serialize = false, deserialize = false)
    private Attribute attribute = null;
    @Expose(serialize = false, deserialize = false)
    private AttributeModifier modifier;

    public void onEntityCreate(ServerLevel level, BlockPos blockPos, LivingEntity entity)
    {
        if(modifier == null)
        {
            attribute = ForgeRegistries.ATTRIBUTES.getValue(attr);
            UUID uuid = UUID.randomUUID();
            //TODO: We need to introduce other types of Operations! Well, interesting tasks. Already have basic ideas.
            //TODO: we share the attr_modifier among all the entities created. Could be dangerous,Umh? Need Observation.
            modifier = new AttributeModifier(uuid,uuid.toString(),val,op);
        }
        if(attribute == null)
        {
            DebugLogger.error("EfcAttrModifier: Attribute %s not found, cannot apply modifier to entity %s".formatted(attr,entity.getStringUUID()));
            return;
        }
        AttributeInstance attrIns = entity.getAttribute(attribute);
        if(attrIns != null)
        {
            attrIns.addPermanentModifier(modifier);
            defaultAttrFix(entity,attribute);
        }
    }

    private void defaultAttrFix(LivingEntity entity, Attribute attribute)
    {
        if(attribute == Attributes.MAX_HEALTH)
            entity.setHealth(entity.getMaxHealth());
    }

    @Override
    public String toString()
    {
        return "EfcAttrModifier{attr: %s, op: %s, val: %f}".formatted(attr,op,val);
    }
}