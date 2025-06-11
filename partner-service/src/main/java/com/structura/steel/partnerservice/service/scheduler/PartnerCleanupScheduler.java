package com.structura.steel.partnerservice.service.scheduler;

import com.structura.steel.partnerservice.entity.Partner;
import com.structura.steel.partnerservice.repository.PartnerRepository;
import com.structura.steel.partnerservice.elasticsearch.repository.PartnerSearchRepository;
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
public class PartnerCleanupScheduler {

	private final PartnerRepository partnerRepository;
	private final PartnerSearchRepository partnerSearchRepository;

	/**
	 * Auto run 2:40 sáng mỗi ngày để clean up soft del Partner (Sau warehouse 10p, lúc 2:30)
	 */
	@Scheduled(cron = "0 40 2 * * *")
	@Transactional
	public void cleanOldSoftDeletedPartners() {
		Instant cutoffDate = ZonedDateTime.now(ZoneId.systemDefault()).minusMonths(6).toInstant();
		log.info("Cleaning soft-deleted partners before: {}", cutoffDate);

		List<Partner> itemsToDelete = partnerRepository.findAllByDeletedTrueAndUpdatedAtBefore(cutoffDate);

		if (itemsToDelete.isEmpty()) {
			log.info("No partners found that need cleanup. Task completed.");
			return;
		}

		log.info("Found {} partners that need to be permanently deleted", itemsToDelete.size());
		List<Long> idsToDelete = itemsToDelete.stream().map(Partner::getId).toList();

		partnerSearchRepository.deleteAllById(idsToDelete);
		partnerRepository.deleteAllInBatch(itemsToDelete);

		log.info("=== Partner cleanup completed successfully. Total deleted: {} ===", idsToDelete.size());
	}
}