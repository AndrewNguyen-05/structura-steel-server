package com.structura.steel.coreservice.elasticsearch.repository;

import com.structura.steel.coreservice.elasticsearch.document.PurchaseOrderDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PurchaseOrderSearchRepository extends ElasticsearchRepository<PurchaseOrderDocument, Long> {

    @Query("""
          {
            "multi_match": {
              "query": "?0",
              "type": "bool_prefix",
              "analyzer": "folding",
              "fields": [
                "importCode",
                "importCode._2gram",
              ]
            }
          }
          """)
    Page<PurchaseOrderDocument> searchByKeyword(String searchKeyword, Pageable pageable);

    // Suggestion query using the "suggestion" field (populated with partnerName)
    @Query("""
          {
            "multi_match": {
              "query": "?0",
              "type": "bool_prefix",
              "analyzer": "folding",
              "fields": [
                "suggestion",
                "suggestion._2gram",
                "suggestion._3gram"
              ]
            }
          }
          """)
    Page<PurchaseOrderDocument> findBySuggestionPrefix(String prefix, Pageable pageable);
}
