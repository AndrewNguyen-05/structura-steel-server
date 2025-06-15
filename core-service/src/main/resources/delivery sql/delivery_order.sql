-- Dữ liệu cho bảng delivery_orders với partner và vehicle được nhúng dưới dạng JSONB
-- Đã bỏ các cột partner_id, vehicle_id và thay bằng cột partner (jsonb), vehicle (jsonb).
-- Đã cập nhật các trường enum (status, confirmation_status).

-- Giao hàng 1 (PO 1, Partner 3, Vehicle 1)
INSERT INTO delivery_orders (
    created_at, created_by, updated_at, updated_by, version,
    delivery_code, status, delivery_date, delivery_address, delivery_type,
    partner, vehicle, driver_name,
    confirmation_from_partner, confirmation_from_factory, confirmation_from_receiver,
    distance, delivery_unit_price, additional_fees, total_delivery_fee,
    delivery_order_note, purchase_order_id, sale_order_id, deleted
) VALUES
    ('2025-04-09 10:00:00+07:00', 'System', '2025-04-09 14:05:00+07:00', 'System', 1,
     'DLV20250409DQRSTUVWX1A1', 'DONE', '2025-04-09 14:00:00+07:00', 'Lô A5, KCN Sóng Thần 1, TP. Dĩ An, Bình Dương', 'WAREHOUSE_IMPORT',
     '{
       "id": 3, "partnerType": "TRANSPORTER", "partnerName": "Công ty Vận Tải Sắt Thép Nam Bắc", "partnerCode": "PTN202505146BYQ1WLTTGC3", "taxCode": "0400100003",
       "legalRepresentative": "Vũ Ngọc Khánh", "legalRepresentativePhone": "0918100003", "contactPerson": "Hoàng Gia Bảo", "contactPersonPhone": "0908100003",
       "bankName": "VietinBank", "bankAccountNumber": "9704150010003",
       "warehouses": [{"id": 3, "warehouseName": "Kho bãi Huy Lan Express (Cát Lái)", "warehouseCode": "WH20250514AXYZ12347CC3", "warehouseAddress": "Bãi xe số 3, Cảng Cát Lái, TP. Thủ Đức, TP.HCM", "partnerId": 3}]
     }'::jsonb,
     '{
       "id": 1, "vehicleType": "TRACTOR_TRAILER", "licensePlate": "51C-10001", "vehicleCode": "VEH202505149ABCEFGHIJA1",
       "capacity": 30.0, "description": "Đầu kéo Mỹ International", "driverName": "Nguyễn Văn Tèo", "partnerId": 3
     }'::jsonb,
     'Nguyễn Văn Tèo',
     'YES', 'YES', 'YES',
     25.0, 15000.00, 50000.00, 425000.00,
     'Giao hàng PO 1 về kho Sóng Thần 1', 1, NULL, false
    );

