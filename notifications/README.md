# Notifications Service

Notification service developed in Ruby on Rails for the restaurant management system.

## Architecture

- **Framework**: Ruby on Rails 7.1 (API-only)
- **Database**: PostgreSQL
- **Messaging**: Kafka (CloudEvents)
- **Notification Services**: Email and SMS (mocked)

## Features

1. **Generic API**: RESTful API for creating notifications for any entity type (generic, domain-agnostic)
2. **Asynchronous Processing**: Notifications are processed asynchronously via background jobs
3. **Notification Sending**: Sends notifications via Email or SMS based on contact type
4. **Send Control**: Stores history of all sent notifications
5. **Event Publishing**: Publishes events about send results (success/failure) to Kafka following DDD patterns

## Structure

```
notifications/
├── app/
│   ├── controllers/     # API REST controllers
│   ├── models/          # ActiveRecord models
│   ├── services/        # Business logic
│   └── jobs/            # Background jobs (async notification processing)
├── config/              # Rails configurations
├── db/                  # Migrations
└── spec/               # Tests
```

## Models

### Notification

Stores information about sent notifications:
- `entity_type`: Type of entity (e.g., 'order', 'payment', etc.) - generic field
- `entity_id`: ID of the entity - generic field
- `notification_type`: Type (email, sms, push)
- `contact_type`: Contact type (email, cellphone)
- `contact_value`: Contact value
- `status`: Status (pending, sent, failed)
- `message`: Sent message
- `error_message`: Error message (if failed)
- `event_type`: Event type that originated the notification (optional)
- `event_id`: Event ID (optional)
- `metadata`: Additional data in JSON

## Services

### NotificationService

Generic service that creates notifications for any entity type. Does not have knowledge of specific domains.

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
- `entity_type`: Filter by entity type
- `entity_id`: Filter by entity ID
- `page`: Page number
- `per_page`: Items per page

### GET /api/v1/notifications/:id

Returns details of a specific notification.

### POST /api/v1/notifications

Creates and enqueues a new notification for asynchronous processing.

**Request body:**
```json
{
  "notification": {
    "entity_type": "order",
    "entity_id": "123",
    "contact_type": "email",
    "contact_value": "user@example.com",
    "message": "Your order has been created!",
    "event_type": "postech.orders.v1.order.created",
    "event_id": "event-123",
    "metadata": {}
  }
}
```

**Alternative with nested contact_data:**
```json
{
  "notification": {
    "entity_type": "order",
    "entity_id": "123",
    "contact_data": {
      "email": {
        "value": "user@example.com"
      }
    },
    "message": "Your order has been created!",
    "metadata": {}
  }
}
```

## Kafka Events

### Published (DDD Domain Events)

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
```
