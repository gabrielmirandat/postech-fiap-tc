class SendNotificationJob < ApplicationJob
  queue_as :default

  def perform(notification_id)
    notification = Notification.find(notification_id)
    
    case notification.notification_type
    when Notification::TYPE_EMAIL
      EmailNotificationSender.new(notification).send
    when Notification::TYPE_SMS
      SmsNotificationSender.new(notification).send
    else
      raise "Unknown notification type: #{notification.notification_type}"
    end
  rescue ActiveRecord::RecordNotFound => e
    Rails.logger.error "Notification not found: #{notification_id}"
  rescue => e
    Rails.logger.error "Error processing notification #{notification_id}: #{e.message}"
    Rails.logger.error e.backtrace.join("\n")
    
    # Try to mark notification as failed if it exists
    notification = Notification.find_by(id: notification_id)
    notification&.mark_as_failed!(e.message)
  end
end
