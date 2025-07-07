package com.azane.spcurs.resource.pack;

import com.azane.spcurs.debug.log.DebugLogger;
import com.azane.spcurs.resource.service.IResourceProvider;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import cpw.mods.jarhandling.SecureJar;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.resource.DelegatingPackResources;
import net.minecraftforge.resource.PathPackResources;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.azane.spcurs.resource.pack.MetaChecker.checkModDependencies;

public enum CustomRepoSource implements RepositorySource
{
    INSTANCE;

    public static final Marker PACK_MARKER = MarkerManager.getMarker("PackLoader");

    private static final String PACK_META_FILE = "spcurs.pack.json";
    private static final String PACK_DIRECTORY = "spcurs";

    public PackType packType;

    @Override
    public void loadPacks(Consumer<Pack> packConsumer) {
        Pack spCursPack = discoverSpCursPacks();
        if (spCursPack != null) {
            packConsumer.accept(spCursPack);
        }
    }

    public Pack discoverSpCursPacks() {
        Path spCursPath = FMLPaths.GAMEDIR.get().resolve(PACK_DIRECTORY);
        File folder = spCursPath.toFile();

        // 确保目录存在
        if (!folder.isDirectory()) {
            try {
                Files.createDirectories(folder.toPath());
                DebugLogger.info(PACK_MARKER,"Created SpCurs directory: " + spCursPath);
            } catch (Exception e) {
                DebugLogger.error(PACK_MARKER,"Failed to create SpCurs directory: " + spCursPath, e);
                return null;
            }
        }

        List<SpCursPack> spCursPacks = scanSpCursPacks(spCursPath);
        if (spCursPacks.isEmpty()) {
            DebugLogger.info(PACK_MARKER,"No SpCurs packs found in directory: " + spCursPath);
            return null;
        }

        List<PathPackResources> packResources = new ArrayList<>();

        for (SpCursPack spCursPack : spCursPacks) {
            PathPackResources packResource = createPackResource(spCursPack);
            if (packResource != null) {
                packResources.add(packResource);
                DebugLogger.info(PACK_MARKER,"Loaded SpCurs pack: " + spCursPack.name());
            }
        }

        if (packResources.isEmpty()) {
            return null;
        }

        return Pack.readMetaAndCreate(
            "spcurs_resources",
            Component.literal("SpCurs Resources"),
            true,
            (id) -> new DelegatingPackResources(
                id,
                false,
                new PackMetadataSection(
                    Component.translatable("spcurs.resources.modresources"),
                    SharedConstants.getCurrentVersion().getPackVersion(packType)
                ),
                packResources
            ) {
                public @Nullable IoSupplier<InputStream> getRootResource(String... paths) {
                    //if (paths.length == 1 && paths[0].equals("pack.png")) {
                    //    Path logoPath = getModIcon(SpcursMod.MOD_ID);
                    //    if (logoPath != null) {
                     //       return IoSupplier.create(logoPath);
                     //   }
                    //}
                    return null;
                }
            },
            packType,
            Pack.Position.BOTTOM,
            PackSource.DEFAULT
        );
    }

