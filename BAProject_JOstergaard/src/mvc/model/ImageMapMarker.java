package mvc.model;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import mvc.main.Main;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;

public class ImageMapMarker extends MapMarkerCircle {
	private Image image;

    public ImageMapMarker(Coordinate coord, Image image) {
        this(coord, 1, image);
    }

    public ImageMapMarker(Coordinate coord, double radius, Image image) {
        super(coord, radius);
        this.image = image;
    }

    @Override
    public void paint(Graphics g, Point position, int radio) {
        g.drawImage(this.image, position.x, position.y, this.image.getWidth(null), this.image.getHeight(null), null);
        this.paintText(g, position);
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
