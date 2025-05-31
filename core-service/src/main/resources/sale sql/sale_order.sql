-- Sale Order 1 (Original partner_id: 22, project_id: 1, warehouse_id: 1)
-- Partner 22 là "Công ty TNHH Thép Hình Tú Hà" (SUPPLIER). Điều chỉnh thành CUSTOMER cho SaleOrder.
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, warehouse, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 'EXP20250407BKLM1NOPQRA1',
     '{
       "id": 22, "partnerType": "CUSTOMER", "partnerName": "Công ty TNHH Thép Hình Tú Hà", "partnerCode": "PTN202505146BYQ1YUGYMMMM", "taxCode": "0100100022",
       "legalRepresentative": "Giang Tuấn Tú", "legalRepresentativePhone": "0908100022", "contactPerson": "Châu Ngọc Hà", "contactPersonPhone": "0918100022",
       "bankName": "Agribank", "bankAccountNumber": "9704050010022",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 1, "partnerId": 22, "projectCode": "PRJ202505148K0QEYBC7KA1", "projectName": "Cung cấp thép kết cấu cho Nhà xưởng Tân Tạo Mở Rộng (Phase 1)",
       "projectAddress": "Lô A1-1, KCN Tân Tạo, Q. Bình Tân, TP.HCM", "projectRepresentative": "Trần Văn Mạnh", "projectRepresentativePhone": "0912345678",
       "contactPerson": "Lê Thị Hoa", "contactPersonPhone": "0987654321", "address": "10 Lý Tự Trọng, P. Bến Nghé, Q.1, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 1, "warehouseName": "Kho Thép Hòa Hưng (Bình Dương)", "warehouseCode": "WH20250514AXYZ12345AA1",
       "warehouseAddress": "Lô A5, KCN Sóng Thần 1, TP. Dĩ An, Bình Dương", "partnerId": 1,
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'NEW', 10000.00, 'Đơn hàng số 1', false);

-- Sale Order 2 (Original partner_id: 5, project_id: 2, warehouse_id: 2)
-- Partner 5 là "Doanh nghiệp Xây Lắp Việt Tùng" (CUSTOMER).
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, warehouse, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 'EXP20250407BKLM2NOPQRB2',
     '{
       "id": 5, "partnerType": "CUSTOMER", "partnerName": "Doanh nghiệp Xây Lắp Việt Tùng", "partnerCode": "PTN202505146BYQ1WVKLEE5", "taxCode": "0100100005",
       "legalRepresentative": "Trịnh Quốc Việt", "legalRepresentativePhone": "0908100005", "contactPerson": "Ngô Thanh Tùng", "contactPersonPhone": "0918100005",
       "bankName": "Techcombank", "bankAccountNumber": "9704360010005",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 2, "partnerId": 5, "projectCode": "PRJ202505148K0QEYGNL8B2", "projectName": "Phân phối thép chuyên dụng cho Trung tâm Nghiên cứu CNC",
       "projectAddress": "Đường D1, Khu công nghệ cao, Q.9, TP. Thủ Đức", "projectRepresentative": "Nguyễn Thị Lan", "projectRepresentativePhone": "0912345679",
       "contactPerson": "Phạm Minh Tuấn", "contactPersonPhone": "0987654322", "address": "25 Pasteur, P. Nguyễn Thái Bình, Q.1, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 2, "warehouseName": "Kho Vật tư Việt Phát (Biên Hòa)", "warehouseCode": "WH20250514AXYZ12346BB2",
       "warehouseAddress": "Đường số 6, KCN Biên Hòa 2, TP. Biên Hòa, Đồng Nai", "partnerId": 2,
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'PROCESSING', 20000.00, 'Đơn hàng số 2', false);

-- Sale Order 3 (Original partner_id: 78, project_id: 3, warehouse_id: 3)
-- Partner 78 là "Dịch Vụ Xe Tải Lợi Nhung" (TRANSPORTER). Điều chỉnh thành CUSTOMER.
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, warehouse, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 'EXP20250407BKLM3NOPQCC3',
     '{
       "id": 78, "partnerType": "CUSTOMER", "partnerName": "Dịch Vụ Xe Tải Lợi Nhung", "partnerCode": "PTN202505146BYQ27PBFQ26", "taxCode": "0400100078",
       "legalRepresentative": "Hoàng Văn Lợi", "legalRepresentativePhone": "0908100078", "contactPerson": "Vũ Cẩm Nhung", "contactPersonPhone": "0918100078",
       "bankName": "VietinBank", "bankAccountNumber": "9704150010078",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 3, "partnerId": 78, "projectCode": "PRJ202505148K0QEYLTTGC3", "projectName": "Cung cấp thép xây dựng Kho vận Long Bình",
       "projectAddress": "Km 15, Xa lộ Hà Nội, TP. Biên Hòa, Đồng Nai", "projectRepresentative": "Vũ Đức Hải", "projectRepresentativePhone": "0912345680",
       "contactPerson": "Hoàng Thu Trang", "contactPersonPhone": "0987654323", "address": "50 Nguyễn Huệ, P. Bến Nghé, Q.1, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 3, "warehouseName": "Kho bãi Huy Lan Express (Cát Lái)", "warehouseCode": "WH20250514AXYZ12347CC3",
       "warehouseAddress": "Bãi xe số 3, Cảng Cát Lái, TP. Thủ Đức, TP.HCM", "partnerId": 3,
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'DONE', 30000.00, 'Đơn hàng số 3', false);

