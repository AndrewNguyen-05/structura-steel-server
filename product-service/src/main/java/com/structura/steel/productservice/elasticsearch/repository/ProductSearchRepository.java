package com.structura.steel.productservice.elasticsearch.repository;

import com.structura.steel.productservice.elasticsearch.document.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

	Page<ProductDocument> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String nameKeyword, String codeKeyword, Pageable pageable);

	@Query("""
          {
            "multi_match": {
              "query":    "?0",
              "productType":     "phrase_prefix",
              "analyzer": "folding",
              "fields": [
                "name",
                "partnerName._2gram",
                "code",
                "partnerCode._2gram"
              ]
            }
          }
          """)
	Page<ProductDocument> searchByKeyword(String searchKeyword, Pageable pageable);

	@Query("{\"multi_match\": {\"query\": \"?0\", \"productType\": \"bool_prefix\", \"fields\": [\"suggestion\", \"suggestion._2gram\", \"suggestion._3gram\"]}}")
	Page<ProductDocument> findBySuggestionPrefix(String prefixKeyword, Pageable pageable);
	// "?0" sẽ lấy giá trị của tham số đầu tiên (prefixKeyword)
}
