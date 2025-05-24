package com.structura.steel.productservice.elasticsearch.repository;

import com.structura.steel.productservice.elasticsearch.document.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

	Page<ProductDocument> findAllByDeleted(boolean deleted, Pageable page);

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
                      "name",
                      "name._2gram",
                      "code",
                      "code._2gram"
                    ]
                  }
                },
                {
                	"term": { "deleted": ?1 }
                }
              ]
            }
          }
         """)
	Page<ProductDocument> searchByKeyword(String searchKeyword,
										  boolean deleted,
										  Pageable pageable);

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
					"suggestion._index_prefix",
					"suggestion._2gram",
					"suggestion._3gram"
				  ]
				}
			  },
			  {
				"term": { "deleted": ?1 }
			  }
			]
		  }
		}
		""")
	Page<ProductDocument> findBySuggestionPrefix(String prefixKeyword, boolean deleted, Pageable pageable);
	// "?0" sẽ lấy giá trị của tham số đầu tiên (prefixKeyword)
}
