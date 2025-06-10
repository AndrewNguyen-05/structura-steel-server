-- Dữ liệu mẫu cho bảng sale_orders
-- Ghi chú: Thông tin các kho hàng (warehouses) được nhúng thành một mảng JSON bên trong cột 'partner'.

-- Sale Order 1: Công ty TNHH Thép Hình Tú Hà - Dự án Tân Tạo
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW() - INTERVAL '5 day', 'System', NOW() - INTERVAL '5 day', 'System', 1, 'EXP20250610AABBCCDD01',
     '{
       "id": 22, "partnerType": "CUSTOMER", "partnerName": "Công ty TNHH Thép Hình Tú Hà", "partnerCode": "PTN202505146BYQ1YUGYMMMM", "taxCode": "0100100022",
       "legalRepresentative": "Giang Tuấn Tú", "legalRepresentativePhone": "0908100022", "contactPerson": "Châu Ngọc Hà", "contactPersonPhone": "0918100022",
       "bankName": "Agribank", "bankAccountNumber": "9704050010022", "debtPayable": 0.00, "debtReceivable": 0.00,
       "warehouses": [{"id": 22, "warehouseName": "Kho Thép Hình Tú Hà", "warehouseCode": "WH20250514AXYZ12366VVM", "warehouseAddress": "Lô H3, KCN Sóng Thần 3, TP. Thủ Dầu Một, Bình Dương", "partnerId": 22}],
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 1, "partnerId": 22, "projectCode": "PRJ202505148K0QEYBC7KA1", "projectName": "Cung cấp thép kết cấu cho Nhà xưởng Tân Tạo Mở Rộng (Phase 1)",
       "projectAddress": "Lô A1-1, KCN Tân Tạo, Q. Bình Tân, TP.HCM", "projectRepresentative": "Trần Văn Mạnh", "projectRepresentativePhone": "0912345678",
       "contactPerson": "Lê Thị Hoa", "contactPersonPhone": "0987654321", "address": "10 Lý Tự Trọng, P. Bến Nghé, Q.1, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'DONE', 125500000.00, 'Đã hoàn thành giao hàng và thanh toán.', false);

-- Sale Order 2: Doanh nghiệp Xây Lắp Việt Tùng - Dự án Trung tâm CNC
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW() - INTERVAL '4 day', 'System', NOW() - INTERVAL '3 day', 'System', 1, 'EXP20250610AABBCCDD02',
     '{
       "id": 5, "partnerType": "CUSTOMER", "partnerName": "Doanh nghiệp Xây Lắp Việt Tùng", "partnerCode": "PTN202505146BYQ1WVKLEE5", "taxCode": "0100100005",
       "legalRepresentative": "Trịnh Quốc Việt", "legalRepresentativePhone": "0908100005", "contactPerson": "Ngô Thanh Tùng", "contactPersonPhone": "0918100005",
       "bankName": "Techcombank", "bankAccountNumber": "9704360010005", "debtPayable": 0.00, "debtReceivable": 0.00,
       "warehouses": [{"id": 5, "warehouseName": "Kho Vật tư Xây lắp Việt Tùng", "warehouseCode": "WH20250514AXYZ12349EE5", "warehouseAddress": "Đường N2, KCN Mỹ Phước 2, TX. Bến Cát, Bình Dương", "partnerId": 5}],
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 2, "partnerId": 5, "projectCode": "PRJ202505148K0QEYGNL8B2", "projectName": "Phân phối thép chuyên dụng cho Trung tâm Nghiên cứu CNC",
       "projectAddress": "Đường D1, Khu công nghệ cao, Q.9, TP. Thủ Đức", "projectRepresentative": "Nguyễn Thị Lan", "projectRepresentativePhone": "0912345679",
       "contactPerson": "Phạm Minh Tuấn", "contactPersonPhone": "0987654322", "address": "25 Pasteur, P. Nguyễn Thái Bình, Q.1, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'PROCESSING', 350200000.00, 'Đang xử lý xuất kho, dự kiến giao hàng trong 2 ngày.', false);

