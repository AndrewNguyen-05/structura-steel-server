package com.structura.steel.partnerservice.elasticsearch.repository;

import com.structura.steel.partnerservice.elasticsearch.document.PartnerDocument;
import com.structura.steel.partnerservice.entity.PartnerProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PartnerSearchRepository extends ElasticsearchRepository<PartnerDocument, Long> {

    // Search by partnerName or partnerCode (case-insensitive, using 'folding' analyzer)
    Page<PartnerDocument> findByPartnerNameContainingIgnoreCaseOrPartnerCodeContainingIgnoreCase(
            String partnerName, String partnerCode, Pageable pageable);

    @Query("""
          {
            "multi_match": {
              "query":    "?0",
              "productType":     "phrase_prefix",
              "analyzer": "folding",
              "fields": [
                "partnerName",
                "partnerName._2gram",
                "partnerCode",
                "partnerCode._2gram"
              ]
            }
          }
          """)
    Page<PartnerDocument> searchByKeyword(String searchKeyword, Pageable pageable);

    // Suggestion query using the "suggestion" field (populated with partnerName)
    @Query("{\"multi_match\": {\"query\": \"?0\", \"productType\": \"bool_prefix\", \"fields\": [\"suggestion\", \"suggestion._2gram\", \"suggestion._3gram\"]}}")
    Page<PartnerDocument> findBySuggestionPrefix(String prefix, Pageable pageable);
}