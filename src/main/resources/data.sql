-- Sample Rules Data
INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'Channel',
    'Amazon discount',
    'Apply 10% discount for Amazon orders over $100',
    10,
    '{"operator": "AND", "conditions": [{"field": "channel", "operator": "equals", "value": "amazon"}, {"field": "order_total", "operator": "greater_than", "value": 100}]}',
    '{"actions": [{"type": "discount", "value": 10, "description": "Apply 10% discount"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'WMS',
    'Low stock reorder',
    'Create reorder when stock level is below threshold',
    20,
    '{"operator": "AND", "conditions": [{"field": "stock_level", "operator": "less_than", "value": 10}, {"field": "product_category", "operator": "in", "value": ["electronics", "clothing"]}]}',
    '{"actions": [{"type": "create_order", "value": 50, "description": "Create reorder for 50 units"}, {"type": "notification", "value": "low_stock_alert", "description": "Send low stock notification"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
); 