# Classic Retro Game (Tetris)

## Project Overview
The primary objective of this game is to arrange falling blocks (known as
Tetrominoes) to form complete horizontal lines. When a line is completed, it disappears, and
the player earns points. You control the falling blocks using the arrow keys: left and right to
move sideways, down to accelerate the fall, and up to rotate the piece. The challenge is to
prevent the blocks from stacking up to the top of the screen.

Checkout [Tetris](https://en.wikipedia.org/wiki/Tetris/) if you want to learn more about the history and impact.

## Project Structure

```
CW2042-CW2025/
├── pom.xml                          # Maven configuration file
├── mvnw / mvnw.cmd                 # Maven wrapper scripts
├── .gitignore                      # Git ignore rules
├── README.md                       #Project documentation
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── comp2042/
│       │           ├── Main.java                    # Application entry point
│       │           ├── GameController.java          # Game logic controller
│       │           ├── GuiController.java           # UI controller
│       │           ├── Board.java                   # Board interface
│       │           ├── SimpleBoard.java             # Board implementation
│       │           ├── Score.java                   # Score management
│       │           ├── MatrixOperations.java        # Matrix utility operations
│       │           ├── BrickRotator.java            # Brick rotation logic
│       │           ├── ClearRow.java                # Row clearing logic
│       │           ├── ViewData.java                # View data model
│       │           ├── MoveEvent.java               # Move event model
│       │           ├── DownData.java                # Down movement data
│       │           ├── NextShapeInfo.java           # Next shape information
│       │           ├── EventSource.java             # Event source enum
│       │           ├── EventType.java               # Event type enum
│       │           ├── InputEventListener.java      # Input event listener interface
│       │           ├── GameOverPanel.java           # Game over UI panel
│       │           ├── NotificationPanel.java       # Notification UI panel
│       │           └── logic/
│       │               └── bricks/
│       │                   ├── Brick.java                    # Brick interface
│       │                   ├── BrickGenerator.java          # Brick generator interface
│       │                   ├── RandomBrickGenerator.java    # Random brick generator
│       │                   ├── IBrick.java                  # I-shaped brick
│       │                   ├── JBrick.java                  # J-shaped brick
│       │                   ├── LBrick.java                  # L-shaped brick
│       │                   ├── OBrick.java                 # O-shaped brick
│       │                   ├── SBrick.java                 # S-shaped brick
│       │                   ├── TBrick.java                 # T-shaped brick
│       │                   └── ZBrick.java                 # Z-shaped brick
│       └── resources/
│           ├── gameLayout.fxml      # FXML UI layout
│           ├── window_style.css     # CSS styling
│           ├── background_image.png # Background image
│           └── digital.ttf          # Custom font
└── target/                         # Compiled classes (generated)
```

## Technology used
- Java: 25.0.1
- Javafx: 25.0.1
- IDE: IntelliJ IDEA


### Build Commands

```bash
# Clean and compile
mvn clean compile

# Run the game
mvn javafx:run

# Or using Maven wrapper (Windows)
mvnw.cmd javafx:run

# Or using Maven wrapper (Unix/Mac)
./mvnw javafx:run

```

## Author
Cui Yi Ning
<br>
student of University of Nottingham Malaysia.


## Version History
| Version | Date             | Change                                                        |
|---------|------------------|---------------------------------------------------------------|
| v1.0    | 12 November 2025 | Initial release                                               |
| v1.1    | 18 November 2025 | fixed Score system, tiles spawn location and matrix operations|
| v1.2    | 25 November 2025 | Added a new feature that shows next three bricks and refactored the needed files|



## Key of changes
### 1. Score Display and Spawn location
- **Location**: `GuiController.java` / `gameLayout.fxml`/ `SimpleBoard.java`
- **Issue**: `bindScore()` method was empty, so the score never appeared in the UI and offset for spawn location is incorrect
- **Fix**: Added a dedicated score label to the FXML layout and bound it to the `Score` property inside `GuiController.bindScore()` and change the offset as y = 0.

### 2. Matrix Indexing Fix
- **Location**: `MatrixOperations.java`
- **Issue**: Brick arrays were indexed with swapped coordinates (`brick[j][i]`), causing incorrect collision detection and merges for asymmetrical shapes.
- **Fix**: Updated `intersect()` and `merge()` loops to compute `targetX`/`targetY` using column/row order and reference `brick[row][col]`

### 3. Show next three bricks
- **Location**: `GuiController.java`/ `GameController.java`/ `SimpleBoard.java`/`GuiController.java`/`gameLayout.fxml`/ `window_style.css`/`ViewData.java`/`BrickGenerator.java`/ `RandomBrickGenerator.java`
- **Addition**: Added a new feature that shows the next three Tetris bricks in the preview panel.
  Refactored the piece generation logic to provide additional upcoming pieces.
  Updated UI layout and rendering to support multiple previews.
  This enhances player visibility and improves strategic gameplay.



## License

This project is for educational purposes only and is not intended for commercial use.

