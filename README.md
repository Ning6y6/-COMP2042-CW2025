# Classic Retro Game (Tetris)

## Project Overview
The primary objective of this game is to arrange falling blocks (known as
Tetrominoes) to form complete horizontal lines. When a line is completed, it disappears, and
the player earns points. You control the falling blocks using the WASD keys: A and D to
move sideways, S to accelerate the fall, and W to rotate the piece. The challenge is to
prevent the blocks from stacking up to the top of the screen.

**Controls:**

Default keybind
- **A** / **D**: Move left / right
- **W**: Rotate piece
- **S**: Soft drop (accelerate fall)
- **P**: Pause game
- **N**: Restart
- **B**: Hold piece
- **SpaceBar**: Hard Drop
- **Settings Button**: Access game settings menu

Checkout [Tetris](https://en.wikipedia.org/wiki/Tetris/) if you want to learn more about the history and impact.

github link :https://github.com/Ning6y6/-COMP2042-CW2025
## Project Structure

```
COMP2042-CW2025/
├── pom.xml                          # Maven configuration file
├── mvnw / mvnw.cmd                 # Maven wrapper scripts
├── .gitignore                      # Git ignore rules
├── README.md                       # Project documentation
├── ERROR_FIXES_SUMMARY.md          # Error fixes and refactoring documentation
├── leaderboard.txt                 # Leaderboard data file
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── comp2042/
│       │           ├── Main.java                    # Application entry point
│       │           ├── GameController.java          # Game logic controller
│       │           ├── GameConstants.java           # Game constants and configuration
│       │           ├── GameLoop.java                # Game loop interface
│       │           ├── event/
│       │           │   ├── ClearRow.java            # Row clearing event data
│       │           │   ├── NextShapeInfo.java       # Next shape rotation information
│       │           │   └── ViewData.java           # View data model for UI rendering
│       │           ├── input/
│       │           │   ├── GameCommand.java         # Command interface (Command Pattern)
│       │           │   ├── InputManager.java         # Input key binding manager
│       │           │   ├── DuplicateKeyBindingException.java  # Custom exception for duplicate bindings
│       │           │   └── commands/
│       │           │       ├── HardDropCommand.java    # Hard drop command
│       │           │       ├── HoldCommand.java         # Hold piece command
│       │           │       ├── MoveLeftCommand.java    # Move left command
│       │           │       ├── MoveRightCommand.java   # Move right command
│       │           │       ├── PauseCommand.java       # Pause command
│       │           │       ├── RestartGameCommand.java # Restart command
│       │           │       ├── RotateCommand.java      # Rotate command
│       │           │       └── SoftDropCommand.java   # Soft drop command
│       │           ├── leaderboard/
│       │           │   ├── LeaderboardDAO.java         # Data access object interface
│       │           │   ├── FileLeaderboardDAO.java     # File-based DAO implementation
│       │           │   ├── LeaderboardEntry.java       # Leaderboard entry model
│       │           │   ├── LeaderboardEntryParser.java # Parser for txt file data
│       │           │   └── LeaderboardService.java     # Leaderboard business logic
│       │           ├── logic/
│       │           │   ├── Board.java                # Board interface
│       │           │   ├── BrickRotator.java         # Brick rotation logic
│       │           │   ├── GameBoard.java            # Board implementation
│       │           │   ├── MatrixOperations.java     # Matrix utility operations
│       │           │   ├── Score.java                # Score management
│       │           │   ├── ChallengeModeManager.java # Challenge mode game logic
│       │           │   ├── GameMode.java              # Game mode enum (ENDLESS, CHALLENGE)
│       │           │   ├── HoldManager.java           # Hold piece management
│       │           │   ├── WallKickTable.java          # Wall kick offset table
│       │           │   └── bricks/
│       │           │       ├── Brick.java             # Brick interface
│       │           │       ├── BrickGenerator.java   # Brick generator interface (Factory Method)
│       │           │       ├── BrickType.java        # Brick type enum (Strategy Pattern)
│       │           │       ├── RandomBrickGenerator.java  # Random brick generator
│       │           │       ├── IBrick.java           # I-shaped brick
│       │           │       ├── JBrick.java           # J-shaped brick
│       │           │       ├── LBrick.java           # L-shaped brick
│       │           │       ├── OBrick.java           # O-shaped brick
│       │           │       ├── SBrick.java           # S-shaped brick
│       │           │       ├── TBrick.java           # T-shaped brick
│       │           │       └── ZBrick.java           # Z-shaped brick
│       │           ├── state/
│       │           │   ├── GameState.java            # Game state interface (State Pattern)
│       │           │   ├── PlayingState.java         # Playing state implementation
│       │           │   ├── PausedState.java          # Paused state implementation
│       │           │   └── GameOverState.java        # Game over state implementation
│       │           ├── ui/
│       │           │   ├── AudioManager.java          # Audio management (Singleton)
│       │           │   ├── GameFXController.java      # Main game view controller
│       │           │   ├── GameOverFXController.java  # Game over screen controller
│       │           │   ├── GameOverView.java          # Game over screen view
│       │           │   ├── HoldView.java              # Hold piece preview UI
│       │           │   ├── JavaFXGameLoop.java        # JavaFX game loop implementation
│       │           │   ├── KeybindsFXController.java  # Key bindings controller
│       │           │   ├── KeybindsView.java          # Key binding configuration UI
│       │           │   ├── LeaderboardFXController.java # Leaderboard controller
│       │           │   ├── LeaderboardView.java       # Leaderboard display UI
│       │           │   ├── MainMenuController.java    # Main menu controller
│       │           │   ├── ModeSelectionFXController.java # Mode selection controller
│       │           │   ├── ModeSelectionView.java     # Mode selection view
│       │           │   ├── NextPieceView.java        # Next piece preview UI
│       │           │   ├── NotificationPanel.java    # Notification UI panel
│       │           │   ├── OptionsFXController.java  # Options menu controller
│       │           │   ├── OptionsView.java          # Options menu view
│       │           │   ├── SettingsFXController.java # Settings menu controller
│       │           │   ├── SettingsView.java         # Settings menu UI (Facade Pattern)
│       │           │   ├── VictoryFXController.java    # Victory screen controller
│       │           │   └── VictoryView.java           # Victory screen view
│       │           └── viewmodel/
│       │               └── GameViewModel.java         # ViewModel for data binding (MVVM)
│       └── resources/
│           ├── fxml/
│           │   ├── GameOverView.fxml      # Game over screen FXML
│           │   ├── GameView.fxml          # Main game view FXML
│           │   ├── KeybindsView.fxml      # Key bindings FXML
│           │   ├── LeaderboardView.fxml   # Leaderboard FXML
│           │   ├── main_menu.fxml         # Main menu FXML
│           │   ├── ModeSelectionView.fxml # Mode selection FXML
│           │   ├── OptionsView.fxml      # Options menu FXML
│           │   ├── SettingsView.fxml      # Settings menu FXML
│           │   └── VictoryView.fxml       # Victory screen FXML
│           ├── assets/
│           │   ├── audio/
│           │   │   └── bgm.mp3            # Background music
│           │   ├── font/
│           │   │   └── main.otf           # Custom font (not yet implement)
│           │   └── images/
│           │       ├── level123.jpg      # Background image for levels 1-3
│           │       ├── level4.jpg        # Background image for level 4
│           │       └── MainMenu.png      # Main menu background
│           └── window_style.css         # CSS styling
│   └── test/
│       └── java/
│           └── com/
│               └── comp2042/
│                   ├── GameControllerTest.java
│                   ├── JavaFXTestBase.java
│                   ├── event/
│                   │   ├── ClearRowTest.java
│                   │   └── ViewDataTest.java
│                   ├── input/
│                   │   └── InputManagerTest.java
│                   ├── leaderboard/
│                   │   ├── LeaderboardEntryParserTest.java
│                   │   ├── LeaderboardEntryTest.java
│                   │   └── LeaderboardServiceTest.java
│                   ├── logic/
│                   │   ├── BrickRotatorTest.java
│                   │   ├── ChallengeModeManagerTest.java
│                   │   ├── GameBoardTest.java
│                   │   ├── HoldManagerTest.java
│                   │   ├── MatrixOperationsTest.java
│                   │   ├── ScoreTest.java
│                   │   ├── WallKickTableTest.java
│                   │   └── bricks/
│                   │       ├── IBrickTest.java
│                   │       ├── JBrickTest.java
│                   │       ├── LBrickTest.java
│                   │       ├── RandomBrickGeneratorTest.java
│                   │       ├── SBrickTest.java
│                   │       ├── TBrickTest.java
│                   │       └── ZBrickTest.java
│                   ├── state/
│                   │   ├── GameOverStateTest.java
│                   │   ├── PausedStateTest.java
│                   │   └── PlayingStateTest.java
│                   └── ui/
│                       └── JavaFXGameLoopTest.java
└── target/                            # Compiled classes (generated)
```

## Technology used
- Java: 25.0.1
- Javafx: 25.0.1
- IDE: IntelliJ IDEA


## Compilation  Instructions

### Prerequisites
- Java 17 or later

- Maven (recommended)

- JavaFX SDK (only required if not using Maven JavaFX plugin)

### How to Compile and Run
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


## Implemented and Working Properly

### Below are the features that are fully implemented and function correctly:

#### Complete MVC + ViewModel Architecture
- Logic (GameBoard, Brick, MatrixOperations) separated from UI.
- Controller mediates between logic and view.
- ViewModel passes clean state to JavaFX.

#### Ghost Piece
- Highlights the landing position.
- Fully independent from actual board matrix.
- No longer interferes with collision or line clearing.

#### Hold System
- Player can store a piece.
- Swapping allowed once per drop cycle.
- Matches modern Tetris rules.

#### Hard Drop
- Instantly locks the piece at ghost position.
- Smooth, no lag, and no double-locking.

#### Wall-Kick for Custom Tail Bricks
- Allows rotation even next to walls.
- Works with non-standard shapes.

#### Game Over Screen
- Proper state detection and UI display.
- Fully responsive.

#### Victory Screen
- Proper state detection and UI display.
- Fully responsive.

#### Key Rebinding System (Achievement #1)
- All actions (movement, rotation, soft drop, hard drop, hold) can be modified.
- Clean decoupling between logic and input system.
- Fully functional UI for remapping keys.

#### Challenge Mode (Achievement #2)
- Increased fall speed.
- Time pressure gameplay.
- Revert movement keys
- Clean mode-handling logic inside controller.

## Implemented but Not Working Properly

### Below are the features that are implemented but not working properly:
#### Dynamic Window Resizing
- It is working properly but turn it off.
- Game board centers automatically.
- UI elements reposition correctly.
- Sacrifice for the main menu and in-game background to avoid the image being stretch and affect the aesthetic looks of the game.

## Features Not Implemented
### Below are the features that are not implemented:
#### Custom font for the game
- included in the game file
- not implemented due to time constraints.

## New Java Classes Introduced

### These files are the new java classes that implement in my code:

#### 1. GameConstants.java
- Location: src/main/java/com/comp2042/
- Defines game-wide constants such as speeds, board size, timing, colors, or feature flags.

#### 2. GameController.java
- Location: src/main/java/com/comp2042/
- Handles the new MVC-style controller logic introduced in my refactoring (Lecture 12–13).
- Manages interaction between model, view, and user inputs.

#### 3. GameLoop.java
- Location: src/main/java/com/comp2042/
- Implements the game’s main loop with consistent timing, frame refresh, and update logic.

#### 4. Main.java
- Location: src/main/java/com/comp2042/
New application entry point. Replaces the old launcher structure.

### Event System — New Classes
- Location: src/main/java/com/comp2042/event
- I introduced a new event-driven architecture:

#### 5. ClearRow.java
- Signals when rows are cleared.

#### 6. NextShapeInfo.java
- Encapsulates next-shape preview information.

#### 7. ViewData.java
- Passes rendering data to the UI without exposing internal logic.

### Input System — New Classes
- Location:src/main/java/com/comp2042/input
- I added a complete keybinding + command architecture.

#### 8. DuplicateKeyBindingException.java
- Custom exception for conflicting key bindings.

#### 9. GameCommand.java
Base interface for all input commands.

#### 10. InputManager.java
Handles dynamic keybinding, key-mapping, and command dispatch.

#### Commands Classes (input/commands/)
11. HardDropCommand.java
12. HoldCommand.java 
13. MoveLeftCommand.java 
14. MoveRightCommand.java 
15. PauseCommand.java 
16. RestartGameCommand.java 
17. RotateCommand.java 
18. SoftDropCommand.java

### Leaderboard System - New Classes
- location: src/main/java/com/comp2042/leaderboard
- I introduced a new leaderboard system:

#### 19. FileLeaderboardDAO.java     
- File-based DAO implementation

#### 20. LeaderboardEntry.java       
- Leaderboard entry model

#### 21. LeaderboardEntryParser.java 
- Parser for txt file data 

#### 22. LeaderboardService.java    
- Leaderboard business logic

### UI - New Classes
- location: src/main/java/com/comp2042/ui
- I Introduce a new classes for ui:

#### 23. AudioManager.java
- Audio management (Singleton)
#### 24. GameFXController.java 
- Main game view controller
#### 25. GameOverFXController.java  
- Game over screen controller
#### 26. GameOverView.java       
- Game over screen view
#### 27. HoldView.java              
- Hold piece preview UI
#### 28. JavaFXGameLoop.java 
- JavaFX game loop implementation
#### 29. KeybindsFXController.java  
- Key bindings controller
#### 30. KeybindsView.java          
Key binding configuration UI
#### 31. LeaderboardFXController.java 
- Leaderboard controller
#### 32. LeaderboardView.java       
Leaderboard display UI
#### 33. MainMenuController.java    
- Main menu controller
#### 34. ModeSelectionFXController.java 
- Mode selection controller
#### 35. ModeSelectionView.java     
- Mode selection view
#### 36. NextPieceView.java        
- Next piece preview UI
#### 37. NotificationPanel.java    
- Notification UI panel
#### 38. OptionsFXController.java  
- Options menu controller
#### 39. OptionsView.java          
- Options menu view
#### 40. SettingsFXController.java 
- Settings menu controller
#### 41. SettingsView.java         
Settings menu UI (Facade Pattern)
#### 42. VictoryFXController.java    
Victory screen controller
#### 43. VictoryView.java      
- Victory screen view

### View Model - New Classes
- location: src/main/java/com/comp2042/ViewModel
- I Introduced a new View Model Classes

#### 44. GameViewModel.java
- ViewModel for data binding (MVVM)

## Modified Java Classes

### 1. Brick & Shape Logic
- location: src/main/java/com/comp2042/logic
#### Adjusted to support:
- Ghost brick projection
- Hard drop calculations
- Hold mechanic
- Challenge mode behavior

### 2. Board / Collision Logic
- location: src/main/java/com/comp2042/logic
#### Adjusted to support:
- Separated into cleaner model classes
- Improved update flow via controller
- Added snapshot logic for ghost brick (partial)

### 3. UI Rendering
- location: src/main/java/com/comp2042/ui
#### Adjusted to support:
- Rebuilt around ViewData
- Separated view from model 

### 4. Input Handling
- location: src/main/java/com/comp2042/input
#### Adjusted to support:
- Original key listeners replaced by command pattern
- Supports dynamic rebinding and robust key mapping

## Unexpected Problems
### 1. Ghost Brick Interference
- Calculations reused the same board reference, causing interference with real landed pieces.
##### Solution (partial):
- Began shifting ghost computations into independent board snapshots.


### 2. Complexity from Refactoring
#### Migrating from a simple monolithic structure to MVC introduced:
- Multiple dependencies 
- Need for careful state synchronization
- Event-driven rendering changes
- Resolved through incremental restructuring and testing.

### 3. Layout Drift During Dynamic Resizing
- UI scaling with image background ruining the looks of the game
#### Addressed by:
- Breaking UI into smaller view components
- Static size of window



## Version History
| Version | Date             | Change                                                                                                       |
|---------|------------------|--------------------------------------------------------------------------------------------------------------|
| v1.0    | 12 November 2025 | Initial release                                                                                              |
| v1.1    | 18 November 2025 | Fixed Score system, tiles spawn location and matrix operations                                               |
| v1.2    | 25 November 2025 | Added a new feature that shows next three bricks and refactored the needed files                             |
| v1.3    | 29 November 2025 | Major refactoring for maintainability, Encapsulation, and SOLID principles                                   |
| v1.4    | 3 December 2025  | Added Settings button, key binding validation, auto-resume, WASD-only controls, and comprehensive error fixes |
| v1.5    | 5 December 2025  | Added Hold system, Hard Drop mechanic, and Notification Panel UI with animation                              |
|  v1.6   | 6 December 2025  | Added GhostBrick, Wall Kick implmentation and Dynamic Window                                                 |
 | v1.7  | 8 December 2025   | Added Challenge Mode and updated the game UI to BLACK GOTHIC FANTASY STYLE                                   |
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

### 7. Introduction of Hold Mechanic
- **Location**:`HoldView.java` / `GameController.java` / `GameView.java` / `ViewData.java`
- **Addition**:Implemented a full Tetris-standard hold system, allowing players to store the active tetromino. Hold is limited to once per drop cycle (prevents infinite swapping abuse). Controller fully manages swapping, resetting active piece position, and rotation restoration. HoldView added as a dedicated MVC-compliant UI class.

### 8. Introduction of Hard Drop Mechanic
- **Location**: `GameController.java` / `InputManager.java` / `commands/HardDropCommand.java`
- **Addition**:Added a new Hard Drop command (default Space key) using the Command Pattern. Instantly places the active brick at the lowest valid position. Awards bonus points per dropped row. Automatically locks the brick, triggers line clear detection, and proceeds to next piece. Implemented safely in PlayingState only—Paused and GameOver states ignore the input.

### 9. NotificationPanel + Animated Score Popups
- **Location**: `NotificationPanel.java` / `GameView.java`
- **Addition**: Created a fully animated floating score popup UI using JavaFX transitions. Includes: Fade-in, Pop scale, Upward float and Fade-out. Notifications now stack smoothly without overlapping by offsetting new panels based on current active notifications. Popup creation is decoupled from model logic. Clean-up implemented via onFinished() to auto-remove nodes.

### 10. Ghost Piece Implementation
- **Location**: `GameBoard.java`/ `ViewData.java`/ `GameView.java`
- **Addition**: Added `calculateGhostY()` in `GameBoard` to simulate where the brick lands without altering game state. Updated `ViewData` to carry `ghostYPosition`. In `GameView`, added a new rendering layer that displays the projection with a transparent fill and colored outline.

### 11. SRS Wall Kicks
- **Location**: `GameBoard.java`/ `WallKickTable.java`
- **Addition**: Introduced `WallKickTable` containing standard offset values (0, ±1, ±2). Modified `rotateLeftBrick` in `GameBoard` to iterate through these offsets. This prevents pieces from getting stuck near walls by automatically shifting them to a valid adjacent position during rotation.

## Design Pattern Overview
### The project implements multiple design patterns effectively:


- **Factory Method Pattern** (Creational): The `BrickGenerator` interface is implemented by `RandomBrickGenerator.java`. This pattern delegates the logic of creating new concrete Brick objects (like IBrick, JBrick, etc.) to subclasses without the client (`GameBoard`) needing to know the instantiation details.

- **Observer Pattern** (Behavioral): The `Score.java` class uses IntegerProperty and `GameView.bindScore()` uses `textProperty().bind()`. This establishes a one-to-many relationship where the scoreLabel automatically updates whenever the Score object's internal value changes, without direct polling. Also used for window close events in `SettingsView`.

- **Strategy Pattern** (Behavioral): The `BrickType` enum acts as a simplified Strategy, where the client requests a color using an ID, and the enum provides the specific color (strategy) based on the input type. Key bindings also use Strategy pattern for different input commands.

- **Command Pattern** (Behavioral): The `GameCommand` interface and command classes (`MoveLeftCommand`, `MoveRightCommand`, etc.) encapsulate requests as objects, allowing for key binding remapping and undo/redo capabilities.

- **State Pattern** (Behavioral): The `GameState` interface with implementations (`PlayingState`, `PausedState`, `GameOverState`) manages game state transitions and behavior changes based on current state.

- **Facade Pattern** (Structural): The `SettingsView` provides a simplified interface to complex subsystems (key bindings, leaderboard, game restart/resume), hiding the complexity of these subsystems.

- **Fail-Fast Pattern**: All validation methods detect errors early with meaningful exception messages, improving debugging and robustness.

## License

This project is for educational purposes only and is not intended for commercial use.

