package com.mrfortuna.morsecraft.blocks;

import com.mojang.logging.LogUtils;
import com.mrfortuna.morsecraft.Morsecraft;
import com.mrfortuna.morsecraft.gui.TelegraphKeyContainerProvider;
import com.mrfortuna.morsecraft.gui.TelegraphKeyMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class TelegraphKeyBlock extends BaseEntityBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int DOT_TICKS = 4;
    private static final int DASH_TICKS = 12;
    private static final int SYMBOL_TIMEOUT = 120;

    public TelegraphKeyBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TelegraphKeyBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TelegraphKeyBlockEntity telegraph) {
                NetworkHooks.openScreen((ServerPlayer) player,
                        new SimpleMenuProvider(
                                (windowId, inv, p) -> new TelegraphKeyMenu(windowId, inv, telegraph),
                                Component.translatable("screen.morsecraft.telegraph_key")
                        ),
                        buf -> buf.writeBlockPos(pos) // 👈 обедать
                );
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }





    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, Boolean.FALSE), 3);
            level.updateNeighborsAt(pos, this);
        }

        if (level.getBlockEntity(pos) instanceof TelegraphKeyBlockEntity te) {
            long last = te.getLastSignalGameTime();
            if (last > 0) {
                long now = level.getServer().getTickCount();
                if (now - last >= SYMBOL_TIMEOUT) {
                    if (!te.getSequence().isEmpty()) {
                        LOGGER.info("[morsecraft] Telegraphed sequence cleared at {}: {}", pos, te.getSequence());
                    }
                    te.clearSequence();
                    te.setLastSignalGameTime(0L);
                    te.setChanged();
                }
            }
        }
    }

    public void insertTape(ServerLevel level, BlockPos pos, ItemStack tape) {
        if (level.getBlockEntity(pos) instanceof TelegraphKeyBlockEntity te) {
            te.setTape(tape);
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return state.getValue(POWERED);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasTag()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TelegraphKeyBlockEntity telegraph) {
                telegraph.addPaper(stack.getOrCreateTag().getInt("Paper"));
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TelegraphKeyBlockEntity te) {
            ItemStack stack = new ItemStack(this.asItem());

            stack.getOrCreateTag().putInt("Paper", te.getPaperCount());

            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);

            // Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.PAPER, te.getPaperCount()));
        }
        super.playerWillDestroy(level, pos, state, player);
    }
}
