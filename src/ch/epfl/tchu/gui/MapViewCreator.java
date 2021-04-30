package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;



//non instanciable et package private
class MapViewCreator {

    private MapViewCreator(){}

    //question is it static?
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
                Group gC = new Group();
                gC.setId(String.format("%s_%s", r.id(),i));

            }
            //ajoute le groupe route a la mapPane
            mapPane.getChildren().add(gR);
        }
        return mapPane;
    }


    
}
