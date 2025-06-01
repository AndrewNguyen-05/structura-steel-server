package com.structura.steel.coreservice.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PurchaseOrderCreatedEvent extends ApplicationEvent {

    private final Long projectId;

    public PurchaseOrderCreatedEvent(Object source, Long projectId) {
        super(source);
        this.projectId = projectId;
    }
}
