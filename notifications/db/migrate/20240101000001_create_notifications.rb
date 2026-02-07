class CreateNotifications < ActiveRecord::Migration[7.1]
  def change
    create_table :notifications do |t|
      t.string :entity_type # Generic entity type (e.g., 'order', 'payment', etc.)
      t.string :entity_id # Generic entity ID
      t.string :notification_type, null: false # email, sms, push
      t.string :contact_type, null: false # email, cellphone
      t.string :contact_value, null: false
      t.string :status, null: false, default: 'pending' # pending, sent, failed
      t.text :message
      t.text :error_message
      t.string :event_type # Event type that originated the notification (optional)
      t.string :event_id # Event ID (optional)
      t.timestamp :sent_at
      t.timestamp :failed_at
      t.jsonb :metadata

      t.timestamps
    end

    add_index :notifications, [:entity_type, :entity_id, :status]
    add_index :notifications, [:entity_type, :entity_id]
    add_index :notifications, [:status, :created_at]
  end
end
