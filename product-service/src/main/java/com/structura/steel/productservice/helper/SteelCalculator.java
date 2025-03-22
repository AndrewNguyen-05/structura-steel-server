package com.structura.steel.productservice.helper;

import com.structura.steel.productservice.dto.request.ProductRequestDto;

public class SteelCalculator {

    // Steel density in kg/m^3
    private static final double STEEL_DENSITY = 7850.0;

    /**
     * Calculate steel weight (in kg) based on the given ProductRequestDto.
     * @param dto Information about the product (name, diameter, thickness, etc.)
     * @return weight (kg)
     */
    public static double calculateSteelWeight(ProductRequestDto dto) {
        if (dto == null || dto.getName() == null) {
            throw new IllegalArgumentException("Invalid product data. Name cannot be null.");
        }

        String lowerName = dto.getName().toLowerCase();

        // 1) Deformed bar / rebar (thép thanh vằn / sắt thép cây)
        if (lowerName.contains("vằn") || lowerName.contains("cây")) {
            if (dto.getDiameter() == null) {
                throw new IllegalArgumentException("Diameter is required for rebar products.");
            }
            double diameterMeter = dto.getDiameter() / 1000.0; // mm -> m
            double crossSectionArea = Math.PI * Math.pow(diameterMeter, 2) / 4.0; // m^2
            double unitWeight = crossSectionArea * STEEL_DENSITY;                 // kg/m
            return unitWeight * dto.getLength();                                  // kg

            // 2) Coil / Plate (thép cuộn / thép tấm)
        } else if (lowerName.contains("cuộn") || lowerName.contains("tấm")) {
            if (dto.getThickness() == null || dto.getWidth() == null) {
                throw new IllegalArgumentException("Thickness and width are required for coil/plate products.");
            }
            double thicknessMeter = dto.getThickness() / 1000.0;
            double widthMeter = dto.getWidth() / 1000.0;
            double volume = thicknessMeter * widthMeter * dto.getLength(); // m^3
            return volume * STEEL_DENSITY;                                 // kg

            // 3) Pipe / Box (thép ống / thép hộp)
        } else if (lowerName.contains("ống") || lowerName.contains("hộp")) {

            // 3a) Round pipe (ống tròn)
            if (lowerName.contains("ống")) {
                if (dto.getDiameter() == null || dto.getThickness() == null) {
                    throw new IllegalArgumentException("Diameter and thickness are required for round pipe products.");
                }
                double d = dto.getDiameter();  // mm
                double t = dto.getThickness(); // mm
                // Outer
                double outerRadius = (d / 2.0) / 1000.0; // m
                double outerArea = Math.PI * outerRadius * outerRadius; // m^2
                // Inner
                double innerDiameter = d - 2.0 * t; // mm
                double innerRadius = (innerDiameter / 2.0) / 1000.0; // m
                double innerArea = (innerDiameter > 0)
                        ? Math.PI * innerRadius * innerRadius
                        : 0.0;
                double crossSectionArea = outerArea - innerArea; // m^2
                double unitWeight = crossSectionArea * STEEL_DENSITY; // kg/m
                return unitWeight * dto.getLength(); // kg

                // 3b) Square/rectangular box (hộp vuông/chữ nhật)
            } else {
                if (dto.getWidth() == null || dto.getThickness() == null) {
                    throw new IllegalArgumentException("Width and thickness are required for box (square/rectangular) products.");
                }
                double w = dto.getWidth();     // mm
                double t = dto.getThickness(); // mm
                // Outer side
                double outerSide = w / 1000.0; // m
                double outerArea = outerSide * outerSide; // m^2
                // Inner side
                double innerSide = (w - 2.0 * t) / 1000.0; // m
                double innerArea = (innerSide > 0) ? (innerSide * innerSide) : 0.0;
                double crossSectionArea = outerArea - innerArea; // m^2
                double unitWeight = crossSectionArea * STEEL_DENSITY; // kg/m
                return unitWeight * dto.getLength(); // kg
            }

            // 4) Structural steel (I, H, U, L, etc.)
        } else if (lowerName.contains("hình")) {
            // Usually, we have a reference table for kg/m. So let's rely on unitWeight field.
            if (dto.getUnitWeight() == null) {
                throw new IllegalArgumentException("unitWeight (kg/m) is required for structural steel products.");
            }
            return dto.getUnitWeight() * dto.getLength();
        }

        // Unknown type
        throw new UnsupportedOperationException("Cannot determine steel type from product name: " + dto.getName());
    }
}
