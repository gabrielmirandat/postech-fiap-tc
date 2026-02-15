# Build stage (Go 1.23 - matches go.mod for hermetic Bazel)
FROM golang:1.23-alpine AS builder
WORKDIR /app
COPY go.mod ./
RUN go mod download
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -o /menu ./cmd/server

# Runtime stage
FROM alpine:3.19
RUN apk --no-cache add ca-certificates
WORKDIR /app
COPY --from=builder /menu .
EXPOSE 8002
ENV HTTP_PORT=8002
ENTRYPOINT ["./menu"]
