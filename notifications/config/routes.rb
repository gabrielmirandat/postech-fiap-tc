Rails.application.routes.draw do
  namespace :api do
    namespace :v1 do
      resources :notifications, only: [:index, :show, :create]
      get 'notifications/order/:order_id', to: 'notifications#by_order', as: 'notifications_by_order'
    end
  end
end
