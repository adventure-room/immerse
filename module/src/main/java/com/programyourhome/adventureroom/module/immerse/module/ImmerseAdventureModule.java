package com.programyourhome.adventureroom.module.immerse.module;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.programyourhome.adventureroom.dsl.antlr.AbstractAntlrDslAdventureModule;
import com.programyourhome.adventureroom.model.Adventure;
import com.programyourhome.adventureroom.model.execution.ExecutionContext;
import com.programyourhome.adventureroom.model.resource.ResourceDescriptor;
import com.programyourhome.adventureroom.module.immerse.model.OutputFormatExternalResource;
import com.programyourhome.adventureroom.module.immerse.model.RoomExternalResource;
import com.programyourhome.adventureroom.module.immerse.model.SoundCardExternalResource;
import com.programyourhome.adventureroom.module.immerse.model.SpeakerExternalResource;
import com.programyourhome.adventureroom.module.immerse.service.Immerse;
import com.programyourhome.immerse.domain.ImmerseSettings;
import com.programyourhome.immerse.domain.Room;
import com.programyourhome.immerse.domain.audio.soundcard.SoundCard;
import com.programyourhome.immerse.domain.format.ImmerseAudioFormat;
import com.programyourhome.immerse.domain.location.Vector3D;
import com.programyourhome.immerse.domain.speakers.Speaker;

import one.util.streamex.StreamEx;

public class ImmerseAdventureModule extends AbstractAntlrDslAdventureModule {

    public static final String ID = "immerse";

    private final Immerse immerse;
    private ImmerseConfig config;
    private ImmerseSettings immerseSettings;

    public ImmerseAdventureModule() {
        super("Immerse");
        this.immerse = this.loadApiImpl(Immerse.class);
        this.initConfig();
    }

    private void initConfig() {
        this.config = new ImmerseConfig();
        this.config.id = ID;
        this.config.name = "Immerse";
        this.config.description = "Module to use the Immerse service";

        ResourceDescriptor<SpeakerExternalResource> speakersDescriptor = new ResourceDescriptor<>();
        speakersDescriptor.id = "speakers";
        speakersDescriptor.name = "Speakers";
        speakersDescriptor.clazz = SpeakerExternalResource.class;
        this.config.addResourceDescriptor(speakersDescriptor);

        ResourceDescriptor<RoomExternalResource> roomDescriptor = new ResourceDescriptor<>();
        roomDescriptor.id = "rooms";
        roomDescriptor.name = "Rooms";
        roomDescriptor.clazz = RoomExternalResource.class;
        this.config.addResourceDescriptor(roomDescriptor);

        ResourceDescriptor<SoundCardExternalResource> soundCardDescriptor = new ResourceDescriptor<>();
        soundCardDescriptor.id = "soundcards";
        soundCardDescriptor.name = "Sound Cards";
        soundCardDescriptor.clazz = SoundCardExternalResource.class;
        this.config.addResourceDescriptor(soundCardDescriptor);

        ResourceDescriptor<OutputFormatExternalResource> outputFormatDescriptor = new ResourceDescriptor<>();
        outputFormatDescriptor.id = "outputformats";
        outputFormatDescriptor.name = "Output Formats";
        outputFormatDescriptor.clazz = OutputFormatExternalResource.class;
        this.config.addResourceDescriptor(outputFormatDescriptor);

        this.config.addConverter(String.class, Vector3D.class, input -> {
            String[] coordinates = input.split(",");
            Function<Integer, Integer> parseCoordinate = index -> Integer.parseInt(coordinates[index].trim());
            return new Vector3D(parseCoordinate.apply(0), parseCoordinate.apply(1), parseCoordinate.apply(2));
        });

        this.config.addTask("Connect to Immerse client", () -> this.immerse.connect(this.immerseSettings, this.config.host, this.config.port));
    }

    @Override
    public void start(Adventure adventure, ExecutionContext context) {
        Map<String, RoomExternalResource> rooms = adventure.getResourceMap(RoomExternalResource.class);
        if (rooms.size() != 1) {
            throw new IllegalStateException("There must be exactly 1 room configured for Immerse, not " + rooms.size());
        }
        Room roomWithoutSpeakers = rooms.values().iterator().next().getRoom();

        Map<String, SpeakerExternalResource> speakerExternalResources = adventure.getResourceMap(SpeakerExternalResource.class);
        if (speakerExternalResources.isEmpty()) {
            throw new IllegalStateException("There must be 1 or more speakers configured for Immerse");
        }
        Set<Speaker> speakers = StreamEx.of(speakerExternalResources.values())
                .map(SpeakerExternalResource::getSpeaker)
                .toSet();
        Room room = Room.builder()
                .name(roomWithoutSpeakers.getName())
                .description(roomWithoutSpeakers.getDescription())
                .dimensions(roomWithoutSpeakers.getDimensions())
                .addSpeakers(speakers)
                .build();

        Map<String, SoundCardExternalResource> soundCardExternalResources = adventure.getResourceMap(SoundCardExternalResource.class);
        if (soundCardExternalResources.isEmpty()) {
            throw new IllegalStateException("There must be 1 or more sound cards configured for Immerse");
        }
        Set<SoundCard> soundCards = StreamEx.of(soundCardExternalResources.values())
                .map(SoundCardExternalResource::getSoundCard)
                .toSet();

        Map<String, OutputFormatExternalResource> outputFormats = adventure.getResourceMap(OutputFormatExternalResource.class);
        if (outputFormats.size() != 1) {
            throw new IllegalStateException("There must be exactly 1 output format configured for Immerse, not " + outputFormats.size());
        }
        ImmerseAudioFormat outputFormat = outputFormats.values().iterator().next().getOutputFormat();

        this.immerseSettings = ImmerseSettings.builder()
                .room(room)
                .soundCards(soundCards)
                .outputFormat(outputFormat)
                .build();
    }

    public Immerse getImmerse() {
        return this.immerse;
    }

    @Override
    public ImmerseConfig getConfig() {
        return this.config;
    }

    @Override
    public void stop(Adventure adventure, ExecutionContext context) {
        this.immerse.quit();
    }

}
