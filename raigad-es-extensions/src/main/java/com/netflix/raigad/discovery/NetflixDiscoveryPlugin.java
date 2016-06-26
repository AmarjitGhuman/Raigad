/**
 * Copyright 2016 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.raigad.discovery;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.discovery.DiscoveryModule;
import org.elasticsearch.plugins.Plugin;

public class NetflixDiscoveryPlugin extends Plugin {
    protected final ESLogger logger = Loggers.getLogger(NetflixDiscoveryPlugin.class);
    private final Settings settings;

    public NetflixDiscoveryPlugin(Settings settings) {
        this.settings = settings;
        logger.info("Starting Netflix custom discovery");
    }

    public static boolean discoveryEnabled(Settings settings, ESLogger logger) {
        if (NetflixDiscovery.DISCOVERY_TYPE.equalsIgnoreCase(settings.get(NetflixDiscovery.DISCOVERY_TYPE_LOCATION))) {
            logger.info("Netflix custom discovery is [ON]");
            return true;
        }

        logger.info("To enable Netflix custom discovery set {} to \"{}\"",
                NetflixDiscovery.DISCOVERY_TYPE_LOCATION, NetflixDiscovery.DISCOVERY_TYPE);

        return false;
    }

    @Override
    public String name() {
        return NetflixDiscovery.DISCOVERY_TYPE;
    }

    @Override
    public String description() {
        return NetflixDiscovery.DISCOVERY_DESCRIPTION;
    }

    public void onModule(DiscoveryModule discoveryModule) {
        if (discoveryEnabled(settings, logger)) {
            discoveryModule.addDiscoveryType(NetflixDiscovery.DISCOVERY_TYPE, NetflixDiscovery.class);
            discoveryModule.addUnicastHostProvider(NetflixDiscoveryUnicastHostsProvider.class);
        }
    }
}
