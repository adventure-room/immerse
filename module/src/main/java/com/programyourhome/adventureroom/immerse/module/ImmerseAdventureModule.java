package com.programyourhome.adventureroom.immerse.module;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.regex.Pattern;

import com.programyourhome.adventureroom.dsl.regex.AbstractRegexDslAdventureModule;
import com.programyourhome.adventureroom.dsl.regex.RegexActionConverter;
import com.programyourhome.adventureroom.immerse.dsl.converters.PlayBackgroundMusicActionConverter;
import com.programyourhome.adventureroom.immerse.service.Immerse;

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

        this.config.deamons.put("Connect to Immerse client", () -> this.immerse.configure(this.config.host, this.config.port));
    }

    public Immerse getImmerse() {
        return this.immerse;
    }

    @Override
    public ImmerseConfig getConfig() {
        return this.config;
    }

    @Override
    protected Map<Pattern, RegexActionConverter<?>> getRegexActionConverters() {
        Map<Pattern, RegexActionConverter<?>> converters = new HashMap<>();
        Pattern pattern = Pattern.compile("play background music (?<id>[a-z]+)");
        converters.put(pattern, new PlayBackgroundMusicActionConverter());

        return converters;
    }

}
