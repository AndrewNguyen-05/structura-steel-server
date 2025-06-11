package com.structura.steel.partnerservice.service.scheduler;

import com.structura.steel.partnerservice.entity.PartnerProject;
import com.structura.steel.partnerservice.repository.PartnerProjectRepository;
import com.structura.steel.partnerservice.elasticsearch.repository.PartnerProjectSearchRepository;
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
public class PartnerProjectCleanupScheduler {

	private final PartnerProjectRepository partnerProjectRepository;
	private final PartnerProjectSearchRepository partnerProjectSearchRepository;

	/**
	 * Auto run 2:10 sáng mỗi ngày để clean up soft del Project (Sau product 10p, lúc 2:00)
	 */
	@Scheduled(cron = "0 10 2 * * *")
	@Transactional
	public void cleanOldSoftDeletedPartnerProjects() {
		Instant cutoffDate = ZonedDateTime.now(ZoneId.systemDefault()).minusMonths(6).toInstant();
		log.info("Cleaning soft-deleted partner projects before: {}", cutoffDate);

		List<PartnerProject> itemsToDelete = partnerProjectRepository.findAllByDeletedTrueAndUpdatedAtBefore(cutoffDate);

		if (itemsToDelete.isEmpty()) {
			log.info("No partner projects found that need cleanup. Task completed.");
			return;
		}

		log.info("Found {} partner projects that need to be permanently deleted", itemsToDelete.size());
		List<Long> idsToDelete = itemsToDelete.stream().map(PartnerProject::getId).toList();

		partnerProjectSearchRepository.deleteAllById(idsToDelete);
		partnerProjectRepository.deleteAllInBatch(itemsToDelete);

		log.info("=== PartnerProject cleanup completed successfully. Total deleted: {} ===", idsToDelete.size());
	}
}