# aoe2-slack

A Slack bot integration for Age of Empires II: Definitive Edition that provides player rankings, match history, and statistics directly in your Slack workspace.

## Overview

This Spring Boot application integrates with the [aoe2.net API](https://aoe2.net/#api) to fetch player statistics and match results for Age of Empires II: Definitive Edition. It responds to Slack slash commands, allowing team members to quickly check rankings, view match details, and track player performance.

**Note:** This project depends heavily on the [aoe2.net API](https://aoe2.net/#api). Many thanks to the folks maintaining it!

## Features

- **Player Rankings**: Check current Team Random Map rankings for any player
- **Match History**: View detailed information about a player's most recent match including:
  - Match outcome (win/loss)
  - Map name and size
  - Team compositions
  - Player civilizations and ratings
  - Match duration
- **Profile Management**: Map friendly profile names to Steam IDs for easier lookups
- **Profile Map Refresh**: Update the profile mapping on-demand via Slack command

## Prerequisites

- Java 8 or higher
- Maven 3.x
- A Slack workspace with permissions to create apps
- Steam IDs for players you want to track

## Technology Stack

- **Spring Boot 2.2.7** - Application framework
- **Slack API Client 1.0.7** - Slack integration
- **OkHttp** - HTTP client for API calls
- **Jackson** - JSON processing
- **Thymeleaf** - Template engine

## Setup

### Environment Variables

The application requires the following environment variables:

| Variable | Description |
|----------|-------------|
| `SLACK_TOKEN` | Your Slack app's bot token (obtained when registering your Slack app) |
| `SLACK_WEBHOOK_URL` | Incoming webhook URL for posting match results (used by `MatchResultsController` - currently a skeleton for future polling functionality) |
| `PROFILE_MAP_SLACK_CHANNEL` | The channel ID (not name) where the profile mapping is stored as a pinned message |

### Slack App Configuration

1. Create a new Slack app at [https://api.slack.com/apps](https://api.slack.com/apps)
2. Enable slash commands for your app
3. Configure a slash command (e.g., `/aoe2`) pointing to your application's endpoint
4. Install the app to your workspace and note the bot token
5. Set up the required environment variables

See [Slack's slash command documentation](https://api.slack.com/interactivity/slash-commands) for detailed instructions.

### Profile Map Setup

The application uses a unique approach to store player profile mappings: a pinned Slack message contains JSON data mapping friendly names to Steam IDs. This avoids the need for a separate database.

1. In your designated Slack channel (specified by `PROFILE_MAP_SLACK_CHANNEL`), create a message with the following format:

```javascript
PROFILE_MAP=[
   {"profileName": "Arthur", "steamId": "76561195555"},
   {"profileName": "Ford", "steamId": "76561195556"},
   {"profileName": "Marvin", "steamId": "76561195557"},
   {"profileName": "Zaphod", "steamId": "76561195558"}
]
```

2. **Pin this message** to the channel
3. The application will read this data on startup and whenever the `refresh-profile-map` command is used

**Important:** The JSON must be valid and follow the exact format shown above. The application has limited error handling for malformed profile data.

## Installation

### Local Development

```bash
# Clone the repository
git clone https://github.com/egineering-llc/aoe2-slack.git
cd aoe2-slack

# Set environment variables
export SLACK_TOKEN="xoxb-your-token"
export SLACK_WEBHOOK_URL="https://hooks.slack.com/services/YOUR/WEBHOOK/URL"
export PROFILE_MAP_SLACK_CHANNEL="C01234567"

# Build and run
./mvnw spring-boot:run
```

### Heroku Deployment

This application is designed to deploy easily to Heroku (works on a free hobby dyno):

```bash
# Create Heroku app
heroku create your-app-name

# Set environment variables
heroku config:set SLACK_TOKEN="xoxb-your-token"
heroku config:set SLACK_WEBHOOK_URL="https://hooks.slack.com/services/YOUR/WEBHOOK/URL"
heroku config:set PROFILE_MAP_SLACK_CHANNEL="C01234567"

# Deploy
git push heroku main
```

## Usage

### Available Commands

All commands use the `/aoe2` slash command (or whatever you configured in your Slack app).

#### Check Player Ranking

Get the current Team Random Map ranking for a player:

```
/aoe2 [profileName | steamId]
```

**Example:**
```
/aoe2 vilgareth
```

**Response:**
```
Team Random Map Rank: 1005 as of Wed, 20 May 2020 20:51:34 -0500
```

#### View Last Match

Get detailed information about a player's most recent match:

```
/aoe2 last-match [profileName | steamId]
```

**Example:**
```
/aoe2 last-match vilgareth
```

**Response:**
```
vilgareth's last match on leaderboard Team Random Map was a WIN!!!
There were 4 players in the game and the map was Hill Fort of size Medium (4 player).
The match concluded at Fri, 22 May 2020 22:12:42 -0500 after lasting 1:32 Hours

Team 1
vilgareth (Mayans)  Rating (1065)
Frodo (Lithuanians)  Rating (984)

Team 2
Gandalf (Japanese)  Rating (1121)
Golem (Goths)  Rating (1061)
```

#### Refresh Profile Map

Reload the profile name to Steam ID mapping from the pinned message:

```
/aoe2 refresh-profile-map
```

This is useful when you've updated the profile map in Slack and want the bot to pick up the changes without restarting.

## API Endpoints

- `POST /api/aoe2` - Handles slash command requests from Slack
- `GET /api/aoe2/profileMap` - Retrieves and refreshes the profile mapping

## Project Structure

```
src/main/java/com/eg/aoe2slackbot/
├── Aoe2SlackBotApplication.java          # Main application class
├── SlackBotSlashCommandController.java    # Handles slash commands
├── MatchResultsController.java            # Skeleton for future polling functionality
├── entity/                                # Data models
│   ├── Aoe2UserConfig.java
│   ├── Match.java
│   ├── MatchPlayer.java
│   ├── MatchResult.java
│   ├── RatingRecord.java
│   ├── SlackMessage.java
│   ├── SlackSlashCommandResponse.java
│   └── refdata/                           # Reference data models
├── service/                               # Business logic
│   ├── MatchService.java
│   ├── RatingService.java
│   └── RefDataService.java
└── view/                                  # View formatting
    └── SlackMatchResult.java
```

## Tools & Resources

- [Slack Block Kit Builder](https://api.slack.com/tools/block-kit-builder) - Useful for customizing message formatting
- [aoe2.net API Documentation](https://aoe2.net/#api) - Complete API reference
- [Slack API Documentation](https://api.slack.com/) - Slack platform documentation

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## License

This project is open source and available under the [MIT License](LICENSE).

## Acknowledgments

- Special thanks to the [aoe2.net](https://aoe2.net) team for providing and maintaining the API
- Built with Spring Boot and the Slack API
