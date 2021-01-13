package ru.klavogonki.kgparser.jsonParser.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.http.UrlConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Player")
public class PlayerEntity implements Serializable {

    public static final int NOT_BLOCKED = 0;

    @Id
//    @GeneratedValue
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pl_SEQ")
    @SequenceGenerator(name = "pl_SEQ", sequenceName = "pl_SEQ", allocationSize = 1000)
    private Long dbId;

    private LocalDateTime importDate; // when PlayerDataDownloader has been executed

    private String getSummaryError; // we import all users, including non-existing

    private String getIndexDataError; // we import all users, including non-existing and users with failed /get-index-data

    private String getStatsOverviewError; // we import all users, including non-existing and users with failed /get-stats-overview

    // fields from PlayerSummary
    @NaturalId
    private Integer playerId; // KG user id

    private String login;

    // this is a solution from https://stackoverflow.com/a/12912377/8534088
    // todo: also think about https://stackoverflow.com/a/57863734/8534088
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "car_id")),
        @AttributeOverride(name = "color", column = @Column(name = "car_color"))
    })
    private CarEntity car;

    // don't care about isOnline

    private Integer rankLevel;

    private String title;

    private Integer blocked;

    // fields from PlayerIndexData
    // don't care for PlayerIndexData.bio for now
    // todo: add bio data if ever required

    // PlayerIndexData.stats

    // see https://www.baeldung.com/jpa-java-time
    private LocalDateTime registered;

    private Integer achievementsCount;

    private Integer totalRacesCount;

    private Integer bestSpeed;

    private Integer ratingLevel;

    private Integer friendsCount;

    private Integer vocabulariesCount;

    private Integer carsCount;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "player_id") // do not create a join table! // todo: it's hard for non-pk column, does not work easily. Investigate this
    List<PlayerVocabularyStatsEntity> stats;

    @Transient
    public String getProfileLink() {
        return UrlConstructor.userProfileLink(playerId);
    }

    @Transient
    public String getRank() {
        return Rank.getRank(rankLevel).name();
    }

    @Transient
    public String getRankDisplayName() {
        return Rank.getDisplayName(rankLevel);
    }
}
