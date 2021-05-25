package ch.epfl.tchu.gui;

final class GuiConstants {

    private GuiConstants() {}

    //The constant int values //TODO : trouver mieux comme comm
    static final int OUTSIDE_RECTANGLE_WIDTH = 60; //TODO : ameliorer les noms pour les rectangles prcq on voit ps ils correspondent a quoi
    static final int OUTSIDE_RECTANGLE_HEIGHT = 90;
    static final int INSIDE_RECTANGLE_WIDTH = 40;
    static final int INSIDE_RECTANGLE_HEIGHT = 70;
    static final int GAUGE_RECTANGLE_WIDTH = 50;
    static final int GAUGE_RECTANGLE_HEIGHT = 5;
    static final int MINIMUM_VISIBLE_CARD = 0;
    static final int MINIMUM_VISIBLE_TEXT = 1;
    static final int CIRCLE_RADIUS_INFO = 5;

    // The map view components' dimensions
    static final int TRACK_WIDTH = 36;
    static final int TRACK_HEIGHT = 12;
    static final int CAR_CIRCLES_RADIUS = 3;
    static final int CAR_CIRCLES_CENTRE_Y = 6;
    static final int CAR_CIRCLE1_CENTRE_X = 12;
    static final int CAR_CIRCLE2_CENTRE_X = 24;

    // The style sheets
    static final String DECK_SS = "decks.css";
    static final String COLORS_SS = "colors.css";
    static final String MAP_SS = "map.css";
    static final String INFO_SS = "info.css";
    static final String CHOOSER_SS = "chooser.css";

    // The style classes
    static final String NEUTRAL_SC = "NEUTRAL";
    static final String FILLED_SC = "filled";
    static final String COUNT_SC = "count";
    static final String CARD_SC = "card";
    static final String OUTSIDE_SC = "outside";
    static final String INSIDE_SC = "inside";
    static final String BACKGROUND_SC = "background";
    static final String FOREGROUND_SC = "foreground";
    static final String GAUGED_SC ="gauged";
    static final String TRAIN_IMAGE_SC = "train-image";
    static final String ROUTE_SC = "route";
    static final String TRACK_SC = "track";
    static final String CAR_SC = "car";

    // The ID setters
    static final String HAND_PANE_ID = "hand-pane";
    static final String CARD_PANE_ID = "card-pane";
    static final String TICKETS_ID = "tickets";
    static final String PLAYER_STATS_ID = "player-stats";
    static final String GAME_INFO_ID = "game-info";
}
