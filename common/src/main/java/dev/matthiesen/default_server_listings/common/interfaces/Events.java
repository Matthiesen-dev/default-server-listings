package dev.matthiesen.default_server_listings.common.interfaces;

import dev.matthiesen.matthiesen_core.common.api.events.EventObservable;

public final class Events {
    public static final EventObservable<ClientStarted> CLIENT_STARTED = new EventObservable<>();
}
