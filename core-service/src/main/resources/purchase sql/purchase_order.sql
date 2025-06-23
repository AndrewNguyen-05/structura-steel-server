-- Cập nhật câu lệnh INSERT cho purchase_orders với trường total_weight
INSERT INTO purchase_orders (created_at, created_by, updated_at, updated_by, version, import_code, supplier, project, purchase_orders_note, status, total_amount, total_weight, confirmation_from_supplier, deleted) VALUES
-- Purchase Order 1 (Total Weight: 45.50 + 23.20 = 68.70)
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250401CDEFGH1JKLMA1',
 '{
   "id": 22, "partnerType": "SUPPLIER", "partnerName": "Công ty TNHH Thép Hình Tú Hà", "partnerCode": "PTN202505146BYQ1YUGYMMMM", "taxCode": "0100100022",
   "legalRepresentative": "Giang Tuấn Tú", "legalRepresentativePhone": "0908100022", "contactPerson": "Châu Ngọc Hà", "contactPersonPhone": "0918100022",
   "bankName": "Agribank", "bankAccountNumber": "9704050010022", "debtPayable": 0.00, "debtReceivable": 0.00,
   "warehouses": [{"id": 22, "warehouseName": "Kho Thép Hình Tú Hà", "warehouseCode": "WH20250514AXYZ12366VVM", "warehouseAddress": "Lô H3, KCN Sóng Thần 3, TP. Thủ Dầu Một, Bình Dương", "partnerId": 22, "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"}],
   "version": 0, "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 '{
   "id": 1, "partnerId": 22, "projectCode": "PRJ202505148K0QEYBC7KA1", "projectName": "Cung cấp thép kết cấu cho Nhà xưởng Tân Tạo Mở Rộng (Phase 1)",
   "projectAddress": "Lô A1-1, KCN Tân Tạo, Q. Bình Tân, TP.HCM", "projectRepresentative": "Trần Văn Mạnh", "projectRepresentativePhone": "0912345678",
   "contactPerson": "Lê Thị Hoa", "contactPersonPhone": "0987654321", "address": "10 Lý Tự Trọng, P. Bến Nghé, Q.1, TP.HCM", "version": 0,
   "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 'Đặt hàng thép vằn cho dự án 1', 'DONE', 75500.50, 68.70, 'NO', false),

-- Purchase Order 2 (Total Weight: 15.00 + 22.50 + 7.50 = 45.00)
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250401CDEFGH2JKLMB2',
 '{
   "id": 5, "partnerType": "SUPPLIER", "partnerName": "Doanh nghiệp Xây Lắp Việt Tùng", "partnerCode": "PTN202505146BYQ1WVKLEE5", "taxCode": "0100100005",
   "legalRepresentative": "Trịnh Quốc Việt", "legalRepresentativePhone": "0908100005", "contactPerson": "Ngô Thanh Tùng", "contactPersonPhone": "0918100005",
   "bankName": "Techcombank", "bankAccountNumber": "9704360010005", "debtPayable": 0.00, "debtReceivable": 0.00,
   "warehouses": [{"id": 5, "warehouseName": "Kho Vật tư Xây lắp Việt Tùng", "warehouseCode": "WH20250514AXYZ12349EE5", "warehouseAddress": "Đường N2, KCN Mỹ Phước 2, TX. Bến Cát, Bình Dương", "partnerId": 5, "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"}],
   "version": 0, "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 '{
   "id": 2, "partnerId": 5, "projectCode": "PRJ202505148K0QEYGNL8B2", "projectName": "Phân phối thép chuyên dụng cho Trung tâm Nghiên cứu CNC",
   "projectAddress": "Đường D1, Khu công nghệ cao, Q.9, TP. Thủ Đức", "projectRepresentative": "Nguyễn Thị Lan", "projectRepresentativePhone": "0912345679",
   "contactPerson": "Phạm Minh Tuấn", "contactPersonPhone": "0987654322", "address": "25 Pasteur, P. Nguyễn Thái Bình, Q.1, TP.HCM", "version": 0,
   "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 'Nhập thép tấm bổ sung kho 4', 'DONE', 120300.00, 45.00, 'NO', false),

