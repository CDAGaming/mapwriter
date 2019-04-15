package mapwriter.region;

import mapwriter.util.Logging;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RegionManager {

    public static Logger logger;
    public final File worldDir;
    public final File imageDir;
    public final RegionFileCache regionFileCache;
    private final LruCache regionMap;
    public BlockColours blockColours;
    public int maxZoom;
    public int minZoom;
    public RegionManager(File worldDir, File imageDir, BlockColours blockColours, int minZoom, int maxZoom) {
        this.worldDir = worldDir;
        this.imageDir = imageDir;
        this.blockColours = blockColours;
        this.regionMap = new LruCache();
        this.regionFileCache = new RegionFileCache(worldDir);
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
    }

    private static int incrStatsCounter(Map<String, Integer> h, String key) {
        int n = 1;
        if (h.containsKey(key)) {
            n = h.get(key) + 1;
        }
        h.put(key, n);
        return n;
    }

    public void close() {
        for (Region region : this.regionMap.values()) {
            if (region != null) {
                region.close();
            }
        }
        this.regionMap.clear();
        this.regionFileCache.close();
    }

    // must not return null
    public Region getRegion(int x, int z, int zoomLevel, int dimension) {
        Region region = this.regionMap.get(Region.getKey(x, z, zoomLevel, dimension));
        if (region == null) {
            // add region
            region = new Region(this, x, z, zoomLevel, dimension);
            this.regionMap.put(region.key, region);
        }
        return region;
    }

    public void printLoadedRegionStats() {
        Logging.logInfo("loaded region listing:");
        Map<String, Integer> stats = new HashMap<String, Integer>();
        for (Region region : this.regionMap.values()) {
            Logging.logInfo("  %s", region);
            incrStatsCounter(stats, String.format("dim%d", region.dimension));
            incrStatsCounter(stats, String.format("zoom%d", region.zoomLevel));
            incrStatsCounter(stats, "total");
        }
        Logging.logInfo("loaded region stats:");
        for (Entry<String, Integer> e : stats.entrySet()) {
            Logging.logInfo("  %s: %d", e.getKey(), e.getValue());
        }
    }

    public void rebuildRegions(int xStart, int zStart, int w, int h, int dimension) {
        // read all zoom level 0 regions
        // then find all regions with a backing image at zoom level 0

        xStart &= Region.MASK;
        zStart &= Region.MASK;
        w = w + Region.SIZE & Region.MASK;
        h = h + Region.SIZE & Region.MASK;

        Logging.logInfo("rebuilding regions from (%d, %d) to (%d, %d)", xStart, zStart, xStart + w, zStart + h);

        for (int rX = xStart; rX < xStart + w; rX += Region.SIZE) {
            for (int rZ = zStart; rZ < zStart + h; rZ += Region.SIZE) {
                Region region = this.getRegion(rX, rZ, 0, dimension);
                if (this.regionFileCache.regionFileExists(rX, rZ, dimension)) {
                    region.clear();
                    for (int cz = 0; cz < 32; cz++) {
                        for (int cx = 0; cx < 32; cx++) {
                            // load chunk from anvil file
                            MwChunk chunk = MwChunk.read((region.x >> 4) +
                                    cx, (region.z >> 4) +
                                    cz, region.dimension, this.regionFileCache);
                            region.updateChunk(chunk);
                        }
                    }
                }
                region.updateZoomLevels();
            }
        }
    }

    public void updateChunk(MwChunk chunk) {
        Region region = this.getRegion(chunk.x << 4, chunk.z << 4, 0, chunk.dimension);
        region.updateChunk(chunk);
    }

    // simple Least Recently Used (LRU) cache implementation
    class LruCache extends LinkedHashMap<Long, Region> {
        private final static long serialVersionUID = 1L;
        private final static int MAX_LOADED_REGIONS = 64;

        public LruCache() {
            // initial capacity, loading factor, true for access time ordering
            super(MAX_LOADED_REGIONS * 2, 0.5f, true);
        }

        // called on every put and putAll call, the entry 'entry' is removed
        // if this function returns true.
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, Region> entry) {
            boolean ret = false;
            if (this.size() > MAX_LOADED_REGIONS) {
                Region region = entry.getValue();
                region.close();
                ret = true;
            }
            return ret;
        }
    }
}
