package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


//non instanciable et package private
class MapViewCreator {

    private MapViewCreator(){}

    public static Node createMapView(/*ObservableGameState observable,ObjectProperty<ClaimRouteHandler> routeHandler, CardChooser cardChooser*/){
        Pane mapPane = new Pane();
        mapPane.getStylesheets().addAll("map.css","colors.css");
        mapPane.getChildren().add(new ImageView());
        for (Route r : ChMap.routes()) {
            //cree le groupe de la route
            Group gR = new Group();
            gR.setId(r.id());
            gR.getStyleClass().addAll("route",r.level().name(),r.color() == null? "NEUTRAL" : r.color().name());
            // create les cases du grp gR
            for (int i = 1; i <= r.length(); ++i) {
                //cree le rectangle representant la voie de la case
                Rectangle voie = new Rectangle(36,12);
                voie.getStyleClass().addAll("track","filled");

                // cree une groupe representant le wagon
                Rectangle voieW = new Rectangle(36,12);
                voieW.getStyleClass().add("filled");
                Circle c1 = new Circle(12,6,3);
                Circle c2 = new Circle(24,6,3);

                Group wagon = new Group();
                wagon.getStyleClass().add("car");
                wagon.getChildren().addAll(voieW, c1, c2);

                Group gC = new Group();
                gC.setId(String.format("%s_%s", r.id(),i));

                gC.getChildren().addAll(voie,wagon);
                gR.getChildren().add(gC);
            }
            //ajoute le groupe route a la mapPane
            mapPane.getChildren().add(gR);
        }
        return mapPane;
    }
}
