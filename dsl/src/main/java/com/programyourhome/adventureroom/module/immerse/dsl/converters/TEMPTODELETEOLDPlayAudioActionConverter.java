package com.programyourhome.adventureroom.module.immerse.dsl.converters;

public class TEMPTODELETEOLDPlayAudioActionConverter {

    private static final String ALL_SPEAKERS = "allSpeakers";
    private static final String AS_ONE_SPEAKER = "asOneSpeaker";
    private static final String AS_ALL_SPEAKERS = "asAllSpeakers";
    private static final String SOURCE_VARIABLE_PREFIX = "source";
    private static final String LISTENER_VARIABLE_PREFIX = "listener";
    private static final String ONCE = "once";
    private static final String FOREVER = "forever";

    // @Override
    // public Map<String, String> getRegexMap() {
    // String playContent = "play " + FILENAME;
    // String speakerCollection = this.either(" at speaker " + SPEAKER_ID, " at speakers " + SPEAKER_IDS, this.asVariable(ALL_SPEAKERS, " at all speakers "));
    // String sourceLocation = this.either(" at location " + SOURCE_STATIC_LOCATION,
    // " moving on path " + SOURCE_LOCATION_PATH + " with speed " + SOURCE_PATH_SPEED,
    // this.createLocationRegex(new CirclingRegexVariables(SOURCE_VARIABLE_PREFIX)));
    // String soundSource = this.either("", speakerCollection, sourceLocation);
    //
    // String listenerLocation = this.either("", " with listener at location " + LISTENER_STATIC_LOCATION,
    // " moving on path " + LISTENER_LOCATION_PATH + " with speed " + LISTENER_PATH_SPEED,
    // this.createLocationRegex(new CirclingRegexVariables(LISTENER_VARIABLE_PREFIX)));
    //
    // String normalize = this.either("", this.asVariable(AS_ONE_SPEAKER, " as one speaker"), this.asVariable(AS_ALL_SPEAKERS, " as all speakers "));
    //
    // String playback = this.either("", this.asVariable(ONCE, " once"), " repeat " + REPEAT + " times", this.asVariable(FOREVER, " forever"));
    //
    // return this.createRegexes(DEFAULT, playContent + this.optionalAtVolume() + soundSource + listenerLocation + normalize + playback);
    // }
    //
    // private String createLocationRegex(CirclingRegexVariables variables) {
    // return " circling" + this.optional(this.either(this.asVariable(variables.getClockWise(), " clockwise"),
    // this.asVariable(variables.getAntiClockWise(), " anti clockwise"))) +
    // " around " + variables.getLocation() + " with radius " + variables.getRadius() +
    // this.optional(" starting at angle " + variables.getStartAngle()) +
    // " with speed " + variables.getSpeed();
    // }
    //
    // @Override
    // public PlayAudioAction convert(MatchResult matchResult, Adventure adventure) {
    // PlayAudioAction action = new PlayAudioAction();
    // action.filename = matchResult.getValue(FILENAME);
    // Optional<Collection<Integer>> optionalSpeakerIds = Optional.empty();
    // if (matchResult.hasValue(SPEAKER_ID)) {
    // optionalSpeakerIds = Optional.of(Arrays.asList(Integer.valueOf(matchResult.getValue(SPEAKER_ID))));
    // } else if (matchResult.hasValue(SPEAKER_IDS)) {
    // optionalSpeakerIds = Optional.of(StreamEx.of(matchResult.getValue(SPEAKER_IDS).split(",")).map(Integer::valueOf).toList());
    // } else if (matchResult.hasValue(ALL_SPEAKERS)) {
    // optionalSpeakerIds = Optional.of(StreamEx.of(adventure.getExternalResources(SpeakerExternalResource.class)).map(Speaker::getId).toList());
    // }
    // optionalSpeakerIds.ifPresent(speakerIds -> action.soundSource = Either.firstOf5(speakerIds));
    //
    // matchResult.getOptionalValue(SOURCE_STATIC_LOCATION)
    // .map(this::parseLocation)
    // .ifPresent(vector -> action.soundSource = Either.secondOf5(vector));
    //
    // if (matchResult.hasValue(SOURCE_LOCATION_PATH)) {
    // Path path = action.new Path();
    // String[] locationStrings = matchResult.getValue(SOURCE_LOCATION_PATH).split(";");
    // path.waypoints = StreamEx.of(locationStrings).map(this::parseLocation).toList();
    // path.speed = Double.parseDouble(matchResult.getValue(SOURCE_PATH_SPEED));
    // }
    //
    // CirclingRegexVariables sourceCirclingRegexVariables = new CirclingRegexVariables(SOURCE_VARIABLE_PREFIX);
    // if (matchResult.hasValue(sourceCirclingRegexVariables.getLocation())) {
    // Circling circling = this.parseCircling(matchResult, action, sourceCirclingRegexVariables);
    // action.soundSource = Either.fourthOf5(circling);
    // }
    //
    // if (action.soundSource == null) {
    // action.soundSource = Either.fifthOf5(None.NONE);
    // }
    //
    // action.volume = matchResult.getOptionalValue(VOLUME).map(Integer::valueOf);
    // return action;
    // }

    // private Circling parseCircling(MatchResult matchResult, PlayAudioAction action, CirclingRegexVariables sourceCirclingRegexVariables) {
    // Circling circling = action.new Circling();
    // circling.center = this.parseLocation(matchResult.getValue(sourceCirclingRegexVariables.getLocation()));
    // circling.radius = Double.parseDouble(matchResult.getValue(sourceCirclingRegexVariables.getRadius()));
    // circling.startAngle = matchResult.getOptionalValue(sourceCirclingRegexVariables.getStartAngle()).map(Double::parseDouble);
    // matchResult.getOptionalValue(sourceCirclingRegexVariables.getClockWise()).ifPresent(clockwise -> circling.clockwise = Optional.of(true));
    // matchResult.getOptionalValue(sourceCirclingRegexVariables.getAntiClockWise()).ifPresent(clockwise -> circling.clockwise = Optional.of(false));
    // circling.speed = Double.parseDouble(matchResult.getValue(sourceCirclingRegexVariables.getSpeed()));
    // return circling;
    // }
    //
    // private Vector3D parseLocation(String locationString) {
    // List<Double> coordinates = StreamEx.of(locationString.split(",")).map(Double::parseDouble).toList();
    // return new Vector3D(coordinates);
    // }

    // private class CirclingRegexVariables {
    // private static final String CIRCLING = "Circling";
    // private final String prefix;
    //
    // public CirclingRegexVariables(String prefix) {
    // this.prefix = prefix;
    // }
    //
    // public String getClockWise() {
    // return this.prefix + CIRCLING + "Clockwise";
    // }
    //
    // public String getAntiClockWise() {
    // return this.prefix + CIRCLING + "AntiClockwise";
    // }
    //
    // public RegexVariable getLocation() {
    // return LOCATION.withName(this.prefix + CIRCLING + "Location");
    // }
    //
    // public RegexVariable getRadius() {
    // return DOUBLE.withName(this.prefix + CIRCLING + "Radius");
    // }
    //
    // public RegexVariable getStartAngle() {
    // return DOUBLE.withName(this.prefix + CIRCLING + "StartAngle");
    // }
    //
    // public RegexVariable getSpeed() {
    // return DOUBLE.withName(this.prefix + CIRCLING + "Speed");
    // }
    //
    // }

}