-- Purchase Order 3 (Total Weight: 33.00 + 25.00 = 58.00)
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250402CDEFGH3JKLMC3',
 '{
   "id": 78, "partnerType": "SUPPLIER", "partnerName": "Dịch Vụ Xe Tải Lợi Nhung", "partnerCode": "PTN202505146BYQ27PBFQ26", "taxCode": "0400100078",
   "legalRepresentative": "Hoàng Văn Lợi", "legalRepresentativePhone": "0908100078", "contactPerson": "Vũ Cẩm Nhung", "contactPersonPhone": "0918100078",
   "bankName": "VietinBank", "bankAccountNumber": "9704150010078", "debtPayable": 0.00, "debtReceivable": 0.00,
   "warehouses": [{"id": 78, "warehouseName": "Kho Xe Tải Lợi Nhung (QL51)", "warehouseCode": "WH20250514BXYZ12322ZZQG6", "warehouseAddress": "Khu vực Ngã Ba Thái Lan, Quốc lộ 51, Đồng Nai", "partnerId": 78, "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"}],
   "version": 0, "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 '{
   "id": 3, "partnerId": 78, "projectCode": "PRJ202505148K0QEYLTTGC3", "projectName": "Cung cấp thép xây dựng Kho vận Long Bình",
   "projectAddress": "Km 15, Xa lộ Hà Nội, TP. Biên Hòa, Đồng Nai", "projectRepresentative": "Vũ Đức Hải", "projectRepresentativePhone": "0912345680",
   "contactPerson": "Hoàng Thu Trang", "contactPersonPhone": "0987654323", "address": "50 Nguyễn Huệ, P. Bến Nghé, Q.1, TP.HCM", "version": 0,
   "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 'Đơn hàng thép hộp dự án 7', 'PROCESSING', 55000.00, 58.00, 'NO', false),

-- Purchase Order 4 (Total Weight: 18.00 + 12.00 + 9.00 = 39.00)
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250402CDEFGH4JKLMD4',
 '{
   "id": 41, "partnerType": "SUPPLIER", "partnerName": "Công ty Hợp Tác Đầu Tư Xây Dựng Khiêm Lam", "partnerCode": "PTN202505146BYQ21SFTC15", "taxCode": "0300100041",
   "legalRepresentative": "Tăng Gia Khiêm", "legalRepresentativePhone": "0908100041", "contactPerson": "Mạc Tường Lam", "contactPersonPhone": "0918100041",
   "bankName": "Vietcombank", "bankAccountNumber": "9704180010041", "debtPayable": 0.00, "debtReceivable": 0.00,
   "warehouses": [{"id": 41, "warehouseName": "Kho Đầu Tư Xây Dựng Khiêm Lam", "warehouseCode": "WH20250514AXYZ12385OOF5", "warehouseAddress": "Đường số 2, KCN Vĩnh Lộc, H. Bình Chánh, TP.HCM", "partnerId": 41, "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"}],
   "version": 0, "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 '{
   "id": 4, "partnerId": 41, "projectCode": "PRJ202505148K0QEYQZ74D4", "projectName": "Quản lý vật tư thép Nhà máy Sản xuất Linh kiện Amata",
   "projectAddress": "Lô B2, KCN Amata, TP. Biên Hòa, Đồng Nai", "projectRepresentative": "Đặng Minh Khoa", "projectRepresentativePhone": "0912345681",
   "contactPerson": "Vũ Ngọc Anh", "contactPersonPhone": "0987654324", "address": "112 Hàm Nghi, P. Bến Thành, Q.1, TP.HCM", "version": 0,
   "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 'Nhập thép hình cho kho 10 (theo SO 10)', 'DONE', 210000.75, 39.00, 'NO', false),

