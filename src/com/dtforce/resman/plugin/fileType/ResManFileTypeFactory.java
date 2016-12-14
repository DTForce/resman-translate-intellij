package com.dtforce.resman.plugin.fileType;

import com.intellij.openapi.fileTypes.*;
import org.jetbrains.annotations.NotNull;

public class ResManFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(ResManFileType.INSTANCE, "resman");
    }
}