module Api
  module V1
    class NotificationsController < ApplicationController
      before_action :set_notification, only: [:show]

      def index
        @notifications = Notification.order(created_at: :desc)
        @notifications = @notifications.where(status: params[:status]) if params[:status].present?
        @notifications = @notifications.where(entity_type: params[:entity_type]) if params[:entity_type].present?
        @notifications = @notifications.where(entity_id: params[:entity_id]) if params[:entity_id].present?
        page = (params[:page] || 1).to_i
        per_page = (params[:per_page] || 20).to_i
        total = @notifications.count
        @notifications = @notifications.offset((page - 1) * per_page).limit(per_page)
        
        render json: {
          notifications: @notifications.map { |n| notification_json(n) },
          pagination: {
            page: page,
            per_page: per_page,
            total: total
          }
        }
      end

      def show
        render json: notification_json(@notification)
      end

      def create
        notification_service = NotificationService.new(notification_params)
        @notification = notification_service.create_notification
        
        if @notification&.persisted?
          # Enqueue job to send notification asynchronously
          SendNotificationJob.perform_later(@notification.id)
          
          render json: notification_json(@notification), status: :created
        else
          render json: { errors: @notification&.errors&.full_messages || ['Failed to create notification'] }, status: :unprocessable_entity
        end
      end

      private

      def set_notification
        @notification = Notification.find(params[:id])
      end

      def notification_params
        params.require(:notification).permit(
          :entity_type, :entity_id, :notification_type, :contact_type, :contact_value,
          :message, :event_type, :event_id, metadata: {}, contact_data: {}
        ).to_h.symbolize_keys
      end

      def notification_json(notification)
        {
          id: notification.id,
          entity_type: notification.entity_type,
          entity_id: notification.entity_id,
          notification_type: notification.notification_type,
          contact_type: notification.contact_type,
          contact_value: notification.contact_value,
          status: notification.status,
          message: notification.message,
          error_message: notification.error_message,
          event_type: notification.event_type,
          event_id: notification.event_id,
          sent_at: notification.sent_at&.iso8601,
          failed_at: notification.failed_at&.iso8601,
          created_at: notification.created_at.iso8601,
          updated_at: notification.updated_at.iso8601,
          metadata: notification.metadata
        }
      end
    end
  end
end