-- Giao hàng 2 (PO 2, Partner 6, Vehicle 4)
INSERT INTO delivery_orders (
    created_at, created_by, updated_at, updated_by, version,
    delivery_code, status, delivery_date, delivery_address, delivery_type,
    partner, vehicle, driver_name,
    confirmation_from_partner, confirmation_from_factory, confirmation_from_receiver,
    distance, delivery_unit_price, additional_fees, total_delivery_fee,
    delivery_order_note, purchase_order_id, sale_order_id, deleted
) VALUES
    ('2025-04-08 07:00:00+07:00', 'System', '2025-04-08 09:10:00+07:00', 'System', 1,
     'DLV20250408DQRSTUVWX2B2', 'DONE', '2025-04-08 09:00:00+07:00', 'Lô C10, KCN Hòa Khánh, Q. Liên Chiểu, Đà Nẵng', 'WAREHOUSE_IMPORT',
     '{
       "id": 6, "partnerType": "TRANSPORTER", "partnerName": "Công ty Logistics Hàng Nặng Cường Trang", "partnerCode": "PTN202505146BYQ1X0R00F6", "taxCode": "0300100006",
       "legalRepresentative": "Bùi Thị Thu Trang", "legalRepresentativePhone": "0918100006", "contactPerson": "Đỗ Mạnh Cường", "contactPersonPhone": "0908100006",
       "bankName": "Vietcombank", "bankAccountNumber": "9704180010006",
       "warehouses": [{"id": 6, "warehouseName": "Kho trung chuyển Cường Trang Logistics", "warehouseCode": "WH20250514AXYZ12350FF6", "warehouseAddress": "Gần Cảng Phú Hữu, TP. Thủ Đức, TP.HCM", "partnerId": 6}]
     }'::jsonb,
     '{
       "id": 4, "vehicleType": "TRACTOR_TRAILER", "licensePlate": "61C-10004", "vehicleCode": "VEH202505149ABCDFGHIJD4",
       "capacity": 40.0, "description": "Đầu kéo Howo A7", "driverName": "Hoàng Văn Nam", "partnerId": 12
     }'::jsonb,
     'Hoàng Văn Nam',
     'YES', 'YES', 'PENDING',
     850.0, 12000.00, NULL, 10200000.00,
     'Vận chuyển PO 2 ra Đà Nẵng', 2, NULL, false
    );

-- Giao hàng 3 (SO 2, Partner 3, Vehicle 2)
INSERT INTO delivery_orders (
    created_at, created_by, updated_at, updated_by, version,
    delivery_code, status, delivery_date, delivery_address, delivery_type,
    partner, vehicle, driver_name,
    confirmation_from_partner, confirmation_from_factory, confirmation_from_receiver,
    distance, delivery_unit_price, additional_fees, total_delivery_fee,
    delivery_order_note, purchase_order_id, sale_order_id, deleted
) VALUES
    ('2025-04-11 08:00:00+07:00', 'System', '2025-04-11 10:35:00+07:00', 'System', 1,
     'DLV20250411DQRSTUVWX3C3', 'DONE', '2025-04-11 10:30:00+07:00', 'Đường D1, Khu công nghệ cao, Q.9, TP. Thủ Đức', 'PROJECT_DELIVERY',
     '{
       "id": 3, "partnerType": "TRANSPORTER", "partnerName": "Công ty Vận Tải Sắt Thép Nam Bắc", "partnerCode": "PTN202505146BYQ1WLTTGC3", "taxCode": "0400100003",
       "legalRepresentative": "Vũ Ngọc Khánh", "legalRepresentativePhone": "0918100003", "contactPerson": "Hoàng Gia Bảo", "contactPersonPhone": "0908100003",
       "bankName": "VietinBank", "bankAccountNumber": "9704150010003",
       "warehouses": [{"id": 3, "warehouseName": "Kho bãi Huy Lan Express (Cát Lái)", "warehouseCode": "WH20250514AXYZ12347CC3", "warehouseAddress": "Bãi xe số 3, Cảng Cát Lái, TP. Thủ Đức, TP.HCM", "partnerId": 3}]
     }'::jsonb,
     '{
       "id": 2, "vehicleType": "TRACTOR_TRAILER", "licensePlate": "60A-10002", "vehicleCode": "VEH202505149ABCDFGHIJB2",
       "capacity": 32.5, "description": "Moóc sàn 3 trục, dài 14m", "driverName": "Lê Minh Tâm", "partnerId": 6
     }'::jsonb,
     'Lê Minh Tâm',
     'YES', 'PENDING', 'YES',
     15.0, 20000.00, 20000.00, 320000.00,
     'Giao hàng SO 2 cho dự án KCNC', NULL, 2, false
    );

