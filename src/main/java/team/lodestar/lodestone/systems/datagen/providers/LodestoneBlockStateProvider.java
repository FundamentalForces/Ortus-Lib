package team.lodestar.lodestone.systems.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import team.lodestar.lodestone.systems.datagen.statesmith.ModularBlockStateSmith;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public abstract class LodestoneBlockStateProvider extends BlockStateProvider {

    public final Set<ResourceLocation> staticTextures = new HashSet<>();

    private final LodestoneBlockModelProvider blockModels;
    public final LodestoneItemModelProvider itemModelProvider;

    private static String texturePath = "";

    public LodestoneBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper, LodestoneItemModelProvider itemModelProvider) {
        super(output, modid, exFileHelper);
        this.itemModelProvider = itemModelProvider;
        this.blockModels = new LodestoneBlockModelProvider(this, output, modid, exFileHelper);
    }

    @Override
    public LodestoneBlockModelProvider models() {
        return blockModels;
    }

    @Override
    public LodestoneItemModelProvider itemModels() {
        return itemModelProvider;
    }

    public void setTexturePath(String texturePath) {
        LodestoneBlockStateProvider.texturePath = texturePath;
    }

    public static String getTexturePath() {
        return texturePath;
    }

    public ModularBlockStateSmith.ModelFileSupplier fromFunction(BiFunction<String, ResourceLocation, ModelFile> modelFileFunction) {
        return b -> {
            String name = getBlockName(b);
            return modelFileFunction.apply(name, getBlockTexture(name));
        };
    }

    public void varyingRotationBlock(Block block, ModelFile model) {
        ConfiguredModel.Builder<VariantBlockStateBuilder> builder = getVariantBuilder(block).partialState().modelForState()
                .modelFile(model)
                .nextModel().modelFile(model).rotationY(90)
                .nextModel().modelFile(model).rotationY(180)
                .nextModel().modelFile(model).rotationY(270);
        simpleBlock(block, builder.build());
    }

    public ModelFile predefinedModel(Block block) {
        return models().getExistingFile(ForgeRegistries.BLOCKS.getKey(block));
    }

    public ModelFile predefinedModel(Block block, String extension) {
        return models().getExistingFile(extend(ForgeRegistries.BLOCKS.getKey(block), extension));
    }

    public ModelFile grassBlockModel(Block block) {
        String name = getBlockName(block);
        ResourceLocation side = getBlockTexture(name);
        ResourceLocation dirt = new ResourceLocation("block/dirt");
        ResourceLocation top = getBlockTexture(name + "_top");
        return models().cubeBottomTop(name, side, dirt, top);
    }

    public ModelFile leavesBlockModel(Block block) {
        String name = getBlockName(block);
        return models().withExistingParent(name, new ResourceLocation("block/leaves")).texture("all", getBlockTexture(name));
    }

    public ModelFile airModel(Block block) {
        String name = getBlockName(block);
        return models().withExistingParent(name, new ResourceLocation("block/air"));
    }

    public ModelFile cubeModelAirTexture(Block block) {
        String name = getBlockName(block);
        return models().cubeAll(name, new ResourceLocation("block/air"));
    }

    public String getBlockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    public ResourceLocation getBlockTexture(String path) {
        return modLoc("block/" + path);
    }

    public ResourceLocation getStaticBlockTexture(String path) {
        return markTextureAsStatic(getBlockTexture(path));
    }

    public ResourceLocation markTextureAsStatic(ResourceLocation texture) {
        staticTextures.add(texture);
        return texture;
    }

    //TODO: move this to some sorta ResourceLocationHelper if it ever becomes needed.
    public ResourceLocation extend(ResourceLocation resourceLocation, String suffix) {
        return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + suffix);
    }
}