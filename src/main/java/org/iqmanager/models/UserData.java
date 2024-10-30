package org.iqmanager.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.iqmanager.dto.PostDTO;


import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/** Модель данных о пользователе */
@NamedEntityGraph(
        name = "user-data-favorite-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("favorites")
        }
)
@NamedEntityGraph(
        name = "user-data-basket-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("orderElements"),
        }
)
@Entity
@Table(name = "user_data")
@Getter
@Setter
@NoArgsConstructor
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Имя */
    @Column(name = "name")
    private String name;

    /** Фамилия */
    @Column(name = "last_name")
    private String lastName;

    /** Email */
    @Column(name ="email")
    private String email;

    /** Ссылка на соцсеть */
    @Column(name ="web")
    private String web;

    /** Страна */
    @Column(name = "country")
    private String country;

    @Column(name = "currency")
    private String currency;

    /** Регион */
    @Column(name = "region")
    private String region;

    /** Город */
    @Column(name = "city")
    private String city;

    @Column(name = "agent")
    private boolean isAgent;

    @Column(name = "blocked")
    private boolean blocked;

    /** Данные для входа */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn (name="user_login_id", referencedColumnName = "id")
    @JsonIgnore
    private UserLoginData userLoginData;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},fetch = FetchType.LAZY)
    @JoinTable(name = "purchased_phone",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "post_id"))
    @JsonIgnore
    private Set<Post> purchasedPerformerNumbers;


    /** Избранное */
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "favorites",
                joinColumns = @JoinColumn(name = "id_user_data"),
                inverseJoinColumns = @JoinColumn(name = "id_post"))
    private List<Post> favorites;

    /** Корзина */
    @OneToMany(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "basket",
                joinColumns = @JoinColumn(name = "id_user_data"),
                inverseJoinColumns = @JoinColumn(name = "id_order_element"))
    private List<OrderElement> orderElements;


    public void addPurchasedPerformerNumbers(Post post) {
        purchasedPerformerNumbers.add(post);
    }


    public UserData(String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + email + '\'' +
                ", web='" + web + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return id == userData.id && Objects.equals(name, userData.name) && Objects.equals(lastName, userData.lastName) && Objects.equals(email, userData.email) && Objects.equals(web, userData.web);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, email, web);
    }

    public void addToFavorites(Post post) {
        if(!favorites.contains(post))
            this.favorites.add(post);
    }

    public void addToBasket(OrderElement orderElement) {
        if(!orderElements.contains(orderElement)) {
            this.orderElements.add(orderElement);
        } else {
            orderElements.remove(orderElement);
            orderElements.add(orderElement);
        }
    }

    public void deleteFromFavorites(Post post) {
        favorites = favorites.stream().filter(x -> x.getId() != post.getId()).collect(Collectors.toList());
    }

    public void deleteFromBasket(OrderElement orderElement) {
        this.orderElements.remove(orderElement);
    }
}
