package mapwriter.api;

import java.awt.*;

public interface IMwChunkOverlay {
    public int getBorderColor();

    public float getBorderWidth();

    public int getColor();

    public Point getCoordinates();

    public float getFilling();

    public boolean hasBorder();
}
