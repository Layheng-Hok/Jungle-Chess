# 斗兽棋 (pinyin: Dou Shou Qi, foreign name: Jungle Chess)

斗兽棋 (Dou Shou Qi), also known by various name variations such as Jungle Chess, Animal Chess, and Beast Fighting Chess, is a traditional Chinese board game with an obscure history. The game is played on a 9×7 board and is popular with children in the far east. Jungle is a two-player strategy game and has been cited by The Playboy Winner's Guide to Board Games as resembling the Western game Stratego.

# Gameplay
### Pieces
Each player owns 8 animal pieces representing different animals of various ranks, and higher ranked animals can capture the animals of lower or equal rank. But there is a special case that eleplant cannot capture the rat while the rat can capture the eleplant.

|   Rank   |        Piece         |     Special Movement     |
|:--------:|:--------------------:|:------------------------:|
| 1        | Rat (鼠, shǔ)        | Entering the river       |
| 2        | Cat (猫, māo)        | None                     |
| 3        | Dog (狗, gǒu)        | None                     |
| 4        | Wolf (狼, láng)      | None                     |
| 5        | Leopard (豹, bào)    | None                     |
| 6        | Tiger (虎, hǔ)       | Jumping across the river |
| 7        | Lion (狮, shī)       | Jumping across the river |
| 8        | Elephant (象, xiàng) | None                     |

### Chessboard
Jungle game has an interesting chessboard with differerent terrains including dens, traps and rivers.

The three kinds of special terrains are explained as:
+ Dens (兽穴): It is not allowed that a piece enters its own den.
+ Trap (陷阱): If a piece entering the opponents's trap, then its rank is reduced into 0 temporarily before exiting. The trapped piece could be captured by any pieces of the defensing side.
+ River (河流): They are located in the center of the chessboard, each comprising 6 squares in a 3×2 rectangle. Only rats could enter the river, and lions and tigers could jump across the river.

### Rules
1. Game Initialization: At the beginning, all 16 pieces are put into the chessboard as the initial state.
  
2. Game Progress: The player with blue color moves first. Two players take the turn alternatively until the game is finished. When a player takes turn, he/she can select one of his pieces and do one of the following moves:
+ Moving one square horizontally or vertically. For lion, tiger and rat, they also have special movements related to the river squares, which have been introduced previously.
+ Capturing an opposing piece. In all captures, the captured pieces is removed from the board and the square is occupied by the attacking piece. A piece can capture any enemy piece following the rules introduced in "Pieces".

3. Game Termination:
+ A player loses the game if one of the following conditions is met:
  - The den is entered by his/her opponent.
  - All of his/her pieces are captured.
  - The player runs out of valid moves in his/her turn.
  - His or her timer hits 0 first during blitz mode.
  - A player clicks the resign button during their turn for Human-Human mode. In Human-AI mode, the human player loses whenever the resign button is clicked.
+ A game is considered a draw if none of winning conditions is met after 150 rounds. In other words, each player has played up to 150 moves each and there is still no winner after 300 moves in total.

Note for threefold repetition: If a player moves the same piece to the same board position for more than three times consecutively, any valid moves of that same piece to the repeated positon will be banned temporarily until the player moves any piece to a new valid position.

