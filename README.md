# aoe2-slack
Slack Integration for AOE2, Definitive Edition

This is VERY VERY dependent on this API:  https://aoe2.net/#api.  Many thanks to the folks maintaining it!

### Setup
This is a standard Spring Boot app and requires the following environment variables to be set:
- SLACK_TOKEN - You get this when you register an app
- SLACK_WEBHOOK_URL - This is used in the `MatchResultsController`, which is NOT used for the slash commands and is currently 
just a skeleton that posts match results by hitting the API endpoint.  The thought was that this could be triggered by a timer
or by a background process that would poll the AOE2.net API and post results to a channel.
- PROFILE_MAP_SLACK_CHANNEL - This is the channel id (not name) that contains the profile map data (see below)

## Profile Map
In order to be able to use profile names instead of just steam id values, a lookup table is needed.  In 
order to avoid introducing a dependency on a datasource, a __pinned message__ is used to store the data.
Yes, you read that correctly:  A slack message is used to store data. It __is__ slightly crazy, but works great.

The data is JSON data and should be of the following form:

```javascript
PROFILE_MAP=[
   {"profileName" : "Arthur", "steamId" : "76561195555"},
   {"profileName" : "Ford", "steamId" : "76561195556"},
   {"profileName" : "Marvin", "steamId" : "76561195557"},
   {"profileName" : "zaphod", "steamId" : "76561195558"}
]
```

The code isn't currently very tolerant of errors, so beware.  Make sure you PIN the message to the channel specified by
the `PROFILE_MAP_SLACK_CHANNEL` environment variable listed above.

__Heroku__ is super simple to deploy this (and free if you use a hobby dyno).

### Slack Slash Command Support
This app responds to a single slash command that you configure on Slack. `/aoe2` is used below, but you can make it
whatever you want as long as it doesn't clash with other apps.  

https://api.slack.com/interactivity/slash-commands

#### Rank
The first is just the slash command followed by the steam id or profile name
`/aoe2 [profilename | steamId]` - Shows rank info

`/aoe2 vilgareth`

*AOE ServiceAPP Team Random Map Rank: 1005 as of Wed, 20 May 2020 20:51:34 -0500*

#### Last Match
The second shows information about the last match of the player and has the form:

`/aoe2 last-match vilgareth`

*vilgareth's last match on leaderboard Team Random Map was a WIN!!!  
There were 4 players in the game and the map was Hill Fort of size Medium (4 player).  
The match concluded at Fri, 22 May 2020 22:12:42 -0500 after lasting 1.32 Hours
Team 1
vilgareth (Mayans)  Rating (1065)
Frodo (Lithuanians)  Rating (984)
Team 2
Gandalf (Japanese)  Rating (1121)
Golem (Goths)  Rating (1061)*

#### Last Match
To refresh the profile name -> steam id map that's stored in the pinned message:

`/aoe2 refresh-profile-map`

If you want to change the formatting on the message, this is a very handy tool:

https://api.slack.com/tools/block-kit-builder
