package com.structura.steel.productservice.config.sync;

import com.structura.steel.productservice.elasticsearch.document.ProductDocument;
import com.structura.steel.productservice.elasticsearch.repository.ProductSearchRepository;
import com.structura.steel.productservice.mapper.ProductMapper;
import com.structura.steel.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ProductConfigSync {
	private final ProductRepository productRepository;
	private final ProductSearchRepository productSearchRepository;
	private final ProductMapper productMapper;
	private final ElasticsearchOperations elasticsearchOperations;

	@Bean
	public ApplicationRunner productSyncRunner() {
		return args -> {
			IndexOperations indexOps = elasticsearchOperations.indexOps(
					ProductDocument.class);

			// 1. Nếu đã có index thì xóa đi để mapping được tái khởi tạo
			if (indexOps.exists()) {
				indexOps.delete();
				log.info("Deleted old ES index `products`");
			}

			// 2. Tạo index mới từ @Document và @Field annotations trong ProductDocument
			indexOps.create();
			indexOps.putMapping(indexOps.createMapping());
			log.info("Created ES index `products` with updated mapping");

			// 3. Lấy toàn bộ record từ Postgres và bulk‐index
			var docs = productRepository.findAll().stream()
					.map(productEntity -> { // productEntity ở đây là kiểu Product (JPA Entity)
						ProductDocument productDocument = productMapper.toDocument(productEntity);

						// Gán giá trị cho trường "suggestion" từ "name" của Product Entity
						if (StringUtils.hasText(productEntity.getName())) {
							productDocument.setSuggestion(productEntity.getName());
						} else {
							productDocument.setSuggestion(""); // Hoặc null, tùy bạn muốn xử lý tên rỗng thế nào
						}
						return productDocument;
					})
					.collect(Collectors.toList()); // Hoặc .toList() nếu bạn dùng Java 16+

			if (!docs.isEmpty()) {
				productSearchRepository.saveAll(docs);
				log.info("Synchronized {} products into Elasticsearch, populating 'suggestion' field.", docs.size());
			} else {
				log.info("No products found in PostgreSQL to synchronize.");
			}
		};
	}
}