-- Giao hàng 4 (PO 4, Partner 9, Vehicle 10)
INSERT INTO delivery_orders (
    created_at, created_by, updated_at, updated_by, version,
    delivery_code, status, delivery_date, delivery_address, delivery_type,
    partner, vehicle, driver_name,
    confirmation_from_partner, confirmation_from_factory, confirmation_from_receiver,
    distance, delivery_unit_price, additional_fees, total_delivery_fee,
    delivery_order_note, purchase_order_id, sale_order_id, deleted
) VALUES
    ('2025-04-15 09:00:00+07:00', 'System', '2025-04-15 11:10:00+07:00', 'System', 1,
     'DLV20250415DQRSTUVWX4D4', 'DONE', '2025-04-15 11:00:00+07:00', 'Lô D1, KCN Long Thành, H. Long Thành, Đồng Nai', 'WAREHOUSE_IMPORT',
     '{
       "id": 9, "partnerType": "TRANSPORTER", "partnerName": "Dịch vụ Vận Chuyển Thắng Hồng", "partnerCode": "PTN202505146BYQ1XG8KMI9", "taxCode": "0500100009",
       "legalRepresentative": "Kiều Thị Hồng", "legalRepresentativePhone": "0918100009", "contactPerson": "Phan Văn Thắng", "contactPersonPhone": "0908100009",
       "bankName": "BIDV", "bankAccountNumber": "9704220010009",
       "warehouses": [{"id": 9, "warehouseName": "Kho hàng Thắng Hồng (Tân Bình)", "warehouseCode": "WH20250514AXYZ12353II9", "warehouseAddress": "Đường số 9, KCN Tân Bình, Q. Tân Phú, TP.HCM", "partnerId": 9}]
     }'::jsonb,
     '{
       "id": 10, "vehicleType": "TRACTOR_TRAILER", "licensePlate": "29C-10010", "vehicleCode": "VEH202505149ABCDFGHIJJA",
       "capacity": 28.0, "description": "Moóc sàn chuyên chở thép cuộn", "driverName": "Giang Việt Dũng", "partnerId": 30
     }'::jsonb,
     'Giang Việt Dũng',
     'YES', 'YES', 'YES',
     45.0, 18000.00, NULL, 810000.00,
     'Nhập hàng PO 4 về kho Long Thành', 4, NULL, false
    );

-- Giao hàng 5 (SO 3, Partner 6, Vehicle 3)
INSERT INTO delivery_orders (
    created_at, created_by, updated_at, updated_by, version,
    delivery_code, status, delivery_date, delivery_address, delivery_type,
    partner, vehicle, driver_name,
    confirmation_from_partner, confirmation_from_factory, confirmation_from_receiver,
    distance, delivery_unit_price, additional_fees, total_delivery_fee,
    delivery_order_note, purchase_order_id, sale_order_id, deleted
) VALUES
    ('2025-04-09 13:00:00+07:00', 'System', '2025-04-09 15:05:00+07:00', 'System', 1,
     'DLV20250409DQRSTUVWX5E5', 'DONE', '2025-04-09 15:00:00+07:00', 'Km 15, Xa lộ Hà Nội, TP. Biên Hòa, Đồng Nai', 'PROJECT_DELIVERY',
     '{
       "id": 6, "partnerType": "TRANSPORTER", "partnerName": "Công ty Logistics Hàng Nặng Cường Trang", "partnerCode": "PTN202505146BYQ1X0R00F6", "taxCode": "0300100006",
       "legalRepresentative": "Bùi Thị Thu Trang", "legalRepresentativePhone": "0918100006", "contactPerson": "Đỗ Mạnh Cường", "contactPersonPhone": "0908100006",
       "bankName": "Vietcombank", "bankAccountNumber": "9704180010006",
       "warehouses": [{"id": 6, "warehouseName": "Kho trung chuyển Cường Trang Logistics", "warehouseCode": "WH20250514AXYZ12350FF6", "warehouseAddress": "Gần Cảng Phú Hữu, TP. Thủ Đức, TP.HCM", "partnerId": 6}]
     }'::jsonb,
     '{
       "id": 3, "vehicleType": "CRANE_TRUCK", "licensePlate": "72B-10003", "vehicleCode": "VEH202505149ABCEFGHIKC3",
       "capacity": 15.0, "description": "Xe tải Hino 3 chân gắn cẩu Unic 5 tấn", "driverName": "Phạm Đức Huy", "partnerId": 9
     }'::jsonb,
     'Phạm Đức Huy',
     'YES', 'PENDING', 'YES',
     30.0, 16000.00, 75000.00, 555000.00,
     'Giao hàng SO 3 dự án Kho Long Bình', NULL, 3, false
    );

