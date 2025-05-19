package com.structura.steel.partnerservice.config.sync;

import com.structura.steel.partnerservice.elasticsearch.document.VehicleDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.VehicleSearchRepository;
import com.structura.steel.partnerservice.mapper.VehicleMapper;
import com.structura.steel.partnerservice.repository.VehicleRepository;
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
public class VehicleSyncConfig {

    private final VehicleRepository vehicleRepository;
    private final VehicleSearchRepository vehicleSearchRepository;
    private final VehicleMapper vehicleMapper;
    private final ElasticsearchOperations elasticsearchOperations;

    @Bean
    public ApplicationRunner vehicleSyncRunner() {
        return args -> {
            IndexOperations indexOps = elasticsearchOperations.indexOps(
                    VehicleDocument.class);

            // 1. Nếu đã có index thì xóa đi để mapping được tái khởi tạo
            if (indexOps.exists()) {
                indexOps.delete();
                log.info("Deleted old ES index `vehicles`");
            }

            // 2. Tạo index mới từ @Document và @Field annotations
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping());
            log.info("Created ES index `vehicles` with updated mapping");

            // 3. Lấy toàn bộ record từ Postgres và bulk‐index
            var docs = vehicleRepository.findAll().stream()
                    .map(vehicleEntity -> { // vehicleEntity ở đây là kiểu Vehicle (JPA Entity)
                        VehicleDocument vehicleDocument = vehicleMapper.toDocument(vehicleEntity);

                        // Gán giá trị cho trường "suggestion" từ "name" của Vehicle Entity
                        if (StringUtils.hasText(vehicleEntity.getDriverName())) {
                            vehicleDocument.setSuggestion(vehicleEntity.getDriverName());
                        } else {
                            vehicleDocument.setSuggestion(""); // Hoặc null, tùy bạn muốn xử lý tên rỗng thế nào
                        }
                        return vehicleDocument;
                    })
                    .collect(Collectors.toList());

            if (!docs.isEmpty()) {
                vehicleSearchRepository.saveAll(docs);
                log.info("Synchronized {} products into Elasticsearch, populating 'suggestion' field.", docs.size());
            } else {
                log.info("No products found in PostgreSQL to synchronize.");
            }
        };
    }
}
