package com.mrfortuna.morsecraft.blocks;

import com.mrfortuna.morsecraft.Morsecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TelegraphKeyBlockEntity extends BlockEntity {

    private int paperCount = 0;
    private final StringBuilder sequence = new StringBuilder();
    private long lastSignalGameTime = 0L;

    private int printedSymbols = 0;

    private ItemStack tape = ItemStack.EMPTY;

    public TelegraphKeyBlockEntity(BlockPos pos, BlockState state) {
        super(Morsecraft.TELEGRAPH_KEY_ENTITY.get(), pos, state);
    }

    public static TelegraphKeyBlockEntity get(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        return (be instanceof TelegraphKeyBlockEntity telegraph) ? telegraph : null;
    }

    public int getPaperCount() {
        return paperCount;
    }

    public void setPaperCount(int count) {
        this.paperCount = Math.max(0, count);
        setChanged();
    }

    public void addPaper(int amount) {
        if (amount <= 0) return;
        this.paperCount += amount;
        setChanged();
    }

    public boolean recordSymbol(Level level, BlockPos pos, char symbol) {
        if (paperCount <= 0) return false;

        sequence.append(symbol);
        printedSymbols++;
        setChanged();

        if (printedSymbols >= 10) {
            printedSymbols = 0;
            paperCount = Math.max(0, paperCount - 1);
            setChanged();

            if (paperCount == 0) {
                flushPrintedTape(level, pos);
            }
        }
        return true;
    }

    public void flushPrintedTape(Level level, BlockPos pos) {
        if (level == null || level.isClientSide) return;
        if (sequence.isEmpty()) return;

        ItemStack tape = new ItemStack(Morsecraft.PAPER_TAPE_ITEM.get());
        tape.getOrCreateTag().putString("sequence", sequence.toString());
        // tape.setHoverName(Component.literal("Tape"));

        Block.popResource(level, pos, tape);

        sequence.setLength(0);
        printedSymbols = 0;
        setChanged();
    }


    public void setTape(ItemStack stack) {
        this.tape = stack;
        setChanged();
    }

    public boolean hasTape() {
        return !this.tape.isEmpty();
    }

    public ItemStack extractTape() {
        ItemStack r = this.tape.copy();
        this.tape = ItemStack.EMPTY;
        setChanged();
        return r;
    }

    public void insertTape(ItemStack stack) {
        if (stack.getItem() instanceof com.mrfortuna.morsecraft.items.PaperTapeItem) {
            tape = stack.copy();
            tape.setCount(1);
            setChanged();
        }
    }

    public void appendSymbol(char symbol) {
        if (paperCount <= 0) return;

        if (symbol == ' ') {
            sequence.append(' ');
        } else if (symbol == '/' || symbol == '.' || symbol == '-') {
            sequence.append(symbol);
        }

        setChanged();
    }

    public String getSequence() {
        return sequence.toString();
    }

    public void clearSequence() {
        sequence.setLength(0);
        setChanged();
    }

    public long getLastSignalGameTime() {
        return lastSignalGameTime;
    }

    public void setLastSignalGameTime(long lastSignalGameTime) {
        this.lastSignalGameTime = lastSignalGameTime;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("sequence", getSequence());
        tag.putInt("paper_count", paperCount);
        tag.putInt("printed_symbols", printedSymbols);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        sequence.setLength(0);
        if (tag.contains("sequence")) {
            String s = tag.getString("sequence");
            if (!s.isEmpty()) sequence.append(s);
        }
        this.paperCount = tag.contains("paper_count") ? tag.getInt("paper_count") : 0;
        this.printedSymbols = tag.contains("printed_symbols") ? tag.getInt("printed_symbols") : 0;
    }
}