-- Sale Order 3: Công ty Hợp Tác Đầu Tư Xây Dựng Khiêm Lam - Dự án nhà máy Amata
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW() - INTERVAL '3 day', 'System', NOW() - INTERVAL '3 day', 'System', 1, 'EXP20250610AABBCCDD03',
     '{
       "id": 41, "partnerType": "CUSTOMER", "partnerName": "Công ty Hợp Tác Đầu Tư Xây Dựng Khiêm Lam", "partnerCode": "PTN202505146BYQ21SFTC15", "taxCode": "0300100041",
       "legalRepresentative": "Tăng Gia Khiêm", "legalRepresentativePhone": "0908100041", "contactPerson": "Mạc Tường Lam", "contactPersonPhone": "0918100041",
       "bankName": "Vietcombank", "bankAccountNumber": "9704180010041", "debtPayable": 0.00, "debtReceivable": 0.00,
       "warehouses": [{"id": 41, "warehouseName": "Kho Đầu Tư Xây Dựng Khiêm Lam", "warehouseCode": "WH20250514BXYZ12385OOF5", "warehouseAddress": "Đường số 2, KCN Vĩnh Lộc, H. Bình Chánh, TP.HCM", "partnerId": 41}],
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 4, "partnerId": 41, "projectCode": "PRJ202505148K0QEYQZ74D4", "projectName": "Quản lý vật tư thép Nhà máy Sản xuất Linh kiện Amata",
       "projectAddress": "Lô B2, KCN Amata, TP. Biên Hòa, Đồng Nai", "projectRepresentative": "Đặng Minh Khoa", "projectRepresentativePhone": "0912345681",
       "contactPerson": "Vũ Ngọc Anh", "contactPersonPhone": "0987654324", "address": "112 Hàm Nghi, P. Bến Thành, Q.1, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'CANCELLED', 410000000.00, 'Khách hàng hủy đơn do thay đổi thiết kế.', true);

-- Sale Order 4: Tập đoàn Xây Dựng Việt Phát - Dự án Cảng Cát Lái
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW() - INTERVAL '2 day', 'System', NOW() - INTERVAL '1 day', 'System', 1, 'EXP20250610AABBCCDD04',
     '{
       "id": 2, "partnerType": "CUSTOMER", "partnerName": "Tập đoàn Xây Dựng Việt Phát", "partnerCode": "PTN202505146BYQ1WGNL8B2", "taxCode": "0100100002",
       "legalRepresentative": "Phạm Minh Đức", "legalRepresentativePhone": "0918100002", "contactPerson": "Lê Văn Thành", "contactPersonPhone": "0908100002",
       "bankName": "Agribank", "bankAccountNumber": "9704050010002", "debtPayable": 0.00, "debtReceivable": 0.00,
       "warehouses": [{"id": 2, "warehouseName": "Kho Vật tư Việt Phát (Biên Hòa)", "warehouseCode": "WH20250514AXYZ12346BB2", "warehouseAddress": "Đường số 6, KCN Biên Hòa 2, TP. Biên Hòa, Đồng Nai", "partnerId": 2}],
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 6, "partnerId": 2, "projectCode": "PRJ202505148K0QEZ0R00F6", "projectName": "Cung cấp thép tấm cho Công trình Nâng cấp Cảng Cát Lái",
       "projectAddress": "Cảng Cát Lái, Đường Nguyễn Thị Định, TP. Thủ Đức", "projectRepresentative": "Đỗ Hoàng Nam", "projectRepresentativePhone": "0912345683",
       "contactPerson": "Ngô Thị Mai", "contactPersonPhone": "0987654326", "address": "30 Tôn Đức Thắng, P. Bến Nghé, Q.1, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'DELIVERED', 88750000.00, 'Đã giao hàng thành công, chờ đối soát công nợ.', false);

-- Sale Order 5: Công ty Thương Mại Kết Cấu Thép Công Ánh - Dự án Phú Mỹ Hưng
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 'EXP20250610AABBCCDD05',
     '{
       "id": 8, "partnerType": "CUSTOMER", "partnerName": "Công ty Thương Mại Kết Cấu Thép Công Ánh", "partnerCode": "PTN202505146BYQ1XAX24H8", "taxCode": "0400100008",
       "legalRepresentative": "Lý Thành Công", "legalRepresentativePhone": "0908100008", "contactPerson": "Đinh Ngọc Ánh", "contactPersonPhone": "0918100008",
       "bankName": "VietinBank", "bankAccountNumber": "9704150010008", "debtPayable": 0.00, "debtReceivable": 0.00,
       "warehouses": [{"id": 8, "warehouseName": "Kho Kết Cấu Thép Công Ánh (An Phú)", "warehouseCode": "WH20250514AXYZ12352HH8", "warehouseAddress": "156 Xa lộ Hà Nội, P. An Phú, TP. Thủ Đức, TP.HCM", "partnerId": 8}],
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 8, "partnerId": 8, "projectCode": "PRJ202505148K0QEZAX24H8", "projectName": "Quản lý phân phối thép cho Dự án Cao ốc Crescent Hub",
       "projectAddress": "Khu đô thị Phú Mỹ Hưng, Q.7, TP.HCM", "projectRepresentative": "Lý Ngọc Hà", "projectRepresentativePhone": "0912345685",
       "contactPerson": "Châu Minh Đức", "contactPersonPhone": "0987654328", "address": "45 Hai Bà Trưng, P. Bến Nghé, Q.1, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'NEW', 560000000.00, 'Đơn hàng mới, yêu cầu báo giá gấp.', false);

