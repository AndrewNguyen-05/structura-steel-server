package com.structura.steel.partnerservice.elasticsearch.repository;

import com.structura.steel.partnerservice.elasticsearch.document.WarehouseDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WarehouseSearchRepository extends ElasticsearchRepository<WarehouseDocument, Long> {

    Page<WarehouseDocument> getAllByPartnerIdAndDeleted(Long partnerId, boolean deleted, Pageable pageable);

    @Query("""
      {
        "bool": {
          "must": [
            {
              "multi_match": {
                "query": "?0",
                "type": "bool_prefix",
                "analyzer": "folding",
                "fields": [
                  "warehouseName",        "warehouseName._2gram",
                  "warehouseCode",        "warehouseCode._2gram"
                ]
              }
            },
            { "term": { "deleted":   ?1 } },
            { "term": { "partnerId": ?2 } }
          ]
        }
      }
    """)
    Page<WarehouseDocument> searchByKeywordAndPartnerId(
            String searchKeyword,
            boolean deleted,
            Long partnerId,
            Pageable pageable
    );

    @Query("""
      {
        "bool": {
          "must": [
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
            },
            { "term": { "deleted":   ?1 } },
            { "term": { "partnerId": ?2 } }
          ]
        }
      }
    """)
    Page<WarehouseDocument> findBySuggestionPrefix(
            String prefixKeyword,
            boolean deleted,
            Long partnerId,
            Pageable pageable
    );
}
