package com.chatbar.domain.user.domain;

import com.chatbar.domain.common.BaseEntity;
import com.chatbar.domain.common.Category;
import com.chatbar.domain.common.CategorySetConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.EnumSet;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "status = 'ACTIVE'")
@Table(name = "users")
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;

    @Email
    @NotNull
    private String email;

    @JsonIgnore
    @NotNull
    private String password;

    @Transient
    private String plainPassword;


    @Column(name = "profile_img")
    private String profileImg;

    @Convert(converter = CategorySetConverter.class)
    private EnumSet<Category> categories = EnumSet.noneOf(Category.class);

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId;

    @Builder
    public User(Long id, String nickname, String email, String password, String profileImg, EnumSet<Category> categories, Role role, Provider provider, String providerId){
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileImg = profileImg;
        this.categories = categories;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updateNickname(String nickname){this.nickname = nickname;}

    public void updatePassword(String password){this.password = password;}

    public void updateProfileImg(String profileImg){this.profileImg = profileImg;}

    public void updateCategories(EnumSet<Category> newCategories){this.categories = newCategories;}

    public void setPlainPassword(String newPassword) {
        this.plainPassword = newPassword;
    }
}
