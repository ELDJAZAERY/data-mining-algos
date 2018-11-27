
package DMweKa.Application;

import javafx.embed.swing.SwingNode;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import weka.core.Instances;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;


/**
 * Demonstration of a box-and-whisker chart using a {@link CategoryPlot}.
 *
 * @author David Browning
 */
public class BoxPlot{


    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public BoxPlot(final String title, Instances weka, SwingNode swing) {

        final BoxAndWhiskerCategoryDataset dataset = createSampleDataset(weka);

        final CategoryAxis xAxis = new CategoryAxis("Attribut");
        final NumberAxis yAxis = new NumberAxis("Valeur");
        yAxis.setAutoRangeIncludesZero(false);
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer() {
            public CategoryItemRendererState initialise(Graphics2D g2,
                                                        Rectangle2D dataArea,
                                                        CategoryPlot plot,
                                                        int rendererIndex,
                                                        PlotRenderingInfo info) {
                CategoryItemRendererState state = super.initialise(g2, dataArea, plot, rendererIndex, info);
                if (state.getBarWidth() > 35)
                    state.setBarWidth(35); 
                return state;
            }
        };

        renderer.setFillBox(false);
        renderer.setSeriesOutlinePaint(0, Color.BLACK);
        renderer.setUseOutlinePaintForWhiskers(true);
        renderer.setSeriesVisibleInLegend(0,false);
        renderer.setMeanVisible(false);
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        final JFreeChart chart = new JFreeChart(
                title,
                new Font("SansSerif", Font.BOLD, 14),
                plot,
                true
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(430, 310));
        swing.setContent(chartPanel);
    }



    /**
     * Creates a sample dataset.
     *
     * @return A sample dataset.
     */
    private BoxAndWhiskerCategoryDataset createSampleDataset(Instances weka) {

        final Double categoryCount = Double.valueOf(weka.numAttributes());
        final int entityCount = weka.numInstances();

        final DefaultBoxAndWhiskerCategoryDataset dataset
                = new DefaultBoxAndWhiskerCategoryDataset();
 
            for (int j = 0; j < categoryCount; j++) {
                final List list = new ArrayList();
                if(weka.attribute(j).isNumeric()){ 
                    for (int k = 0; k < entityCount; k++) {
                        final double value1 = weka.get(k).value(j);
                        list.add(new Double(value1));
                    }
                }

                dataset.add(list, " " , weka.attribute(j).name());
            }

        
        return dataset;
    }


}