package mapwriter.api;

import java.util.List;

public interface IMapView {
    int adjustZoomLevel(int n);

    int getDimension();

    void setDimension(int dimension);

    double getDimensionScaling(int playerDimension);

    double getHeight();

    double getMaxX();

    double getMaxZ();

    double getMinX();

    double getMinZ();

    int getPixelsPerBlock();

    int getRegionZoomLevel();

    boolean getUndergroundMode();

    void setUndergroundMode(boolean enabled);

    double getWidth();

    double getX();

    double getZ();

    int getZoomLevel();

    boolean isBlockWithinView(double bX, double bZ, boolean circular);

    void nextDimension(List<Integer> dimensionList, int n);

    void panView(double relX, double relZ);

    void setDimensionAndAdjustZoom(int dimension);

    void setMapWH(IMapMode mapMode);

    void setMapWH(int w, int h);

    void setTextureSize(int n);

    void setViewCentre(double vX, double vZ);

    void setViewCentreScaled(double vX, double vZ, int playerDimension);

    int setZoomLevel(int zoomLevel);

    // bX and bZ are the coordinates of the block the zoom is centred on.
    // The relative position of the block in the view will remain the same
    // as before the zoom.
    void zoomToPoint(int newZoomLevel, double bX, double bZ);
}
