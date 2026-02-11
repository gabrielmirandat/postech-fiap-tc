use axum::{
    routing::{get, post},
    Router,
    response::Json,
};
use serde::{Deserialize, Serialize};
use std::net::SocketAddr;
use tracing_subscriber::{layer::SubscriberExt, util::SubscriberInitExt};

mod handlers;
mod models;

#[derive(Serialize)]
struct HealthResponse {
    status: String,
    service: String,
    version: String,
}

async fn health_check() -> Json<HealthResponse> {
    Json(HealthResponse {
        status: "UP".to_string(),
        service: "deliveries".to_string(),
        version: "1.0.0".to_string(),
    })
}

#[tokio::main]
async fn main() {
    // Initialize tracing
    tracing_subscriber::registry()
        .with(
            tracing_subscriber::EnvFilter::try_from_default_env()
                .unwrap_or_else(|_| "deliveries=debug,tower_http=debug".into()),
        )
        .with(tracing_subscriber::fmt::layer())
        .init();

    // Build application routes
    let app = Router::new()
        .route("/health", get(health_check))
        .route("/api/deliveries", get(handlers::list_deliveries))
        .route("/api/deliveries", post(handlers::create_delivery));

    // Run server
    let port = std::env::var("PORT").unwrap_or_else(|_| "8007".to_string());
    let addr: SocketAddr = format!("0.0.0.0:{}", port)
        .parse()
        .expect("Failed to parse address");

    tracing::info!("Deliveries service listening on {}", addr);

    let listener = tokio::net::TcpListener::bind(addr).await.unwrap();
    axum::serve(listener, app).await.unwrap();
}
