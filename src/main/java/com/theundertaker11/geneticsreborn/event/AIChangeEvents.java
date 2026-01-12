package com.theundertaker11.geneticsreborn.event;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AIChangeEvents {

	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event) {		
		Entity e = event.getEntity();
		if (e instanceof EntityPlayer) {
			AbstractAttributeMap map = ((EntityPlayer)e).getAttributeMap();
			if (map.getAttributeInstance(GeneticsReborn.CLIMBING_ATT) == null)
				map.registerAttribute(GeneticsReborn.CLIMBING_ATT);
			if (map.getAttributeInstance(GeneticsReborn.EFFICIENCY_ATT) == null)
				map.registerAttribute(GeneticsReborn.EFFICIENCY_ATT);
		}

		if (e instanceof EntityCreeper) attachScareTask((EntityCreature) event.getEntity(), this::hasCreeperGene);
		if (e instanceof EntityZombie) attachScareTask((EntityCreature) event.getEntity(), this::hasZombieGene);
		if (e instanceof EntitySkeleton) attachScareTask((EntityCreature) event.getEntity(), this::hasSkeletonGene);
		if (e instanceof EntitySpider) attachScareTask((EntityCreature) event.getEntity(), this::hasSpiderGene);
	}

	private boolean hasScareGene(@Nullable EntityPlayer player, EnumGenes gene) {
		return ModUtils.getIGenes(player) != null && ModUtils.getIGenes(player).hasGene(gene);
	}
	
	private boolean hasCreeperGene(@Nullable EntityPlayer player) {
		return hasScareGene(player, EnumGenes.SCARE_CREEPERS);
	}

	private boolean hasSkeletonGene(@Nullable EntityPlayer player) {
		return hasScareGene(player, EnumGenes.SCARE_SKELETONS);
	}

	private boolean hasZombieGene(@Nullable EntityPlayer player) {
		return hasScareGene(player, EnumGenes.SCARE_ZOMBIES);
	}

	private boolean hasSpiderGene(@Nullable EntityPlayer player) {
		return hasScareGene(player, EnumGenes.SCARE_SPIDERS);
	}

    public void attachScareTask(EntityCreature entity, Predicate<? super EntityPlayer> predicate) {
        if (entity instanceof EntityCreeper) {
            entity.tasks.addTask(3, new EntityAIAvoidEntity<>(entity, EntityPlayer.class, predicate, 6.0F, 1.0D, 1.2D));
            for (EntityAITaskEntry a : entity.targetTasks.taskEntries) {
                EntityAIBase ai = a.action;
                if (ai instanceof EntityAINearestAttackableTarget && a.priority == 1) {
                    entity.targetTasks.removeTask(ai);
                    entity.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(entity, EntityPlayer.class, 10, true, false, player -> !predicate.test(player)));
                    break;
                }
            }
        } else if (entity instanceof EntityPigZombie) {
            for (EntityAITaskEntry a : entity.targetTasks.taskEntries) {
                EntityAIBase ai = a.action;
                if (ai instanceof EntityAINearestAttackableTarget && a.priority == 2) {
                    entity.targetTasks.removeTask(ai);
                    entity.targetTasks.addTask(2, new AIChangeEvents.AITargetAggressor<>(entity, EntityPlayer.class, 10, true, false, predicate));
                    break;
                }
            }
        } else if (entity instanceof EntitySpider) {
            for (EntityAITaskEntry a : entity.targetTasks.taskEntries) {
                EntityAIBase ai = a.action;
                if (ai instanceof EntityAINearestAttackableTarget && a.priority == 2) {
                    entity.targetTasks.removeTask(ai);
                    entity.targetTasks.addTask(2, new AIChangeEvents.AISpiderTarget<>(entity, EntityPlayer.class, 10, true, false, player -> !predicate.test(player)));
                    break;
                }
            }
        } else {
            for (EntityAITaskEntry a : entity.targetTasks.taskEntries) {
                EntityAIBase ai = a.action;
                if (ai instanceof EntityAINearestAttackableTarget && a.priority == 2) {
                    entity.targetTasks.removeTask(ai);
                    entity.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(entity, EntityPlayer.class, 10, true, false, player -> !predicate.test(player)));
                    break;
                }
            }
        }
    }
	
	static class AITargetAggressor<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
        
        public AITargetAggressor(EntityCreature creature, Class<T> classTarget, int chance, boolean checkSight,	boolean onlyNearby, Predicate<? super T> targetSelector) {
			super(creature, classTarget, chance, checkSight, onlyNearby, targetSelector);
		}

		public boolean shouldExecute() {
            return ((EntityPigZombie)this.taskOwner).isAngry() && super.shouldExecute();
        }
    }

	static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {
		public AISpiderTarget(EntityCreature creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, Predicate<? super T> targetSelector) {
			super(creature, classTarget, chance, checkSight, onlyNearby, targetSelector);
		}

		public boolean shouldExecute() {
			float f = this.taskOwner.getBrightness();
			return !(f >= 0.5F) && super.shouldExecute();
		}
	}
}
