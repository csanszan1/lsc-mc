package hu.csanszan1.lscmc;

public class PlotConfig {
    private int plotSize;
    private int roadWith;
    private int groundHeight;

    public PlotConfig(int plotSize, int roadWith, int groundHeight) {
        this.plotSize = plotSize;
        this.roadWith = roadWith;
        this.groundHeight = groundHeight;
    }

    // Getters
    public int getPlotSize() {
        return plotSize;
    }

    public int getRoadWith() {
        return roadWith;
    }

    public int getGroundHeight() {
        return groundHeight;
    }

    // Optionally: toString()
    @Override
    public String toString() {
        return "PlotConfig{" +
                "plotSize=" + plotSize +
                ", roadWith=" + roadWith +
                ", groundHeight=" + groundHeight +
                '}';
    }
}