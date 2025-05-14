package com.structura.steel.commons.utils;

import com.structura.steel.commons.enumeration.EntityType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class CodeGenerator {
	private static final Map<String, String> ENTITY_PREFIX_MAP = Map.of(
			"Product", "PRD",
			"Partner", "PTN",
			"Warehouse", "WH",
			"Vehicle", "VEH",
			"Project", "PRJ",
			"Export", "EXP",
			"Import", "IMP",
			"Delivery", "DLV"
	);

	private static String getPrefix(String entityName) {
		if (entityName == null) return null;
		return ENTITY_PREFIX_MAP.get(entityName);
	}

	public static String generateCode(EntityType entityType) {
		String entityName = entityType.name();
		String prefix = getPrefix(entityName);
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String nanoPart = Long.toString(System.nanoTime(), 36).toUpperCase();
		String randomPart = getRandomAlphaNumeric(2);
		return prefix.toUpperCase() + date + nanoPart + randomPart;
	}

	private static String getRandomAlphaNumeric(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder(length);
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}
}
