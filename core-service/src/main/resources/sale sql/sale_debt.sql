-- Debt for SO_ID=1, Product_ID=1 (Product có ID=1 là "Thép vằn phi 100")
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-04-08T08:00:00Z', 'System', '2025-04-08T08:00:00Z', 'System', 1, 5000.00, 5000.00, 'Partial payment due for order 1',
     '{
       "id": 1, "code": "PRD202503225EWPERSI4A1B", "name": "Thép vằn phi 100", "unitWeight": 61.7, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 100, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'UNPAID', 1);

-- Debt for SO_ID=2, Product_ID=2 (Product có ID=2 là "Thép vằn phi 105")
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-04-08T08:05:00Z', 'System', '2025-04-08T08:05:00Z', 'System', 1, 20000.00, 0.00, 'Full payment received for order 2',
     '{
       "id": 2, "code": "PRD202503225EWPERXHSJC2", "name": "Thép vằn phi 105", "unitWeight": 68.0, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 105, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'PAID', 2);

-- Debt for SO_ID=3, Product_ID=3 (Product có ID=3 là "Thép vằn phi 110")
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-04-08T08:10:00Z', 'System', '2025-04-08T08:10:00Z', 'System', 1, 15000.00, 15000.00, 'Partial debt for order 3',
     '{
       "id": 3, "code": "PRD202503225EWPES383KD3", "name": "Thép vằn phi 110", "unitWeight": 74.6, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 110, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'UNPAID', 3);

-- Debt for SO_ID=4, Product_ID=4 (Product có ID=4 là "Thép vằn phi 115")
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-04-08T08:15:00Z', 'System', '2025-04-08T08:15:00Z', 'System', 1, 0.00, 0.00, 'Order 4 cancelled, no debt',
     '{
       "id": 4, "code": "PRD202503225EWPES8HPRE4", "name": "Thép vằn phi 115", "unitWeight": 81.5, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 115, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'CANCELLED', 4);

-- Debt for SO_ID=5, Product_ID=5 (Product có ID=5 là "Thép vằn phi 120")
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-04-08T08:20:00Z', 'System', '2025-04-08T08:20:00Z', 'System', 1, 50000.00, 50000.00, 'Full amount pending for order 5',
     '{
       "id": 5, "code": "PRD202503225EWPESE2B4F5", "name": "Thép vằn phi 120", "unitWeight": 88.8, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 120, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'UNPAID', 5);

-- Debt for SO_ID=6, Product_ID=6 (Product có ID=6 là "Thép vằn phi 125")
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-04-08T08:25:00Z', 'System', '2025-04-08T08:25:00Z', 'System', 1, 30000.00, 30000.00, 'Partial payment, 50% due for order 6',
     '{
       "id": 6, "code": "PRD202503225EWPESJMQNG6", "name": "Thép vằn phi 125", "unitWeight": 96.4, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 125, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'UNPAID', 6);

-- Debt for SO_ID=7, Product_ID=7 (Product có ID=7 là "Thép vằn phi 130")
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-04-08T08:30:00Z', 'System', '2025-04-08T08:30:00Z', 'System', 1, 70000.00, 0.00, 'Full payment received for order 7, paid late',
     '{
       "id": 7, "code": "PRD202503225EWPESP72RH7", "name": "Thép vằn phi 130", "unitWeight": 104.2, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 130, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'PAID', 7);

-- Debt for SO_ID=8, Product_ID=8 (Product có ID=8 là "Thép vằn phi 135")
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-04-08T08:35:00Z', 'System', '2025-04-08T08:35:00Z', 'System', 1, 0.00, 0.00, 'Cancelled order, no debt for order 8',
     '{
       "id": 8, "code": "PRD202503225EWPESUMMUI8", "name": "Thép vằn phi 135", "unitWeight": 112.3, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 135, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'CANCELLED', 8);

-- Debt for SO_ID=9, Product_ID=9 (Product có ID=9 là "Thép vằn phi 140")
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-04-08T08:40:00Z', 'System', '2025-04-08T08:40:00Z', 'System', 1, 45000.00, 45000.00, 'Partial payment, remaining due for order 9',
     '{
       "id": 9, "code": "PRD202503225EWPET0H88J9", "name": "Thép vằn phi 140", "unitWeight": 120.9, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 140, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'UNPAID', 9);

-- Debt for SO_ID=10, Product_ID=10 (Product có ID=10 là "Thép vằn phi 145")
INSERT INTO sale_debts (created_at, created_by, updated_at, updated_by, version, original_amount, remaining_amount, debt_note, product, status, sale_order_id) VALUES
    ('2025-04-08T08:45:00Z', 'System', '2025-04-08T08:45:00Z', 'System', 1, 100000.00, 0.00, 'Full payment received for order 10',
     '{
       "id": 10, "code": "PRD202503225EWPET61T6KA", "name": "Thép vằn phi 145", "unitWeight": 129.7, "productType": "RIBBED_BAR",
       "length": 100, "width": null, "height": null, "thickness": null, "diameter": 145, "standard": "Big Rebar",
       "version": 1, "createdAt": "2025-03-22T10:00:00Z", "updatedAt": "2025-03-22T10:00:00Z", "createdBy": "System", "updatedBy": "System"
     }'::jsonb, 'PAID', 10);