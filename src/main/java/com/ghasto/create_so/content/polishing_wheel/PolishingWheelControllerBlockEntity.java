package com.ghasto.create_so.content.polishing_wheel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.ghasto.create_so.ModRecipeTypes;
import com.simibubi.create.AllDamageTypes;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import io.github.fabricators_of_create.porting_lib.util.NBTSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PolishingWheelControllerBlockEntity extends SmartBlockEntity implements SidedStorageBlockEntity {

	public Entity processingEntity;
	private UUID entityUUID;
	protected boolean searchForEntity;

	public ProcessingInventory inventory;
	public float crushingspeed;

	public PolishingWheelControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		inventory = new ProcessingInventory(this::itemInserted) {

			@Override
			public boolean isItemValid(int slot, ItemVariant stack) {
				return super.isItemValid(slot, stack) && processingEntity == null;
			}

		};
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(new DirectBeltInputBehaviour(this).onlyInsertWhen(this::supportsDirectBeltInput));
	}

	private boolean supportsDirectBeltInput(Direction side) {
		BlockState blockState = getBlockState();
		if (blockState == null)
			return false;
		Direction direction = blockState.getValue(PolishingWheelControllerBlock.FACING);
		return direction == Direction.DOWN || direction == side;
	}

	@Override
	public void tick() {
		super.tick();
		if (searchForEntity) {
			searchForEntity = false;
			List<Entity> search = level.getEntities((Entity) null, new AABB(getBlockPos()),
					e -> entityUUID.equals(e.getUUID()));
			if (search.isEmpty())
				clear();
			else
				processingEntity = search.get(0);
		}

		if (!isOccupied())
			return;
		if (crushingspeed == 0)
			return;

		if (level.isClientSide)
			EnvExecutor.runWhenOn(EnvType.CLIENT, () -> () -> this.tickAudio());

		float speed = crushingspeed * 4;

		Vec3 centerPos = VecHelper.getCenterOf(worldPosition);
		Direction facing = getBlockState().getValue(PolishingWheelControllerBlock.FACING);
		int offset = facing.getAxisDirection()
				.getStep();
		Vec3 outSpeed = new Vec3((facing.getAxis() == Direction.Axis.X ? 0.25D : 0.0D) * offset,
				offset == 1 ? (facing.getAxis() == Direction.Axis.Y ? 0.5D : 0.0D) : 0.0D // Increased upwards speed so upwards
				// crushing wheels shoot out the item
				// properly.
				, (facing.getAxis() == Direction.Axis.Z ? 0.25D : 0.0D) * offset); // No downwards speed, so downwards crushing wheels
		// drop the items as before.
		Vec3 outPos = centerPos.add((facing.getAxis() == Direction.Axis.X ? .55f * offset : 0f),
				(facing.getAxis() == Direction.Axis.Y ? .55f * offset : 0f), (facing.getAxis() == Direction.Axis.Z ? .55f * offset : 0f));

		if (!hasEntity()) {

			float processingSpeed =
					Mth.clamp((speed) / (!inventory.appliedRecipe ? Mth.log2(inventory.getStackInSlot(0)
							.getCount()) : 1), .25f, 20);
			inventory.remainingTime -= processingSpeed;
			spawnParticles(inventory.getStackInSlot(0));

			if (level.isClientSide)
				return;

			if (inventory.remainingTime < 20 && !inventory.appliedRecipe) {
				applyRecipe();
				inventory.appliedRecipe = true;
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2 | 16);
				return;
			}

			if (inventory.remainingTime > 0) {
				return;
			}
			inventory.remainingTime = 0;

			// Output Items
			if (facing != Direction.UP) {
				BlockPos nextPos = worldPosition.offset(facing.getAxis() == Direction.Axis.X ? 1 * offset : 0, -1,
						facing.getAxis() == Direction.Axis.Z ? 1 * offset : 0);
				DirectBeltInputBehaviour behaviour =
						BlockEntityBehaviour.get(level, nextPos, DirectBeltInputBehaviour.TYPE);
				if (behaviour != null) {
					boolean changed = false;
					if (!behaviour.canInsertFromSide(facing))
						return;
					for (int slot = 0; slot < inventory.getSlotCount(); slot++) {
						ItemStack stack = inventory.getStackInSlot(slot);
						if (stack.isEmpty())
							continue;
						ItemStack remainder = behaviour.handleInsertion(stack, facing, false);
						if (ItemStack.matches(stack, remainder))
							continue;
						inventory.setStackInSlot(slot, remainder);
						changed = true;
					}
					if (changed) {
						setChanged();
						sendData();
					}
					return;
				}
			}

			// Eject Items
			for (int slot = 0; slot < inventory.getSlotCount(); slot++) {
				ItemStack stack = inventory.getStackInSlot(slot);
				if (stack.isEmpty())
					continue;
				ItemEntity entityIn = new ItemEntity(level, outPos.x, outPos.y, outPos.z, stack);
				entityIn.setDeltaMovement(outSpeed);
				level.addFreshEntity(entityIn);
			}
			inventory.clear();
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2 | 16);

			return;
		}

		if (!processingEntity.isAlive() || !processingEntity.getBoundingBox()
				.intersects(new AABB(worldPosition).inflate(.5f))) {
			clear();
			return;
		}

		double xMotion = ((worldPosition.getX() + .5f) - processingEntity.getX()) / 2f;
		double zMotion = ((worldPosition.getZ() + .5f) - processingEntity.getZ()) / 2f;
		if (processingEntity.isShiftKeyDown())
			xMotion = zMotion = 0;
		double movement = Math.max(-speed / 4f, -.5f) * -offset;
		processingEntity.setDeltaMovement(
				new Vec3(facing.getAxis() == Direction.Axis.X ? movement : xMotion, facing.getAxis() == Direction.Axis.Y ? movement : 0f // Do
						// not
						// move
						// entities
						// upwards
						// or
						// downwards
						// for
						// horizontal
						// crushers,
						, facing.getAxis() == Direction.Axis.Z ? movement : zMotion)); // Or they'll only get their feet crushed.

		if (level.isClientSide)
			return;

		if (!(processingEntity instanceof ItemEntity)) {
			Vec3 entityOutPos = outPos.add(facing.getAxis() == Direction.Axis.X ? .5f * offset : 0f,
					facing.getAxis() == Direction.Axis.Y ? .5f * offset : 0f, facing.getAxis() == Direction.Axis.Z ? .5f * offset : 0f);
			int crusherDamage = AllConfigs.server().kinetics.crushingDamage.get();

			if (processingEntity instanceof LivingEntity) {
				if ((((LivingEntity) processingEntity).getHealth() - crusherDamage <= 0) // Takes LivingEntity instances
						// as exception, so it can
						// move them before it would
						// kill them.
						&& (((LivingEntity) processingEntity).hurtTime <= 0)) { // This way it can actually output the items
					// to the right spot.
					processingEntity.setPos(entityOutPos.x, entityOutPos.y, entityOutPos.z);
				}
			}
			processingEntity.hurt(AllDamageTypes.CRUSH.source(level), crusherDamage);
			if (!processingEntity.isAlive()) {
				processingEntity.setPos(entityOutPos.x, entityOutPos.y, entityOutPos.z);
			}
			return;
		}

		ItemEntity itemEntity = (ItemEntity) processingEntity;
		itemEntity.setPickUpDelay(20);
		if (facing.getAxis() == Direction.Axis.Y) {
			if (processingEntity.getY() * -offset < (centerPos.y - .25f) * -offset) {
				intakeItem(itemEntity);
			}
		} else if (facing.getAxis() == Direction.Axis.Z) {
			if (processingEntity.getZ() * -offset < (centerPos.z - .25f) * -offset) {
				intakeItem(itemEntity);
			}
		} else {
			if (processingEntity.getX() * -offset < (centerPos.x - .25f) * -offset) {
				intakeItem(itemEntity);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public void tickAudio() {
		float pitch = Mth.clamp((crushingspeed / 256f) + .45f, .85f, 1f);
		if (entityUUID == null && inventory.getStackInSlot(0)
				.isEmpty())
			return;
		SoundScapes.play(SoundScapes.AmbienceGroup.CRUSHING, worldPosition, pitch);
	}

	private void intakeItem(ItemEntity itemEntity) {
		inventory.clear();
		inventory.setStackInSlot(0, itemEntity.getItem()
				.copy());
		itemInserted(inventory.getStackInSlot(0));
		itemEntity.discard();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2 | 16);
	}

	protected void spawnParticles(ItemStack stack) {
		if (stack == null || stack.isEmpty())
			return;

		ParticleOptions particleData = null;
		if (stack.getItem() instanceof BlockItem)
			particleData = new BlockParticleOption(ParticleTypes.BLOCK, ((BlockItem) stack.getItem()).getBlock()
					.defaultBlockState());
		else
			particleData = new ItemParticleOption(ParticleTypes.ITEM, stack);

		RandomSource r = level.random;
		for (int i = 0; i < 4; i++)
			level.addParticle(particleData, worldPosition.getX() + r.nextFloat(), worldPosition.getY() + r.nextFloat(),
					worldPosition.getZ() + r.nextFloat(), 0, 0, 0);
	}

	private void applyRecipe() {
		Optional<ProcessingRecipe<Container>> recipe = findRecipe();

		List<ItemStack> list = new ArrayList<>();
		if (recipe.isPresent()) {
			int rolls = inventory.getStackInSlot(0)
					.getCount();
			inventory.clear();
			for (int roll = 0; roll < rolls; roll++) {
				List<ItemStack> rolledResults = recipe.get()
						.rollResults();
				for (int i = 0; i < rolledResults.size(); i++) {
					ItemStack stack = rolledResults.get(i);
					ItemHelper.addToList(stack, list);
				}
			}
			for (int slot = 0; slot < list.size() && slot + 1 < inventory.getSlotCount(); slot++)
				inventory.setStackInSlot(slot + 1, list.get(slot));
		} else {
			inventory.clear();
		}

	}

	public Optional<ProcessingRecipe<Container>> findRecipe() {
		return ModRecipeTypes.POLISHING.find(inventory, level);
	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		if (hasEntity())
			compound.put("Entity", NbtUtils.createUUID(entityUUID));
		compound.put("Inventory", NBTSerializer.serializeNBT(inventory));
		compound.putFloat("Speed", crushingspeed);
		super.write(compound, clientPacket);
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		if (compound.contains("Entity") && !isOccupied()) {
			entityUUID = NbtUtils.loadUUID(NBTHelper.getINBT(compound, "Entity"));
			this.searchForEntity = true;
		}
		crushingspeed = compound.getFloat("Speed");
		inventory.deserializeNBT(compound.getCompound("Inventory"));
	}

	public void startCrushing(Entity entity) {
		processingEntity = entity;
		entityUUID = entity.getUUID();
	}

	private void itemInserted(ItemStack stack) {
		Optional<ProcessingRecipe<Container>> recipe = findRecipe();
		inventory.remainingTime = recipe.isPresent() ? recipe.get()
				.getProcessingDuration() : 100;
		inventory.appliedRecipe = false;
	}

	@Nullable
	@Override
	public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
		return inventory;
	}

	public void clear() {
		processingEntity = null;
		entityUUID = null;
	}

	public boolean isOccupied() {
		return hasEntity() || !inventory.isEmpty();
	}

	public boolean hasEntity() {
		return processingEntity != null;
	}

}
