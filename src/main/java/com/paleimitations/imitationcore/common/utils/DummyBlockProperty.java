package com.paleimitations.imitationcore.common.utils;

import java.util.Collection;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.level.block.state.properties.Property;

public class DummyBlockProperty extends Property<Boolean> {
   protected DummyBlockProperty(String name) {
      super(name, Boolean.class);
   }

   @Override
   public Collection<Boolean> getPossibleValues() {
      return ImmutableSet.of(false);
   }

   @Override
   public Optional<Boolean> getValue(String value) {
      return Optional.of(false);
   }

   @Override
   public String getName(Boolean value) {
      return "false";
   }

   public static DummyBlockProperty create(String name) {
      return new DummyBlockProperty(name);
   }
}
