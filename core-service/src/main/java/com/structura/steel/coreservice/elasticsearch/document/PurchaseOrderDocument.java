package com.structura.steel.coreservice.elasticsearch.document;

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
import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "purchase_orders")
@Setting(settingPath = "elasticsearch/settings.json")
public class PurchaseOrderDocument {

    @Id
    private Long id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "folding"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)}
    )
    private String importCode;

    @Field(type = FieldType.Long)
    private Long supplierId;

    @Field(type = FieldType.Keyword)
    private String supplierName;

    @Field(type = FieldType.Long)
    private Long projectId;

    @Field(type = FieldType.Long)
    private Long saleOrderId;

    @Field(type = FieldType.Keyword)
    private String projectName;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private Instant orderDate;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Double)
    private BigDecimal totalAmount;

    @Field(type = FieldType.Text)
    private String purchaseOrdersNote;

    @Field(type = FieldType.Boolean)
    private Boolean deleted;

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
