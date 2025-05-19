package com.structura.steel.partnerservice.config.sync;

import com.structura.steel.partnerservice.elasticsearch.document.WarehouseDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.WarehouseSearchRepository;
import com.structura.steel.partnerservice.mapper.WarehouseMapper;
import com.structura.steel.partnerservice.repository.WarehouseRepository;
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
public class WarehouseSyncConfig {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseSearchRepository warehouseSearchRepository;
    private final WarehouseMapper warehouseMapper;
    private final ElasticsearchOperations elasticsearchOperations;

    @Bean
    public ApplicationRunner warehouseSyncRunner() {
        return args -> {
            IndexOperations indexOps = elasticsearchOperations.indexOps(
                    WarehouseDocument.class);

            // 1. Nếu đã có index thì xóa đi để mapping được tái khởi tạo
            if (indexOps.exists()) {
                indexOps.delete();
                log.info("Deleted old ES index `warehouses`");
            }

            // 2. Tạo index mới từ @Document và @Field annotations
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping());
            log.info("Created ES index `warehouses` with updated mapping");

            // 3. Lấy toàn bộ record từ Postgres và bulk‐index
            var docs = warehouseRepository.findAll().stream()
                    .map(warehouseEntity -> { // warehouseEntity ở đây là kiểu Warehouse (JPA Entity)
                        WarehouseDocument warehouseDocument = warehouseMapper.toDocument(warehouseEntity);

                        // Gán giá trị cho trường "suggestion" từ "name" của Warehouse Entity
                        if (StringUtils.hasText(warehouseEntity.getWarehouseName())) {
                            warehouseDocument.setSuggestion(warehouseEntity.getWarehouseName());
                        } else {
                            warehouseDocument.setSuggestion(""); // Hoặc null, tùy bạn muốn xử lý tên rỗng thế nào
                        }
                        return warehouseDocument;
                    })
                    .collect(Collectors.toList());

            if (!docs.isEmpty()) {
                warehouseSearchRepository.saveAll(docs);
                log.info("Synchronized {} products into Elasticsearch, populating 'suggestion' field.", docs.size());
            } else {
                log.info("No products found in PostgreSQL to synchronize.");
            }
        };
    }
}
