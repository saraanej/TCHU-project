package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static ch.epfl.tchu.gui.GuiConstants.*;

import java.util.List;

/**
 * The MapViewCreator class of the ch.epfl.tchu.gui package, non-instantiable and package private,
 * contains a single public method allowing to create the map view.
 *
 * @author Yasmin Ben Rahhal (329912)
 * @author Sara Anejjar (329905)
 */
final class MapViewCreator {

    private final static String EMPTY_STRING = "";

    private MapViewCreator() {
    }

    /**
     * @param observable   the observable gameState.
     * @param routeHandler a property containing the action manager to use
     *                     when the player wants to seize a road.
     * @param cardChooser  a card selector handler.
     * @return the Node containing the map view of the game.
     */
    public static Node createMapView(ObservableGameState observable,
                                     ObjectProperty<ActionHandlers.ClaimRouteHandler> routeHandler,
                                     CardChooser cardChooser) {
        Pane mapPane = new Pane();
        mapPane.getStylesheets().addAll(MAP_SS, COLORS_SS);
        mapPane.getChildren().add(new ImageView());

        for (Route r : ChMap.routes()) {
            //creates the group of the Route
            Group groupRoute = new Group();
            groupRoute.setId(r.id());
            groupRoute.getStyleClass().addAll(EMPTY_STRING, ROUTE_SC, r.level().name(),
                    r.color() == null ? NEUTRAL_SC : r.color().name());


            observable.routeOwner(r).addListener((o, oV, nV) -> {
                if (nV != null) groupRoute.getStyleClass().set(0, nV.name());
            });

            groupRoute.disableProperty().bind(
                    routeHandler.isNull().or(observable.canClaimRoute(r).not()));

            groupRoute.setOnMouseClicked(e -> {
                List<SortedBag<Card>> possibleClaimCards = observable.possibleClaimCards(r);
                if (possibleClaimCards.size() == 1)
                    routeHandler.get().onClaimRoute(r, possibleClaimCards.get(0));
                else cardChooser.chooseCards(possibleClaimCards,
                        chosenCards -> routeHandler.get().onClaimRoute(r, chosenCards));
            });

            //creates the boxes of the group groupRoute
            for (int i = 1; i <= r.length(); ++i)
                createBox(groupRoute, String.format("%s_%s", r.id(), i));

            //Adds the groupRoute to the mapPane
            mapPane.getChildren().add(groupRoute);
        }
        return mapPane;
    }

    private static void createBox(Group groupRoute, String id) {
        //creates the rectangle representing the track of the box
        Rectangle trackBox = new Rectangle(TRACK_WIDTH, TRACK_HEIGHT);
        trackBox.getStyleClass().addAll(TRACK_SC, FILLED_SC);

        //creates the rectangle and the circles of the wagon car
        Rectangle trackWagon = new Rectangle(TRACK_WIDTH, TRACK_HEIGHT);
        trackWagon.getStyleClass().add(FILLED_SC);
        Circle circle1 = new Circle(CAR_CIRCLE1_CENTRE_X, CAR_CIRCLES_CENTRE_Y, CAR_CIRCLES_RADIUS);
        Circle circle2 = new Circle(CAR_CIRCLE2_CENTRE_X, CAR_CIRCLES_CENTRE_Y, CAR_CIRCLES_RADIUS);

        //creates a group representing the wagon car
        Group wagon = new Group();
        wagon.getStyleClass().add(CAR_SC);
        wagon.getChildren().addAll(trackWagon, circle1, circle2);

        //creates the group of the box
        Group groupBox = new Group();
        groupBox.setId(id);

        //adds the box to the groupRoute's children
        groupBox.getChildren().addAll(trackBox, wagon);
        groupRoute.getChildren().add(groupBox);
    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ActionHandlers.ChooseCardsHandler handler);
    }
}
