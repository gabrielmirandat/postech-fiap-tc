use axum::{response::Json, http::StatusCode};
use crate::models::{Delivery, DeliveryStatus, CreateDeliveryRequest};

pub async fn list_deliveries() -> Json<Vec<Delivery>> {
    // Mock data - in a real application, this would query a database
    let deliveries = vec![
        Delivery {
            id: "1".to_string(),
            order_id: "order-123".to_string(),
            address: "123 Main St".to_string(),
            status: DeliveryStatus::InTransit,
            driver_id: Some("driver-001".to_string()),
        },
    ];

    Json(deliveries)
}

pub async fn create_delivery(
    Json(payload): Json<CreateDeliveryRequest>,
) -> (StatusCode, Json<Delivery>) {
    // Mock creation - in a real application, this would insert into a database
    let delivery = Delivery {
        id: uuid::Uuid::new_v4().to_string(),
        order_id: payload.order_id,
        address: payload.address,
        status: DeliveryStatus::Pending,
        driver_id: None,
    };

    (StatusCode::CREATED, Json(delivery))
}
