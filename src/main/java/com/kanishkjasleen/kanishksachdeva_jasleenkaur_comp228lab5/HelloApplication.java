package com.kanishkjasleen.kanishksachdeva_jasleenkaur_comp228lab5;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.sql.*;

import java.io.IOException;
import java.text.MessageFormat;

public class HelloApplication extends Application {
    private Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException, ClassNotFoundException, SQLException {

        Class.forName("oracle.jdbc.OracleDriver");
        final Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@199.212.26.208:1521:SQLD", "COMP228_F20_3421", "password");

        if (conn != null){
            System.out.println("Successfully connected");

        } else {
            System.out.println("Not Connected");
        }
        stage = primaryStage;
        primaryStage.setScene(newplayerForm(conn));

        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public Scene newplayerForm(Connection conn) {
        stage.setTitle("Student Information");
        Panel panel = new Panel("Player Information");
        panel.getStyleClass().add("panel-primary");
        GridPane content = new GridPane();
        content.setAlignment(Pos.CENTER);
        content.setHgap(10);
        content.setVgap(10);
        content.setPadding(new Insets(15));

//        First name
        Label first_name_lbl = new Label("First Name:");
        content.add(first_name_lbl, 0, 1);
        final TextField first_name = new TextField();
        content.add(first_name, 1, 1);

        //        Last name
        Label last_name_lbl = new Label("Last Name:");
        content.add(last_name_lbl, 0, 2);
        final TextField last_name = new TextField();
        content.add(last_name, 1, 2);

        //        Address
        Label address_lbl = new Label("Address:");
        content.add(address_lbl, 0, 3);
        final TextField address = new TextField();
        content.add(address, 1, 3);

        //       Postal Code
        Label postalcode_lbl = new Label("Postal Code:");
        content.add(postalcode_lbl, 0, 4);
        final TextField postalcode = new TextField();
        content.add(postalcode, 1, 4);

        //       Province
        Label province_lbl = new Label("Province:");
        content.add(province_lbl, 0, 5);
        final TextField province = new TextField();
        content.add(province, 1, 5);

        //       Phone Number
        Label phone_no_lbl = new Label("Phone number:");
        content.add(phone_no_lbl, 0, 6);
        final TextField phone_no = new TextField();
        content.add(phone_no, 1, 6);


        Button submitBtn = new Button("Submit");
        submitBtn.getStyleClass().setAll("btn","btn-success");
        submitBtn.setMinWidth(200);

        submitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String fname = first_name.getText();
                String lname = last_name.getText();
                String addr = address.getText();
                String postalc = postalcode.getText();
                String prov = province.getText();
                int phoneno = Integer.parseInt(phone_no.getText());

                try {
                    assert conn != null;
                    Statement sql_stmt = conn.createStatement();

                    String sql = "SELECT MAX(player_id) FROM PLAYER";
                    int player_id = 0;
                    ResultSet maxplayerid = sql_stmt.executeQuery(sql);
                    if (maxplayerid == null) {
                        player_id = 0;
                    } else {
                        while (maxplayerid.next())
                        {player_id = maxplayerid.getInt(1) + 1;}

                    }

                    String playernew = "INSERT INTO Player (player_id, first_name, last_name, address, postal_code, province, phone_number) VALUES (?, ?, ?, ? ,?, ?, ?)";

                    PreparedStatement statement = conn.prepareStatement(playernew);
                    statement.setInt(1, player_id);
                    statement.setString(2, fname);
                    statement.setString(3, lname);
                    statement.setString(4, addr);
                    statement.setString(5, postalc);
                    statement.setString(6, prov);
                    statement.setInt(7, phoneno);

                    int rowsInserted = statement.executeUpdate();

                    if (rowsInserted > 0) {
                        System.out.println("A new player was inserted successfully!");
                        Alert alert = new Alert(Alert.AlertType.NONE);
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("A new Player has been added");
                        alert.show();
                        stage.setScene(Dashboard(conn));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        content.add(submitBtn,1,7);

        panel.setBody(content);
        panel.setFooter(footerBox(0, conn));

        Scene scene = new Scene(panel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        return scene;
    }

    public Scene newGameForm(Connection conn) {
        stage.setTitle("Add a new game");
        Panel panel = new Panel("Game Information");
        panel.getStyleClass().add("panel-warning");
        GridPane content = new GridPane();
        content.setAlignment(Pos.CENTER);
        content.setHgap(10);
        content.setVgap(10);
        content.setPadding(new Insets(15));

        //       Game
        Label game_lbl = new Label("Game title:");
        content.add(game_lbl, 0, 1);
        final TextField gameTitle = new TextField();
        content.add(gameTitle, 1, 1);

        Button submitGameBtn = new Button("Submit");
        submitGameBtn.getStyleClass().setAll("btn","btn-success");
        submitGameBtn.setMinWidth(200);

        submitGameBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Statement sql_stmt = null;
                try {
                    sql_stmt = conn.createStatement();

                    String gamesql = "SELECT MAX(game_id) FROM GAME";
                    int game_id = 0;
                    ResultSet maxgameid = sql_stmt.executeQuery(gamesql);
                    if (maxgameid == null) {
                        game_id = 0;
                    } else {
                        while (maxgameid.next())
                        {game_id = maxgameid.getInt(1) + 1;}
                    }
                    String gamenew = "INSERT INTO GAME (game_id, game_title) VALUES (?, ?)";

                    PreparedStatement statement = conn.prepareStatement(gamenew);
                    statement.setInt(1, game_id);
                    statement.setString(2, gameTitle.getText());

                    int gameInserted = statement.executeUpdate();

                    if (gameInserted > 0) {
                        System.out.println("A new game was inserted successfully!");
                        Alert alert = new Alert(Alert.AlertType.NONE);
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("A new Game has been added");
                        alert.show();
                        stage.setScene(newGameForm(conn));
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        content.add(submitGameBtn, 1, 2);
        ObservableList<String> games = FXCollections.observableArrayList();
        try {
            String sql = "SELECT game_title FROM GAME";

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()){
                String name = result.getString(1);
                games.add(name);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        ListView<String> gamesListView = new ListView<String>(games);
        gamesListView.setMaxHeight(200);
        content.add(new Label("Games already added:"), 0, 3);
        content.add(gamesListView, 1, 3);

        panel.setBody(content);
        panel.setFooter(footerBox(1, conn));

        Scene scene = new Scene(panel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        return scene;
    }

    public Scene newMatch(Connection conn) {
        stage.setTitle("Tournament");
        Panel panel = new Panel("Match Information");
        panel.getStyleClass().add("panel-info");
        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(15));

        Statement statement = null;
        int player_id = 0;
        int game_id = 0;


        ObservableList<String> data = FXCollections.observableArrayList();
        try {
            statement = conn.createStatement();
            String sql = "SELECT * FROM PLAYER";

            ResultSet result = statement.executeQuery(sql);
            while (result.next()){
                player_id = result.getInt(1);
                String player_fname = result.getString(2);
                String player_lname = result.getString(3);

                String province = result.getString(6);
                data.add(MessageFormat.format("{0}. {1} {2}",player_id,player_fname, player_lname));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ListView<String> playersListView = new ListView<String>(data);
        playersListView.setMaxWidth(150);
        playersListView.setMaxHeight(150);

        VBox vBox = new VBox(10);
        vBox.getChildren().add(new Label("Select Player you want to match:"));
        vBox.getChildren().add(playersListView);

        TextArea textArea = new TextArea();
        textArea.setMaxHeight(100);
        textArea.setEditable(false);
        textArea.setText("Select a player");

        playersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    Statement statement1 = conn.createStatement();
                    String sql = "SELECT * FROM PLAYER WHERE player_id = "+ Integer.parseInt(t1.replaceAll("[^0-9]", ""));

                    ResultSet result = statement1.executeQuery(sql);
                    while (result.next()){
                        int player_id = result.getInt(1);
                        String player_fname = result.getString(2);
                        String player_lname = result.getString(3);
                        String address = result.getString(4);
                        String postal_code = result.getString(5);
                        int phone_number = result.getInt(7);

                        String province = result.getString(6);
                        textArea.setText(MessageFormat.format("Player Information:\nName: {1} {2}\nAddress: {3}\nPostal Code: {4}\nPhone Number: {5}",
                                player_id,player_fname, player_lname, address,postal_code, phone_number));


                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        vBox.getChildren().add(textArea);
        vBox.setMaxWidth(150);

        content.getChildren().add(vBox);


        VBox vBox1 = new VBox(10);
        vBox1.getChildren().add(new Label("Select game for player:"));
        ObservableList<String> games = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM GAME";

            Statement statement3 = conn.createStatement();
            ResultSet result = statement3.executeQuery(sql);
            while (result.next()){
                String name = result.getString(2);
                game_id = result.getInt(1);
                games.add(name);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ListView<String> gamesListView = new ListView<String>(games);

        gamesListView.setMaxHeight(150);
        vBox1.getChildren().add(gamesListView);


        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        Label scorelabel = new Label("Scores: ");
        gridPane.add(scorelabel, 0 , 1);
        TextField scoretext = new TextField();
        gridPane.add(scoretext,1,1);

        Label datelabel = new Label("Date: ");
        gridPane.add(datelabel, 0 , 2);
        TextField datetext = new TextField();
        gridPane.add(datetext,1,2);
        vBox1.getChildren().add(gridPane);

        Button submitBtn1 = new Button("Submit");
        submitBtn1.getStyleClass().setAll("btn","btn-success");

        int finalPlayer_id = player_id;
        submitBtn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int finalGame_id = gamesListView.getSelectionModel().getSelectedIndex() + 1;
                try {
                Statement sql_stmt = conn.createStatement();

                String maxplayergameidsql = "SELECT MAX(player_game_id) FROM PLAYERANDGAME";
                int playergameid = 0;
                ResultSet maxplayergameid = sql_stmt.executeQuery(maxplayergameidsql);
                if (maxplayergameid == null) {
                    playergameid = 0;
                } else {
                    while (maxplayergameid.next())
                    {playergameid = maxplayergameid.getInt(1) + 1;}
                }

                String playernew = "INSERT INTO PlayerAndGame VALUES (?, ?, ?, ? ,?)";

                PreparedStatement statement1 = null;

                    statement1 = conn.prepareStatement(playernew);
                    statement1.setInt(1, playergameid);
                    statement1.setInt(2, finalGame_id);
                    statement1.setInt(3, finalPlayer_id);
                    statement1.setString(4, datetext.getText());
                    statement1.setString(5, scoretext.getText());

                    int rowsInserted = statement1.executeUpdate();

                    if (rowsInserted > 0) {
                        System.out.println("A new record was inserted successfully!");

                        Alert alert = new Alert(Alert.AlertType.NONE);
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("Player match has been recorded");
                        alert.show();

                        stage.setScene(Dashboard(conn));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }



            }
        });

        vBox1.getChildren().add(submitBtn1);


        content.getChildren().add(vBox1);




        panel.setBody(content);
        panel.setFooter(footerBox(3, conn));

        Scene scene = new Scene(panel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        return scene;
    }

    public Scene Dashboard(Connection conn) {
        stage.setTitle("Dashboard");
        Panel panel = new Panel("Dashboard");
        panel.getStyleClass().add("panel-success");
        HBox content = new HBox(0);
//        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(2));

        TableView table = new TableView();
        table.setMinWidth(240);
        table.setEditable(false);

        TableColumn<Player, String> column1 = new TableColumn<>("Player Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("player_name"));
        TableColumn<Player, String> column2 = new TableColumn<>("Address");
        column2.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<Player, String> column3 = new TableColumn<>("Game Played");
        column3.setCellValueFactory(new PropertyValueFactory<>("gamename"));
        TableColumn<Player, String> column4 = new TableColumn<>("Score");
        column4.setCellValueFactory(new PropertyValueFactory<>("score"));
        TableColumn<Player, String> column5 = new TableColumn<>("Date Played");
        column5.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Player, String> column6 = new TableColumn<>("Phone number");
        column6.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));


        table.getColumns().addAll(column1, column2, column3, column4, column5, column6);

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PLAYERANDGAME");
            while (resultSet.next()){
                String name = "";
                String address = "";
                int phonenumber = 0;

                try {
                    Statement stm1 = conn.createStatement();
                    ResultSet rs1 = stm1.executeQuery("SELECT * FROM PLAYER WHERE player_id = " + resultSet.getString(3));
                    while (rs1.next()){
                        name = rs1.getString(2) + " " + rs1.getString(3);
                        address = rs1.getString(4) + ", " + rs1.getString(6) + ", " + rs1.getString(5);
                        phonenumber = rs1.getInt(7);
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }

                String game_title = "";
                try {
                    Statement stm2 = conn.createStatement();
                    ResultSet rs2 = stm2.executeQuery("SELECT * FROM GAME WHERE game_id = " + resultSet.getString(2));
                    while (rs2.next()){
                        game_title = rs2.getString(2);
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }

                table.getItems().add(new Player(name,address,game_title,resultSet.getString(5), resultSet.getString(4), phonenumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        table.getItems().add(new Player("hi","hi","ijio","10","26 nov",65565));
//        table.getItems().add(new Player("hi","hi","ijio","10","26 nov",65565));
//        table.getItems().add(new Player("hi","hi","ijio","10","26 nov",65565));
        content.getChildren().add(table);

        panel.setBody(content);
        panel.setFooter(footerBox(2, conn));

        Scene scene = new Scene(panel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        return scene;
    }

    public class Player {
        String player_name, address, gamename, score, date;
        int phonenumber;

        public Player(String player_name, String address, String gamename, String score, String date, int phonenumber) {
            this.player_name = player_name;
            this.address = address;
            this.gamename = gamename;
            this.score = score;
            this.date = date;
            this.phonenumber = phonenumber;
        }

        public String getPlayer_name() {
            return player_name;
        }

        public void setPlayer_name(String player_name) {
            this.player_name = player_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getGamename() {
            return gamename;
        }

        public void setGamename(String gamename) {
            this.gamename = gamename;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(int phonenumber) {
            this.phonenumber = phonenumber;
        }
    }

    public HBox footerBox(int i, Connection conn){
        HBox hBox = new HBox(10);
        Button btn1 = new Button("New Player");
        Button btn2 = new Button("New game");
        Button btn4 = new Button("New Match");
        Button btn3 = new Button("Dashboard");

        if (i == 0){
            btn1.getStyleClass().setAll("btn","btn-warning");
            btn2.getStyleClass().setAll("btn","btn-primary");
            btn3.getStyleClass().setAll("btn","btn-primary");
            btn4.getStyleClass().setAll("btn","btn-primary");
        } else if (i == 1) {
            btn2.getStyleClass().setAll("btn","btn-warning");
            btn1.getStyleClass().setAll("btn","btn-primary");
            btn3.getStyleClass().setAll("btn","btn-primary");
            btn4.getStyleClass().setAll("btn","btn-primary");
        } else if (i == 2) {
            btn3.getStyleClass().setAll("btn","btn-warning");
            btn1.getStyleClass().setAll("btn","btn-primary");
            btn2.getStyleClass().setAll("btn","btn-primary");
            btn4.getStyleClass().setAll("btn","btn-primary");
        } else if (i == 3) {
            btn4.getStyleClass().setAll("btn","btn-warning");
            btn1.getStyleClass().setAll("btn","btn-primary");
            btn2.getStyleClass().setAll("btn","btn-primary");
            btn3.getStyleClass().setAll("btn","btn-primary");
        }

        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(newplayerForm(conn));
            }
        });

        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(newGameForm(conn));
            }
        });

        btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(newMatch(conn));
            }
        });

        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(Dashboard(conn));
            }
        });

        hBox.getChildren().addAll(btn1, btn2,btn4, btn3);
        return hBox;
    }

    public static void main(String[] args) {
        launch();
    }
}
