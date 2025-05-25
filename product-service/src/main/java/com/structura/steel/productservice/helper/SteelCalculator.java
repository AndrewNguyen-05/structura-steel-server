package com.structura.steel.productservice.helper;

import com.structura.steel.commons.dto.product.request.ProductRequestDto;
import com.structura.steel.commons.enumeration.ProductType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SteelCalculator {

    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);
    private static final BigDecimal DENSITY = BigDecimal.valueOf(7850.0); // kg/m^3
    private static final BigDecimal PI = BigDecimal.valueOf(Math.PI);

    /**
     * Tính khối lượng thép (kg) dựa vào ProductRequestDto.
     * Sử dụng ProductType để xác định công thức tính.
     * @param dto Thông tin về thép cần tính (productType, dimensions, etc.)
     * @return weight (kg)
     */
    public static BigDecimal calculateSteelWeight(ProductRequestDto dto) {
        // --- 1. Kiểm tra đầu vào cơ bản ---
        if (dto == null || dto.productType() == null) {
            throw new IllegalArgumentException("Invalid product data. ProductType cannot be null.");
        }
        if (dto.length() == null || dto.length().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid product data. Length must be provided and greater than zero.");
        }

        // --- 2. Kiểm tra các thuộc tính bắt buộc theo ProductType ---
        validateRequiredFields(dto);

        BigDecimal length = dto.length(); // m
        ProductType type = dto.productType();

        // --- 3. Tính toán dựa trên ProductType ---
        switch (type) {
            case SHAPED:
                // Thép hình: dùng unitWeight
                return dto.unitWeight().multiply(length);

            case RIBBED_BAR:
            case WIRE_COIL: {
                // Thép vằn/cây/dây cuộn: dùng diameter
                BigDecimal diameterMeter = dto.diameter().divide(THOUSAND, 10, RoundingMode.HALF_UP);
                BigDecimal volume = getCircleVolume(diameterMeter, length);
                return volume.multiply(DENSITY);
            }

            case COIL: // Đây là cuộn tấm
            case PLATE: {
                // Thép cuộn tấm/tấm: dùng width & thickness
                BigDecimal thicknessMeter = dto.thickness().divide(THOUSAND, 10, RoundingMode.HALF_UP);
                BigDecimal widthMeter = dto.width().divide(THOUSAND, 10, RoundingMode.HALF_UP);
                BigDecimal volume = getRectangularPrismVolume(widthMeter, thicknessMeter, length);
                return volume.multiply(DENSITY);
            }

            case PIPE: {
                // Thép ống tròn: dùng diameter & thickness
                BigDecimal thicknessMeter = dto.thickness().divide(THOUSAND, 10, RoundingMode.HALF_UP);
                BigDecimal diameterMeter = dto.diameter().divide(THOUSAND, 10, RoundingMode.HALF_UP);

                BigDecimal totalVolume = getCircleVolume(diameterMeter, length);
                BigDecimal innerDiameter = diameterMeter.subtract(BigDecimal.valueOf(2).multiply(thicknessMeter));

                if (innerDiameter.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Thickness cannot be greater than or equal to half the diameter for PIPE.");
                }
                BigDecimal innerVolume = getCircleVolume(innerDiameter, length);
                return totalVolume.subtract(innerVolume).multiply(DENSITY);
            }

            case BOX: {
                // Thép hộp: dùng width, height & thickness
                BigDecimal thicknessMeter = dto.thickness().divide(THOUSAND, 10, RoundingMode.HALF_UP);
                BigDecimal widthMeter = dto.width().divide(THOUSAND, 10, RoundingMode.HALF_UP);
                BigDecimal heightMeter = dto.height().divide(THOUSAND, 10, RoundingMode.HALF_UP);

                BigDecimal totalVolume = getRectangularPrismVolume(widthMeter, heightMeter, length);
                BigDecimal innerWidth = widthMeter.subtract(BigDecimal.valueOf(2).multiply(thicknessMeter));
                BigDecimal innerHeight = heightMeter.subtract(BigDecimal.valueOf(2).multiply(thicknessMeter));

                if (innerWidth.compareTo(BigDecimal.ZERO) <= 0 || innerHeight.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Thickness cannot be greater than or equal to half the width/height for BOX.");
                }
                BigDecimal innerVolume = getRectangularPrismVolume(innerWidth, innerHeight, length);
                return totalVolume.subtract(innerVolume).multiply(DENSITY);
            }

            default:
                throw new UnsupportedOperationException("Weight calculation not implemented for product type: " + type);
        }
    }

    /**
     * Kiểm tra các trường bắt buộc dựa trên ProductType.
     */
    private static void validateRequiredFields(ProductRequestDto dto) {
        ProductType type = dto.productType();
        switch (type) {
            case RIBBED_BAR:
            case WIRE_COIL:
                require(dto.diameter(), "Diameter must not be null for RIBBED_BAR/WIRE_COIL.");
                break;
            case COIL: // Cuộn tấm
            case PLATE:
                require(dto.thickness(), "Thickness must not be null for COIL/PLATE.");
                require(dto.width(), "Width must not be null for COIL/PLATE.");
                break;
            case PIPE:
                require(dto.thickness(), "Thickness must not be null for PIPE.");
                require(dto.diameter(), "Diameter must not be null for PIPE.");
                break;
            case BOX:
                require(dto.thickness(), "Thickness must not be null for BOX.");
                require(dto.width(), "Width must not be null for BOX.");
                require(dto.height(), "Height must not be null for BOX.");
                break;
            case SHAPED:
                require(dto.unitWeight(), "Unit weight must not be null for SHAPED steel.");
                break;
            default:
                break;
        }
    }

    /**
     * Hàm hỗ trợ kiểm tra giá trị không được null.
     */
    private static void require(Object fieldValue, String message) {
        if (fieldValue == null) {
            throw new IllegalArgumentException(message);
        }
        if (fieldValue instanceof BigDecimal && ((BigDecimal) fieldValue).compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message.replace("must not be null", "must be positive"));
        }
    }

    /**
     * Tính thể tích hình hộp chữ nhật (hoặc tấm).
     */
    private static BigDecimal getRectangularPrismVolume(BigDecimal width, BigDecimal height, BigDecimal length) {
        return width.multiply(height).multiply(length);
    }

    /**
     * Tính thể tích hình trụ tròn (thép cây/ống/dây).
     */
    private static BigDecimal getCircleVolume(BigDecimal diameter, BigDecimal length) {
        BigDecimal diameterSquared = diameter.multiply(diameter);
        BigDecimal crossSectionArea = PI.multiply(diameterSquared)
                .divide(BigDecimal.valueOf(4), 10, RoundingMode.HALF_UP);
        return crossSectionArea.multiply(length);
    }
}