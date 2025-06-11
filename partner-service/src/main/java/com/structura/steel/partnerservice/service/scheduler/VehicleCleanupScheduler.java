package com.structura.steel.partnerservice.service.scheduler;

import com.structura.steel.partnerservice.entity.Vehicle;
import com.structura.steel.partnerservice.repository.VehicleRepository;
import com.structura.steel.partnerservice.elasticsearch.repository.VehicleSearchRepository;
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
public class VehicleCleanupScheduler {

	private final VehicleRepository vehicleRepository;
	private final VehicleSearchRepository vehicleSearchRepository;

	/**
	 * Auto run 2:20 sáng mỗi ngày để clean up soft del Vehicle (Sau project 10p, lúc 2:10)
	 */
	@Scheduled(cron = "0 20 2 * * *")
	@Transactional
	public void cleanOldSoftDeletedVehicles() {
		Instant cutoffDate = ZonedDateTime.now(ZoneId.systemDefault()).minusMonths(6).toInstant();
		log.info("Cleaning soft-deleted vehicles before: {}", cutoffDate);

		List<Vehicle> itemsToDelete = vehicleRepository.findAllByDeletedTrueAndUpdatedAtBefore(cutoffDate);

		if (itemsToDelete.isEmpty()) {
			log.info("No vehicles found that need cleanup. Task completed.");
			return;
		}

		log.info("Found {} vehicles that need to be permanently deleted", itemsToDelete.size());
		List<Long> idsToDelete = itemsToDelete.stream().map(Vehicle::getId).toList();

		vehicleSearchRepository.deleteAllById(idsToDelete);
		vehicleRepository.deleteAllInBatch(itemsToDelete);

		log.info("=== Vehicle cleanup completed successfully. Total deleted: {} ===", idsToDelete.size());
	}
}