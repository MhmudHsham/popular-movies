package  com.hisham.mahmoud.popularmovies.provider;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;


    @SimpleSQLConfig(
            name = "MovieProvider",
            authority = "com.hisham.mahmoud.popularmovies.movie_provider.authority",
            database = "favoriate_movie_db.db",
            version = 5)
    public class MyProviderConfig implements ProviderConfig {
        @Override
        public UpgradeScript[] getUpdateScripts() {
            return new UpgradeScript[0];

    }
}
