
    import javafx.application.Application;
    import javafx.beans.property.ObjectProperty;
    import javafx.beans.property.SimpleObjectProperty;
    import javafx.geometry.Pos;
    import javafx.stage.Modality;
    import javafx.stage.Stage;
    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.Spinner;
    import javafx.scene.layout.BorderPane;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.Pane;
    import javafx.scene.layout.VBox;
    import javafx.scene.paint.Color;
    import javafx.scene.shape.Line;
    import javafx.scene.shape.Ellipse;
    
    public class Main extends Application {
        private int gameSize;
        private char lastTurn;
        private Scene ticTacToeScene;
        private TicTacToePane ticTacToeNew;
        private ObjectProperty<Character> turn;
    
        @Override 
        public void start(Stage primaryStage) {
            gameSize = 5;
            VBox gameStartPane = new VBox(10);
            gameStartPane.setAlignment(Pos.CENTER);
            HBox hBoxStartButtons = new HBox(10);
            hBoxStartButtons.setAlignment(Pos.CENTER);
            Button btnExit = new Button("Exit");
            Button btnStartGame = new Button("Start");
            hBoxStartButtons.getChildren().addAll(btnStartGame, btnExit);
            HBox hBoxGameOptions = new HBox(10);
            hBoxGameOptions.setAlignment(Pos.CENTER);
            Spinner<Integer> startGameSize = new Spinner<>(1, 100, gameSize, 1);
            hBoxGameOptions.getChildren().addAll(new Label("Game Dimensions:"), startGameSize);
            gameStartPane.getChildren().addAll(new Label("TIC TAC TOE"), new Label(""), hBoxGameOptions, hBoxStartButtons);
    
            Scene startGameScene = new Scene(gameStartPane, 300, 150);
    
            btnExit.setOnAction(e -> {
                primaryStage.close();
            });
    
            btnStartGame.setOnAction(e -> {
                gameSize = startGameSize.getValue();
                turn = new SimpleObjectProperty<Character>();
                TicTacToePane ticTacToe = new TicTacToePane(gameSize);
                ticTacToeScene = new Scene(ticTacToe, 300, 300);
                turn.bind(ticTacToe.whoseTurn);
                turn.addListener(ov -> {
                    if (turn.get() == ' ') {
                        Stage gameOver = new Stage();
                        gameOver.initModality(Modality.APPLICATION_MODAL);
                        VBox gameOverPane = new VBox(10);
                        gameOverPane.setAlignment(Pos.CENTER);
                        HBox hBoxButtons = new HBox(10);
                        HBox hBoxNewGameOptions = new HBox(10);
                        Spinner<Integer> newGameSize = new Spinner<>(1, 100, gameSize, 1);
                        hBoxNewGameOptions.setAlignment(Pos.CENTER);
                        hBoxNewGameOptions.getChildren().addAll(new Label("New Game Dimensions:"), newGameSize);
                        hBoxButtons.setAlignment(Pos.CENTER);
                        Button btnQuit = new Button("Quit");
                        btnQuit.setOnAction(f -> {
                            primaryStage.close();
                            gameOver.close();
                        });
    
                        Button btnNewGame = new Button("New Game");
                        btnNewGame.setOnAction(f -> {
                            gameSize = newGameSize.getValue();
                            turn.unbind();
                            ticTacToeNew = new TicTacToePane(gameSize);
                            turn.bind(ticTacToeNew.whoseTurn);
                            ticTacToeScene.setRoot(ticTacToeNew);
                            gameOver.close();
                        });
                        hBoxButtons.getChildren().addAll(btnNewGame, btnQuit);
    
                        if (((TicTacToePane) ticTacToeScene.getRoot()).isDraw == false) {
                            gameOverPane.getChildren().addAll(new Label("GAME OVER!"), new Label(lastTurn + " WINS!"),
                                    hBoxNewGameOptions, hBoxButtons);
                            gameOver.setTitle("GAME OVER! " + lastTurn + " WINS!");
                        } else {
                            gameOverPane.getChildren().addAll(new Label("GAME OVER!"), new Label("TIE!"),
                                    hBoxNewGameOptions, hBoxButtons);
                            gameOver.setTitle("GAME OVER! TIE!");
                        }
                        Scene gameOverScene = new Scene(gameOverPane, 300, 150);
                        gameOver.setResizable(false);
    
                        gameOver.setScene(gameOverScene);
                        gameOver.showAndWait();
                    } else {
                        lastTurn = turn.get();
                    }
                });
                primaryStage.setScene(ticTacToeScene);
            });
    
            primaryStage.setTitle("TicTacToe"); 
            primaryStage.setScene(startGameScene);
            primaryStage.show(); 
        }
    
        public class TicTacToePane extends BorderPane {
    
            public TicTacToePane(int size) {
                super();
                this.size = size;
                this.cell = new Cell[size][size];
                
                GridPane pane = new GridPane();
                for (int i = 0; i < size; i++)
                    for (int j = 0; j < size; j++)
                        pane.add(cell[i][j] = new Cell(), j, i);
    
                this.setCenter(pane);
                this.setBottom(lblStatus);
    
            }
    
            
            private int size;
    
            
            public ObjectProperty<Character> whoseTurn = new SimpleObjectProperty<Character>('X');
    
            public boolean isDraw = false;
    
            
            private Cell[][] cell;
    
            
            private Label lblStatus = new Label("X's turn to play");
    
            
            public boolean isFull() {
                for (int i = 0; i < size; i++)
                    for (int j = 0; j < size; j++)
                        if (cell[i][j].getToken() == ' ')
                            return false;
    
                return true;
            }
    
            
            public boolean isWon(char token) {
                if (checkRows(token) || checkColumns(token) || checkMainDiagonal(token) || checkOtherDiagonal(token)) {
                    return true;
                }
                return false;
            }
    
            private boolean checkRows(char token) {
                for (int i = 0; i < size; i++) {
                    int count = 0;
                    for (int j = 0; j < size; j++) {
                        if (cell[i][j].getToken() == token) {
                            count++;
                        }
                        if (count == size) {
                            return true;
                        }
                    }
                }
                return false;
            }
    
            private boolean checkColumns(char token) {
                for (int j = 0; j < size; j++) {
                    int count = 0;
                    for (int i = 0; i < size; i++) {
                        if (cell[i][j].getToken() == token) {
                            count++;
                        }
                        if (count == size) {
                            return true;
                        }
                    }
                }
                return false;
            }
    
            private boolean checkMainDiagonal(char token) {
    
                int count = 0;
                for (int i = 0; i < size; i++) {
                    if (cell[i][i].getToken() == token) {
                        count++;
                    }
                    if (count == size) {
                        return true;
                    }
                }
    
                return false;
            }
    
            private boolean checkOtherDiagonal(char token) {
    
                int count = 0;
                for (int i = 0; i < size; i++) {
                    if (cell[i][size - i - 1].getToken() == token) {
                        count++;
                    }
                    if (count == size) {
                        return true;
                    }
                }
    
                return false;
            }
    
          
            public class Cell extends Pane {
              
                private char token = ' ';
    
                public Cell() {
                    setStyle("-fx-border-color: black; -fx-background-color: white;");
                    this.setPrefSize(800, 800);
                    this.setOnMouseClicked(e -> handleMouseClick());
                }
    
            
                public char getToken() {
                    return token;
                }
    
                
                public void setToken(char c) {
                    token = c;
    
                    if (token == 'X') {
                        Line line1 = new Line(10, 10,
                                this.getWidth() - 10, this.getHeight() - 10);
                        line1.endXProperty().bind(this.widthProperty().subtract(10));
                        line1.endYProperty().bind(this.heightProperty().subtract(10));
                        Line line2 = new Line(10, this.getHeight() - 10,
                                this.getWidth() - 10, 10);
                        line2.startYProperty().bind(
                                this.heightProperty().subtract(10));
                        line2.endXProperty().bind(this.widthProperty().subtract(10));
    
                      
                        this.getChildren().addAll(line1, line2);
                    } else if (token == 'O') {
                        Ellipse ellipse = new Ellipse(this.getWidth() / 2,
                                this.getHeight() / 2, this.getWidth() / 2 - 10,
                                this.getHeight() / 2 - 10);
                        ellipse.centerXProperty().bind(
                                this.widthProperty().divide(2));
                        ellipse.centerYProperty().bind(
                                this.heightProperty().divide(2));
                        ellipse.radiusXProperty().bind(
                                this.widthProperty().divide(2).subtract(10));
                        ellipse.radiusYProperty().bind(
                                this.heightProperty().divide(2).subtract(10));
                        ellipse.setStroke(Color.RED);
                        ellipse.setFill(Color.GREEN);
    
                        getChildren().add(ellipse); 
                    }
                }
    
                
                private void handleMouseClick() {
                    
                    if (token == ' ' && whoseTurn.get() != ' ') {
                        setToken(whoseTurn.get()); 
    
                        
                        if (isWon(whoseTurn.get())) {
                            lblStatus.setText(whoseTurn.get() + " Congrats! game over");
                            whoseTurn.set(' '); 
    
                        } else if (isFull()) {
                            lblStatus.setText("TIE! game over");
                            isDraw = true;
                            whoseTurn.set(' ');
    
                        } else {
                          
                            if (whoseTurn.get() == 'X') {
                                whoseTurn.set('O');
                            } else {
                                whoseTurn.set('X');
                            }
    
                            
                            lblStatus.setText(whoseTurn.get() + "'s turn");
                        }
                    }
                }
            }
        }
    
        
        public static void main(String[] args) {
            launch(args);
        }
    }