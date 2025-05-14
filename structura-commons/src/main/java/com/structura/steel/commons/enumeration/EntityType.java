package com.structura.steel.commons.enumeration;

public enum EntityType {
	PRODUCT("Product"),
	PARTNER("Partner"),
	WAREHOUSE("Warehouse"),
	VEHICLE("Vehicle"),
	PROJECT("Project"),
	EXPORT("Export"),
	IMPORT("Import"),
	DELIVERY("Delivery");

	final String name;

	EntityType(String name) {
		this.name = name;
	}
}
