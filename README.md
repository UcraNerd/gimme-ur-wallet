# GimmeurWallet

A software application simulating a virtual casino environment, allowing users to interact with three different game modes: Blackjack, Roulette, and Slot Machine.

## 🏗️ System Architecture

The project is built using the **Model-View-Controller (MVC)** design pattern to ensure a clean separation of concerns:

* **Centralized Control:** A main controller manages the initial game selection requests and implements `MouseListener` to handle user interactions on the primary interface.
* **Shared State:** The **Escape** model serves as the shared resource (the wallet), maintaining a consistent player balance across all three game modules.
* **Modular Design:** Each game operates within its own MVC structure, coordinated by specific controllers and dedicated windows.

## 🎮 Game Modules

### 🃏 Blackjack (`BJController`)
Coordinates the betting process and verifies wallet funds. 
* **Animations:** Uses sequential timers for card distribution.
* **Logic:** Automatically calculates scores, manages additional card requests, and updates the shared wallet based on the hand's outcome.

### 🎰 Slot Machine (`SMController`)
Simulates a classic reel-spinning experience.
* **Concurrency:** Utilizes **concurrent threads** to manage the independent rotation of the three reels.
* **Rewards:** Checks for winning combinations and jackpots, processing payments directly to the wallet.

### 🎡 Roulette (`RTController`)
Provides a comprehensive betting interface.
* **Betting:** Allows users to place chips on specific numbers or external zones.
* **Processing:** Calculates wins or losses by comparing the total amount wagered against the result of the spin and updates the balance accordingly.

## 🛠️ Technical Highlights
* **Pattern:** Model-View-Controller (MVC).
* **Concurrency:** Multithreaded reel simulation in the Slot Machine.
* **UI/UX:** Event-driven interaction via `MouseListener` and animated gameplay via `Timers`.