    private PathPackResources createPackResource(SpCursPack spCursPack) {
        try {
            return new PathPackResources(spCursPack.name(), false,spCursPack.path()) {
                private final SecureJar secureJar = SecureJar.from(spCursPack.path());

                @NotNull
                protected Path resolve(String... paths) {
                    if (paths.length < 1) {
                        throw new IllegalArgumentException("Missing path");
                    }
                    return this.secureJar.getPath(String.join("/", paths));
                }

                public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
                    return super.getResource(type, location);
                }

                public void listResources(PackType type, String namespace, String path, PackResources.ResourceOutput resourceOutput) {
                    super.listResources(type, namespace, path, resourceOutput);
                }
            };
        } catch (Exception e) {
            DebugLogger.error(PACK_MARKER,"Failed to create pack resource for: " + spCursPack.name(), e);
            return null;
        }
    }

    private static List<SpCursPack> scanSpCursPacks(Path spCursPath) {
        List<SpCursPack> spCursPacks = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(spCursPath)) {
            for (Path entry : stream) {
                SpCursPack spCursPack = null;

                if (Files.isDirectory(entry)) {
                    spCursPack = fromDirectoryPath(entry);
                } else if (entry.toString().toLowerCase().endsWith(".zip")) {
                    spCursPack = fromZipPath(entry);
                }

                if (spCursPack != null) {
                    spCursPacks.add(spCursPack);
                }
            }
        } catch (IOException e) {
            DebugLogger.error(PACK_MARKER,"Failed to scan SpCurs packs from: " + spCursPath, e);
        }

        return spCursPacks;
    }

    private static SpCursPack fromDirectoryPath(Path path) {
        Path packMetaPath = path.resolve(PACK_META_FILE);

        if (!Files.exists(packMetaPath)) {
            // 如果没有元数据文件，使用目录名作为包名
            String packName = path.getFileName().toString();
            DebugLogger.info(PACK_MARKER,"No meta file found for pack: " + packName + ", using directory name");
            return new SpCursPack(path, packName);
        }

        try (InputStream stream = Files.newInputStream(packMetaPath)) {
            PackMeta info = IResourceProvider.GSON.fromJson(
                new InputStreamReader(stream, StandardCharsets.UTF_8),
                PackMeta.class
            );

            if (info == null) {
                DebugLogger.warn(PACK_MARKER,"Failed to read pack meta: " + packMetaPath);
                return new SpCursPack(path, path.getFileName().toString());
            }

            // 检查模组版本依赖
            if (info.getDependencies() != null && ! checkModDependencies(info)) {
                DebugLogger.warn(PACK_MARKER,"Mod dependencies not met for pack: " + path.getFileName());
                return null;
            }

            return new SpCursPack(path, info.getName());

        } catch (IOException | JsonSyntaxException | JsonIOException e) {
            DebugLogger.warn(PACK_MARKER,"Failed to read pack meta: " + packMetaPath + ", using directory name");
            return new SpCursPack(path, path.getFileName().toString());
        }
    }

    private static SpCursPack fromZipPath(Path path) {
        try (ZipFile zipFile = new ZipFile(path.toFile())) {
            ZipEntry metaEntry = zipFile.getEntry(PACK_META_FILE);

            if (metaEntry == null) {
                // 如果没有元数据文件，使用文件名作为包名
                String packName = path.getFileName().toString().replaceAll("\\.zip$", "");
                DebugLogger.info(PACK_MARKER,"No meta file found in ZIP: " + packName + ", using filename");
                return new SpCursPack(path, packName);
            }

            try (InputStream stream = zipFile.getInputStream(metaEntry)) {
                PackMeta info = IResourceProvider.GSON.fromJson(
                    new InputStreamReader(stream, StandardCharsets.UTF_8),
                    PackMeta.class
                );

                if (info == null) {
                    DebugLogger.warn(PACK_MARKER,"Failed to read pack meta from ZIP: " + path);
                    return new SpCursPack(path, path.getFileName().toString().replaceAll("\\.zip$", ""));
                }

                // 检查模组版本依赖
                if (info.getDependencies() != null && !checkModDependencies(info)) {
                    DebugLogger.warn(PACK_MARKER,"Mod dependencies not met for ZIP pack: " + path.getFileName());
                    return null;
                }

                return new SpCursPack(path, info.getName());

            } catch (IOException | JsonSyntaxException | JsonIOException e) {
                DebugLogger.warn(PACK_MARKER,"Failed to read pack meta from ZIP: " + path + ", using filename");
                return new SpCursPack(path, path.getFileName().toString().replaceAll("\\.zip$", ""));
            }
        } catch (IOException e) {
            DebugLogger.error("Failed to load pack from ZIP: " + path, e);
            return null;
        }
    }

    /*
    @Nullable
    private static Path getModIcon(String modId) {
        Optional<? extends ModContainer> modContainer = ModList.get().getModContainerById(modId);
        if (modContainer.isPresent()) {
            IModInfo mod = modContainer.get().getModInfo();
            IModFile file = mod.getOwningFile().getFile();
            if (file != null) {
                Path logoPath = file.findResource("icon.png");
                if (Files.exists(logoPath)) {
                    return logoPath;
                }
            }
        }
        return null;
    }
     */

    public record SpCursPack(Path path, String name) {
    }
}