package nl.soft.pelorus.pelorus3.api.MySQLDatabase;

import java.util.List;
import java.util.Map;

import nl.soft.pelorus.pelorus3.entity.Boat;
import nl.soft.pelorus.pelorus3.entity.Event;
import nl.soft.pelorus.pelorus3.entity.Location;
import nl.soft.pelorus.pelorus3.entity.Mark;
import nl.soft.pelorus.pelorus3.entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by tobia on 4-8-2017.
 */

public interface MySQLDatabaseClient {
    @GET("api/core.php/users/{user}")
    Call<User> getUser(@Path("user") String userId);

    @POST("api/core.php/users/")
    Call<Integer> createUser(@Body User user);

    @GET("api/core.php/events/*")
    Call<List<Event>> getAllEvents();

    @GET("api/core.php/boats/*")
    Call<List<Boat>> getAllBoats();

    @POST("api/core.php/boats/")
    Call<Integer> createBoat(@Body Boat boat);

    @POST("api/core.php/locations/")
    Call<Integer> createLocation(@Body Location location);

    @POST("tokensignin.php")
    Call<String>  sendToken(@Body String idToken);

    @GET("api/core.php/marks/")
    Call<List<Mark>> getAllMarks();

    @GET("api/core.php/marks/")
    Call<List<Mark>> getAllMarksEvent(@Query("eventid")int eventid);

    @GET("api/core.php/locations/")
    Call<List<Location>> getLastLocations(@QueryMap Map<String,String> options);
}
