package com.programyourhome.adventureroom.immerse.module;

import java.util.Arrays;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Function;

import com.programyourhome.adventureroom.dsl.regex.AbstractRegexDslAdventureModule;
import com.programyourhome.adventureroom.dsl.regex.RegexActionConverter;
import com.programyourhome.adventureroom.immerse.dsl.converters.PlayBackgroundMusicActionConverter;
import com.programyourhome.adventureroom.immerse.dsl.converters.StopBackgroundMusicActionConverter;
import com.programyourhome.adventureroom.immerse.model.SpeakerExternalResource;
import com.programyourhome.adventureroom.immerse.service.Immerse;
import com.programyourhome.adventureroom.model.resource.ResourceDescriptor;
import com.programyourhome.immerse.domain.location.Vector3D;

public class ImmerseAdventureModule extends AbstractRegexDslAdventureModule {

    public static final String ID = "immerse";

    private Immerse immerse;
    private ImmerseConfig config;

    public ImmerseAdventureModule() {
        // TODO: move to util
        // We assume there will be one implementation available on the classpath. If not, behavior is undefined.
        ServiceLoader.load(Immerse.class).forEach(impl -> this.immerse = impl);
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

        this.config.addConverter(String.class, Vector3D.class, input -> {
            String[] coordinates = input.split(",");
            Function<Integer, Integer> parseCoordinate = index -> Integer.parseInt(coordinates[index].trim());
            return new Vector3D(parseCoordinate.apply(0), parseCoordinate.apply(1), parseCoordinate.apply(2));
        });

        this.config.addTask("Connect to Immerse client", () -> this.immerse.connect(this.config.host, this.config.port));
    }

    public Immerse getImmerse() {
        return this.immerse;
    }

    @Override
    public ImmerseConfig getConfig() {
        return this.config;
    }

    @Override
    protected Collection<RegexActionConverter<?>> getRegexActionConverters() {
        return Arrays.asList(new PlayBackgroundMusicActionConverter(),
                new StopBackgroundMusicActionConverter());
    }

    @Override
    public void stop() {
        this.immerse.quit();
    }

}
