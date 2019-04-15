package mapwriter.api;

import java.util.ArrayList;

public interface IMwDataProvider {
    ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ);

    // return null if nothing should be drawn on fullscreen map
    ILabelInfo getLabelInfo(int mouseX, int mouseY);

    // Returns what should be added to the status bar by the addon.
    String getStatusString(int dim, int bX, int bY, int bZ);

    // Callback for dimension change on the map
    void onDimensionChanged(int dimension, IMapView mapview);

    void onDraw(IMapView mapview, IMapMode mapmode);

    void onMapCenterChanged(double vX, double vZ, IMapView mapview);

    // Call back for middle click.
    void onMiddleClick(int dim, int bX, int bZ, IMapView mapview);

    boolean onMouseInput(IMapView mapview, IMapMode mapmode);

    void onOverlayActivated(IMapView mapview);

    void onOverlayDeactivated(IMapView mapview);

    void onZoomChanged(int level, IMapView mapview);
}
