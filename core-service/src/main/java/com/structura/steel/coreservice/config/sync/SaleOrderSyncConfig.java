package com.structura.steel.coreservice.config.sync;

import com.structura.steel.coreservice.elasticsearch.document.SaleOrderDocument;
import com.structura.steel.coreservice.elasticsearch.repository.SaleOrderSearchRepository;
import com.structura.steel.coreservice.mapper.SaleOrderMapper;
import com.structura.steel.coreservice.repository.SaleOrderRepository;
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
public class SaleOrderSyncConfig {

    private final SaleOrderRepository saleOrderRepository;
    private final SaleOrderSearchRepository saleOrderSearchRepository;
    private final SaleOrderMapper saleOrderMapper;
    private final ElasticsearchOperations elasticsearchOperations;

    @Bean
    public ApplicationRunner projectSyncRunner() {
        return args -> {
            IndexOperations indexOps = elasticsearchOperations.indexOps(
                    SaleOrderDocument.class);

            // 1. Nếu đã có index thì xóa đi để mapping được tái khởi tạo
            if (indexOps.exists()) {
                indexOps.delete();
                log.info("Deleted old ES index `sale`");
            }

            // 2. Tạo index mới từ @Document và @Field annotations
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping());
            log.info("Created ES index `sale` with updated mapping");

            // 3. Lấy toàn bộ record từ Postgres và bulk‐index
            var docs = saleOrderRepository.findAll().stream()
                    .map(saleOrder -> {
                        SaleOrderDocument saleDocument = saleOrderMapper.toDocument(saleOrder);

                        // Gán giá trị cho trường "suggestion" từ "name" của Project Entity
                        if (StringUtils.hasText(saleOrder.getExportCode())) {
                            saleDocument.setSuggestion(saleOrder.getExportCode());
                        } else {
                            saleDocument.setSuggestion(""); // Hoặc null, tùy bạn muốn xử lý tên rỗng thế nào
                        }
                        return saleDocument;
                    })
                    .collect(Collectors.toList());

            if (!docs.isEmpty()) {
                saleOrderSearchRepository.saveAll(docs);
                log.info("Synchronized {} products into Elasticsearch, populating 'suggestion' field.", docs.size());
            } else {
                log.info("No sales found in PostgreSQL to synchronize.");
            }
        };
    }
}
