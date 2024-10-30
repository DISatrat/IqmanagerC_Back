package org.iqmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/** Модель данных доп. услуг */

@Table(name = "extra")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Extra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "type")
    private String type;

    /** Короткое описание */
    @Column(name = "title")
    @Lob
    private String title;

    @OneToMany(mappedBy = "extra", cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    private Set<RatesAndServices> ratesAndServices;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    @Override
    public String toString() {
        return "Extra{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Extra extra = (Extra) o;

        if (id != extra.id) return false;
        if (!Objects.equals(type, extra.type)) return false;
        return Objects.equals(title, extra.title);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}