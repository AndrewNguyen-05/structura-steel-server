-- Ghi chú: Đã loại bỏ cột `id` khỏi câu lệnh INSERT.

INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, total_weight, sale_orders_note, deleted) VALUES
-- Sale Order 1
('2025-07-01 16:00:00+07', 'System', '2025-07-01 16:00:00+07', 'System', 1, 'EXP20250701AABBCC01', '{"id": 22, "partnerName": "Công ty TNHH Thép Hình Tú Hà"}'::jsonb, '{"id": 1, "projectName": "Nhà xưởng Tân Tạo Mở Rộng"}'::jsonb,
 'DONE', 1567506.00, 90.87, 'Đã hoàn thành giao hàng và thanh toán.', false),

-- Sale Order 2
('2025-07-02 16:00:00+07', 'System', '2025-07-03 16:00:00+07', 'System', 1, 'EXP20250702AABBCC02', '{"id": 5, "partnerName": "Doanh nghiệp Xây Lắp Việt Tùng"}'::jsonb, '{"id": 2, "projectName": "Trung tâm Nghiên cứu CNC"}'::jsonb,
 'PROCESSING', 11499022.00, 666.61, 'Đang xử lý xuất kho, dự kiến giao hàng trong 2 ngày.', false),

-- Sale Order 3
('2025-07-03 16:00:00+07', 'System', '2025-07-03 16:00:00+07', 'System', 1, 'EXP20250703AABBCC03', '{"id": 41, "partnerName": "Công ty Xây Dựng Khiêm Lam"}'::jsonb, '{"id": 4, "projectName": "Nhà máy Sản xuất Linh kiện Amata"}'::jsonb,
 'CANCELLED', 14624550.00, 847.80, 'Khách hàng hủy đơn do thay đổi thiết kế.', true),

-- Sale Order 4
('2025-07-04 16:00:00+07', 'System', '2025-07-05 16:00:00+07', 'System', 1, 'EXP20250704AABBCC04', '{"id": 2, "partnerName": "Tập đoàn Xây Dựng Việt Phát"}'::jsonb, '{"id": 6, "projectName": "Nâng cấp Cảng Cát Lái"}'::jsonb,
 'DELIVERED', 1611667.00, 93.43, 'Đã giao hàng thành công, chờ đối soát công nợ.', false),

-- Sale Order 5
('2025-07-06 16:00:00+07', 'System', '2025-07-06 16:00:00+07', 'System', 1, 'EXP20250706AABBCC05', '{"id": 8, "partnerName": "Công ty Thép Công Ánh"}'::jsonb, '{"id": 8, "projectName": "Cao ốc Crescent Hub"}'::jsonb,
 'NEW', 1414672.00, 82.01, 'Đơn hàng mới, yêu cầu báo giá gấp.', false),

-- Sale Order 6
('2025-06-26 16:00:00+07', 'System', '2025-07-04 16:00:00+07', 'System', 1, 'EXP20250626AABBCC06', '{"id": 11, "partnerName": "Xây dựng Dân Dụng Long Nhi"}'::jsonb, '{"id": 11, "projectName": "Trung tâm Thương mại Sala Center"}'::jsonb,
 'IN_TRANSIT', 11271120.00, 653.40, 'Xe BKS 51C-10001 đang vận chuyển, dự kiến tới nơi lúc 16:00.', false),

-- Sale Order 7
('2025-06-16 16:00:00+07', 'System', '2025-06-21 16:00:00+07', 'System', 1, 'EXP20250616AABBCC07', '{"id": 3, "partnerName": "Vận Tải Sắt Thép Nam Bắc"}'::jsonb, '{"id": 20, "projectName": "Tòa nhà Văn phòng Sunshine Tower"}'::jsonb,
 'DONE', 4830000.00, 280.00, 'Đơn hàng nhỏ, đã hoàn tất.', false),

-- Sale Order 8
('2025-07-06 16:00:00+07', 'System', '2025-07-06 16:00:00+07', 'System', 1, 'EXP20250706AABBCC08', '{"id": 77, "partnerName": "Nam Lan Construction"}'::jsonb, '{"id": 91, "projectName": "Bệnh viện Vinmec Times City"}'::jsonb,
 'NEW', 649358900.00, 37644.00, 'Đơn hàng lớn, cần xác nhận khả năng cung ứng.', false);