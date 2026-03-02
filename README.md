BBB Predict Bot 🤖📊

BBB Predict Bot is an automated service built with Spring Boot that calculates and daily posts the win probabilities of Big Brother Brasil 26 (BBB26) contestants on X (formerly Twitter). It gathers real-time prediction market data from Polymarket, calculates the odds, and automatically generates tweets featuring the contestant's photo, win probability, and percentage variation.
✨ Features

    Automated Daily Tweets: Runs a scheduled job every day to post predictions on X for all active contestants.

    Polymarket Integration: Consumes the Polymarket CLOB API to fetch real-time buy and sell token prices.

    Probability Calculation: Calculates the midpoint probability based on market prices and tracks historical variations.

    Image Uploads: Automatically downloads contestant photos and attaches them to the generated tweets.

    REST API: Provides secure endpoints to manage contestants and track their probability histories.

    API Key Security: All API endpoints are protected using a custom API Key interceptor.

🛠️ Tech Stack

    Java 25

    Spring Boot (Web, Data JPA, Validation, Scheduling)

    PostgreSQL (Database)

    MapStruct (Object mapping)

    ScribeJava (Twitter OAuth 1.0a and v2 API integration)

    Docker & Docker Compose (Containerization)

⚙️ Prerequisites

To run this project, you need:

    Docker and Docker Compose installed.

    An X/Twitter Developer account with Read and Write permissions (to generate API Keys and Tokens).

    A PostgreSQL database (if running outside of Docker).

🚀 Environment Variables

The application requires several environment variables to run properly. If using Docker Compose, create an .env file in the root directory:
Snippet de código

# API Security
API_KEY=your_secure_api_key

# Database Configuration
DB_URL=jdbc:postgresql://your_db_host:5432/your_db_name
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password

# Twitter/X API Credentials
TWITTER_API_KEY=your_twitter_api_key
TWITTER_API_SECRET=your_twitter_api_secret
TWITTER_ACCESS_TOKEN=your_twitter_access_token
TWITTER_ACCESS_SECRET=your_twitter_access_secret

🐳 Running with Docker

The easiest way to start the application is via Docker Compose.

    Clone the repository.

    Ensure your .env file is set up with the variables listed above.

    Build and run the container:
    Bash

    docker-compose up -d --build

    The API will be available at http://localhost:8080.

📡 API Endpoints

Note: All endpoints require an API_KEY header matching the key set in your environment variables.
Contestants (/contestants)

    GET /contestants - List all contestants.

    GET /contestants/{id} - Get a specific contestant by ID.

    POST /contestants - Add a new contestant (requires name and clobTokenId).

    PATCH /contestants/{id} - Update a contestant's details (e.g., photo URL or Polymarket token ID).

    DELETE /contestants/{id} - Deactivate a contestant.

Probability History (/probability-history)

    GET /probability-history - List all historical probability records.

    GET /probability-history/{id} - Get a specific record.

    GET /probability-history/latest-two/{name} - Get the two latest probabilities for a specific contestant to calculate variations.

    POST /probability-history/force-update - Force the system to fetch and update probabilities for all active contestants immediately.

    POST /probability-history/{contestantId} - Manually save the current win probability for a specific contestant.

🕒 Scheduling Routine

The bot features a built-in scheduler (PredictionScheduler.java) configured to run automatically every day at 14:05 (America/Sao_Paulo timezone).

To prevent API rate limits and provide organic activity, the bot calculates a random delay interval (between 4 and 10 minutes) between each published tweet.
📄 License

This project is open-source. Please refer to the Apache License 2.0 headers within the project files.
