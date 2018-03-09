package PlotPackage;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public abstract class Plotter extends ApplicationFrame {

    protected ChartPanel chartPanel;
    protected JFreeChart chart;


    public Plotter(String title) {
        super(title);
    }

    public void setUpAndShow() {
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

    public abstract void addData(double X, double Y);
}
