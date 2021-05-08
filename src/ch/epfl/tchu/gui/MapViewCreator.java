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

import java.util.List;

/**
 *
 */
final class MapViewCreator {

    //TODO : creer les constantes static des nbres utilises

    private MapViewCreator(){}

    public static Node createMapView(ObservableGameState observable ,
                                     ObjectProperty<ActionHandlers.ClaimRouteHandler> routeHandler,
                                     CardChooser cardChooser){
        Pane mapPane = new Pane();
        mapPane.getStylesheets().addAll("map.css","colors.css");
        mapPane.getChildren().add(new ImageView());
        for (Route r : ChMap.routes()) {
            //cree le groupe de la route
            Group groupRoute = new Group();
            groupRoute.setId(r.id());
            groupRoute.getStyleClass().addAll("route",r.level().name(),
                    r.color() == null ? "NEUTRAL" : r.color().name());

            // create les cases du grp groupRoute
            for (int i = 1; i <= r.length(); ++i) {
                //cree le rectangle representant la voieCase de la case
                Rectangle voieCase = new Rectangle(36,12);
                voieCase.getStyleClass().addAll("track","filled");

                // cree le retangle et les cercles composants le wagon
                Rectangle voieWagon = new Rectangle(36,12);
                voieWagon.getStyleClass().add("filled");
                Circle circle1 = new Circle(12,6,3);
                Circle circle2 = new Circle(24,6,3);

                // cree un groupe representant le wagon
                Group wagon = new Group();
                wagon.getStyleClass().add("car");
                wagon.getChildren().addAll(voieWagon, circle1, circle2);

                // cree groupe case
                Group groupCase = new Group();
                groupCase.setId(String.format("%s_%s", r.id(),i));

                groupCase.getChildren().addAll(voieCase,wagon);
                groupRoute.getChildren().add(groupCase);

                observable.routeOwner(r).addListener((o,oV,nV) -> {
                    if( nV != null) groupRoute.getStyleClass().add(nV.name());
                });
                groupRoute.disableProperty().bind(
                        routeHandler.isNull().or(observable.canClaimRoute(r).not()));

                groupRoute.setOnMouseClicked(e -> {
                    List<SortedBag<Card>> possibleClaimCards = observable.possibleClaimCards(r);
                    if(possibleClaimCards.size() == 1)
                        routeHandler.get().onClaimRoute(r,possibleClaimCards.get(0));
                    else cardChooser.chooseCards(possibleClaimCards,
                            chosenCards -> routeHandler.get().onClaimRoute(r, chosenCards));
                });
            }
            //ajoute le groupe route a la mapPane
            mapPane.getChildren().add(groupRoute);
        }
        return mapPane;
    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ActionHandlers.ChooseCardsHandler handler);
    }
}
