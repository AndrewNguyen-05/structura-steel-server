package com.structura.steel.coreservice.elasticsearch.repository;

import com.structura.steel.coreservice.elasticsearch.document.DeliveryOrderDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DeliveryOrderSearchRepository extends ElasticsearchRepository<DeliveryOrderDocument, Long> {

    @Query("""
          {
            "multi_match": {
              "query":    "?0",
              "productType":     "phrase_prefix",
              "analyzer": "folding",
              "fields": [
                "deliveryCode",
                "deliveryCode._2gram",
                "driverName",
                "driverName._2gram"
              ]
            }
          }
          """)
    Page<DeliveryOrderDocument> searchByKeyword(String searchKeyword, Pageable pageable);

    // Suggestion query using the "suggestion" field (populated with partnerName)
    @Query("{\"multi_match\": {\"query\": \"?0\", \"productType\": \"bool_prefix\", \"fields\": [\"suggestion\", \"suggestion._2gram\", \"suggestion._3gram\"]}}")
    Page<DeliveryOrderDocument> findBySuggestionPrefix(String prefix, Pageable pageable);
}
