package reservoirViewerUtils;

import br.unicamp.ft.mra.IReorderableMatrix;
import br.unicamp.ft.mra.ReorderableMatrix;
import br.unicamp.ft.mra.reordering.IMatrixReorderingAlgorithm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import reservoirViewer.ICurve;
import reservoirViewer.PeanoHilbertCurve;
import reservoirViewer.PseudoPeanoHilbertCurve;
import reservoirViewer.ReservoirMap;
import reservoirViewer.SnakeCurve;
import reservoirViewer.Well;
import reservoirViewer.WellList;
import reservoirViewer.WellType;
import reservoirViewer.Dimension;
import reservoirViewerConfig.ClusteringData;
import reservoirViewerConfig.Configuracao;
import reservoirViewerXmlParser.Property;

/**
 * Classe responsavel por implemnetar as N maneiras de se "desenhar" a
 * visualização de um reservatório
 *
 */
public class ReservoirDrawer {

    public ReservoirDrawer() {
    }

    /**
     * Método responsável por desenhar um Reservatório qualquer seguindo uma
     * Configuração
     *
     * @param configuracao
     */
    public void DrawReservoir(Configuracao configuracao) throws IOException {
        DrawManyodels(configuracao);
    }

    /**
     * Metodo que Desenha uma visualização do tipo SmallMultiples
     *
     * @param configuracao
     * @param res
     * @param clusteringData
     */
    public void DrawSmallMultiples(Configuracao configuracao, ReservoirMap res,ClusteringData clusteringData, Property property) throws IOException {
        
        //arquivo de progresso
        FileWriter logFile = new FileWriter(configuracao.getRoot() + File.separator + configuracao.getProgressFile());
        PrintWriter logFileWriter = new PrintWriter(logFile);
        logFileWriter.printf("50:ProgRVDesInit" + property.getName() + "\n");
//        logFile.close();

        ICurve curve = null;

        // Obtem o tipo de curva usado na visualização
        String layoutCurve = configuracao.getLayoutCurve().toUpperCase();

        // DRAWING PROPERTY VALUES ACCORDING TO THE RESERVOIR MAP.
        int maxI = res.getMaxI();
        int maxJ = res.getMaxJ();
        int maxK = res.getMaxK();

        List<Integer> representativeModels = getRepresentativeModelsList(configuracao);

        Dimension dimension; 
        int kxSize = 0;
        int kySize = 0;

        switch (layoutCurve) {
            case "PEANOHILBERT":
                curve = new PeanoHilbertCurve(maxK);
//                curve = new PseudoPeanoHilbertCurve(maxK, new Dimension(15, 15));
                // WITH PEANO-HILBERT CURVE
                dimension = curve.getDimension();
                kxSize = dimension.x;
                kySize = dimension.y;

                break;
            case "PSEUDOPEANOHILBERT":
//                curve = new PeanoHilbertCurve(maxK);
                curve = new PseudoPeanoHilbertCurve(maxK, getCurveDimensionForSmallMultiples(maxI, maxJ, maxK));
                // WITH PEANO-HILBERT CURVE
                dimension = curve.getDimension();
                kxSize = dimension.x;
                kySize = dimension.y;

                break;
            case "SNAKE":
                curve = new SnakeCurve(maxK, getCurveDimensionForSmallMultiples(maxI, maxJ, maxK));
                // WITHOUT PEANO-HILBERT CURVE
//                int kIni = 1;
//                int kFin = maxK; // for many models
//                int deltaK = kFin - kIni + 1;
//                kxSize = (int) Math.round(Math.sqrt(deltaK));
//                double d = Math.ceil((double) deltaK / kxSize);
//                kySize = (int) d;
                
                dimension = curve.getDimension();
                kxSize = dimension.x;
                kySize = dimension.y;

                break;
            default:
                throw new IllegalArgumentException("Layout Curve não implementado");
        }

        String propertyName = property.getName();

        double actualMaxValue = res.getMaxValue(propertyName);
        double actualMinValue = res.getMinValue(propertyName);
        double filterMaxValue = 1.0 * (actualMaxValue - actualMinValue) + actualMinValue;
        double filterMinValue = 0.0 * (actualMaxValue - actualMinValue) + actualMinValue;

        // Intervalo para ajustar paleta de cores entre P5 e P95
        ReservoirMap.Interval maxMinInterval = res.getInterval(propertyName, 0.05, 0.95);
        double maxValue = maxMinInterval.end;
        double minValue = maxMinInterval.start;
        //        maxValue *=0.3; // ajuste para ver intervalo especifico.

        double maxkxkySize = Math.max(kxSize, kySize);
//        double zoom = 0.8; // TODO should be automatically calculated based on the window height and width
        double zoom = 3; // TODO should be automatically calculated based on the window height and width
        double xSize = kySize / maxkxkySize * zoom;
        double ySize = kxSize / maxkxkySize * zoom;
        //int mainCellSize = size * 10;
        double gapSize = 1;
        final double deltaForClusterBorder = 1;
        //float mainCellSizePlusGap = mainCellSize + gapSize;
        int bottom = (int) (ySize * kySize + gapSize) * maxJ;
        int right = (int) (xSize * kxSize + gapSize) * maxI;
        //Pane root = new Pane();
        Pane backgroundLayer = new Pane();
        Pane reservoirMapLayer = new Pane();
        Pane wellLayer = new Pane();
        Pane wellLabelLayer = new Pane();
        // Create a matrix that defines to which cluster a reservoir belongs.
        // It will be used to draw cluster boundaries
        int[][] clusterIdMatrix = new int[kxSize][kySize]; //0...kxSize-1, 0...kySize-1
        if (clusteringData != null) {
            for (int ky = 1; ky <= kySize; ky++) {
                for (int kx = 1; kx <= kxSize; kx++) {
                    int k = curve.getD(kx - 1, ky - 1) + 1; // Peano-Hibert layout
                    if (k <= maxK && k > 0) {
                        int realK = res.getRealK(k);
                        System.out.println("k=" + k + "\t realK=" + realK);
                        clusterIdMatrix[kx - 1][ky - 1] = clusteringData.getClusterId(realK - 1);
                    } else {
                        System.err.println("Error - trying to get reservoir k=" + k + " when defining clusters (clusterIdMatrix)");
                        clusterIdMatrix[kx - 1][ky - 1] = -1;
                    }
                }
            }
        }
        Pane curveLayer = new Pane();
        Rectangle mainRect = new Rectangle(0, 0, right, bottom);
        mainRect.setStrokeType(StrokeType.INSIDE);
        mainRect.setStroke(Color.web("white", 0.1F));
        backgroundLayer.getChildren().add(mainRect);
        Pane clusterLayer = new Pane();
        ObservableList<Node> reservoirMapLayerChildren = reservoirMapLayer.getChildren();
        ObservableList<Node> clusterLayerChildren = null;
        if (clusteringData != null) {
            clusterLayerChildren = clusterLayer.getChildren();
        }
        double valueForPainting;
        // Drawing inside a IJ cell
        double rectangleXSize = xSize * maxI + gapSize * 2;
        double rectangleYSize = ySize * maxJ + gapSize * 2;
        Blend blend = new Blend();
        blend.setOpacity(1.0);
        for (int ky = 1; ky <= kySize; ky++) {
            for (int kx = 1; kx <= kxSize; kx++) {
                int k = 1 + curve.getD(kx - 1, ky - 1); // Peano-Hibert layout
                int realK = res.getRealK(k);
                // Surround a representative model with a rectangle
                if (representativeModels.contains(realK)) {
                    double xIni = getXSmallMultiples(kx, xSize, maxI, gapSize) - gapSize;
                    double yIni = getYSmallMultiples(bottom, ky, ySize, maxJ, gapSize) - gapSize;
                    Rectangle r = new Rectangle(xIni, yIni, rectangleXSize, rectangleYSize);
                    r.setStrokeType(StrokeType.INSIDE);
                    r.setStroke(Color.web("white", 1.0F));
                    r.setFill(Color.DARKGRAY);
                    reservoirMapLayer.getChildren().add(r);
                }
                if (clusteringData != null) {
                    Color c = Color.web("yellow", 1.0F);
                    int currentClusterId = clusterIdMatrix[kx - 1][ky - 1];
                    System.out.println("Analyzing cluster " + currentClusterId);
                    if (kx < kxSize) {
                        int rightClusterId = clusterIdMatrix[kx][ky - 1];
                        if (currentClusterId != rightClusterId) {
                            double xIni = getXSmallMultiples(kx + 1, xSize, maxI, gapSize) - gapSize / 2;
                            double yIni = getYSmallMultiples(bottom, ky, ySize, maxJ, gapSize) - gapSize / 2;
                            Rectangle r = new Rectangle(xIni - deltaForClusterBorder / 2, yIni - deltaForClusterBorder / 2, deltaForClusterBorder, rectangleYSize + deltaForClusterBorder - gapSize * 2);
                            r.setStrokeType(StrokeType.INSIDE);
                            r.setStroke(c);
                            r.setFill(c);
                            clusterLayerChildren.add(r);
                            System.out.println("Adding delimiter between clusters " + currentClusterId + " and " + rightClusterId);
                        }
                    }
                    if (ky < kySize) {
                        int bottomClusterId = clusterIdMatrix[kx - 1][ky];
                        if (currentClusterId != bottomClusterId) {
                            double xIni = getXSmallMultiples(kx, xSize, maxI, gapSize) - gapSize / 2;
                            double yIni = getYSmallMultiples(bottom, ky + 1, ySize, maxJ, gapSize) - gapSize / 2;
                            Rectangle r = new Rectangle(xIni - deltaForClusterBorder / 2, yIni - deltaForClusterBorder / 2, rectangleXSize + deltaForClusterBorder - gapSize * 2, deltaForClusterBorder);
                            r.setStrokeType(StrokeType.INSIDE);
                            r.setStroke(c);
                            r.setFill(c);
                            clusterLayerChildren.add(r);
                            System.out.println("Adding delimiter between clusters " + currentClusterId + " and " + bottomClusterId);
                        }
                    }
                }
                System.out.println("Drawing delimiters (end)");
                //int k = kIni - 1 + kx + (ky - 1) * kxSize; // left-to-right, top-down layout
                for (int i = 1; i <= maxI; i++) {
                    for (int j = 1; j <= maxJ; j++) {
                        double value = res.getValue(propertyName, i, j, k);
                        if (!res.isNull(i, j, k)) {
                            //                             if (value==maxValue) {
                            //                                System.out.println("max is at "+i+","+j+","+k);
                            //                            }
                            //Rectangle r = new Rectangle(i * mainCellSizePlusGap + ky * size, j * mainCellSizePlusGap + kx * size, size, size);
                            double xIni = getXSmallMultiples(kx, xSize, maxI, gapSize) + (i - 1) * xSize;
                            double yIni = getYSmallMultiples(bottom, ky, ySize, maxJ, gapSize) + (maxJ - j) * ySize;
                            Rectangle r = new Rectangle(xIni, yIni, xSize, ySize);
                            r.setStrokeType(StrokeType.INSIDE);
                            r.setStroke(Color.web("white", 0.0F));
                            if (value >= filterMinValue && value <= filterMaxValue) {
                                valueForPainting = Math.max(Math.min(value, maxValue), minValue);
                                // Color palette: blue for minimum value, red for maximum value, hue-based linear palette
                                r.setFill(Color.hsb(240 - 240 * (valueForPainting - minValue) / (maxValue - minValue), 1, 1.0F));
                            } else {
                                r.setFill(Color.GRAY);
                            }
                            r.setStrokeWidth(0.0F);
                            r.setEffect(blend);
                            reservoirMapLayer.getChildren().add(r);
                        }
                    }
                }
            }
        }
        Rectangle begin = new Rectangle(0, 0, 500, 700);
        begin.setStroke(Color.web("yellow"));
        // create main content
        Pane group = new Pane(backgroundLayer, curveLayer, reservoirMapLayer, wellLayer, wellLabelLayer, clusterLayer);
        group.setBlendMode(BlendMode.SRC_OVER);
        //group.setLayoutX(0);
        //group.setLayoutY(0);
        float scale = 1.0F;
        group.setScaleX(scale);
        group.setScaleY(scale);
        //group.setTranslateX(0);
        //group.setTranslateY(0);
        JFXPanel jfxPanel = new JFXPanel();
        Text title = new Text("Reservoir Viewer");
        Pane top = new Pane(title);
        title.setFill(Color.web("white"));
        
        // DRAWING THE SCALE COLOR BAR
        Pane scalePane = new Pane();
         Rectangle scaleMainRect = new Rectangle(0, 0, 600, bottom);
         double yStart=scaleMainRect.getHeight()/2;
         double yEnd=scaleMainRect.getHeight()-50;
         mainRect.setStrokeType(StrokeType.INSIDE);
         //mainRect.setStroke(Color.web("black", 0.1f));
         mainRect.setStroke(Color.web("black", 0f));
         scalePane.getChildren().add(scaleMainRect);
         double scaleColorBarWidth = 100;
         double scaleColorBarStartX = 0;

         // - drawing the bar
         double scaleNumberOfSteps = 100;
         double interval = (yEnd-yStart)/scaleNumberOfSteps;
         for (int i=0; i<scaleNumberOfSteps; i++) {
             double y = yEnd-interval*i;
             Rectangle scaleItemRect = new
             Rectangle(scaleColorBarStartX, y-interval, scaleColorBarWidth, interval);
             scaleItemRect.setStrokeType(StrokeType.INSIDE);
             scaleItemRect.setStroke(Color.web("black", 0.1f));
             scaleItemRect.setFill(Color.hsb(240 - 240 * (double)i /
             (double)scaleNumberOfSteps, 1, 1));
             scalePane.getChildren().add(scaleItemRect);
         }

         // - Defining exponent "exp" to use in scientific notation :  n.10^exp
         int exp=0;
         double mantissa=maxValue;
         while (mantissa>=10) {
             exp+=1;
             mantissa = mantissa / 10d;
         }

         // - drawing ruler and numbers
         scaleNumberOfSteps = 4;
         double interval2 = (yEnd-yStart)/scaleNumberOfSteps;
         
         Text tBenchmark = new Text(configuracao.getBenchmark() != null ? configuracao.getBenchmark() : "");
             Font fBenchmark = Font.font("Verdana", 30);
             tBenchmark.setFont(fBenchmark);
             tBenchmark.setFill(Color.WHITE);
             tBenchmark.setFontSmoothingType(FontSmoothingType.LCD);
             tBenchmark.setStroke(Color.BLACK);
             tBenchmark.setStrokeLineCap(StrokeLineCap.ROUND);
             tBenchmark.setStrokeLineJoin(StrokeLineJoin.ROUND);
             tBenchmark.setStrokeType(StrokeType.OUTSIDE);
             tBenchmark.setStrokeWidth(gapSize);
             tBenchmark.setX(scaleColorBarStartX+scaleColorBarWidth+25);
             tBenchmark.setY(yStart-tBenchmark.getBoundsInParent().getHeight()*4+tBenchmark.getBaselineOffset()-(tBenchmark.getBoundsInParent().getHeight())/2);
             scalePane.getChildren().add(tBenchmark);
         if (exp!=0) {  // draw a string "values X 10^exp" above the color bar
             Text t = new Text("Values \u00d7 10^"+exp);
             Font f = Font.font("Verdana", 30);
             t.setFont(f);
             t.setFill(Color.WHITE);
             t.setFontSmoothingType(FontSmoothingType.LCD);
             t.setStroke(Color.BLACK);
             t.setStrokeLineCap(StrokeLineCap.ROUND);
             t.setStrokeLineJoin(StrokeLineJoin.ROUND);
             t.setStrokeType(StrokeType.OUTSIDE);
             t.setStrokeWidth(gapSize);
             t.setX(scaleColorBarStartX+scaleColorBarWidth+25);
             t.setY(yStart-t.getBoundsInParent().getHeight()*2+t.getBaselineOffset()-(t.getBoundsInParent().getHeight())/2);
             scalePane.getChildren().add(t);
         }
         for (int i=0; i<=scaleNumberOfSteps; i++) {
             double y = yEnd-interval2*i;

             Line line = new Line(
            scaleColorBarStartX+scaleColorBarWidth-scaleColorBarWidth/2, y,scaleColorBarStartX+scaleColorBarWidth+20, y);
             line.setStroke(Color.WHITE);
             line.setStrokeWidth(5);
             scalePane.getChildren().add(line);


             double value = minValue + ((double)i /
             (double)scaleNumberOfSteps) * (maxValue-minValue);
//            System.out.println("original value="+value);
             value = value / Math.pow(10,exp); // converting to a mantissa
             value = Math.rint(value*1000)/1000;
//            System.out.println("rounded value="+value);
             String prefix="";
             if (i == 0 && value > 0) {
                 prefix = "\u2264 "; // less than or equal to
             } else if (i == scaleNumberOfSteps) {
                 prefix = "\u2265 "; // greater than or equal to
             }

             Text t = new Text(prefix+Double.toString(value));
             Font f = Font.font("Verdana", 30);
             t.setFont(f);
             t.setFill(Color.WHITE);
             t.setFontSmoothingType(FontSmoothingType.LCD);
             t.setStroke(Color.BLACK);
             t.setStrokeLineCap(StrokeLineCap.ROUND);
             t.setStrokeLineJoin(StrokeLineJoin.ROUND);
             t.setStrokeType(StrokeType.OUTSIDE);
             t.setStrokeWidth(gapSize);
             t.setX(scaleColorBarStartX+scaleColorBarWidth+25);
             t.setY(y+t.getBaselineOffset()-(t.getBoundsInParent().getHeight())/2);
             scalePane.getChildren().add(t);

         }
        
        BorderPane mainPane = new BorderPane(group, top, scalePane, null, null);
        mainPane.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), Insets.EMPTY)));
        Scene scene = new Scene(mainPane);

        Runtime.getRuntime().gc();
        logFileWriter.printf("75:ProgRVDesFim" + property.getName() + "\n");
        logFile.close();
        
        saveScene(scene, configuracao, property, null);
    }
    
    private Dimension getCurveDimensionForSmallMultiples(int maxI, int maxJ, int maxK) {
        /* Retorna no. de linhas e colunas do small multiples
        de forma que a figura gerada seja parecida com  um retângulo áureo.
        Um retângulo áureo tem proporção 1:1.618
        */
        
        /* Find the small value for numberOfRows and numberOfColumns:
                (maxJ * numberOfRows) = (maxI * numberOfColumns) / 1.618
                maxK<=numberOfRows * numberOfColumns 
                1<=numberOfRows<=maxK
                1<=numberOfColumns<=maxK
        
            maxK<=numberOfRows * numberOfColumns 
            maxK/numberOfColumns<=numberOfRows
            numberOfRows>=maxK/numberOfColumns
        
            (maxJ * numberOfRows) = (maxI * numberOfColumns) / 1.618
            numberOfRows = (maxI * numberOfColumns) / (1.618 * maxJ)
            (maxI * numberOfColumns) / (1.618 * maxJ) >= maxK/numberOfColumns
            (maxI * numberOfColumns^2) / (1.618 * maxJ) >= maxK
            (numberOfColumns^2) / (1.618 * maxJ) >= maxK / maxI 
            numberOfColumns^2 >= (1.618 * maxJ * maxK) / maxI 
            numberOfColumns >= sqrt((1.618 * maxJ * maxK) / maxI )
        
            numberOfRows>=maxK/numberOfColumns
            numberOfColumns>=maxK/numberOfRows
        
        
        
            I want the lowest possible value of numberOfColumns, so:
            numberOfColumns = ceil(sqrt((1.618 * maxJ * maxK) / maxI ))

            numberOfRows>=maxK/numberOfColumns
            I want the lowest possible value of numberOfRows, so:
            numberOfRows = ceil(maxK/numberOfColumns)
        
        */ 
        double dNumberOfColumns = Math.sqrt((1.618* maxJ * maxK) / maxI );
        int numberOfColumns = (int)Math.ceil(dNumberOfColumns);
        double dNumberOfRows = maxK/dNumberOfColumns;
        int numberOfRows = (int)Math.ceil(dNumberOfRows);
        System.out.println("Curve dimensions: ");
        System.out.println("In double: "+dNumberOfRows +"x"+dNumberOfColumns);
        System.out.println("In int: "+numberOfRows +"x"+numberOfColumns);
        System.out.println("maxI="+maxI);
        System.out.println("maxJ="+maxJ);
        System.out.println("maxK="+maxK);
        
        return new Dimension(numberOfColumns, numberOfRows); ////<<<<<TESTAR!!!!
        
    }

    /**
     * Metodo que desenha uma visualização do tipo Pixelization
     *
     * @param configuracao
     * @param res
     * @param clusteringData
     * @param wellList
     *
     */
    public void DrawPixelization(Configuracao configuracao, ReservoirMap res,
            ClusteringData clusteringData, WellList wellList, Property property) throws IOException {
        
        //arquivo de progresso
                FileWriter logFile = new FileWriter(configuracao.getRoot() + File.separator + configuracao.getProgressFile());
                PrintWriter logFileWriter = new PrintWriter(logFile);
                logFileWriter.printf("Desenho iniciado\n");
                logFile.close();

        ICurve curve = null;

        // Obtem o tipo de curva usado na visualização
        String layoutCurve = configuracao.getLayoutCurve().toUpperCase();

        // DRAWING PROPERTY VALUES ACCORDING TO THE RESERVOIR MAP.
        int maxI = res.getMaxI();
        int maxJ = res.getMaxJ();
        int maxK = res.getMaxK();

        List<Integer> representativeModels = getRepresentativeModelsList(configuracao);

        Dimension dimension;
        int kxSize = 0;
        int kySize = 0;

        switch (layoutCurve.toUpperCase()) {
            case "PEANOHILBERT":
                curve = new PeanoHilbertCurve(maxK);
                // WITH PEANO-HILBERT CURVE
                dimension = curve.getDimension();
                kxSize = dimension.x;
                kySize = dimension.y;
                break;
            case "PSEUDOPEANOHILBERT":
                curve = new PseudoPeanoHilbertCurve(maxK, new Dimension(15, 15));
                // WITH PEANO-HILBERT CURVE
                dimension = curve.getDimension();
                kxSize = dimension.x;
                kySize = dimension.y;
                break;
            case "SNAKECURVE":
                curve = new SnakeCurve(maxK);
                // WITHOUT PEANO-HILBERT CURVE
                int kIni = 1;
                int kFin = maxK; // for many models
                int deltaK = kFin - kIni + 1;
                kxSize = (int) Math.round(Math.sqrt(deltaK));
                double d = Math.ceil((double) deltaK / kxSize);
                kySize = (int) d;
                break;
            default:
                break;
        }

        String propertyName = property.getName();

        double actualMaxValue = res.getMaxValue(propertyName);
        double actualMinValue = res.getMinValue(propertyName);
        double filterMaxValue = 1.0 * (actualMaxValue - actualMinValue) + actualMinValue;
        double filterMinValue = 0.0 * (actualMaxValue - actualMinValue) + actualMinValue;

        // Intervalo para ajustar paleta de cores entre P5 e P95
        ReservoirMap.Interval maxMinInterval = res.getInterval(propertyName, 0.05, 0.95);
        double maxValue = maxMinInterval.end;
        double minValue = maxMinInterval.start;
        //        maxValue *=0.3; // ajuste para ver intervalo especifico.

        double maxkxkySize = Math.max(kxSize, kySize);
//        double zoom = 0.8; // TODO should be automatically calculated based on the window height and width
        double zoom = 10; // TODO should be automatically calculated based on the window height and width
        double xSize = kySize / maxkxkySize * zoom;
        double ySize = kxSize / maxkxkySize * zoom;;
        //int mainCellSize = size * 10;
        double gapSize = 1;
        // -->
        

        double bottom = -getY(0, maxJ, ySize, kySize, gapSize);
        double right = getX(maxI + 1, xSize, kxSize, gapSize);

        //Pane root = new Pane();
        Pane reservoirMapLayer = new Pane();
        Pane wellLayer = new Pane();
        Pane wellLabelLayer = new Pane();

        Rectangle mainRect = new Rectangle(0, 0, right, bottom);
        mainRect.setStrokeType(StrokeType.INSIDE);
        mainRect.setStroke(Color.web("black", 0.1f));
        reservoirMapLayer.getChildren().add(mainRect);

        ObservableList<Node> reservoirMapLayerChildren = reservoirMapLayer.getChildren();
        double valueForPainting;
        for (int i = 1; i <= maxI; i++) {
            System.out.println("Row:" + i + "/" + maxI + "...");
            for (int j = 1; j <= maxJ; j++) {

                // Drawing inside a IJ cell
                for (int ky = 1; ky <= kySize; ky++) {
                    for (int kx = 1; kx <= kxSize; kx++) {

                        //int k = kIni - 1 + kx + (ky - 1) * kxSize; // left-to-right, bottom-up layout
//                        Integer test = new Integer(curve.getD(kx - 1, ky - 1));
//                        if (Integer.parseInt(curve.getD(kx - 1, ky - 1)) != null) {
//                            
//                        }
                        int k = 1 + curve.getD(kx - 1, ky - 1); // Peano-Hibert layout
                        double value = res.getValue(propertyName, i, j, k);

                        if (!res.isNull(i, j, k)) {
                            if (value >= filterMinValue && value <= filterMaxValue) {
                                //jeito antigo:
                                Rectangle r = new Rectangle(
                                        getX(i, xSize, kxSize, gapSize)
                                        + (kx - 1) * xSize,
                                        getY(bottom, j, ySize, kySize, gapSize)
                                        + (ky - 1) * ySize,
                                        xSize, ySize);

                                r.setStrokeType(StrokeType.INSIDE);
                                r.setStroke(Color.web("white", 0.1f));
                                valueForPainting = Math.max(Math.min(value, maxValue), minValue);
                                // Color palette: blue for minimum value, red for maximum value, hue-based linear palette
                                r.setFill(Color.hsb(240 - 240 * (valueForPainting - minValue) / (maxValue - minValue), 1, 1));

                                r.setStrokeWidth(1f);

                                reservoirMapLayer.getChildren().add(r);
                            }
                        }
                    }
                }

            }
        }

        Rectangle begin = new Rectangle(0, 0, 500, 700);
        begin.setStroke(Color.web("yellow"));

        ObservableList<Node> wellLayerChildren = wellLayer.getChildren();
        ObservableList<Node> wellLabelLayerChildren = wellLabelLayer.getChildren();
        
        List<Text> wellLabels = new ArrayList<>();
        for (Well well : wellList) {
            double x1 = getX(well.getiMin(), xSize, kxSize, gapSize);
            double y1 = getY(bottom, well.getjMin() - 1, ySize, kySize, gapSize);
            double x2 = getX(well.getiMax() + 1, xSize, kxSize, gapSize);
            double y2 = getY(bottom, well.getjMax(), ySize, kySize, gapSize);
            double deltaX = Math.abs(x2 - x1) - gapSize;
            double deltaY = Math.abs(y1 - y2) - gapSize;
            double x = Math.min(x1, x2);
            double y = Math.min(y1, y2);
            Rectangle r = new Rectangle(x, y, deltaX, deltaY);

            r.setStrokeType(StrokeType.OUTSIDE);
            String color;
            if (well.getType().equals(WellType.INJECTOR)) {
                color = "red";
            } else if (well.getType().equals(WellType.PRODUCER)) {
                color = "rgb(0,0,255)";//dark blue
            } else {
                color = "white";
            }
            r.setStroke(Color.web(color, 1f));
            r.setStrokeWidth(3d);
            r.setOpacity(1d);
            r.setFill(Color.web("white", 0f));
            //reservoirMapLayerChildren.add(r);
            wellLayerChildren.add(r);

            Text t = new Text(well.getName());
            t.setFont(Font.font("Verdana", 20));
            t.setFill(Color.WHITE);
            t.setFontSmoothingType(FontSmoothingType.LCD);
            t.setStroke(Color.BLACK);
            t.setStrokeLineCap(StrokeLineCap.ROUND);
            t.setStrokeLineJoin(StrokeLineJoin.ROUND);
            t.setStrokeType(StrokeType.OUTSIDE);
            t.setStrokeWidth(1d);
            t.setX(x);
            t.setY(y);
            
            boolean hasCollision = false;
             for (Text label: wellLabels) {
                 if
             (label.getBoundsInParent().intersects(t.getBoundsInParent())) {
                     hasCollision = true;
                 }
             }
             if (hasCollision) {
                 double yMax = Math.max(y1, y2);
                 t.setY(yMax+t.getBaselineOffset());
             }


             //reservoirMapLayerChildren.add(t);
             wellLabels.add(t);

            wellLabelLayerChildren.add(t);

        }
        
        // create main content
        Pane group = new Pane(
                reservoirMapLayer,
                wellLayer,
                wellLabelLayer
        );
        reservoirMapLayer.relocate(0, 0);
        wellLayer.relocate(0, 0);
        wellLabelLayer.relocate(0, 0);

        float scale = 1f;
        group.setScaleX(scale);
        group.setScaleY(scale);

        Text title = new Text("Reservoir Viewer");
        Pane top = new Pane(title);
        title.setFill(Color.web("white"));
        
        Pane scalePane = new Pane();
        // DRAWING THE SCALE COLOR BAR
        Rectangle scaleMainRect = new Rectangle(0, 0, 600, bottom);
         double yStart=scaleMainRect.getHeight()/2;
         double yEnd=scaleMainRect.getHeight()-50;
         mainRect.setStrokeType(StrokeType.INSIDE);
         //mainRect.setStroke(Color.web("black", 0.1f));
         mainRect.setStroke(Color.web("black", 0f));
         scalePane.getChildren().add(scaleMainRect);
         double scaleColorBarWidth = 100;
         double scaleColorBarStartX = 0;

         // - drawing the bar

         double scaleNumberOfSteps = 100;
         double interval = (yEnd-yStart)/scaleNumberOfSteps;
         for (int i=0; i<scaleNumberOfSteps; i++) {
             double y = yEnd-interval*i;
             Rectangle scaleItemRect = new
             Rectangle(scaleColorBarStartX, y-interval, scaleColorBarWidth, interval);
             scaleItemRect.setStrokeType(StrokeType.INSIDE);
             scaleItemRect.setStroke(Color.web("black", 0.1f));
             scaleItemRect.setFill(Color.hsb(240 - 240 * (double)i /
             (double)scaleNumberOfSteps, 1, 1));
             scalePane.getChildren().add(scaleItemRect);
         }

         // - Defining exponent "exp" to use in scientific notation :  n.10^exp
         int exp=0;
         double mantissa=maxValue;
         while (mantissa>=10) {
             exp+=1;
             mantissa = mantissa / 10d;
         }



         // - drawing ruler and numbers

         scaleNumberOfSteps = 4;
         double interval2 = (yEnd-yStart)/scaleNumberOfSteps;

         if (exp!=0) {  // draw a string "values X 10^exp" above the color bar
             Text t = new Text("Values \u00d7 10^"+exp);
             Font f = Font.font("Verdana", 60);
             t.setFont(f);
             t.setFill(Color.WHITE);
             t.setFontSmoothingType(FontSmoothingType.LCD);
             t.setStroke(Color.BLACK);
             t.setStrokeLineCap(StrokeLineCap.ROUND);
             t.setStrokeLineJoin(StrokeLineJoin.ROUND);
             t.setStrokeType(StrokeType.OUTSIDE);
             t.setStrokeWidth(gapSize);
             t.setX(scaleColorBarStartX+scaleColorBarWidth+25);
             t.setY(yStart-t.getBoundsInParent().getHeight()*2+t.getBaselineOffset()-(t.getBoundsInParent().getHeight())/2);
             scalePane.getChildren().add(t);
         }
         for (int i=0; i<=scaleNumberOfSteps; i++) {
             double y = yEnd-interval2*i;

             Line line = new Line(
            scaleColorBarStartX+scaleColorBarWidth-scaleColorBarWidth/2, y,scaleColorBarStartX+scaleColorBarWidth+20, y);
             line.setStroke(Color.WHITE);
             line.setStrokeWidth(5);
             scalePane.getChildren().add(line);


             double value = minValue + ((double)i /
             (double)scaleNumberOfSteps) * (maxValue-minValue);
//            System.out.println("original value="+value);
             value = value / Math.pow(10,exp); // converting to a mantissa
             value = Math.rint(value*1000)/1000;
//            System.out.println("rounded value="+value);
             String prefix="";
             if (i == 0 && value > 0) {
                 prefix = "\u2264 "; // less than or equal to
             } else if (i == scaleNumberOfSteps) {
                 prefix = "\u2265 "; // greater than or equal to
             }

             Text t = new Text(prefix+Double.toString(value));
             Font f = Font.font("Verdana", 60);
             t.setFont(f);
             t.setFill(Color.WHITE);
             t.setFontSmoothingType(FontSmoothingType.LCD);
             t.setStroke(Color.BLACK);
             t.setStrokeLineCap(StrokeLineCap.ROUND);
             t.setStrokeLineJoin(StrokeLineJoin.ROUND);
             t.setStrokeType(StrokeType.OUTSIDE);
             t.setStrokeWidth(gapSize);
             t.setX(scaleColorBarStartX+scaleColorBarWidth+25);
             t.setY(y+t.getBaselineOffset()-(t.getBoundsInParent().getHeight())/2);
             scalePane.getChildren().add(t);

         }

        BorderPane mainPane = new BorderPane(group, top, null, null, null);

        Scene scene = new Scene(mainPane);
        
        saveScene(scene, configuracao, property, wellList);
    }
    
        /**
     * Metodo que desenha uma visualização do tipo Pixelization
     *
     * @param configuracao
     * @param res
     * @param clusteringData
     * @param wellList
     *
     */
    private void DrawPixelization1(Configuracao configuracao, ReservoirMap res,
            ClusteringData clusteringData, WellList wellList, Property property) throws IOException {
        
        //arquivo de progresso
        FileWriter logFile = new FileWriter(configuracao.getRoot() + File.separator + configuracao.getProgressFile());
        PrintWriter logFileWriter = new PrintWriter(logFile);
        logFileWriter.printf("50:ProgRVDesInit" + property.getName() + "\n");
//        logFile.close();

         int maxI = res.getMaxI();
         int maxJ = res.getMaxJ();
         int maxK = res.getMaxK();

         ICurve curve = null;

        // Obtem o tipo de curva usado na visualização
        String layoutCurve = configuracao.getLayoutCurve().toUpperCase();

         double actualMaxValue = res.getMaxValue(property.getName());
         double actualMinValue = res.getMinValue(property.getName());
         double filterMaxValue = 1.0 * (actualMaxValue - actualMinValue) + actualMinValue;
         double filterMinValue = 0.0 * (actualMaxValue - actualMinValue) + actualMinValue;

         ReservoirMap.Interval maxMinInterval = res.getInterval(property.getName(), 0.05, 0.95);
         //ReservoirMap.Interval maxMinInterval = res.getInterval(property, 0, 1);
         double maxValue = maxMinInterval.end;
         double minValue = maxMinInterval.start;

         Dimension dimension;
        int kxSize = 0;
        int kySize = 0;

        switch (layoutCurve.toUpperCase()) {
            case "PEANOHILBERT":
                curve = new PeanoHilbertCurve(maxK);
                // WITH PEANO-HILBERT CURVE
                dimension = curve.getDimension();
                kxSize = dimension.x;
                kySize = dimension.y;
                break;
            case "PSEUDOPEANOHILBERT":
                curve = new PseudoPeanoHilbertCurve(maxK);
                // WITH PEANO-HILBERT CURVE
                dimension = curve.getDimension();
                kxSize = dimension.x;
                kySize = dimension.y;
                break;
            case "SNAKE":
                curve = new SnakeCurve(maxK);
                // WITHOUT PEANO-HILBERT CURVE
                int kIni = 1;
                int kFin = maxK; // for many models
                int deltaK = kFin - kIni + 1;
                kxSize = (int) Math.round(Math.sqrt(deltaK));
                double d = Math.ceil((double) deltaK / kxSize);
                kySize = (int) d;
                
//                dimension = curve.getDimension();
//                kxSize = dimension.x;
//                kySize = dimension.y;
                break;
            default:
                throw new IllegalArgumentException("Layout Curve não implementado");
        }
         
         double cellSize = 50; // a 50 xMin 50 cell;
         
         double cellGapSize = 3;
         double subCellGapSize = 0;

         double xSize=(cellSize-cellGapSize)/kxSize;
         double ySize=(cellSize-cellGapSize)/kySize;
         
//         double xSize=(cellSize-cellGapSize);
//         double ySize=(cellSize-cellGapSize);


         //double zoom = .7; // TODO should be automatically calculated based on the window height and width
         //double zoom = 1; // TODO should be automatically calculated based on the window height and width

//        double xSize = kySize / maxkxkySize * zoom;
//        double ySize = kxSize / maxkxkySize * zoom;

         //int mainCellSize = size * 10;


         //gapSize = 1; //for ONEMODEL
         //float mainCellSizePlusGap = mainCellSize + gapSize;
////        int bottom = (int) (ySize * kySize + gapSize) * maxJ;
////        int right = (int) (xSize * kxSize + gapSize) * maxI;
//        int bottom = (int) (ySize * maxJ + gapSize) * kySize;
//        int right = (int) (xSize * maxI + gapSize) * kxSize;
         double bottom = -getY(0, maxJ, ySize, kySize, cellGapSize);
         double right = getX(maxI + 1, xSize, kxSize, cellGapSize);

         //Pane root = new Pane();
         Pane reservoirMapLayer = new Pane();
         Pane wellLayer = new Pane();
         Pane wellLabelLayer = new Pane();

         Rectangle mainRect = new Rectangle(0, 0, right, bottom);
         mainRect.setStrokeType(StrokeType.INSIDE);
         mainRect.setStroke(Color.web("black", 0.1f));
         reservoirMapLayer.getChildren().add(mainRect);

         ObservableList<Node> reservoirMapLayerChildren =
         reservoirMapLayer.getChildren();
         double valueForPainting;
         for (int i = 1; i <= maxI; i++) {
             System.out.println("Row:" + i + "/" + maxI + "...");
             for (int j = 1; j <= maxJ; j++) {

                 // Drawing inside a IJ cell
                 for (int ky = 1; ky <= kySize; ky++) {
                     for (int kx = 1; kx <= kxSize; kx++) {

                         //int k = kIni - 1 + kx + (ky - 1) * kxSize; // left-to-right, bottom-up layout
                         int k = 1 + curve.getD(kx - 1, ky - 1); // Peano-Hibert layout
                         double value = res.getValue(property.getName(), i, j, k);

                         if (!res.isNull(i, j, k)) {
                             if (value >= filterMinValue && value <= filterMaxValue) {
                                 //Rectangle r = new Rectangle(i * mainCellSizePlusGap + ky * size, j * mainCellSizePlusGap + kx * size, size, size);
                                 Rectangle r = new Rectangle(
                                         //i * ((xSize * kxSize + gapSize) - 1)
                                         getX(i, xSize, kxSize, cellGapSize)
                                         + (kx - 1) * xSize,
                                         //bottom - j * ((ySize * kySize + gapSize) - 1)
                                         getY(bottom, j, ySize, kySize, cellGapSize)
                                         + (ky - 1) * ySize, xSize-subCellGapSize, ySize-subCellGapSize);

                                 r.setStrokeType(StrokeType.INSIDE);
                                 r.setStroke(Color.web("white", 0.1f));
                                 valueForPainting = Math.max(Math.min(value, maxValue), minValue);
                                 // Color palette: blue for minimum value, red for maximum value, hue-based linear palette
                                 r.setFill(Color.hsb(240 - 240 * (valueForPainting - minValue) / (maxValue - minValue), 1, 1));

                                 r.setStrokeWidth(1f);
                                //reservoirMapLayer.getChildren().add(r);
                                reservoirMapLayerChildren.add(r);
                             }
                         }
                     }
                 }

             }
         }

         Rectangle begin = new Rectangle(0, 0, 500, 700);
         begin.setStroke(Color.web("yellow"));

//        double cellSizeX = xSize * kxSize;
//        double cellSizeY = ySize * kySize;
         ObservableList<Node> wellLayerChildren = wellLayer.getChildren();
         ObservableList<Node> wellLabelLayerChildren = wellLabelLayer.getChildren();

         List<Text> wellLabels = new ArrayList<>();
         
         if (wellList != null ) {
         for (Well well : wellList) {
             double x1 = getX(well.getiMin(), xSize, kxSize, cellGapSize);
             double y1 = getY(bottom, well.getjMin() - 1, ySize, kySize,cellGapSize);
             double x2 = getX(well.getiMax() + 1, xSize, kxSize,cellGapSize);
             double y2 = getY(bottom, well.getjMax(), ySize, kySize,cellGapSize);
             double deltaX = Math.abs(x2 - x1) - cellGapSize;
             double deltaY = Math.abs(y1 - y2) - cellGapSize;
             double xMin = Math.min(x1, x2);
             double yMin = Math.min(y1, y2);
             Rectangle r = new Rectangle(xMin, yMin, deltaX, deltaY);

//                    getY(bottom, well.getjMin(), ySize, kySize, gapSize),
//                    getX(well.getiMin(), xSize, kxSize, gapSize),
             //(cellSizeX+gapSize)*(well.getiMax() - well.getiMin() + 1)-gapSize,
             //(cellSizeY+gapSize)*(well.getjMax() - well.getjMin() + 1)-gapSize
             //);
             r.setStrokeType(StrokeType.OUTSIDE);
             String color = "black";
             if (well.getType().equals(WellType.INJECTOR)) {
                 color = "white";
                 //r.getStrokeDashArray().addAll(10.0, 10.0, 5.0, 10.0);
             r.getStrokeDashArray().addAll(20d,25d,5d,25d);
                 //r.setStrokeDashOffset(100);
             } else if (well.getType().equals(WellType.PRODUCER)) {
//                color = "rgb(0,0,255)";//dark blue
                   color = "white";
             } else {
                 color = "white";
             }
             r.setStroke(Color.web(color, 1f));
             r.setStrokeWidth(3*cellGapSize);
             r.setOpacity(1d);
             r.setFill(Color.web("white", 0f));
             //reservoirMapLayerChildren.add(r);
             wellLayerChildren.add(r);

             Text t = new Text(well.getName());
             Font f = Font.font("Verdana", 60);
             t.setFont(f);
             t.setFill(Color.WHITE);
             t.setFontSmoothingType(FontSmoothingType.LCD);
             t.setStroke(Color.BLACK);
             t.setStrokeLineCap(StrokeLineCap.ROUND);
             t.setStrokeLineJoin(StrokeLineJoin.ROUND);
             t.setStrokeType(StrokeType.OUTSIDE);
             t.setStrokeWidth(cellGapSize);
             t.setX(xMin);
             t.setY(yMin);


             // Check if a label intersects other. If so, moves it to the "bottom" of the well
             // Obs.: This is not a robust solution and should be improved.
             boolean hasCollision = false;
             for (Text label: wellLabels) {
                 if
             (label.getBoundsInParent().intersects(t.getBoundsInParent())) {
                     hasCollision = true;
                 }
             }
             if (hasCollision) {
                 double yMax = Math.max(y1, y2);
                 t.setY(yMax+t.getBaselineOffset());
             }


             //reservoirMapLayerChildren.add(t);
             wellLabels.add(t);
             wellLabelLayerChildren.add(t);

         }
         }

//      for (Well well : wellList) {
//            for (IJKKey key : well.getCoordinates()) {
//                Rectangle r = new Rectangle(
//                        getX(key.i, xSize, kxSize, gapSize),
//                        getY(bottom, key.j, ySize, kySize, gapSize),
//                        cellSizeX,
//                        cellSizeY);
//                r.setStrokeType(StrokeType.OUTSIDE);
//                String color;
//                if (well.getType().equals(WellType.INJECTOR)) {
//                    color = "red";
//                } else if (well.getType().equals(WellType.PRODUCER)) {
//                    color = "green";
//                } else {
//                    color = "white";
//                }
//                r.setStroke(Color.web(color, 1f));
//                r.setStrokeWidth(3f);
//                r.setOpacity(0.3);
//                reservoirMapLayerChildren.add(r);
//            }
//        }
         // create main content
         Pane group = new Pane(
                 reservoirMapLayer,
                 wellLayer,
                 wellLabelLayer
         );
         reservoirMapLayer.relocate(0, 0);
         wellLayer.relocate(0, 0);
         wellLabelLayer.relocate(0, 0);

         //group.setLayoutX(0);
         //group.setLayoutY(0);
         float scale = 1f;
         group.setScaleX(scale);
         group.setScaleY(scale);
         //group.setTranslateX(0);
         //group.setTranslateY(0);

         Text title = new Text("Reservoir Viewer");
         Pane top = new Pane(title);
         title.setFill(Color.web("black"));

         Pane scalePane = new Pane();

         // DRAWING THE SCALE COLOR BAR
         Rectangle scaleMainRect = new Rectangle(0, 0, 800, bottom);
         double yStart=scaleMainRect.getHeight()/2;
         double yEnd=scaleMainRect.getHeight()-50;
         mainRect.setStrokeType(StrokeType.INSIDE);
         //mainRect.setStroke(Color.web("black", 0.1f));
         mainRect.setStroke(Color.web("black", 0f));
         scalePane.getChildren().add(scaleMainRect);
         double scaleColorBarWidth = 100;
         double scaleColorBarStartX = 0;

         // - drawing the bar
         double scaleNumberOfSteps = 100;
         double interval = (yEnd-yStart)/scaleNumberOfSteps;
         for (int i=0; i<scaleNumberOfSteps; i++) {
             double y = yEnd-interval*i;
             Rectangle scaleItemRect = new
             Rectangle(scaleColorBarStartX, y-interval, scaleColorBarWidth, interval);
             scaleItemRect.setStrokeType(StrokeType.INSIDE);
             scaleItemRect.setStroke(Color.web("black", 0.1f));
             scaleItemRect.setFill(Color.hsb(240 - 240 * (double)i /
             (double)scaleNumberOfSteps, 1, 1));
             scalePane.getChildren().add(scaleItemRect);
         }

         // - Defining exponent "exp" to use in scientific notation :  n.10^exp
         int exp=0;
         double mantissa=maxValue;
         while (mantissa>=10) {
             exp+=1;
             mantissa = mantissa / 10d;
         }

         // - drawing ruler and numbers
         scaleNumberOfSteps = 4;
         double interval2 = (yEnd-yStart)/scaleNumberOfSteps;
         
         Text tBenchmark = new Text(configuracao.getBenchmark() != null ? configuracao.getBenchmark() : "");
             Font fBenchmark = Font.font("Verdana", 60);
             tBenchmark.setFont(fBenchmark);
             tBenchmark.setFill(Color.WHITE);
             tBenchmark.setFontSmoothingType(FontSmoothingType.LCD);
             tBenchmark.setStroke(Color.BLACK);
             tBenchmark.setStrokeLineCap(StrokeLineCap.ROUND);
             tBenchmark.setStrokeLineJoin(StrokeLineJoin.ROUND);
             tBenchmark.setStrokeType(StrokeType.OUTSIDE);
             tBenchmark.setStrokeWidth(cellGapSize);
             tBenchmark.setX(scaleColorBarStartX+scaleColorBarWidth+25);
             tBenchmark.setY(yStart-tBenchmark.getBoundsInParent().getHeight()*4+tBenchmark.getBaselineOffset()-(tBenchmark.getBoundsInParent().getHeight())/2);
             scalePane.getChildren().add(tBenchmark);
         if (exp!=0) {  // draw a string "values X 10^exp" above the color bar
             Text t = new Text("Values \u00d7 10^"+exp);
             Font f = Font.font("Verdana", 60);
             t.setFont(f);
             t.setFill(Color.WHITE);
             t.setFontSmoothingType(FontSmoothingType.LCD);
             t.setStroke(Color.BLACK);
             t.setStrokeLineCap(StrokeLineCap.ROUND);
             t.setStrokeLineJoin(StrokeLineJoin.ROUND);
             t.setStrokeType(StrokeType.OUTSIDE);
             t.setStrokeWidth(cellGapSize);
             t.setX(scaleColorBarStartX+scaleColorBarWidth+25);
             t.setY(yStart-t.getBoundsInParent().getHeight()*2+t.getBaselineOffset()-(t.getBoundsInParent().getHeight())/2);
             scalePane.getChildren().add(t);
         }
         for (int i=0; i<=scaleNumberOfSteps; i++) {
             double y = yEnd-interval2*i;

             Line line = new Line(
            scaleColorBarStartX+scaleColorBarWidth-scaleColorBarWidth/2, y,scaleColorBarStartX+scaleColorBarWidth+20, y);
             line.setStroke(Color.WHITE);
             line.setStrokeWidth(5);
             scalePane.getChildren().add(line);


             double value = minValue + ((double)i /
             (double)scaleNumberOfSteps) * (maxValue-minValue);
//            System.out.println("original value="+value);
             value = value / Math.pow(10,exp); // converting to a mantissa
             value = Math.rint(value*1000)/1000;
//            System.out.println("rounded value="+value);
             String prefix="";
             if (i == 0 && value > 0) {
                 prefix = "\u2264 "; // less than or equal to
             } else if (i == scaleNumberOfSteps) {
                 prefix = "\u2265 "; // greater than or equal to
             }

             Text t = new Text(prefix+Double.toString(value));
             Font f = Font.font("Verdana", 60);
             t.setFont(f);
             t.setFill(Color.WHITE);
             t.setFontSmoothingType(FontSmoothingType.LCD);
             t.setStroke(Color.BLACK);
             t.setStrokeLineCap(StrokeLineCap.ROUND);
             t.setStrokeLineJoin(StrokeLineJoin.ROUND);
             t.setStrokeType(StrokeType.OUTSIDE);
             t.setStrokeWidth(cellGapSize);
             t.setX(scaleColorBarStartX+scaleColorBarWidth+25);
             t.setY(y+t.getBaselineOffset()-(t.getBoundsInParent().getHeight())/2);
             scalePane.getChildren().add(t);

         }
         
         Insets insets = new Insets(200);

         BorderPane mainPane = new BorderPane();
         mainPane.setCenter(group);
         mainPane.setRight(scalePane);
         mainPane.setMargin(scalePane, insets);
         mainPane.setTop(top);
         
         mainPane.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), Insets.EMPTY)));

         //primaryStage.setResizable(true);
         //primaryStage.setMaximized(true);
         //Scene scene = new Scene(group, WIDTH, HEIGHT);
         Scene scene = new Scene(mainPane);
         
         //arquivo de progresso
