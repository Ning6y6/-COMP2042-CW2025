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
├── README.md                       # Project documentation
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── comp2042/
│       │           ├── Main.java                    # Application entry point
│       │           ├── GameController.java          # Game logic controller
│       │           ├── GameConstants.java          # Game constants and configuration
│       │           ├── event/
│       │           │   ├── ClearRow.java            # Row clearing logic
│       │           │   ├── DownData.java            # Down movement data
│       │           │   ├── EventSource.java         # Event source enum
│       │           │   ├── EventType.java           # Event type enum
│       │           │   ├── GameInputHandler.java    # Input event handler
│       │           │   ├── MoveEvent.java           # Move event model
│       │           │   ├── NextShapeInfo.java       # Next shape information
│       │           │   └── ViewData.java            # View data model
│       │           ├── logic/
│       │           │   ├── Board.java               # Board interface
│       │           │   ├── BrickRotator.java        # Brick rotation logic
│       │           │   ├── GameBoard.java           # Board implementation
│       │           │   ├── MatrixOperations.java    # Matrix utility operations
│       │           │   ├── Score.java               # Score management
│       │           │   └── bricks/
│       │           │       ├── Brick.java                    # Brick interface
│       │           │       ├── BrickGenerator.java          # Brick generator interface
│       │           │       ├── BrickType.java               # Brick type enum
│       │           │       ├── RandomBrickGenerator.java    # Random brick generator
│       │           │       ├── IBrick.java                  # I-shaped brick
│       │           │       ├── JBrick.java                  # J-shaped brick
│       │           │       ├── LBrick.java                  # L-shaped brick
│       │           │       ├── OBrick.java                  # O-shaped brick
│       │           │       ├── SBrick.java                  # S-shaped brick
│       │           │       ├── TBrick.java                  # T-shaped brick
│       │           │       └── ZBrick.java                  # Z-shaped brick
│       │           └── ui/
│       │               ├── GameOverPanel.java        # Game over UI panel
│       │               ├── GameView.java             # Main game view UI
│       │               ├── NextPieceView.java        # Next piece preview UI
│       │               └── NotificationPanel.java    # Notification UI panel
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
| Version | Date             | Change                                                                           |
|---------|------------------|----------------------------------------------------------------------------------|
| v1.0    | 12 November 2025 | Initial release                                                                  |
| v1.1    | 18 November 2025 | fixed Score system, tiles spawn location and matrix operations                   |
| v1.2    | 25 November 2025 | Added a new feature that shows next three bricks and refactored the needed files |
| v1.3    | 29 November 2025 | Major refactoring for maintainability, Encapsulation, and SOLID principles.      |


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

### 4. Encapsulation and Data Integrity
- **Location**: `GameBoard.java`
- **Issue**: Exposed mutable state (`java.awt.Point currentOffset`) and returned a direct reference to the internal game matrix (`currentGameMatrix`).
- **Fix**: Replaced `currentOffset` with private primitive integers (`currentX`, `currentY`) to prevent external state modification. `getBoardMatrix()` now returns a **deep copy** of the matrix.

### 5. Single Responsibility Principle (SRP)
- **Location**: `GameView.java` / `NextPieceView.java`
- **Issue**: previous`GuiController.java` handled both the main board rendering and complex preview panel logic.
- **Fix**: Extracted all next piece rendering logic and fields into the dedicated `NextPieceView` class. and renamed to `GameView.java`.

### 6. Magic Numbers and Type Code Elimination
- **Location**: `GameController.java` / `GameBoard.java` / `GameView.java` / `NextPieceView.java` / `RandomBrickGenerator.java`
- **Issue**: Hardcoded values (e.g., board size 10/25, brick size 20, color logic switch statements) were scattered throughout the codebase (Magic Number and Duplicated Code smells).
- **Fix**: Introduced **`GameConstants.java`** to centralize all numeric literals and **`BrickType.java`** (enum) to handle color mapping, eliminating duplicative `switch` statements from UI classes (Replace Type Code with Polymorphism/Enum).



### Design Pattern Overview
## The project currently implements two key design patterns effectively:

- Factory Method Pattern (Creational): The BrickGenerator interface is implemented by `RandomBrickGenerator.java`. This pattern delegates the logic of creating new concrete Brick objects (like IBrick, JBrick, etc.) to subclasses without the client (`GameBoard`) needing to know the instantiation details.

- Observer Pattern (JavaFX) (Behavioral): The `Score.java` class uses IntegerProperty and `GameView.bindScore()` uses `textProperty().bind()`. This establishes a one-to-many relationship where the scoreLabel automatically updates whenever the Score object's internal value changes, without direct polling.

- Strategy Pattern (Simplified): The `BrickType` enum acts as a simplified Strategy, where the client requests a color using an ID, and the enum provides the specific color (strategy) based on the input type.

## License

This project is for educational purposes only and is not intended for commercial use.

