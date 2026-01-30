require 'cloudevents'
require 'json'

class OrderEventConsumerJob
  def self.perform_now
    new.perform
  end

  def perform
    topic = ENV.fetch("KAFKA_DOMAIN_TOPIC", "orders")
    KAFKA_CONSUMER.subscribe(topic)

    Rails.logger.info "Starting to consume events from topic: #{topic}"

    KAFKA_CONSUMER.each_message do |message|
      begin
        process_message(message)
      rescue => e
        Rails.logger.error "Error processing message: #{e.message}"
        Rails.logger.error e.backtrace.join("\n")
      end
    end
  end

  private

  def process_message(message)
    event_data = JSON.parse(message.value)
    event_type = event_data['type'] || event_data['eventType']
    
    Rails.logger.info "Received event: #{event_type}"

    case event_type
    when 'postech.orders.v1.order.created'
      handle_order_created(event_data)
    when 'postech.orders.v1.order.updated'
      handle_order_updated(event_data)
    when 'postech.orders.v1.order.canceled'
      handle_order_canceled(event_data)
    else
      Rails.logger.warn "Unknown event type: #{event_type}"
    end
  end

  def handle_order_created(event_data)
    order_data = extract_order_data(event_data)
    contact_data = extract_contact_data(order_data)
    
    if contact_data
      NotificationService.new(
        order_data.merge('event_type' => 'postech.orders.v1.order.created', 'event_id' => event_data['id']),
        contact_data
      ).send_notification
    else
      Rails.logger.warn "No contact information found for order #{order_data['orderId']}"
    end
  end

  def handle_order_updated(event_data)
    order_data = extract_order_data(event_data)
    contact_data = extract_contact_data(order_data)
    
    if contact_data
      NotificationService.new(
        order_data.merge('event_type' => 'postech.orders.v1.order.updated', 'event_id' => event_data['id']),
        contact_data
      ).send_notification
    end
  end

  def handle_order_canceled(event_data)
    order_data = extract_order_data(event_data)
    contact_data = extract_contact_data(order_data)
    
    if contact_data
      NotificationService.new(
        order_data.merge('event_type' => 'postech.orders.v1.order.canceled', 'event_id' => event_data['id']),
        contact_data
      ).send_notification
    end
  end

  def extract_order_data(event_data)
    # CloudEvents format: data field contains the order
    order_data = event_data['data'] || event_data
    
    # Handle both JSON string and object
    if order_data.is_a?(String)
      JSON.parse(order_data)
    else
      order_data
    end
  end

  def extract_contact_data(order_data)
    # Handle different possible structures
    contact = order_data['contact'] || 
              order_data['notification'] ||
              order_data['additionalContact']
    
    return nil unless contact
    
    # Handle both object and string formats
    if contact.is_a?(String)
      JSON.parse(contact) rescue { 'type' => 'cellphone', 'cellphone' => { 'value' => contact } }
    else
      contact
    end
  end
end