# Game Engine (Artificial Intelligence / Chessbot)
### About Engine
The engine is built into three different chess bots:
+ [BOT 1](https://github.com/Layheng-Hok/Jungle-Chess/blob/main/src/model/artificialintelligence/Minimax.java) implements the minimax algorithm without any further enhancements. It can search to a depth of 4.
+ [BOT 2](https://github.com/Layheng-Hok/Jungle-Chess/blob/main/src/model/artificialintelligence/AlphaBetaWithMoveOrdering.java) implements the minimax algorithm with alpha-beta pruning and move ordering enhancements. It can reach a depth of 6.
+ [BOT 3](https://github.com/Layheng-Hok/Jungle-Chess/blob/main/src/model/artificialintelligence/PruningOrderingQuiescenceSearch.java) implements the aforementioned algorithms in BOT 2 plus quiescence search to increase the search depth for a certain situation. Thus, this bot has a dynamic search depth.

### Evaluation Function
The implemented algorithm of the engine is a depth first search of the game tree, in which the search will stop at a certain depth and evaluate the board position. Since Jungle Chess is a zero-sum game, we can represent the position in terms of benet to blue player. The same set of values can be used for both players by rotating and negating the development tables when we consider red player's pieces.

*Formula: net score = scorePlayer(board.bluePlayer(), depth) - scorePlayer(board.redPlayer(), depth)*

Evaluation details:
+ Piece Value: considering each piece's power and development position.
+ Mobility: considering the ratio of size of a player's valid moves over the size of the opponent's valid moves.
+ Capturing Move: making a piece seeks for a position that can capture its opponent's piece or stays aware of opponent's attacks.
+ Penetration of Opponent's Den: making a piece seeks for entering its opponent's den for the endgame winning condition.

[*[See the detailed implementation]*](https://github.com/Layheng-Hok/Jungle-Chess/blob/main/src/model/artificialintelligence/StandardBoardEvaluator.java)

A positive net score suggests that blue player has an edge over red player, while a negative net score suggests that red player has an upper hand.

### Minimax
At its core, the Minimax algorithm is a technique used by AI agents to make optimal decisions while considering the potential outcomes of their actions. In Jungle Chess, where players aim to outmaneuver each other using animal pieces with varied strengths and abilities, the Minimax algorithm simulates multiple possible moves ahead to determine the best course of action.

The term "Minimax" reflects the two essential aspects that the algorithm balances: minimizing the potential loss while maximizing the potential gain. It achieves this by recursively exploring the game tree, analyzing all possible moves and counter-moves, and assigning a value to each possible board state. These values represent the desirability of each state from the perspective of the player and their opponent.

The number of leaf nodes that are evaluated at a certain depth from the initial configuration:
|   Depth   | No. of evaluated nodes |   
|:---------:|:----------------------:|
| 1         | 24                     | 
| 2         | 576                    | 
| 3         | 12,240                 | 
| 4         | 260_099                | 
| 5         | 5,111.620              |
| 6         | 100,453,636            | 

The time complexity is O(b<sup>n</sup>) where n is the search depth and b is the maximum branching factor of the game tree, but the space complexity is O(n) because it doesn't need to retain all the information about each branch.

### Alpha-Beta Pruning
Alpha-Beta Pruning is a powerful optimization technique that further enhances the efficiency of the Minimax algorithm. It works by reducing the number of nodes that need to be evaluated in the game tree. The basic idea is to eliminate branches of the tree that are guaranteed to be less desirable, thereby reducing the computation time.

When searching the game tree, the algorithm keeps track of two values: alpha and beta. The alpha value represents the minimum score that the maximizing player is assured of, while the beta value represents the maximum score that the minimizing player is assured of. As the algorithm progresses, it prunes branches that are outside the range defined by alpha and beta, since the players would never choose those branches.

By pruning these unnecessary branches, Alpha-Beta Pruning significantly reduces the number of nodes that need to be evaluated, leading to a dramatic improvement in the efficiency of the Minimax algorithm. This is particularly crucial in complex games like Jungle Chess, where the game tree can be vast.

### Move Ordering
Move-ordering is an enhancement of alpha-beta pruning which searches the moves at each position in a specic order based on how likely they are to cause a refutation.

We have two types of move sorter:
+ Standard Move Sorter: it is used after searching the initial depth, prioritizing caputuring moves and moves made by lion, tiger, and elephant.
+ Expensive Move Sorter: it is used when starting the search in the initial depth, employing the above-mentioned methods in Standard Move Sorter and also considering moves that enter opponent's trap without any opponent's pieces nearby and moves that enter opponent's den.

[*[See the detailed implementation]*](https://github.com/Layheng-Hok/Jungle-Chess/blob/main/src/model/artificialintelligence/MoveSorter.java)

### Quiescence Search
Quiescence Search is a technique used to mitigate the "horizon effect," a common problem in game tree search algorithms where the search may end at a point where the game is not fully resolved, leading to suboptimal moves. In Jungle Chess AI, this technique helps to address situations where a capturing move and a move that enters the opponent's den are involved.

In a position with high tactical activities, instead of immediately returning the evaluation of the position, the algorithm extends the search further to explore the consequences of the capturing move and any subsequent forced responses. This ensures that the search goes deeper into positions where tactical exchanges and captures are taking place.

By employing Quiescence Search, the AI agent can avoid making hasty decisions based on incomplete information and can better evaluate positions with high tactical activity. This is crucial in Jungle Chess, where capturing opponent pieces and tactical maneuvers play a significant role in gaining an advantage.

The addition of Quiescence Search enhances the strategic depth of the AI's decision-making process, enabling it to handle complex positions more effectively and make more informed moves. This further elevates the level of play and provides a more challenging and engaging experience for both human and AI players.
