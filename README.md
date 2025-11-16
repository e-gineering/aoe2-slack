# AOE2 Slack Bot

A Slack integration bot for Age of Empires II: Definitive Edition that provides player rankings, match history, and statistics directly in your Slack workspace.

## Overview

This bot allows Slack users to query player rankings and match information from Age of Empires II: Definitive Edition using simple slash commands. It integrates with the [AOE2.net API](https://aoe2.net/#api) to fetch real-time player data.

**Key Features:**
- Query player rankings by profile name or Steam ID
- View detailed last match information including teams, civilizations, and results
- Support for profile name aliases (no need to remember Steam IDs)
- Real-time data updates from AOE2.net API

## Technology Stack

- **Framework:** Spring Boot 2.2.7
- **Language:** Java 8
- **Build Tool:** Maven
- **Key Dependencies:**
  - Slack API Client (1.0.7)
  - OkHttp3 for HTTP requests
  - Jackson for JSON processing
  - Spring Boot Actuator for monitoring

## Prerequisites

- Java 8 or higher
- Maven 3.x
- A Slack workspace with admin privileges
- Slack app with slash command capabilities

## Setup

### 1. Slack App Configuration

Create a Slack app and configure a slash command:

1. Go to [Slack API Apps](https://api.slack.com/apps)
2. Create a new app or select an existing one
3. Navigate to "Slash Commands" and create a new command (e.g., `/aoe2`)
4. Set the Request URL to: `https://your-domain.com/api/aoe2`
5. Get your app's OAuth token from "OAuth & Permissions"

For more details, see the [Slack Slash Commands documentation](https://api.slack.com/interactivity/slash-commands).

### 2. Environment Variables

Set the following environment variables:

| Variable | Description | Required |
|----------|-------------|----------|
| `SLACK_TOKEN` | OAuth token from your Slack app | Yes |
| `PROFILE_MAP_SLACK_CHANNEL` | Channel ID containing the profile map pinned message | Yes |
| `SLACK_WEBHOOK_URL` | Webhook URL for posting match results (future feature) | No |

### 3. Profile Map Setup

The bot uses a clever approach to store profile name to Steam ID mappings without requiring a database - it uses a pinned Slack message!

**Steps:**

1. Create or select a Slack channel for storing the profile map
2. Get the channel ID (right-click channel → View channel details)
3. Post a message in that channel with the following format:

```javascript
PROFILE_MAP=[
   {"profileName" : "Arthur", "steamId" : "76561198012345678"},
   {"profileName" : "Ford", "steamId" : "76561198087654321"},
   {"profileName" : "Marvin", "steamId" : "76561198011111111"}
]
```

4. **Pin the message** to the channel
5. Set the `PROFILE_MAP_SLACK_CHANNEL` environment variable to this channel's ID

**Note:** Ensure the JSON is properly formatted. The bot loads this map on startup and when the `refresh-profile-map` command is used.

## Building and Running

### Local Development

```bash
# Build the application
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Or run the JAR directly
java -jar target/aoe2-slack-bot-0.0.1-SNAPSHOT.jar
```

The application will start on port 8080 by default.

## Deployment

### Heroku

This app is designed to work seamlessly with Heroku:

1. Create a new Heroku app:
   ```bash
   heroku create your-app-name
   ```

2. Set environment variables:
   ```bash
   heroku config:set SLACK_TOKEN=your-token
   heroku config:set PROFILE_MAP_SLACK_CHANNEL=your-channel-id
   ```

3. Deploy:
   ```bash
   git push heroku main
   ```

The free Heroku tier is sufficient for most use cases.

### Other Platforms

The application can be deployed to any platform that supports Java applications:
- AWS Elastic Beanstalk
- Google Cloud Platform
- Azure App Service
- Docker containers

## Usage

Once configured, use the following slash commands in Slack:

### Check Player Ranking

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

### View Last Match

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
The match concluded at Fri, 22 May 2020 22:12:42 -0500 after lasting 1:32
Team 1
  vilgareth (Mayans) Rating (1065)
  Frodo (Lithuanians) Rating (984)
Team 2
  Gandalf (Japanese) Rating (1121)
  Golem (Goths) Rating (1061)
```

### Refresh Profile Map

```
/aoe2 refresh-profile-map
```

Reloads the profile name mappings from the pinned Slack message. Use this after updating the profile map.

## Project Structure

```
src/main/java/com/eg/aoe2slackbot/
├── Aoe2SlackBotApplication.java          # Main application entry point
├── SlackBotSlashCommandController.java   # Handles slash commands
├── MatchResultsController.java            # Match results endpoint (future feature)
├── entity/                                # Domain entities
│   ├── Aoe2UserConfig.java               # Profile name to Steam ID mapping
│   ├── Match.java                        # Match data
│   ├── MatchPlayer.java                  # Player in a match
│   ├── MatchResult.java                  # Match result data
│   ├── RatingRecord.java                 # Player rating
│   └── refdata/                          # Reference data (civs, maps, etc.)
├── service/                               # Business logic
│   ├── MatchService.java                 # Match data operations
│   ├── RatingService.java                # Rating operations
│   └── RefDataService.java               # Reference data operations
└── view/                                  # View formatters
    └── SlackMatchResult.java             # Formats match results for Slack
```

## API Dependencies

This bot relies heavily on the [AOE2.net API](https://aoe2.net/#api). Huge thanks to the maintainers of this excellent community resource!

**Key Endpoints Used:**
- Player rankings and ratings
- Match history
- Reference data (civilizations, maps, game types)

## Customization

### Message Formatting

To customize the Slack message formatting, modify the `SlackMatchResult` class or use the [Slack Block Kit Builder](https://api.slack.com/tools/block-kit-builder) to design and test custom layouts.

### Leaderboard Types

The bot currently focuses on "Team Random Map" rankings. To support other leaderboards (1v1, custom scenarios, etc.), modify the service layer to accept leaderboard type parameters.

## Troubleshooting

**Bot not responding:**
- Verify environment variables are set correctly
- Check that the Slack app has proper permissions
- Ensure the request URL in Slack points to your deployed app

**Profile names not working:**
- Verify the profile map message is pinned
- Check the JSON format is valid
- Run `/aoe2 refresh-profile-map` to reload mappings

**Match data not showing:**
- Ensure the player has recent matches on their profile
- Verify the Steam ID is correct
- Check AOE2.net API status

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## License

This project is open source and available for use and modification.

## Acknowledgments

- [AOE2.net API](https://aoe2.net/#api) - Community API for AOE2 statistics
- Slack API team for excellent documentation and tools
- Age of Empires II community
