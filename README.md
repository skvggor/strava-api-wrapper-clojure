# strava-api-wrapper-clojure (WIP)

[![wakatime](https://wakatime.com/badge/user/6fd82e7b-8d36-4419-85dc-58cd397667a8/project/0cc5c842-0060-4667-b377-78b66c418b6d.svg)](https://wakatime.com/badge/user/6fd82e7b-8d36-4419-85dc-58cd397667a8/project/0cc5c842-0060-4667-b377-78b66c418b6d) [![CI and Deploy](https://github.com/skvggor/strava-api-wrapper-clojure/actions/workflows/ci-and-deploy.yml/badge.svg)](https://github.com/skvggor/strava-api-wrapper-clojure/actions/workflows/ci-and-deploy.yml)

I developed a program that integrates with the Strava API to retrieve and display total distance for the current year. By connecting to your Strava account, it fetches the necessary data and outputs the total distance for different sports, providing an easy way to track annual progress. I created this project as a way to learn and explore the [Clojure](https://clojure.org/) programming language.

## Features

- Retrieve total distance for current year
- Support for multiple sports (run, ride)
- RESTful API with JSON responses
- Authentication via refresh token

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/skvggor/strava-api-wrapper-clojure.git
   cd strava-api-wrapper-clojure
   ```

2. Create a `config.edn` file based on `config.edn.example`:
   ```clojure
   {:strava-domain "https://www.strava.com"
    :strava-api-url "/api/v3"
    :strava-client-id "YOUR_CLIENT_ID"
    :strava-client-secret "YOUR_CLIENT_SECRET"
    :strava-refresh-token "YOUR_REFRESH_TOKEN"
    :strava-user-id "YOUR_USER_ID"
    :port 3000}
   ```

3. Install dependencies:
   ```bash
   lein deps
   ```

## Usage

Start the server:
```bash
bash start.sh
```

Or manually:
```bash
lein run
```

The server will start on port 3000 (or the port specified in config.edn).

## Docker Deployment

For production deployment with Docker and Caddy reverse proxy, create a `compose.yml` file:

```yaml
services:
  strava-api:
    build:
      context: .
      dockerfile: Dockerfile
    restart: unless-stopped
    networks:
      - caddy_net
    labels:
      - caddy=your-subdomain.yourdomain.com
      - caddy.reverse_proxy={{upstreams 3002}}
      - caddy.encode=zstd gzip
    expose:
      - 3002
    volumes:
      - ./config.edn:/usr/src/app/config.edn

networks:
  caddy_net:
    external: true
```

Replace `your-subdomain.yourdomain.com` with your actual domain. Ensure your `config.edn` file is present and properly configured.

## API Endpoints

### Get Total Distance

Retrieve total distance for the current year.

**Endpoint:** `GET /api/v1/strava/total-distance/current-year`

**Query Parameters:**
- `sport` (optional): Sport type. Supported values: `run` (default), `ride`

**Examples:**
```bash
# Get running distance (default)
curl http://localhost:3000/api/v1/strava/total-distance/current-year

# Get running distance
curl http://localhost:3000/api/v1/strava/total-distance/current-year?sport=run

# Get cycling distance
curl http://localhost:3000/api/v1/strava/total-distance/current-year?sport=ride
```

**Response:**
```json
{
  "distance": 123.4
}
```

## Running Tests

```bash
lein test
```

## Project Structure

```
strava-api-wrapper-clojure/
├── src/
│   └── strava_api/
│       ├── core.clj         # Main application and server setup
│       ├── handlers.clj     # HTTP request handlers
│       ├── strava.clj       # Strava API integration
│       └── utils.clj        # Utility functions
├── test/
│   └── strava_api/
│       └── utils_test.clj   # Unit tests
├── config.edn               # Configuration file (not tracked)
├── config.edn.example       # Example configuration
└── start.sh                 # Script to start the server
```

## Tech Stack

- **Clojure 1.12.4** - Functional programming language
- **Compojure** - Routing library
- **http-kit** - HTTP server
- **clj-http** - HTTP client
- **cheshire** - JSON parsing

## License

This project is licensed under the Eclipse Public License - v 2.0.

