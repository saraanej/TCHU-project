package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Non instanciable and priavte
 */

final class DecksViewCreator {

    // retourne la vue de la main
    public static Node createHandView(ObservableGameState observableGameState){
        HBox handView = new HBox();
        handView.getStylesheets().addAll("decks.css","colors.css");

        //billets
        ListView<Ticket> listView = new ListView<Ticket>(observableGameState.ticketList().get());
        listView.setId("tickets");

        HBox handPane = new HBox();
        handPane.setId("hand-pane");

        handView.getChildren().addAll(listView, handPane);

        //carte + compteur noir
        //la classe NEUTRAL est attachée au dernier fils
        //Les fils suivants sont chacun une instance de StackPane montrant
        //les cartes d'une couleur donnée que le joueur a en main
        for(Card card : Card.ALL){
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().addAll(card == Card.LOCOMOTIVE ? "NEUTRAL" : card.name(), "card");

            //carte couleur
            Rectangle outside = new Rectangle(60,90);
            outside.getStyleClass().add("Outside");

            Rectangle filledInside = new Rectangle(40,70);
            filledInside.getStyleClass().addAll("filled", "inside");

            Rectangle trainImage = new Rectangle(40,70);
            trainImage.getStyleClass().add("train-image");

            //compteur couleur
            Text cardCounter = new Text(observableGameState.numberCardsOfType(card).toString());
            cardCounter.getStyleClass().add("count");

            stackPane.getChildren().addAll(cardCounter, outside, filledInside,trainImage);

            handPane.getChildren().add(stackPane);
        }
        return handView;
    }

    // gestionnaires d'action : un gère
    public void createCardsView(ObservableGameState observableGameState){
        VBox deckView = new VBox();
        deckView.getStylesheets().addAll("decks.css","colors.css");
        deckView.getStyleClass().add("card-pane");

        Button ticketsDeck = new Button();
        ticketsDeck.getStyleClass().add("gauged");
        buttonGauge(ticketsDeck);

        Button cardsDeck = new Button();
        cardsDeck.getStyleClass().add("gauged");
        buttonGauge(cardsDeck);

        /*Donc pour les cartes dont la face est visible,
        vous devez créer le graphe de scène mais sans essayer de savoir quelle est la couleur de la carte se trouvant à chaque emplacement.
        Pour cela, il vous suffit de ne pas ajouter
        la classe représentant la couleur de la carte aux classes de style de l’instance de StackPane.
        Cette classe de style ne doit être ajoutée que par l’auditeur décrit à la §3.5.2.*/

        for(int i = 0; i < Constants.FACE_UP_CARDS_COUNT; ++i){
            //ReadOnlyObjectProperty<Card> card = observableGameState.faceUpCard(i);
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().add("card"); //TODO : 3.5.2 ajouter la couleur

            Rectangle outside = new Rectangle(60,90);
            outside.getStyleClass().add("Outside");

            Rectangle filledInside = new Rectangle(40,70);
            filledInside.getStyleClass().addAll("filled", "inside");

            Rectangle trainImage = new Rectangle(40,70);
            trainImage.getStyleClass().add("train-image");

            stackPane.getChildren().addAll(outside, filledInside,trainImage);
        }



    }

    private void buttonGauge(Button button){
        Group group = new Group();

        Rectangle background = new Rectangle(50,5);
        background.getStyleClass().add("background");
        Rectangle foreground = new Rectangle(50,5);
        foreground.getStyleClass().add("foreground");

        group.getChildren().addAll(background,foreground);
        button.setGraphic(group);
    }

    private void rectangles(StackPane stackPane){
        Rectangle outside = new Rectangle(60,90);
        outside.getStyleClass().add("Outside");

        Rectangle filledInside = new Rectangle(40,70);
        filledInside.getStyleClass().addAll("filled", "inside");

        Rectangle trainImage = new Rectangle(40,70);
        trainImage.getStyleClass().add("train-image");

        stackPane.getChildren().addAll(outside, filledInside,trainImage);
    }



}