-- Purchase Order 5 (Total Weight: 0.00 - No details provided)
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250403CDEFGH5JKLME5',
 '{
   "id": 99, "partnerType": "SUPPLIER", "partnerName": "Dịch vụ Giao Hàng Nhanh Kiên Liên", "partnerCode": "PTN202505146BYQ2B1IFQ2R", "taxCode": "0500100099",
   "legalRepresentative": "Phan Trung Kiên", "legalRepresentativePhone": "0908100099", "contactPerson": "Kiều Bích Liên", "contactPersonPhone": "0918100099",
   "bankName": "BIDV", "bankAccountNumber": "9704220010099", "debtPayable": 0.00, "debtReceivable": 0.00,
   "warehouses": [{"id": 99, "warehouseName": "Kho Giao Hàng Kiên Liên (Phú Mỹ)", "warehouseCode": "WH20250514BXYZ12343UULBR", "warehouseAddress": "Khu vực cầu Phú Mỹ, Q.7, TP.HCM", "partnerId": 99, "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"}],
   "version": 0, "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 '{
   "id": 5, "partnerId": 99, "projectCode": "PRJ202505148K0QEYVKLEE5", "projectName": "Phân phối thép cho Dự án Mở rộng KCN VSIP 1 - Giai đoạn 2",
   "projectAddress": "Đường số 8, KCN VSIP 1, TP. Thuận An, Bình Dương", "projectRepresentative": "Trịnh Thanh Tâm", "projectRepresentativePhone": "0912345682",
   "contactPerson": "Đặng Văn Bình", "contactPersonPhone": "0987654325", "address": "88 Đồng Khởi, P. Bến Nghé, Q.1, TP.HCM", "version": 0,
   "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 'Đặt thép tấm kho 13', 'NEW', 95800.00, 0.00, 'NO', false),

-- Purchase Order 6 (Total Weight: 0.00 - No details provided)
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250403CDEFGH6JKLMF6',
 '{
   "id": 15, "partnerType": "TRANSPORTER", "partnerName": "Đội Xe Chuyên Chở Thép Phúc Liên", "partnerCode": "PTN202505146BYQ1YCVD0FF", "taxCode": "0100100015",
   "legalRepresentative": "Yên Văn Phúc", "legalRepresentativePhone": "0908100015", "contactPerson": "Thạch Bích Liên", "contactPersonPhone": "0918100015",
   "bankName": "Techcombank", "bankAccountNumber": "9704360010015", "debtPayable": 0.00, "debtReceivable": 0.00,
   "warehouses": [{"id": 15, "warehouseName": "Kho bãi xe Phúc Liên", "warehouseCode": "WH20250514AXYZ12359OOF", "warehouseAddress": "Gần Ngã tư Vũng Tàu, TP. Biên Hòa, Đồng Nai", "partnerId": 15, "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"}],
   "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 '{
   "id": 6, "partnerId": 15, "projectCode": "PRJ202505148K0QEZ0R00F6", "projectName": "Cung cấp thép tấm cho Công trình Nâng cấp Cảng Cát Lái",
   "projectAddress": "Cảng Cát Lái, Đường Nguyễn Thị Định, TP. Thủ Đức", "projectRepresentative": "Đỗ Hoàng Nam", "projectRepresentativePhone": "0912345683",
   "contactPerson": "Ngô Thị Mai", "contactPersonPhone": "0987654326", "address": "30 Tôn Đức Thắng, P. Bến Nghé, Q.1, TP.HCM", "version": 0,
   "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 'Thép công nghiệp dự án 16', 'DONE', 180000.00, 0.00, 'NO', false),

