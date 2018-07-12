package nl.soft.pelorus.pelorus3.repository;

import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nl.soft.pelorus.pelorus3.api.MySQLDatabase.MySQLDatabaseClient;
import nl.soft.pelorus.pelorus3.db.UserDatabase;
import nl.soft.pelorus.pelorus3.entity.Mark;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tobia on 12-9-2017.
 */

public class MarkRepositoryImpl implements MarkRepository {
    @Inject
    MySQLDatabaseClient mySQLDatabaseClient;

    @Inject
    UserDatabase userDatabase;

    @Inject
    SharedPreferences sharedPreferences;

    public MarkRepositoryImpl(UserDatabase userDatabase, MySQLDatabaseClient mySQLDatabaseClient, SharedPreferences sharedPreferences) {
        this.mySQLDatabaseClient = mySQLDatabaseClient;
        this.userDatabase = userDatabase;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public LiveData<List<Mark>> getAllMarks() {
        mySQLDatabaseClient.getAllMarks().enqueue(new Callback<List<Mark>>() {
            @Override
            public void onResponse(Call<List<Mark>> call, Response<List<Mark>> response) {
                for(Mark mark:response.body()) {
                    Completable.fromAction(() -> userDatabase.markDao().addMark(mark))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onComplete() {
                            Log.i("Mark insert local", "completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("Error", e.toString());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Mark>> call, Throwable t) {

            }
        });

        return userDatabase.markDao().getAllMarks(sharedPreferences.getInt("eventId",0));
    }

    @Override
    public LiveData<List<Mark>> getAllMarksEvent() {
        mySQLDatabaseClient.getAllMarksEvent(sharedPreferences.getInt("eventId",0)).enqueue(new Callback<List<Mark>>() {
            @Override
            public void onResponse(Call<List<Mark>> call, Response<List<Mark>> response) {

                Completable.fromAction(() -> userDatabase.markDao().nukeTable())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onComplete() {
                                Log.i("Marks", "deleted");
                                for(Mark mark:response.body()) {
                                    Completable.fromAction(() -> userDatabase.markDao().addMark(mark))
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new CompletableObserver() {
                                                @Override
                                                public void onSubscribe(Disposable d) {
                                                }

                                                @Override
                                                public void onComplete() {
                                                    Log.i("Mark insert local", "completed");
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Log.i("Error", e.toString());
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("Error", e.toString());
                            }
                        });


            }

            @Override
            public void onFailure(Call<List<Mark>> call, Throwable t) {

            }
        });

        return userDatabase.markDao().getAllMarks(sharedPreferences.getInt("eventId",0));
    }

    public LiveData<Mark> getMark(int number) {
        if (userDatabase.markDao().getMark(sharedPreferences.getInt("eventId",0), number) == null) {
            getAllMarksEvent();
        }
        return userDatabase.markDao().getMark(sharedPreferences.getInt("eventId",0),number);
    }
}
