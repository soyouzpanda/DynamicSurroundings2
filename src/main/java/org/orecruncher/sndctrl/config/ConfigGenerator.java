/*
 * Dynamic Surroundings
 * Copyright (C) 2020  OreCruncher
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

package org.orecruncher.sndctrl.config;

import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.forge.impl.builders.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.orecruncher.lib.config.ClothAPIFactory;
import org.orecruncher.sndctrl.library.IndividualSoundConfig;

import javax.annotation.Nonnull;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class ConfigGenerator {

    @Nonnull
    public static SubCategoryBuilder generate(@Nonnull final ConfigBuilder builder, @Nonnull final ConfigEntryBuilder entryBuilder) {

        SubCategoryBuilder modCategory = ClothAPIFactory.createSubCategory(entryBuilder, "sndctrl.modname", false);

        SubCategoryBuilder subCategory = ClothAPIFactory.createSubCategory(entryBuilder, "sndctrl.cfg.logging", false);
        subCategory.add(
                ClothAPIFactory.createBoolean(
                        builder,
                        Config.CLIENT.logging.enableLogging));

        subCategory.add(
                ClothAPIFactory.createInteger(
                        builder,
                        Config.CLIENT.logging.flagMask,
                        0,
                        Integer.MAX_VALUE));

        modCategory.add(subCategory.build());

        subCategory = ClothAPIFactory.createSubCategory(entryBuilder, "sndctrl.cfg.sound", false);
        subCategory.add(
                ClothAPIFactory.createBoolean(
                        builder,
                        Config.CLIENT.sound.enableEnhancedSounds));

        subCategory.add(
                ClothAPIFactory.createBoolean(
                        builder,
                        Config.CLIENT.sound.enableOcclusionCalcs));

        subCategory.add(
                ClothAPIFactory.createBoolean(
                        builder,
                        Config.CLIENT.sound.enableMonoConversion));

        subCategory.add(
                ClothAPIFactory.createBoolean(
                        builder,
                        Config.CLIENT.sound.enhancedWeather));

        subCategory.add(
                ClothAPIFactory.createInteger(
                        builder,
                        Config.CLIENT.sound.cullInterval,
                        0,
                        Integer.MAX_VALUE));

        subCategory.add(
                ClothAPIFactory.createIntegerSlider(
                        builder,
                        Config.CLIENT.sound.backgroundThreadWorkers,
                        0,
                        8));

        subCategory.add(
                ClothAPIFactory.createStringList(
                        builder,
                        Config.CLIENT.sound.individualSounds,
                        (v) -> {
                            if (!IndividualSoundConfig.isValid(v))
                                return Optional.of(new TranslationTextComponent("sndctrl.message.cfg.soundconfig.invalid"));
                            return Optional.empty();
                        }));

        subCategory.add(
                ClothAPIFactory.createStringList(
                        builder,
                        Config.CLIENT.sound.startupSoundList,
                        null));

        modCategory.add(subCategory.build());

        subCategory = ClothAPIFactory.createSubCategory(entryBuilder, "sndctrl.cfg.effects", false);
        subCategory.add(
                ClothAPIFactory.createBoolean(
                        builder,
                        Config.CLIENT.effects.fixupRandoms));

        subCategory.add(
                ClothAPIFactory.createIntegerSlider(
                        builder,
                        Config.CLIENT.effects.effectRange,
                        16,
                        64));

        modCategory.add(subCategory.build());

        return modCategory;
    }
}