-- Sale Order 4 (Original partner_id: 41, project_id: 4, warehouse_id: 4)
-- Partner 41 là "Công ty Hợp Tác Đầu Tư Xây Dựng Khiêm Lam" (CUSTOMER).
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, warehouse, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 'EXP20250407BKLM4NOPQDD4',
     '{
       "id": 41, "partnerType": "CUSTOMER", "partnerName": "Công ty Hợp Tác Đầu Tư Xây Dựng Khiêm Lam", "partnerCode": "PTN202505146BYQ21SFTC15", "taxCode": "0300100041",
       "legalRepresentative": "Tăng Gia Khiêm", "legalRepresentativePhone": "0908100041", "contactPerson": "Mạc Tường Lam", "contactPersonPhone": "0918100041",
       "bankName": "Vietcombank", "bankAccountNumber": "9704180010041",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 4, "partnerId": 41, "projectCode": "PRJ202505148K0QEYQZ74D4", "projectName": "Quản lý vật tư thép Nhà máy Sản xuất Linh kiện Amata",
       "projectAddress": "Lô B2, KCN Amata, TP. Biên Hòa, Đồng Nai", "projectRepresentative": "Đặng Minh Khoa", "projectRepresentativePhone": "0912345681",
       "contactPerson": "Vũ Ngọc Anh", "contactPersonPhone": "0987654324", "address": "112 Hàm Nghi, P. Bến Thành, Q.1, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 4, "warehouseName": "Kho Thép Miền Trung (Đà Nẵng)", "warehouseCode": "WH20250514AXYZ12348DD4",
       "warehouseAddress": "Lô C10, KCN Hòa Khánh, Q. Liên Chiểu, Đà Nẵng", "partnerId": 4,
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'CANCELED', 40000.00, 'Đơn hàng số 4', false);

-- Sale Order 5 (Original partner_id: 99, project_id: 5, warehouse_id: 5)
-- Partner 99 là "Dịch vụ Giao Hàng Nhanh Kiên Liên" (TRANSPORTER). Điều chỉnh thành CUSTOMER.
INSERT INTO sale_orders (created_at, created_by, updated_at, updated_by, version, export_code, partner, project, warehouse, status, total_amount, sale_orders_note, deleted) VALUES
    (NOW(), 'System', NOW(), 'System', 1, 'EXP20250407BKLM5NOPQEE5',
     '{
       "id": 99, "partnerType": "CUSTOMER", "partnerName": "Dịch vụ Giao Hàng Nhanh Kiên Liên", "partnerCode": "PTN202505146BYQ2B1IFQ2R", "taxCode": "0500100099",
       "legalRepresentative": "Phan Trung Kiên", "legalRepresentativePhone": "0908100099", "contactPerson": "Kiều Bích Liên", "contactPersonPhone": "0918100099",
       "bankName": "BIDV", "bankAccountNumber": "9704220010099",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 5, "partnerId": 99, "projectCode": "PRJ202505148K0QEYVKLEE5", "projectName": "Phân phối thép cho Dự án Mở rộng KCN VSIP 1 - Giai đoạn 2",
       "projectAddress": "Đường số 8, KCN VSIP 1, TP. Thuận An, Bình Dương", "projectRepresentative": "Trịnh Thanh Tâm", "projectRepresentativePhone": "0912345682",
       "contactPerson": "Đặng Văn Bình", "contactPersonPhone": "0987654325", "address": "88 Đồng Khởi, P. Bến Nghé, Q.1, TP.HCM",
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     '{
       "id": 5, "warehouseName": "Kho Vật tư Xây lắp Việt Tùng", "warehouseCode": "WH20250514AXYZ12349EE5",
       "warehouseAddress": "Đường N2, KCN Mỹ Phước 2, TX. Bến Cát, Bình Dương", "partnerId": 5,
       "version": 0, "createdAt": "2025-05-14T10:00:00Z", "updatedAt": "2025-05-14T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb,
     'NEW', 50000.00, 'Đơn hàng số 5', false);