package PlotPackage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;


public class XYPlotter extends Plotter {

    /**
     * A demonstration application showing an XY xySeries containing a null value.
     *
     * @param title  the frame title.
     */
    private  XYSeries xySeries;
    private XYSeriesCollection dataXY;

    public XYPlotter(String title, String XLabel , String YLabel) {

        super(title);
        new XYSeries("Data");
        this.xySeries = new XYSeries(XLabel);
        this.dataXY = new XYSeriesCollection(xySeries);
        this.chart = ChartFactory.createXYLineChart(
                title,
                XLabel,
                YLabel,
                dataXY,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        range.setRange(0.0, 100.0);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(640, 480));
        setContentPane(chartPanel);

    }


    @Override
    public void addData(double X, double Y) {
        this.xySeries.add(X,Y);
    }
}