-- Sale Order 6: Đối tác Xây dựng Dân Dụng Long Nhi - Dự án Sala
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW() - INTERVAL '10 day', 'System', NOW() - INTERVAL '2 day', 'System', 1, 'EXP20250610AABBCCDD06',
     '{
       "id": 11, "partnerType": "CUSTOMER", "partnerName": "Đối tác Xây dựng Dân Dụng Long Nhi", "partnerCode": "PTN202505146BYQ1XQSI8KB", "taxCode": "0300100011",
       "legalRepresentative": "Tăng Bảo Long", "legalRepresentativePhone": "0908100011", "contactPerson": "Mạc Yến Nhi", "contactPersonPhone": "0918100011",
       "bankName": "Vietcombank", "bankAccountNumber": "9704180010011", "debtPayable": 0.00, "debtReceivable": 0.00,
       "warehouses": [{"id": 11, "warehouseName": "Kho Vật liệu Xây dựng Long Nhi (Quận 8)", "warehouseCode": "WH20250514AXYZ12355KKB", "warehouseAddress": "Đường Tạ Quang Bửu, P.5, Q.8, TP.HCM", "partnerId": 11}],
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 11, "partnerId": 11, "projectCode": "PRJ202505148K0QEZQSI8KB", "projectName": "Cung cấp thép xây dựng Trung tâm Thương mại Sala Center",
       "projectAddress": "Khu đô thị Sala, TP. Thủ Đức", "projectRepresentative": "Tăng Minh Khang", "projectRepresentativePhone": "0912345688",
       "contactPerson": "Lương Thị Hồng", "contactPersonPhone": "0987654331", "address": "123 Võ Văn Tần, P.6, Q.3, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'IN_TRANSIT', 220450000.00, 'Xe BKS 51C-10001 đang vận chuyển, dự kiến tới nơi lúc 16:00.', false);

-- Sale Order 7: Công ty Vận Tải Sắt Thép Nam Bắc - Dự án Sunshine Tower
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW() - INTERVAL '20 day', 'System', NOW() - INTERVAL '15 day', 'System', 1, 'EXP20250610AABBCCDD07',
     '{
       "id": 3, "partnerType": "CUSTOMER", "partnerName": "Công ty Vận Tải Sắt Thép Nam Bắc", "partnerCode": "PTN202505146BYQ1WLTTGC3", "taxCode": "0400100003",
       "legalRepresentative": "Vũ Ngọc Khánh", "legalRepresentativePhone": "0918100003", "contactPerson": "Hoàng Gia Bảo", "contactPersonPhone": "0908100003",
       "bankName": "VietinBank", "bankAccountNumber": "9704150010003", "debtPayable": 0.00, "debtReceivable": 0.00,
       "warehouses": [
         {"id": 3, "warehouseName": "Kho bãi Huy Lan Express (Cát Lái)", "warehouseCode": "WH20250514AXYZ12347CC3", "warehouseAddress": "Bãi xe số 3, Cảng Cát Lái, TP. Thủ Đức, TP.HCM", "partnerId": 3},
         {"id": 99, "warehouseName": "Kho Giao Hàng Kiên Liên (Phú Mỹ)", "warehouseCode": "WH20250514BXYZ12343UULBR", "warehouseAddress": "Khu vực cầu Phú Mỹ, Q.7, TP.HCM", "partnerId": 99}
       ],
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 20, "partnerId": 3, "projectCode": "PRJ202505148K0QF0K04CKK", "projectName": "Cung cấp thép hình cho Tòa nhà Văn phòng Sunshine Tower",
       "projectAddress": "Khu đô thị Ciputra, Q. Tây Hồ, Hà Nội", "projectRepresentative": "Trịnh Văn An", "projectRepresentativePhone": "0912345697",
       "contactPerson": "Vũ Minh Hiếu", "contactPersonPhone": "0987654340", "address": "205 Nguyễn Văn Trỗi, P.10, Q. Phú Nhuận, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'DONE', 95000000.00, 'Đơn hàng nhỏ, đã hoàn tất.', false);

