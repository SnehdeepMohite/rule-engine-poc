# Rule Engine POC

A Spring Boot 3.x based rule engine proof of concept with PostgreSQL database, caching, and RESTful APIs for rule management and evaluation.

## 🚀 Features

- **Rule Engine**: Evaluate rules based on conditions and execute actions
- **Caching**: In-memory cache for improved performance
- **CRUD APIs**: Full administration interface for rule management
- **PostgreSQL**: JSONB support for flexible conditions and actions
- **Validation**: Comprehensive rule validation
- **Multi-product Support**: Rules organized by product domains

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Docker (optional)

## 🗄️ Database Setup

### Option 1: Using Docker (Recommended)

```bash
# Start PostgreSQL container
docker run --name ruleengine-postgres \
  -e POSTGRES_DB=ruleengine \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15
```

### Option 2: Local PostgreSQL

1. Install PostgreSQL
2. Create database:
```sql
CREATE DATABASE ruleengine;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE ruleengine TO postgres;
```

## 🏃‍♂️ Running the Application

### 1. Clone and Build

```bash
git clone <repository-url>
cd rule-engine-poc
mvn clean install
```

### 2. Run Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📊 Sample Data

The application comes with two sample rules:

1. **Amazon Discount Rule** (Product: Channel)
   - Applies 10% discount for Amazon orders over $100

2. **Low Stock Reorder Rule** (Product: WMS)
   - Creates reorder when stock level is below 10 for electronics/clothing

## 🔧 API Endpoints

### Rule Evaluation

#### Evaluate Rules
```bash
POST /evaluate
Content-Type: application/json

{
  "product": "Channel",
  "payload": {
    "channel": "amazon",
    "order_total": 150
  }
}
```

**Response:**
```json
{
  "product": "Channel",
  "matchedActions": [
    {
      "type": "discount",
      "value": 10,
      "description": "Apply 10% discount"
    }
  ],
  "actionCount": 1
}
```

#### Get Cache Status
```bash
GET /evaluate/cache/status
```

### Admin APIs

#### Get All Rules
```bash
GET /admin/rules
```

#### Get All Rules (Paginated)
```bash
GET /admin/rules/paginated?page=1&size=10&sortBy=name&sortDirection=asc
```

**Query Parameters:**
- `page` (optional): Page number (1-based, default: 1)
- `size` (optional): Number of items per page (1-100, default: 10)
- `sortBy` (optional): Field to sort by (id, name, product, priority, enabled, version, createdAt, updatedAt, default: name)
- `sortDirection` (optional): Sort direction (asc, desc, default: asc)

**Response:**
```json
{
  "content": [
    {
      "id": "uuid",
      "name": "Rule Name",
      "product": "Product",
      "priority": 1,
      "enabled": true,
      "createdAt": "2024-01-01T00:00:00Z",
      "updatedAt": "2024-01-01T00:00:00Z"
    }
  ],
  "page": 1,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "sortBy": "name",
  "sortDirection": "asc"
}
```

#### Get Rules by Product
```bash
GET /admin/rules/product/{product}
```

#### Get Rules by Product (Paginated)
```bash
GET /admin/rules/product/{product}/paginated?page=1&size=10&sortBy=name&sortDirection=asc
```

**Query Parameters:** Same as above

#### Get Rules by Status (Paginated)
```bash
GET /admin/rules/status/{enabled}?page=1&size=10&sortBy=name&sortDirection=asc
```

**Path Parameters:**
- `enabled`: true for enabled rules, false for disabled rules

**Query Parameters:** Same as above

#### Search Rules (Paginated)
```bash
GET /admin/rules/search?name=discount&product=ecommerce&enabled=true&page=1&size=10&sortBy=name&sortDirection=asc
```

