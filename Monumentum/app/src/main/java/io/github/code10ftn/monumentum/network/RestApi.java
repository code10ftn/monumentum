package io.github.code10ftn.monumentum.network;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Part;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import io.github.code10ftn.monumentum.model.Monument;
import io.github.code10ftn.monumentum.model.UserComment;
import io.github.code10ftn.monumentum.model.dto.AddCommentRequest;
import io.github.code10ftn.monumentum.model.dto.SettingsDto;
import io.github.code10ftn.monumentum.model.dto.SignInRequest;
import io.github.code10ftn.monumentum.model.dto.SignUpRequest;
import io.github.code10ftn.monumentum.model.dto.SigninResponse;
import io.github.code10ftn.monumentum.model.dto.UpdateFavoriteMonumentRequest;
import io.github.code10ftn.monumentum.model.dto.UpdateMonumentRatingRequest;
import io.github.code10ftn.monumentum.model.dto.UpdateVisitedMonumentRequest;
import io.github.code10ftn.monumentum.utils.ApiServiceConstants;

@Rest(rootUrl = ApiServiceConstants.ROOT_URL, converters = {GsonHttpMessageConverter.class,
        MappingJackson2HttpMessageConverter.class, FormHttpMessageConverter.class}, interceptors = AuthClientInterceptor.class)
public interface RestApi {

    @Post(value = ApiServiceConstants.SIGNUP_PATH)
    void signup(@Body SignUpRequest request);

    @Post(value = ApiServiceConstants.SIGNIN_PATH)
    SigninResponse signin(@Body SignInRequest request);

    @Post(value = ApiServiceConstants.MONUMENTS_PATH)
    void addMonument(@Part String name, @Part String description, @Part String location, @Part String longitude, @Part String latitude, @Part FileSystemResource img);

    @Get(value = ApiServiceConstants.MONUMENTS_PATH)
    List<Monument> getAllMonuments();

    @Get(value = ApiServiceConstants.FAVORITE_MONUMENTS_PATH)
    List<Monument> getFavoriteMonuments();

    @Get(value = ApiServiceConstants.VISITED_MONUMENTS_PATH)
    List<Monument> getVisitedMonuments();

    @Get(value = ApiServiceConstants.SINGLE_MONUMENT_PATH)
    Monument getMonument(@Path Long id);

    @Post(value = ApiServiceConstants.COMMENT_MONUMENT_PATH)
    UserComment addComment(@Body AddCommentRequest addCommentRequest);

    @Post(value = ApiServiceConstants.RATE_MONUMENT_PATH)
    void rateMonument(@Body UpdateMonumentRatingRequest updateMonumentRatingRequest);

    @Post(value = ApiServiceConstants.MARK_FAVORITE_MONUMENT_PATH)
    void markFavorite(@Body UpdateFavoriteMonumentRequest updateFavoriteMonumentRequest);

    @Post(value = ApiServiceConstants.MARK_VISITED_MONUMENT_PATH)
    void markVisited(@Body UpdateVisitedMonumentRequest updateVisitedMonumentRequest);

    @Post(value = ApiServiceConstants.SETTINGS_PATH)
    void updateSettings(@Body SettingsDto settingsDto);

    @Get(value = ApiServiceConstants.SETTINGS_PATH)
    SettingsDto getSettings();
}