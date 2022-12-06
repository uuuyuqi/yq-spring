package com.yq.springframework.core.io;

import cn.hutool.core.lang.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * 位于文件系统的资源
 */
public class FileSystemResource implements Resource {

    private final String path;

    private final File file;

    private final Path filePath;

    public FileSystemResource(String path) {
        Assert.notNull(path, "Path must not be null");
        this.path = path;
        this.file = new File(path);
        this.filePath = this.file.toPath();
    }

    public FileSystemResource(File file) {
        Assert.notNull(file, "File must not be null");
        this.file = file;
        this.path = file.getPath();
        this.filePath = file.toPath();
    }

    public FileSystemResource(Path filePath) {
        Assert.notNull(filePath, "Path must not be null");
        this.path = filePath.toString();
        this.file = null;
        this.filePath = filePath;
    }

    /**
     * 直接获取文件输入流
     * @return filePath 获取的 流
     * @throws IOException IOE
     */
    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return Files.newInputStream(this.filePath);
        }
        catch (NoSuchFileException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    /**
     * 只读通道
     * @return channel
     * @throws IOException IOE
     */
    @Override
    public ReadableByteChannel readableChannel() throws IOException {
        try {
            return FileChannel.open(this.filePath, StandardOpenOption.READ);
        }
        catch (NoSuchFileException ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }
}
