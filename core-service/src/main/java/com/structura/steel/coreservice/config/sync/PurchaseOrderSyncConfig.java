package com.structura.steel.coreservice.config.sync;

import com.structura.steel.coreservice.elasticsearch.document.PurchaseOrderDocument;
import com.structura.steel.coreservice.elasticsearch.repository.PurchaseOrderSearchRepository;
import com.structura.steel.coreservice.mapper.PurchaseOrderMapper;
import com.structura.steel.coreservice.repository.PurchaseOrderRepository;
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
public class PurchaseOrderSyncConfig {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderSearchRepository purchaseOrderSearchRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final ElasticsearchOperations elasticsearchOperations;

    @Bean
    public ApplicationRunner vehicleSyncRunner() {
        return args -> {
            IndexOperations indexOps = elasticsearchOperations.indexOps(
                    PurchaseOrderDocument.class);

            // 1. Nếu đã có index thì xóa đi để mapping được tái khởi tạo
            if (indexOps.exists()) {
                indexOps.delete();
                log.info("Deleted old ES index `purchase`");
            }

            // 2. Tạo index mới từ @Document và @Field annotations
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping());
            log.info("Created ES index `purchase` with updated mapping");

            // 3. Lấy toàn bộ record từ Postgres và bulk‐index
            var docs = purchaseOrderRepository.findAll().stream()
                    .map(purchaseOrder -> {
                        PurchaseOrderDocument purchaseDocument = purchaseOrderMapper.toDocument(purchaseOrder);

                        // Gán giá trị cho trường "suggestion" từ "name" của Vehicle Entity
                        if (StringUtils.hasText(purchaseOrder.getImportCode())) {
                            purchaseDocument.setSuggestion(purchaseOrder.getImportCode());
                        } else {
                            purchaseDocument.setSuggestion(""); // Hoặc null, tùy bạn muốn xử lý tên rỗng thế nào
                        }
                        return purchaseDocument;
                    })
                    .collect(Collectors.toList());

            if (!docs.isEmpty()) {
                purchaseOrderSearchRepository.saveAll(docs);
                log.info("Synchronized {} products into Elasticsearch, populating 'suggestion' field.", docs.size());
            } else {
                log.info("No purchase found in PostgreSQL to synchronize.");
            }
        };
    }
}