-- Sale Order 8: Đối Tác Chiến Lược Nam Lan Construction - Dự án Vinmec
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 'EXP20250610AABBCCDD08',
     '{
       "id": 77, "partnerType": "CUSTOMER", "partnerName": "Đối Tác Chiến Lược Nam Lan Construction", "partnerCode": "PTN202505146BYQ27HZU425", "taxCode": "0100100077",
       "legalRepresentative": "Lê Đình Nam", "legalRepresentativePhone": "0908100077", "contactPerson": "Phạm Tuyết Lan", "contactPersonPhone": "0918100077",
       "bankName": "Agribank", "bankAccountNumber": "9704050010077", "debtPayable": 0.00, "debtReceivable": 0.00,
       "warehouses": [{"id": 77, "warehouseName": "Kho Nam Lan Construction (Phú Hữu)", "warehouseCode": "WH20250514BXYZ12321YYPF5", "warehouseAddress": "Đường Nguyễn Duy Trinh, P. Phú Hữu, TP. Thủ Đức, TP.HCM", "partnerId": 77}],
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 91, "partnerId": 77, "projectCode": "PRJ202505148K0QFCDECDG2N", "projectName": "Phân phối thép xây dựng Bệnh viện Vinmec Times City",
       "projectAddress": "Khu đô thị Time City, Q. Hai Bà Trưng, Hà Nội", "projectRepresentative": "Lê Duy Mạnh", "projectRepresentativePhone": "0912345754",
       "contactPerson": "Lê Bảo Ngọc", "contactPersonPhone": "0987654415", "address": "101 Phùng Hưng, P.14, Q.5, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'NEW', 1800000000.00, 'Đơn hàng lớn, cần xác nhận khả năng cung ứng.', false);

-- Sale Order 9: Công ty Thép Khang Vân - Dự án Cầu Vĩnh Tuy
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW() - INTERVAL '8 day', 'System', NOW() - INTERVAL '1 day', 'System', 1, 'EXP20250610AABBCCDD09',
     '{
       "id": 100, "partnerType": "CUSTOMER", "partnerName": "Công ty Thép Khang Vân", "partnerCode": "PTN202505146BYQ2B731U2S", "taxCode": "0100100100",
       "legalRepresentative": "Quách Tuấn Khang", "legalRepresentativePhone": "0908100100", "contactPerson": "Lương Hồng Vân", "contactPersonPhone": "0918100100",
       "bankName": "Techcombank", "bankAccountNumber": "9704360010100", "debtPayable": 0.00, "debtReceivable": 0.00,
       "warehouses": [{"id": 100, "warehouseName": "Kho Thép Khang Vân (Biên Hòa)", "warehouseCode": "WH20250514BXYZ12344VVMCS", "warehouseAddress": "Lô L42, KCN Biên Hòa 2, TP. Biên Hòa, Đồng Nai", "partnerId": 100}],
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 92, "partnerId": 100, "projectCode": "PRJ202505148K0QF9PBFQ26", "projectName": "Cung cấp thép dầm cho Cầu Vĩnh Tuy - Giai đoạn 2",
       "projectAddress": "Đường Vành Đai 2 trên cao, đoạn Vĩnh Tuy - Ngã Tư Sở", "projectRepresentative": "Hoàng Tấn Phát", "projectRepresentativePhone": "0912345755",
       "contactPerson": "Châu Phương Thảo", "contactPersonPhone": "0987654398", "address": "70 Lãnh Binh Thăng, P.13, Q.11, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'DELIVERED', 495800000.00, 'Đã giao 2/3 lô hàng. Lô cuối đang trên đường.', false);

-- Sale Order 10: Đối Tác Xây Dựng Khánh Dương - Dự án Aeon Mall Hà Đông
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW() - INTERVAL '1 day', 'System', NOW(), 'System', 1, 'EXP20250610AABBCCDD10',
     '{
       "id": 50, "partnerType": "CUSTOMER", "partnerName": "Đối Tác Xây Dựng Khánh Dương", "partnerCode": "PTN202505146BYQ2379201E", "taxCode": "0100100050",
       "legalRepresentative": "Trịnh Duy Khánh", "legalRepresentativePhone": "0908100050", "contactPerson": "Ngô Thùy Dương", "contactPersonPhone": "0918100050",
       "bankName": "Techcombank", "bankAccountNumber": "9704360010050", "debtPayable": 0.00, "debtReceivable": 0.00,
       "warehouses": [{"id": 50, "warehouseName": "Kho Xây Dựng Khánh Dương (Bình Tân)", "warehouseCode": "WH20250514BXYZ12394XXOE", "warehouseAddress": "Đường Võ Văn Kiệt, P. An Lạc, Q. Bình Tân, TP.HCM", "partnerId": 50}],
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 100, "partnerId": 50, "projectCode": "PRJ202505148K0QFBFBJS2H", "projectName": "Quản lý phân phối thép Trung tâm Thương mại Aeon Mall Hà Đông",
       "projectAddress": "Khu đô thị Dương Nội, Q. Hà Đông, Hà Nội", "projectRepresentative": "Xa Anh Dũng", "projectRepresentativePhone": "0912345766",
       "contactPerson": "Vương Ngọc Hà", "contactPersonPhone": "0987654409", "address": "25 Thuận Kiều, P.12, Q.5, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'PROCESSING', 782150000.00, 'Đã nhận cọc. Chuẩn bị hàng theo yêu cầu.', false);