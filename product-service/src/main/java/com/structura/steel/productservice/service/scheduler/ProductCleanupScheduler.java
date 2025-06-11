package com.structura.steel.productservice.service.scheduler;

import com.structura.steel.productservice.entity.Product;
import com.structura.steel.productservice.repository.ProductRepository;
import com.structura.steel.productservice.elasticsearch.repository.ProductSearchRepository;
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
public class ProductCleanupScheduler {

	private final ProductRepository productRepository;
	private final ProductSearchRepository productSearchRepository;

	/**
	 * Auto run vào 2:00 sáng mỗi ngày để dọn dẹp dữ liệu.
	 * Cron expression: second, minute, hour, day of month, month, day of week
	 * "0 0 2 * * *" = 0 giây, 0 phút, 2 giờ, mỗi ngày, mỗi tháng, mỗi thứ trong tuần.
	 * Để test thì có thể set cron la 0 * * * * * (mỗi phút)
	 */
	@Scheduled(cron = "0 0 2 * * *")
	@Transactional
	public void cleanOldSoftDeletedProducts() {
		// 1. Tính toán thời gian mốc là 6 tháng trước
		Instant cutoffDate = ZonedDateTime.now(ZoneId.systemDefault())
				.minusMonths(6)
				.toInstant();
		log.info("Cleaning soft-deleted products before: {}", cutoffDate);

		// 2. Tìm tất cả các sản phẩm thỏa mãn điều kiện
		List<Product> productsToDelete = productRepository.findAllByDeletedTrueAndUpdatedAtBefore(cutoffDate);

		if (productsToDelete.isEmpty()) {
			log.info("No products found that need cleanup. Task completed.");
			return;
		}

		log.info("Found {} products that need to be permanently deleted", productsToDelete.size());

		// 3. Lấy ra danh sách các ID
		List<Long> idsToDelete = productsToDelete.stream()
				.map(Product::getId)
				.toList();

		// 4. Xóa vĩnh viễn
		// Xóa trong Elasticsearch
		productSearchRepository.deleteAllById(idsToDelete);

		// Xóa trong database chính (PostgreSQL)
		productRepository.deleteAllInBatch(productsToDelete);

		log.info("=== Product cleanup completed successfully. Total deleted: {} ===", idsToDelete.size());
	}
}