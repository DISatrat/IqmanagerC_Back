package org.iqmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "order_extra")
@Getter
@Setter
@NoArgsConstructor
public class OrderedExtras {

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



    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "order_extra_service",
            joinColumns = @JoinColumn(name = "order_extra_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    @JsonIgnore
    private Set<RatesAndServices> ratesAndServices;


    public void addRatesAndServices(RatesAndServices ratesAndService) {
        ratesAndServices.add(ratesAndService);
    }

    public void deleteFromRatesAndServices(RatesAndServices ratesAndService) {
        ratesAndServices.remove(ratesAndService);
    }

    @Override
    public String toString() {
        return "OrderedExtras{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderedExtras that = (OrderedExtras) o;

        if (id != that.id) return false;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
