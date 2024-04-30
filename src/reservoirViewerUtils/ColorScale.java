/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reservoirViewerUtils;

import javafx.scene.paint.Color;

/**
 *
 * @author kleyt
 */
public class ColorScale {
    private double max;
    private double min;
    private double ceil = Double.NEGATIVE_INFINITY;
    
    public ColorScale(){
        this(5.0, -5.0);
    }
    
    public ColorScale(double max, double min){
        this.max = max;
        this.min = min;
    }
    
    private class ColorParameters{
        public double s = 1.0;
        public double b = 1.0;
        public double color = 0.0;
        
        public ColorParameters(double col){
            this(col, 1, 1);
        }

        public ColorParameters(double col, double sat){
            this(col, sat, 1);
        }
        
        public ColorParameters(double col, double sat, double bri){
            this.s = sat;
            this.color = col;
            this.b = bri;
        }
    }
    
    private double getCeil() {
        if (ceil == Double.NEGATIVE_INFINITY) {
            double max = Math.abs(this.max);
            double min = Math.abs(this.min);
            ceil = Math.max(max, min);
        }
        
        return ceil;
    }
    
    private ColorParameters getPallete(double value){
        double calc = 0;
        if ((calc = value/max) * 5 > 1.5) {
            return new ColorParameters(240 - ((225 * calc) - 1.5) / 3.5);
        }
        else if (calc * 5 > 1.0) {
            return new ColorParameters(240, (calc * 5) - 0.5, (calc * 5) - 0.5);
        }
        else if (calc > 0.0){
            return new ColorParameters(240, calc * 2.5, 1 - (calc*2.5));
        }
        if ((calc = value/min) * 5 <= 1.0) {
            return new ColorParameters(0, calc * 2.5, 1 - (calc*2.5));
        }
        if (calc * 5 <= 1.5) {
            return new ColorParameters(0, (calc * 5) - 0.5, (calc * 5) - 0.5);
        }
        return new ColorParameters(0 + ((100 * calc) - 1.5) / 3.5);
    }
    
    public Color getColor(double val) {
        ColorParameters pallete = getPallete(val);
        
        return Color.hsb(pallete.color, pallete.s, pallete.b);
    }
}
