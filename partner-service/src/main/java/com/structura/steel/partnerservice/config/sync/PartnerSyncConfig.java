package com.structura.steel.partnerservice.config.sync;

import com.structura.steel.partnerservice.elasticsearch.document.PartnerDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.PartnerSearchRepository;
import com.structura.steel.partnerservice.mapper.PartnerMapper;
import com.structura.steel.partnerservice.repository.PartnerRepository;
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
public class PartnerSyncConfig {

    private final PartnerRepository partnerRepository;
    private final PartnerSearchRepository partnerSearchRepository;
    private final PartnerMapper partnerMapper;
    private final ElasticsearchOperations elasticsearchOperations;

    @Bean
    public ApplicationRunner partnerSyncRunner() {
        return args -> {
            IndexOperations indexOps = elasticsearchOperations.indexOps(
                    PartnerDocument.class);

            // 1. Nếu đã có index thì xóa đi để mapping được tái khởi tạo
            if (indexOps.exists()) {
                indexOps.delete();
                log.info("Deleted old ES index `partners`");
            }

            // 2. Tạo index mới từ @Document và @Field annotations
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping());
            log.info("Created ES index `partners` with updated mapping");

            // 3. Lấy toàn bộ record từ Postgres và bulk‐index
            var docs = partnerRepository.findAll().stream()
                    .map(partnerEntity -> { // partnerEntity ở đây là kiểu Partner (JPA Entity)
                        PartnerDocument partnerDocument = partnerMapper.toDocument(partnerEntity);

                        // Gán giá trị cho trường "suggestion" từ "name" của Partner Entity
                        if (StringUtils.hasText(partnerEntity.getPartnerName())) {
                            partnerDocument.setSuggestion(partnerEntity.getPartnerName());
                        } else {
                            partnerDocument.setSuggestion(""); // Hoặc null, tùy bạn muốn xử lý tên rỗng thế nào
                        }
                        return partnerDocument;
                    })
                    .collect(Collectors.toList());

            if (!docs.isEmpty()) {
                partnerSearchRepository.saveAll(docs);
                log.info("Synchronized {} products into Elasticsearch, populating 'suggestion' field.", docs.size());
            } else {
                log.info("No products found in PostgreSQL to synchronize.");
            }
        };
    }
}
