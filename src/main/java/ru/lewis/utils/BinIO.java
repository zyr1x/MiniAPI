package ru.lewis.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BinIO {
    public static void write(Path path, CheckedConsumer<DataOutputStream> out) {
        try (DataOutputStream dataOutputStream = new DataOutputStream(Files.newOutputStream(path))) {
            out.accept(dataOutputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error write", e);
        }
    }

    public static void read(Path path, CheckedConsumer<DataInputStream> input) {
        try (DataInputStream dataInputStream = new DataInputStream(Files.newInputStream(path))) {
            input.accept(dataInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error read", e);
        }
    }

}
