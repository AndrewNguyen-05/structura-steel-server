INSERT INTO delivery_orders
(created_at, created_by, updated_at, updated_by, version, additional_fees, confirmation_from_factory, confirmation_from_partner, delivery_address, delivery_date, delivery_order_note, delivery_type, delivery_unit_price, distance, driver_name, total_delivery_fee, vehicle_id, warehouse_id, purchase_order_id, sale_order_id)
VALUES
-- Use user_id: '46162566-7394-4722-b287-a2c88ffa42ec'
-- Link to either purchase_order_id (1-10) or sale_order_id (1-10)
-- Lookup addresses, drivers based on linked orders/vehicles
-- Timestamps use +07:00 timezone

-- 1. Delivery for Purchase Order 1 (Completed) -> Nhập kho 1
('2025-04-09 10:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-09 10:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, 50000.00, 'NCC1_CONF_001', NULL, 'Lô A5, KCN Sóng Thần 1, TP. Dĩ An, Bình Dương', '2025-04-09 14:00:00+07:00', 'Giao hàng PO 1 về kho Sóng Thần 1', 'Nhập kho', 15000.00, 25.0, 'Nguyễn Văn Tèo', 425000.00, 1, 1, 1, NULL),

-- 2. Delivery for Purchase Order 2 (Received) -> Nhập kho 4
('2025-04-06 11:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-06 11:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, NULL, 'NCC4_CONF_002', NULL, 'Lô C10, KCN Hòa Khánh, Q. Liên Chiểu, Đà Nẵng', '2025-04-08 09:00:00+07:00', 'Vận chuyển PO 2 ra Đà Nẵng', 'Nhập kho', 12000.00, 850.0, 'Hoàng Văn Nam', 10200000.00, 4, 4, 2, NULL),

-- 3. Delivery for Sales Order 2 (Processing) -> Giao đến dự án 2
('2025-04-10 14:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-10 14:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, 20000.00, NULL, 'KH_CONF_002', 'Đường D1, Khu công nghệ cao, Q.9, TP. Thủ Đức', '2025-04-11 10:30:00+07:00', 'Giao hàng SO 2 cho dự án KCNC', 'Giao hàng dự án', 20000.00, 15.0, 'Lê Minh Tâm', 320000.00, 2, 5, NULL, 2), -- Lấy từ Kho 5 (Kho Vật tư Xây lắp Việt Tùng)

-- 4. Delivery for Purchase Order 4 (Completed) -> Nhập kho 10
('2025-04-14 16:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-14 16:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, NULL, 'NCC10_CONF_003', NULL, 'Lô D1, KCN Long Thành, H. Long Thành, Đồng Nai', '2025-04-15 11:00:00+07:00', 'Nhập hàng PO 4 về kho Long Thành', 'Nhập kho', 18000.00, 45.0, 'Giang Việt Dũng', 810000.00, 10, 10, 4, NULL),

-- 5. Delivery for Sales Order 3 (Completed) -> Giao đến dự án 3
('2025-04-09 08:30:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-09 08:30:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, 75000.00, NULL, 'KH_CONF_003', 'Km 15, Xa lộ Hà Nội, TP. Biên Hòa, Đồng Nai', '2025-04-09 15:00:00+07:00', 'Giao hàng SO 3 dự án Kho Long Bình', 'Giao hàng dự án', 16000.00, 30.0, 'Phạm Đức Huy', 555000.00, 3, 78, NULL, 3), -- Lấy từ kho 78 (Kho Xe Tải Lợi Nhung)

-- 6. Delivery for Purchase Order 6 (Received) -> Nhập kho 16
('2025-04-18 10:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-18 10:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, 100000.00, 'NCC16_CONF_004', NULL, 'Lô E2, KCN Hiệp Phước, H. Nhà Bè, TP.HCM', '2025-04-19 09:30:00+07:00', 'Nhập hàng PO 6 về kho Hiệp Phước', 'Nhập kho', 22000.00, 22.0, 'Kiều Bảo Lộc', 584000.00, 15, 16, 6, NULL),

-- 7. Delivery for Sales Order 6 (Processing) -> Giao đến dự án 6
('2025-04-12 09:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-12 09:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, NULL, NULL, 'KH_CONF_006', 'Cảng Cát Lái, Đường Nguyễn Thị Định, TP. Thủ Đức', '2025-04-12 14:00:00+07:00', 'Giao hàng SO 6 cho Cảng Cát Lái', 'Giao hàng dự án', 25000.00, 8.0, 'Đặng Minh Long', 200000.00, 6, 15, NULL, 6), -- Lấy từ kho 15 (Kho bãi xe Phúc Liên)

-- 8. Delivery for Purchase Order 7 (Completed) -> Nhập kho 19
('2025-04-12 13:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-12 13:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, NULL, 'NCC19_CONF_005', NULL, 'Lô G9, KCN Mỹ Phước 1, TX. Bến Cát, Bình Dương', '2025-04-13 10:00:00+07:00', 'Nhập PO 7 kho Mỹ Phước 1', 'Nhập kho', 14000.00, 55.0, 'Trịnh Văn Bảy', 770000.00, 7, 19, 7, NULL),

-- 9. Delivery for Sales Order 7 (Completed) -> Giao đến dự án 7
('2025-04-11 15:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-11 15:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, 30000.00, NULL, 'KH_CONF_007', 'Lô C3, KCN Nhơn Trạch 2, Nhơn Trạch, Đồng Nai', '2025-04-12 11:00:00+07:00', 'Giao SO 7 KCN Nhơn Trạch 2', 'Giao hàng dự án', 19000.00, 60.0, 'Ngô Thanh Hải', 1170000.00, 8, 88, NULL, 7), -- Lấy từ kho 88 (Kho Thép Dũng Mai)

-- 10. Delivery for Purchase Order 9 (Received) -> Nhập kho 25
('2025-04-13 16:30:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-13 16:30:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, NULL, 'NCC25_CONF_006', NULL, 'Lô I6, KCN Tân Đông Hiệp B, TP. Dĩ An, Bình Dương', '2025-04-14 14:30:00+07:00', 'Nhập hàng PO 9 kho Tân Đông Hiệp B', 'Nhập kho', 16000.00, 32.0, 'Bùi Quốc Khánh', 512000.00, 9, 25, 9, NULL),

-- 11. Delivery for Sales Order 10 (Processing) -> Giao đến dự án 10
('2025-04-14 10:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-14 10:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, 45000.00, NULL, 'KH_CONF_010', 'Lô D5, KCN Mỹ Phước 3, TX. Bến Cát, Bình Dương', '2025-04-15 09:00:00+07:00', 'Giao hàng SO 10 KCN Mỹ Phước 3', 'Giao hàng dự án', 17000.00, 65.0, 'Phan Minh Trí', 1150000.00, 14, 10, NULL, 10), -- Lấy từ kho 10 (Tổng kho Phân Phối Thép Tuấn Hà)

-- 12. Delivery for Purchase Order 1 (Second truckload) -> Nhập kho 1
('2025-04-09 11:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-09 11:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, 50000.00, 'NCC1_CONF_001B', NULL, 'Lô A5, KCN Sóng Thần 1, TP. Dĩ An, Bình Dương', '2025-04-09 16:00:00+07:00', 'Giao hàng PO 1 (chuyến 2)', 'Nhập kho', 15000.00, 25.0, 'Ngô Việt Anh', 425000.00, 34, 1, 1, NULL), -- Dùng xe khác của partner 3

-- 13. Delivery for Sales Order 2 (Second delivery) -> Giao đến dự án 2
('2025-04-15 09:30:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-15 09:30:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, NULL, NULL, 'KH_CONF_002B', 'Đường D1, Khu công nghệ cao, Q.9, TP. Thủ Đức', '2025-04-15 15:00:00+07:00', 'Giao hàng SO 2 đợt 2', 'Giao hàng dự án', 20000.00, 15.0, 'Bùi Đình Sơn', 300000.00, 35, 5, NULL, 2), -- Dùng xe khác của partner 6, từ kho 5

-- 14. Delivery for Purchase Order 4 (Part 2) -> Nhập kho 10
('2025-04-15 08:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-15 08:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, NULL, 'NCC10_CONF_003B', NULL, 'Lô D1, KCN Long Thành, H. Long Thành, Đồng Nai', '2025-04-16 09:00:00+07:00', 'Nhập hàng PO 4 (đợt 2)', 'Nhập kho', 18000.00, 45.0, 'Lương Tấn Lực', 810000.00, 30, 10, 4, NULL), -- Dùng xe của partner 30

-- 15. Delivery for Sales Order 7 (Part 2) -> Giao đến dự án 7
('2025-04-13 14:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-13 14:00:00+07:00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, NULL, NULL, 'KH_CONF_007B', 'Lô C3, KCN Nhơn Trạch 2, Nhơn Trạch, Đồng Nai', '2025-04-14 10:00:00+07:00', 'Giao SO 7 KCN Nhơn Trạch 2 (đợt 2)', 'Giao hàng dự án', 19000.00, 60.0, 'Vũ Anh Đức', 1140000.00, 31, 88, NULL, 7); -- Dùng xe của partner 93, từ kho 88