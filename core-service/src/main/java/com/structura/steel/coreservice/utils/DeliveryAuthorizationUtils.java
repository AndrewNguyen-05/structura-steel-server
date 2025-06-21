package com.structura.steel.coreservice.utils;

import com.structura.steel.commons.dto.core.request.delivery.DeliveryOrderRequestDto;
import com.structura.steel.commons.dto.core.response.delivery.DeliveryOrderResponseDto;
import com.structura.steel.commons.exception.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class DeliveryAuthorizationUtils {

    public void validateCreatePermission(DeliveryOrderRequestDto dto, String authorities) {
        if (authorities.contains("ROLE_ADMIN")) return;

        if (dto.purchaseOrderId() != null && !authorities.contains("ROLE_IMPORTER")) {
            throw new ForbiddenException("Only ADMIN or IMPORTER can create purchase delivery orders");
        }

        if (dto.saleOrderId() != null && !authorities.contains("ROLE_SALER")) {
            throw new ForbiddenException("Only ADMIN or SALER can create sale delivery orders");
        }
    }

    public void validateUpdatePermission(DeliveryOrderResponseDto order, String authorities) {
        if (authorities.contains("ROLE_ADMIN")) return;

        if (order.getPurchaseOrderId() != null && !authorities.contains("ROLE_IMPORTER")) {
            throw new ForbiddenException("Only ADMIN or IMPORTER can modify purchase delivery orders");
        }

        if (order.getSaleOrderId() != null && !authorities.contains("ROLE_SALER")) {
            throw new ForbiddenException("Only ADMIN or SALER can modify sale delivery orders");
        }
    }
}
