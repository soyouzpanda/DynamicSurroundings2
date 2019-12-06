/*
 * Dynamic Surroundings: Sound Control
 * Copyright (C) 2019  OreCruncher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package org.orecruncher.sndctrl.audio.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import org.orecruncher.lib.WorldUtils;
import org.orecruncher.sndctrl.audio.EffectRegistry;
import org.orecruncher.sndctrl.events.AudioEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public final class WorldContext {

    /**
     * Quick Minecraft reference
     */
    @Nullable
    public final Minecraft mc;
    /**
     * Reference to the client side PlayerEntity
     */
    @Nullable
    public final PlayerEntity player;
    /**
     * Reference to the player's world
     */
    @Nullable
    public final World world;
    /**
     * Position of the player.
     */
    @Nonnull
    public final Vec3d playerPosition;
    /**
     * Position of the player's eyes.
     */
    @Nonnull
    public final Vec3d playerEyePosition;
    /**
     * Block position of the player.
     */
    @Nonnull
    public final BlockPos playerPos;
    /**
     * Block position of the player's eyes.
     */
    @Nonnull
    public final BlockPos playerEyePos;
    /**
     * Flag indicating if it is precipitating
     */
    public final boolean isPrecipitating;
    /**
     * Current strength of precipitation.
     */
    public final float precipitationStrength;
    /**
     * Coefficient used for dampening sound.  Usually caused by the player's head being in lava or water.
     */
    @Nonnull
    public final float auralDampening;

    public WorldContext() {
        if (WorldUtils.isInGame()) {
            this.player = Minecraft.getInstance().player;
            this.world = Minecraft.getInstance().world;
            this.isPrecipitating = this.world.isRaining();
            this.playerPosition = this.player.getPositionVec();
            this.playerEyePosition = this.player.getEyePosition(1F);
            this.playerPos = new BlockPos(this.playerPosition);
            this.playerEyePos = new BlockPos(this.playerEyePosition);

            final Fluid fs = this.player.world.getFluidState(this.playerEyePos).getFluid();
            final ResourceLocation name = fs.getRegistryName();
            if (name != null)
                this.auralDampening = EffectRegistry.getFluidCoeffcient(name);
            else
                this.auralDampening = 0;

            // Get our current rain strength.
            final AudioEvent.PrecipitationStrengthEvent evt = new AudioEvent.PrecipitationStrengthEvent(this.world);
            MinecraftForge.EVENT_BUS.post(evt);
            this.precipitationStrength = evt.getStrength();
            this.mc = Minecraft.getInstance();
        } else {
            this.mc = null;
            this.player = null;
            this.world = null;
            this.isPrecipitating = false;
            this.playerPosition = Vec3d.ZERO;
            this.playerEyePosition = Vec3d.ZERO;
            this.playerPos = BlockPos.ZERO;
            this.playerEyePos = BlockPos.ZERO;
            this.auralDampening = 0;
            this.precipitationStrength = 0F;
        }
    }

    public boolean isNotValid() {
        return this.mc == null;
    }

    @Nullable
    public BlockRayTraceResult rayTraceBlocks(@Nonnull final Vec3d src, @Nonnull final Vec3d dest) {
        return rayTraceBlocks(src, dest, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.SOURCE_ONLY);
    }

    @Nullable
    public BlockRayTraceResult rayTraceBlocks(@Nonnull final Vec3d src, @Nonnull final Vec3d dest, @Nonnull final RayTraceContext.BlockMode bm, @Nonnull final RayTraceContext.FluidMode fm) {
        assert this.world != null;
        assert this.player != null;
        return WorldUtils.rayTraceBlock(this.world, src, dest, bm, fm);
    }

}