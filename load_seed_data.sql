-- Clear existing data
DELETE FROM rules;

-- OMS (Order Management System) Rules
INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OMS',
    'High value order processing',
    'Expedited processing for orders over $500',
    10,
    '{"operator": "AND", "conditions": [{"field": "order_total", "operator": "greater_than", "value": 500}, {"field": "order_status", "operator": "equals", "value": "pending"}]}',
    '{"actions": [{"type": "expedite_processing", "value": true, "description": "Expedite order processing"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OMS',
    'International order validation',
    'Additional validation for international orders',
    15,
    '{"operator": "AND", "conditions": [{"field": "shipping_country", "operator": "not_equals", "value": "US"}, {"field": "order_total", "operator": "greater_than", "value": 100}]}',
    '{"actions": [{"type": "additional_validation", "value": true, "description": "Apply additional validation"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OMS',
    'VIP customer priority',
    'Priority processing for VIP customers',
    20,
    '{"operator": "AND", "conditions": [{"field": "customer_tier", "operator": "equals", "value": "vip"}, {"field": "order_status", "operator": "equals", "value": "pending"}]}',
    '{"actions": [{"type": "priority_processing", "value": true, "description": "Set priority processing"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OMS',
    'Fraud detection alert',
    'Alert for suspicious order patterns',
    25,
    '{"operator": "AND", "conditions": [{"field": "order_total", "operator": "greater_than", "value": 1000}, {"field": "customer_age_days", "operator": "less_than", "value": 7}]}',
    '{"actions": [{"type": "fraud_alert", "value": true, "description": "Trigger fraud detection alert"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OMS',
    'Backorder notification',
    'Notify customer for backordered items',
    30,
    '{"operator": "AND", "conditions": [{"field": "item_availability", "operator": "equals", "value": "backorder"}, {"field": "customer_notification", "operator": "equals", "value": true}]}',
    '{"actions": [{"type": "backorder_notification", "value": true, "description": "Send backorder notification"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OMS',
    'Split order processing',
    'Split orders with mixed availability',
    35,
    '{"operator": "AND", "conditions": [{"field": "has_backorder_items", "operator": "equals", "value": true}, {"field": "has_in_stock_items", "operator": "equals", "value": true}]}',
    '{"actions": [{"type": "split_order", "value": true, "description": "Split order for partial fulfillment"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OMS',
    'Order cancellation validation',
    'Validate order cancellation requests',
    40,
    '{"operator": "AND", "conditions": [{"field": "order_status", "operator": "in", "value": ["pending", "processing"]}, {"field": "cancellation_reason", "operator": "not_equals", "value": null}]}',
    '{"actions": [{"type": "validate_cancellation", "value": true, "description": "Validate cancellation request"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- OXM (Order Experience Management) Rules
INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OXM',
    'Customer satisfaction survey',
    'Send satisfaction survey for completed orders',
    10,
    '{"operator": "AND", "conditions": [{"field": "order_status", "operator": "equals", "value": "delivered"}, {"field": "delivery_date", "operator": "greater_than", "value": "2024-01-01"}]}',
    '{"actions": [{"type": "send_survey", "value": "satisfaction", "description": "Send customer satisfaction survey"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OXM',
    'Delivery tracking notification',
    'Send tracking updates to customers',
    15,
    '{"operator": "AND", "conditions": [{"field": "order_status", "operator": "equals", "value": "shipped"}, {"field": "tracking_available", "operator": "equals", "value": true}]}',
    '{"actions": [{"type": "tracking_notification", "value": true, "description": "Send tracking notification"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OXM',
    'Return authorization',
    'Auto-authorize returns for eligible orders',
    20,
    '{"operator": "AND", "conditions": [{"field": "return_window_days", "operator": "less_than_or_equal", "value": 30}, {"field": "return_reason", "operator": "in", "value": ["defective", "wrong_item", "size_issue"]}]}',
    '{"actions": [{"type": "auto_authorize_return", "value": true, "description": "Auto-authorize return"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OXM',
    'Customer feedback analysis',
    'Analyze customer feedback for sentiment',
    25,
    '{"operator": "AND", "conditions": [{"field": "feedback_received", "operator": "equals", "value": true}, {"field": "feedback_type", "operator": "in", "value": ["review", "survey", "support_ticket"]}]}',
    '{"actions": [{"type": "sentiment_analysis", "value": true, "description": "Perform sentiment analysis"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OXM',
    'Loyalty points calculation',
    'Calculate loyalty points for completed orders',
    30,
    '{"operator": "AND", "conditions": [{"field": "order_status", "operator": "equals", "value": "delivered"}, {"field": "loyalty_program", "operator": "equals", "value": true}]}',
    '{"actions": [{"type": "calculate_loyalty_points", "value": true, "description": "Calculate and award loyalty points"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OXM',
    'Customer communication preference',
    'Respect customer communication preferences',
    35,
    '{"operator": "AND", "conditions": [{"field": "communication_preference", "operator": "equals", "value": "email"}, {"field": "notification_type", "operator": "in", "value": ["order_update", "promotion", "survey"]}]}',
    '{"actions": [{"type": "email_notification", "value": true, "description": "Send email notification"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OXM',
    'Order delay notification',
    'Notify customers of order delays',
    40,
    '{"operator": "AND", "conditions": [{"field": "estimated_delivery", "operator": "less_than", "value": "current_date"}, {"field": "order_status", "operator": "not_equals", "value": "delivered"}]}',
    '{"actions": [{"type": "delay_notification", "value": true, "description": "Send delay notification"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- WMS (Warehouse Management System) Rules
INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'WMS',
    'Low stock reorder',
    'Create reorder when stock level is below threshold',
    10,
    '{"operator": "AND", "conditions": [{"field": "stock_level", "operator": "less_than", "value": 10}, {"field": "product_category", "operator": "in", "value": ["electronics", "clothing"]}]}',
    '{"actions": [{"type": "create_order", "value": 50, "description": "Create reorder for 50 units"}, {"type": "notification", "value": "low_stock_alert", "description": "Send low stock notification"}]}',
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
    'High demand alert',
    'Alert for high demand products',
    15,
    '{"operator": "AND", "conditions": [{"field": "demand_level", "operator": "greater_than", "value": 80}, {"field": "stock_level", "operator": "less_than", "value": 20}]}',
    '{"actions": [{"type": "notification", "value": "high_demand_alert", "description": "Send high demand notification"}]}',
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
    'Expiry date warning',
    'Warning for products nearing expiry',
    20,
    '{"operator": "AND", "conditions": [{"field": "days_to_expiry", "operator": "less_than", "value": 30}, {"field": "stock_level", "operator": "greater_than", "value": 0}]}',
    '{"actions": [{"type": "notification", "value": "expiry_warning", "description": "Send expiry warning"}]}',
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
    'Inventory cycle count',
    'Schedule cycle count for high-value items',
    25,
    '{"operator": "AND", "conditions": [{"field": "item_value", "operator": "greater_than", "value": 100}, {"field": "last_count_days", "operator": "greater_than", "value": 30}]}',
    '{"actions": [{"type": "schedule_cycle_count", "value": true, "description": "Schedule inventory cycle count"}]}',
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
    'Warehouse capacity alert',
    'Alert when warehouse capacity is high',
    30,
    '{"operator": "AND", "conditions": [{"field": "warehouse_utilization", "operator": "greater_than", "value": 85}, {"field": "incoming_orders", "operator": "greater_than", "value": 100}]}',
    '{"actions": [{"type": "capacity_alert", "value": true, "description": "Send warehouse capacity alert"}]}',
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
    'Pick optimization',
    'Optimize pick routes for efficiency',
    35,
    '{"operator": "AND", "conditions": [{"field": "pick_list_size", "operator": "greater_than", "value": 20}, {"field": "warehouse_zone", "operator": "equals", "value": "multi_zone"}]}',
    '{"actions": [{"type": "optimize_pick_route", "value": true, "description": "Optimize pick route for efficiency"}]}',
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
    'Quality control check',
    'Trigger quality control for fragile items',
    40,
    '{"operator": "AND", "conditions": [{"field": "item_fragility", "operator": "equals", "value": "high"}, {"field": "order_priority", "operator": "equals", "value": "express"}]}',
    '{"actions": [{"type": "quality_control_check", "value": true, "description": "Trigger quality control check"}]}',
    true,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Disabled Rules for Testing
INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OMS',
    'Disabled OMS rule',
    'This OMS rule is disabled for testing purposes',
    100,
    '{"operator": "AND", "conditions": [{"field": "test_field", "operator": "equals", "value": "test"}]}',
    '{"actions": [{"type": "test_action", "value": "test", "description": "Test action"}]}',
    false,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'OXM',
    'Disabled OXM rule',
    'This OXM rule is disabled for testing purposes',
    110,
    '{"operator": "AND", "conditions": [{"field": "test_field", "operator": "equals", "value": "test"}]}',
    '{"actions": [{"type": "test_action", "value": "test", "description": "Test action"}]}',
    false,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO rules (id, product, name, description, priority, conditions, actions, enabled, version, created_by, created_at, updated_at) 
VALUES (
    gen_random_uuid(),
    'WMS',
    'Disabled WMS rule',
    'This WMS rule is disabled for testing purposes',
    120,
    '{"operator": "AND", "conditions": [{"field": "test_field", "operator": "equals", "value": "test"}]}',
    '{"actions": [{"type": "test_action", "value": "test", "description": "Test action"}]}',
    false,
    1,
    'system',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Display the inserted data
SELECT id, product, name, priority, enabled, created_at FROM rules ORDER BY product, priority; 