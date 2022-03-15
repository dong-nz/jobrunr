package org.jobrunr.spring.autoconfigure;

import org.jobrunr.jobs.details.CachingJobDetailsGenerator;
import org.jobrunr.jobs.filters.RetryFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = "org.jobrunr")
public class JobRunrProperties {

    private Database database = new Database();

    private Jobs jobs = new Jobs();

    private JobScheduler jobScheduler = new JobScheduler();

    private Dashboard dashboard = new Dashboard();

    private BackgroundJobServer backgroundJobServer = new BackgroundJobServer();

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Jobs getJobs() {
        return jobs;
    }

    public void setJobs(Jobs jobs) {
        this.jobs = jobs;
    }

    public JobScheduler getJobScheduler() {
        return jobScheduler;
    }

    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public BackgroundJobServer getBackgroundJobServer() {
        return backgroundJobServer;
    }

    public void setBackgroundJobServer(BackgroundJobServer backgroundJobServer) {
        this.backgroundJobServer = backgroundJobServer;
    }

    /**
     * JobRunr dashboard related settings. These settings may not have an effect for certain NoSQL Databases (e.g. Redis).
     */
    public static class Database {
        /**
         * Allows to skip the creation of the tables - this means you should add them manually or by database migration tools like FlywayDB.
         */
        private boolean skipCreate = false;

        /**
         * The name of the database to use (only used by MongoDBStorageProvider). By default, it is 'jobrunr'.
         */
        private String databaseName;

        /**
         * Allows to set the table prefix used by JobRunr
         */
        private String tablePrefix;

        /**
         * An optional named {@link javax.sql.DataSource} to use. Defaults to the 'default' datasource.
         */
        private String datasource;

        /**
         * If multiple types of databases are available in the Spring Context (e.g. a DataSource and an Elastic RestHighLevelClient), this setting allows to specify the type of database for JobRunr to use.
         * Valid values are 'sql', 'mongodb', 'redis-lettuce', 'redis-jedis' and 'elasticsearch'.
         */
        private String type;

        public void setSkipCreate(boolean skipCreate) {
            this.skipCreate = skipCreate;
        }

        public boolean isSkipCreate() {
            return skipCreate;
        }

        public String getTablePrefix() {
            return tablePrefix;
        }

        public void setTablePrefix(String tablePrefix) {
            this.tablePrefix = tablePrefix;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        public String getDatasource() {
            return datasource;
        }

        public void setDatasource(String datasource) {
            this.datasource = datasource;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /**
     * JobRunr job related settings
     */
    public static class Jobs {

        /**
         * Configures the default amount of retries.
         */
        private int defaultNumberOfRetries = RetryFilter.DEFAULT_NBR_OF_RETRIES;

        /**
         * Configures the seed for the exponential back-off when jobs are retried in case of an Exception.
         */
        private int backOffTimeSeed = RetryFilter.DEFAULT_BACKOFF_POLICY_TIME_SEED;

        public int getDefaultNumberOfRetries() {
            return defaultNumberOfRetries;
        }

        public void setDefaultNumberOfRetries(int defaultNumberOfRetries) {
            this.defaultNumberOfRetries = defaultNumberOfRetries;
        }

        public int getRetryBackOffTimeSeed() {
            return backOffTimeSeed;
        }

        public void setRetryBackOffTimeSeed(int backOffTimeSeed) {
            this.backOffTimeSeed = backOffTimeSeed;
        }
    }

    /**
     * JobRunr JobScheduler related settings
     */
    public static class JobScheduler {

        /**
         * Enables the scheduling of jobs.
         */
        private boolean enabled = true;

        /**
         * Defines the JobDetailsGenerator to use. This should be the fully qualified classname of the
         * JobDetailsGenerator, and it should have a default no-argument constructor.
         */
        private String jobDetailsGenerator = CachingJobDetailsGenerator.class.getName();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getJobDetailsGenerator() {
            return jobDetailsGenerator;
        }

        public void setJobDetailsGenerator(String jobDetailsGenerator) {
            this.jobDetailsGenerator = jobDetailsGenerator;
        }
    }

    /**
     * JobRunr BackgroundJobServer related settings
     */
    public static class BackgroundJobServer {

        /**
         * Enables the background processing of jobs.
         */
        private boolean enabled = false;

        /**
         * Sets the workerCount for the BackgroundJobServer which defines the maximum number of jobs that will be run in parallel.
         * By default, this will be determined by the amount of available processor.
         */
        private Integer workerCount;

        /**
         * Set the pollIntervalInSeconds for the BackgroundJobServer to see whether new jobs need to be processed
         */
        private Integer pollIntervalInSeconds = 15;

        /**
         * Sets the duration to wait before changing jobs that are in the SUCCEEDED state to the DELETED state. If a duration suffix
         * is not specified, hours will be used.
         */
        @DurationUnit(ChronoUnit.HOURS)
        private Duration deleteSucceededJobsAfter = Duration.ofHours(36);

        /**
         * Sets the duration to wait before permanently deleting jobs that are in the DELETED state. If a duration suffix
         * is not specified, hours will be used.
         */
        @DurationUnit(ChronoUnit.HOURS)
        private Duration permanentlyDeleteDeletedJobsAfter = Duration.ofHours(72);

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Integer getWorkerCount() {
            return workerCount;
        }

        public void setWorkerCount(Integer workerCount) {
            this.workerCount = workerCount;
        }

        public Integer getPollIntervalInSeconds() {
            return pollIntervalInSeconds;
        }

        public void setPollIntervalInSeconds(Integer pollIntervalInSeconds) {
            this.pollIntervalInSeconds = pollIntervalInSeconds;
        }

        public Duration getDeleteSucceededJobsAfter() {
            return deleteSucceededJobsAfter;
        }

        public void setDeleteSucceededJobsAfter(Duration deleteSucceededJobsAfter) {
            this.deleteSucceededJobsAfter = deleteSucceededJobsAfter;
        }

        public Duration getPermanentlyDeleteDeletedJobsAfter() {
            return permanentlyDeleteDeletedJobsAfter;
        }

        public void setPermanentlyDeleteDeletedJobsAfter(Duration permanentlyDeleteDeletedJobsAfter) {
            this.permanentlyDeleteDeletedJobsAfter = permanentlyDeleteDeletedJobsAfter;
        }
    }

    /**
     * JobRunr dashboard related settings
     */
    public static class Dashboard {

        /**
         * Enables the JobRunr dashboard.
         */
        private boolean enabled = false;

        /**
         * The port on which the Dashboard should run
         */
        private int port = 8000;

        /**
         * The username for the basic authentication which protects the dashboard
         */
        private String username = null;

        /**
         * The password for the basic authentication which protects the dashboard. WARNING: this is insecure as it is in clear text
         */
        private String password = null;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }


        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
