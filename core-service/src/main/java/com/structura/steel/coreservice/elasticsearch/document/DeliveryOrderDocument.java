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
@Document(indexName = "delivery_orders")
@Setting(settingPath = "elasticsearch/settings.json")
public class DeliveryOrderDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long purchaseOrderId;

    @Field(type = FieldType.Long)
    private Long saleOrderId;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "folding"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)}
    )
    private String deliveryCode;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    private Instant deliveryDate;

    @Field(type = FieldType.Long)
    private Long vehicleId;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "folding"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)}
    )
    private String driverName;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "folding"),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)}
    )
    private String deliveryAddress;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Double)
    private BigDecimal distance;

    @Field(type = FieldType.Double)
    private BigDecimal deliveryUnitPrice;

    @Field(type = FieldType.Double)
    private BigDecimal additionalFees;

    @Field(type = FieldType.Double)
    private BigDecimal totalDeliveryFee;

    @Field(type = FieldType.Keyword)
    private String deliveryType;

    @Field(type = FieldType.Text)
    private String deliveryOrderNote;

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
