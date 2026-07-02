package com.paleimitations.schoolsofmagic.client.entity.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class MowzieModelBase<T extends Entity> extends EntityModel<T> {

   protected final ModelPart root;
   protected List<MowzieModelRenderer> parts = new ArrayList<>();

   public MowzieModelBase(ModelPart root) {
      this.root = root;
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {
      this.root.render(pose, buf, light, overlay, r, g, b, a);
   }

   public ModelPart getRandomModelPart(Random rand) {
      return this.parts.size() > 0 ? this.parts.get(rand.nextInt(this.parts.size())).part : null;
   }

   protected void setInitPose() {
      for (MowzieModelRenderer p : this.parts) {
         p.setInitValuesToCurrentPose();
      }
   }

   public void setToInitPose() {
      for (MowzieModelRenderer p : this.parts) {
         p.setCurrentPoseToInitValues();
      }
   }

   public void addPart(MowzieModelRenderer p) {
      this.parts.add(p);
   }

   protected void addChildTo(MowzieModelRenderer child, MowzieModelRenderer parent) {

   }

   public void faceTarget(MowzieModelRenderer box, float f, float f3, float f4) {
      box.part.yRot += f3 / (180.0F / (float) Math.PI) / f;
      box.part.xRot += f4 / (180.0F / (float) Math.PI) / f;
   }

   public float rotateBox(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
      return invert
         ? -Mth.cos(f * speed + offset) * degree * f1 + weight * f1
         : Mth.cos(f * speed + offset) * degree * f1 + weight * f1;
   }

   public float spinBox(float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
      float end = (f * speed + offset) * degree * f1 + weight * f1;
      return invert ? -end : end;
   }

   public float moveBox(float speed, float degree, boolean bounce, float f, float f1) {
      return bounce
         ? -Mth.abs(Mth.sin(f * speed) * f1 * degree)
         : Mth.sin(f * speed) * f1 * degree - f1 * degree;
   }

   public void walk(MowzieModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
      int inverted = invert ? -1 : 1;
      box.part.xRot += Mth.cos(f * speed + offset) * degree * (float) inverted * f1 + weight * f1;
   }

   public void flap(MowzieModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
      int inverted = invert ? -1 : 1;
      box.part.zRot += Mth.cos(f * speed + offset) * degree * (float) inverted * f1 + weight * f1;
   }

   public void swing(MowzieModelRenderer box, float speed, float degree, boolean invert, float offset, float weight, float f, float f1) {
      int inverted = invert ? -1 : 1;
      box.part.yRot += Mth.cos(f * speed + offset) * degree * (float) inverted * f1 + weight * f1;
   }

   public void bob(MowzieModelRenderer box, float speed, float degree, boolean bounce, float f, float f1) {
      float bobAmt = (float) (Math.sin((double) (f * speed)) * (double) f1 * (double) degree - (double) (f1 * degree));
      if (bounce) {
         bobAmt = (float) (-Math.abs(Math.sin((double) (f * speed)) * (double) f1 * (double) degree));
      }
      box.part.y += bobAmt;
   }

   public void chainSwing(MowzieModelRenderer[] boxes, float speed, float degree, double rootOffset, float f, float f1) {
      int n = boxes.length;
      float offset = (float) (rootOffset * Math.PI / (double) (2 * n));
      for (int i = 0; i < n; i++) {
         boxes[i].part.yRot += Mth.cos(f * speed + offset * (float) i) * f1 * degree;
      }
   }

   public void tailSwing(MowzieModelRenderer[] boxes, float speed, float degree, double rootOffset, float frame) {
      int n = boxes.length;
      float offset = (float) (rootOffset * Math.PI / (double) (2 * n));
      for (int i = 0; i < n; i++) {
         boxes[i].part.yRot += Mth.cos(frame * speed + offset * (float) i) * degree;
      }
   }

   public void chainWave(MowzieModelRenderer[] boxes, float speed, float degree, double rootOffset, float f, float f1) {
      int n = boxes.length;
      float offset = (float) (rootOffset * Math.PI / (double) (2 * n));
      for (int i = 0; i < n; i++) {
         boxes[i].part.xRot += Mth.cos(f * speed + offset * (float) i) * f1 * degree;
      }
   }

   public void chainFlap(MowzieModelRenderer[] boxes, float speed, float degree, double rootOffset, float f, float f1) {
      int n = boxes.length;
      float offset = (float) (rootOffset * Math.PI / (double) (2 * n));
      for (int i = 0; i < n; i++) {
         boxes[i].part.zRot += Mth.cos(f * speed + offset * (float) i) * f1 * degree;
      }
   }

   protected void rotateTo(MowzieModelRenderer rotating, MowzieModelRenderer to, float t) {
      float rotXDif = to.part.xRot - rotating.part.xRot;
      float rotYDif = to.part.yRot - rotating.part.yRot;
      float rotZDif = to.part.zRot - rotating.part.zRot;
      float posXDif = to.part.x - rotating.part.x;
      float posYDif = to.part.y - rotating.part.y;
      float posZDif = to.part.z - rotating.part.z;
      rotating.part.xRot += rotXDif / 20.0F * t;
      rotating.part.yRot += rotYDif / 20.0F * t;
      rotating.part.zRot += rotZDif / 20.0F * t;
      rotating.part.x += posXDif / 20.0F * t;
      rotating.part.y += posYDif / 20.0F * t;
      rotating.part.z += posZDif / 20.0F * t;
   }
}
