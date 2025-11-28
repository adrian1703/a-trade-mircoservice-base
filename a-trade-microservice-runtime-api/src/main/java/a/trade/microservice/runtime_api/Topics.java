package a.trade.microservice.runtime_api;

import java.time.Duration;

public class Topics {

    private Topics() {}

    /**
     * <p>
     * Represents predefined instances of various data aggregation and topic
     * configurations.
     * Each {@code Instance} defines specific combinations of {@link ContentType},
     * {@link AggregateKind}, {@link TimeWindowSize}, and {@link TimeUnit}, intended to
     * simplify
     * topic generation and data handling logic.
     * </p>
     */
    public enum Instance {
        STOCKAGGREGATE_ALL_1_MINUTE(ContentType.STOCKAGGREGATE, AggregateKind.ALL,
                TimeWindowSize.ONE, TimeUnit.MINUTE),
        STOCKAGGREGATE_SINGLE_1_MINUTE(ContentType.STOCKAGGREGATE, AggregateKind.SINGLE
                , TimeWindowSize.ONE, TimeUnit.MINUTE),
        ;

        public final ContentType    contentType;
        public final AggregateKind  aggregateKind;
        public final TimeWindowSize timeWindowSize;
        public final TimeUnit       timeUnit;

        Instance(ContentType contentType, AggregateKind aggregateKind,
                 TimeWindowSize timeWindowSize, TimeUnit timeUnit) {
            this.contentType    = contentType;
            this.aggregateKind  = aggregateKind;
            this.timeWindowSize = timeWindowSize;
            this.timeUnit       = timeUnit;
        }


        /**
         * Computes the duration of the instance's time window in milliseconds.
         *
         * <p>
         * It multiplies the {@link TimeWindowSize} value by the duration of the
         * associated
         * {@link TimeUnit}, then converts the result to milliseconds.
         * </p>
         *
         * @return The duration of the time window, in milliseconds.
         */
        public long getDurationInMillis() {
            return timeUnit.getDuration()
                           .multipliedBy(timeWindowSize.value)
                           .toMillis();
        }

        /**
         * Constructs a root name based on the instance's {@link ContentType},
         * {@link AggregateKind}, {@link TimeWindowSize}, and {@link TimeUnit}.
         *
         * @return A {@link String} representing the root name of the aggregate based
         * on the instance's properties.
         */
        public String rootName() {
            return contentType.value() + "_" + aggregateKind.value() + "_" + timeWindowSize.value() + "_" + timeUnit.value();
        }

        /**
         * Constructs a unique topic name for the specified ticker based on the current
         * instance's
         * {@link ContentType}, {@link AggregateKind}, {@link TimeWindowSize}, and
         * {@link TimeUnit}.
         *
         * @param ticker The ticker symbol for which the topic name is to be generated.
         *               Must not be {@code null}.
         * @return A formatted string representing the topic name for identification.
         * @throws IllegalArgumentException If {@code ticker} is {@code null}.
         * @throws IllegalArgumentException If the instance's {@link AggregateKind} is
         *                                  {@link AggregateKind#ALL}.
         */
        public String topicNameFor(String ticker) {
            return switch (aggregateKind) {
                case ALL -> rootName();
                case SINGLE -> rootName() + "_" + ticker.toUpperCase();
            };
        }
    }

    public enum ContentType {
        STOCKAGGREGATE("stockaggregate");
        private final String value;

        ContentType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    public enum AggregateKind {
        ALL("all"), SINGLE("single");

        private final String value;

        AggregateKind(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    public enum TimeWindowSize {
        ONE(1), TWO(2), FIVE(5), TEN(10), THIRTY(30),
        ;
        private final int value;

        TimeWindowSize(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    public enum TimeUnit {
        MINUTE("min", Duration.ofMinutes(1)), HOUR("hour", Duration.ofHours(1)), DAY(
                "day", Duration.ofDays(1)), WEEK("week", Duration.ofDays(7)),
        ;
        private final String   value;
        private final Duration duration;

        TimeUnit(String value, Duration duration) {
            this.value    = value;
            this.duration = duration;
        }

        public String value() {
            return value;
        }

        public Duration getDuration() {
            return duration;
        }
    }
}

