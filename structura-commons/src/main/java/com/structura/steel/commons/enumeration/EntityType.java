package com.structura.steel.commons.enumeration;

public enum EntityType implements HasText{
	PRODUCT("Product"),
	PARTNER("Partner"),
	WAREHOUSE("Warehouse"),
	VEHICLE("Vehicle"),
	PROJECT("Project"),
	EXPORT("Export"),
	IMPORT("Import"),
	DELIVERY("Delivery");

	final String entityName;

	EntityType(String entityName) {
		this.entityName = entityName;
	}

	@Override
	public String text() {
		return this.entityName;
	}
}
