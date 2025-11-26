package a.trade.microservice.runtime_api;

public class Topics {

    private Topics() {}

    /**
     * <p>
     * Represents predefined instances of various data aggregation and topic
     * configurations.
     * Each {@code Instance} defines specific combinations of {@link ContentType},
     * {@link AggregateKind}, {@link TimeWindow}, and {@link TimeUnit}, intended to
     * simplify
     * topic generation and data handling logic.
     * </p>
     */
    public enum Instance {
        STOCKAGGREGATE_ALL_1_MINUTE(ContentType.STOCKAGGREGATE, AggregateKind.ALL,
                TimeWindow.ONE, TimeUnit.MINUTE),
        STOCKAGGREGATE_SINGLE_1_MINUTE(ContentType.STOCKAGGREGATE, AggregateKind.SINGLE
                , TimeWindow.ONE, TimeUnit.MINUTE),
        ;

        public final ContentType   contentType;
        public final AggregateKind aggregateKind;
        public final TimeWindow    timeWindow;
        public final TimeUnit      timeUnit;

        Instance(ContentType contentType, AggregateKind aggregateKind,
                 TimeWindow timeWindow, TimeUnit timeUnit) {
            this.contentType   = contentType;
            this.aggregateKind = aggregateKind;
            this.timeWindow    = timeWindow;
            this.timeUnit      = timeUnit;
        }

        /**
         * Generates the topic name based on the instance's {@link ContentType},
         * {@link AggregateKind},
         * {@link TimeWindow}, and {@link TimeUnit}.
         * If you need the base topic name for {@link AggregateKind#SINGLE} use
         * {@link Instance#rootName}
         *
         * @return A {@link String} representing the constructed topic name.
         * @throws IllegalArgumentException If the {@link AggregateKind} is
         *                                  {@link AggregateKind#SINGLE}.
         */
        public String topicName() {
            if (aggregateKind == AggregateKind.SINGLE) {
                throw new IllegalArgumentException("aggregateKind cannot be SINGLE for "
                                                   + "topicName() use topicNameFor() " + "instead");
            }
            return rootName();
        }


        /**
         * Constructs a root name based on the instance's {@link ContentType},
         * {@link AggregateKind}, {@link TimeWindow}, and {@link TimeUnit}.
         *
         * @return A {@link String} representing the root name of the aggregate based
         * on the instance's properties.
         */
        public String rootName() {
            return contentType.value() + "_" + aggregateKind.value() + "_" + timeWindow.value() + "_" + timeUnit.value();
        }

        /**
         * Constructs a unique topic name for the specified ticker based on the current
         * instance's
         * {@link ContentType}, {@link AggregateKind}, {@link TimeWindow}, and
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
            if (ticker == null) {
                throw new IllegalArgumentException("ticker cannot be null");
            }
            if (aggregateKind == AggregateKind.ALL) {
                throw new IllegalArgumentException("aggregateKind cannot be ALL for " + "topicNameFor");
            }
            return rootName() + "_" + ticker.toLowerCase();
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

    public enum TimeWindow {
        ONE(1), TWO(2), FIVE(5), TEN(10), THIRTY(30),
        ;
        private final int value;

        TimeWindow(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    public enum TimeUnit {
        MINUTE("min"), HOUR("hour"), DAY("day"), WEEK("week"), MONTH("month"),
        ;
        private final String value;

        TimeUnit(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}

