package com.structura.steel.partnerservice.config.sync;

import com.structura.steel.partnerservice.elasticsearch.document.PartnerDocument;
import com.structura.steel.partnerservice.elasticsearch.document.PartnerProjectDocument;
import com.structura.steel.partnerservice.elasticsearch.repository.PartnerProjectSearchRepository;
import com.structura.steel.partnerservice.mapper.PartnerProjectMapper;
import com.structura.steel.partnerservice.repository.PartnerProjectRepository;
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
public class PartnerProjectSyncConfig {

    private final PartnerProjectRepository partnerProjectRepository;
    private final PartnerProjectSearchRepository partnerProjectSearchRepository;
    private final PartnerProjectMapper partnerProjectMapper;
    private final ElasticsearchOperations elasticsearchOperations;

    @Bean
    public ApplicationRunner projectSyncRunner() {
        return args -> {
            IndexOperations indexOps = elasticsearchOperations.indexOps(
                    PartnerProjectDocument.class);

            // 1. Nếu đã có index thì xóa đi để mapping được tái khởi tạo
            if (indexOps.exists()) {
                indexOps.delete();
                log.info("Deleted old ES index `projects`");
            }

            // 2. Tạo index mới từ @Document và @Field annotations
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping());
            log.info("Created ES index `projects` with updated mapping");

            // 3. Lấy toàn bộ record từ Postgres và bulk‐index
            var docs = partnerProjectRepository.findAll().stream()
                    .map(projectEntity -> { // projectEntity ở đây là kiểu Project (JPA Entity)
                        PartnerProjectDocument projectDocument = partnerProjectMapper.toDocument(projectEntity);

                        // Gán giá trị cho trường "suggestion" từ "name" của Project Entity
                        if (StringUtils.hasText(projectEntity.getProjectName())) {
                            projectDocument.setSuggestion(projectEntity.getProjectName());
                        } else {
                            projectDocument.setSuggestion(""); // Hoặc null, tùy bạn muốn xử lý tên rỗng thế nào
                        }
                        return projectDocument;
                    })
                    .collect(Collectors.toList());

            if (!docs.isEmpty()) {
                partnerProjectSearchRepository.saveAll(docs);
                log.info("Synchronized {} products into Elasticsearch, populating 'suggestion' field.", docs.size());
            } else {
                log.info("No products found in PostgreSQL to synchronize.");
            }
        };
    }
}
