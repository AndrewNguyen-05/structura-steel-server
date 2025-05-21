package com.structura.steel.partnerservice.elasticsearch.repository;

import com.structura.steel.partnerservice.elasticsearch.document.PartnerProjectDocument;
import com.structura.steel.partnerservice.entity.PartnerProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PartnerProjectSearchRepository extends ElasticsearchRepository<PartnerProjectDocument, Long> {

    @Query("""
          {
            "multi_match": {
              "query":    "?0",
              "type":     "phrase_prefix",
              "analyzer": "folding",
              "fields": [
                "projectName",
                "projectName._2gram",
                "projectCode",
                "projectCode._2gram"
              ]
            }
          }
          """)
    Page<PartnerProjectDocument> searchByKeyword(String searchKeyword, Pageable pageable);

    // Suggestion query using the "suggestion" field (populated with partnerName)
    @Query("{\"multi_match\": {\"query\": \"?0\", \"type\": \"bool_prefix\", \"fields\": [\"suggestion\", \"suggestion._2gram\", \"suggestion._3gram\"]}}")
    Page<PartnerProjectDocument> findBySuggestionPrefix(String prefix, Pageable pageable);

    Page<PartnerProjectDocument> getAllByPartnerId(Long partnerId, Pageable pageable);
}
