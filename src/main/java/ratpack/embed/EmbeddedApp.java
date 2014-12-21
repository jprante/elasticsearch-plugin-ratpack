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

package ratpack.embed;

import org.slf4j.LoggerFactory;
import ratpack.embed.internal.LaunchConfigEmbeddedApp;
import ratpack.func.Action;
import ratpack.func.Function;
import ratpack.handling.Chain;
import ratpack.handling.Handler;
import ratpack.handling.Handlers;
import ratpack.launch.HandlerFactory;
import ratpack.launch.LaunchConfig;
import ratpack.launch.LaunchConfigBuilder;
import ratpack.server.RatpackServer;

import java.nio.file.Path;

import static ratpack.util.ExceptionUtils.uncheck;

/**
 * An application created and used at runtime.
 */
public interface EmbeddedApp extends AutoCloseable {

    /**
     * Creates an embedded application by building a {@link LaunchConfig}.
     * <p>
     * The given {@link LaunchConfigBuilder} will be configured to not have base dir, and to use an ephemeral port.
     *
     * @param function a function that builds a launch config from a launch config builder
     * @return a newly created embedded application
     */
    static EmbeddedApp fromLaunchConfigBuilder(Function<? super LaunchConfigBuilder, ? extends LaunchConfig> function) {
        return new LaunchConfigEmbeddedApp() {
            @Override
            protected LaunchConfig createLaunchConfig() {
                return uncheck(() -> function.apply(LaunchConfigBuilder.noBaseDir().development(true).port(0)));
            }
        };
    }

    /**
     * Creates an embedded application by building a {@link LaunchConfig} with the given base dir.
     * <p>
     * The given {@link LaunchConfigBuilder} will be configured to use an ephemeral port.
     *
     * @param baseDir  the base dir for the embedded app
     * @param function a function that builds a launch config from a launch config builder
     * @return a newly created embedded application
     */
    static EmbeddedApp fromLaunchConfigBuilder(Path baseDir, Function<? super LaunchConfigBuilder, ? extends LaunchConfig> function) {
        return new LaunchConfigEmbeddedApp() {
            @Override
            protected LaunchConfig createLaunchConfig() {
                return uncheck(() -> function.apply(LaunchConfigBuilder.baseDir(baseDir).development(true).port(0)));
            }
        };
    }

    /**
     * Creates an embedded application with a default launch config (no base dir, ephemeral port) and the given handler.
     * <p>
     * If you need to tweak the launch config, use {@link #fromLaunchConfigBuilder(Path, Function)}.
     *
     * @param handlerFactory a handler factory
     * @return a newly created embedded application
     */
    static EmbeddedApp fromHandlerFactory(HandlerFactory handlerFactory) {
        return fromLaunchConfigBuilder(lcb -> lcb.build(handlerFactory::create));
    }

    /**
     * Creates an embedded application with a default launch config (ephemeral port) and the given handler.
     * <p>
     * If you need to tweak the launch config, use {@link #fromLaunchConfigBuilder(Path, Function)}.
     *
     * @param baseDir        the base dir for the embedded app
     * @param handlerFactory a handler factory
     * @return a newly created embedded application
     */
    static EmbeddedApp fromHandlerFactory(Path baseDir, HandlerFactory handlerFactory) {
        return fromLaunchConfigBuilder(baseDir, lcb -> lcb.build(handlerFactory::create));
    }

    /**
     * Creates an embedded application with a default launch config (no base dir, ephemeral port) and the given handler.
     * <p>
     * If you need to tweak the launch config, use {@link #fromLaunchConfigBuilder(Function)}.
     *
     * @param handler the application handler
     * @return a newly created embedded application
     */
    static EmbeddedApp fromHandler(Handler handler) {
        return fromLaunchConfigBuilder(lcb -> lcb.build(lc -> handler));
    }

    /**
     * Creates an embedded application with a default launch config (ephemeral port) and the given handler.
     * <p>
     * If you need to tweak the launch config, use {@link #fromLaunchConfigBuilder(Path, Function)}.
     *
     * @param baseDir the base dir for the embedded app
     * @param handler the application handler
     * @return a newly created embedded application
     */
    static EmbeddedApp fromHandler(Path baseDir, Handler handler) {
        return fromLaunchConfigBuilder(baseDir, lcb -> lcb.build(lc -> handler));
    }

    /**
     * Creates an embedded application with a default launch config (no base dir, ephemeral port) and the given handler chain.
     * <p>
     * If you need to tweak the launch config, use {@link #fromLaunchConfigBuilder(Function)}.
     *
     * @param action the handler chain definition
     * @return a newly created embedded application
     */
    static EmbeddedApp fromChain(Action<? super Chain> action) {
        return fromLaunchConfigBuilder(lcb -> lcb.build(lc -> Handlers.chain(lc, action)));
    }

    /**
     * The server for the application.
     * <p>
     * Calling this method does not implicitly start the server.
     *
     * @return The server for the application
     */
    RatpackServer getServer();

    /**
     * Stops the server returned by {@link #getServer()}.
     * <p>
     * Exceptions thrown by calling {@link RatpackServer#stop()} are suppressed and written to {@link System#err System.err}.
     */
    @Override
    default public void close() {
        try {
            getServer().stop();
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error("", e);
        }
    }

}