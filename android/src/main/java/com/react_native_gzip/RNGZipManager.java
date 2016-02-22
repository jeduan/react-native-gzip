package com.react_native_gzip;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class RNGZipManager extends ReactContextBaseJavaModule {
    public RNGZipManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNGZipManager";
    }

    @ReactMethod
    public void gunzip(String source, String dest, Boolean force, Promise promise) {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) {
            promise.reject("-2", "file not found");
            return;
        }

        File destFolder = new File(dest);
        if (destFolder.exists()) {
            if (!force) {
                promise.reject("-2", "folder exists");
                return;
            }

            try {
                if (destFolder.isDirectory()) {
                    FileUtils.deleteDirectory(destFolder);
                } else {
                    destFolder.delete();
                }
                destFolder.mkdirs();
            } catch (IOException ex) {
                promise.reject("-2", "could not delete destination folder", ex);
            }
        }

        ArchiveInputStream inputStream = null;
        try {
            final FileInputStream fileInputStream = FileUtils.openInputStream(sourceFile);
            final CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.GZIP, fileInputStream);

            inputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.TAR, compressorInputStream);

            ArchiveEntry archiveEntry = inputStream.getNextEntry();
            while (archiveEntry != null) {
                File destFile = new File(destFolder, archiveEntry.getName());
                if (archiveEntry.isDirectory()) {
                    destFile.mkdirs();
                } else {
                    final FileOutputStream outputStream = FileUtils.openOutputStream(destFile);
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                }
                archiveEntry = inputStream.getNextEntry();
            }

            WritableMap map = Arguments.createMap();
            map.putString("path", destFolder.getAbsolutePath());
            promise.resolve(map);

        } catch (IOException e) {
            promise.reject("-2", e);
        } catch (ArchiveException e) {
            promise.reject("-2", "unable to open archive", e);
        } catch (CompressorException e) {
            promise.reject("-2", "unable to decompress file", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
