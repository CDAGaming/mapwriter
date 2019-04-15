package mapwriter.map;

import mapwriter.Mw;
import mapwriter.config.ConfigurationHandler;
import mapwriter.map.mapmode.MapMode;
import mapwriter.util.Reference;
import mapwriter.util.Render;

import java.awt.*;
import java.util.LinkedList;

public class Trail {

    public LinkedList<TrailMarker> trailMarkerList = new LinkedList<TrailMarker>();
    public int maxLength = 30;
    public String name;
    public boolean enabled;
    public long lastMarkerTime = 0;
    public long intervalMillis = 5000;
    private Mw mw;
    public Trail(Mw mw, String name) {
        this.mw = mw;
        this.name = name;
        this.enabled = ConfigurationHandler.configuration.getBoolean(this.name +
                "TrailEnabled", Reference.catOptions, false, "");
        this.maxLength = ConfigurationHandler.configuration.getInt(this.name +
                "TrailMaxLength", Reference.catOptions, this.maxLength, 1, 200, "");
        this.intervalMillis = ConfigurationHandler.configuration.getInt(this.name +
                "TrailMarkerIntervalMillis", Reference.catOptions, (int) this.intervalMillis, 100, 360000, "");
    }

    public void addMarker(double x, double y, double z, double heading) {
        this.trailMarkerList.add(new TrailMarker(x, y, z, heading));
        // remove elements from beginning of list until the list has at most
        // maxTrailLength elements
        while (this.trailMarkerList.size() > this.maxLength) {
            this.trailMarkerList.poll();
        }
        int i = this.maxLength - this.trailMarkerList.size();
        for (TrailMarker marker : this.trailMarkerList) {
            marker.alphaPercent = i * 100 / this.maxLength;
            i++;
        }
    }

    public void close() {
        // this.mw.config.setBoolean(Mw.catOptions, this.name + "TrailEnabled",
        // this.enabled);
        // this.mw.config.setInt(Mw.catOptions, this.name + "TrailMaxLength",
        // this.maxLength);
        // this.mw.config.setInt(Mw.catOptions, this.name +
        // "TrailMarkerIntervalMillis", (int) this.intervalMillis);
        this.trailMarkerList.clear();
    }

    public void draw(MapMode mapMode, MapView mapView) {
        for (TrailMarker marker : this.trailMarkerList) {
            marker.draw(mapMode, mapView);
        }
    }

    // for other types of trails will need to extend Trail and override this
    // method
    public void onTick() {
        long time = System.currentTimeMillis();
        if (time - this.lastMarkerTime > this.intervalMillis) {
            this.lastMarkerTime = time;
            this.addMarker(this.mw.playerX, this.mw.playerY, this.mw.playerZ, this.mw.playerHeading);
        }
    }

    class TrailMarker {

        static final int borderColour = 0xff000000;
        static final int colour = 0xff00ffff;

        double x, y, z, heading;
        int alphaPercent;

        public TrailMarker(double x, double y, double z, double heading) {
            this.set(x, y, z, heading);
        }

        public void draw(MapMode mapMode, MapView mapView) {
            if (mapView.isBlockWithinView(this.x, this.z, mapMode.getConfig().circular)) {
                Point.Double p = mapMode.blockXZtoScreenXY(mapView, this.x, this.z);

                // draw a coloured arrow centered on the calculated (x, y)
                Render.setColourWithAlphaPercent(borderColour, this.alphaPercent);
                Render.drawArrow(p.x, p.y, this.heading, mapMode.getConfig().trailMarkerSize);
                Render.setColourWithAlphaPercent(colour, this.alphaPercent);
                Render.drawArrow(p.x, p.y, this.heading, mapMode.getConfig().trailMarkerSize - 1.0);
            }
        }

        public void set(double x, double y, double z, double heading) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.heading = heading;
            this.alphaPercent = 100;
        }
    }

}
