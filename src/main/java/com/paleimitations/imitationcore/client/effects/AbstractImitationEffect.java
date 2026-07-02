package com.paleimitations.imitationcore.client.effects;

public abstract class AbstractImitationEffect implements IImitationEffect {
   private static long id_counter = 0L;
   public final long id = id_counter++;
   protected int age;
   protected int maxAge;
   protected boolean dead;

   public AbstractImitationEffect() {
   }

   @Override
   public void setDead() {
      this.dead = true;
   }

   @Override
   public boolean isDead() {
      return this.dead;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public int getAge() {
      return this.age;
   }

   public void setMaxAge(int maxAge) {
      this.maxAge = maxAge;
   }

   public int getMaxAge() {
      return this.maxAge;
   }

   @Override
   public void update() {
      this.age++;
      if (this.age >= this.maxAge) {
         this.setDead();
      }
   }
}