-- ... Các bản ghi khác sẽ được tạo theo cấu trúc tương tự ...

-- Giao hàng 6 (SO 6, Partner 6, Vehicle 6)
INSERT INTO delivery_orders (
    created_at, created_by, updated_at, updated_by, version,
    delivery_code, status, delivery_date, delivery_address, delivery_type,
    partner, vehicle, driver_name,
    confirmation_from_partner, confirmation_from_factory, confirmation_from_receiver,
    distance, delivery_unit_price, additional_fees, total_delivery_fee,
    delivery_order_note, purchase_order_id, sale_order_id, deleted
) VALUES
    ('2025-06-01 13:00:00+07:00', 'System', '2025-06-01 14:05:00+07:00', 'System', 1,
     'DLV20250412DQRSTUVWX7G7', 'DONE', '2025-06-01 14:00:00+07:00', 'Khu đô thị Sala, TP. Thủ Đức', 'PROJECT_DELIVERY',
     '{
       "id": 6, "partnerType": "TRANSPORTER", "partnerName": "Công ty Logistics Hàng Nặng Cường Trang", "partnerCode": "PTN202505146BYQ1X0R00F6", "taxCode": "0300100006",
       "legalRepresentative": "Bùi Thị Thu Trang", "legalRepresentativePhone": "0918100006", "contactPerson": "Đỗ Mạnh Cường", "contactPersonPhone": "0908100006",
       "bankName": "Vietcombank", "bankAccountNumber": "9704180010006",
       "warehouses": [{"id": 6, "warehouseName": "Kho trung chuyển Cường Trang Logistics", "warehouseCode": "WH20250514AXYZ12350FF6", "warehouseAddress": "Gần Cảng Phú Hữu, TP. Thủ Đức, TP.HCM", "partnerId": 6}]
     }'::jsonb,
     '{
       "id": 6, "vehicleType": "HEAVY_TRUCK", "licensePlate": "51D-10006", "vehicleCode": "VEH202505149ABCDFGHIJF6",
       "capacity": 18.0, "description": "Xe tải Dongfeng Hoàng Huy", "driverName": "Đặng Minh Long", "partnerId": 18
     }'::jsonb,
     'Đặng Minh Long',
     'YES', 'PENDING', 'YES',
     8.0, 25000.00, NULL, 200000.00,
     'Giao hàng SO 6 cho dự án Sala', NULL, 6, false
    );

