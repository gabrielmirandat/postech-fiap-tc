module Api
  module V1
    class NotificationsController < ApplicationController
      before_action :set_notification, only: [:show]

      def index
        @notifications = Notification.order(created_at: :desc)
        @notifications = @notifications.where(status: params[:status]) if params[:status].present?
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

      def by_order
        @notifications = Notification.by_order(params[:order_id])
        render json: {
          order_id: params[:order_id],
          notifications: @notifications.map { |n| notification_json(n) }
        }
      end

      def create
        @notification = Notification.new(notification_params)
        
        if @notification.save
          # Trigger sending
          case @notification.notification_type
          when Notification::TYPE_EMAIL
            EmailNotificationSender.new(@notification).send
          when Notification::TYPE_SMS
            SmsNotificationSender.new(@notification).send
          end
          
          render json: notification_json(@notification), status: :created
        else
          render json: { errors: @notification.errors.full_messages }, status: :unprocessable_entity
        end
      end

      private

      def set_notification
        @notification = Notification.find(params[:id])
      end

      def notification_params
        params.require(:notification).permit(
          :order_id, :notification_type, :contact_type, :contact_value,
          :message, :event_type, :event_id, metadata: {}
        )
      end

      def notification_json(notification)
        {
          id: notification.id,
          order_id: notification.order_id,
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