**Query Parameters:**
- `name` (optional): Search for rules containing this text in the name (case-insensitive)
- `product` (optional): Search for rules containing this text in the product (case-insensitive)
- `enabled` (optional): Filter by enabled status (true/false)
- `page` (optional): Page number (1-based, default: 1)
- `size` (optional): Number of items per page (1-100, default: 10)
- `sortBy` (optional): Field to sort by (id, name, product, priority, enabled, version, createdAt, updatedAt, default: name)
- `sortDirection` (optional): Sort direction (asc, desc, default: asc)

**Search Examples:**
- Search by name only: `?name=discount`
- Search by product only: `?product=ecommerce`
- Search by name and product: `?name=amazon&product=channel`
- Search enabled rules by name: `?name=stock&enabled=true`
- Search disabled rules by product: `?product=mobile&enabled=false`
- Search by all criteria: `?name=order&product=wms&enabled=true`

#### Get Rule by ID
```bash
GET /admin/rules/{id}
```

#### Create Rule
```bash
POST /admin/rules
Content-Type: application/json

{
  "product": "Channel",
  "name": "New Rule",
  "description": "Rule description",
  "priority": 15,
  "conditions": "{\"operator\": \"AND\", \"conditions\": [{\"field\": \"amount\", \"operator\": \"greater_than\", \"value\": 50}]}",
  "actions": "{\"actions\": [{\"type\": \"notification\", \"value\": \"high_value_order\", \"description\": \"Send notification\"}]}",
  "enabled": true,
  "createdBy": "admin"
}
```

#### Update Rule
```bash
PUT /admin/rules/{id}
Content-Type: application/json

{
  "product": "Channel",
  "name": "Updated Rule",
  "description": "Updated description",
  "priority": 20,
  "conditions": "{\"operator\": \"AND\", \"conditions\": [{\"field\": \"amount\", \"operator\": \"greater_than\", \"value\": 100}]}",
  "actions": "{\"actions\": [{\"type\": \"discount\", \"value\": 15, \"description\": \"Apply 15% discount\"}]}",
  "enabled": true
}
```

#### Delete Rule
```bash
DELETE /admin/rules/{id}
```

#### Toggle Rule Status
```bash
PATCH /admin/rules/{id}/toggle
```

#### Refresh Cache
```bash
POST /admin/rules/refresh-cache
```

## 🧪 Testing Examples

### Test Amazon Discount Rule

```bash
curl -X POST http://localhost:8080/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "product": "Channel",
    "payload": {
      "channel": "amazon",
      "order_total": 150
    }
  }'
```

### Test WMS Low Stock Rule

```bash
curl -X POST http://localhost:8080/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "product": "WMS",
    "payload": {
      "stock_level": 5,
      "product_category": "electronics"
    }
  }'
```

### Test No Match Scenario

```bash
curl -X POST http://localhost:8080/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "product": "Channel",
    "payload": {
      "channel": "ebay",
      "order_total": 50
    }
  }'
```

### Test Paginated Endpoints

#### Get All Rules (Paginated)
```bash
curl "http://localhost:8080/admin/rules/paginated?page=1&size=5&sortBy=priority&sortDirection=desc"
```

#### Get Rules by Product (Paginated)
```bash
curl "http://localhost:8080/admin/rules/product/ecommerce/paginated?page=1&size=10&sortBy=name&sortDirection=asc"
```

#### Get Enabled Rules (Paginated)
```bash
curl "http://localhost:8080/admin/rules/status/true?page=1&size=10&sortBy=createdAt&sortDirection=desc"
```

#### Get Rules with Default Parameters
```bash
curl "http://localhost:8080/admin/rules/paginated"
```

### Test Search Endpoints

#### Search Rules by Name
```bash
curl "http://localhost:8080/admin/rules/search?name=discount&page=1&size=10&sortBy=name&sortDirection=asc"
```

#### Search Rules by Product
```bash
curl "http://localhost:8080/admin/rules/search?product=ecommerce&page=1&size=10&sortBy=name&sortDirection=asc"
```

#### Search Rules by Name and Product
```bash
curl "http://localhost:8080/admin/rules/search?name=amazon&product=channel&page=1&size=10&sortBy=priority&sortDirection=desc"
```

