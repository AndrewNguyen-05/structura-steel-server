INSERT INTO products
(created_at, created_by, updated_at, updated_by, version,
 code, name, product_type, standard, unit_weight, length,
 diameter, width, height, thickness,
 import_price, profit_percentage, export_price, deleted)
VALUES
    -------------------------------------------------------------------------------
    -- NHÓM 1: THÉP VẰN (RIBBED_BAR) - THEO TCVN 1651-2:2018
    -- Ghi chú: unit_weight là kg/cây 11.7m. Length tính bằng mm.
    -------------------------------------------------------------------------------
    ('2025-07-07 08:00:00+00','System','2025-07-07 08:00:00+00','System',1,
     'PRD20250707RBAR10D','Thép vằn D10','RIBBED_BAR','TCVN 1651-2:2018',7.21,11700,
     10,NULL,NULL,NULL,
     108150, 10, 118965, FALSE),
    ('2025-07-07 08:10:00+00','System','2025-07-07 08:10:00+00','System',1,
     'PRD20250707RBAR12D','Thép vằn D12','RIBBED_BAR','TCVN 1651-2:2018',10.38,11700,
     12,NULL,NULL,NULL,
     155700, 12, 174384, FALSE),
    ('2025-07-07 08:20:00+00','System','2025-07-07 08:20:00+00','System',1,
     'PRD20250707RBAR14D','Thép vằn D14','RIBBED_BAR','TCVN 1651-2:2018',14.13,11700,
     14,NULL,NULL,NULL,
     211950, 14, 241623, FALSE),
    ('2025-07-07 08:30:00+00','System','2025-07-07 08:30:00+00','System',1,
     'PRD20250707RBAR16D','Thép vằn D16','RIBBED_BAR','TCVN 1651-2:2018',18.46,11700,
     16,NULL,NULL,NULL,
     276900, 15, 318435, FALSE),
    ('2025-07-07 08:40:00+00','System','2025-07-07 08:40:00+00','System',1,
     'PRD20250707RBAR18D','Thép vằn D18','RIBBED_BAR','TCVN 1651-2:2018',23.37,11700,
     18,NULL,NULL,NULL,
     350550, 16, 406638, FALSE),

    -------------------------------------------------------------------------------
    -- NHÓM 2: THÉP TẤM (PLATE) - THEO JIS G3101
    -- Ghi chú: unit_weight là kg/m. Length tính bằng mm.
    -------------------------------------------------------------------------------
    ('2025-07-07 10:00:00+00','System','2025-07-07 10:00:00+00','System',1,
     'PRD20250707PLAT10T','Thép tấm 10ly x 3m','PLATE','JIS G3101',235.5,1000,
     NULL,3000,NULL,10,
     3532500, 8, 3815100, FALSE),
    ('2025-07-07 10:10:00+00','System','2025-07-07 10:10:00+00','System',1,
     'PRD20250707PLAT12T','Thép tấm 12ly x 3m','PLATE','JIS G3101',282.6,1000,
     NULL,3000,NULL,12,
     4239000, 9, 4620510, FALSE),
    ('2025-07-07 10:20:00+00','System','2025-07-07 10:20:00+00','System',1,
     'PRD20250707PLAT14T','Thép tấm 14ly x 3m','PLATE','JIS G3101',329.7,1000,
     NULL,3000,NULL,14,
     4945500, 10, 5440050, FALSE),

    -------------------------------------------------------------------------------
    -- NHÓM 3: THÉP HỘP VUÔNG (BOX) - THEO JIS G3466
    -- Ghi chú: unit_weight là kg/m. Length tính bằng mm.
    -------------------------------------------------------------------------------
    ('2025-07-07 13:00:00+00','System','2025-07-07 13:00:00+00','System',1,
     'PRD20250707BOX10010','Thép hộp vuông 100x100x10','BOX','JIS G3466',29.29,1000,
     NULL,100,100,10,
     439350, 11, 487679, FALSE),
    ('2025-07-07 13:10:00+00','System','2025-07-07 13:10:00+00','System',1,
     'PRD20250707BOX15010','Thép hộp vuông 150x150x10','BOX','JIS G3466',45.09,1000,
     NULL,150,150,10,
     676350, 12, 757512, FALSE),
    ('2025-07-07 13:20:00+00','System','2025-07-07 13:20:00+00','System',1,
     'PRD20250707BOX20010','Thép hộp vuông 200x200x10','BOX','JIS G3466',61.02,1000,
     NULL,200,200,10,
     915300, 13, 1034289, FALSE),

    -------------------------------------------------------------------------------
    -- NHÓM 4: THÉP HÌNH I (SHAPED) - THEO JIS G3192
    -- Ghi chú: unit_weight là kg/m. Length tính bằng mm.
    -------------------------------------------------------------------------------
    ('2025-07-07 14:30:00+00','System','2025-07-07 14:30:00+00','System',1,
     'PRD20250707SHPI150','Thép hình I 150x75x5','SHAPED','JIS G3192',14.0,1000,
     NULL,NULL,NULL,NULL,
     210000, 9, 228900, FALSE),
    ('2025-07-07 14:40:00+00','System','2025-07-07 14:40:00+00','System',1,
     'PRD20250707SHPI200','Thép hình I 200x100x5.5','SHAPED','JIS G3192',21.3,1000,
     NULL,NULL,NULL,NULL,
     319500, 10, 351450, FALSE),
    ('2025-07-07 14:50:00+00','System','2025-07-07 14:50:00+00','System',1,
     'PRD20250707SHPI300','Thép hình I 300x150x6.5','SHAPED','JIS G3192',36.7,1000,
     NULL,NULL,NULL,NULL,
     550500, 11, 611055, FALSE)
;