-- Giao hàng 7 (cho SO 10, Partner 12, Vehicle 14)
INSERT INTO delivery_orders (
    created_at, created_by, updated_at, updated_by, version,
    delivery_code, status, delivery_date, delivery_address, delivery_type,
    partner, vehicle, driver_name,
    confirmation_from_partner, confirmation_from_factory, confirmation_from_receiver,
    distance, delivery_unit_price, additional_fees, total_delivery_fee,
    delivery_order_note, purchase_order_id, sale_order_id, deleted
) VALUES
    ('2025-06-09 08:00:00+07:00', 'System', NOW(), 'System', 1,
     'DLV20250610DQRSTUVWXYAA', 'IN_TRANSIT', '2025-06-10 15:00:00+07:00', 'Lô D5, KCN Mỹ Phước 3, TX. Bến Cát, Bình Dương', 'PROJECT_DELIVERY',
     '{
       "id": 12, "partnerType": "TRANSPORTER", "partnerName": "Vận Tải Container Sơn Kim", "partnerCode": "PTN202505146BYQ1XW3XWLC", "taxCode": "0100100012",
       "legalRepresentative": "Uông Đình Sơn", "legalRepresentativePhone": "0908100012", "contactPerson": "Nông Thị Kim", "contactPersonPhone": "0918100012",
       "bankName": "Agribank", "bankAccountNumber": "9704050010012",
       "warehouses": [{"id": 12, "warehouseName": "Bãi tập kết Container Sơn Kim", "warehouseCode": "WH20250514AXYZ12356LLC", "warehouseAddress": "Khu vực Depot Sóng Thần, TP. Dĩ An, Bình Dương", "partnerId": 12}]
     }'::jsonb,
     '{
       "id": 14, "vehicleType": "CRANE_TRUCK", "licensePlate": "61B-10014", "vehicleCode": "VEH202505149ABCDFGHIJLE",
       "capacity": 9.0, "description": "Xe tải Thaco gắn cẩu Soosan 3 tấn", "driverName": "Phan Minh Trí", "partnerId": 42
     }'::jsonb,
     'Phan Minh Trí',
     'YES', 'PENDING', 'PENDING',
     65.0, 17000.00, 45000.00, 1150000.00,
     'Giao hàng SO 10 KCN Mỹ Phước 3', NULL, 10, false
    );

INSERT INTO delivery_orders (
    created_at, created_by, updated_at, updated_by, version, delivery_code,
    status, delivery_date, delivery_address, delivery_type,
    partner, vehicle, driver_name,
    total_delivery_fee, delivery_order_note,
    purchase_order_id, sale_order_id, deleted
) VALUES (
             '2025-06-13T12:00:00Z', 'TestUser', '2025-06-13T15:00:00Z', 'TestUser', 1, 'DLV20250613L9Y4QRS2B8',
             'DONE', '2025-06-13T15:00:00Z', 'Kho Structura - Sóng Thần', 'WAREHOUSE_IMPORT',
             '{
               "id": 3, "partnerType": "TRANSPORTER", "partnerName": "Công ty Vận Tải Sắt Thép Nam Bắc"
             }'::jsonb,
             '{
               "id": 1, "vehicleType": "TRACTOR_TRAILER", "licensePlate": "51C-10001", "driverName": "Nguyễn Văn Tèo", "partnerId": 3
             }'::jsonb,
             'Nguyễn Văn Tèo',
             1500000.00, 'Giao hàng cho PO liên quan SO4',
             (SELECT id FROM purchase_orders WHERE import_code = 'IMP20250613K4X3PQR1A7'), NULL, false
         );

INSERT INTO delivery_orders (
    created_at, created_by, updated_at, updated_by, version, delivery_code,
    status, delivery_date, delivery_address, delivery_type,
    partner, vehicle, driver_name,
    total_delivery_fee, delivery_order_note,
    purchase_order_id, sale_order_id, deleted
) VALUES (
             '2025-06-14T08:00:00Z', 'TestUser', '2025-06-14T11:30:00Z', 'TestUser', 1, 'DLV20250614M1Z5STU3C9',
             'DONE', '2025-06-14T11:30:00Z', 'Cảng Cát Lái, Đường Nguyễn Thị Định, TP. Thủ Đức', 'PROJECT_DELIVERY',
             '{
               "id": 6, "partnerType": "TRANSPORTER", "partnerName": "Công ty Logistics Hàng Nặng Cường Trang"
             }'::jsonb,
             '{
               "id": 2, "vehicleType": "TRACTOR_TRAILER", "licensePlate": "60A-10002", "driverName": "Lê Minh Tâm", "partnerId": 6
             }'::jsonb,
             'Lê Minh Tâm',
             3250000.00, 'Giao hàng SO4 đến Cảng Cát Lái',
             NULL, 4, false
         );