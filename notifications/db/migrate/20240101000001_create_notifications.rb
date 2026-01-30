class CreateNotifications < ActiveRecord::Migration[7.1]
  def change
    create_table :notifications do |t|
      t.string :order_id, null: false, index: true
      t.string :notification_type, null: false # email, sms, push
      t.string :contact_type, null: false # email, cellphone
      t.string :contact_value, null: false
      t.string :status, null: false, default: 'pending' # pending, sent, failed
      t.text :message
      t.text :error_message
      t.string :event_type # postech.orders.v1.order.created, etc
      t.string :event_id
      t.timestamp :sent_at
      t.timestamp :failed_at
      t.jsonb :metadata

      t.timestamps
    end

    add_index :notifications, [:order_id, :status]
    add_index :notifications, [:status, :created_at]
  end
end
