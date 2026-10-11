# 2048 Game — Java

A complete implementation of the classic 2048 tile-merging game in pure Java (Swing).

**Play Online:** Click "Code" → "Codespaces" → "Create codespace on main" → run `cd java/src && javac *.java && java Main`
**GitHub:** https://github.com/Sumitdarkcodes/2048-Game-java-

## Screenshot

![2048 Game](screenshot.png)

## Features

- Complete game logic in 2D array
- Tile sliding, merging, and random spawning
- Win detection (reaching 2048) and game-over detection
- Score tracking with best score
- Colorful Swing UI with keyboard controls
- Zero external libraries — pure Java

## How to Play

Use arrow keys (↑ ↓ ← →) to slide tiles. When two tiles with the same number touch, they merge into one. Reach 2048 to win!

Press **R** to restart at any time.

## Run Locally

Requires Java 17+:

```bash
cd java/src
javac *.java
java Main
