require 'cloudevents'

class NotificationEventPublisher
  def initialize(notification, status, error_message = nil)
    @notification = notification
    @status = status
    @error_message = error_message
  end

  def publish
    event = build_cloud_event
    topic = ENV.fetch("KAFKA_NOTIFICATIONS_TOPIC", "notifications")
    
    begin
      KAFKA_CLIENT.deliver_message(
        event.to_json,
        topic: topic,
        key: @notification.order_id
      )
      Rails.logger.info "Published notification event: #{@status} for order #{@notification.order_id}"
    rescue => e
      Rails.logger.error "Failed to publish notification event: #{e.message}"
    end
  end

  private

  def build_cloud_event
    event_type = @status == 'sent' ? 
      'postech.notifications.v1.notification.sent' : 
      'postech.notifications.v1.notification.failed'
    
    payload = {
      notification_id: @notification.id,
      order_id: @notification.order_id,
      notification_type: @notification.notification_type,
      contact_type: @notification.contact_type,
      contact_value: @notification.contact_value,
      status: @status,
      error_message: @error_message,
      sent_at: @notification.sent_at&.iso8601,
      failed_at: @notification.failed_at&.iso8601,
      timestamp: Time.current.iso8601
    }

    CloudEvents::Event.create(
      spec_version: "1.0",
      id: SecureRandom.uuid,
      source: "post/notifications",
      type: event_type,
      data: payload,
      data_content_type: "application/json"
    )
  end
end
