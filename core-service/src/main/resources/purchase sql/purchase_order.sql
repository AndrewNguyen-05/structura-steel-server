-- Ghi chú: Đã bổ sung created_by, updated_by và bỏ cú pháp gộp nhiều VALUES.
-- Mỗi dòng là một câu lệnh INSERT hoàn chỉnh.

INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, sale_order_id, import_code, supplier, project, status, total_amount, total_weight, purchase_orders_note, confirmation_from_supplier, deleted) VALUES
    (NOW() - interval '4 day', 'System', NOW() - interval '4 day', 'System', 1, 1, 'IMP-L-SO1-20250710', '{"id": 22, "partnerName": "Thép Hình Tú Hà"}'::jsonb, '{"id": 1, "projectName": "Nhà xưởng Tân Tạo"}'::jsonb, 'DONE', 1363050.00, 90.87, 'Mua hàng theo đơn bán hàng #1', 'YES', false);

INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, sale_order_id, import_code, supplier, project, status, total_amount, total_weight, purchase_orders_note, confirmation_from_supplier, deleted) VALUES
    (NOW() - interval '3 day', 'System', NOW() - interval '3 day', 'System', 1, NULL, 'IMP-F-STOCK1-20250711', '{"id": 5, "partnerName": "Xây Lắp Việt Tùng"}'::jsonb, NULL, 'DONE', 142845000.00, 9523.00, 'Nhập thép tấm và thép hộp về kho chính', 'YES', false);

INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, sale_order_id, import_code, supplier, project, status, total_amount, total_weight, purchase_orders_note, confirmation_from_supplier, deleted) VALUES
    (NOW() - interval '2 day', 'System', NOW() - interval '2 day', 'System', 1, 4, 'IMP-L-SO4-20250712', '{"id": 41, "partnerName": "Xây Dựng Khiêm Lam"}'::jsonb, '{"id": 6, "projectName": "Nâng cấp Cảng Cát Lái"}'::jsonb, 'PROCESSING', 1401450.00, 93.43, 'Mua hàng theo đơn bán hàng #4', 'NO', false);

INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, sale_order_id, import_code, supplier, project, status, total_amount, total_weight, purchase_orders_note, confirmation_from_supplier, deleted) VALUES
    (NOW() - interval '1 day', 'System', NOW() - interval '1 day', 'System', 1, NULL, 'IMP-F-STOCK2-20250713', '{"id": 22, "partnerName": "Thép Hình Tú Hà"}'::jsonb, NULL, 'NEW', 54075000.00, 3605.00, 'Nhập số lượng lớn thép vằn D10', 'NO', false);

INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, sale_order_id, import_code, supplier, project, status, total_amount, total_weight, purchase_orders_note, confirmation_from_supplier, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 5, 'IMP-L-SO5-20250714', '{"id": 8, "partnerName": "Thép Công Ánh"}'::jsonb, '{"id": 8, "projectName": "Cao ốc Crescent Hub"}'::jsonb, 'NEW', 1230150.00, 82.01, 'Mua hàng theo đơn bán hàng #5', 'NO', false);

INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, sale_order_id, import_code, supplier, project, status, total_amount, total_weight, purchase_orders_note, confirmation_from_supplier, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, NULL, 'IMP-F-STOCK3-20250715', '{"id": 99, "partnerName": "Giao Hàng Kiên Liên"}'::jsonb, NULL, 'CANCELLED', 35325000.00, 2355.00, 'Hủy đơn do nhà cung cấp hết hàng', 'NO', true);

INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, sale_order_id, import_code, supplier, project, status, total_amount, total_weight, purchase_orders_note, confirmation_from_supplier, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 2, 'IMP-L-SO2-20250716', '{"id": 5, "partnerName": "Xây Lắp Việt Tùng"}'::jsonb, '{"id": 2, "projectName": "Trung tâm Nghiên cứu CNC"}'::jsonb, 'PROCESSING', 9999150.00, 666.61, 'Mua hàng theo đơn bán hàng #2', 'NO', false);

INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, sale_order_id, import_code, supplier, project, status, total_amount, total_weight, purchase_orders_note, confirmation_from_supplier, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, NULL, 'IMP-F-STOCK4-20250717', '{"id": 88, "partnerName": "Thép Dũng Mai"}'::jsonb, NULL, 'DONE', 10500000.00, 700.00, 'Nhập kho thép hình I-150', 'YES', false);