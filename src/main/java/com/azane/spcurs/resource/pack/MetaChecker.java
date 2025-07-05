package com.azane.spcurs.resource.pack;

import com.azane.spcurs.debug.log.DebugLogger;
import net.minecraftforge.fml.ModList;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

import java.util.HashMap;

import static com.azane.spcurs.resource.pack.CustomRepoSource.PACK_MARKER;

public final class MetaChecker
{
    public static boolean checkModDependencies(PackMeta info) {
        HashMap<String, String> dependencies = info.getDependencies();
        for (String modId : dependencies.keySet()) {
            if (!isModVersionCompatible(modId, dependencies.get(modId))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isModVersionCompatible(String modId, String versionSpec) {
        try {
            VersionRange versionRange = VersionRange.createFromVersionSpec(versionSpec);
            return ModList.get().getModContainerById(modId)
                .map(mod -> {
                    ArtifactVersion modVersion = mod.getModInfo().getVersion();
                    return versionRange.containsVersion(modVersion);
                })
                .orElse(false);
        } catch (InvalidVersionSpecificationException e) {
            DebugLogger.warn(PACK_MARKER,"Invalid version specification for mod " + modId + ": " + versionSpec);
            return false;
        }
    }
}
