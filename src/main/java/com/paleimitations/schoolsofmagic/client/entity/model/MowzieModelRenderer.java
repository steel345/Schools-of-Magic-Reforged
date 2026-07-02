package com.paleimitations.schoolsofmagic.client.entity.model;

import org.joml.Vector3f;

import net.minecraft.client.model.geom.ModelPart;

public class MowzieModelRenderer {

   public final ModelPart part;

   public float initRotateAngleX;
   public float initRotateAngleY;
   public float initRotateAngleZ;
   public float initRotationPointX;
   public float initRotationPointY;
   public float initRotationPointZ;

   public MowzieModelRenderer(ModelPart part) {
      this.part = part;
      this.setInitValuesToCurrentPose();
   }

   public float getRotateAngleX() { return part.xRot; }
   public float getRotateAngleY() { return part.yRot; }
   public float getRotateAngleZ() { return part.zRot; }
   public float getRotationPointX() { return part.x; }
   public float getRotationPointY() { return part.y; }
   public float getRotationPointZ() { return part.z; }

   public void setRotationPoint(float x, float y, float z) {
      part.x = x; part.y = y; part.z = z;
   }

   public void setRotationAngles(float x, float y, float z) {
      part.xRot = x; part.yRot = y; part.zRot = z;
   }

   public boolean isVisible() { return part.visible; }
   public void setVisible(boolean visible) { part.visible = visible; }

   public void setInitValuesToCurrentPose() {
      this.initRotateAngleX = part.xRot;
      this.initRotateAngleY = part.yRot;
      this.initRotateAngleZ = part.zRot;
      this.initRotationPointX = part.x;
      this.initRotationPointY = part.y;
      this.initRotationPointZ = part.z;
   }

   public void setCurrentPoseToInitValues() {
      part.xRot = this.initRotateAngleX;
      part.yRot = this.initRotateAngleY;
      part.zRot = this.initRotateAngleZ;
      part.x = this.initRotationPointX;
      part.y = this.initRotationPointY;
      part.z = this.initRotationPointZ;
   }

   public Vector3f getInitAngles() {
      return new Vector3f(this.initRotateAngleX, this.initRotateAngleY, this.initRotateAngleZ);
   }

   public Vector3f getInitRotPoint() {
      return new Vector3f(this.initRotationPointX, this.initRotationPointY, this.initRotationPointZ);
   }

   public Vector3f animateAnglesToLinear(Vector3f start, Vector3f change, int tick, int time) {
      float x = start.x + (float) tick * (change.x / (float) time);
      float y = start.y + (float) tick * (change.y / (float) time);
      float z = start.z + (float) tick * (change.z / (float) time);
      this.setRotationAngles(x, y, z);
      return new Vector3f(x, y, z);
   }

   public Vector3f animateAnglesToSoft(Vector3f start, Vector3f change, int tick, int time) {
      float x = (float) ((double) (-change.x) * 0.5 * (Math.cos(Math.PI * (double) (tick / time)) - 1.0) + (double) start.x);
      float y = (float) ((double) (-change.y) * 0.5 * (Math.cos(Math.PI * (double) (tick / time)) - 1.0) + (double) start.y);
      float z = (float) ((double) (-change.z) * 0.5 * (Math.cos(Math.PI * (double) (tick / time)) - 1.0) + (double) start.z);
      this.setRotationAngles(x, y, z);
      return new Vector3f(x, y, z);
   }

   public Vector3f animatePointsToLinear(Vector3f start, Vector3f change, int tick, int time) {
      float x = start.x + (float) tick * (change.x / (float) time);
      float y = start.y + (float) tick * (change.y / (float) time);
      float z = start.z + (float) tick * (change.z / (float) time);
      this.setRotationPoint(x, y, z);
      return new Vector3f(x, y, z);
   }

   public Vector3f animatePointsToSoft(Vector3f start, Vector3f change, int tick, int time) {
      float x = (float) ((double) (-change.x) * 0.5 * (Math.cos(Math.PI * (double) (tick / time)) - 1.0) + (double) start.x);
      float y = (float) ((double) (-change.y) * 0.5 * (Math.cos(Math.PI * (double) (tick / time)) - 1.0) + (double) start.y);
      float z = (float) ((double) (-change.z) * 0.5 * (Math.cos(Math.PI * (double) (tick / time)) - 1.0) + (double) start.z);
      this.setRotationPoint(x, y, z);
      return new Vector3f(x, y, z);
   }

   public void animateToInit(int tick, int time) {
      float div = (float) (time - tick);
      if (div <= 0) { setCurrentPoseToInitValues(); return; }
      part.xRot += (this.initRotateAngleX - part.xRot) / div;
      part.yRot += (this.initRotateAngleY - part.yRot) / div;
      part.zRot += (this.initRotateAngleZ - part.zRot) / div;
      part.x += (this.initRotationPointX - part.x) / div;
      part.y += (this.initRotationPointY - part.y) / div;
      part.z += (this.initRotationPointZ - part.z) / div;
      if (tick >= time) setCurrentPoseToInitValues();
   }
}
