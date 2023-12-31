/**
 * Classe NewGame écrite par Clément, Maxence et Nicolas.
 * FISA Informatique UTBM en PR70 2023.
 */

package application.vue.pages;

import application.controleur.Plateau;
import application.controleur.vue.NewGameController;
import application.modele.Joueur;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * La classe NewGame regroupe l'ensemble du code pour la création d'une partie du jeu.
 */
public class NewGame extends Parent {

    private NewGameController controller;
    private TextField nameGame;
    private Label labelGame;
    private ArrayList<Joueur> listJoueurs = new ArrayList<>();
    private ArrayList<String> joueurs = new ArrayList<>();
    private ArrayList<String> couleurs = new ArrayList<>();
    private String nomDeLaPartie;
    private Scene scene;

    private static final Map<String, String> colorMap = new HashMap<>();

    static {
        colorMap.put("rouge", "#FF0000");
        colorMap.put("bleu", "#0000FF");
        colorMap.put("vert", "#00FF00");
        colorMap.put("jaune", "#FFFF00");
        colorMap.put("violet", "#FF00FF");
    }

    public static String getColorCode(String colorName) {
        return colorMap.getOrDefault(colorName.toLowerCase(), colorName);
    }

    /**
     * Affiche la page de création d'une partie.
     *
     * @param scene
     */
    public NewGame(Scene scene) {
        this.scene = scene;
        initializeComponents();
        setController(new NewGameController(scene));

        VBox vBox = createVBox();
        vBox.setAlignment(Pos.CENTER);

        Button backButton = createBackButton();

        StackPane stackPane = createStackPane();


        VBox root = new VBox(backButton, vBox, stackPane);
        VBox.setMargin(backButton, new Insets(10, 0, 0, 10));
        getChildren().add(root);

        applyGameStyle();
        root.setPrefWidth(1280);
    }

    /**
     * Affiche le texte.
     */
    private void initializeComponents() {
        nameGame = new TextField();
        labelGame = new Label("Nom de la partie : ");
    }

    /**
     * Affiche la boîte de joueur avec son nom et sa couleur.
     *
     * @return VBox
     */
    private VBox createVBox() {
        VBox vBox = new VBox();

        HBox playersRow = createPlayersRow();
        playersRow.setAlignment(Pos.CENTER);

        VBox.setMargin(playersRow, new Insets(30, 30, 30, 30));
        VBox.setMargin(nameGame, new Insets(20, 0, 0, 0));
        VBox.setMargin(labelGame, new Insets(20, 0, 0, 0));

        vBox.getChildren().addAll(labelGame, nameGame, playersRow);

        return vBox;
    }

    /**
     * Affecte le bouton à la stackPane.
     *
     * @return StackPane
     */
    private StackPane createStackPane() {
        StackPane stackPane = new StackPane();

        Button button = createCreateButton();

        stackPane.getChildren().add(button);

        return stackPane;
    }

    /**
     * Ajote le bouton de démarrage du jeu.
     *
     * @return Button
     */
    private Button createCreateButton() {
        Button button = new Button("Créer la partie");
        button.getStyleClass().add("createButton");

        button.setOnMouseEntered((MouseEvent e) -> applyButtonHoverEffect(button, 1.1));
        button.setOnMouseExited((MouseEvent e) -> applyButtonHoverEffect(button, 1));
        button.setOnMousePressed(e -> button.getStyleClass().add("createButtonPressed"));
        button.setOnMouseReleased(e -> button.getStyleClass().remove("createButtonPressed"));

        button.setOnAction(e -> createGame());

        return button;
    }

    /**
     * Le bouton de retour.
     *
     * @return Button
     */
    public Button createBackButton() {
        Button backButton = new Button();
        backButton.setFont(Font.font("Arial", 14));

        // Créer une flèche pointant vers la gauche avec un Polygon
        Polygon arrow = new Polygon(10, 0, 0, 5, 10, 10);
        arrow.setStyle("-fx-fill: #000000;"); // Couleur de la flèche

        // Créer une HBox pour contenir la flèche et aligner au centre
        HBox hbox = new HBox(arrow);
        hbox.setAlignment(Pos.CENTER);

        // Ajouter la HBox (avec la flèche centrée) comme contenu graphique du bouton
        backButton.setGraphic(hbox);

        backButton.setOnAction(e -> {
            //code un retour à la premiere page (home)
            controller.goToHome();
        });
        backButton.setStyle("-fx-cursor: hand");

        return backButton;
    }

