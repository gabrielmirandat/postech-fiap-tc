FROM ruby:3.2.0-slim

WORKDIR /app

# Install dependencies
RUN apt-get update && apt-get install -y \
    build-essential \
    libpq-dev \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy Gemfile
COPY Gemfile Gemfile.lock ./

# Install gems
RUN bundle install

# Copy application
COPY . .

# Expose port
EXPOSE 8005

# Start server
CMD ["bundle", "exec", "puma", "-C", "config/puma.rb"]
