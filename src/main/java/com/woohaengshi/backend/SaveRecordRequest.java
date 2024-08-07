package com.woohaengshi.backend;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SaveRecordRequest {

    private LocalDate date;
    private int time;
    private List<String> subjects;

    private SaveRecordRequest() {}
}
