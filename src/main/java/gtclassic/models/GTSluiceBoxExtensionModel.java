package gtclassic.models;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import gtclassic.GTMod;
import gtclassic.util.GTValues;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.Ic2Models;
import ic2.core.platform.textures.models.BaseModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.PerspectiveMapWrapper;

public class GTSluiceBoxExtensionModel extends BaseModel {
	GTModelBaker baker;

	public GTSluiceBoxExtensionModel() {
		super(Ic2Models.getBlockTransforms());
	}

	@Override
	public void init() {
		baker = GTModelBaker.getBaker(getBakery(), Ic2Icons.getTextures(GTMod.MODID + "_sluicebox")[0]);

		// bottom main part
		baker.addModel(1, 41, 1, 0, 1, 14, 1, 15);
		// bottom sides
		baker.addModel(0, 18, 0, 0, 0, 1, 6, 16);
		baker.addModel(0, 18, 15, 0, 0, 1, 6, 16);
		// stripes
		baker.addModel(0, 59, 1, 1, 6, 14, 1, 1);
		baker.addModel(0, 59, 1, 1, 10, 14, 1, 1);
		baker.addModel(0, 59, 1, 1, 14, 14, 1, 1);

		baker.bake();
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return baker.getQuads(state == null ? EnumFacing.NORTH : state.getValue(GTValues.FACING));
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return Pair.of(this,
				PerspectiveMapWrapper.handlePerspective(this, getCamera(), cameraTransformType).getRight());
	}
}
