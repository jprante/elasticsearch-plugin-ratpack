/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.embed.internal;

import ratpack.embed.BaseDirBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;

import static ratpack.util.ExceptionUtils.uncheck;

public class PathBaseDirBuilder implements BaseDirBuilder {

    private final Path baseDir;

    public PathBaseDirBuilder(File baseDir) {
        this(baseDir.toPath());
    }

    public PathBaseDirBuilder(Path baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public Path file(String path) {
        return toUsablePath(path);
    }

    @Override
    public Path file(String path, String content) {
        Path file = file(path);
        try {
            Files.write(file, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            // zip file system doesn't update on write over existing file
            Files.setLastModifiedTime(file, FileTime.fromMillis(System.currentTimeMillis()));
        } catch (IOException e) {
            throw uncheck(e);
        }
        return file;
    }

    @Override
    public Path dir(String path) {
        Path file = toUsablePath(path);
        try {
            Files.createDirectory(file);
        } catch (IOException e) {
            throw uncheck(e);
        }
        return file;
    }

    @Override
    public void close() throws IOException {
        FileSystem fileSystem = baseDir.getFileSystem();
        if (!fileSystem.equals(FileSystems.getDefault())) {
            fileSystem.close();
        }
    }

    @Override
    public Path build() {
        return baseDir;
    }

    private Path toUsablePath(String path) {
        Path p = baseDir.resolve(path);
        try {
            Files.createDirectories(p.getParent());
        } catch (IOException e) {
            throw uncheck(e);
        }
        return p;
    }
}