class NotificationService
  def initialize(params)
    @params = params
  end

  def create_notification
    contact_type = determine_contact_type(@params)
    contact_value = extract_contact_value(@params)
    
    return nil unless contact_type && contact_value

    notification_type = map_contact_to_notification_type(contact_type)
    
    Notification.create!(
      entity_type: @params[:entity_type],
      entity_id: @params[:entity_id],
      notification_type: notification_type,
      contact_type: contact_type,
      contact_value: contact_value,
      status: Notification::STATUS_PENDING,
      message: @params[:message],
      event_type: @params[:event_type],
      event_id: @params[:event_id],
      metadata: @params[:metadata] || {}
    )
  end

  private

  def determine_contact_type(params)
    # Try contact_data first (nested structure)
    contact_data = params[:contact_data] || params['contact_data']
    if contact_data
      return 'email' if contact_data['email'] || contact_data[:email]
      return 'cellphone' if contact_data['cellphone'] || contact_data[:cellphone]
    end
    
    # Fallback to direct contact_type
    return params[:contact_type] if params[:contact_type]
    return params['contact_type'] if params['contact_type']
    nil
  end

  def extract_contact_value(params)
    # Try contact_data first (nested structure)
    contact_data = params[:contact_data] || params['contact_data']
    if contact_data
      email_data = contact_data['email'] || contact_data[:email]
      if email_data && (email_data['value'] || email_data[:value])
        return email_data['value'] || email_data[:value]
      end
      
      cellphone_data = contact_data['cellphone'] || contact_data[:cellphone]
      if cellphone_data && (cellphone_data['value'] || cellphone_data[:value])
        return cellphone_data['value'] || cellphone_data[:value]
      end
    end
    
    # Fallback to direct contact_value
    return params[:contact_value] if params[:contact_value]
    return params['contact_value'] if params['contact_value']
    nil
  end

  def map_contact_to_notification_type(contact_type)
    case contact_type.to_s.downcase
    when 'email'
      Notification::TYPE_EMAIL
    when 'cellphone'
      Notification::TYPE_SMS
    else
      Notification::TYPE_EMAIL
    end
  end
end
