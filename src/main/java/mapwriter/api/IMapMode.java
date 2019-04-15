package mapwriter.api;

import java.awt.*;

public interface IMapMode {
    Point.Double blockXZtoScreenXY(IMapView mapView, double bX, double bZ);

    Point.Double getClampedScreenXY(IMapView mapView, double bX, double bZ);

    IMapModeConfig getConfig();

    int getH();

    int getHPixels();

    Point.Double getNewPosPoint(double mouseX, double mouseY);

    int getTextColour();

    int getTextX();

    int getTextY();

    int getW();

    int getWPixels();

    int getX();

    int getXTranslation();

    int getY();

    int getYTranslation();

    boolean posWithin(int mouseX, int mouseY);

    Point screenXYtoBlockXZ(IMapView mapView, int sx, int sy);

    void setScreenRes();

    void setScreenRes(int dw, int dh, int sw, int sh, double scaling);

    void updateMargin();
}
