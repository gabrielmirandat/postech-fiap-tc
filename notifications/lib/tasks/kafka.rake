namespace :kafka do
  desc "Start Kafka consumer for order events"
  task consumer: :environment do
    puts "Starting Kafka consumer..."
    OrderEventConsumerJob.perform_now
  end
end