#### Search Enabled Rules by Name
```bash
curl "http://localhost:8080/admin/rules/search?name=stock&enabled=true&page=1&size=10&sortBy=name&sortDirection=asc"
```

#### Search Rules by All Criteria
```bash
curl "http://localhost:8080/admin/rules/search?name=order&product=wms&enabled=true&page=1&size=5&sortBy=priority&sortDirection=desc"
```

#### Search Rules with Default Values
```bash
curl "http://localhost:8080/admin/rules/search"
```

## 📝 Rule Format

### Conditions JSON Structure
```json
{
  "operator": "AND|OR",
  "conditions": [
    {
      "field": "field_name",
      "operator": "equals|not_equals|greater_than|less_than|in|not_in|contains|not_contains",
      "value": "field_value"
    }
  ]
}
```

### Actions JSON Structure
```json
{
  "actions": [
    {
      "type": "action_type",
      "value": "action_value",
      "description": "Action description"
    }
  ]
}
```

## 🔍 Supported Operators

### Comparison Operators
- `equals`: Exact match
- `not_equals`: Not equal
- `greater_than`: Numeric greater than
- `less_than`: Numeric less than
- `greater_than_or_equal`: Numeric greater than or equal
- `less_than_or_equal`: Numeric less than or equal

### Collection Operators
- `in`: Value is in collection
- `not_in`: Value is not in collection

### String Operators
- `contains`: String contains
- `not_contains`: String does not contain

## 🏗️ Project Structure

```
rule-engine-poc/
├── pom.xml                          # Maven configuration
├── src/main/java/com/example/ruleengine/
│   ├── RuleEngineApplication.java   # Spring Boot main class
│   ├── controller/                  # REST controllers
│   │   ├── RuleAdminController.java # Admin CRUD APIs
│   │   └── RuleEngineController.java # Rule evaluation API
│   ├── dto/                         # Data Transfer Objects
│   │   ├── ActionDto.java          # Action structure
│   │   ├── ConditionDto.java       # Condition structure
│   │   └── EvaluateRequest.java    # Evaluation request
│   ├── entity/                      # JPA entities
│   │   └── RuleEntity.java         # Rule database entity
│   ├── repository/                  # Data access layer
│   │   └── RuleRepository.java     # JPA repository
│   ├── service/                     # Business logic
│   │   └── RuleAdminService.java   # Admin service
│   └── engine/                      # Rule engine core
│       └── RuleEngine.java         # Rule evaluation engine
├── src/main/resources/
│   ├── application.yml              # Application configuration
│   └── data.sql                     # Sample data
└── README.md                        # This file
```

## 🔧 Configuration

### Database Configuration
Edit `src/main/resources/application.yml` to modify database settings:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ruleengine
    username: postgres
    password: postgres
```

### Logging Configuration
The application includes detailed logging for debugging:

```yaml
logging:
  level:
    com.example.ruleengine: DEBUG
    org.hibernate.SQL: DEBUG
```

## 🚀 Performance Features

- **In-Memory Caching**: Rules are cached by product for fast evaluation
- **Short-Circuit Evaluation**: AND/OR conditions are evaluated efficiently
- **Database Optimization**: Indexed queries and JSONB storage
- **Connection Pooling**: HikariCP for database connection management

## 🔒 Security Considerations

- Input validation for all rule data
- JSON schema validation for conditions and actions
- SQL injection protection through JPA
- Error handling and logging

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify PostgreSQL is running
   - Check database credentials in `application.yml`
   - Ensure database `ruleengine` exists

2. **Rule Evaluation Fails**
   - Check JSON format of conditions and actions
   - Verify field names in payload match condition fields
   - Review application logs for detailed error messages

3. **Cache Issues**
   - Use `/admin/rules/refresh-cache` to refresh cache
   - Check cache status via `/evaluate/cache/status`

## 📈 Monitoring

The application provides several endpoints for monitoring:

- `/evaluate/cache/status`: Cache statistics
- `/admin/rules`: List all rules
- Application logs with detailed rule evaluation information

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License. 
