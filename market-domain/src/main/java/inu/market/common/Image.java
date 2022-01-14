package inu.market.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Image {

    private String imageUrl;

    public static Image createImage(String imageUrl){
        Image image = new Image(imageUrl);
        return image;
    }

}
