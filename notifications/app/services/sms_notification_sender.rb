class SmsNotificationSender
  def initialize(notification)
    @notification = notification
  end

  def send
    # Mocked SMS service (similar to Twilio, AWS SNS, etc.)
    # In production, this would integrate with a real SMS service
    begin
      # Simulate API call delay
      sleep(0.1)
      
      # Mock success/failure (85% success rate for demo)
      success = rand > 0.15
      
      if success
        Rails.logger.info "SMS sent successfully to #{@notification.contact_value}"
        @notification.mark_as_sent!
        publish_notification_sent_event
      else
        error_msg = "SMS service temporarily unavailable"
        Rails.logger.error "Failed to send SMS: #{error_msg}"
        @notification.mark_as_failed!(error_msg)
        publish_notification_failed_event(error_msg)
      end
    rescue => e
      Rails.logger.error "Error sending SMS: #{e.message}"
      @notification.mark_as_failed!(e.message)
      publish_notification_failed_event(e.message)
    end
  end

  private

  def publish_notification_sent_event
    NotificationEventPublisher.new(@notification, 'sent').publish
  end

  def publish_notification_failed_event(error_message)
    NotificationEventPublisher.new(@notification, 'failed', error_message).publish
  end
end
