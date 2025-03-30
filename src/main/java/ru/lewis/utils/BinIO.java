package ru.lewis.utils;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;
import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class BinIO {
    public static <R> R read(Path input, Decoder<R> decoder) {
        try {
            return BinIO.read(Files.newInputStream(input), decoder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <R> R read(InputStream input, Decoder<R> decoder) {
        Inflater inf = new Inflater(true);
        try (DataInputStream in = new DataInputStream(new FastBufferedInputStream(new InflaterInputStream(input, inf)))) {
            return decoder.decode(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            inf.end();
        }
    }

    public static void write(Path output, Encoder encoder) {
        try {
            BinIO.write(Files.newOutputStream(output), encoder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(OutputStream output, Encoder encoder) {
        Deflater def = new Deflater(9, true);
        try (DataOutputStream out = new DataOutputStream(new FastBufferedOutputStream(new DeflaterOutputStream(output, def, true)))) {
            encoder.encode(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            def.end();
        }
    }

    @FunctionalInterface
    public interface Decoder<R> {

        R decode(DataInputStream in) throws IOException;
    }


    @FunctionalInterface
    public interface Encoder {

        void encode(DataOutputStream out) throws IOException;
    }
}
