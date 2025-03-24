package com.structura.steel.productservice.helper;

import com.structura.steel.productservice.dto.request.ProductRequestDto;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SteelCalculator {

    private static final BigDecimal thousand = BigDecimal.valueOf(1000);
    private static final BigDecimal density = BigDecimal.valueOf(7850.0);
    private static final BigDecimal pi = BigDecimal.valueOf(Math.PI);

    /**
     * Tinh khoi luong thep (kg) dua vao ProductRequestDto.
     * @param dto thong tin ve thep can tinh (name, diameter, thickness, etc.)
     * @return weight (kg)
     */
    public static BigDecimal calculateSteelWeight(ProductRequestDto dto) {
        if (dto == null || dto.name() == null) {
            throw new IllegalArgumentException("Invalid product data. Name cannot be null.");
        } else if(dto.length() == null) {
            throw new IllegalArgumentException("Invalid product data. Length cannot be null.");
        }

        String lowerName = dto.name().toLowerCase();

        // Xác định các loại thép theo tên
        boolean isHinh = lowerName.contains("hinh");    // thép hình (I, H, U, ...)
        boolean isHop = lowerName.contains("hop");        // thép hộp (box)
        boolean isVan = lowerName.contains("van");        // thép vằn (rebar)
        boolean isCay = lowerName.contains("cay");        // thép cây (rebar)
        boolean isOng = lowerName.contains("ong");        // có thể là ống tròn hoặc ống hộp
        boolean isCuon = lowerName.contains("cuon");      // thép cuộn
        boolean isTam = lowerName.contains("tam");        // thép tấm

        if(isHop) {
            if(dto.width() == null || dto.height() == null) {
                throw new IllegalArgumentException("Invalid product data. Width and height cannot be null.");
            }
            if(dto.thickness() == null) {
                throw new IllegalArgumentException("thickness is required for structural steel products.");
            }
        } else if(isHinh) {
            if (dto.unitWeight() == null) {
                throw new IllegalArgumentException("unitWeight (kg/m) is required for structural steel products.");
            }
        } else if(isVan || isCay || isOng) {
            if(dto.diameter() == null) {
                throw new IllegalArgumentException("diameter is required for structural steel products.");
            }
            if(dto.thickness() == null) {
                throw new IllegalArgumentException("thickness is required for structural steel products.");
            }
        } else if(isCuon || isTam) {
            if(dto.thickness() == null) {
                throw new IllegalArgumentException("thickness is required for structural steel products.");
            }
        }

        BigDecimal length = dto.length();    // m
        BigDecimal unitWeight = dto.unitWeight();   // kg


        // 1) Thep hinh (I, H, U, L, vv...)
        if (lowerName.contains("hinh")) {
            return dto.unitWeight().multiply(length);
        }

        // Neu co unit weight thi tra ve khoi luong luon
        if(unitWeight != null) {
            return unitWeight.multiply(length);
        }

        // 2) Thep thanh van / sat thep cay
        if (lowerName.contains("van") || lowerName.contains("cay")) {
            BigDecimal diameterMeter = dto.diameter().divide(thousand, 10, RoundingMode.HALF_UP);
            BigDecimal volume = getCircleVolume(diameterMeter, length);
            return volume.multiply(density);
        }

        // 3) Thep cuon / thep tam
        else if (lowerName.contains("cuon") || lowerName.contains("tam")) {
            BigDecimal thicknessMeter = dto.thickness().divide(thousand, 10, RoundingMode.HALF_UP);
            BigDecimal width = dto.width().divide(thousand, 10, RoundingMode.HALF_UP);
            BigDecimal volume = getRectangleVolume(thicknessMeter, width, length);
            return volume.multiply(density);
        }

        // 4) Thep ong / thep hop
        else if (lowerName.contains("ong") || lowerName.contains("hop")) {

            BigDecimal thickness = dto.thickness().divide(thousand, 10, RoundingMode.HALF_UP);

            if (lowerName.contains("ong")) {
                BigDecimal diameter = dto.diameter().divide(thousand, 10, RoundingMode.HALF_UP);
                BigDecimal totalVolume = getCircleVolume(diameter, length);

                // Tinh canh trong: innerSide = (w - 2*t)
                BigDecimal innerDiameter = diameter.subtract(BigDecimal.valueOf(2).multiply(thickness));
                BigDecimal innerVolume = getCircleVolume(innerDiameter, length);

                return totalVolume.subtract(innerVolume).multiply(density);

            } else {
                BigDecimal width = dto.width().divide(thousand, 10, RoundingMode.HALF_UP);
                BigDecimal height = dto.height().divide(thousand, 10, RoundingMode.HALF_UP);
                BigDecimal totalVolume = getRectangleVolume(width, height, length);

                // Tinh canh trong: innerSide = (w - 2*t)
                BigDecimal innerSide = width.subtract(BigDecimal.valueOf(2).multiply(thickness));
                BigDecimal innerVolume = getRectangleVolume(innerSide, innerSide, length);

                return totalVolume.subtract(innerVolume).multiply(density);
            }
        }

        throw new UnsupportedOperationException("Cannot determine steel type from product name: " + dto.name());
    }

    private static BigDecimal getRectangleVolume(BigDecimal thickness, BigDecimal width, BigDecimal length) {
        if (thickness == null || width == null) {
            throw new IllegalArgumentException("Thickness and width are required for coil/plate products.");
        }

        return thickness.multiply(width).multiply(length);
    }

    private static BigDecimal getCircleVolume(BigDecimal diameter, BigDecimal length) {
        if (diameter == null) {
            throw new IllegalArgumentException("Diameter is required for rebar products.");
        }

        // Tinh dien tich: (PI * diameter^2) / 4
        BigDecimal diameterSquared = diameter.multiply(diameter);
        BigDecimal crossSectionArea = pi.multiply(diameterSquared)
                .divide(BigDecimal.valueOf(4), 10, RoundingMode.HALF_UP);

        return crossSectionArea.multiply(length);
        //test cherry pick 123
    }
}
