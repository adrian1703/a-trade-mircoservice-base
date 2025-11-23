package a.trade.microservice.runtime_api;

public class Topics {

    private Topics() {}

    public enum Instances {
        STOCKAGGREGATE_ALL_1_MINUTE(ContentType.STOCKAGGREGATE, AggregateKind.ALL,
                TimeWindow.ONE, TimeUnit.MINUTE);

        private final ContentType   contentType;
        private final AggregateKind aggregateKind;
        private final TimeWindow    timeWindow;
        private final TimeUnit      timeUnit;

        Instances(ContentType contentType, AggregateKind aggregateKind,
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
            return contentType.value() + "_" + aggregateKind.value() + "_" + timeWindow.value() + "_" + timeUnit.value();
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

