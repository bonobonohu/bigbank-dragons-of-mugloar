# Bigbank: Dragons of Mugloar challenge

https://www.dragonsofmugloar.com/

(Choose "Scripting adventure / backend")

## How to start the game

`./gradlew bootRun`

## Configuration

### Runner

- `runner.no-of-characters`: How many threads should run parallel
- `runner.character-names`: Names pool for the character names to choose from randomly
- `runner.max-runs`: How many iterations we would like to run (In one iteration multiple actions can happen)

### Player

- `player.purchase-lives-threshold`: Below (inclusive) how many lives we should try to purchase new lives (healing potions)
- `player.extra-lives`: How many extra lives should be purchased

### DungeonMaster

- `dungeon-master.verbose-logging`: Turn on verbose logging or leave it false for less noise

## CSV logs

/game_logs

## Test coverage

- http://localhost:63342/dragons/build/reports/jacoco/test/html/index.html
- Minimum requirements: 90% (Instruction/Line/Branch)

## Misc

- Checkstyle; based on Sun configuration (see: https://checkstyle.sourceforge.io/sun_style.html)