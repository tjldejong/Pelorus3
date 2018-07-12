package nl.soft.pelorus.pelorus3.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import nl.soft.pelorus.pelorus3.entity.Mark;

/**
 * Created by tobia on 12-9-2017.
 */

public interface MarkRepository {
    LiveData<List<Mark>> getAllMarks();

    LiveData<List<Mark>> getAllMarksEvent();

    LiveData<Mark> getMark(int number);
}
