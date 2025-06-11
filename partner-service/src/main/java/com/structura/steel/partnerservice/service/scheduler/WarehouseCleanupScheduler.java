package com.structura.steel.partnerservice.service.scheduler;

import com.structura.steel.partnerservice.entity.Warehouse;
import com.structura.steel.partnerservice.repository.WarehouseRepository;
import com.structura.steel.partnerservice.elasticsearch.repository.WarehouseSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseCleanupScheduler {

	private final WarehouseRepository warehouseRepository;
	private final WarehouseSearchRepository warehouseSearchRepository;

	/**
	 * Auto run 2:30 sáng mỗi ngày để clean up soft del Vehicle (Sau vehicle 10p, lúc 2:20)
	 */
	@Scheduled(cron = "0 30 2 * * *")
	@Transactional
	public void cleanOldSoftDeletedWarehouses() {
		Instant cutoffDate = ZonedDateTime.now(ZoneId.systemDefault()).minusMonths(6).toInstant();
		log.info("Cleaning soft-deleted warehouses before: {}", cutoffDate);

		List<Warehouse> itemsToDelete = warehouseRepository.findAllByDeletedTrueAndUpdatedAtBefore(cutoffDate);

		if (itemsToDelete.isEmpty()) {
			log.info("No warehouses found that need cleanup. Task completed.");
			return;
		}

		log.info("Found {} warehouses that need to be permanently deleted", itemsToDelete.size());
		List<Long> idsToDelete = itemsToDelete.stream().map(Warehouse::getId).toList();

		warehouseSearchRepository.deleteAllById(idsToDelete);
		warehouseRepository.deleteAllInBatch(itemsToDelete);

		log.info("=== Warehouse cleanup completed successfully. Total deleted: {} ===", idsToDelete.size());
	}
}