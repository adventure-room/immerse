package com.programyourhome.adventureroom.module.immerse.model;

import com.programyourhome.adventureroom.model.resource.AbstractExternalResource;
import com.programyourhome.immerse.domain.format.ImmerseAudioFormat;

public class OutputFormatExternalResource extends AbstractExternalResource<ImmerseAudioFormat> {

    public OutputFormatExternalResource(ImmerseAudioFormat outputFormat) {
        super(outputFormat);
    }

    public ImmerseAudioFormat getOutputFormat() {
        return this.getWrappedObject();
    }

    @Override
    public String getId() {
        ImmerseAudioFormat format = this.getOutputFormat();
        return format.getRecordingMode() + "-" + format.getSampleRate() + "-" + format.getSampleSize()
                + "-" + format.getByteOrder() + "-" + (format.isSigned() ? "signed" : "unsigned");
    }

    @Override
    public String getName() {
        return this.getId();
    }

    @Override
    public String getDescription() {
        return this.getId();
    }

}
