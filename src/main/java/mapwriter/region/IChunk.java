package mapwriter.region;

import net.minecraft.block.state.IBlockState;

public interface IChunk {
    int getBiome(int x, int y, int z);

    IBlockState getBlockState(int x, int y, int z);

    int getLightValue(int x, int y, int z);

    int getMaxY();
}
