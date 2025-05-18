package com.structura.steel.partnerservice.elasticsearch.repository;

import com.structura.steel.partnerservice.elasticsearch.document.WarehouseDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WarehouseSearchRepository extends ElasticsearchRepository<WarehouseDocument, Long> {

    @Query("""
          {
            "multi_match": {
              "query":    "?0",
              "type":     "phrase_prefix",
              "analyzer": "folding",
              "fields": [
                "warehouseName",
                "warehouseName._2gram",
                "warehouseCode",
                "warehouseCode._2gram"
              ]
            }
          }
          """)
    Page<WarehouseDocument> searchByKeyword(String searchKeyword, Pageable pageable);

    // Suggestion query using the "suggestion" field (populated with partnerName)
    @Query("{\"multi_match\": {\"query\": \"?0\", \"type\": \"bool_prefix\", \"fields\": [\"suggestion\", \"suggestion._2gram\", \"suggestion._3gram\"]}}")
    Page<WarehouseDocument> findBySuggestionPrefix(String prefix, Pageable pageable);
}
