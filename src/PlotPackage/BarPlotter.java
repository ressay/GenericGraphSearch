package PlotPackage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;


public class BarPlotter extends Plotter {

    /**
     * A demonstration application showing an XY series containing a null value.
     *
     * @param title  the frame title.
     */
    private DefaultCategoryDataset dataset = new DefaultCategoryDataset( );


    public BarPlotter(String title, String XLabel , String YLabel) {

        super(title);
        this.chart = ChartFactory.createBarChart(
                title,
                XLabel,
                YLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        /***************** Adjust look of the chart *******************/
        chart.setBackgroundPaint(Color.white);
        // Set the background color of the chart
        chart.getTitle().setPaint(Color.DARK_GRAY);
        chart.setBorderVisible(true);
        // Adjust the color of the title
        CategoryPlot plot = chart.getCategoryPlot();
        // Get the Plot object for a bar graph
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.blue);
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0,0,125));
        /*************************************************************/
        chart.getCategoryPlot().getRangeAxis().setLowerBound(0);
        chart.getCategoryPlot().getRangeAxis().setUpperBound(100);

        this.chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(640 , 480));
        setContentPane(chartPanel);

    }


    @Override
    public void addData(double attempt, double percent) {
        this.dataset.addValue(percent,"Attempt",Integer.toString((int)attempt));
    }

    public void addData(double attempt, double percent,String key) {
        this.dataset.addValue(percent,key,Integer.toString((int)attempt));
    }

}