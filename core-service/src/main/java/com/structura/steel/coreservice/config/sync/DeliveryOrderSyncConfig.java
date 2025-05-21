package com.structura.steel.coreservice.config.sync;

import com.structura.steel.coreservice.elasticsearch.document.DeliveryOrderDocument;
import com.structura.steel.coreservice.elasticsearch.repository.DeliveryOrderSearchRepository;
import com.structura.steel.coreservice.mapper.DeliveryOrderMapper;
import com.structura.steel.coreservice.repository.DeliveryOrderRepository;
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
public class DeliveryOrderSyncConfig {

    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DeliveryOrderSearchRepository deliveryOrderSearchRepository;
    private final DeliveryOrderMapper deliveryOrderMapper;
    private final ElasticsearchOperations elasticsearchOperations;

    @Bean
    public ApplicationRunner partnerSyncRunner() {
        return args -> {
            IndexOperations indexOps = elasticsearchOperations.indexOps(
                    DeliveryOrderDocument.class);

            // 1. Nếu đã có index thì xóa đi để mapping được tái khởi tạo
            if (indexOps.exists()) {
                indexOps.delete();
                log.info("Deleted old ES index `deliveries`");
            }

            // 2. Tạo index mới từ @Document và @Field annotations
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping());
            log.info("Created ES index `deliveries` with updated mapping");

            // 3. Lấy toàn bộ record từ Postgres và bulk‐index
            var docs = deliveryOrderRepository.findAll().stream()
                    .map(deliveryOrder -> {
                        DeliveryOrderDocument deliveryOrderDocument = deliveryOrderMapper.toDocument(deliveryOrder);

                        // Gán giá trị cho trường "suggestion" từ "name" của Partner Entity
                        if (StringUtils.hasText(deliveryOrder.getDeliveryCode())) {
                            deliveryOrderDocument.setSuggestion(deliveryOrder.getDeliveryCode());
                        } else {
                            deliveryOrderDocument.setSuggestion(""); // Hoặc null, tùy bạn muốn xử lý tên rỗng thế nào
                        }
                        return deliveryOrderDocument;
                    })
                    .collect(Collectors.toList());

            if (!docs.isEmpty()) {
                deliveryOrderSearchRepository.saveAll(docs);
                log.info("Synchronized {} products into Elasticsearch, populating 'suggestion' field.", docs.size());
            } else {
                log.info("No deliveries found in PostgreSQL to synchronize.");
            }
        };
    }
}