-- Purchase Order 7 (Total Weight: 0.00 - No details provided)
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250404CDEFGH7JKLMG7',
 '{
   "id": 88, "partnerType": "SUPPLIER", "partnerName": "Công ty TNHH Thép Dũng Mai", "partnerCode": "PTN202505146BYQ299ZX42G", "taxCode": "0400100088",
   "legalRepresentative": "Vương Tiến Dũng", "legalRepresentativePhone": "0908100088", "contactPerson": "Ông Hoàng Mai", "contactPersonPhone": "0918100088",
   "bankName": "VietinBank", "bankAccountNumber": "9704150010088", "debtPayable": 0.00, "debtReceivable": 0.00,
   "warehouses": [{"id": 88, "warehouseName": "Kho Thép Dũng Mai (Amata)", "warehouseCode": "WH20250514BXYZ12332JJA0G", "warehouseAddress": "Lô G30, KCN Amata, TP. Biên Hòa, Đồng Nai", "partnerId": 88, "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"}],
   "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 '{
   "id": 7, "partnerId": 88, "projectCode": "PRJ202505148K0QEZ5WIGG7", "projectName": "Cung cấp thép không gỉ cho Nhà máy Chế biến Thực phẩm Nhơn Trạch",
   "projectAddress": "Lô C3, KCN Nhơn Trạch 2, Nhơn Trạch, Đồng Nai", "projectRepresentative": "Giang Thị Thu", "projectRepresentativePhone": "0912345684",
   "contactPerson": "Bùi Quang Huy", "contactPersonPhone": "0987654327", "address": "15 Lê Thánh Tôn, P. Bến Nghé, Q.1, TP.HCM", "version": 0,
   "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 'Nhập thép ống kho 19', 'DONE', 45200.25, 0.00, 'NO', false),

-- Purchase Order 8 (Total Weight: 0.00 - No details provided)
(NOW(), 'System', NOW(), 'System', 1, 'IMP20250404CDEFGH8JKLMH8',
 '{
   "id": 5, "partnerType": "SUPPLIER", "partnerName": "Doanh nghiệp Xây Lắp Việt Tùng", "partnerCode": "PTN202505146BYQ1WVKLEE5", "taxCode": "0100100005",
   "legalRepresentative": "Trịnh Quốc Việt", "legalRepresentativePhone": "0908100005", "contactPerson": "Ngô Thanh Tùng", "contactPersonPhone": "0918100005",
   "bankName": "Techcombank", "bankAccountNumber": "9704360010005", "debtPayable": 0.00, "debtReceivable": 0.00,
   "warehouses": [{"id": 5, "warehouseName": "Kho Vật tư Xây lắp Việt Tùng", "warehouseCode": "WH20250514AXYZ12349EE5", "warehouseAddress": "Đường N2, KCN Mỹ Phước 2, TX. Bến Cát, Bình Dương", "partnerId": 5, "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"}],
   "version": 0, "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 '{
   "id": 8, "partnerId": 5, "projectCode": "PRJ202505148K0QEZAX24H8", "projectName": "Quản lý phân phối thép cho Dự án Cao ốc Crescent Hub",
   "projectAddress": "Khu đô thị Phú Mỹ Hưng, Q.7, TP.HCM", "projectRepresentative": "Lý Ngọc Hà", "projectRepresentativePhone": "0912345685",
   "contactPerson": "Châu Minh Đức", "contactPersonPhone": "0987654328", "address": "45 Hai Bà Trưng, P. Bến Nghé, Q.1, TP.HCM", "version": 0,
   "createdAt": "2025-05-31T10:00:00Z", "updatedAt": "2025-05-31T10:00:00Z", "createdBy": "System", "updatedBy": "System"
 }'::jsonb,
 'Đặt hàng thép hình từ NCC 22 (theo SO 1)', 'PROCESSING', 300500.00, 0.00, 'NO', false),

(
    '2025-06-13T10:00:00Z', 'System', '2025-06-13T11:00:00Z', 'System', 1,
    'IMP20250613K4X3PQR1A7',
    '{
      "id": 1, "partnerType": "SUPPLIER", "partnerName": "Công ty TNHH Thép Hòa Hưng", "partnerCode": "PTN202505146BYQ1WBC7KA1",
      "taxCode": "0300100001", "bankName": "Vietcombank", "bankAccountNumber": "9704180010001"
    }'::jsonb,
    '{
      "id": 6, "partnerId": 2, "projectCode": "PRJ202505148K0QEZ0R00F6", "projectName": "Cung cấp thép tấm cho Công trình Nâng cấp Cảng Cát Lái"
    }'::jsonb,
    NULL, -- purchase_orders_note (Thêm giá trị NULL)
    'DONE',
    25000000.00,
    0.00,
    'NO', -- confirmation_from_supplier (Thêm giá trị 'NO')
    false
);