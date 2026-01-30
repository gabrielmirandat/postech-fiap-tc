class NotificationService
  def initialize(order_data, contact_data)
    @order_data = order_data
    @contact_data = contact_data
  end

  def send_notification
    return nil unless @contact_data

    contact_type = determine_contact_type(@contact_data)
    contact_value = extract_contact_value(@contact_data)
    
    return nil unless contact_type && contact_value

    notification_type = map_contact_to_notification_type(contact_type)
    
    notification = Notification.create!(
      order_id: extract_order_id(@order_data),
      notification_type: notification_type,
      contact_type: contact_type,
      contact_value: contact_value,
      status: Notification::STATUS_PENDING,
      message: build_message(@order_data),
      event_type: @order_data['event_type'],
      event_id: @order_data['event_id'],
      metadata: { order_data: @order_data, contact_data: @contact_data }
    )

    send_notification_by_type(notification)
    notification
  end

  private

  def determine_contact_type(contact_data)
    return 'email' if contact_data['email']
    return 'cellphone' if contact_data['cellphone']
    return contact_data['type'] if contact_data['type']
    nil
  end

  def extract_contact_value(contact_data)
    return contact_data['email']['value'] if contact_data['email']
    return contact_data['cellphone']['value'] if contact_data['cellphone']
    return contact_data['custom_value'] if contact_data['custom_value']
    nil
  end

  def map_contact_to_notification_type(contact_type)
    case contact_type.downcase
    when 'email'
      Notification::TYPE_EMAIL
    when 'cellphone'
      Notification::TYPE_SMS
    else
      Notification::TYPE_EMAIL
    end
  end

  def extract_order_id(order_data)
    order_data['orderId']&.dig('value') || 
    order_data['order_id'] || 
    order_data['id'] ||
    'unknown'
  end

  def build_message(order_data)
    status = order_data['status'] || 'created'
    order_id = extract_order_id(order_data)
    
    case status.downcase
    when 'created'
      "Your order ##{order_id} has been created successfully!"
    when 'preparation'
      "Your order ##{order_id} is being prepared!"
    when 'packaging'
      "Your order ##{order_id} is being packaged!"
    when 'pickup'
      "Your order ##{order_id} is ready for pickup!"
    when 'delivery'
      "Your order ##{order_id} is on the way!"
    when 'completed'
      "Your order ##{order_id} has been delivered successfully!"
    when 'canceled'
      "Your order ##{order_id} has been canceled."
    else
      "Update about your order ##{order_id}"
    end
  end

  def send_notification_by_type(notification)
    case notification.notification_type
    when Notification::TYPE_EMAIL
      EmailNotificationSender.new(notification).send
    when Notification::TYPE_SMS
      SmsNotificationSender.new(notification).send
    else
      raise "Unknown notification type: #{notification.notification_type}"
    end
  end
end
