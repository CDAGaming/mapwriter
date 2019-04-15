package mapwriter.api;

public interface IMapModeConfig {
    int getAlphaPercent();

    String getBiomeMode();

    boolean getBorderMode();

    boolean getCircular();

    String getConfigCategory();

    String getCoordsMode();

    String[] getCoordsModeStringArray();

    boolean getEnabled();

    double getHeightPercent();

    String getMapPosCategory();

    int getMarkerSize();

    int getPlayerArrowSize();

    boolean getRotate();

    int getTrailMarkerSize();

    double getWidthPercent();

    double getXPos();

    double getYPos();
}
