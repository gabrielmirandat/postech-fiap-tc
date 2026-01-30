class Notification < ApplicationRecord
  # Statuses
  STATUS_PENDING = 'pending'
  STATUS_SENT = 'sent'
  STATUS_FAILED = 'failed'

  # Types
  TYPE_EMAIL = 'email'
  TYPE_SMS = 'sms'
  TYPE_PUSH = 'push'

  # Contact types (from core proto)
  CONTACT_TYPE_EMAIL = 'email'
  CONTACT_TYPE_CELLPHONE = 'cellphone'

  validates :order_id, presence: true
  validates :notification_type, presence: true, inclusion: { in: [TYPE_EMAIL, TYPE_SMS, TYPE_PUSH] }
  validates :contact_type, presence: true, inclusion: { in: [CONTACT_TYPE_EMAIL, CONTACT_TYPE_CELLPHONE] }
  validates :contact_value, presence: true
  validates :status, presence: true, inclusion: { in: [STATUS_PENDING, STATUS_SENT, STATUS_FAILED] }

  scope :by_order, ->(order_id) { where(order_id: order_id) }
  scope :pending, -> { where(status: STATUS_PENDING) }
  scope :sent, -> { where(status: STATUS_SENT) }
  scope :failed, -> { where(status: STATUS_FAILED) }

  def mark_as_sent!
    update!(status: STATUS_SENT, sent_at: Time.current)
  end

  def mark_as_failed!(error_message)
    update!(status: STATUS_FAILED, failed_at: Time.current, error_message: error_message)
  end
end
