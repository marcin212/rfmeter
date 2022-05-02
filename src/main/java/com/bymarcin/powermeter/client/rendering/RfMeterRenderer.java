package com.bymarcin.powermeter.client.rendering;

import com.bymarcin.powermeter.RfMeterMod;
import com.bymarcin.powermeter.blockentity.RfMeterBlockEntity;
import com.bymarcin.powermeter.blocks.RfMeterBlock;
import com.bymarcin.powermeter.client.ClientSetup;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;

public class RfMeterRenderer implements BlockEntityRenderer<RfMeterBlockEntity> {

    public RfMeterRenderer(Context context) {
    }

    public void renderNumber(long number, boolean dot, PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay) {
        poseStack.pushPose();
        float numberOffset = 0.75f;
        for(int i=0;number != 0 || i<(dot?1:2);number = number/10,i++){
            int dig = (int) (number%10);

            Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), vertexConsumer, null, ClientSetup.MODEL_MAP.get(ClientSetup.DIGIT_TO_RL[dig]), 0f, 1f, 0f, light, overlay);
            poseStack.translate(numberOffset / 16f, 0f, 0f);

            if(i==0) {
                if(dot) {
                    //TODO dot rendering
                }
                //TODO dot offset
            }
        }
        poseStack.popPose();
    }

    public void renderUnitMultiplication(SI letter, PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay) {
        poseStack.pushPose();
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), vertexConsumer, null, ClientSetup.MODEL_MAP.get(ClientSetup.SI_TO_RL[letter.ordinal()]), 0f, 1f, 0f, light, overlay);
        poseStack.popPose();
    }

    @Override
    public void render(RfMeterBlockEntity blockEntity, float partial, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
        stack.pushPose();

        var facing = blockEntity.getBlockState().getValue(RfMeterBlock.FACING);

        stack.translate(0.5f, 0.5f, 0.5);
        switch (facing) {
            case EAST -> stack.mulPose(Vector3f.YP.rotationDegrees(270));
            case NORTH -> stack.mulPose(Vector3f.YP.rotationDegrees(0));
            case SOUTH -> stack.mulPose(Vector3f.YP.rotationDegrees(180));
            case WEST -> stack.mulPose(Vector3f.YP.rotationDegrees(90));
        }
        stack.translate(-0.5f, -0.5f, -0.5);
        VertexConsumer vertex = bufferSource.getBuffer(RenderType.solid());

        long number = blockEntity.logic.getTransfer();
        long number2 = blockEntity.logic.getCurrentValue();

        stack.pushPose();
        stack.translate(4/16f, 12.25/16f, 3/16f - 0.001f);
        renderNumber(number, true, stack, vertex, light, overlay);
        stack.popPose();

        stack.pushPose();
        stack.translate(4/16f, 9.5/16f, 3/16f - 0.001f);
        renderNumber(number2, true, stack, vertex, light, overlay);
        stack.popPose();

        stack.pushPose();
        stack.translate(4/16f, 8.25/16f, 3/16f - 0.001f);
        renderUnitMultiplication(SI.K, stack, vertex, light, overlay);
        stack.popPose();

        stack.popPose();
    }


    enum SI {
        K(26, 40),
        M(32, 40),
        G(38, 40),
        T(44, 40),
        P(50, 40),
        none(0, 0);
        int x, y, width, height;
        public static final SI[] reverse = valuesReverse();

        SI(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        SI(int x, int y) {
            this(x, y, 5, 7);
        }

        private static SI[] valuesReverse() {
            int len = SI.values().length;
            SI[] arr = new SI[len];
            for (SI si : values()) {
                arr[len - si.ordinal() - 1] = si;
            }
            return arr;
        }
    }
//
//    public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Transformation rotation, TextureAtlasSprite sprite) {
//        Vector3f normal = v3.copy();
//        normal.sub(v2);
//        Vector3f temp = v1.copy();
//        temp.sub(v2);
//        normal.cross(temp);
//        normal.normalize();
//
//        int tw = sprite.getWidth();
//        int th = sprite.getHeight();
//
//        rotation = rotation.blockCenterToCorner();
//        rotation.transformNormal(normal);
//
//        Vector4f vv1 = new Vector4f(v1); rotation.transformPosition(vv1);
//        Vector4f vv2 = new Vector4f(v2); rotation.transformPosition(vv2);
//        Vector4f vv3 = new Vector4f(v3); rotation.transformPosition(vv3);
//        Vector4f vv4 = new Vector4f(v4); rotation.transformPosition(vv4);
//
//        var builder = new BakedQuadBuilder(sprite);
//        builder.setQuadOrientation(Direction.getNearest(normal.x(), normal.y(), normal.z()));
//        putVertex(builder, normal, vv1, 0, 0, sprite);
//        putVertex(builder, normal, vv2, 0, th, sprite);
//        putVertex(builder, normal, vv3, tw, th, sprite);
//        putVertex(builder, normal, vv4, tw, 0, sprite);
//        return builder.build();
//    }
//
//    public static class CustomFaceBakery extends FaceBakery {
//        public static final CustomFaceBakery INSTANCE = new CustomFaceBakery();
//
//        public BakedQuad makeBakedQuad(Vector3f min, Vector3f max, int tintIndex,
//                                       TextureAtlasSprite icon, Direction facing, BlockModelRotation rot, boolean uvLocked) {
//            return makeBakedQuad(min, max, tintIndex, calculateUV(min, max, facing), icon, facing, rot, uvLocked);
//        }
//
//        public BakedQuad makeBakedQuad(Vector3f min, Vector3f max, int tintIndex, float[] uv,
//                                       TextureAtlasSprite icon, Direction facing, BlockModelRotation rot, boolean uvLocked) {
//            boolean hasColorIndex = tintIndex != -1 && ((tintIndex & 0xFF000000) == 0);
//            boolean hasColor = tintIndex != -1 && ((tintIndex & 0xFF000000) != 0);
//
//            ClientTools.
//
//
//                BakedQuad quad = makeBakedQuad(
//                    min, max, new BlockElementFace(null, hasColorIndex ? tintIndex : -1, "", new BlockFaceUV(uv, 0)),
//
//                    icon, facing, rot, null, null, uvLocked, true
//            );
//
//            if (hasColor) {
//                //recolorQuad(quad, tintIndex);
//            }
//
//            return quad;
//        }
//
//        public static float[] calculateUV(Vector3f from, Vector3f to, Direction facing1) {
//            Direction facing = facing1;
//            if (facing == null) {
//                if (from.y() == to.y()) {
//                    facing = Direction.UP;
//                } else if (from.x() == to.x()) {
//                    facing = Direction.EAST;
//                } else if (from.z() == to.z()) {
//                    facing = Direction.SOUTH;
//                } else {
//                    return null; // !?
//                }
//            }
//
//            switch (facing) {
//                case DOWN:
//                    return new float[] {from.x(), 16.0F - to.z(), to.x(), 16.0F - from.z()};
//                case UP:
//                    return new float[] {from.x(), from.z(), to.x(), to.z()};
//                case NORTH:
//                    return new float[] {16.0F - to.x(), 16.0F - to.y(), 16.0F - from.x(), 16.0F - from.y()};
//                case SOUTH:
//                    return new float[] {from.x(), 16.0F - to.y(), to.x(), 16.0F - from.y()};
//                case WEST:
//                    return new float[] {from.z(), 16.0F - to.y(), to.z(), 16.0F - from.y()};
//                case EAST:
//                    return new float[] {16.0F - to.z(), 16.0F - to.y(), 16.0F - from.z(), 16.0F - from.y()};
//            }
//
//            return null;
//        }
//    }
//
//
//    public List<BakedQuad> generateNumber(List<BakedQuad> quads, long number, boolean dot, float posFrom){
//        quads.clear();
//        float offset = 3.8f;
//
//        for(int i=0;number != 0 || i<(dot?1:2);number = number/10,i++){
//            long dig = number%10;
//            long x = dig *6;
//            FaceBakery a = new FaceBakery();
//            quads.add(
//
//                    CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(13.002f,posFrom,offset), new Vector3f(13.002f,posFrom+2,offset + 4.5f/4f),this.color,
//                            new float[] {x/4f, 0, (x+6)/4f, 11/4f},
//                            texture, EnumFacing.EAST, rotationOpposite, true)
//            );
//
//            offset += 4.5f/4f;
//            if(i==0){
//                if(dot){
//                    quads.add(
//                            CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(13.002f,9,offset), new Vector3f(13.002f,11,offset + 1.5f/4f),this.color,
//                                    new float[] {60f/4f, 0, 62f/4f, 11/4f},
//                                    texture, EnumFacing.EAST, rotationOpposite, true)
//                    );
//                }
//                offset += 1.5f/4f;
//            }
//        }
//
//        return quads;
//    }




    /*
    	@Override
	public void renderTileEntityFast(RFMeterTileEntity tile, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder bufferBuilder) {
		if(!(tile.getWorld().getBlockState(tile.getPos()).getBlock() instanceof RFMeterBlock)) return;
		EnumFacing facing = tile.getWorld().getBlockState(tile.getPos()).getValue(RFMeterBlock.front);
		BlockPos pos = tile.getPos();

		if(model==null) {
			model = new RFMeterModel[EnumFacing.HORIZONTALS.length][EnumDyeColor.values().length];
			for (int i = 0; i < EnumFacing.HORIZONTALS.length; i++) {
				for (int j = 0; j<EnumDyeColor.values().length; j++)
					model[i][j] = new RFMeterModel(EnumFacing.HORIZONTALS[i], EnumDyeColor.values()[j]);
			}
		}

		long total = tile.getCurrentValue();
		int unit=0;
		total *=10;
		while(Math.ceil(Math.log10(total))>6){
			unit++;
			total /=1000;
		}

		SI si = SI.reverse[unit];


		RFMeterModel m = model[facing.getHorizontalIndex()][tile.color];
		m.setNumber(total,tile.getTransfer(), si, tile.isInverted());

		bufferBuilder.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(tile.getWorld(),m, tile.getWorld().getBlockState(tile.getPos()), tile.getPos(), bufferBuilder, false);
		bufferBuilder.setTranslation(0, 0, 0);
	}



	public class RFMeterModel extends BaseBakedModel {
    List<BakedQuad> quadsBackground = new LinkedList<>();
    List<BakedQuad> quadsNumberUP = new LinkedList<>();
    List<BakedQuad> quadsNumberDown = new LinkedList<>();
    List<BakedQuad> finalQuads = new LinkedList<>();
    long numberUP;
    long numberDOWN;
    public static ResourceLocation cTexture1 = new ResourceLocation(ZettaIndustries.MODID,"blocks/counter");
    ModelRotation rotation;
    ModelRotation rotationOpposite;
    TextureAtlasSprite texture;
    BakedQuad[] si = new BakedQuad[RFMeterRender.SI.values().length - 1];
    BakedQuad[] direction = new BakedQuad[2];
    int color;
    int colorDark;


    public RFMeterModel(EnumFacing facing, EnumDyeColor color) {
        this.texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(cTexture1.toString());
        this.color = color.getColorValue();
        this.color = 0XFF000000 | ((this.color >> 16 & 255)) | ((this.color >> 8 & 255)<<8) | ((this.color & 255)<<16);
        colorDark = 0XFF000000 | ((int)((this.color >> 16 & 255)*0.3d)<<16) | ((int)((this.color >> 8 & 255)*0.3d)<<8) | ((int)((this.color & 255)*0.3d));


        switch (facing){
            case WEST: rotation = ModelRotation.X0_Y0; rotationOpposite = ModelRotation.X0_Y180; break;
            case EAST: rotation = ModelRotation.X0_Y180; rotationOpposite = ModelRotation.X0_Y0; break;
            case NORTH: rotation = ModelRotation.X0_Y90; rotationOpposite = ModelRotation.X0_Y270; break;
            case SOUTH: rotation = ModelRotation.X0_Y270; rotationOpposite = ModelRotation.X0_Y90; break;
        }

        // up LCD background
        quadsBackground.add(
                CustomFaceBakery.INSTANCE.makeBakedQuad(new Vector3f(2.999f,12,4), new Vector3f(2.999f,14,12),this.color,
                        new float[] {0, 11f/4f,
                                43f/4f, 22f/4f},
                        texture, EnumFacing.WEST, rotation, true)
        );

        // down LCD background
        quadsBackground.add(
                CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(2.999f,9,4), new Vector3f(2.999f,11,12),this.color,
                        new float[] {0, 11f/4f,
                                43f/4f, 22f/4f},
                        texture, EnumFacing.WEST, rotation, true)
        );

        // SI background
        quadsBackground.add(
                CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(2.999f,5,4), new Vector3f(2.999f,7,12),colorDark,
                        new float[] {26f/4f, 40f/4f,
                                55f/4f, 47f/4f},
                        texture, EnumFacing.WEST, rotation, true)
        );

        cacheSI();

        // Direction background
        float[] up=new float[] {56f/4f, 40f/4f, 62f/4f, 47f/4f};
        float[] down=new float[] {62f/4f, 47f/4f, 56f/4f, 40f/4f};
        direction[0] = CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(2.999f,2,7), new Vector3f(2.999f,4,9),this.color,
                down, texture, EnumFacing.WEST, rotation, false);
        direction[1] =
                CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(2.999f,2,7), new Vector3f(2.999f,4,9),this.color,
                        up, texture, EnumFacing.WEST, rotation, false);
    }


    private void cacheSI(){
        float siSize = 6f / 4f;
        float startTex = 26f / 4f;
        for (RFMeterRender.SI si : RFMeterRender.SI.values()) {
            if(si == RFMeterRender.SI.none) continue;
            float textMin = startTex + siSize * (RFMeterRender.SI.K.ordinal() - si.ordinal());
            this.si[si.ordinal()] =
                    CustomFaceBakery.INSTANCE.makeBakedQuad(
                            new Vector3f(2.998f, 5, 4 + 1.655f * (RFMeterRender.SI.K.ordinal() - si.ordinal())),
                            new Vector3f(2.998f, 7, 4 + 1.655f * (RFMeterRender.SI.K.ordinal() - si.ordinal() + 1)),
                            this.color,
                            new float[]{textMin, 40f / 4f,
                                    textMin + siSize, 47f / 4f},
                            texture, EnumFacing.WEST, rotation, true);
        }
    }

    public void setNumber(long numberUP, long numberDOWN, RFMeterRender.SI si, boolean inverted) {
        this.numberUP = numberUP;
        this.numberDOWN = numberDOWN;
        finalQuads.clear();

        generateNumber(quadsNumberUP, numberUP, false, 9);
        generateNumber(quadsNumberDown, numberDOWN, true, 12);
        if (si!= RFMeterRender.SI.none)
            finalQuads.add(this.si[si.ordinal()]);
        if (inverted)
            finalQuads.add(direction[1]);
        else
            finalQuads.add(direction[0]);

        finalQuads.addAll(quadsBackground);
        finalQuads.addAll(quadsNumberUP);
        finalQuads.addAll(quadsNumberDown);
    }

    public List<BakedQuad> generateNumber(List<BakedQuad> quads, long number, boolean dot, float posFrom){
        quads.clear();
        float offset = 3.8f;

        for(int i=0;number != 0 || i<(dot?1:2);number = number/10,i++){
            long dig = number%10;
            long x = dig *6;

            quads.add(
                    CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(13.002f,posFrom,offset), new Vector3f(13.002f,posFrom+2,offset + 4.5f/4f),this.color,
                            new float[] {x/4f, 0, (x+6)/4f, 11/4f},
                            texture, EnumFacing.EAST, rotationOpposite, true)
            );

            offset += 4.5f/4f;
            if(i==0){
                if(dot){
                    quads.add(
                            CustomFaceBakery.INSTANCE.makeBakedQuad( new Vector3f(13.002f,9,offset), new Vector3f(13.002f,11,offset + 1.5f/4f),this.color,
                                    new float[] {60f/4f, 0, 62f/4f, 11/4f},
                                    texture, EnumFacing.EAST, rotationOpposite, true)
                    );
                }
                offset += 1.5f/4f;
            }
        }

        return quads;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (side == EnumFacing.NORTH)
            return finalQuads;
        else
            return Collections.EMPTY_LIST;
    }
}
     */
}
