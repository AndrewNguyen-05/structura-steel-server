package com.structura.steel.productservice.elasticsearch.document;

import com.structura.steel.commons.enumeration.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "products")
@Setting(settingPath = "elasticsearch/settings.json")
public class ProductDocument {
	@Id
	private Long id;

	@MultiField(
			mainField = @Field(type = FieldType.Text, analyzer = "folding"),
			otherFields = {
					@InnerField(suffix = "keyword", type = FieldType.Keyword)
			}
	)
	private String code;

	@MultiField(
			mainField = @Field(type = FieldType.Text, analyzer = "folding"),
			otherFields = {
					@InnerField(suffix = "keyword", type = FieldType.Keyword)
			}
	)
	private String name;

	@Field(type = FieldType.Keyword)
	private ProductType type;
	private BigDecimal unitWeight;
	private BigDecimal length;
	private BigDecimal width;
	private BigDecimal height;
	private BigDecimal thickness;
	private BigDecimal diameter;
	private String standard;

	@Field(type = FieldType.Short)
	private Short version;

	@Field(type = FieldType.Date, format = DateFormat.date_optional_time)
	private Date createdAt;

	@Field(type = FieldType.Date, format = DateFormat.date_optional_time)
	private Date updatedAt;

	@Field(type = FieldType.Keyword)
	private String createdBy;

	@Field(type = FieldType.Keyword)
	private String updatedBy;

	@Field(type = FieldType.Search_As_You_Type)
	private String suggestion;
}

