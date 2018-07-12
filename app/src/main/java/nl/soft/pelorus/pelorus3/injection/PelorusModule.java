package nl.soft.pelorus.pelorus3.injection;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import nl.soft.pelorus.pelorus3.PelorusApplication;
import nl.soft.pelorus.pelorus3.R;
import nl.soft.pelorus.pelorus3.api.MySQLDatabase.MySQLDatabaseClient;
import nl.soft.pelorus.pelorus3.db.UserDatabase;
import nl.soft.pelorus.pelorus3.api.Google.GoogleApi;
import nl.soft.pelorus.pelorus3.api.Google.GoogleApiImpl;
import nl.soft.pelorus.pelorus3.repository.BoatRepository;
import nl.soft.pelorus.pelorus3.repository.BoatRepositoryImpl;
import nl.soft.pelorus.pelorus3.repository.EventRepository;
import nl.soft.pelorus.pelorus3.repository.EventRepositoryImpl;
import nl.soft.pelorus.pelorus3.repository.LocationRepository;
import nl.soft.pelorus.pelorus3.repository.LocationRepositoryImpl;
import nl.soft.pelorus.pelorus3.repository.MarkRepository;
import nl.soft.pelorus.pelorus3.repository.MarkRepositoryImpl;
import nl.soft.pelorus.pelorus3.repository.UserRepository;
import nl.soft.pelorus.pelorus3.repository.UserRepositoryImpl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tobia on 3-8-2017.
 */
@Module
public class PelorusModule {

    private PelorusApplication pelorusApplication;

    public PelorusModule(PelorusApplication pelorusApplication){
        this.pelorusApplication = pelorusApplication;
    }

    @Provides
    Context apllicationContext() {return pelorusApplication;}

    @Provides
    @Singleton
    UserRepository providesUserRepository(UserDatabase userDatabase, GoogleApi googleApi, MySQLDatabaseClient mySQLDatabaseClient,SharedPreferences sharedPreferences){
        return new UserRepositoryImpl(userDatabase,googleApi,mySQLDatabaseClient,sharedPreferences);
    }

    @Provides
    @Singleton
    EventRepository providesEventRepository(UserDatabase userDatabase, MySQLDatabaseClient mySQLDatabaseClient, SharedPreferences sharedPreferences){
        return new EventRepositoryImpl(userDatabase,mySQLDatabaseClient,sharedPreferences);
    }

    @Provides
    @Singleton
    BoatRepository providesBoatRepository(UserDatabase userDatabase, MySQLDatabaseClient mySQLDatabaseClient, SharedPreferences sharedPreferences){
        return new BoatRepositoryImpl(userDatabase,mySQLDatabaseClient,sharedPreferences);
    }

    @Provides
    @Singleton
    LocationRepository providesLocationRepository(GoogleApi googleApi, UserDatabase userDatabase,SharedPreferences sharedPreferences, MySQLDatabaseClient mySQLDatabaseClient){
        return new LocationRepositoryImpl(googleApi,userDatabase,sharedPreferences,mySQLDatabaseClient);
    }

    @Provides
    @Singleton
    MarkRepository providesMarkRepository(UserDatabase userDatabase, MySQLDatabaseClient mySQLDatabaseClient, SharedPreferences sharedPreferences){
        return new MarkRepositoryImpl(userDatabase,mySQLDatabaseClient,sharedPreferences);
    }

    @Provides
    @Singleton
    GoogleApi providesGoogleApi(GoogleApiClient googleApiClient){
        return new GoogleApiImpl(googleApiClient);
    }

    @Provides
    @Singleton
    UserDatabase provideUserDatabase(Context context){
        RoomDatabase.Builder<UserDatabase> builder = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class,"user_db").fallbackToDestructiveMigration();
        return builder.build();
    }

    @Provides
    @Singleton
    GoogleApiClient provideGoogleApiClient(Context context){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("139978490047-0cvsbod7fmqh497irnjb7dl7ibjhq08t.apps.googleusercontent.com")
                .requestEmail()
                .build();

        return new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(LocationServices.API)
                .build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor){
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Provides
    @Singleton
    MySQLDatabaseClient provideMySQLDatabase(OkHttpClient client){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://download.soft.nl/pelorus/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MySQLDatabaseClient.class);
    }

    @Provides
    @Singleton
    SharedPreferences sharedPreferences(Context context){
        return context.getSharedPreferences("MyPrefFile",0);
    }



}
