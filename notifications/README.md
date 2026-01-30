# Notifications Service

Notification service developed in Ruby on Rails for the restaurant management system.

## Architecture

- **Framework**: Ruby on Rails 7.1 (API-only)
- **Database**: PostgreSQL
- **Messaging**: Kafka (CloudEvents)
- **Notification Services**: Email and SMS (mocked)

## Features

1. **Event Consumption**: Consumes Kafka events about order status changes
2. **Notification Sending**: Sends notifications via Email or SMS based on contact type
3. **Send Control**: Stores history of all sent notifications
4. **Event Publishing**: Publishes events about send results (success/failure)

## Structure

```
notifications/
├── app/
│   ├── controllers/     # API REST controllers
│   ├── models/          # ActiveRecord models
│   ├── services/        # Business logic
│   └── jobs/            # Background jobs (Kafka consumer)
├── config/              # Rails configurations
├── db/                  # Migrations
└── spec/               # Tests
```

## Models

### Notification

Stores information about sent notifications:
- `order_id`: Related order ID
- `notification_type`: Type (email, sms, push)
- `contact_type`: Contact type (email, cellphone)
- `contact_value`: Contact value
- `status`: Status (pending, sent, failed)
- `message`: Sent message
- `error_message`: Error message (if failed)
- `event_type`: Event type that originated the notification
- `metadata`: Additional data in JSON

## Services

### NotificationService

Main service that processes order events and creates notifications.

### EmailNotificationSender

Mocked service for sending emails (similar to SendGrid, Mailgun).

### SmsNotificationSender

Mocked service for sending SMS (similar to Twilio, AWS SNS).

### NotificationEventPublisher

Publishes events about notification send results to Kafka.

## REST API

### GET /api/v1/notifications

Lists all notifications (with pagination).

**Query params:**
- `status`: Filter by status (pending, sent, failed)
- `page`: Page number
- `per_page`: Items per page

### GET /api/v1/notifications/:id

Returns details of a specific notification.

### GET /api/v1/notifications/order/:order_id

Returns all notifications for a specific order.

### POST /api/v1/notifications

Creates and sends a new notification.

## Kafka Events

### Consumed

- `postech.orders.v1.order.created`: Order created
- `postech.orders.v1.order.updated`: Order updated
- `postech.orders.v1.order.canceled`: Order canceled

### Published

- `postech.notifications.v1.notification.sent`: Notification sent successfully
- `postech.notifications.v1.notification.failed`: Failed to send notification

## Configuration

Environment variables:

- `POSTGRES_HOST`: PostgreSQL host (default: localhost)
- `POSTGRES_PORT`: PostgreSQL port (default: 5432)
- `POSTGRES_USER`: PostgreSQL user
- `POSTGRES_PASSWORD`: PostgreSQL password
- `POSTGRES_DB`: Database name
- `KAFKA_SERVER_URL`: Kafka server URL (default: localhost:9092)
- `KAFKA_GROUP_ID`: Consumer group ID
- `KAFKA_DOMAIN_TOPIC`: Topic to consume order events (default: orders)
- `KAFKA_NOTIFICATIONS_TOPIC`: Topic to publish notification events (default: notifications)

## Development

### Setup

```bash
# Install dependencies
bundle install

# Create database
rails db:create
rails db:migrate

# Start server
rails server -p 8005

# Start Kafka consumer (in another terminal)
rails kafka:consumer
```

### Tests

```bash
# Run tests
bundle exec rspec
```

## Build with Bazel

```bash
# Build image
bazel build //notifications:notifications_image

# Push to Docker Hub
bazel run //notifications:notifications_push
```

## Makefile

```bash
# Complete setup
make setup

# Build
make build

# Tests
make test

# Server
make server

# Kafka consumer
make consumer
```
