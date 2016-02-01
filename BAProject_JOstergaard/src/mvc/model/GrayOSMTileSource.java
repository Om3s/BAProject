package mvc.model;

import org.openstreetmap.gui.jmapviewer.tilesources.AbstractOsmTileSource;

public class GrayOSMTileSource {
	
	public static class BWMapnik extends AbstractOsmTileSource {

        private static final String PATTERN = "http://a.tile.stamen.com/toner/";

        private static final String[] SERVER = {"a", "b", "c"};

        private int serverNum;

        /**
         * Constructs a new {@code "Mapnik"} tile source.
         */
        public BWMapnik() {
            super("BWToner", PATTERN, "BWToner");
        }

        @Override
        public String getBaseUrl() {
            String url = String.format(this.baseUrl, new Object[] {SERVER[serverNum]});
            serverNum = (serverNum + 1) % SERVER.length;
            return url;
        }
    }
}