    /**
     * Ajoute l'effet de grossissement sur le bouton de création de jeu.
     *
     * @param button      Button
     * @param scaleFactor double
     */
    private void applyButtonHoverEffect(Button button, double scaleFactor) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(scaleFactor);
        scaleTransition.setToY(scaleFactor);
        scaleTransition.play();
    }

    /**
     * Ligne où la création de joueur sera affichée.
     *
     * @return HBox
     */
    private HBox createPlayersRow() {
        HBox hbox = new HBox(10);

        ObservableList<String> typeJoueurs = FXCollections.observableArrayList("Joueur1", "Joueur2", "Joueur3", "Joueur4", "IA 1", "IA 2", "IA 3");
        ObservableList<String> couleurs = FXCollections.observableArrayList("Rouge", "Bleu", "Vert", "Jaune", "Violet");

        for (int i = 0; i < 4; i++) {
            StackPane square = createSquare();
            ComboBox<String> typeComboBox = createComboBox(typeJoueurs);
            ComboBox<String> colorComboBox = createComboBox(couleurs);

            configureComboBox(typeComboBox, typeJoueurs, this.joueurs);
            configureComboBox(colorComboBox, couleurs, this.couleurs);

            VBox squareContainer = new VBox(square, typeComboBox, colorComboBox);
            squareContainer.setAlignment(Pos.CENTER);

            hbox.getChildren().add(squareContainer);
        }

        return hbox;
    }

    /**
     * Liste des choix.
     *
     * @param items ObservableList<String>
     * @return ComboBox<String>
     */
    private ComboBox<String> createComboBox(ObservableList<String> items) {
        ComboBox<String> comboBox = new ComboBox<>(items);
        comboBox.getStyleClass().add("comboBox");
        VBox.setMargin(comboBox, new Insets(10, 0, 0, 0));
        return comboBox;
    }

    /**
     * Affichage de l'image du joueur.
     *
     * @return StackPane
     */
    private StackPane createSquare() {
        StackPane square = new StackPane();
        square.setMinSize(80, 80);
        square.setMaxSize(80, 80);

        // Chargez l'image depuis le fichier
        Image image = new Image("file:src/main/ressources/pictures/user.png", 70, 70, true, true);


        // Créez une vue d'image et définissez l'image
        ImageView imageView = new ImageView(image);

        square.getStyleClass().add("square");
        square.getChildren().add(imageView);

        StackPane.setAlignment(imageView, Pos.CENTER);

        return square;
    }

    /**
     * Change le style.
     */
    private void applyGameStyle() {
        nameGame.getStyleClass().add("nameGame");
        labelGame.getStyleClass().add("labelGame");
    }

    /**
     * Modifie les listes déroulantes.
     *
     * @param comboBox
     * @param items
     * @param globalList
     */
    private void configureComboBox(ComboBox<String> comboBox, ObservableList<String> items, ArrayList<String> globalList) {
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<String> temp = FXCollections.observableArrayList(newValue);
                comboBox.setItems(temp);

                // Supprimer la valeur sélectionnée des autres ComboBox
                items.remove(newValue);

                // Ajout du type de joueur dans la liste globale
                globalList.add(newValue);
            }
        });
    }

    /**
     * Créait un jeu en fonction des choix de l'utilisateur.
     */
    private void createGame() {
        try {
            String title = this.nameGame.getText().trim();

            if (!title.isEmpty()) {
                if (joueurs.size() < 2) {
                    throw new IllegalArgumentException("Vous devez être au moins deux joueurs. Si vous n'avez pas d'amis, il y a l'IA.");
                } else {
                    if (joueurs.size() == couleurs.size()) {
                        for (int i = 0; i < joueurs.size(); i++) {
                            String couleur = couleurs.get(i);
                            String couleurCode = getColorCode(couleur);
                            listJoueurs.add(new Joueur(i + 1, joueurs.get(i), couleurCode, 10, 0));
                        }

                        this.nomDeLaPartie = title;
                        HashMap<Joueur, Integer> pointsJoueur = new HashMap<>();

                        Plateau plateau = new Plateau(9, 9);

                        Integer[][] emplacementJoueur = new Integer[][]{
                                {4, 8},
                                {4, 0},
                                {8, 4},
                                {0, 4},
                        };

                        int position = 0;
                        int idJoueurActuel = 1;
                        for (Joueur joueur : listJoueurs) {
                            joueur.setPion(plateau.getEmplacementCasePion(emplacementJoueur[position][0], emplacementJoueur[position][1]));
                            pointsJoueur.put(joueur, 0);
                            position++;
                        }
                        Object[] data = new Object[]{plateau, pointsJoueur, idJoueurActuel};
                        controller.goToGame(this.nomDeLaPartie, data);

                    } else {
                        throw new IllegalArgumentException("Le nombre de joueurs et de couleurs doit être identique.");
                    }
                }

            } else {
                throw new IllegalArgumentException("Le titre de la partie ne peut pas être vide ou contenir seulement des espaces.");
            }
        } catch (IllegalArgumentException e) {
            // Afficher une alerte en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    private void setController(NewGameController controller) {
        this.controller = controller;

    }
}