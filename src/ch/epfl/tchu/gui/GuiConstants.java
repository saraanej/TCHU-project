package ch.epfl.tchu.gui;

final class GuiConstants {

    private GuiConstants() {}

    //The constant int values //TODO : trouver mieux comme comm
    public static final int OUTSIDE_RECTANGLE_WIDTH = 60; //TODO : ameliorer les noms pour les rectangles prcq on voit ps ils correspondent a quoi
    public static final int OUTSIDE_RECTANGLE_HEIGHT = 90;
    public static final int INSIDE_RECTANGLE_WIDTH = 40;
    public static final int INSIDE_RECTANGLE_HEIGHT = 70;
    public static final int GAUGE_RECTANGLE_WIDTH = 50;
    public static final int GAUGE_RECTANGLE_HEIGHT = 5;
    public static final int MINIMUM_VISIBLE_CARD = 0;
    public static final int MINIMUM_VISIBLE_TEXT = 1;
    public final static int CIRCLE_RADIUS_INFO = 5;

    // The map view components' dimensions
    public static final int TRACK_WIDTH = 36;
    public static final int TRACK_HEIGHT = 12;
    public static final int CAR_CIRCLE_RADIUS = 3;
    public static final int CAR_CIRCLE_CENTRE_Y = 6;
    public static final int CAR_CIRCLE1_CENTRE_X = 12;
    public static final int CAR_CIRCLE2_CENTRE_X = 24;

    // The style sheets
    public static final String DECK_SS = "decks.css";
    public static final String COLORS_SS = "colors.css";
    public static final String MAP_SS = "map.css";
    public static final String INFO_SS = "info.css";

    // The style classes
    public static final String NEUTRAL_SC = "NEUTRAL";
    public static final String FILLED_SC = "filled";
    public static final String COUNT_SC = "count";
    public static final String CARD_SC = "card";
    public static final String OUTSIDE_SC = "outside";
    public static final String INSIDE_SC = "inside";
    public static final String BACKGROUND_SC = "background";
    public static final String FOREGROUND_SC = "foreground";
    public static final String GAUGED_SC ="gauged";
    public static final String TRAIN_IMAGE_SC = "train-image";
    public static final String ROUTE_SC = "route";
    public static final String TRACK_SC = "track";
    public static final String CAR_SC = "car";

    // The ID setters
    public static final String HAND_PANE_ID = "hand-pane";
    public static final String CARD_PANE_ID = "card-pane";
    public static final String TICKETS_ID = "tickets";
    public static final String PLAYER_STATS_ID = "player-stats";
    public static final String GAME_INFO_ID = "game-info";
}