//        FileWriter logFile = new FileWriter(configuracao.getRoot() + File.separator + configuracao.getProgressFile());
//        PrintWriter logFileWriter = new PrintWriter(logFile);
        logFileWriter.printf("75:ProgRVDesFim" + property.getName() + "\n");
        logFile.close();
         
         saveScene(scene, configuracao, property, wellList);
         //primaryStage.setScene(scene);
     }

    /**
     * Método que faz o processo de desenho para many models
     * Levando em consideração:
     * - O tipo de visualização
     * - O algoritmo de reordenação
     *
     * @param configuracao
     */
    public void DrawManyodels(Configuracao configuracao) throws IOException {
        
        Clustering clustering = new Clustering();
        ReservoirMap res = new ReservoirMap();
        ClusteringData clusteringData = null;
        List<WellList> estrategias = new ArrayList<WellList>();
        
        if (configuracao.getStrategies() != null) {
            for (int i = 0 ; i < configuracao.getStrategies().size() ; i++) {
                estrategias.add(new WellList());
                estrategias.get(i).setStrategyName(configuracao.getStrategies().get(i).getName());
                String path = configuracao.getStrategies().get(i).getPath();
                estrategias.get(i).loadFile(path);
            }
        }
 
        String chartType = configuracao.getChartType();
        
        for (Property property : configuracao.getProperties()) {
            
//            for (WellList wellList : estrategias) {
            
            int meanType = getMeanType(property);
            res.loadStaticMapManyModels(
                property.getName(),
                configuracao.getRoot()
                + File.separator
                + configuracao.getFolder2d()
                + File.separator
                + property.getFile2d(), 
                meanType
                );
            
            //leitura dos null blocks:
            res.loadStaticMapManyModels("null", configuracao.getRoot()
                                    + File.separator
                                    + configuracao.getFolder2d()
                                    + File.separator
                                    + configuracao.getNullBlocks(),
                                    meanType);
            
            //caso passe distance matrix:
            if (configuracao.getClusteringConfig().getDistanceMatrix().equals("MODELS3D_ALL_PROP") || configuracao.getClusteringConfig().getDistanceMatrix().equals("MODELS3D_PROP")) {
                
                //arquivo de progresso
                FileWriter logFile = new FileWriter(configuracao.getRoot() + File.separator + configuracao.getProgressFile());
                PrintWriter logFileWriter = new PrintWriter(logFile);
                logFileWriter.printf("25:ProgRVClstInit\n");
//                logFile.close();
                
                String distanceMatrixFileName = property.getFileDistanceMatrix();
                String distanceMatrixPath = configuracao.getRoot()
                    + File.separator
                    + configuracao.getFolderDistanceMatrix()
                    + File.separator
                    + distanceMatrixFileName;
                clusteringData = clustering.clusterReservoirsByMatrixFile(distanceMatrixPath,configuracao,false);
                reorderReservoirsByClusters(res, clusteringData);
                
                //arquivo de progresso
                logFileWriter.printf("50:ProgRVClstFin\n");
                logFile.close();
            }
            
            //caso passe feature vector:
            if (configuracao.getClusteringConfig().getDistanceMatrix().equals("FEATVECTORS_PROP")) {
                
                //arquivo de progresso
                FileWriter logFile = new FileWriter(configuracao.getRoot() + File.separator + configuracao.getProgressFile());
                PrintWriter logFileWriter = new PrintWriter(logFile);
                logFileWriter.printf("25:ProgRVClstInit\n");
//                logFile.close();
                
                String featureVectorFile = clustering.createReservoirFeatureVectorMatrix(configuracao, res, property);
                clusteringData = clustering.clusterReservoirsByFeatureVectors(featureVectorFile, configuracao);
                reorderReservoirsByClusters(res, clusteringData);
                
                //arquivo de progresso
                logFileWriter.printf("50:ProgRVClstFin\n");
                logFile.close();
            }
            
            switch (chartType.toLowerCase()) {
                case "pixelization":
//                    try {
                        if (configuracao.getStrategies() != null) {
                            for (WellList wellList : estrategias) {
                                DrawPixelization1(configuracao, res, clusteringData, wellList, property);
                            }
                        } else {
                            DrawPixelization1(configuracao, res, clusteringData, null, property);
                        }
                        
                        break;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("abortando");
//                        break;
//                    }
                case "smallmultiples":
//                    try {
                        // Efetivamente Desenha
                        DrawSmallMultiples(configuracao, res, clusteringData, property);
                        break;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("abortando");
//                        break;
//                    }
                default:
                    throw new IllegalArgumentException("Chart Type não implementado");
            }
            
            Runtime.getRuntime().gc();
            }
//        }
    }
    
    /**
     * Método responsável por obter a lista dos modelos representativos
     * 
     * @param configuracao
     *
     * @return
     * Uma Lista contendo os modelos representativos
     */
    private List<Integer> getRepresentativeModelsList(Configuracao configuracao) {
        List<Integer> models = new ArrayList<>();

        BufferedReader inputFile;
        String[] values;

        // Define reservoir valid cells.
        try {
            inputFile = new BufferedReader(new FileReader(configuracao.getRoot() + File.separator + configuracao.getHighlightedModels()));
            String line;
            int lineNumber = 0;
            while ((line = inputFile.readLine()) != null) {
                lineNumber++;
                if (!line.startsWith("#")) {
                    values = line.split(" ");
                    if (values.length != 2) {
                        System.out.println("Line number " + lineNumber
                                + " ignored (2 values are expected: model "
                                + "id and name");
                    } else {
                        // Name is not used
                        int id = Integer.parseInt(values[0]);
                        String name = values[1];
                        models.add(id);
                        System.out.println("Setting model " + id + " (" + name
                                + ") as representative."); // debug only
                    }
                }
            }
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }

        return models;
    }
    
    /**
     * Método responsável por calcular a ordem que os reservatórios serão
     * apresentados com base na matrix de distancia
     * 
     * @param distanceMatrix_name
     * @param meanType
     * @param res
     * @param sortingAlgorithm
     * @param configuracao
     * @return 
     */
    private int[] calculateReservoirOrderByDistanceMatrix(
            String distanceMatrix_name, int meanType, ReservoirMap res,
            IMatrixReorderingAlgorithm sortingAlgorithm,
            Configuracao configuracao) {

        // Caminho até a pasta;
        String distanceMatrixPath = configuracao.getRoot() + File.pathSeparator;
        distanceMatrixPath += configuracao.getFolderDistanceMatrix()
                + File.pathSeparator;

        if (distanceMatrix_name == null) {
            throw new IllegalArgumentException("DistanceMatrix não pode ser null");
        }

        // Nome do arquivo distance matrix
        String distanceMatrixFileName = distanceMatrix_name;

        // para one model ?
        Boolean isManyModels = Boolean.TRUE;

        if (isManyModels) {
            distanceMatrixPath += "many_models";
        } else {
            distanceMatrixPath += "one_model" + File.pathSeparator;
        }

        File distanceMatrixFile = new File(distanceMatrixPath
                + distanceMatrix_name);

        int[] reservoirOrder;
        if (distanceMatrixFile.exists()) {
            IReorderableMatrix distanceMatrix
                    = new ReorderableMatrix(distanceMatrixFile, true);//file with column and row labels

            distanceMatrix = sortingAlgorithm.sort(distanceMatrix);
            reservoirOrder = distanceMatrix.getColumnOrder();
        } else {
            System.out.println("Distance matrix file " + distanceMatrixFileName
                    + " not found. Reservoir reordering disabled.");
            reservoirOrder = new int[res.getMaxK()];
            for (int i = 0; i < reservoirOrder.length; i++) {
                reservoirOrder[i] = i;
            }
        }
        return reservoirOrder;
    }


    /**
     * Método responsável por Reordenar o mapa de reservátorios
     * 
     * @param res
     * @param newReservoirOrder 
     */
    private void reorderReservoirs(ReservoirMap res, int newReservoirOrder[]) {
        for (int k = 0; k < newReservoirOrder.length; k++) {
            res.setRealKPointer(k + 1, newReservoirOrder[k] + 1);
        }
    }
    
    /**
     * Método responsável por Reordenar o mapa de Reservátórios com base nos
     * Cluster
     * 
     * @param res
     * @param clusteringData 
     */
    private void reorderReservoirsByClusters(ReservoirMap res,
            ClusteringData clusteringData) {
        int k = 0;
        int[] newReservoirOrder = new int[res.getMaxK()];
        if (clusteringData != null) {
            //for (Integer clusterId : clusteringData.getClusterIdSet()) {
            for (Integer clusterId : clusteringData
                    .getClusterIdListOrderBySize()) {

                List<Integer> dataIdList = clusteringData
                        .getInstancesId(clusterId);

                for (Integer dataId : dataIdList) {
                    newReservoirOrder[k] = dataId;
                    k++;
                }
            }
//            for (Map.Entry<Integer, List<Integer>> entry : clusterMap.entrySet()) {
//                List<Integer> dataIdList = entry.getValue();
//                for (Integer dataId : dataIdList) {
//                    newReservoirOrder[k]=dataId;
//                    k++;
//                }
//            }
            reorderReservoirs(res, newReservoirOrder);
        }
    }

    /**
     * Método responsável por Obter os dados da dimensão X para visualizações do
     * tipo Small Multiples
     * 
     * @param kx
     * @param xSize
     * @param iMax
     * @param gapSize
     * @return 
     */
    private double getXSmallMultiples(int kx, double xSize, int iMax,
            double gapSize) {
        // (tamanho horizontal do reservatorio * xSize + gap) * (xSize-1)
        return (kx - 1) * ((xSize * iMax + gapSize) - 1);
        //return (i - 1) * ((xSize * kxSize + gapSize) - 1);
    }

    /**
     * Método responsável por Obter os dados da dimensão Y para visualizações do
     * tipo Small Multiples
     * 
     * @param bottom
     * @param ky
     * @param ySize
     * @param jMax
     * @param gapSize
     * @return 
     */
    private double getYSmallMultiples(double bottom, int ky, double ySize,
            int jMax, double gapSize) {
        return (ky - 1) * ((ySize * jMax + gapSize) - 1);
        //return bottom - (ky -1) * ((ySize * jMax + gapSize) - 1);
        //return bottom - j * ((ySize * kySize + gapSize) - 1);
    }

    /**
     * Método responsável por obter os dados da dimensão X para visualizações
     * diferetes de Small Multiplles
     * 
     * @param i
     * @param xSize
     * @param kxSize
     * @param gapSize
     * @return 
     */
    private double getX(int i, double xSize, int kxSize, double gapSize) {
        return (i - 1) * ((xSize * kxSize + gapSize) - 1);
    }

    /**
     * Método responsável por obter os dados da dimensão Y para visualizações
     * diferetes de Small Multiplles 
     * 
     * @param bottom
     * @param j
     * @param ySize
     * @param kySize
     * @param gapSize
     * @return 
     */
    
    private double getY(double bottom, int j, double ySize, int kySize, double gapSize) {
        return bottom - j * ((ySize * kySize + gapSize) - 1);
    }

    /**
     * Método responsável por salvar a JavaFX Scene contendo a visualização
     * 
     * @param scene
     * @param configuracao 
     */
    public void saveScene(Scene scene, Configuracao configuracao, Property property, WellList wellList) throws IOException {
        
        //arquivo de progresso
        FileWriter logFile = new FileWriter(configuracao.getRoot() + File.separator + configuracao.getProgressFile());
        PrintWriter logFileWriter = new PrintWriter(logFile);
        logFileWriter.printf("75:ProgRVSlvn" + property.getName() + "\n");
        logFile.close();

        System.out.println("Saving image...");

        // String base para o nome da imagem da visualização
        String fileName = fixPngName(configuracao.getPngFileName(),property, configuracao, wellList);
        String fileNameFinal = configuracao.getRoot() + File.separator + fileName;
        new File(fileNameFinal).mkdirs();
        File file = new File(fileNameFinal);

        WritableImage snapImage = scene.snapshot(null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapImage, null),"png", file);
            System.out.println("Image was saved.");
        } catch (IOException ex) {
            Logger.getLogger(snapImage.getClass().getName()).log(Level.SEVERE,null, ex);
        }
    }
    
    private String fixPngName (String name, Property property, Configuracao configuracao, WellList wellList) {
        String finalName = name.replace("[PROP]", property.getName() != null ? property.getName().toUpperCase() : "_" )
                                .replace("[CHART]", configuracao.getChartType() != null ? configuracao.getChartType().toUpperCase() : "" )
                                .replace("[CURVE]", configuracao.getLayoutCurve() != null ? configuracao.getLayoutCurve().toUpperCase() : "" )
                                .replace("[FUNC]", property.getFunction() != null ? property.getFunction().toUpperCase() : "" )
                                .replace("[CLUSTERING]", configuracao.getClusteringConfig().getXsiType() != null ? configuracao.getClusteringConfig().getXsiType().toUpperCase() : "" )
                                .replace("[STRATEGY]", wellList != null ? wellList.getStrategyName().toUpperCase() : "NO-STRATEGY")
                                ;
        return finalName;
    }

    private int getMeanType(Property property) {
        switch (property.getFunction()) {
            case "MIN":
                return 3;
            case "MAX":
                return 4;
            case "SUM":    
                return 5;
            case "STDDEV":
                return 6;
            case "ARITHMETICMEAN":
                return 7;
            case "GEOMETRICMEAN":
                return 8;
            case "HARMONICMEAN":
                return 9;
            case "MODE":
                return 10;

            default:
                throw new IllegalArgumentException(property.getFunction()
                        + " Não é um tipo conhecido");

        }
    }

}
