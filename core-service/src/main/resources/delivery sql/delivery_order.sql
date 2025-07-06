-- Ghi chú: Đã bổ sung cột 'updated_by' còn thiếu và loại bỏ cột 'id'.

INSERT INTO delivery_orders (
    created_at, created_by, updated_at, updated_by, version,
    delivery_code, status, delivery_date, sender_address, delivery_address,
    partner, vehicle, driver_name,
    confirmation_from_partner, confirmation_from_factory, confirmation_from_receiver,
    distance, delivery_unit_price, additional_fees, total_delivery_fee,
    delivery_order_note, purchase_order_id, deleted
) VALUES
-- Giao hàng 1 (cho PO 1)
('2025-07-10 10:00:00+07:00', 'System', '2025-07-10 14:05:00+07:00', 'System', 1,
 'DLV20250710A1', 'DONE', '2025-07-10 14:00:00+07:00', 'Kho Thép Hòa Phát, KCN Phú Mỹ', 'Lô A5, KCN Sóng Thần 1, TP. Dĩ An',
 '{"id": 3, "partnerName": "Vận Tải Sắt Thép Nam Bắc"}'::jsonb,
 '{"id": 1, "licensePlate": "51C-10001"}'::jsonb, 'Nguyễn Văn Tèo',
 'YES', 'YES', 'YES', 25.0, 15000.00, 50000.00, 425000.00,
 'Giao hàng PO 1 về kho Sóng Thần 1', 1, false),

-- Giao hàng 2 (cho PO 2)
('2025-07-11 07:00:00+07:00', 'System', '2025-07-11 09:10:00+07:00', 'System', 1,
 'DLV20250711B2', 'IN_TRANSIT', '2025-07-12 09:00:00+07:00', 'Kho Thép Pomina, KCN Phú Mỹ', 'Lô C10, KCN Hòa Khánh, Đà Nẵng',
 '{"id": 6, "partnerName": "Logistics Cường Trang"}'::jsonb,
 '{"id": 4, "licensePlate": "61C-10004"}'::jsonb, 'Hoàng Văn Nam',
 'YES', 'YES', 'PENDING', 850.0, 12000.00, 0, 10200000.00,
 'Vận chuyển PO 2 ra Đà Nẵng', 2, false),

-- Giao hàng 3 (cũng cho PO 2)
('2025-07-11 08:00:00+07:00', 'System', '2025-07-11 10:35:00+07:00', 'System', 1,
 'DLV20250711C3', 'DONE', '2025-07-11 10:30:00+07:00', 'Kho Thép Việt-Đức, Sóng Thần 2', 'Khu công nghệ cao, TP. Thủ Đức',
 '{"id": 3, "partnerName": "Vận Tải Sắt Thép Nam Bắc"}'::jsonb,
 '{"id": 2, "licensePlate": "60A-10002"}'::jsonb, 'Lê Minh Tâm',
 'YES', 'YES', 'YES', 15.0, 20000.00, 20000.00, 320000.00,
 'Giao hàng từ PO 2 cho dự án KCNC (phục vụ SO 2)', 2, false),

-- Giao hàng 4 (cho PO 4)
('2025-07-12 09:00:00+07:00', 'System', '2025-07-12 11:10:00+07:00', 'System', 1,
 'DLV20250712D4', 'DONE', '2025-07-12 11:00:00+07:00', 'Cảng Quốc tế Long An', 'KCN Long Thành, Đồng Nai',
 '{"id": 9, "partnerName": "Vận Chuyển Thắng Hồng"}'::jsonb,
 '{"id": 10, "licensePlate": "29C-10010"}'::jsonb, 'Giang Việt Dũng',
 'YES', 'YES', 'YES', 45.0, 18000.00, 0, 810000.00,
 'Nhập hàng PO 4 về kho Long Thành', 4, false),

-- Giao hàng 5 (cho PO 3)
('2025-07-13 13:00:00+07:00', 'System', '2025-07-13 15:05:00+07:00', 'System', 1,
 'DLV20250713E5', 'DONE', '2025-07-13 15:00:00+07:00', 'Kho Thép Miền Nam, KCN Biên Hòa 1', 'Km 15, Xa lộ Hà Nội, Biên Hòa',
 '{"id": 6, "partnerName": "Logistics Cường Trang"}'::jsonb,
 '{"id": 3, "licensePlate": "72B-10003"}'::jsonb, 'Phạm Đức Huy',
 'YES', 'YES', 'YES', 30.0, 16000.00, 75000.00, 555000.00,
 'Giao hàng từ PO 3 đến dự án Kho Long Bình (phục vụ SO 4)', 3, false),

-- Giao hàng 6 (cho PO 7)
('2025-07-14 13:00:00+07:00', 'System', '2025-07-14 14:05:00+07:00', 'System', 1,
 'DLV20250714F6', 'DELIVERED', '2025-07-14 14:00:00+07:00', 'Tổng kho Thép Nguyễn Minh, Thủ Đức', 'Khu đô thị Sala, TP. Thủ Đức',
 '{"id": 6, "partnerName": "Logistics Cường Trang"}'::jsonb,
 '{"id": 6, "licensePlate": "51D-10006"}'::jsonb, 'Đặng Minh Long',
 'YES', 'YES', 'YES', 8.0, 25000.00, 0, 200000.00,
 'Giao hàng từ PO 7 cho dự án KCNC (phục vụ SO 2)', 7, false),

-- Giao hàng 7 (cho PO 8)
('2025-07-15 08:00:00+07:00', 'System', NOW(), 'System', 1,
 'DLV20250715G7', 'NEW', '2025-07-16 15:00:00+07:00', 'Nhà máy Thép ống SeAH, Biên Hòa 2', 'Lô D5, KCN Mỹ Phước 3, Bến Cát',
 '{"id": 12, "partnerName": "Vận Tải Container Sơn Kim"}'::jsonb,
 '{"id": 14, "licensePlate": "61B-10014"}'::jsonb, 'Phan Minh Trí',
 'PENDING', 'PENDING', 'PENDING', 65.0, 17000.00, 45000.00, 1150000.00,
 'Giao hàng từ PO 8 về kho Bến Cát', 8, false),

-- Giao hàng 8 (cũng cho PO 3)
('2025-07-15 12:00:00+07:00', 'System', '2025-07-15 15:00:00+07:00', 'System', 1,
 'DLV20250715H8', 'DONE', '2025-07-15 15:00:00+07:00', 'Kho NCC Thép Việt Nhật, Đồng Nai', 'Kho Structura - Sóng Thần',
 '{"id": 3, "partnerName": "Vận Tải Sắt Thép Nam Bắc"}'::jsonb,
 '{"id": 1, "licensePlate": "51C-10001"}'::jsonb, 'Nguyễn Văn Tèo',
 'YES', 'YES', 'YES', 20.0, 15000.00, 0.00, 300000.00,
 'Nhận hàng cho PO 3 (phục vụ SO 4)', 3, false);