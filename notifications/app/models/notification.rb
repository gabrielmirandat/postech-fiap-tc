class Notification < ApplicationRecord
  # Statuses
  STATUS_PENDING = 'pending'
  STATUS_SENT = 'sent'
  STATUS_FAILED = 'failed'

  # Types
  TYPE_EMAIL = 'email'
  TYPE_SMS = 'sms'
  TYPE_PUSH = 'push'

  # Contact types
  CONTACT_TYPE_EMAIL = 'email'
  CONTACT_TYPE_CELLPHONE = 'cellphone'

  validates :notification_type, presence: true, inclusion: { in: [TYPE_EMAIL, TYPE_SMS, TYPE_PUSH] }
  validates :contact_type, presence: true, inclusion: { in: [CONTACT_TYPE_EMAIL, CONTACT_TYPE_CELLPHONE] }
  validates :contact_value, presence: true
  validates :status, presence: true, inclusion: { in: [STATUS_PENDING, STATUS_SENT, STATUS_FAILED] }

  scope :by_entity, ->(entity_type, entity_id) { where(entity_type: entity_type, entity_id: entity_id) }
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
