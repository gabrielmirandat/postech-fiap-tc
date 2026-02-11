use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct Delivery {
    pub id: String,
    pub order_id: String,
    pub address: String,
    pub status: DeliveryStatus,
    pub driver_id: Option<String>,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum DeliveryStatus {
    Pending,
    Assigned,
    InTransit,
    Delivered,
    Failed,
}

#[derive(Debug, Deserialize)]
pub struct CreateDeliveryRequest {
    pub order_id: String,
    pub address: String,
}
