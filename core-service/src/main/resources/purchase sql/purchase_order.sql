INSERT INTO purchase_orders
(created_at, created_by, updated_at, updated_by, version, order_date, project_id, purchase_orders_note, status, supplier_id, total_amount, warehouse_id, user_id)
VALUES
-- PO 1: supplier_id=1 không khớp SO nào -> project_id=1 (giữ nguyên)
('2025-04-01 08:00:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-01 08:00:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, '2025-04-01 07:30:00+00', 1, 'Đặt hàng thép vằn cho dự án 1', 'Completed', 22   , 75500.50, 1, '46162566-7394-4722-b287-a2c88ffa42ec'),
-- PO 2: supplier_id=4 không khớp SO nào -> project_id=NULL (giữ nguyên)
('2025-04-01 09:15:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-01 09:15:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, '2025-04-01 09:00:00+00', 2, 'Nhập thép tấm bổ sung kho 4', 'Received', 5, 120300.00, 4, '46162566-7394-4722-b287-a2c88ffa42ec'),
-- PO 3: supplier_id=7 không khớp SO nào -> project_id=7 (giữ nguyên)
('2025-04-02 10:00:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-02 10:00:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, '2025-04-02 09:45:00+00', 3, 'Đơn hàng thép hộp dự án 7', 'Processing', 78, 55000.00, 7, '46162566-7394-4722-b287-a2c88ffa42ec'),
-- PO 4: supplier_id=10 khớp SO 10 (partner_id=10, project_id=10) -> CẬP NHẬT project_id = 10
('2025-04-02 11:30:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-02 11:30:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, '2025-04-02 11:00:00+00', 4, 'Nhập thép hình cho kho 10 (theo SO 10)', 'Completed', 41, 210000.75, 10, '46162566-7394-4722-b287-a2c88ffa42ec'),
-- PO 5: supplier_id=13 không khớp SO nào -> project_id=NULL (giữ nguyên)
('2025-04-03 14:00:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-03 14:00:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, '2025-04-03 13:30:00+00', 5, 'Đặt thép tấm kho 13', 'New', 99, 95800.00, 13, '46162566-7394-4722-b287-a2c88ffa42ec'),
-- PO 6: supplier_id=16 không khớp SO nào -> project_id=16 (giữ nguyên)
('2025-04-03 15:45:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-03 15:45:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, '2025-04-03 15:00:00+00', 6, 'Thép công nghiệp dự án 16', 'Received', 15, 180000.00, 16, '46162566-7394-4722-b287-a2c88ffa42ec'),
-- PO 7: supplier_id=19 không khớp SO nào -> project_id=NULL (giữ nguyên)
('2025-04-04 08:30:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-04 08:30:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, '2025-04-04 08:00:00+00', 7, 'Nhập thép ống kho 19', 'Completed', 88, 45200.25, 19, '46162566-7394-4722-b287-a2c88ffa42ec'),
-- PO 8: supplier_id=22 khớp SO 1 (partner_id=22, project_id=1) -> CẬP NHẬT project_id = 1
('2025-04-04 09:00:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-04 09:00:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, '2025-04-04 08:45:00+00', 8, 'Đặt hàng thép hình từ NCC 22 (theo SO 1)', 'Processing', 5, 300500.00, 22, '46162566-7394-4722-b287-a2c88ffa42ec'),
-- PO 9: supplier_id=25 không khớp SO nào -> project_id=25 (giữ nguyên)
('2025-04-05 10:10:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-05 10:10:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, '2025-04-05 10:00:00+00', 9, 'Thép tấm cho dự án 25', 'Received', 63, 110000.00, 25, '46162566-7394-4722-b287-a2c88ffa42ec'),
-- PO 10: supplier_id=28 không khớp SO nào -> project_id=NULL (giữ nguyên)
('2025-04-05 13:00:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', '2025-04-05 13:00:00+00', '46162566-7394-4722-b287-a2c88ffa42ec', 1, '2025-04-05 12:30:00+00', 10, 'Đơn hàng bị hủy từ NCC 28', 'Cancelled', 10, 88000.00, 28, '46162566-7394-4722-b287-a2c88ffa42ec');