package team.lodestar.lodestone.systems.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec2;

import java.util.List;

public record Face(List<Vertex> vertices) {
    public void renderFace(PoseStack poseStack, RenderType renderType, int packedLight) {
        MultiBufferSource.BufferSource mcBufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer buffer = mcBufferSource.getBuffer(renderType);
        int vertexCount = vertices.size();

        if (vertexCount == 4) renderQuad(poseStack, buffer, packedLight);
        else if (vertexCount == 3) renderTriangle(poseStack, buffer, packedLight);
        else throw new RuntimeException("Face has invalid number of vertices. Supported vertex counts are 3 and 4.");
    }
    public void renderTriangle(PoseStack poseStack, VertexConsumer buffer, int packedLight) {
        this.vertices().forEach(vertex -> addVertex(buffer, vertex, poseStack, packedLight));
        addVertex(buffer, this.vertices().get(0), poseStack, packedLight);
    }

    public void renderQuad(PoseStack poseStack, VertexConsumer buffer, int packedLight) {
        this.vertices().forEach(vertex -> addVertex(buffer, vertex, poseStack, packedLight));
    }

    private void addVertex(VertexConsumer buffer, Vertex vertex, PoseStack poseStack, int packedLight) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();

        Vector3f position = vertex.position();
        Vector3f normal = vertex.normal();
        Vec2 uv = vertex.uv();

        buffer.vertex(matrix4f, position.x(), position.y(), position.z())
                .color(255, 255, 255, 255)
                .uv(uv.x, -uv.y)
                .uv2(packedLight)
                .normal(normalMatrix, normal.x(), normal.y(), normal.z())
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .endVertex();
    }

    public Vector3f getCentroid() {
        Vector3f centroid = new Vector3f();
        for (Vertex vertex : vertices) centroid.add(vertex.position());
        centroid.mul(1f/vertices.size()); // centroid.div(vertices.size()); ImplNote: same functionality
        return centroid;
    }
}