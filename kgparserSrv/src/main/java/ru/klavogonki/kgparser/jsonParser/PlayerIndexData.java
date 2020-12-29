package ru.klavogonki.kgparser.jsonParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Required data from <a href="http://klavogonki.ru/api/profile/get-index-data?userId=242585">get-index-data</a> API request.
 */
@Deprecated(forRemoval = true) // "Use GetIndexDataResponse generated from OAS instead"
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerIndexData { // todo: remove this

    public static final int OK_CORRECT_VALUE = 1;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bio {
        @JsonProperty("user_id")
        public Integer userId;

        @JsonProperty("old_text")
        public String oldText;

        @JsonProperty("old_text_removed")
        public String oldTextRemoved;

        public String text;
    }

    /**
     * Microtime PHP format.
     * @see <a href="https://stackoverflow.com/a/3656731/8534088">SO answer</a>
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Registered {
        public Long sec;
        public Long usec;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Stats {
        /**
         * Зарегистрирован.
         */
        public Registered registered;

        /**
         * Количество достижений.
         */
        @JsonProperty("achieves_cnt")
        public Integer achievementsCount;

        /**
         * Общий пробег (по всем словарям).
         */
        @JsonProperty("total_num_races")
        public Integer totalRacesCount;

        /**
         * Рекорд в обычном.
         */
        @JsonProperty("best_speed")
        public Integer bestSpeed;

        /**
         * Уровень.
         */
        @JsonProperty("rating_level")
        public Integer ratingLevel;

        /**
         * Количество друзей.
         */
        @JsonProperty("friends_cnt")
        public Integer friendsCount;

        /**
         * Количество используемых словарей.
         */
        @JsonProperty("vocs_cnt")
        public Integer vocabulariesCount;

        /**
         * Машин в гараже.
         */
        @JsonProperty("cars_cnt")
        public Integer carsCount;
    }

    /**
     * Non-empty error will indicate error.
     * Typically, it is {@link PlayerSummary#INVALID_USER_ID_ERROR "invalid user id"}.
     */
    public String err;

    /**
     * Should be {@link #OK_CORRECT_VALUE 1} for empty {@link #err}.
     */
    public Integer ok;

    public Bio bio;

    public Stats stats;

    // todo: achieves if ever required
}
